package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.StaffActivityEntity;
import com.gms.entity.input.LogSearch;
import com.gms.repository.StaffActivityRepository;
import static com.gms.service.BaseService.FORMATDATE;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
public class StaffActivityService extends BaseService {

    @Autowired
    private StaffActivityRepository activityRepository;

    /**
     * Log action
     *
     * @auth vvThành
     * @param entity
     * @return
     */
    public StaffActivityEntity log(StaffActivityEntity entity) {
        try {
            Date currentDate = new Date();
            entity.setCreateAT(currentDate);
            return activityRepository.save(entity);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }

    }

    /**
     * Lấy log hơn 15 ngày
     *
     * @auth vvThành
     * @return
     */
    public List<StaffActivityEntity> findActivitiesOlderThanAMonth() {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        return activityRepository.findActivitiesOlderThanAMonth(cal.getTime(), Sort.by(Sort.Direction.ASC, new String[]{"createAT"}));
    }

    /**
     * Xoá các entity
     *
     * @auth vvThành
     * @param entitys
     */
    public void deleteAll(List<StaffActivityEntity> entitys) {
        activityRepository.deleteAll(entitys);
    }

    /**
     * Danh sách lịch sử hoạt động
     *
     * @param userID
     * @param page
     * @param size
     * @return
     */
    public DataPage<StaffActivityEntity> find(Long userID, int page, int size) {
        DataPage<StaffActivityEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(page);
        dataPage.setMaxResult(size);
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"createAT"});

        Pageable pageable = PageRequest.of(dataPage.getCurrentPage() - 1, dataPage.getMaxResult(), sortable);
        Page<StaffActivityEntity> pages = activityRepository.find(userID, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public DataPage<StaffActivityEntity> findAll(LogSearch search) {
        DataPage<StaffActivityEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"userID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<StaffActivityEntity> pages = activityRepository.findAll(search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                search.getRequestMethod(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }
}
