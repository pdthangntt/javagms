package com.gms.service;

import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepStageEntity;
import com.gms.repository.PqmPrepStageRepository;
import com.gms.repository.PqmPrepVisitRepository;
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
public class PqmPrepStageService extends BaseService implements IBaseService<PqmPrepStageEntity, Long> {

    @Autowired
    private PqmPrepVisitRepository pqmPrepVisitRepository;
    @Autowired
    private PqmPrepStageRepository pqmPrepStageRepository;
    @Autowired
    private PqmPrepService pqmPrepService;

    @Override
    public List<PqmPrepStageEntity> findAll() {
        return pqmPrepStageRepository.findAll();
    }

    @Override
    public PqmPrepStageEntity findOne(Long ID) {
        Optional<PqmPrepStageEntity> optional = pqmPrepStageRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmPrepStageEntity save(PqmPrepStageEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                try {
                    model.setCreatedBY(getCurrentUser().getUser().getID());
                } catch (Exception e) {
                    model.setCreatedBY(Long.valueOf(0));
                }

            }
            model.setUpdateAt(currentDate);
            try {
                model.setUpdatedBY(getCurrentUser().getUser().getID());
            } catch (Exception e) {
                model.setUpdatedBY(Long.valueOf(0));
            }

            pqmPrepStageRepository.save(model);

            setPrep(model.getPrepID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                pqmPrepStageRepository.deleteById(ID);
            }
        }
    }

    public List<PqmPrepStageEntity> findByPrepIDs(Set<Long> prepID) {
        List<PqmPrepStageEntity> sites = pqmPrepStageRepository.findByPrepIDs(prepID);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    public List<PqmPrepStageEntity> findByPrepIDAndStartTime(Long ID, String startTime) {
        List<PqmPrepStageEntity> sites = pqmPrepStageRepository.findByPrepIDAndStartTime(ID, startTime);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    private boolean setPrep(Long prepID) {
        PqmPrepEntity prep = pqmPrepService.findOne(prepID);
        List<PqmPrepStageEntity> entity = pqmPrepStageRepository.findMaxByPrepID(prepID);
        if (prep == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmPrepStageEntity visitLast = entity.get(0);
        PqmPrepStageEntity visitFirst = entity.get(entity.size() - 1);

        prep.setType(visitFirst.getType());
        prep.setStartTime(visitFirst.getStartTime());
        prep.setEndTime(visitLast.getEndTime());

        pqmPrepService.save(prep);

        return true;
    }

    public PqmPrepStageEntity findMaxByPrepID(Long ID) {
        List<PqmPrepStageEntity> entity = pqmPrepStageRepository.findMaxByPrepID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmPrepStageEntity> findByPrepID(Long ID) {
        List<PqmPrepStageEntity> entity = pqmPrepStageRepository.findMaxByPrepID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }

}
