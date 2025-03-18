package com.gms.repository;

import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvStageEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pdThang
 */
@Repository
public interface PqmArvStageRepository extends CrudRepository<PqmArvStageEntity, Long> {

    @Override
    public List<PqmArvStageEntity> findAll();

    @Query("SELECT s FROM PqmArvStageEntity s WHERE s.arvID = :arvID ORDER BY s.registrationTime DESC, s.treatmentTime DESC")
    public List<PqmArvStageEntity> findByArvID(@Param("arvID") Long arvID);

    @Query("SELECT s FROM PqmArvStageEntity s WHERE s.arvID = :arvID "
            + " AND DATE_FORMAT(s.registrationTime, '%Y-%m-%d') = :registrationTime "
    )
    public List<PqmArvStageEntity> findByArvIDAndRegistrationTime(@Param("arvID") Long arvID, @Param("registrationTime") String registrationTime);

    @Query(" SELECT e FROM PqmArvStageEntity e WHERE e.arvID IN (:arvID) ")
    public List<PqmArvStageEntity> findByArvIDs(@Param("arvID") Set<Long> arvID);

    @Query("SELECT s FROM PqmArvStageEntity s WHERE s.arvID = :arvID ORDER BY s.treatmentTime DESC")
    public List<PqmArvStageEntity> findByArvIDOderByTreatmentTimeDesc(@Param("arvID") Long arvID);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_arv_stage e WHERE e.arv_id IN (:arvID) ",
            nativeQuery = true)
    public void resetDataByArvID(
            @Param("arvID")  Set<Long> arvID
    );

}
