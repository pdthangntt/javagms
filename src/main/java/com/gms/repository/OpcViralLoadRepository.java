package com.gms.repository;

import com.gms.entity.db.OpcViralLoadEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcViralLoadRepository extends CrudRepository<OpcViralLoadEntity, Long> {

    @Override
    public Optional<OpcViralLoadEntity> findById(Long ID);

    @Override
    public OpcViralLoadEntity save(OpcViralLoadEntity model);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.arvID=:arvID AND e.remove = :remove ORDER BY e.testTime desc, e.ID desc")
    public List<OpcViralLoadEntity> findByArvID(@Param("arvID") Long arvID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.arvID=:arvID AND e.remove = :remove AND e.siteID = :siteID AND e.testTime IS NOT NULL ORDER BY  e.testTime desc, e.createAT desc")
    public List<OpcViralLoadEntity> findByArvIDandSiteID(@Param("arvID") Long arvID, @Param("remove") boolean isRemove, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.arvID=:arvID AND e.siteID = :siteID AND e.remove = :remove")
    public List<OpcViralLoadEntity> findByArvID(@Param("arvID") Long arvID, @Param("siteID") Long siteID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.patientID=:patientID AND e.remove = false ORDER BY e.testTime asc")
    public List<OpcViralLoadEntity> findByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.patientID=:patientID AND e.remove = :remove ORDER BY e.testTime desc, e.id desc")
    public List<OpcViralLoadEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.patientID=:patientID AND e.remove = :remove AND e.siteID = :siteID ORDER BY e.testTime asc")
    public List<OpcViralLoadEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove, @Param("siteID") Long siteID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM opc_viral_load WHERE arv_id = :arvID", nativeQuery = true)
    public void deleteByArvID(@Param("arvID") Long arvID);

    @Query("SELECT count(e) > 0 FROM OpcViralLoadEntity e WHERE e.stageID = :stageID")
    public boolean countByStageID(@Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.stageID = :stageID AND e.remove = false AND e.importable = true "
            + " AND DATE_FORMAT(e.testTime, '%Y-%m-%d') = :date ORDER BY e.testTime desc, e.ID desc")
    public List<OpcViralLoadEntity> findByTestTimeAndStageID(@Param("date") String date, @Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcViralLoadEntity e WHERE e.arvID=:arvID AND e.stageID = :stageID AND e.dataID = :dataID")
    public List<OpcViralLoadEntity> findByArvIDAndStageIDAndDataID(@Param("arvID") Long arvID, @Param("stageID") Long stageID, @Param("dataID") String dataID);
}
