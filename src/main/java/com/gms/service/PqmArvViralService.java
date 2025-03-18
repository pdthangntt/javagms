package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvViralLoadEntity;
import com.gms.repository.PqmArvViralRepository;
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
public class PqmArvViralService extends BaseService implements IBaseService<PqmArvViralLoadEntity, Long> {

//    @Autowired
//    private PqmArvStageRepository stageRepository;
    @Autowired
    private PqmArvViralRepository viralRepository;
    @Autowired
    private PqmArvService arvService;
    
    public void resetDataByArvID( Set<Long> arvID) {
        viralRepository.resetDataByArvID(arvID);
    }

    @Override
    public List<PqmArvViralLoadEntity> findAll() {
        return viralRepository.findAll();
    }
    
     public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                viralRepository.deleteById(ID);
            }
        }
    }

    @Override
    public PqmArvViralLoadEntity findOne(Long ID) {
        Optional<PqmArvViralLoadEntity> optional = viralRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmArvViralLoadEntity save(PqmArvViralLoadEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());

            PqmArvEntity arv = arvService.findOne(model.getArvID());
            if (arv != null) {
                model.setInsuranceNo(arv.getInsuranceNo());
            }
            viralRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public PqmArvViralLoadEntity saveApi(PqmArvViralLoadEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);

            PqmArvEntity arv = arvService.findOne(model.getArvID());
            if (arv != null) {
                model.setInsuranceNo(arv.getInsuranceNo());
            }
            viralRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

//    public List<PqmArvViralLoadEntity> findByIDs(Set<Long> siteID) {
//        List<PqmArvViralLoadEntity> sites = pqmVisitRepository.findByIDs(siteID);
//        return sites == null || sites.isEmpty() ? null : sites;
//    }
    private boolean set(Long arvID) {
        if (arvID == null) {
            return false;
        }
        PqmArvEntity arv = arvService.findOne(arvID);
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvID(arvID);
        if (arv == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmArvViralLoadEntity stage = entity.get(0);
        arv.setTestTime(stage.getTestTime());
        arvService.saveApi(arv);
        return true;
    }

    public PqmArvViralLoadEntity findMaxByArvID(Long ID) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public PqmArvViralLoadEntity findByArvAndInsuranceNo(Long ID, String insu) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvAndInsuranceNo(ID, insu);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public PqmArvViralLoadEntity findByArvAndInsuranceNoAndTestTime(Long ID, String insu, String testTime) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvAndInsuranceNoAndTestTime(ID, insu, testTime);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmArvViralLoadEntity> findByArvAndTestTime(Long ID, String testTime) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvAndTestTime(ID, TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, testTime));
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvViralLoadEntity> findByArvID(Long ID) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvViralLoadEntity> findByArvIDs(Set<Long> IDs) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvIDs(IDs);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvViralLoadEntity> findByArvIDAndTestTime(Long ID, String registrationTime) {
        List<PqmArvViralLoadEntity> entity = viralRepository.findByArvIDAndTestTime(ID, TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, registrationTime));
        return entity == null || entity.isEmpty() ? null : entity;
    }

}
