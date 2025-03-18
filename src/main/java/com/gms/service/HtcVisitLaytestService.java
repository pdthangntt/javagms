package com.gms.service;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcVisitLaytestEntity;
import com.gms.repository.HtcVisitLaytestRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TrangBN
 */
@Service
public class HtcVisitLaytestService extends BaseService implements IBaseService<HtcVisitLaytestEntity, Long> {

    @Autowired
    private HtcVisitLaytestRepository htcVisitLaytestRepository;
    
    /**
     * Chuyển dữ liệu từ HtcLaytestEntity sang HtcVisitLaytestEntity
     * 
     * @auth TrangBN
     * @param user
     * @param laytestEntity
     * @return 
     */
    public HtcVisitLaytestEntity getHtcVisitLaytest(HtcLaytestEntity laytestEntity) {
        
        HtcVisitLaytestEntity visitLaytest = new HtcVisitLaytestEntity();
        visitLaytest.setAcceptDate(laytestEntity.getAcceptDate());
        visitLaytest.setAcceptStaffID(getCurrentUserID());
        visitLaytest.setSourceSiteID(laytestEntity.getSiteID());
        visitLaytest.setSourceStaffID(laytestEntity.getCreatedBY());
        visitLaytest.setSourceID(laytestEntity.getID());
        visitLaytest.setCreatedAt(new Date());
        visitLaytest.setCreatedBy(getCurrentUserID());
        visitLaytest.setUpdatedAt(new Date());
        visitLaytest.setUpdatedBy(getCurrentUserID());
        return visitLaytest;
    }

    @Override
    public List<HtcVisitLaytestEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HtcVisitLaytestEntity findOne(Long ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HtcVisitLaytestEntity save(HtcVisitLaytestEntity condition) {
        return htcVisitLaytestRepository.save(condition);
    }
    
}
