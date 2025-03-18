package com.gms.service;

import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepVisitEntity;
import com.gms.repository.PqmPrepVisitRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class PqmPrepVisitService extends BaseService implements IBaseService<PqmPrepVisitEntity, Long> {

    @Autowired
    private PqmPrepVisitRepository pqmPrepVisitRepository;
    @Autowired
    private PqmPrepService pqmPrepService;

    @Override
    public List<PqmPrepVisitEntity> findAll() {
        return pqmPrepVisitRepository.findAll();
    }

    @Override
    public PqmPrepVisitEntity findOne(Long ID) {
        Optional<PqmPrepVisitEntity> optional = pqmPrepVisitRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                pqmPrepVisitRepository.deleteById(ID);
            }
        }
    }

    @Override
    public PqmPrepVisitEntity save(PqmPrepVisitEntity model) {
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
            pqmPrepVisitRepository.save(model);

            setPrep(model.getPrepID());//set lượt khám mới nhất vào prep
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean setPrep(Long prepID) {
        PqmPrepEntity prep = pqmPrepService.findOne(prepID);
        List<PqmPrepVisitEntity> entity = pqmPrepVisitRepository.findMaxByPrepID(prepID);
        if (prep == null || entity == null || entity.isEmpty()) {
            return false;
        }
        PqmPrepVisitEntity visitLast = entity.get(0);

        prep.setTreatmentRegimen(visitLast.getTreatmentRegimen());
        prep.setDaysReceived(visitLast.getDaysReceived());
        prep.setAppointmentTime(visitLast.getAppointmentTime());
        prep.setExaminationTime(visitLast.getExaminationTime());

        pqmPrepService.save(prep);

        return true;
    }

    public List<PqmPrepVisitEntity> findByPrepIDs(Set<Long> prepID) {
        List<PqmPrepVisitEntity> visits = pqmPrepVisitRepository.findByPrepIDs(prepID);
        return visits == null || visits.isEmpty() ? null : visits;
    }

//    private boolean setPrep(Long prepID) {
//        PqmPrepEntity prep = pqmPrepService.findOne(prepID);
//        List<PqmPrepVisitEntity> entity = pqmPrepVisitRepository.findMaxByPrepID(prepID);
//        if (prep == null || entity == null || entity.isEmpty()) {
//            return false;
//        }
//        PqmPrepVisitEntity visit = entity.get(0);
//        prep.setExaminationTime(visit.getExaminationTime());
//        prep.setResultsID(visit.getResultsID());
//        prep.setTreatmentStatus(visit.getTreatmentStatus());
//        prep.setTreatmentRegimen(visit.getTreatmentRegimen());
//        prep.setDaysReceived(visit.getDaysReceived());
//        prep.setAppointmentTime(visit.getAppointmentTime());
//
//        pqmPrepService.save(prep);
//
//        return true;
//    }
    public PqmPrepVisitEntity findMaxByPrepID(Long ID) {
        List<PqmPrepVisitEntity> entity = pqmPrepVisitRepository.findMaxByPrepID(ID);
        return entity == null || entity.isEmpty() ? null : entity.get(0);
    }

    public List<PqmPrepVisitEntity> findByPrepIDAndExaminationTime(Long ID, String time) {
        List<PqmPrepVisitEntity> sites = pqmPrepVisitRepository.findByPrepIDAndExaminationTime(ID, time);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    public List<PqmPrepVisitEntity> findByPrepID(Long ID) {
        List<PqmPrepVisitEntity> entity = pqmPrepVisitRepository.findMaxByPrepID(ID);
        return entity == null || entity.isEmpty() ? null : entity;
    }
}
