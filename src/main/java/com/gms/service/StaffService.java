package com.gms.service;

import com.gms.components.annotation.interf.FieldValueExists;
import com.gms.components.onesignal.Onesignal;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.AuthAssignmentEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.db.StaffNotificationEntity;
import com.gms.entity.input.StaffSearch;
import com.gms.repository.StaffNotificationRepository;
import com.gms.repository.StaffRepository;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class StaffService extends BaseService implements IBaseService<StaffEntity, Long>, FieldValueExists {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffNotificationRepository notificationRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private Onesignal onesignal;

    @Override
    public List<StaffEntity> findAll() {
        return (List<StaffEntity>) staffRepository.findAll();
    }

    @Override
    public StaffEntity findOne(Long ID) {
        Optional<StaffEntity> model = null;
        try {
            model = staffRepository.findById(ID);
            return model != null && model.isPresent() ? model.get() : null;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public StaffEntity save(StaffEntity entity) {
        try {
            Date currentDate = new Date();
            StaffEntity model = findOne(entity.getID());
            if (model == null) {
                model = new StaffEntity();
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUserID());
                model.setUsername(entity.getUsername());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            model.set(entity);
            return staffRepository.save(model);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Lưu quản trị viên và cấp quyền
     *
     * @auth vvThành
     * @param entity
     * @param roleIDs
     * @return
     */
    public StaffEntity save(StaffEntity entity, List<Long> roleIDs) {
        entity = save(entity);
        if (entity != null && roleIDs != null && !roleIDs.isEmpty()) {
            authService.removeAssignmentByUserID(entity.getID());
            AuthAssignmentEntity assignment = null;
            for (Long roleID : roleIDs) {
                assignment = new AuthAssignmentEntity();
                assignment.setRole(authService.findOneRole(roleID));
                assignment.setUser(entity);
                authService.saveAssignment(assignment);
            }
        }
        return entity;
    }

    /**
     * Danh sách quản trị viên, phân trang
     *
     * @auth vvThành
     * @param searchInput
     * @return
     */
    public DataPage<StaffEntity> find(StaffSearch searchInput) {
        DataPage<StaffEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());

        searchInput.setSiteID(searchInput.getSiteID() == null ? 0 : searchInput.getSiteID());

        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"id"});
        if (searchInput.getSort().equals(StaffSearch.ID_ASC)) {
            sortable = Sort.by(Sort.Direction.ASC, new String[]{"id"});
        }

        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);
        Page<StaffEntity> pages = staffRepository.find(searchInput.getSiteID(), searchInput.getEmail(), searchInput.isIsActive(), pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    /**
     * Lấy quản trị viên theo username
     *
     * @auth vvThành
     * @param username
     * @return
     */
    public StaffEntity findByUsername(String username) {
        return staffRepository.getByUsername(username);
    }

    /**
     * Kiểm tra tồn tại
     *
     * @param value
     * @param fieldName
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (value != null && !"".equals(value.toString())) {
            Optional<StaffEntity> optional = staffRepository.findById(Long.parseLong(value.toString()));
            return optional.isPresent();
        }
        return true;
    }

    /**
     * Danh sách nhân viên theo cơ sở
     *
     * @param siteID
     * @return
     */
    public List<StaffEntity> findBySite(Long siteID) {
        return staffRepository.findBySiteID(siteID);
    }

    /**
     * Danh sách nhân viên theo ids
     *
     * @param IDs
     * @return
     */
    public List<StaffEntity> findAllByIDs(Set<Long> IDs) {
        return staffRepository.findAllById(IDs);
    }

    /**
     * Gửi thông báo trên màn hình
     *
     * @param ids
     * @param title
     * @param content
     * @param url
     */
    public void sendMessage(Set<Long> ids, String title, String content, String url) {
        List<StaffEntity> staffs = findAllByIDs(ids);
        List<String> devices = new ArrayList<>();
        List<StaffNotificationEntity> logs = new ArrayList<>();
        StaffNotificationEntity notification;
        for (StaffEntity staff : staffs) {
            notification = new StaffNotificationEntity();
            notification.setStaffID(staff.getID());
            notification.setTime(new Date());
            notification.setRead(false);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setUrl(url);
            if (staff.getDevice() != null && !staff.getDevice().isEmpty()) {
                notification.setToken(staff.getDevice().get(0));
                devices.addAll(staff.getDevice());
            }
            logs.add(notification);
        }
        notificationRepository.saveAll(logs);
        if (devices.size() > 0) {
            onesignal.send(title, content, devices, url);
        }
    }

    /**
     *
     * @param id
     * @param title
     * @param content
     * @param url
     */
    public void sendMessage(Long id, String title, String content, String url) {
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        sendMessage(ids, title, content, url);
    }

    /**
     * @auth vvThanh
     * @param id1
     * @param id2
     * @param title
     * @param content
     * @param url
     */
    public void sendMessage(Long id1, Long id2, String title, String content, String url) {
        Set<Long> ids = new HashSet<>();
        ids.add(id1);
        ids.add(id2);
        sendMessage(ids, title, content, url);
    }

    /**
     *
     * @param email
     * @param title
     * @param content
     * @param url
     */
    public void sendMessage(String email, String title, String content, String url) {
        List<StaffEntity> staffs = staffRepository.getByEmail(email);
        if (staffs != null) {
            Set<Long> ids = new HashSet<>();
            for (StaffEntity staff : staffs) {
                ids.add(staff.getID());
            }
            sendMessage(ids, title, content, url);
        }
    }

    /**
     * @auth vvThành
     * @param email
     * @return
     */
    public List<StaffEntity> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    /**
     * @param token
     * @auth vvThành
     * @return
     */
    public StaffEntity findByToken(String token) {
        if (token == null || token.equals("")) {
            return null;
        }
        return staffRepository.findByToken(token);
    }

    /**
     * @param page
     * @param size
     * @auth vvThành
     * @param staffID
     * @return
     */
    public DataPage<StaffNotificationEntity> getNotifications(Long staffID, int page, int size) {
        DataPage<StaffNotificationEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(page);
        dataPage.setMaxResult(size);
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"time"});
        Pageable pageable = PageRequest.of(dataPage.getCurrentPage() - 1, dataPage.getMaxResult(), sortable);
        Page<StaffNotificationEntity> pages = notificationRepository.find(staffID, pageable);
        List<StaffNotificationEntity> dataread = notificationRepository.findReadByStaffID(staffID);
        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords(dataread.size());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * @auth pdThang
     * @param staffID
     * @return
     */
    public List<StaffNotificationEntity> findAllByStaffID(Long staffID) {
        List<StaffNotificationEntity> data = notificationRepository.findReadByStaffID(staffID);
        return data;
    }

    public List<StaffNotificationEntity> saveAllNotify(List<StaffNotificationEntity> entities) {
        Iterable<StaffNotificationEntity> all = notificationRepository.saveAll(entities);
        List<StaffNotificationEntity> copy = ImmutableList.copyOf(all);
        return copy;
    }

    public StaffNotificationEntity findOneNotify(Long ID) {
        StaffNotificationEntity model = notificationRepository.findOne(ID);
        return model;
    }

    /**
     * @author pdThang
     * @param searchInput
     * @return
     */
    public DataPage<StaffEntity> findAllAdmin(StaffSearch searchInput) {
        DataPage<StaffEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        searchInput.setSiteID(searchInput.getSiteID() == null ? 0 : searchInput.getSiteID());

        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);
        Page<StaffEntity> pages = staffRepository.findAllAdmin(searchInput.getEmail(), searchInput.isIsActive(),
                searchInput.getID(),
                searchInput.getUserName(),
                searchInput.getName(),
                searchInput.getPhone(),
                searchInput.getSiteIDs(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    /**
     * Thông báo quá hạn
     *
     * @return
     */
    public List<StaffNotificationEntity> findNotificationsOlderThanAMonth() {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -15);
        return notificationRepository.findActivitiesOlderThanAMonth(cal.getTime(), Sort.by(Sort.Direction.ASC, new String[]{"time"}));
    }

    /**
     * Xoá thông báo
     *
     * @param models
     */
    public void deleteAllNotification(List<StaffNotificationEntity> models) {
        notificationRepository.deleteAll(models);
    }
}
