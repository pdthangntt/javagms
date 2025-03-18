package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvTestEntity;
import com.gms.entity.db.PqmArvTestEntity;
import com.gms.repository.PqmArvTestRepository;
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
public class PqmArvTestService extends BaseService implements IBaseService<PqmArvTestEntity, Long> {

//    @Autowired
//    private PqmArvStageRepository stageRepository;
    @Autowired
    private PqmArvTestRepository testRepository;
    @Autowired
    private PqmArvService arvService;
    
    public void resetDataByArvID( Set<Long> arvID) {
        testRepository.resetDataByArvID(arvID);
    }
    

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                testRepository.deleteById(ID);
            }
        }
    }

    @Override
    public List<PqmArvTestEntity> findAll() {
        return testRepository.findAll();
    }

    @Override
    public PqmArvTestEntity findOne(Long ID) {
        Optional<PqmArvTestEntity> optional = testRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmArvTestEntity save(PqmArvTestEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());
            testRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public PqmArvTestEntity saveApi(PqmArvTestEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            testRepository.save(model);
            set(model.getArvID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

//    public List<PqmArvTestEntity> findByIDs(Set<Long> siteID) {
//        List<PqmArvTestEntity> sites = pqmVisitRepository.findByIDs(siteID);
//        return sites == null || sites.isEmpty() ? null : sites;
//    }
    private boolean set(Long arvID) {
        if (arvID == null) {
            return false;
        }
        PqmArvEntity arv = arvService.findOne(arvID);
        List<PqmArvTestEntity> entity = testRepository.findByArvID(arvID);
        if (arv == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmArvTestEntity stage = entity.get(0);
        arv.setInhFromTime(stage.getInhFromTime());
        arvService.saveApi(arv);
        return true;
    }

    public PqmArvTestEntity findMaxByArvID(Long ID) {
        List<PqmArvTestEntity> entity = testRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmArvTestEntity> findByArvID(Long ID) {
        List<PqmArvTestEntity> entity = testRepository.findByArvID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvTestEntity> findByArvIDs(Set<Long> IDs) {
        List<PqmArvTestEntity> entity = testRepository.findByArvIDs(IDs);
        return entity == null || entity.isEmpty() ? null : entity;
    }

    public List<PqmArvTestEntity> findByArvIDAndInhFromTime(Long ID, String inhFromTime) {
        List<PqmArvTestEntity> entity = testRepository.findByArvIDAndInhFromTime(ID, TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, inhFromTime));
        return entity == null || entity.isEmpty() ? null : entity;
    }

}
