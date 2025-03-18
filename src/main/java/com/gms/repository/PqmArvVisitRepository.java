package com.gms.repository;

import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.entity.db.PqmArvVisitEntity;
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
public interface PqmArvVisitRepository extends CrudRepository<PqmArvVisitEntity, Long> {

    @Override
    public List<PqmArvVisitEntity> findAll();

    @Query("SELECT s FROM PqmArvVisitEntity s WHERE s.arvID = :arvID ORDER BY s.examinationTime DESC")
    public List<PqmArvVisitEntity> findByArvID(@Param("arvID") Long arvID);

    @Query("SELECT s FROM PqmArvVisitEntity s WHERE s.stageID = :stageID ORDER BY s.examinationTime DESC")
    public List<PqmArvVisitEntity> findByStageID(@Param("stageID") Long stageID);

    @Query(" SELECT e FROM PqmArvVisitEntity e WHERE e.arvID IN (:arvID) ")
    public List<PqmArvVisitEntity> findByArvIDs(@Param("arvID") Set<Long> arvID);
    
    @Query("SELECT s FROM PqmArvVisitEntity s WHERE s.arvID = :arvID "
            + " AND DATE_FORMAT(s.examinationTime, '%Y-%m-%d') = :examinationTime "
            )
    public List<PqmArvVisitEntity> findByArvIDAndExaminationTime(@Param("arvID") Long arvID, @Param("examinationTime") String examinationTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_arv_visit e WHERE e.arv_id IN (:arvID) ",
            nativeQuery = true)
    public void resetDataByArvID(
            @Param("arvID")  Set<Long> arvID
    );
    
    
}
