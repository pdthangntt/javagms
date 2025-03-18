package com.gms.repository;

import com.gms.entity.db.PqmArvTestEntity;
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
public interface PqmArvTestRepository extends CrudRepository<PqmArvTestEntity, Long> {

    @Override
    public List<PqmArvTestEntity> findAll();

    @Query("SELECT s FROM PqmArvTestEntity s WHERE s.arvID = :arvID ORDER BY s.inhFromTime DESC")
    public List<PqmArvTestEntity> findByArvID(@Param("arvID") Long arvID);

    @Query("SELECT s FROM PqmArvTestEntity s WHERE s.stageID = :stageID ORDER BY s.inhFromTime DESC")
    public List<PqmArvTestEntity> findByStageID(@Param("stageID") Long stageID);

    @Query(" SELECT e FROM PqmArvTestEntity e WHERE e.arvID IN (:arvID) ")
    public List<PqmArvTestEntity> findByArvIDs(@Param("arvID") Set<Long> arvID);

    @Query("SELECT s FROM PqmArvTestEntity s WHERE s.arvID = :arvID "
            + " AND DATE_FORMAT(s.inhFromTime, '%Y-%m-%d') = :inhFromTime "
    )
    public List<PqmArvTestEntity> findByArvIDAndInhFromTime(@Param("arvID") Long arvID, @Param("inhFromTime") String inhFromTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_arv_test e WHERE e.arv_id IN (:arvID) ",
            nativeQuery = true)
    public void resetDataByArvID(
            @Param("arvID")  Set<Long> arvID
    );
    
}
