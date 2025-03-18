package com.gms.repository;

import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcStageEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TrangBN
 */
@Repository
public interface OpcArvRevisionRepository extends CrudRepository<OpcArvRevisionEntity, Long> {

    @Override
    public Optional<OpcArvRevisionEntity> findById(Long ID);

    @Override
    public OpcArvRevisionEntity save(OpcArvRevisionEntity model);

    /**
     *
     * @param siteID
     * @param arvID
     * @param timeChangeFrom
     * @param timeChangeTo
     * @param registrationTime
     * @param treatmentTime
     * @param statusOfTreatmentID
     * @param endCase
     * @param endTime
     * @param tranferFromTime
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM OpcArvRevisionEntity e WHERE e.arvID = :arvID "
            + "AND ( e.siteID = :siteID ) "
            + "AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') >= :timeChangeFrom OR :timeChangeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') <= :timeChangeTo OR :timeChangeTo IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') >= :registrationTime OR :registrationTime IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') <= :registrationTime OR :registrationTime IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d')    >= :treatmentTime OR :treatmentTime IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d')    <= :treatmentTime OR :treatmentTime IS NULL) "
            + "AND ( e.statusOfTreatmentID  = :statusOfTreatmentID OR :statusOfTreatmentID IS NULL ) "
            + "AND ( e.endCase = :endCase OR :endCase IS NULL ) "
            + "AND (DATE_FORMAT(e.endTime, '%Y-%m-%d')    >= :endTime OR :endTime IS NULL) "
            + "AND (DATE_FORMAT(e.endTime, '%Y-%m-%d')    <= :endTime OR :endTime IS NULL) "
            + "AND (DATE_FORMAT(e.tranferFromTime, '%Y-%m-%d')    >= :tranferFromTime OR :tranferFromTime IS NULL) "
            + "AND (DATE_FORMAT(e.tranferFromTime, '%Y-%m-%d')    <= :tranferFromTime OR :tranferFromTime IS NULL) "
    )
    public Page<OpcArvRevisionEntity> findArvRevision(
            @Param("siteID") Long siteID,
            @Param("arvID") Long arvID,
            @Param("timeChangeFrom") String timeChangeFrom,
            @Param("timeChangeTo") String timeChangeTo,
            @Param("registrationTime") String registrationTime,
            @Param("treatmentTime") String treatmentTime,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("endCase") String endCase,
            @Param("endTime") String endTime,
            @Param("tranferFromTime") String tranferFromTime,
            Pageable pageable);
    
     @Query("SELECT e FROM OpcArvRevisionEntity e WHERE e.arvID=:arvID AND e.siteID = :siteID ORDER BY e.createAT asc")
    public List<OpcArvRevisionEntity> findByArvIDAndSite(@Param("arvID") Long arvID, @Param("siteID") Long siteID);
     
    @Query("SELECT e FROM OpcArvRevisionEntity e WHERE e.arvID=:arvID AND e.siteID = :siteID AND e.stageID = :stageID ORDER BY e.createAT desc")
    public List<OpcArvRevisionEntity> findByArvIDAndSiteAndStage(@Param("arvID") Long arvID, @Param("siteID") Long siteID, @Param("stageID") Long stageID);

}
