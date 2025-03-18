package com.gms.repository;

import com.gms.entity.db.OpcArvEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcArvRepository extends CrudRepository<OpcArvEntity, Long> {

    @Override
    public Optional<OpcArvEntity> findById(Long ID);

    @Override
    public List<OpcArvEntity> findAllById(Iterable<Long> IDs);

    @Query(" SELECT e FROM OpcArvEntity e WHERE e.siteID = :siteID AND e.code = :code")
    public OpcArvEntity findByCode(@Param("siteID") Long siteID, @Param("code") String code);

    @Override
    public OpcArvEntity save(OpcArvEntity model);

    @Query(" SELECT COUNT(*) FROM OpcArvEntity e WHERE e.siteID = :siteID AND e.code = :code")
    public Integer existsByCode(@Param("siteID") Long siteID, @Param("code") String code);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = :remove "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') >= :treatmentStageTimeFrom OR :treatmentStageTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') <= :treatmentStageTimeTo OR :treatmentStageTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') >= :registrationTimeFrom OR :registrationTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') <= :registrationTimeTo OR :registrationTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.dateOfArrival, '%Y-%m-%d') >= :dateOfArrivalFrom OR :dateOfArrivalFrom IS NULL) "
            + "AND (DATE_FORMAT(e.dateOfArrival, '%Y-%m-%d') <= :dateOfArrivalTo OR :dateOfArrivalTo IS NULL) "
            + "AND (DATE_FORMAT(e.tranferToTime, '%Y-%m-%d') >= :tranferToTimeFrom OR :tranferToTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.tranferToTime, '%Y-%m-%d') <= :tranferToTimeTo OR :tranferToTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.tranferFromTime, '%Y-%m-%d') >= :tranferFromTimeFrom OR :tranferFromTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.tranferFromTime, '%Y-%m-%d') <= :tranferFromTimeTo OR :tranferFromTimeTo IS NULL) "
            + "AND ((e.insuranceNo IS NOT NULL AND e.insuranceNo <> '' AND e.insuranceExpiry IS NOT NULL AND DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d') > DATE_FORMAT(CURDATE(), '%Y-%m-%d') AND  DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) >= 1 AND  DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) <= 30 AND :insuranceExpiry = '1') OR "
            + " (e.insuranceNo IS NOT NULL AND e.insuranceNo <> '' AND e.insuranceExpiry IS NOT NULL AND DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d') > DATE_FORMAT(CURDATE(), '%Y-%m-%d') AND  DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) > 30 AND  DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) <= 60 AND :insuranceExpiry = '2') OR "
            + " (e.insuranceNo IS NOT NULL AND e.insuranceNo <> '' AND e.insuranceExpiry IS NOT NULL AND DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d') > DATE_FORMAT(CURDATE(), '%Y-%m-%d') AND  DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) > 60 AND   DATEDIFF(DATE_FORMAT(e.insuranceExpiry, '%Y-%m-%d'), DATE_FORMAT(CURDATE(), '%Y-%m-%d')) <= 90 AND :insuranceExpiry = '3') OR "
            + " :insuranceExpiry IS NULL OR :insuranceExpiry IS '') "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (t.fullName LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL) "
            + "AND (t.identityCard LIKE CONCAT('%',:identityCard, '%') OR :identityCard IS '' OR :identityCard IS NULL) "
            + "AND (e.insuranceNo LIKE CONCAT('%',:insuranceNo, '%') OR :insuranceNo IS '' OR :insuranceNo IS NULL) "
            + "AND (e.insurance = :insurance OR :insurance IS '' OR :insurance IS NULL) "
            + "AND (e.insuranceType = :insuranceType OR :insuranceType IS '' OR :insuranceType IS NULL) "
            + "AND (e.treatmentStageID IN :treatmentStageID OR coalesce(:treatmentStageID, null) IS NULL) "
            + "AND (e.statusOfTreatmentID IN :statusOfTreatmentID OR coalesce(:statusOfTreatmentID, null) IS NULL) "
            + "AND (('-1' NOT IN :treatmentRegimenID AND e.treatmentRegimenID IN :treatmentRegimenID) OR ('-1' IN :treatmentRegimenID AND (e.treatmentRegimenID IN :treatmentRegimenID OR e.treatmentRegimenID IS NULL OR  e.treatmentRegimenID IS '')) OR coalesce(:treatmentRegimenID, null) IS NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + "AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + "AND ( ( :gsph = '0' AND e.transferTimeGSPH is null) OR ( :gsph = '1' AND e.transferTimeGSPH is not null) OR :gsph is null ) "
    )
    public Page<OpcArvEntity> find(
            @Param("remove") boolean remove,
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("name") String name,
            @Param("identityCard") String identityCard,
            @Param("insurance") String insurance,
            @Param("insuranceType") String insuranceType,
            @Param("insuranceNo") String insuranceNo,
            @Param("treatmentStageTimeFrom") String treatmentStageTimeFrom,
            @Param("treatmentStageTimeTo") String treatmentStageTimeTo,
            @Param("registrationTimeFrom") String registrationTimeFrom,
            @Param("registrationTimeTo") String registrationTimeTo,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            @Param("dateOfArrivalFrom") String dateOfArrivalFrom,
            @Param("dateOfArrivalTo") String dateOfArrivalTo,
            @Param("tranferToTimeFrom") String tranferToTimeFrom,
            @Param("tranferToTimeTo") String tranferToTimeTo,
            @Param("tranferFromTimeFrom") String tranferFromTimeFrom,
            @Param("tranferFromTimeTo") String tranferFromTimeTo,
            @Param("statusOfTreatmentID") Set<String> statusOfTreatmentID,
            @Param("treatmentStageID") Set<String> treatmentStageID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("treatmentRegimenID") Set<String> treatmentRegimenID,
            @Param("insuranceExpiry") String insuranceExpiry,
            @Param("gsph") String gsph,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = false "
            + "AND (t.identityCard = :identityCard OR :identityCard IS '' OR :identityCard IS NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
    )
    public List<OpcArvEntity> findConnectHtcVisit(
            @Param("siteID") Set<Long> siteID,
            @Param("identityCard") String identityCard);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = false "
            + "AND ( LOWER(t.fullName) LIKE CONCAT('%', CONVERT(:name, BINARY), '%') ) "
            + "AND (TIMESTAMPDIFF(MONTH, t.dob, :examinationTime) = :month) "
            + "AND ( t.genderID = :gender ) "
            + "AND ( e.insuranceNo = :insurance OR :insurance IS null  OR :insurance IS '' ) "
            + "AND ( e.siteID = :siteID ) "
            + "ORDER BY e.createAT desc")
    public List<OpcArvEntity> findArvImportExcelVisit(
            @Param("name") String name,
            @Param("month") Integer month,
            @Param("examinationTime") String examinationTime,
            @Param("gender") String gender,
            @Param("insurance") String insurance,
            @Param("siteID") Long siteID
    );

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = false "
            + "AND (LOWER(t.fullName) LIKE CONCAT('%', CONVERT(:name, BINARY), '%')) "
            + "AND ( YEAR(t.dob) = :yearOfBirth ) "
            + "AND ( t.genderID = :gender ) "
            + "AND ( e.insuranceNo = :insurance OR :insurance IS NULL OR :insurance IS '') "
            + "AND ( e.siteID = :siteID ) "
            + "ORDER BY e.createAT desc")
    public List<OpcArvEntity> findArvImportExcelViral(
            @Param("name") String name,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("gender") String gender,
            @Param("insurance") String insurance,
            @Param("siteID") Long siteID
    );

    @Query(value = "SELECT "
            + "    a.* "
            + "FROM opc_arv as a INNER JOIN opc_patient as p ON a.patient_id = p.id "
            + "WHERE a.is_remove = 0 "
            + "    AND a.site_id = :siteID"
            + "    AND a.id IN ("
            + "        SELECT m.arv_id FROM (SELECT * "
            + "        FROM (SELECT *"
            + "            FROM opc_arv_revision as logmax "
            + "            WHERE logmax.created_at <= :registrationTimeTo  AND logmax.site_id = :siteID"
            + "            ORDER BY logmax.id desc , logmax.created_at desc) as m1  "
            + "        GROUP BY m1.arv_id, m1.stage_id) as m "
            + "        WHERE m.is_remove = 0 "
            + "            AND m.registration_time BETWEEN :registrationTimeFrom AND :registrationTimeTo  ORDER BY m.registration_time desc)",
            nativeQuery = true)
    public Page<OpcArvEntity> findPreArv(
            @Param("siteID") Long siteID,
            @Param("registrationTimeFrom") String registrationTimeFrom,
            @Param("registrationTimeTo") String registrationTimeTo,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = :remove "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') >= :treatmentStageTimeFrom OR :treatmentStageTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') <= :treatmentStageTimeTo OR :treatmentStageTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') >= :registrationTimeFrom OR :registrationTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') <= :registrationTimeTo OR :registrationTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.viralLoadTime, '%Y-%m-%d') >= :viralLoadTimeFrom OR :viralLoadTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.viralLoadTime, '%Y-%m-%d') <= :viralLoadTimeTo OR :viralLoadTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.viralLoadRetryTime, '%Y-%m-%d') >= :viralLoadRetryFrom OR :viralLoadRetryFrom IS NULL) "//Ngày hẹn xét nghiệm
            + "AND (DATE_FORMAT(e.viralLoadRetryTime, '%Y-%m-%d') <= :viralLoadRetryTo OR :viralLoadRetryTo IS NULL) "
            + "AND (e.viralLoadResult = :resultID OR :resultID IS '' OR :resultID IS NULL) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (t.fullName LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL) "
            + "AND (t.identityCard LIKE CONCAT('%',:identityCard, '%') OR :identityCard IS '' OR :identityCard IS NULL) "
            + "AND (e.insuranceNo LIKE CONCAT('%',:insuranceNo, '%') OR :insuranceNo IS '' OR :insuranceNo IS NULL) "
            + "AND (e.insurance = :insurance OR :insurance IS '' OR :insurance IS NULL) "
            + "AND (e.insuranceType = :insuranceType OR :insuranceType IS '' OR :insuranceType IS NULL) "
            + "AND (e.treatmentStageID = :treatmentStageID OR :treatmentStageID IS '' OR :treatmentStageID IS NULL) "
            + "AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + "AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + "AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + "AND (:tab = '1' AND e.viralLoadRetryTime IS NOT NULL OR :tab = '0') "
            + "AND (e.tranferFromTime IS NULL) "
    )
    public Page<OpcArvEntity> findViral(
            @Param("tab") String tab,
            @Param("remove") boolean remove,
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("name") String name,
            @Param("identityCard") String identityCard,
            @Param("insurance") String insurance,
            @Param("insuranceType") String insuranceType,
            @Param("insuranceNo") String insuranceNo,
            @Param("treatmentStageTimeFrom") String treatmentStageTimeFrom,
            @Param("treatmentStageTimeTo") String treatmentStageTimeTo,
            @Param("registrationTimeFrom") String registrationTimeFrom,
            @Param("registrationTimeTo") String registrationTimeTo,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            @Param("viralLoadTimeFrom") String viralLoadTimeFrom,
            @Param("viralLoadTimeTo") String viralLoadTimeTo,
            @Param("viralLoadRetryFrom") String viralLoadRetryFrom,
            @Param("viralLoadRetryTo") String viralLoadRetryTo,
            @Param("resultID") String resultID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("treatmentStageID") String treatmentStageID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = :remove AND e.appointmentTime IS NOT NULL "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') >= :treatmentStageTimeFrom OR :treatmentStageTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') <= :treatmentStageTimeTo OR :treatmentStageTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') >= :registrationTimeFrom OR :registrationTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') <= :registrationTimeTo OR :registrationTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.viralLoadTime, '%Y-%m-%d') >= :viralLoadTimeFrom OR :viralLoadTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.viralLoadTime, '%Y-%m-%d') <= :viralLoadTimeTo OR :viralLoadTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.lastExaminationTime, '%Y-%m-%d') >= :lastExaminationTimeFrom OR :lastExaminationTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.lastExaminationTime, '%Y-%m-%d') <= :lastExaminationTimeTo OR :lastExaminationTimeTo IS NULL) "
            + "AND (e.viralLoadResult = :resultID OR :resultID IS '' OR :resultID IS NULL) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (t.fullName LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL) "
            + "AND (t.identityCard LIKE CONCAT('%',:identityCard, '%') OR :identityCard IS '' OR :identityCard IS NULL) "
            + "AND (e.insuranceNo LIKE CONCAT('%',:insuranceNo, '%') OR :insuranceNo IS '' OR :insuranceNo IS NULL) "
            + "AND (e.insurance = :insurance OR :insurance IS '' OR :insurance IS NULL) "
            + "AND (e.insuranceType = :insuranceType OR :insuranceType IS '' OR :insuranceType IS NULL) "
            + "AND (e.treatmentStageID = :treatmentStageID OR :treatmentStageID IS '' OR :treatmentStageID IS NULL) "
            + "AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + "AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + "AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + "AND (e.tranferFromTime IS NULL) "
            + "AND ((:tab = '1' AND DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') >= :currentDate) OR (:tab = '2' AND DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') < :currentDate) OR (:tab = '3' AND DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') <= :late84) OR (:tab = '4' AND DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') <= :late30 AND DATE_FORMAT(e.appointmentTime, '%Y-%m-%d') >= :late90) OR :tab = '0') "
    )
    public Page<OpcArvEntity> findVisit(
            @Param("tab") String tab,
            @Param("remove") boolean remove,
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("name") String name,
            @Param("identityCard") String identityCard,
            @Param("insurance") String insurance,
            @Param("insuranceType") String insuranceType,
            @Param("insuranceNo") String insuranceNo,
            @Param("treatmentStageTimeFrom") String treatmentStageTimeFrom,
            @Param("treatmentStageTimeTo") String treatmentStageTimeTo,
            @Param("registrationTimeFrom") String registrationTimeFrom,
            @Param("registrationTimeTo") String registrationTimeTo,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            @Param("viralLoadTimeFrom") String viralLoadTimeFrom,
            @Param("viralLoadTimeTo") String viralLoadTimeTo,
            @Param("resultID") String resultID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("treatmentStageID") String treatmentStageID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("currentDate") String currentDate,
            @Param("lastExaminationTimeFrom") String lastExaminationTimeFrom,
            @Param("lastExaminationTimeTo") String lastExaminationTimeTo,
            @Param("late84") String late84,
            @Param("late30") String late30,
            @Param("late90") String late90,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e INNER JOIN OpcPatientEntity t ON e.patientID = t.ID WHERE e.remove = false "
            + "AND (e.endCase = '3') "
            + "AND (e.removeTranfer = :remove) "
            + "AND (e.tranferFromTime IS NOT NULL) "
            + "AND (e.transferSiteID = :currentSiteID) "
            + "AND (DATE_FORMAT(e.tranferFromTime, '%Y-%m-%d') = :tranferTime OR :tranferTime IS NULL) "
            + "AND (DATE_FORMAT(t.confirmTime, '%Y-%m-%d') = :confirmTime OR :confirmTime IS NULL) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (t.fullName LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL) "
            + "AND (t.confirmCode LIKE CONCAT('%',:confirmCode, '%') OR :confirmCode IS '' OR :confirmCode IS NULL) "
            + "AND (e.siteID = :sourceSiteID OR :sourceSiteID IS NULL) "
            + "AND ((:status = '0' AND e.tranferToTimeOpc IS NULL) OR (:status = '1' AND e.tranferToTimeOpc IS NOT NULL) OR :status = '-1') "
    )
    public Page<OpcArvEntity> findReceive(
            @Param("remove") boolean remove,
            @Param("code") String code,
            @Param("name") String name,
            @Param("confirmCode") String confirmCode,
            @Param("confirmTime") String confirmTime,
            @Param("tranferTime") String tranferTime,
            @Param("currentSiteID") Long currentSiteID,
            @Param("sourceSiteID") Long sourceSiteID,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e WHERE e.siteID = :siteID AND e.code = :code")
    public OpcArvEntity findBySiteIDAndCode(@Param("siteID") Long siteID, @Param("code") String code);
   
    @Query("SELECT e FROM OpcArvEntity e WHERE e.siteID IN (:siteID) ")
    public List<OpcArvEntity> findBySiteIDs(@Param("siteID") Set<Long> siteID);

    @Query("SELECT e FROM OpcArvEntity e WHERE e.siteID = :siteID AND e.code = :code AND e.dataID = :dataID")
    public OpcArvEntity findBySiteIDAndCodeAndDataID(@Param("siteID") Long siteID, @Param("code") String code, @Param("dataID") String dataID);

    @Query("SELECT e FROM OpcArvEntity e WHERE e.sourceID=:sourceID AND e.sourceServiceID = :sourceServiceID ORDER BY e.createAT desc")
    public List<OpcArvEntity> findBySourceIDAndSourceServiceID(@Param("sourceID") Long sourceID, @Param("sourceServiceID") String sourceServiceID);

    @Query("SELECT e FROM OpcArvEntity e WHERE e.remove = false ORDER BY e.registrationTime asc")
    public List<OpcArvEntity> getOpcArv();

    /**
     * DS biến động điều trị
     *
     * @author DSNAnh
     * @param siteID
     * @param treatmentStageTimeFrom
     * @param treatmentStageTimeTo
     * @param treatmentStageID
     * @param permanentDistrictID
     * @param permanentProvinceID
     * @param keywords
     * @param pageable
     * @return list
     */
    @Query("SELECT e FROM OpcArvEntity e JOIN OpcPatientEntity o ON e.patientID = o.ID  WHERE e.remove = false "
            + "AND (e.treatmentStageTime IS NOT NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') >= :treatmentStageTimeFrom OR :treatmentStageTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentStageTime, '%Y-%m-%d') <= :treatmentStageTimeTo OR :treatmentStageTimeTo IS NULL) "
            + "AND (e.treatmentStageID IN :treatmentStageID  OR coalesce(:treatmentStageID, null) IS NULL) "
            + "AND (e.statusOfTreatmentID IN :statusOfTreatmentID  OR coalesce(:statusOfTreatmentID, null) IS NULL) "
            + "AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + "AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + "AND (:keywords IS NULL OR :keywords IS '' OR o.fullName LIKE %:keywords% OR o.identityCard LIKE %:keywords% OR e.patientPhone LIKE %:keywords% OR e.insuranceNo LIKE %:keywords% OR e.code LIKE %:keywords% ) "
    )
    public Page<OpcArvEntity> findTreatmentFluctuations(
            @Param("siteID") Set<Long> siteID,
            @Param("treatmentStageTimeFrom") String treatmentStageTimeFrom,
            @Param("treatmentStageTimeTo") String treatmentStageTimeTo,
            @Param("treatmentStageID") Set<String> treatmentStageID,
            @Param("statusOfTreatmentID") Set<String> statusOfTreatmentID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("keywords") String keywords,
            Pageable pageable);

    /**
     * DSBN quản lý
     *
     * @author DSNAnh
     * @param inhFrom
     * @param inhTo
     * @param cotriFrom
     * @param cotriTo
     * @param siteID
     * @param laoFrom
     * @param laoTo
     * @param registrationTimeFrom
     * @param registrationTimeTo
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param statusOfTreatmentID
     * @param permanentDistrictID
     * @param permanentProvinceID
     * @param keywords
     * @param registrationType
     * @param treatmentRegimenID
     * @param gpsh
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM OpcArvEntity e JOIN OpcPatientEntity o ON e.patientID = o.ID WHERE e.remove = false "
            + "AND (e.registrationTime IS NOT NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') >= :registrationTimeFrom OR :registrationTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.registrationTime, '%Y-%m-%d') <= :registrationTimeTo OR :registrationTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.inhFromTime, '%Y-%m-%d') >= :inhFrom OR :inhFrom IS NULL) "
            + "AND (DATE_FORMAT(e.inhFromTime, '%Y-%m-%d') <= :inhTo OR :inhTo IS NULL) "
            + "AND (DATE_FORMAT(e.cotrimoxazoleFromTime, '%Y-%m-%d') >= :cotriFrom OR :cotriFrom IS NULL) "
            + "AND (DATE_FORMAT(e.cotrimoxazoleFromTime, '%Y-%m-%d') <= :cotriTo OR :cotriTo IS NULL) "
            + "AND (DATE_FORMAT(e.laoStartTime, '%Y-%m-%d') >= :laoFrom OR :laoFrom IS NULL) "
            + "AND (DATE_FORMAT(e.laoStartTime, '%Y-%m-%d') <= :laoTo OR :laoTo IS NULL) "
            + "AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + "AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + "AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + "AND (e.registrationType = :registrationType OR :registrationType IS '' OR :registrationType IS NULL) "
            + "AND ( ( :gsph = '0' AND e.transferTimeGSPH is null) OR ( :gsph = '1' AND e.transferTimeGSPH is not null) OR :gsph is null ) "
            + "AND ((:treatmentRegimenID IS NOT NULL AND :treatmentRegimenID <> '' AND :treatmentRegimenID <> '-1' AND e.treatmentRegimenID = :treatmentRegimenID) OR (:treatmentRegimenID = '-1' AND (e.treatmentRegimenID IS NULL OR e.treatmentRegimenID IS '')) OR :treatmentRegimenID IS '' OR :treatmentRegimenID IS NULL) "
            + "AND (:keywords IS NULL OR :keywords IS '' OR o.fullName LIKE %:keywords% OR o.identityCard LIKE %:keywords% OR e.patientPhone LIKE %:keywords% OR e.insuranceNo LIKE %:keywords% OR e.code LIKE %:keywords% ) "
    )
    public Page<OpcArvEntity> findOpcPatient(
            @Param("inhFrom") String inhFrom,
            @Param("inhTo") String inhTo,
            @Param("cotriFrom") String cotriFrom,
            @Param("cotriTo") String cotriTo,
            @Param("laoFrom") String laoFrom,
            @Param("laoTo") String laoTo,
            @Param("siteID") Set<Long> siteID,
            @Param("registrationTimeFrom") String registrationTimeFrom,
            @Param("registrationTimeTo") String registrationTimeTo,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("keywords") String keywords,
            @Param("registrationType") String registrationType,
            @Param("treatmentRegimenID") String treatmentRegimenID,
            @Param("gsph") String gsph,
            Pageable pageable);

    /**
     * Sổ ARV
     *
     * @author DSNAnh
     * @param siteID
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM OpcArvEntity e WHERE e.remove = false "
            + "AND (e.treatmentTime IS NOT NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
    )
    public Page<OpcArvEntity> findArvBook(
            @Param("siteID") Set<Long> siteID,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            Pageable pageable);

    @Query("SELECT e FROM OpcArvEntity e WHERE e.remove = false "
            + "AND (e.treatmentTime IS NOT NULL) "
            + "AND (e.siteID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >= :treatmentTimeFrom OR :treatmentTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <= :treatmentTimeTo OR :treatmentTimeTo IS NULL) "
    )
    public Page<OpcArvEntity> findArvBooks(
            @Param("siteID") Set<Long> siteID,
            @Param("treatmentTimeFrom") String treatmentTimeFrom,
            @Param("treatmentTimeTo") String treatmentTimeTo,
            Pageable pageable);

    /**
     * Lấy ds bệnh nhân bên OPC với ngày cập nhật chuyển gsph null
     *
     * @param page
     * @return
     */
    @Query(value = "SELECT * FROM opc_arv as opc WHERE opc.update_time_gsph is not null AND opc.is_remove is false ORDER BY opc.updated_at asc", nativeQuery = true)
    public List<OpcArvEntity> getForUpdateOpcToPac(Pageable page);

    @Query(value = "SELECT * FROM opc_arv as opc WHERE opc.TRANSFER_TIME_GSPH is null AND opc.gsph_id is null AND opc.STATUS_OF_TREATMENT_ID != '0' AND opc.is_remove is false ORDER BY opc.updated_at asc", nativeQuery = true)
    public List<OpcArvEntity> getNoForPac(Pageable page);  

    /**
     * Ca đăng ký điều trị đầu tiên
     *
     * @auth vvThành
     * @param siteIds
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM OpcArvEntity e WHERE e.siteID IN :siteID AND registrationTime is not null AND e.remove = false")
    public Page<OpcArvEntity> getFirst(@Param("siteID") Set<Long> siteIds, Pageable pageable);

    /**
     * Tìm theo bệnh nhân
     *
     * @auth vvThành
     * @param patientID
     * @return
     */
    @Query("SELECT e FROM OpcArvEntity e WHERE e.patientID = :patientID AND e.remove = false")
    public List<OpcArvEntity> findByPatient(@Param("patientID") Long patientID);

    @Query(value = "SELECT v.* FROM opc_arv v INNER JOIN "
            + "(SELECT id, "
            + "   SUBSTRING_INDEX(e.code, '-', -1) as tutang "
            + "   FROM opc_arv as e "
            + "	WHERE e.site_id= :siteID "
            + "   AND e.code LIKE CONCAT(:siteCode, '%') "
            + "    ORDER BY tutang + 0 DESC LIMIT 1 ) a "
            + " ON v.id = a.id;", nativeQuery = true)
    public OpcArvEntity findLastCodeBySiteID(@Param("siteID") Long siteID, @Param("siteCode") String siteCode);
}
