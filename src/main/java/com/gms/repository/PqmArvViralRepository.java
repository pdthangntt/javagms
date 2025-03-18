package com.gms.repository;

import com.gms.entity.db.PqmArvViralLoadEntity;
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
public interface PqmArvViralRepository extends CrudRepository<PqmArvViralLoadEntity, Long> {

    @Override
    public List<PqmArvViralLoadEntity> findAll();

    @Query("SELECT s FROM PqmArvViralLoadEntity s WHERE s.arvID = :arvID ORDER BY s.testTime DESC")
    public List<PqmArvViralLoadEntity> findByArvID(@Param("arvID") Long arvID);

    @Query("SELECT s FROM PqmArvViralLoadEntity s WHERE s.stageID = :stageID ORDER BY s.testTime DESC")
    public List<PqmArvViralLoadEntity> findByStageID(@Param("stageID") Long stageID);

    @Query(" SELECT e FROM PqmArvViralLoadEntity e WHERE e.arvID IN (:arvID) ")
    public List<PqmArvViralLoadEntity> findByArvIDs(@Param("arvID") Set<Long> arvID);

    @Query(" SELECT e FROM PqmArvViralLoadEntity e WHERE e.arvID = :arvID AND e.insuranceNo = :insuranceNo ")
    public List<PqmArvViralLoadEntity> findByArvAndInsuranceNo(@Param("arvID") Long arvID, @Param("insuranceNo") String insuranceNo);

    @Query(" SELECT e FROM PqmArvViralLoadEntity e WHERE e.arvID = :arvID AND e.insuranceNo = :insuranceNo "
            + "AND (DATE_FORMAT(e.testTime, '%Y-%m-%d') = :testTime ) ")
    public List<PqmArvViralLoadEntity> findByArvAndInsuranceNoAndTestTime(@Param("arvID") Long arvID, @Param("insuranceNo") String insuranceNo, @Param("testTime") String testTime);

    @Query(" SELECT e FROM PqmArvViralLoadEntity e WHERE e.arvID = :arvID AND DATE_FORMAT(e.testTime, '%Y-%m-%d') = :testTime  ")
    public List<PqmArvViralLoadEntity> findByArvAndTestTime(@Param("arvID") Long arvID, @Param("testTime") String testTime);

    @Query("SELECT s FROM PqmArvViralLoadEntity s WHERE s.arvID = :arvID "
            + " AND DATE_FORMAT(s.testTime, '%Y-%m-%d') = :testTime "
    )
    public List<PqmArvViralLoadEntity> findByArvIDAndTestTime(@Param("arvID") Long arvID, @Param("testTime") String testTime);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_arv_viral_load e WHERE e.arv_id IN (:arvID) ",
            nativeQuery = true)
    public void resetDataByArvID(
            @Param("arvID")  Set<Long> arvID
    );
    
    
}
