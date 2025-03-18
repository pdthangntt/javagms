package com.gms.repository;

import com.gms.entity.db.PacPatientRequestEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DSNAnh
 */
@Repository
public interface PacPatientRequestRepository extends CrudRepository<PacPatientRequestEntity, Long> {
    
    @Override
    public Optional<PacPatientRequestEntity> findById(Long id);
    
    
}
