package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.repository.PqmArvStageRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pdThang
 */
@Service
public class PqmArvStageService extends BaseService implements IBaseService<PqmArvStageEntity, Long> {

    @Autowired
    private PqmArvStageRepository stageRepository;
    @Autowired
    private PqmArvService arvService;

    public void resetDataByArvID( Set<Long> arvID) {
        stageRepository.resetDataByArvID(arvID);
    }

    @Override
    public List<PqmArvStageEntity> findAll() {
        return stageRepository.findAll();
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                stageRepository.deleteById(ID);
            }
        }
    }

    @Override
    public PqmArvStageEntity findOne(Long ID) {
        Optional<PqmArvStageEntity> optional = stageRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmArvStageEntity save(PqmArvStageEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());
            stageRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmArvStageEntity saveApi(PqmArvStageEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            stageRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

//    public List<PqmArvStageEntity> findByIDs(Set<Long> siteID) {
//        List<PqmArvStageEntity> sites = pqmVisitRepository.findByIDs(siteID);
//        return sites == null || sites.isEmpty() ? null : sites;
//    }
    private boolean set(Long arvID) {
        if (arvID == null) {
            return false;
        }
        PqmArvEntity arv = arvService.findOne(arvID);
        List<PqmArvStageEntity> entity = stageRepository.findByArvID(arvID);
        if (arv == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmArvStageEntity stage = entity.get(0);

        arv.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
        arv.setTreatmentTime(stage.getTreatmentTime());
        arv.setRegistrationTime(stage.getRegistrationTime());
        arv.setEndTime(stage.getEndTime());
        arvService.saveApi(arv);
        return true;
    }

    public PqmArvStageEntity findMaxByArvID(Long ID) {
        List<PqmArvStageEntity> entity = stageRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmArvStageEntity> findByArvID(Long ID) {
        List<PqmArvStageEntity> entity = stageRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvStageEntity> findByArvIDAndRegistrationTime(Long ID, String registrationTime) {
        List<PqmArvStageEntity> entity = stageRepository.findByArvIDAndRegistrationTime(ID, TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, registrationTime));
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvStageEntity> findByArvIDOderByTreatmentTimeDesc(Long ID) {
        List<PqmArvStageEntity> entity = stageRepository.findByArvIDOderByTreatmentTimeDesc(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvStageEntity> findByArvIDs(Set<Long> IDs) {
        List<PqmArvStageEntity> entity = stageRepository.findByArvIDs(IDs);
        return entity == null || entity.isEmpty() ? null : entity;
    }
}
