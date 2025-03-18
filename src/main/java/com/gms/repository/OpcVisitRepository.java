package com.gms.repository;

import com.gms.entity.db.OpcVisitEntity;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcVisitRepository extends CrudRepository<OpcVisitEntity, Long> {

    @Override
    public Optional<OpcVisitEntity> findById(Long ID);

    @Override
    public OpcVisitEntity save(OpcVisitEntity model);

    @Query("SELECT e FROM OpcVisitEntity e WHERE e.arvID=:arvID AND e.remove = :remove ORDER BY e.examinationTime desc, e.ID desc")
    public List<OpcVisitEntity> findByArvID(@Param("arvID") Long arvID, @Param("remove") boolean isRemove);
    
    @Query("SELECT e FROM OpcVisitEntity e WHERE e.stageID = :stageID ORDER BY e.examinationTime ASC, e.ID ASC")
    public LinkedList<OpcVisitEntity> findByStageIDAndOderByExaminationTime(@Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcVisitEntity e WHERE e.stageID=:stageID AND e.remove = false AND e.importable = true "
            + " AND DATE_FORMAT(e.examinationTime, '%Y-%m-%d') = :date ORDER BY e.examinationTime desc, e.ID desc")
    public List<OpcVisitEntity> findByExaminationTimeAndStageIDImportVisit(@Param("date") String date, @Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcVisitEntity e WHERE e.arvID=:arvID AND e.stageID = :stageID AND e.dataID = :dataID")
    public List<OpcVisitEntity> findByArvIDAndStageIDAndDataID(@Param("arvID") Long arvID, @Param("stageID") Long stageID, @Param("dataID") String dataID);

    @Query("SELECT e FROM OpcVisitEntity e WHERE e.patientID=:patientID AND e.remove = :remove ORDER BY e.examinationTime desc, e.ID desc")
    public List<OpcVisitEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove);
    
    @Query("SELECT e FROM OpcVisitEntity e WHERE e.patientID=:patientID AND e.remove = :remove ORDER BY e.examinationTime asc")
    public List<OpcVisitEntity> findByPatientIDAsc(@Param("patientID") Long patientID, @Param("remove") boolean isRemove);

    @Query("SELECT count(e) > 0 FROM OpcVisitEntity e WHERE e.stageID = :stageID")
    public boolean countByStageID(@Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcVisitEntity e WHERE e.arvID = :arvID "
            + "AND DATE_FORMAT(e.examinationTime, '%Y-%m-%d') BETWEEN :createdAtFrom AND :createdAtTo "
            + "AND e.remove = 0 "
            + "ORDER BY e.examinationTime DESC")
    public List<OpcVisitEntity> findByArvID(@Param("arvID") Long arvID,
            @Param("createdAtFrom") String createdAtFrom,
            @Param("createdAtTo") String createdAtTo);
}
