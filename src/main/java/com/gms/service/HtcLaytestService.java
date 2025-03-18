package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.InitCodeEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcLaytestLogEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.input.LaytestSearch;
import com.gms.entity.input.LogSearch;
import com.gms.repository.DistrictRepository;
import com.gms.repository.HtcLaytestLogRepository;
import com.gms.repository.HtcLaytestRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import static com.gms.service.BaseService.FORMATDATE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author pdThang
 */
@Service
public class HtcLaytestService extends BaseService implements IBaseService<HtcLaytestEntity, Long> {

    @Autowired
    private HtcLaytestLogRepository logRepository;

    @Autowired
    private HtcLaytestRepository htcLaytestRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    @Override
    public List<HtcLaytestEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<HtcLaytestEntity> findAllByIDs(Set<Long> IDs) {
        return htcLaytestRepository.findAllByIDs(IDs);
    }

    @Override
    public HtcLaytestEntity findOne(Long ID) {
        Optional<HtcLaytestEntity> optional = htcLaytestRepository.findById(ID);
        HtcLaytestEntity entity = null;
        if (optional.isPresent()) {
            List<HtcLaytestEntity> models = new ArrayList<>();
            models.add(optional.get());
            entity = getAddress(models).get(0);
        }
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HtcLaytestEntity save(HtcLaytestEntity model) {

        try {
            HtcLaytestEntity originModel = model;
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                    model.setCreatedBY(getCurrentUserID());
                }
                model.setCode(model.getCode());
                model.setVisitRemove(false);
            }

            if (model.getID() != null) {
                // Get from database
                model = findOne(model.getID());
                model.set(originModel);
            }

            if (getCurrentUser() != null) {
                model.setProjectID(getCurrentUser().getSite().getProjectID());
            }
            if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                model.setUpdatedBY(getCurrentUserID());
            }

            model.setUpdateAt(currentDate);
            model.set(model);

            model.setServiceID(ServiceTestEnum.KC.getKey());
            model.setPatientName(TextUtils.toFullname(model.getPatientName()));
            model = htcLaytestRepository.save(model);
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author pdThang
     * @param searchInput
     * @return
     */
    public DataPage<HtcLaytestEntity> find(LaytestSearch searchInput) {
        DataPage<HtcLaytestEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        searchInput.setID(searchInput.getID() == null || searchInput.getID() < 0 ? 0 : searchInput.getID());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcLaytestEntity> pages = htcLaytestRepository.find(
                searchInput.getSiteID(),
                searchInput.getCode(),
                searchInput.getName(),
                searchInput.isRemove(),
                searchInput.getVisitRemove(),
                searchInput.getStaffID(),
                searchInput.getSiteVisitID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTime()),
                searchInput.getSendStatus(),
                searchInput.getStatus(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeTo()),
                searchInput.isSampleSentDate(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    /**
     * Lấy full thông tin địa chỉ
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<HtcLaytestEntity> getAddress(List<HtcLaytestEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<String> pIDs = new HashSet<>();
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentProvinceID")));
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentProvinceID")));
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        for (ProvinceEntity model : provinceRepository.findByIDs(pIDs)) {
            provinces.put(model.getID(), model);
        }

        Set<String> dIDs = new HashSet<>();
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentDistrictID")));
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentDistrictID")));
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity model : districtRepository.findByIDs(dIDs)) {
            districts.put(model.getID(), model);
        }

        Set<String> wIDs = new HashSet<>();
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentWardID")));
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentWardID")));
        Map<String, WardEntity> wards = new HashMap<>();
        for (WardEntity model : wardRepository.findByIDs(wIDs)) {
            wards.put(model.getID(), model);
        }

        for (HtcLaytestEntity entity : entities) {
            //Địa chỉ thường trú
            entity.setPermanentAddressFull(buildAddress(
                    entity.getPermanentAddress(),
                    entity.getPermanentAddressStreet(),
                    entity.getPermanentAddressGroup(),
                    provinces.get(entity.getPermanentProvinceID()),
                    districts.get(entity.getPermanentDistrictID()),
                    wards.get(entity.getPermanentWardID())
            ));
            //Địa chỉ hiện tại
            entity.setCurrentAddressFull(buildAddress(
                    entity.getCurrentAddress(),
                    entity.getCurrentAddressStreet(),
                    entity.getCurrentAddressGroup(),
                    provinces.get(entity.getCurrentProvinceID()),
                    districts.get(entity.getCurrentDistrictID()),
                    wards.get(entity.getCurrentWardID())
            ));
        }

        return entities;
    }

    /**
     * Validate existence for code and siteID in case add new customer
     *
     * @auth TrangBN
     * @param code
     * @param siteID
     * @return
     */
    public boolean existCodeAndSiteID(String code, Long siteID) {
        if (siteID == null || StringUtils.isEmpty(code)) {
            return false;
        }
        return htcLaytestRepository.existByCodeAndSiteID(siteID, code.trim());
    }

    /**
     * Log các hành động của HTC theo khách hàng
     *
     * @auth TrangBN
     * @param laytestID
     * @param content
     * @return
     */
    public HtcLaytestLogEntity log(Long laytestID, String content) {
        HtcLaytestLogEntity model = new HtcLaytestLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setCreateAt(new Date());
        model.setHtcLaytestID(laytestID);
        model.setContent(content);
        return logRepository.save(model);
    }

    /**
     * Danh sách log theo bệnh nhân
     *
     * @auth DSNAnh
     * @param oID
     * @return
     */
    public List<HtcLaytestLogEntity> getLogs(Long oID) {
        return logRepository.findByLaytestID(oID);
    }

    public void remove(HtcLaytestEntity entity) {
        if (entity != null) {
            entity.setRemove(true);
            htcLaytestRepository.save(entity);
        }
    }

    /**
     * Xóa khách hàng chưa tiếp nhận bên HTC
     *
     * @author pdThang
     * @param entity
     */
    public void removeVisitLaytest(HtcLaytestEntity entity) {
        if (entity != null) {
            entity.setVisitRemove(true);
            htcLaytestRepository.save(entity);
        }
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author pdThang
     * @param ID
     */
    public void delete(Long ID) {
        HtcLaytestEntity e = findOne(ID);
        htcLaytestRepository.delete(e);
    }

    /**
     * Lấy mã khách hàng theo người tạo
     *
     * @param code
     * @param staffCode
     * @auth TrangBN
     * @return
     */
    public String getCode(String code) {

        HtcLaytestEntity laytest = htcLaytestRepository.findLastBysiteIDCreateBy(getCurrentUser().getSite().getID(), getCurrentUserID(), code);
        if (laytest == null || !laytest.getCode().contains("-") || !StringUtils.containsIgnoreCase(laytest.getCode(), code)) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING_LAYTEST.getKey()).toUpperCase();
        }

        try {
            String[] split = laytest.getCode().split("-", -1);
            Long identity = Long.valueOf(split[(split.length - 1)]);
            if (identity.toString().length() < 6) {
                String leftPad = StringUtils.leftPad(String.valueOf((identity + 1)), 6, "0");
                return String.format("%s-%s", code, leftPad).toUpperCase();
            } else {
                return String.format("%s-%s", code, String.valueOf((identity + 1))).toUpperCase();
            }
        } catch (Exception e) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING_LAYTEST.getKey()).toUpperCase();
        }
    }

    /**
     * Khôi phục khách hàng
     *
     * @author pdThang
     * @param entity
     */
    public void restoreLaytest(HtcLaytestEntity entity) {
        if (entity != null) {
            entity.setRemove(false);
            htcLaytestRepository.save(entity);
        }
    }

    /**
     * Tìm danh sách khách hàng xuất sổ tvxn
     *
     * @param startDate
     * @param endDate
     * @param siteID
     * @param staffID
     * @param objects
     * @return
     */
    public List<HtcLaytestEntity> findLaytestBook(Date startDate, Date endDate, Set<Long> siteID, Long staffID, List<String> objects) {
        List<HtcLaytestEntity> models = htcLaytestRepository.findLaytestBook(startDate, endDate, siteID, staffID, objects);
        return getAddress(models);
    }
    //30/10/2019

    /**
     * DS tư vấn trước XN
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    public List<HtcLaytestEntity> findSoNguoiTuVanTruocXN(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findSoNguoiTuVanTruocXN(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID);
        return getAddress(models);
    }

    /**
     * DS được xét nghiệm (dương tính)
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    public List<HtcLaytestEntity> findSoNguoiDuocXNDuongTinh(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findSoNguoiDuocXNDuongTinh(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID);
        return getAddress(models);
    }

    /**
     * Danh sách KH XN được giới thiệu bởi bạn tình bạn chích
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    public List<HtcLaytestEntity> findSoNguoiGioiThieuBanChich(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findSoNguoiGioiThieuBanChich(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID);
        return getAddress(models);
    }

    /**
     * Danh sach chuyển gửi điều trị của từng nhóm đối tượng
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    public List<HtcLaytestEntity> findSoNguoiChuyenGuiDieuTri(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findSoNguoiChuyenGuiDieuTri(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID);
        return getAddress(models);
    }

    public List<HtcLaytestEntity> findUndefined(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID, Set<String> genderID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findUndefined(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID, genderID);
        return getAddress(models);
    }

    public List<HtcLaytestEntity> findUnderOne(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID, Set<String> genderID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findUnderOne(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID, genderID);
        return getAddress(models);
    }

    public List<HtcLaytestEntity> findAgeRange(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID, Set<String> genderID, int minAge, int maxAge) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findAgeRange(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID, genderID, minAge, maxAge);
        return getAddress(models);
    }

    public List<HtcLaytestEntity> findAgeRangeSum(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID, int minAge, int maxAge) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findAgeRangeSum(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID, minAge, maxAge);
        return getAddress(models);
    }

    /**
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    public List<HtcLaytestEntity> findTable02MER(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID, Set<String> confirmResultsID, Set<String> genderID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findTable02MER(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID, confirmResultsID, genderID);
        return getAddress(models);
    }

    /**
     * Hiển thị DS theo các nhóm đối tượng còn lại (nam or nữ)
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param confirmResultsID
     * @param genderID
     * @return
     */
    public List<HtcLaytestEntity> findTable02MEROther(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID, Set<String> genderID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findTable02MEROther(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID, genderID);
        return getAddress(models);
    }

    /**
     * Hiển thị DS theo các nhóm đối tượng còn lại (tổng)
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param confirmResultsID
     * @return
     */
    public List<HtcLaytestEntity> findTable02MEROtherSum(Set<Long> siteID, Long staffID, String start, String end, Set<String> confirmResultsID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findTable02MEROtherSum(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                confirmResultsID);
        return getAddress(models);
    }

    public List<HtcLaytestEntity> findTable02MERSum(Set<Long> siteID, Long staffID, String start, String end, Set<String> objectGroupID, Set<String> confirmResultsID) {
        List<HtcLaytestEntity> models = null;
        models = htcLaytestRepository.findTable02MERSum(siteID, staffID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                objectGroupID, confirmResultsID);
        return getAddress(models);
    }

    public DataPage<HtcLaytestLogEntity> findAllLog(LogSearch search) {
        DataPage<HtcLaytestLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"htcLaytestID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<HtcLaytestLogEntity> pages = logRepository.findAll(search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }
}
