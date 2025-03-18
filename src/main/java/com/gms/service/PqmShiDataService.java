package com.gms.service;

import com.gms.entity.db.PqmShiDataEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gms.repository.PqmShiDataRepository;
import com.gms.repository.impl.PqmDrugEstimateImpl;

/**
 *
 * @author pdThang
 */
@Service
public class PqmShiDataService extends BaseService implements IBaseService<PqmShiDataEntity, Long> {

    @Autowired
    private PqmShiDataRepository pqmDrugDataRepository;
    @Autowired
    private PqmDrugEstimateImpl pqmDrugEstimateImpl;

    @Override
    public List<PqmShiDataEntity> findAll() {
        return pqmDrugDataRepository.findAll();
    }

    @Override
    public PqmShiDataEntity findOne(Long ID) {
        Optional<PqmShiDataEntity> optional = pqmDrugDataRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmShiDataEntity save(PqmShiDataEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            pqmDrugDataRepository.save(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PqmShiDataEntity findByUniqueConstraints(Long siteID,
            int month,
            int year,
            String provinceID
    ) {
        PqmShiDataEntity model = pqmDrugDataRepository.findByUniqueConstraints(siteID, month, year, provinceID);
        return model;
    }

    public List<PqmShiDataEntity> findData(
            int month,
            int year,
            String provinceID
    ) {
        List<PqmShiDataEntity> model = pqmDrugDataRepository.findData(
                month,
                year,
                provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

    public void resetDataProvince(
            int month,
            int year,
            String provinceID
    ) {
        pqmDrugDataRepository.resetDataProvince(
                month,
                year,
                provinceID);
    }

}
