package com.gms.repository;

import com.gms.entity.db.HtcVisitLogEntity;
import com.gms.entity.db.OpcArvLogEntity;
import java.util.List;
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
public interface OpcArvLogRepository extends CrudRepository<OpcArvLogEntity, Long> {

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findByArvID(@Param("arvID") Long arvID);
    
    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.viralLoadID IS NOT NULL AND e.arvID = :arvID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findViralByArvID(@Param("arvID") Long arvID);
    
    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.visitID IS NOT NULL AND e.arvID = :arvID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findVisitByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.patientID = :patientID ORDER BY e.time asc")
    public List<HtcVisitLogEntity> findByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.stageID is not null ORDER BY e.time asc")
    public List<HtcVisitLogEntity> findStageByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.testID is not null ORDER BY e.time asc")
    public List<HtcVisitLogEntity> findTestByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.viralLoadID is not null ORDER BY e.time asc")
    public List<HtcVisitLogEntity> findViralLoadByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.stageID = :stageID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findByArvIDAndStageID(@Param("arvID") Long arvID, @Param("stageID") Long stageID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.testID = :testID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findByArvIDAndTestID(@Param("arvID") Long arvID, @Param("testID") Long testID);

    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.visitID = :visitID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findByArvIDAndVisitID(@Param("arvID") Long arvID, @Param("visitID") Long visitID);
    
    @Query("SELECT e FROM OpcArvLogEntity e WHERE e.arvID = :arvID AND e.viralLoadID = :viralLoadID ORDER BY e.time asc")
    public List<OpcArvLogEntity> findByArvIDAndViralID(@Param("arvID") Long arvID, @Param("viralLoadID") Long viralLoadID);
    
    
    /**
     * pdThang 
     * Lấy toàn bô ls
     * @param ID
     * @param from
     * @param to
     * @param category
     * @param staffID
     * @param pageable
     * @return 
     */
    @Query("SELECT e FROM OpcArvLogEntity e WHERE 1 = 1 "
//            + " AND (e.patientID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.staffID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " AND ( (:category = '1' AND e.stageID IS NULL AND e.visitID IS NULL AND e.testID IS NULL AND e.viralLoadID IS NULL ) "
            + " OR (:category = '2' AND e.stageID IS NOT NULL) "
            + " OR (:category = '3' AND e.visitID IS NOT NULL) "
            + " OR (:category = '4' AND e.testID IS NOT NULL) "
            + " OR (:category = '5' AND e.viralLoadID IS NOT NULL) "
            + " OR  :category IS NULL ) "
            + " ORDER BY e.time DESC "
    )
    public Page<OpcArvLogEntity> findAll(
//            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            @Param("category") String category,
            Pageable pageable);
    
}
