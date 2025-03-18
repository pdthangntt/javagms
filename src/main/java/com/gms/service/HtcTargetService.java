package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.components.annotation.interf.FieldValueUnique;
import com.gms.entity.db.HtcTargetEntity;
import com.gms.repository.HtcTargetRepository;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author NamAnh_HaUI
 */
@Service
public class HtcTargetService extends BaseService implements IBaseService<HtcTargetEntity, Long> {

    @Autowired
    private HtcTargetRepository htcTargetRepository;

    /**
     * Danh sách chỉ tiêu
     * @param siteID
     * @return
     */
    public List<HtcTargetEntity> findAll(Long siteID) {
        return htcTargetRepository.findAll(siteID, Sort.by(Sort.Direction.DESC, new String[]{"years"}));
    }

    /**
     * Xóa chỉ tiêu
     * @param ID
     */
    public void remove(Long ID) {
        htcTargetRepository.deleteById(ID);
    }

    /**
     * Tìm chỉ tiêu theo mã
     * @param ID
     * @return
     */
    @Override
    public HtcTargetEntity findOne(Long ID) {
        HtcTargetEntity entity = htcTargetRepository.findOne(ID);
        return entity;
    }

    /**
     * Kiểm tra năm đã có chỉ tiêu chưa
     * @param years
     * @param siteID
     * @return
     */
    public boolean existsByYearsAndSiteID(Long years, Long siteID) {
        return htcTargetRepository.existsByYearsAndSiteID(years, siteID);
    }
    
    public HtcTargetEntity findByYearsAndSiteID(Long years, Long siteID) {
        return htcTargetRepository.findByYearsAndSiteID(years, siteID);
    }

    /**
     * Thêm mới và cập nhật chỉ tiêu
     * @param entity
     * @return
     */
    @Override
    public HtcTargetEntity save(HtcTargetEntity entity) {
        Date currentDate = new Date();
        HtcTargetEntity model;
        if (entity.getID() == null || entity.getID() == 0) {
            model = new HtcTargetEntity();
            model.setCreateAT(currentDate);
            model.setCreatedBY(getCurrentUserID());
        } else {
            model = findOne(entity.getID());
        }
        try {
            model.set(entity);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(HtcTargetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.setUpdateAt(currentDate);
        model.setUpdatedBY(getCurrentUserID());
        model = htcTargetRepository.save(model);
        return model;
    }

    @Override
    public List<HtcTargetEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
