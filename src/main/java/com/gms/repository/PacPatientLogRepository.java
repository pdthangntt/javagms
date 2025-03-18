package com.gms.repository;

import com.gms.entity.db.PacPatientLogEntity;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface PacPatientLogRepository extends CrudRepository<PacPatientLogEntity, Long> {

    @Cacheable(value = "pac_patient_log_cache_findByPatientID")
    @Query("SELECT e FROM PacPatientLogEntity e WHERE e.patientID = :patientID ORDER BY e.time asc")
    public List<PacPatientLogEntity> findByPatientID(@Param("patientID") Long patientID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_patient_log_cache_findByPatientID", allEntries = true)
    })
    public <S extends PacPatientLogEntity> S save(S s);
    
    /**
     * pdThang 
     * Lấy toàn bô ls
     * @param ID
     * @param from
     * @param to
     * @param staffID
     * @param pageable
     * @return 
     */
    @Query("SELECT e FROM PacPatientLogEntity e WHERE 1 = 1 "
            + " AND (e.patientID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.staffID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " ORDER BY e.time DESC "
    )
    public Page<PacPatientLogEntity> findAll(
            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            Pageable pageable);

}
