package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.repository.PqmArvStageRepository;
import com.gms.repository.PqmArvVisitRepository;
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
public class PqmArvVisitService extends BaseService implements IBaseService<PqmArvVisitEntity, Long> {

//    @Autowired
//    private PqmArvStageRepository stageRepository;
    @Autowired
    private PqmArvVisitRepository visitRepository;
    @Autowired
    private PqmArvService arvService;

    public void resetDataByArvID( Set<Long> arvID) {
        visitRepository.resetDataByArvID(arvID);
    }

    @Override
    public List<PqmArvVisitEntity> findAll() {
        return visitRepository.findAll();
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                visitRepository.deleteById(ID);
            }
        }
    }

    @Override
    public PqmArvVisitEntity findOne(Long ID) {
        Optional<PqmArvVisitEntity> optional = visitRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmArvVisitEntity save(PqmArvVisitEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());
            visitRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmArvVisitEntity saveApi(PqmArvVisitEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            visitRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

//    public List<PqmArvVisitEntity> findByIDs(Set<Long> siteID) {
//        List<PqmArvVisitEntity> sites = pqmVisitRepository.findByIDs(siteID);
//        return sites == null || sites.isEmpty() ? null : sites;
//    }
    private boolean set(Long arvID) {
        if (arvID == null) {
            return false;
        }
        PqmArvEntity arv = arvService.findOne(arvID);
        List<PqmArvVisitEntity> entity = visitRepository.findByArvID(arvID);
        if (arv == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmArvVisitEntity stage = entity.get(0);
        arv.setExaminationTime(stage.getExaminationTime());
        arvService.saveApi(arv);
        return true;
    }

    public PqmArvVisitEntity findMaxByArvID(Long ID) {
        List<PqmArvVisitEntity> entity = visitRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmArvVisitEntity> findByArvID(Long ID) {
        List<PqmArvVisitEntity> entity = visitRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvVisitEntity> findByArvIDs(Set<Long> IDs) {
        List<PqmArvVisitEntity> entity = visitRepository.findByArvIDs(IDs);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvVisitEntity> findByArvIDAndExaminationTime(Long ID, String registrationTime) {
        List<PqmArvVisitEntity> entity = visitRepository.findByArvIDAndExaminationTime(ID, TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, registrationTime));
        return entity == null || entity.isEmpty() ? null : entity;
    }

}
