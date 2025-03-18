package com.gms.service;

import com.gms.entity.db.PacPatientRequestEntity;
import com.gms.repository.PacPatientRequestRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class PacPatientRequestService extends BaseService {
    
    @Autowired
    private PacPatientRequestRepository pacPatientRequestRepository;
    
    public PacPatientRequestEntity findOne(Long ID) {
        Optional<PacPatientRequestEntity> optional = pacPatientRequestRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }
    
    public PacPatientRequestEntity save(PacPatientRequestEntity model) throws Exception {
        pacPatientRequestRepository.save(model);
        return model;
    }
    
    public void remove(PacPatientRequestEntity model) throws Exception {
        pacPatientRequestRepository.delete(model);
    }
        
    
}
