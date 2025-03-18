package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.FeedbackEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.input.FeedbackSearch;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gms.repository.FeedbackRepository;

/**
 *
 * @author NamAnh_HaUI
 */

@Service
public class FeedbackService extends BaseService implements IBaseService<FeedbackEntity, Long> {

    @Autowired
    private FeedbackRepository feedBackRepository;
    
    /**
     * Liệt kê danh sách góp ý của KH
     * @param feedBackSearch
     * @return
     */
    public DataPage<FeedbackEntity> findAll(FeedbackSearch feedBackSearch) {
        DataPage<FeedbackEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(feedBackSearch.getPageIndex());
        dataPage.setMaxResult(feedBackSearch.getPageSize());
        feedBackSearch.setID(feedBackSearch.getID() == null || feedBackSearch.getID() < 0 ? 0 : feedBackSearch.getID());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"ID"});
        Pageable pageable = PageRequest.of(feedBackSearch.getPageIndex() - 1, feedBackSearch.getPageSize(), sortable);
        
        Page<FeedbackEntity> pages = feedBackRepository.findAll(
                feedBackSearch.getStatus(),
                feedBackSearch.isRemove(),
                pageable);
        
        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * Tìm góp ý của KH theo mã
     * @param ID
     * @return
     */
    @Override
    public FeedbackEntity findOne(Long ID) {
        return feedBackRepository.findOne(ID);
    }

    /**
     * Thêm & cập nhật góp ý của KH
     * @param entity
     * @return
     */
    @Override
    public FeedbackEntity save(FeedbackEntity entity) {
        Date currentDate = new Date();
        entity.setCreateAT(currentDate);
        entity.setCreatedBY(getCurrentUserID());
        entity.setUpdateAt(currentDate);
        entity.setUpdatedBY(getCurrentUserID());
        entity.setIsRemove(false);
        return feedBackRepository.save(entity);
    }
    
    /**
     * Xóa góp ý của KH
     * @param ID
     */
    public void remove(Long ID) {
        FeedbackEntity e = feedBackRepository.findOne(ID);
        if (e != null) {
            e.setIsRemove(true);
        }
        feedBackRepository.save(e);
    }

    @Override
    public List<FeedbackEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
