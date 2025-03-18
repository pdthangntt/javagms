package com.gms.repository;

import com.gms.entity.db.PacPatientInfoEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
public interface PacPatientInfoRepository extends CrudRepository<PacPatientInfoEntity, Long> {

    /**
     *
     * @author pdThang
     * @param fullname
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param isRemove
     * @param yearOfBirth
     * @param currentPermanentProvinceID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param detectProvinceID
     * @param provinceID
     * @param permanentProvince
     * @param detectProvince
     * @param acceptPermanentTime
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param genderID
     * @param identityCard
     * @param service
     * @param bloodBase
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND ((e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL) OR (e.acceptPermanentTime IS NOT NULL AND :currentPermanentProvinceID = e.detectProvinceID AND :currentPermanentProvinceID <> e.provinceID)) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.ID = :ID OR :ID = 0) "
            + " AND  e.sourceServiceID <> '103' "
            + " AND ((e.sourceServiceID = :service ) OR :service IS '' OR :service IS NULL) "
            + " AND ((e.bloodBaseID = :bloodBase ) OR :bloodBase IS '' OR :bloodBase IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (( :permanentProvince = 1 AND (e.permanentProvinceID = :currentPermanentProvinceID)) OR (:permanentProvince = 2 AND e.requestVaacTime is null AND e.permanentProvinceID <> :currentPermanentProvinceID AND (:searchProvinceID = e.permanentProvinceID OR :searchProvinceID IS '' OR :searchProvinceID IS NULL)) OR :permanentProvince = 0 ) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + " AND (( :detectProvince = 1 AND (e.detectProvinceID = :detectProvinceID)) OR ( :detectProvince = 2 AND e.requestVaacTime is not null AND (e.detectProvinceID <> :detectProvinceID) AND ( :searchDetectProvinceID = e.detectProvinceID OR :searchDetectProvinceID IS '' OR :searchDetectProvinceID IS NULL)) OR :detectProvince = 0) "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (( :acceptPermanentTime = '1' AND e.requestVaacTime IS NOT NULL) OR :acceptPermanentTime IS NULL OR :acceptPermanentTime IS '') "
            + " AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
//    @Cacheable(value = "pac_patient_cache_findNew")
    public Page<PacPatientInfoEntity> findNew(
            @Param("fullname") String fullname,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("isRemove") boolean isRemove,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("currentPermanentProvinceID") String currentPermanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("detectProvinceID") String detectProvinceID,
            @Param("provinceID") String provinceID,
            @Param("permanentProvince") int permanentProvince,
            @Param("detectProvince") int detectProvince,
            @Param("acceptPermanentTime") String acceptPermanentTime,
            @Param("searchProvinceID") String searchProvinceID,
            @Param("genderID") String genderID,
            @Param("searchDetectProvinceID") String searchDetectProvinceID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("identityCard") String identityCard,
            @Param("service") String service,
            @Param("bloodBase") String bloodBase,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("ID") Long ID,
            Pageable pageable);

    /**
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param ID
     * @auth TrangBN
     *
     * @param fullname
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param isRemove
     * @param yearOfBirth
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvince
     * @param bloodBase
     * @param genderID
     * @param identityCard
     * @param service
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND ((e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL)) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND  e.sourceServiceID <> '103' "
            + " AND ((e.sourceServiceID = :service ) OR :service IS '' OR :service IS NULL) "
            + " AND ((e.bloodBaseID = :bloodBase ) OR :bloodBase IS '' OR :bloodBase IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.ID = :ID OR :ID = 0) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    public Page<PacPatientInfoEntity> findNewVAAC(
            @Param("fullname") String fullname,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("isRemove") boolean isRemove,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("permanentProvince") int permanentProvince,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("identityCard") String identityCard,
            @Param("service") String service,
            @Param("bloodBase") String bloodBase,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("ID") Long ID,
            Pageable pageable);

    /**
     * Danh sách biến động điều trị
     *
     * @author DSNAnh
     * @param isRemove
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param connectStatus
     * @param fullname
     * @param yearOfBirth
     * @param genderID
     * @param identityCard
     * @param healthInsuranceNo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param startTreatmentTime
     * @param endTreatmentTime
     * @param opcStatus
     * @param pageable
     * @param addressFilter
     * @param opcCode
     * @param statusOfTreatmentID
     * @param facility
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.sourceServiceID = '103') "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS '' OR :wardID IS NULL) "
            //            + " AND (e.sourceSiteID IN (SELECT s.ID FROM SiteEntity s WHERE s.provinceID = :sourceSiteID) OR :sourceSiteID IS '' OR :sourceSiteID IS NULL) "
            + " AND ((:connectStatus = 1 AND (e.parentID IS NULL OR e.parentID = 0)) OR (:connectStatus = 2 AND e.parentID IS NOT NULL AND e.parentID <> 0) OR :connectStatus IS '' OR :connectStatus IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:healthInsuranceNo, '%')) OR :healthInsuranceNo IS '' OR :healthInsuranceNo IS NULL) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + "     (:addressFilter = 'hokhau' AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL)) OR"
            + "     (:addressFilter = 'tamtru' AND (e.currentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) AND (e.currentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) AND (e.currentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL)))"
            + " AND (DATE_FORMAT(e.startTreatmentTime, '%Y-%m-%d') >= :startTreatmentTime OR :startTreatmentTime IS NULL) "
            + " AND (DATE_FORMAT(e.startTreatmentTime, '%Y-%m-%d') <= :endTreatmentTime OR :endTreatmentTime IS NULL) "
            + " AND (e.statusOfChangeTreatmentID = :opcStatus OR :opcStatus IS '' OR :opcStatus IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :facility OR :facility IS '' OR :facility IS NULL) "
            + " AND ( :statusOfTreatmentID = '0' AND ( e.statusOfTreatmentID = '' OR e.statusOfTreatmentID IS null OR e.statusOfTreatmentID = '0' OR e.statusOfTreatmentID = '5') OR ( :statusOfTreatmentID <> '0' AND e.statusOfTreatmentID = :statusOfTreatmentID ) OR (   :statusOfTreatmentID = '' OR :statusOfTreatmentID IS NULL ) ) "
            + " AND (e.opcCode = :opcCode OR :opcCode IS '' OR :opcCode IS NULL) "
            + " AND (e.confirmCode = :confirmCode OR :confirmCode IS '' OR :confirmCode IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findChange")
    public Page<PacPatientInfoEntity> findChange(
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            //            @Param("sourceSiteID") String sourceSiteID,
            @Param("connectStatus") Integer connectStatus,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("startTreatmentTime") String startTreatmentTime,
            @Param("endTreatmentTime") String endTreatmentTime,
            @Param("opcStatus") String opcStatus,
            @Param("addressFilter") String addressFilter,
            @Param("facility") String facility,
            @Param("opcCode") String opcCode,
            @Param("confirmCode") String confirmCode,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            Pageable pageable);

    //DSNAnh 10 10 2019
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + " AND (e.statusOfResidentID = :statusOfResidentID OR :statusOfResidentID IS '' OR :statusOfResidentID IS NULL) "
            + " AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findNewExport")
    public Page<PacPatientInfoEntity> findNewExport(
            @Param("isRemove") boolean isRemove,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("statusOfResidentID") String statusOfResidentID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("genderID") String genderID,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            Pageable pageable);

    /**
     * Tìm người nhiễm cần rà soát
     *
     * @author pdThang
     * @param addressFilter
     * @param fullname
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param isRemove
     * @param yearOfBirth
     * @param genderID
     * @param identityCard
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param hasReview
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND ((:hasReview IS NULL AND e.reviewWardTime IS NULL) OR (:hasReview = 1 AND e.reviewWardTime IS NOT NULL)) "
            + " AND ((:hasReview IS NULL AND e.reviewProvinceTime IS NULL) OR (:hasReview = 1 AND e.reviewProvinceTime IS NOT NULL)) "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS '' OR :wardID IS NULL) "
            + " AND (e.ID = :ID OR :ID = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND ((:hasReview = 1 AND e.checkProvinceTime IS NOT NULL AND e.checkWardTime IS NULL AND e.checkDistrictTime IS NULL) OR :hasReview IS NULL) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL ))))"
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findReview")
    public Page<PacPatientInfoEntity> findReview(
            @Param("addressFilter") String addressFilter,
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("hasReview") Integer hasReview,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("ID") Long ID,
            Pageable pageable);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND ( e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.reviewVaacTime is not null ) "
            + " AND (e.provinceID = :provinceID) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND ((e.sourceServiceID = :service ) OR :service IS '' OR :service IS NULL) "
            + " AND ((e.bloodBaseID = :bloodBase ) OR :bloodBase IS '' OR :bloodBase IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    public Page<PacPatientInfoEntity> findVAACNew(
            @Param("provinceID") String provinceID,
            @Param("fullname") String fullname,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("service") String service,
            @Param("bloodBase") String bloodBase,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            Pageable pageable);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND ( e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.reviewVaacTime is not null ) "
            + " AND (e.provinceID = :provinceID) "
    )
    public Long countVAACNew(
            @Param("provinceID") String provinceID);

    /**
     * Trung ương yêu cầu rà soát
     *
     * @param addressFilter
     * @param isRemove
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param fullname
     * @param yearOfBirth
     * @param genderID
     * @param identityCard
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param hasReview
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND ((:hasReview IS NULL AND e.reviewWardTime IS NULL) OR (:hasReview = 1 AND e.reviewWardTime IS NOT NULL)) "
            + " AND ((:hasReview IS NULL AND e.reviewProvinceTime IS NULL) OR (:hasReview = 1 AND e.reviewProvinceTime IS NOT NULL)) "
            + " AND (e.reviewVaacTime IS NOT NULL AND e.reviewVaacTime <> '') "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS '' OR :wardID IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND ((:hasReview = 1 AND e.checkProvinceTime IS NOT NULL AND e.checkWardTime IS NULL AND e.checkDistrictTime IS NULL) OR :hasReview IS NULL) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL ))))"
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findReview")
    public Page<PacPatientInfoEntity> findReviewVaac(
            @Param("addressFilter") String addressFilter,
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("hasReview") Integer hasReview,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            Pageable pageable);

    /**
     * Tìm người nhiễm đã rad soát
     *
     * @author pdThang
     * @param addressFilter
     * @param fullname
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param isRemove
     * @param yearOfBirth
     * @param healthInsuranceNo
     * @param genderID
     * @param identityCard
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param statusOfResidentID
     * @param hasReview
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND ((:hasReview IS NULL AND e.reviewProvinceTime IS NULL) OR (:hasReview = 1 AND e.reviewProvinceTime IS NOT NULL)) "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS '' OR :wardID IS NULL) "
            + " AND (e.ID = :ID OR :ID = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.statusOfResidentID = :statusOfResidentID OR :statusOfResidentID IS '' OR :statusOfResidentID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:healthInsuranceNo, '%')) OR :healthInsuranceNo IS '' OR :healthInsuranceNo IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND ((:hasReview = 1 AND e.checkProvinceTime IS NOT NULL AND e.checkWardTime IS NOT NULL) OR :hasReview IS NULL) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL ))))"
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findAccept")
    public Page<PacPatientInfoEntity> findAccept(
            @Param("addressFilter") String addressFilter,
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("statusOfResidentID") String statusOfResidentID,
            @Param("hasReview") Integer hasReview,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("ID") Long ID,
            Pageable pageable);

    /**
     * pdThang
     *
     * @param isRemove
     * @param ID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param fullname
     * @param yearOfBirth
     * @param genderID
     * @param identityCard
     * @param healthInsuranceNo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param requestDeathTimeFrom
     * @param requestDeathTimeTo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param statusOfResidentID
     * @param statusOfTreatmentID
     * @param dead
     * @param earlyHiv
     * @param hasRequest
     * @param hasReview
     * @param addressFilter
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :provinceID  OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID  OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID  OR :wardID IS '' OR :wardID IS NULL) "
            + " AND (e.ID = :ID  OR :ID = 0) "
            + " AND (e.ID = :hivID  OR :hivID = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND ( e.statusOfResidentID IN (:statusOfResidentID) OR coalesce(:statusOfResidentID, null) IS NULL) "
            + " AND ( :statusOfTreatmentID = '0' AND ( e.statusOfTreatmentID = '' OR e.statusOfTreatmentID IS null OR e.statusOfTreatmentID = '0' OR e.statusOfTreatmentID = '5') OR ( :statusOfTreatmentID <> '0' AND e.statusOfTreatmentID = :statusOfTreatmentID ) OR (   :statusOfTreatmentID = '' OR :statusOfTreatmentID IS NULL ) ) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:healthInsuranceNo, '%')) OR :healthInsuranceNo IS '' OR :healthInsuranceNo IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL OR :confirmTimeFrom = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL OR :confirmTimeTo = '') "
            + " AND ((FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') >= :requestDeathTimeFrom  OR :requestDeathTimeFrom IS NULL) AND (FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') <= :requestDeathTimeTo OR :requestDeathTimeTo IS NULL)) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + "     (:addressFilter = 'hokhau' AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) "
            + " AND (e.permanentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.permanentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL)) OR"
            + "     (:addressFilter = 'tamtru' AND (e.currentProvinceID = :permanentProvinceID OR :permanentProvinceID IS '' OR :permanentProvinceID IS NULL) AND (e.currentDistrictID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) AND (e.currentWardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL)))"
            + " AND ((:dead = 1 AND e.deathTime IS NULL) OR (:dead = 2 AND e.deathTime IS NOT NULL) OR :dead = 0) "
            + " AND (e.earlyHiv = :earlyHiv OR :earlyHiv IS '' OR :earlyHiv IS NULL) "
            + " AND ((:hasRequest = 1 AND e.requestTime IS NOT NULL) OR :hasRequest IS NULL) "
            + " AND ((:hasReview = 1 AND e.checkProvinceTime IS NOT NULL) OR :hasReview IS NULL) "
            + " AND ((:other = 1 AND e.permanentProvinceID <> e.provinceID) OR :other IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID  OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID  OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
    )
    @Cacheable(value = "pac_patient_cache_findPatient")
    public Page<PacPatientInfoEntity> findPatient(
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("requestDeathTimeFrom") String requestDeathTimeFrom,
            @Param("requestDeathTimeTo") String requestDeathTimeTo,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("statusOfResidentID") List<String> statusOfResidentID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("dead") int dead,
            @Param("earlyHiv") String earlyHiv,
            @Param("hasRequest") Integer hasRequest,
            @Param("hasReview") Integer hasReview,
            @Param("other") Integer other,
            @Param("addressFilter") String addressFilter,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("ID") Long ID,
            @Param("hivID") Long hivID,
            Pageable pageable);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :provinceID  OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :identityCard ) OR :identityCard IS '' OR :identityCard IS NULL) "
    )
    public List<PacPatientInfoEntity> findPatientConnectOPC(
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("identityCard") String identityCard);

    /**
     * export partion
     *
     * pdThang
     *
     * @param tab
     * @param from
     * @param to
     * @param provinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param reviewProvinceTimeFrom
     * @param reviewProvinceTimeTo
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.acceptTime IS NOT NULL "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :provinceID) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (DATE_FORMAT(e.reviewProvinceTime, '%Y-%m-%d') >= :reviewProvinceTimeFrom OR :reviewProvinceTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.reviewProvinceTime, '%Y-%m-%d') <= :reviewProvinceTimeTo OR :reviewProvinceTimeTo IS NULL) "
            + " AND (e.districtID = :permanentDistrictID OR :permanentDistrictID IS '' OR :permanentDistrictID IS NULL) "
            + " AND (e.wardID = :permanentWardID OR :permanentWardID IS '' OR :permanentWardID IS NULL) "
            + " AND (( :tab = '1' and e.permanentProvinceID = :provinceID ) OR ( :tab = '2' AND e.permanentProvinceID <> :provinceID ) OR :tab = '0') "
            + " AND ( (DATE_FORMAT(e.updateAt, '%Y-%m-%d') >= :from AND DATE_FORMAT(e.updateAt, '%Y-%m-%d') <= :to  AND e.updateAt IS NOT NULL) OR (DATE_FORMAT(e.createAT, '%Y-%m-%d') >= :from AND DATE_FORMAT(e.createAT, '%Y-%m-%d') <= :to AND e.createAT IS NOT NULL)  )"
    )
    public Page<PacPatientInfoEntity> findExportPatient(
            @Param("tab") String tab,
            @Param("from") String from,
            @Param("to") String to,
            @Param("provinceID") String provinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("reviewProvinceTimeFrom") String reviewProvinceTimeFrom,
            @Param("reviewProvinceTimeTo") String reviewProvinceTimeTo,
            Pageable pageable);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = 0 AND e.acceptTime IS  NULL "
            + " AND (e.reviewWardTime IS  NULL) "
            + " AND (e.reviewProvinceTime IS  NULL) "
            + " AND (e.permanentProvinceID <> :provinceID) "
            + " AND (e.detectProvinceID = :provinceID) "
    )
    public Page<PacPatientInfoEntity> findExportOutProvince(
            @Param("provinceID") String provinceID,
            Pageable pageable);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.acceptTime IS NOT NULL"
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :provinceID) "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " AND (( :tab = '1' and e.permanentProvinceID = :provinceID ) OR ( :tab = '2' AND e.permanentProvinceID <> :provinceID ) OR :tab = '0') "
    )
    public Long countExportPatient(
            @Param("tab") String tab,
            @Param("from") String from,
            @Param("to") String to,
            @Param("provinceID") String provinceID);

    /**
     *
     * @author DSNAnh
     * @param ids
     * @return
     */
    @Override
    @Cacheable(value = "pac_patient_cache_findAllById")
    public List<PacPatientInfoEntity> findAllById(Iterable<Long> ids);

    /**
     * @auth vvThành
     * @param page
     * @return
     */
    @Query(value = "SELECT * FROM pac_patient_info as info WHERE info.latlng_time is null AND info.review_province_time is not null ORDER BY info.updated_at asc", nativeQuery = true)
    public List<PacPatientInfoEntity> getForUpdateLatlng(Pageable page);

    /**
     * Tìm kiếm trùng lắp
     *
     * @param ID
     * @param provinceID
     * @param districtID
     * @auth vvThành
     * @param fullname
     * @param genderID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param identityCard
     * @param healthInsuranceNo
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false AND e.ID != :ID AND e.provinceID = :provinceID "
            + " AND ("
            + "    (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :healthInsuranceNo))"
            + " OR (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :identityCard))"
            + " OR (    e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE :fullname)"
            + "     AND e.genderID = :genderID"
            + "     AND e.permanentProvinceID = :permanentProvinceID AND e.permanentDistrictID = :permanentDistrictID AND e.permanentWardID = :permanentWardID"
            + "     )"
            + " )")
    @Cacheable(value = "pac_patient_cache_duplicateFilter")
    public List<PacPatientInfoEntity> duplicateFilter(
            @Param("ID") Long ID,
            @Param("provinceID") String provinceID,
            @Param("fullname") String fullname,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false AND e.ID != :ID AND e.provinceID = :provinceID "
            + " AND ( e.districtID = :districtID ) "
            + " AND ( (  e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR ( e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL  ) OR ( e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL ) )   "
            + " AND ("
            + "    (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :healthInsuranceNo))"
            + " OR (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :identityCard))"
            + " OR (    e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE :fullname)"
            + "     AND e.genderID = :genderID"
            + "     AND e.permanentProvinceID = :permanentProvinceID AND e.permanentDistrictID = :permanentDistrictID AND e.permanentWardID = :permanentWardID"
            + "     )"
            + " )")
    @Cacheable(value = "pac_patient_cache_duplicateFilterTTYT")
    public List<PacPatientInfoEntity> duplicateFilterTTYT(
            @Param("ID") Long ID,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("fullname") String fullname,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false AND e.provinceID = :provinceID AND YEAR(e.virusLoadArvLastestDate) <= :year ")
    public List<PacPatientInfoEntity> findViralLoads(
            @Param("year") Integer year,
            @Param("provinceID") String provinceID
    );

    /**
     * DSNAnh Xuất danh sách người nhiễm phát hiẹn
     *
     * @param addressFilter
     * @param patientStatus
     * @param dateFilter
     * @param ageFrom
     * @param confirmTimeFrom
     * @param ageTo
     * @param confirmTimeTo
     * @param isRemove
     * @param permanentDistrictID
     * @param permanentWardID
     * @param genderID
     * @param permanentProvinceID
     * @param statusOfResidentID
     * @param statusOfTreatmentID
     * @param pageable
     * @param raceID
     * @param objectGroupID
     * @param modeOfTransmissionID
     * @return list PacPatientInfoEntity
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :permanentProvinceID) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + " (:addressFilter = 'tamtru' AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL ))))"
            + "  AND (:patientStatus IS NULL OR :patientStatus = '' OR (:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> '')))"
            + "  AND (:dateFilter IS NULL OR :dateFilter = '' OR "
            + " (:dateFilter = 'ngayxn' AND (FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom  OR :confirmTimeFrom IS NULL) AND (FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) ) OR "
            + " (:dateFilter = 'chuyenquanly' AND (FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :confirmTimeFrom  OR :confirmTimeFrom IS NULL) AND (FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :confirmTimeTo OR :confirmTimeTo IS NULL))) "
            + " AND (e.genderID IN (:genderID) OR coalesce(:genderID, null) IS NULL) "
            + " AND (e.modeOfTransmissionID IN (:modeOfTransmissionID) OR coalesce(:modeOfTransmissionID, null) IS NULL) "
            + " AND (e.raceID IN (:raceID) OR coalesce(:raceID, null) IS NULL) "
            + " AND (e.objectGroupID IN (:objectGroupID) OR coalesce(:objectGroupID, null) IS NULL) "
            + " AND (e.statusOfResidentID IN (:statusOfResidentID) OR coalesce(:statusOfResidentID, null) IS NULL) "
            + " AND (e.statusOfTreatmentID IN (:statusOfTreatmentID) OR coalesce(:statusOfTreatmentID, null) IS NULL) "
            + " AND (((e.confirmTime IS NOT NULL AND e.confirmTime <> '') AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.confirmTime) - e.yearOfBirth >= :ageFrom ) OR :ageFrom IS NULL OR :ageFrom = '')"
            + " AND (((e.confirmTime IS NOT NULL AND e.confirmTime <> '') AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.confirmTime) - e.yearOfBirth <= :ageTo ) OR :ageTo IS NULL OR :ageTo = '')"
    )
    public Page<PacPatientInfoEntity> findNewExport(
            @Param("addressFilter") String addressFilter,
            @Param("patientStatus") String patientStatus,
            @Param("dateFilter") String dateFilter,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("isRemove") boolean isRemove,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("genderID") List<String> genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("statusOfResidentID") List<String> statusOfResidentID,
            @Param("statusOfTreatmentID") List<String> statusOfTreatmentID,
            @Param("raceID") List<String> raceID,
            @Param("modeOfTransmissionID") List<String> modeOfTransmissionID,
            @Param("objectGroupID") List<String> objectGroupID,
            Pageable pageable);

    /**
     *
     * @param code
     * @param name
     * @param addressFilter
     * @param blood
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param confirmTimeFrom
     * @param inputTimeFrom
     * @param requestDeathTimeFrom
     * @param inputTimeTo
     * @param requestDeathTimeTo
     * @param deathTimeFrom
     * @param deathTimeTo
     * @param confirmTimeTo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param genderID
     * @param raceID
     * @param modeOfTransmissionID
     * @param objectGroupID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param ageFrom
     * @param ageTo
     * @param isTTYT
     * @param isTYT
     * @param placeTest
     * @param facility
     * @param manageStatus
     * @param isVAAC
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.deathTime IS NOT NULL) "
            + " AND ((((:manageStatus = '-1' AND :isTTYT = FALSE AND :isTYT = FALSE) OR (:manageStatus = '-1' AND (:isTTYT = TRUE OR :isTYT = TRUE) AND (e.sourceServiceID = '103' OR e.acceptTime IS NOT NULL))) AND ( "
            + " (e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID))) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL) "
            + " )) OR ((:manageStatus = '1' AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID)) AND :isTTYT = FALSE AND :isTYT = FALSE) OR "
            + " (:manageStatus = '2' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (:manageStatus = '3' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR"
            + " (:manageStatus = '4' AND :isVAAC IS FALSE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL AND e.provinceID = :provinceID)) OR (:isVAAC IS TRUE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL )) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )))) "
            + " AND ((FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom  OR :confirmTimeFrom IS NULL) AND (FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') >= :requestDeathTimeFrom  OR :requestDeathTimeFrom IS NULL) AND (FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') <= :requestDeathTimeTo OR :requestDeathTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.deathTime, '%Y-%m-%d') >= :deathTimeFrom  OR :deathTimeFrom IS NULL) AND (FUNCTION('date_format', e.deathTime, '%Y-%m-%d') <= :deathTimeTo OR :deathTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.createAT, '%Y-%m-%d') >= :inputTimeFrom  OR :inputTimeFrom IS NULL) AND (FUNCTION('date_format', e.createAT, '%Y-%m-%d') <=  :inputTimeTo OR :inputTimeTo IS NULL))"
            + " AND ((FUNCTION('date_format', e.updateAt, '%Y-%m-%d') >= :updateTimeFrom  OR :updateTimeFrom IS NULL) AND (FUNCTION('date_format', e.updateAt, '%Y-%m-%d') <=  :updateTimeTo OR :updateTimeTo IS NULL))"
            + " AND (e.genderID IN (:genderID) OR coalesce(:genderID, null) IS NULL) "
            + " AND (e.modeOfTransmissionID IN (:modeOfTransmissionID) OR coalesce(:modeOfTransmissionID, null) IS NULL) "
            + " AND (e.raceID IN (:raceID) OR coalesce(:raceID, null) IS NULL) "
            + " AND (e.objectGroupID IN (:objectGroupID) OR coalesce(:objectGroupID, null) IS NULL) "
            + " AND (((e.deathTime <> '' AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.deathTime) - e.yearOfBirth >= :ageFrom) OR :ageFrom IS NULL OR :ageFrom = '') "
            + " AND ((e.deathTime <> '' AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.deathTime) - e.yearOfBirth <= :ageTo) OR :ageTo IS NULL OR :ageTo = '')) "
            + " AND ((e.provinceID = :provinceID AND :isVAAC IS FALSE) OR :isVAAC IS TRUE )"
            + " AND (e.districtID = :districtID OR :districtID IS NULL) "
            + " AND (e.ID = :code OR :code = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:name, '%')) OR :name IS '' OR :name IS NULL) "
            + " AND (e.bloodBaseID = :blood OR :blood IS '' OR :blood IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS NULL) "
            + " AND (e.siteConfirmID = :placeTest OR :placeTest IS NULL OR :placeTest = '') "
            + " AND (e.siteTreatmentFacilityID = :facility OR :facility IS NULL OR :facility = '') "
    )
    public Page<PacPatientInfoEntity> findDead(
            @Param("code") Long code,
            @Param("name") String name,
            @Param("blood") String blood,
            @Param("addressFilter") String addressFilter,
            @Param("updateTimeFrom") String updateTimeFrom,
            @Param("updateTimeTo") String updateTimeTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("requestDeathTimeFrom") String requestDeathTimeFrom,
            @Param("requestDeathTimeTo") String requestDeathTimeTo,
            @Param("inputTimeFrom") String inputTimeFrom,
            @Param("inputTimeTo") String inputTimeTo,
            @Param("deathTimeFrom") String deathTimeFrom,
            @Param("deathTimeTo") String deathTimeTo,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("genderID") List<String> genderID,
            @Param("raceID") List<String> raceID,
            @Param("modeOfTransmissionID") List<String> modeOfTransmissionID,
            @Param("objectGroupID") List<String> objectGroupID,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo,
            @Param("manageStatus") String manageStatus,
            @Param("isVAAC") boolean isVAAC,
            @Param("isTTYT") boolean isTTYT,
            @Param("isTYT") boolean isTYT,
            @Param("placeTest") String placeTest,
            @Param("facility") String facility,
            Pageable pageable);

    /**
     *
     * @param code
     * @param addressFilter
     * @param blood
     * @param name
     * @param updateTimeFrom
     * @param confirmTimeFrom
     * @param updateTimeTo
     * @param inputTimeFrom
     * @param aidsFrom
     * @param inputTimeTo
     * @param aidsTo
     * @param deathTimeFrom
     * @param deathTimeTo
     * @param confirmTimeTo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param genderID
     * @param raceID
     * @param modeOfTransmissionID
     * @param objectGroupID
     * @param aliveID
     * @param residentID
     * @param treatmentID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param ageFrom
     * @param ageTo
     * @param ageCurrentFrom
     * @param ageCurrentTo
     * @param manageStatus
     * @param isVAAC
     * @param isPAC
     * @param placeTest
     * @param pageable
     * @param facility
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND ((:manageStatus = '-1' AND ( "
            + " ( :isPAC IS TRUE AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID))) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL) "
            + " )) OR ((:manageStatus = '1' AND :isPAC IS TRUE AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID))) OR "
            + " (:manageStatus = '2' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (:manageStatus = '3' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR"
            + " (:manageStatus = '4' AND :isVAAC IS FALSE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL AND e.provinceID = :provinceID)) OR (:isVAAC IS TRUE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL )) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )))) "
            + " AND ((FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom  OR :confirmTimeFrom IS NULL) AND (FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :deathTimeFrom  OR :deathTimeFrom IS NULL) AND (FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <= :deathTimeTo OR :deathTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.createAT, '%Y-%m-%d') >= :inputTimeFrom  OR :inputTimeFrom IS NULL) AND (FUNCTION('date_format', e.createAT, '%Y-%m-%d') <=  :inputTimeTo OR :inputTimeTo IS NULL))"
            + " AND ((FUNCTION('date_format', e.updateAt, '%Y-%m-%d') >= :updateTimeFrom  OR :updateTimeFrom IS NULL) AND (FUNCTION('date_format', e.updateAt, '%Y-%m-%d') <=  :updateTimeTo OR :updateTimeTo IS NULL))"
            + " AND (( FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') >= :aidsFrom OR :aidsFrom IS NULL OR :aidsFrom = '') AND (FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') <= :aidsTo OR :aidsTo IS NULL OR :aidsTo = ''))  "
            + " AND (e.genderID IN (:genderID) OR coalesce(:genderID, null) IS NULL) "
            + " AND (e.modeOfTransmissionID IN (:modeOfTransmissionID) OR coalesce(:modeOfTransmissionID, null) IS NULL) "
            + " AND ((:aliveID = '1' AND e.deathTime is NULL) OR (:aliveID = '2' AND e.deathTime is NOT NULL) OR :aliveID is NULL) "
            + " AND (e.raceID IN (:raceID) OR coalesce(:raceID, null) IS NULL) "
            + " AND ( :rule = '1' AND (e.statusOfTreatmentID IN (:treatmentID) OR e.statusOfTreatmentID is null OR e.statusOfTreatmentID = '' OR e.statusOfTreatmentID = '5') OR ( :rule = '0' AND  e.statusOfTreatmentID IN (:treatmentID)) OR coalesce(:treatmentID, null) IS NULL) "
            + " AND (e.statusOfResidentID IN (:residentID) OR coalesce(:residentID, null) IS NULL) "
            + " AND (e.objectGroupID IN (:objectGroupID) OR coalesce(:objectGroupID, null) IS NULL) "
            + " AND (((e.confirmTime IS NOT NULL AND e.confirmTime <> '') AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.confirmTime) - e.yearOfBirth >= :ageFrom ) OR :ageFrom IS NULL OR :ageFrom = '')"
            + " AND (((e.confirmTime IS NOT NULL AND e.confirmTime <> '') AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.confirmTime) - e.yearOfBirth <= :ageTo ) OR :ageTo IS NULL OR :ageTo = '')"
            + " AND (((e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(CURDATE()) - e.yearOfBirth >= :ageCurrentFrom ) OR :ageCurrentFrom IS NULL OR :ageCurrentFrom = '')"
            + " AND (((e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(CURDATE()) - e.yearOfBirth <= :ageCurrentTo ) OR :ageCurrentTo IS NULL OR :ageCurrentTo = '')"
            + " AND ((e.provinceID = :provinceID AND :isVAAC IS FALSE) OR :isVAAC IS TRUE )"
            + " AND (e.districtID = :districtID OR :districtID IS NULL) "
            + " AND (e.ID = :code OR :code = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:name, '%')) OR :name IS '' OR :name IS NULL) "
            + " AND (e.bloodBaseID = :blood OR :blood IS '' OR :blood IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS NULL) "
            + " AND (e.siteConfirmID = :placeTest OR :placeTest IS NULL OR :placeTest = '') "
            + " AND (e.siteTreatmentFacilityID = :facility OR :facility IS NULL OR :facility = '') "
    )
    public Page<PacPatientInfoEntity> findNewExports(
            @Param("code") Long code,
            @Param("name") String name,
            @Param("blood") String blood,
            @Param("addressFilter") String addressFilter,
            @Param("updateTimeFrom") String updateTimeFrom,
            @Param("updateTimeTo") String updateTimeTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("aidsFrom") String aidsFrom,
            @Param("aidsTo") String aidsTo,
            @Param("inputTimeFrom") String inputTimeFrom,
            @Param("inputTimeTo") String inputTimeTo,
            @Param("deathTimeFrom") String deathTimeFrom,//ngày quản lý
            @Param("deathTimeTo") String deathTimeTo,//ngày quản lý
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("genderID") List<String> genderID,
            @Param("raceID") List<String> raceID,
            @Param("modeOfTransmissionID") List<String> modeOfTransmissionID,
            @Param("objectGroupID") List<String> objectGroupID,
            @Param("aliveID") String aliveID,
            @Param("residentID") List<String> residentID,
            @Param("treatmentID") List<String> treatmentID,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo,
            @Param("ageCurrentFrom") Integer ageCurrentFrom,
            @Param("ageCurrentTo") Integer ageCurrentTo,
            @Param("manageStatus") String manageStatus,
            @Param("isVAAC") boolean isVAAC,
            @Param("isPAC") boolean isPAC,
            @Param("placeTest") String placeTest, // Nơi xét nghiệm khẳng định
            @Param("facility") String facility, // Nới điều trị
            @Param("rule") String rule,
            Pageable pageable);

    @Override
//    @Cacheable(value = "pac_patient_cache_findById")
    public Optional<PacPatientInfoEntity> findById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_patient_cache_findNew", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findChange", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findNewExport", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findReview", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAccept", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findPatient", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAllById", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_duplicateFilter", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findById", allEntries = true)
    })
    public void delete(PacPatientInfoEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_patient_cache_findNew", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findChange", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findNewExport", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findReview", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAccept", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findPatient", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAllById", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_duplicateFilter", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findById", allEntries = true)
    })
    public void deleteById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_patient_cache_findNew", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findChange", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findNewExport", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findReview", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAccept", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findPatient", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAllById", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_duplicateFilter", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findById", allEntries = true)
    })
    public <S extends PacPatientInfoEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_patient_cache_findNew", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findChange", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findNewExport", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findReview", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAccept", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findPatient", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findAllById", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_duplicateFilter", allEntries = true),
        @CacheEvict(value = "pac_patient_cache_findById", allEntries = true)
    })
    public <S extends PacPatientInfoEntity> S save(S s);

    @Query("SELECT e FROM PacPatientInfoEntity e WHERE e.provinceID = :provinceID AND confirmTime is not null  AND e.remove = false")
    public Page<PacPatientInfoEntity> getFirst(@Param("provinceID") String provinceID, Pageable pageable);

    @Query("SELECT e FROM PacPatientInfoEntity e WHERE confirmTime is not null  AND e.remove = false")
    public Page<PacPatientInfoEntity> getFirst(Pageable pageable);

    @Query("SELECT e FROM PacPatientInfoEntity e WHERE e.aidsStatusDate is not null  AND e.remove = false")
    public Page<PacPatientInfoEntity> getFirstAids(Pageable pageable);

    /**
     *
     * @author DSNAnh
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM PacPatientInfoEntity e WHERE e.provinceID = :provinceID AND e.districtID = :districtID AND e.wardID = :wardID AND confirmTime IS NOT NULL AND e.remove = FALSE")
    public Page<PacPatientInfoEntity> getFirst(@Param("provinceID") String provinceID, @Param("districtID") String districtID, @Param("wardID") String wardID, Pageable pageable);

    /**
     * Tìm ngày tử vong nhỏ nhất thỏa mãn tiêu chí tìm kiếm BC tử vong
     *
     * @param aidsFrom
     * @param aidsTo
     * @param requestDeathTimeFrom
     * @param requestDeathTimeTo
     * @param updateTimeFrom
     * @param updateTimeTo
     * @DSNAnh
     * @param isTTYT
     * @param isTYT
     * @param manageStatus
     * @param addressFilter
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param deathTimeFrom
     * @param deathTimeTo
     * @param ageFrom
     * @param ageTo
     * @param job
     * @param gender
     * @param testObject
     * @param statusResident
     * @param statusTreatment
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM PacPatientInfoEntity e WHERE e.remove = FALSE "
            + "AND e.deathTime IS NOT NULL "
            + "AND ( "
            + "        ( "
            + "            ((:manageStatus = '-1' AND :isTTYT = FALSE AND :isTYT = FALSE) OR (:manageStatus = '-1' AND (:isTTYT = TRUE OR :isTYT = TRUE) AND (e.sourceServiceID = '103' OR e.acceptTime IS NOT NULL))) "
            + "            AND ( "
            + "               (e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID) "
            + "               OR (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL) "
            + "                OR (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL) "
            + "                OR (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL) "
            + "                OR (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL) "
            + "            ) "
            + "        ) "
            + "        OR (:manageStatus = '1' AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND :isTTYT = FALSE AND :isTYT = FALSE) "
            + "        OR (:manageStatus = '2' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL) "
            + "        OR (:manageStatus = '3' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL)"
            + "        OR (:manageStatus = '4' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL)      "
            + "    ) "
            + "    AND (e.jobID = :job OR :job IS NULL OR :job = '' ) "
            + "    AND (e.genderID = :gender OR :gender IS NULL OR :gender = '' ) "
            + "    AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' ) "
            + "    AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' ) "
            + "    AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' ) "
            + "    AND (( FUNCTION('date_format', e.deathTime, '%Y-%m-%d') >= :deathTimeFrom OR :deathTimeFrom IS NULL OR :deathTimeFrom = '') AND (FUNCTION('date_format', e.deathTime, '%Y-%m-%d') <= :deathTimeTo OR :deathTimeTo IS NULL OR :deathTimeTo = ''))  "
            + "    AND (( FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') >= :aidsFrom OR :aidsFrom IS NULL OR :aidsFrom = '') AND (FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') <= :aidsTo OR :aidsTo IS NULL OR :aidsTo = ''))  "
            + "    AND (( FUNCTION('date_format', e.updateAt, '%Y-%m-%d') >= :updateTimeFrom OR :updateTimeFrom IS NULL OR :updateTimeFrom = '') AND (FUNCTION('date_format', e.updateAt, '%Y-%m-%d') <= :updateTimeTo OR :updateTimeTo IS NULL OR :updateTimeTo = ''))  "
            + "    AND ((FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') >= :requestDeathTimeFrom  OR :requestDeathTimeFrom IS NULL) AND (FUNCTION('date_format', e.requestDeathTime, '%Y-%m-%d') <= :requestDeathTimeTo OR :requestDeathTimeTo IS NULL)) "
            + "    AND ((YEAR(e.deathTime) - e.yearOfBirth) >= :ageFrom OR :ageFrom IS NULL OR  :ageFrom = '')  "
            + "    AND ((YEAR(e.deathTime) - e.yearOfBirth) <= :ageTo OR :ageTo IS NULL OR  :ageTo = '') "
            + "    AND (e.provinceID = :provinceID OR :provinceID is null) "
            + "    AND (e.districtID = :districtID OR :districtID is null) "
            + "    AND (e.wardID = :wardID OR :wardID is null)   "
            + "    AND ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL) AND (e.permanentDistrictID IN (:permanentDistrictID)  OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL)) "
            + "    OR (:addressFilter <> 'hokhau' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL) AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL)) ) "
    )
    public Page<PacPatientInfoEntity> getFirstDead(
            @Param("isTTYT") boolean isTTYT,
            @Param("isTYT") boolean isTYT,
            @Param("manageStatus") String manageStatus,
            @Param("addressFilter") String addressFilter,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("deathTimeFrom") String deathTimeFrom,
            @Param("deathTimeTo") String deathTimeTo,
            @Param("aidsFrom") String aidsFrom,
            @Param("aidsTo") String aidsTo,
            @Param("requestDeathTimeFrom") String requestDeathTimeFrom,
            @Param("requestDeathTimeTo") String requestDeathTimeTo,
            @Param("updateTimeFrom") String updateTimeFrom,
            @Param("updateTimeTo") String updateTimeTo,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo,
            @Param("job") String job,
            @Param("gender") String gender,
            @Param("testObject") String testObject,
            @Param("statusResident") String statusResident,
            @Param("statusTreatment") String statusTreatment,
            Pageable pageable);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (:permanentDistrictID IS NULL OR :permanentDistrictID IS '' OR e.permanentDistrictID = :permanentDistrictID ) "
            + " AND (:permanentWardID IS NULL OR :permanentWardID IS '' OR e.permanentWardID = :permanentWardID) "
            + " AND e.permanentProvinceID = :permanentProvinceID "
            + " AND e.provinceID = :permanentProvinceID "
            + " AND (e.acceptTime IS NOT NULL OR e.acceptTime <> '') "
            + " AND (e.reviewWardTime IS NOT NULL OR e.reviewWardTime <> '') "
            + " AND (e.reviewProvinceTime IS NOT NULL OR e.reviewProvinceTime <> '') "
            + " AND ((:alive = '1' AND e.deathTime IS NULL AND e.confirmTime >= :confirmTimeFrom) OR :confirmTimeFrom IS NULL OR :confirmTimeFrom = '' OR (:alive <> '1' AND :alive IS NOT NULL)) "
            + " AND ((:alive = '1' AND e.deathTime IS NULL AND e.confirmTime <= :confirmTimeTo) OR :confirmTimeTo IS NULL OR :confirmTimeTo = '' OR (:alive <> '1' AND :alive IS NOT NULL)) "
            + " AND ((:alive = '0' AND e.deathTime IS NOT NULL AND e.deathTime >= :confirmTimeFrom) OR :confirmTimeFrom IS NULL OR :confirmTimeFrom = '' OR (:alive <> '0' AND :alive IS NOT NULL)) "
            + " AND ((:alive = '0' AND e.deathTime IS NOT NULL AND e.deathTime <= :confirmTimeTo) OR :confirmTimeTo IS NULL OR :confirmTimeTo = '' OR (:alive <> '0' AND :alive IS NOT NULL)) "
            + " AND (((:alive = '' OR :alive IS NULL) AND e.deathTime IS NULL AND e.confirmTime >= :confirmTimeFrom) OR (:alive <> '' AND :alive IS NOT NULL OR e.deathTime IS NOT NULL)) "
            + " AND (((:alive = '' OR :alive IS NULL) AND e.deathTime IS NULL AND e.confirmTime <= :confirmTimeTo) OR (:alive <> '' AND :alive IS NOT NULL OR e.deathTime IS NOT NULL))  "
            + " AND (((:alive = '' OR :alive IS NULL) AND e.deathTime IS NOT NULL AND e.deathTime >= :confirmTimeFrom) OR (:alive <> '' AND :alive IS NOT NULL OR e.deathTime IS NULL)) "
            + " AND (((:alive = '' OR :alive IS NULL) AND e.deathTime IS NOT NULL AND e.deathTime <= :confirmTimeTo) OR (:alive <> '' AND :alive IS NOT NULL OR e.deathTime IS NULL)) "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findLocalPatient(
            @Param("confirmTimeFrom") Date fromTime,
            @Param("confirmTimeTo") Date toTime,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("alive") String alive
    );

    /**
     * Báo cáo nhiễm mới
     *
     * @auth TrangBN
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param bloodBaseID
     * @param objectGroupID
     * @param genderID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param statusOfTreatmentID
     * @param statusOfResidentID
     * @param yearOld
     * @param jobID
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (:permanentDistrictID IS NULL OR :permanentDistrictID IS '' OR e.permanentDistrictID = :permanentDistrictID ) "
            + " AND (:permanentWardID IS NULL OR :permanentWardID IS '' OR e.permanentWardID = :permanentWardID) "
            + " AND e.permanentProvinceID = :permanentProvinceID "
            + " AND e.provinceID = :permanentProvinceID "
            + " AND (e.acceptTime IS NOT NULL OR e.acceptTime <> '') "
            + " AND (e.reviewWardTime IS NOT NULL OR e.reviewWardTime <> '') "
            + " AND (e.reviewProvinceTime IS NOT NULL OR e.reviewProvinceTime <> '') "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.jobID = :jobID OR :jobID IS '' OR :jobID IS NULL) "
            + " AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID IS NULL) "
            + " AND (e.bloodBaseID = :bloodBaseID OR :bloodBaseID IS '' OR :bloodBaseID IS NULL) "
            + " AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + " AND (e.statusOfResidentID = :statusOfResidentID OR :statusOfResidentID IS '' OR :statusOfResidentID IS NULL) "
            + " AND (e.confirmTime >= :confirmTimeFrom OR :confirmTimeFrom IS NULL OR :confirmTimeFrom IS '' ) "
            + " AND (e.confirmTime <= :confirmTimeTo OR :confirmTimeTo IS NULL OR :confirmTimeTo IS '' ) "
            + " AND (:yearOld IS NULL OR :yearOld IS '' OR (:yearOld = '1' AND YEAR(CURRENT_DATE) - e.yearOfBirth < 15) OR :yearOld <> '1') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '2' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 15 AND YEAR(CURRENT_DATE) - e.yearOfBirth <= 25 )) OR :yearOld <> '2') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '3' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 26 AND YEAR(CURRENT_DATE) - e.yearOfBirth <= 49 )) OR :yearOld <> '3') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '4' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 50)) OR :yearOld <> '4') "
    )
    public List<PacPatientInfoEntity> findEarlyHivPatient(
            @Param("confirmTimeFrom") Date confirmTimeFrom,
            @Param("confirmTimeTo") Date confirmTimeTo,
            @Param("bloodBaseID") String bloodBaseID,
            @Param("objectGroupID") String objectGroupID,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("statusOfResidentID") String statusOfResidentID,
            @Param("yearOld") String yearOld,
            @Param("jobID") String jobID
    );

    /**
     * Click search bệnh nhân tử vong
     *
     * @auth TrangBN
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param bloodBaseID
     * @param objectGroupID
     * @param genderID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param statusOfTreatmentID
     * @param statusOfResidentID
     * @param yearOld
     * @param jobID
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false AND e.deathTime IS NOT NULL"
            + " AND (:permanentDistrictID IS NULL OR :permanentDistrictID IS '' OR e.permanentDistrictID = :permanentDistrictID ) "
            + " AND (:permanentWardID IS NULL OR :permanentWardID IS '' OR e.permanentWardID = :permanentWardID) "
            + " AND e.permanentProvinceID = :permanentProvinceID "
            + " AND e.provinceID = :permanentProvinceID "
            + " AND (e.acceptTime IS NOT NULL OR e.acceptTime <> '') "
            + " AND (e.reviewWardTime IS NOT NULL OR e.reviewWardTime <> '') "
            + " AND (e.reviewProvinceTime IS NOT NULL OR e.reviewProvinceTime <> '') "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.jobID = :jobID OR :jobID IS '' OR :jobID IS NULL) "
            + " AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID IS NULL) "
            + " AND (e.bloodBaseID = :bloodBaseID OR :bloodBaseID IS '' OR :bloodBaseID IS NULL) "
            + " AND (e.statusOfTreatmentID = :statusOfTreatmentID OR :statusOfTreatmentID IS '' OR :statusOfTreatmentID IS NULL) "
            + " AND (e.statusOfResidentID = :statusOfResidentID OR :statusOfResidentID IS '' OR :statusOfResidentID IS NULL) "
            + " AND (e.deathTime >= :confirmTimeFrom OR :confirmTimeFrom IS NULL OR :confirmTimeFrom IS '' ) "
            + " AND (e.deathTime <= :confirmTimeTo OR :confirmTimeTo IS NULL OR :confirmTimeTo IS '' ) "
            + " AND (:yearOld IS NULL OR :yearOld IS '' OR (:yearOld = '1' AND YEAR(CURRENT_DATE) - e.yearOfBirth < 15) OR :yearOld <> '1') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '2' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 15 AND YEAR(CURRENT_DATE) - e.yearOfBirth <= 25 )) OR :yearOld <> '2') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '3' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 26 AND YEAR(CURRENT_DATE) - e.yearOfBirth <= 49 )) OR :yearOld <> '3') "
            + " AND ((:yearOld IS NULL OR :yearOld IS '') OR (:yearOld = '4' AND ( YEAR(CURRENT_DATE) - e.yearOfBirth >= 50)) OR :yearOld <> '4') "
            + "ORDER BY e.deathTime ASC"
    )
    public List<PacPatientInfoEntity> findPatientDeadHiv(
            @Param("confirmTimeFrom") Date confirmTimeFrom,
            @Param("confirmTimeTo") Date confirmTimeTo,
            @Param("bloodBaseID") String bloodBaseID,
            @Param("objectGroupID") String objectGroupID,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("statusOfTreatmentID") String statusOfTreatmentID,
            @Param("statusOfResidentID") String statusOfResidentID,
            @Param("yearOld") String yearOld,
            @Param("jobID") String jobID
    );

    @Query("SELECT e"
            + "    FROM PacPatientInfoEntity e"
            + "    WHERE"
            + "        e.remove = false"
            + "        AND e.provinceID = :provinceID"
            + "        AND e.acceptTime IS NOT NULL"
            + "        AND e.reviewWardTime IS NOT NULL"
            + "        AND e.reviewProvinceTime IS NOT NULL"
            + "        AND (:addressFilter IS NULL OR :addressFilter = '' OR"
            + " ((:addressFilter = 'hokhau' AND (e.permanentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' ) AND (e.permanentWardID = :wardID OR :wardID IS NULL OR :wardID = '' )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' )  AND (e.currentWardID = :wardID OR :wardID IS NULL OR :wardID = '' ))))"
            + "        AND (:patientStatus IS NULL OR :patientStatus = '' OR (:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> '')))"
            + "        AND (:dateFilter IS NULL OR :dateFilter = '' OR (:dateFilter = 'ngayxn' AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :toDate ) OR (:dateFilter = 'chuyenquanly' AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :toDate)) "
            + "        AND (e.jobID = :job OR :job IS NULL OR :job = '' )"
            + "        AND ((:flag = '3' AND (e.objectGroupID IS NULL OR e.objectGroupID = '')) OR (:flag IN ('1', '2') AND e.objectGroupID IS NOT NULL AND e.objectGroupID <> '') OR :flag IS NULL OR :flag = '')"
            + "        AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' )"
            + "        AND (e.objectGroupID IN (:testObjects) OR coalesce(:testObjects, null) IS NULL) "
            + "        AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' )"
            + "        AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' )"
            + "        AND (:otherProvinceID IS NULL OR :otherProvinceID = '' OR (:addressFilter = 'hokhau' AND e.permanentProvinceID = :otherProvinceID) OR (:addressFilter = 'tamtru' AND e.currentProvinceID = :otherProvinceID)) "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findPacDetectHivObject(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("testObject") String testObject,
            @Param("testObjects") Set<String> testObjects,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("job") String job,
            @Param("addressFilter") String addressFilter,
            @Param("dateFilter") String dateFilter,
            @Param("patientStatus") String patientStatus,
            @Param("provinceID") String provinceID,
            @Param("otherProvinceID") String otherProvinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("flag") String flag
    );

    /**
     * Lấy thông tin khách hàng phát hiện theo giới tính khi click
     *
     * @auth TrangBN
     * @param fromTime
     * @param toTime
     * @param addressType
     * @param patientStatus
     * @param searchTime
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param job
     * @param testObject
     * @param statusTreatment
     * @param statusResident
     * @param gender
     * @param otherProvinceID
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (:provinceID = e.provinceID) "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (:patientStatus IS NULL OR :patientStatus = '' OR ((:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> '')))) "
            + " AND (:searchTime IS NULL OR :searchTime = '' OR (:searchTime = 'ngayxn' AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :fromTime AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :toTime ) OR (:searchTime = 'chuyenquanly' AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :fromTime AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :toTime)) "
            + " AND (e.jobID = :job OR :job IS NULL OR :job = '' ) "
            + " AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' ) "
            + " AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' ) "
            + " AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' ) "
            + " AND (e.genderID = :gender OR :gender IS NULL OR :gender = '' ) "
            + " AND (e.genderID IN ('1','2')) "
            + " AND (:addressType IS NULL OR :addressType = '' OR ((:addressType = 'hokhau' AND e.permanentDistrictID = :districtID) OR (:addressType = 'tamtru' AND e.currentDistrictID = :districtID)) OR :districtID = '' OR :districtID IS NULL) "
            + " AND (:addressType IS NULL OR :addressType = '' OR ((:addressType = 'hokhau' AND e.permanentWardID = :wardID) OR (:addressType = 'tamtru' AND e.currentWardID = :wardID)) OR :wardID = '' OR :wardID IS NULL) "
            + " AND (:otherProvinceID IS NULL OR :otherProvinceID = '' OR (:addressType = 'hokhau' AND e.permanentProvinceID = :otherProvinceID) OR (:addressType = 'tamtru' AND e.currentProvinceID = :otherProvinceID)) "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findDetectHivGender(
            @Param("fromTime") String fromTime,
            @Param("toTime") String toTime,
            @Param("addressType") String addressType,
            @Param("patientStatus") String patientStatus,
            @Param("searchTime") String searchTime,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("job") String job,
            @Param("testObject") String testObject,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("gender") String gender,
            @Param("otherProvinceID") String otherProvinceID
    );

    @Query(""
            + "    SELECT e"
            + "    FROM PacPatientInfoEntity e"
            + "    WHERE"
            + "        e.remove = false"
            + "        AND e.provinceID = :provinceID"
            + "        AND e.acceptTime IS NOT NULL"
            + "        AND e.reviewWardTime IS NOT NULL"
            + "        AND e.reviewProvinceTime IS NOT NULL"
            + "        AND (:addressFilter IS NULL OR :addressFilter = '' OR"
            + " ((:addressFilter = 'hokhau' AND (e.permanentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' ) AND (e.permanentWardID = :wardID OR :wardID IS NULL OR :wardID = '' )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' )  AND (e.currentWardID = :wardID OR :wardID IS NULL OR :wardID = '' ))))"
            + "        AND (:patientStatus IS NULL OR :patientStatus = '' OR ((:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> ''))))"
            + "        AND (:dateFilter IS NULL OR :dateFilter = '' OR (:dateFilter = 'ngayxn' AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :toDate ) OR (:dateFilter = 'chuyenquanly' AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :toDate)) "
            + "        AND (e.jobID = :job OR :job IS NULL OR :job = '' )"
            + "        AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' )"
            + "        AND ((:flag = '1' AND e.modeOfTransmissionID IN :modeOfTransmisson ) OR (:flag = '2' AND (e.modeOfTransmissionID IS NULL OR e.modeOfTransmissionID = '')) OR :flag IS NULL OR :flag = '' )"
            + "        AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' )"
            + "        AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' )"
            + "        AND (:otherProvinceID IS NULL OR :otherProvinceID = '' OR (:addressFilter = 'hokhau' AND e.permanentProvinceID = :otherProvinceID) OR (:addressFilter = 'tamtru' AND e.currentProvinceID = :otherProvinceID)) "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findPacDetectHivTransmisson(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("modeOfTransmisson") Set<String> modeOfTransmisson,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("job") String job,
            @Param("testObject") String testObject,
            @Param("addressFilter") String addressFilter,
            @Param("dateFilter") String dateFilter,
            @Param("patientStatus") String patientStatus,
            @Param("provinceID") String provinceID,
            @Param("otherProvinceID") String otherProvinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("flag") String flag
    );

    @Query("SELECT e"
            + "    FROM PacPatientInfoEntity e"
            + "    WHERE"
            + "        e.remove = false"
            + "        AND e.provinceID = :provinceID"
            + "        AND e.acceptTime IS NOT NULL"
            + "        AND e.reviewWardTime IS NOT NULL"
            + "        AND e.reviewProvinceTime IS NOT NULL"
            + "        AND (:addressFilter IS NULL OR :addressFilter = '' OR"
            + "         ((:addressFilter = 'hokhau' AND (e.permanentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' ) AND (e.permanentWardID = :wardID OR :wardID IS NULL OR :wardID = '' )) OR "
            + "         (:addressFilter = 'tamtru' AND (e.currentDistrictID = :districtID OR :districtID IS NULL OR :districtID = '' )  AND (e.currentWardID = :wardID OR :wardID IS NULL OR :wardID = '' ))))"
            + "        AND (:patientStatus IS NULL OR :patientStatus = '' OR ((:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> ''))))"
            + "        AND (:dateFilter IS NULL OR :dateFilter = '' OR "
            + "         (:dateFilter = 'ngayxn' AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :toDate ) OR "
            + "         (:dateFilter = 'chuyenquanly' AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :fromDate AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :toDate))"
            + "        AND (e.jobID = :job OR :job IS NULL OR :job = '' )"
            + "        AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' )"
            + "        AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' )"
            + "        AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' )"
            + "        AND (:otherProvinceID IS NULL OR :otherProvinceID = '' OR (:addressFilter = 'hokhau' AND e.permanentProvinceID = :otherProvinceID) OR (:addressFilter = 'tamtru' AND e.currentProvinceID = :otherProvinceID)) "
            + "        AND ((:flag = '1' AND (e.statusOfTreatmentID IS NULL OR e.statusOfTreatmentID = '' OR e.statusOfTreatmentID = '5')) OR "
            + "             (:flag = '3' AND (e.statusOfResidentID IS NULL OR e.statusOfResidentID = '')) OR "
            + "             (:flag = '4' AND (e.siteIDTF IS NULL OR e.siteIDTF = '' OR (e.siteIDTF IN :siteLocals OR coalesce(:siteLocals, null) IS NULL))) OR "
            + "             (:flag = '5' AND (e.siteIDTF IS NOT NULL AND e.siteIDTF <> '' AND (e.siteIDTF NOT IN :siteLocals OR coalesce(:siteLocals, null) IS NULL))) OR "
            + " :flag IS NULL OR :flag = '' ) "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findPacDetectHivResident(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("job") String job,
            @Param("testObject") String testObject,
            @Param("addressFilter") String addressFilter,
            @Param("dateFilter") String dateFilter,
            @Param("patientStatus") String patientStatus,
            @Param("provinceID") String provinceID,
            @Param("otherProvinceID") String otherProvinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("flag") String flag,
            @Param("siteLocals") List<Long> siteLocals
    );

    /**
     * Lấy thông tin khách hàng phát hiện theo nhóm tuổi khi click
     *
     * @auth TrangBN
     * @param fromTime
     * @param toTime
     * @param addressType
     * @param patientStatus
     * @param searchTime
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param job
     * @param testObject
     * @param statusTreatment
     * @param statusResident
     * @param otherProvinceID
     * @param ageFrom
     * @param ageTo
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (:provinceID = e.provinceID) "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (:patientStatus IS NULL OR :patientStatus = '' OR ((:patientStatus = 'alive' AND (e.deathTime IS NULL OR e.deathTime = '')) OR (:patientStatus = 'die' AND (e.deathTime IS NOT NULL AND e.deathTime <> '')))) "
            + " AND (:searchTime IS NULL OR :searchTime = '' OR (:searchTime = 'ngayxn' AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :fromTime AND FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :toTime ) OR (:searchTime = 'chuyenquanly' AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') >= :fromTime AND FUNCTION('date_format', e.reviewProvinceTime, '%Y-%m-%d') <=  :toTime)) "
            + " AND (e.jobID = :job OR :job IS NULL OR :job = '' ) "
            + " AND (e.objectGroupID = :testObject OR :testObject IS NULL OR :testObject = '' ) "
            + " AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS NULL OR :statusTreatment = '' ) "
            + " AND (e.statusOfResidentID = :statusResident OR :statusResident IS NULL OR :statusResident = '' ) "
            + " AND (:addressType IS NULL OR :addressType = '' OR ((:addressType = 'hokhau' AND e.permanentDistrictID = :districtID) OR (:addressType = 'tamtru' AND e.currentDistrictID = :districtID)) OR :districtID = '' OR :districtID IS NULL) "
            + " AND (:addressType IS NULL OR :addressType = '' OR ((:addressType = 'hokhau' AND e.permanentWardID = :wardID) OR (:addressType = 'tamtru' AND e.currentWardID = :wardID)) OR :wardID = '' OR :wardID IS NULL) "
            + " AND (:otherProvinceID IS NULL OR :otherProvinceID = '' OR (:addressType = 'hokhau' AND e.permanentProvinceID = :otherProvinceID) OR (:addressType = 'tamtru' AND e.currentProvinceID = :otherProvinceID)) "
            + " AND (:ageFrom IS NULL OR (:searchTime = 'ngayxn' AND (YEAR(e.confirmTime) - e.yearOfBirth) >= :ageFrom AND (YEAR(e.confirmTime) - e.yearOfBirth) <= :ageTo) OR :searchTime <> 'ngayxn') "
            + " AND (:ageFrom IS NULL OR (:searchTime = 'chuyenquanly' AND (YEAR(e.reviewProvinceTime) - e.yearOfBirth) >= :ageFrom AND (YEAR(e.reviewProvinceTime) - e.yearOfBirth) <= :ageTo) OR :searchTime <> 'chuyenquanly') "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientInfoEntity> findDetectHivAge(
            @Param("fromTime") String fromTime,
            @Param("toTime") String toTime,
            @Param("addressType") String addressType,
            @Param("patientStatus") String patientStatus,
            @Param("searchTime") String searchTime,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("job") String job,
            @Param("testObject") String testObject,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("otherProvinceID") String otherProvinceID,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo
    );

    /**
     * Lấy DS người nhiễm sổ A10/YTCS
     *
     * @author DSNAnh
     * @param isRemove
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param fullname
     * @param yearOfBirth
     * @param genderID
     * @param identityCard
     * @param healthInsuranceNo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param objectGroupID
     * @param pageable
     * @return list
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = :isRemove "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + " AND (e.provinceID = :provinceID  OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.districtID = :districtID  OR :districtID IS '' OR :districtID IS NULL) "
            + " AND (e.wardID = :wardID  OR :wardID IS '' OR :wardID IS NULL) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:fullname, '%')) OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:healthInsuranceNo, '%')) OR :healthInsuranceNo IS '' OR :healthInsuranceNo IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
    )
    public Page<PacPatientInfoEntity> findPatientA10(
            @Param("isRemove") boolean isRemove,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("fullname") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("objectGroupID") String objectGroupID,
            Pageable pageable);

    /**
     * Tìm kiếm theo sourceID và source service
     *
     * @param sourceID
     * @param sourceService
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND e.sourceID = :sourceID "
            + " AND e.sourceServiceID = :sourceService ")
    public List<PacPatientInfoEntity> findBySourceIDSourceService(@Param("sourceID") Long sourceID, @Param("sourceService") String sourceService);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND e.sourceSiteID = :siteID "
            + " AND e.sourceID = :sourceID "
            + " AND e.sourceServiceID = :sourceService ")
    public List<PacPatientInfoEntity> findByTransfer(@Param("siteID") Long siteID, @Param("sourceID") Long sourceID, @Param("sourceService") String sourceService);

    /**
     *
     * @param code
     * @param name
     * @param addressFilter
     * @param blood
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param confirmTimeFrom
     * @param inputTimeFrom
     * @param aidsFrom
     * @param aidsTo
     * @param requestDeathTimeFrom
     * @param inputTimeTo
     * @param requestDeathTimeTo
     * @param deathTimeFrom
     * @param deathTimeTo
     * @param confirmTimeTo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param genderID
     * @param raceID
     * @param modeOfTransmissionID
     * @param objectGroupID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param ageFrom
     * @param ageTo
     * @param isTTYT
     * @param isTYT
     * @param placeTest
     * @param facility
     * @param manageStatus
     * @param isVAAC
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.aidsStatusDate IS NOT NULL) "
            + " AND ((((:manageStatus = '-1' AND :isTTYT = FALSE AND :isTYT = FALSE) OR (:manageStatus = '-1' AND (:isTTYT = TRUE OR :isTYT = TRUE) AND (e.sourceServiceID = '103' OR e.acceptTime IS NOT NULL))) AND ( "
            + " (e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID))) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL) "
            + " )) OR ((:manageStatus = '1' AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL AND e.sourceServiceID <> '103' AND ((e.permanentProvinceID = :provinceID AND e.provinceID = :provinceID AND e.detectProvinceID = :provinceID) OR (e.requestVaacTime is not null AND e.provinceID = :provinceID)) AND :isTTYT = FALSE AND :isTYT = FALSE) OR "
            + " (:manageStatus = '2' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL ) OR "
            + " (:manageStatus = '3' AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NULL ) OR"
            + " (:manageStatus = '4' AND :isVAAC IS FALSE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL AND e.provinceID = :provinceID)) OR (:isVAAC IS TRUE AND e.acceptTime IS NOT NULL AND e.reviewWardTime IS NOT NULL AND e.reviewProvinceTime IS NOT NULL )) "
            + " AND (:addressFilter IS NULL OR :addressFilter = '' OR "
            + " ((:addressFilter = 'hokhau' AND (e.permanentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.permanentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL ) AND (e.permanentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )) OR "
            + "(:addressFilter = 'tamtru' AND (e.currentProvinceID IN (:permanentProvinceID) OR coalesce(:permanentProvinceID, null) IS NULL ) AND (e.currentDistrictID IN (:permanentDistrictID) OR coalesce(:permanentDistrictID, null) IS NULL )  AND (e.currentWardID IN (:permanentWardID) OR coalesce(:permanentWardID, null) IS NULL )))) "
            + " AND ((FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom  OR :confirmTimeFrom IS NULL) AND (FUNCTION('date_format', e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') >= :aidsFrom  OR :aidsFrom IS NULL) AND (FUNCTION('date_format', e.aidsStatusDate, '%Y-%m-%d') <= :aidsTo OR :aidsTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.deathTime, '%Y-%m-%d') >= :deathTimeFrom  OR :deathTimeFrom IS NULL) AND (FUNCTION('date_format', e.deathTime, '%Y-%m-%d') <= :deathTimeTo OR :deathTimeTo IS NULL)) "
            + " AND ((FUNCTION('date_format', e.createAT, '%Y-%m-%d') >= :inputTimeFrom  OR :inputTimeFrom IS NULL) AND (FUNCTION('date_format', e.createAT, '%Y-%m-%d') <=  :inputTimeTo OR :inputTimeTo IS NULL))"
            + " AND ((FUNCTION('date_format', e.updateAt, '%Y-%m-%d') >= :updateTimeFrom  OR :updateTimeFrom IS NULL) AND (FUNCTION('date_format', e.updateAt, '%Y-%m-%d') <=  :updateTimeTo OR :updateTimeTo IS NULL))"
            + " AND (e.genderID IN (:genderID) OR coalesce(:genderID, null) IS NULL) "
            + " AND (e.modeOfTransmissionID IN (:modeOfTransmissionID) OR coalesce(:modeOfTransmissionID, null) IS NULL) "
            + " AND (e.raceID IN (:raceID) OR coalesce(:raceID, null) IS NULL) "
            + " AND (e.objectGroupID IN (:objectGroupID) OR coalesce(:objectGroupID, null) IS NULL) "
            + " AND (((e.aidsStatusDate <> '' AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.aidsStatusDate) - e.yearOfBirth >= :ageFrom) OR :ageFrom IS NULL OR :ageFrom = '') "
            + " AND ((e.aidsStatusDate <> '' AND (e.yearOfBirth IS NOT NULL AND e.yearOfBirth <> '') AND YEAR(e.aidsStatusDate) - e.yearOfBirth <= :ageTo) OR :ageTo IS NULL OR :ageTo = '')) "
            + " AND ((e.provinceID = :provinceID AND :isVAAC IS FALSE) OR :isVAAC IS TRUE )"
            + " AND (e.districtID = :districtID OR :districtID IS NULL) "
            + " AND (e.ID = :code OR :code = 0) "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:name, '%')) OR :name IS '' OR :name IS NULL) "
            + " AND (e.bloodBaseID = :blood OR :blood IS '' OR :blood IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS NULL) "
            + " AND (e.wardID = :wardID OR :wardID IS NULL) "
            + " AND (e.siteConfirmID = :placeTest OR :placeTest IS NULL OR :placeTest = '') "
            + " AND (e.siteTreatmentFacilityID = :facility OR :facility IS NULL OR :facility = '') "
    )
    public Page<PacPatientInfoEntity> findAidsReport(
            @Param("code") Long code,
            @Param("name") String name,
            @Param("blood") String blood,
            @Param("addressFilter") String addressFilter,
            @Param("updateTimeFrom") String updateTimeFrom,
            @Param("updateTimeTo") String updateTimeTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("aidsFrom") String aidsFrom,
            @Param("aidsTo") String aidsTo,
            @Param("inputTimeFrom") String inputTimeFrom,
            @Param("inputTimeTo") String inputTimeTo,
            @Param("deathTimeFrom") String deathTimeFrom,
            @Param("deathTimeTo") String deathTimeTo,
            @Param("permanentProvinceID") List<String> permanentProvinceID,
            @Param("permanentDistrictID") List<String> permanentDistrictID,
            @Param("permanentWardID") List<String> permanentWardID,
            @Param("genderID") List<String> genderID,
            @Param("raceID") List<String> raceID,
            @Param("modeOfTransmissionID") List<String> modeOfTransmissionID,
            @Param("objectGroupID") List<String> objectGroupID,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("ageFrom") Integer ageFrom,
            @Param("ageTo") Integer ageTo,
            @Param("manageStatus") String manageStatus,
            @Param("isVAAC") boolean isVAAC,
            @Param("isTTYT") boolean isTTYT,
            @Param("isTYT") boolean isTYT,
            @Param("placeTest") String placeTest,
            @Param("facility") String facility,
            Pageable pageable);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE 1 = 1 "
            + " AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL "
            + " AND e.detectProvinceID <> e.permanentProvinceID "
            + " AND (e.provinceID = :provinceID OR :provinceID IS '' OR :provinceID IS NULL) "
            + " AND (e.permanentProvinceID = :permanentProvinceID OR :permanentProvinceID is null OR :permanentProvinceID = '' ) "
            + " AND (e.currentProvinceID = :currentProvinceID OR :currentProvinceID is null OR :currentProvinceID = '') "
            + " AND (e.detectProvinceID = :detectProvinceID OR :detectProvinceID is null OR :detectProvinceID = '') "
            + " AND (e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:name, '%')) OR :name IS '' OR :name IS NULL) "
            + " AND (e.yearOfBirth = :yearOfBirth OR :yearOfBirth IS '' OR :yearOfBirth IS NULL) "
            + " AND (e.genderID = :genderID OR :genderID IS '' OR :genderID IS NULL) "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%')) OR :identityCard IS '' OR :identityCard IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (e.bloodBaseID = :bloodBaseID OR :bloodBaseID IS '' OR :bloodBaseID IS NULL) "
            + " AND (e.sourceServiceID = :sourceServiceID OR :sourceServiceID IS '' OR :sourceServiceID IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.siteTreatmentFacilityID = :siteTreatmentFacilityID OR :siteTreatmentFacilityID IS '' OR :siteTreatmentFacilityID IS NULL) "
            + " AND ( (:status = '1' AND e.updatedPacTime is null) OR (:status = '2' AND e.updatedPacTime is not null ) OR :status IS '' OR :status IS NULL) "
            + " AND (  ( :statusPatient = 'new' AND e.status = 'new') OR ( :statusPatient = 'old' AND e.status = 'old') or :statusPatient = '' ) "
            + " AND (  ( :statusManage = '1' AND e.refID is null) OR ( :statusManage = '2' AND e.refID is not null) or :statusManage = '' ) "
            + " AND (e.objectGroupID = :testObject OR :testObject IS '' OR :testObject IS NULL) "
            + " AND (e.modeOfTransmissionID = :transmision OR :transmision IS '' OR :transmision IS NULL) "
            + " AND (e.statusOfTreatmentID = :statusTreatment OR :statusTreatment IS '' OR :statusTreatment IS NULL) "
            + " AND (e.statusOfResidentID = :statusResident OR :statusResident IS '' OR :statusResident IS NULL) "
            + " AND (e.siteConfirmID = :siteConfirmID OR :siteConfirmID IS '' OR :siteConfirmID IS NULL) "
            + " AND (e.ID = :hivID  OR :hivID = 0) "
            + " AND ( ( :tab is null AND e.requestVaacTime is null  AND e.remove = false ) OR ( :tab = 'remove' AND e.remove = true ) OR (:tab = 'vaac' AND e.refID is null AND e.requestVaacTime is null  AND e.remove = false ) OR  (:tab = 'pac' AND e.refID is not null AND e.requestVaacTime is null   AND e.remove = false) OR  (:tab = 'review' AND e.reviewVaacTime is not null AND e.requestVaacTime is null   AND e.remove = false) OR (:tab = 'manage' AND e.requestVaacTime is not null   AND e.remove = false) ) "
    )
    public Page<PacPatientInfoEntity> findOutProvince(
            //            @Param("remove") boolean remove,
            @Param("tab") String tab,
            @Param("provinceID") String provinceID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("currentProvinceID") String currentProvinceID,
            @Param("detectProvinceID") String detectProvinceID,
            @Param("name") String fullname,
            @Param("yearOfBirth") Integer yearOfBirth,
            @Param("genderID") String genderID,
            @Param("identityCard") String identityCard,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("bloodBaseID") String bloodBaseID,
            @Param("sourceServiceID") String sourceServiceID,
            @Param("siteConfirmID") String siteConfirmID,
            @Param("siteTreatmentFacilityID") String siteTreatmentFacilityID,
            @Param("status") String status,
            @Param("statusPatient") String statusPatient,
            @Param("statusManage") String statusManage,
            @Param("testObject") String testObject,
            @Param("transmision") String transmision,
            @Param("statusTreatment") String statusTreatment,
            @Param("statusResident") String statusResident,
            @Param("hivID") Long hivID,
            Pageable pageable);

    @Query(" SELECT COUNT(e.ID) FROM PacPatientInfoEntity e WHERE 1 = 1 "
            + " AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL "
            + " AND e.detectProvinceID <> e.permanentProvinceID "
            + " AND ( ( :tab is '' AND e.requestVaacTime is null  AND e.remove = false ) OR ( :tab = 'remove' AND e.remove = true ) OR (:tab = 'vaac' AND e.refID is null AND e.requestVaacTime is null  AND e.remove = false ) OR  (:tab = 'pac' AND e.refID is not null AND e.requestVaacTime is null   AND e.remove = false) OR  (:tab = 'review' AND e.reviewVaacTime is not null AND e.requestVaacTime is null   AND e.remove = false) OR (:tab = 'manage' AND e.requestVaacTime is not null   AND e.remove = false) ) "
    )
    public Long countPacOutProvince(
            @Param("tab") String tab);

    /**
     * Phát hiện trong tỉnh
     *
     * @param provinceID
     * @return
     */
    @Query(" SELECT COUNT(e) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + "AND  e.sourceServiceID <> '103' "
            + "AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL "
            + "AND e.permanentProvinceID = :provinceID "
            + "AND e.provinceID = :provinceID "
            + "AND e.detectProvinceID = :provinceID")
    public Long countInProvince(@Param("provinceID") String provinceID);

    /**
     * Tỉnh khác phát hiện
     *
     * @param provinceID
     * @return
     */
    @Query(" SELECT COUNT(e) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + "AND  e.sourceServiceID <> '103' "
            + "AND e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL "
            //            + "AND e.acceptPermanentTime IS NOT NULL "
            + "AND e.requestVaacTime IS NOT NULL "
            //            + "AND e.permanentProvinceID = :provinceID "
            + "AND e.provinceID = :provinceID ")
//            + "AND e.detectProvinceID <> :provinceID")
    public Long countOtherProvince(@Param("provinceID") String provinceID);

    /**
     * Phát hiện ngoại tỉnh
     *
     * @param provinceID
     * @return
     */
    @Query(" SELECT COUNT(e) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + "AND  e.sourceServiceID <> '103' "
            + " AND ((e.acceptTime IS NULL AND e.reviewWardTime IS NULL AND e.reviewProvinceTime IS NULL) OR (e.acceptPermanentTime IS NOT NULL AND :provinceID = e.detectProvinceID AND :provinceID <> e.provinceID)) "
            + "AND e.requestVaacTime IS NULL "
            + "AND e.permanentProvinceID <> :provinceID "
            + "AND e.detectProvinceID = :provinceID")
    public Long countOutProvince(@Param("provinceID") String provinceID);

    /**
     * Tìm kiếm trùng lắp ngoại tỉnh vaac
     *
     * @param ID
     * @param fullname
     * @param genderID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param identityCard
     * @param healthInsuranceNo
     * @param patientPhone
     * @param refID
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false AND e.ID != :ID "
            + "AND ((:refID is not null AND e.ID != :refID) OR :refID is null) "
            + "AND ((e.metaName is not null AND e.metaName is not null AND e.metaName IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE :fullname)) OR :fullname IS NULL) "
            + "AND ((e.genderID is not null AND e.genderID is not null  AND e.genderID = :genderID) OR :genderID IS NULL) "
            + "AND ((e.patientPhone is not null AND e.patientPhone is not null AND e.patientPhone = :patientPhone) OR :patientPhone IS NULL) "
            + "AND ((e.metaIdentityCard is not null AND e.metaIdentityCard is not null AND e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :identityCard)) OR :identityCard is null) "
            + "AND ((e.metaHealthInsuranceNo is not null AND e.metaHealthInsuranceNo is not null AND e.metaHealthInsuranceNo IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content = :healthInsuranceNo)) OR :healthInsuranceNo is null) "
            + "AND ((e.permanentProvinceID is not null AND e.permanentProvinceID is not null AND e.permanentProvinceID = :permanentProvinceID) OR :permanentProvinceID is null) "
            + "AND ((e.permanentDistrictID is not null AND e.permanentDistrictID = :permanentDistrictID) OR :permanentDistrictID is null) "
            + "AND ((e.permanentWardID is not null AND e.permanentWardID = :permanentWardID) OR :permanentWardID is null)")
    public Page<PacPatientInfoEntity> duplicateFilterOutprovince(
            @Param("ID") Long ID,
            @Param("refID") Long refID,
            @Param("fullname") String fullname,
            @Param("genderID") String genderID,
            @Param("permanentProvinceID") String permanentProvinceID,
            @Param("permanentDistrictID") String permanentDistrictID,
            @Param("permanentWardID") String permanentWardID,
            @Param("identityCard") String identityCard,
            @Param("healthInsuranceNo") String healthInsuranceNo,
            @Param("patientPhone") String patientPhone,
            Pageable pageable);

    /**
     *
     * @author DSNAnh
     * @param provinceID
     * @return
     */
    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL) "
            + " AND (e.reviewWardTime IS NOT NULL) "
            + " AND (e.reviewProvinceTime IS NOT NULL) "
            + "AND e.provinceID = :provinceID ")
    public List<PacPatientInfoEntity> findAllByProvinceId(@Param("provinceID") String provinceID);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND e.confirmCode = :confirmCode "
            + " AND e.provinceID = :province "
            + " AND (e.metaIdentityCard IN (SELECT s.ID FROM  MetaDataEntity s WHERE s.content LIKE CONCAT('%',:identityCard, '%'))) ")
    public List<PacPatientInfoEntity> findByConfirm(@Param("confirmCode") String confirmCode, @Param("identityCard") String identityCard, @Param("province") String province);

    @Query(" SELECT e FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND e.confirmCode = :confirmCode "
            + " AND e.provinceID = :province "
            + " AND e.sourceServiceID = '103' "
    )
    public List<PacPatientInfoEntity> findByConfirms(@Param("confirmCode") String confirmCode, @Param("province") String province);

    //Lấy group by tỉnh, huyện quản lý theo thời gian cập nhật quản lý: Theo nhiễm mới
    @Query("SELECT new com.gms.entity.form.ql.Local(e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.earlyHivTime, '%m'), DATE_FORMAT(e.earlyHivTime, '%Y')) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (e.provinceID IS NOT NULL) AND (e.districtID IS NOT NULL)"
            + " AND (e.earlyHivTime IS NOT NULL)"
            + " AND e.earlyHiv = '8' "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " GROUP BY e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.earlyHivTime, '%m'), DATE_FORMAT(e.earlyHivTime, '%Y')"
    )
    public List<com.gms.entity.form.ql.Local> findProvinceNhiemMoi(
            @Param("from") String from,
            @Param("to") String to);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " AND e.earlyHiv = '8' "
            + " AND e.permanentProvinceID = :provinceID "
            + " AND e.permanentDistrictID = :districtID "
            + " AND e.permanentWardID = :wardID "
    )
    public int countProvinceNhiemMoiThuongTru(
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("from") String from,
            @Param("to") String to);

//    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
//            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
//            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
//            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
//            + " AND e.earlyDiagnose = '2' "
//            + " AND e.currentProvinceID = :provinceID "
//            + " AND e.currentDistrictID = :districtID "
//    )
//    public int countProvinceNhiemMoiTamtru(
//            @Param("provinceID") String provinceID,
//            @Param("districtID") String districtID,
//            @Param("from") String from,
//            @Param("to") String to);
    /**
     * Phát hiện theo ngày xn khẳng định
     *
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT new com.gms.entity.form.ql.Local(e.permanentProvinceID, e.permanentDistrictID, DATE_FORMAT(e.confirmTime, '%m'), DATE_FORMAT(e.confirmTime, '%Y')) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (e.provinceID IS NOT NULL) AND (e.districtID IS NOT NULL)"
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " GROUP BY e.permanentProvinceID, e.permanentDistrictID, e.currentProvinceID, e.currentDistrictID, DATE_FORMAT(e.confirmTime, '%m'), DATE_FORMAT(e.confirmTime, '%Y')"
    )
    public List<com.gms.entity.form.ql.Local> findProvincePhathien(
            @Param("from") String from,
            @Param("to") String to);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " AND e.permanentProvinceID = :provinceID "
            + " AND e.permanentDistrictID = :districtID "
            + " AND e.permanentWardID = :wardID "
    )
    public int countProvincePhatHienThuongTru(
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("from") String from,
            @Param("to") String to);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (e.deathTime is null OR DATE_FORMAT(e.deathTime, '%Y-%m-%d') > :to)"
            + " AND e.permanentProvinceID = :provinceID "
    )
    public int countProvincePhatHienConSong(
            @Param("provinceID") String provinceID,
            @Param("to") String to);

    /**
     * Tìm số tử vong thay đổi trong khoảng thời gian
     *
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT new com.gms.entity.form.ql.Local(e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.deathTime, '%m'), DATE_FORMAT(e.deathTime, '%Y')) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (e.provinceID IS NOT NULL) AND (e.districtID IS NOT NULL)"
            + " AND (e.deathTime IS NOT NULL)"
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " GROUP BY e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.deathTime, '%m'), DATE_FORMAT(e.deathTime, '%Y')"
    )
    public List<com.gms.entity.form.ql.Local> findProvinceTuVong(
            @Param("from") String from,
            @Param("to") String to);

    /**
     * Số tử vong theo tỉnh/hiện/xã
     *
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param from
     * @param to
     * @return
     */
    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (DATE_FORMAT(e.deathTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.deathTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " AND e.permanentProvinceID = :provinceID "
            + " AND e.permanentDistrictID = :districtID "
            + " AND e.permanentWardID = :wardID "
    )
    public int countProvinceTuVongThuongTru(
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("from") String from,
            @Param("to") String to);

    /**
     * Mới nhiễm thay đổi
     *
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT new com.gms.entity.form.ql.Local(e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.earlyHivTime, '%m'), DATE_FORMAT(e.earlyHivTime, '%Y')) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (e.provinceID IS NOT NULL) AND (e.districtID IS NOT NULL)"
            + " AND (e.earlyHivTime IS NOT NULL)"
            //            + " AND e.earlyHiv = '8' "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.updateAt, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " GROUP BY e.permanentProvinceID, e.permanentDistrictID, e.permanentWardID, e.currentProvinceID, e.currentDistrictID, e.currentWardID, DATE_FORMAT(e.earlyHivTime, '%m'), DATE_FORMAT(e.earlyHivTime, '%Y')"
    )
    public List<com.gms.entity.form.ql.Local> findProvinceMoiNhiem(
            @Param("from") String from,
            @Param("to") String to);

    @Query(" SELECT count(e.ID) FROM PacPatientInfoEntity e WHERE e.remove = false "
            + " AND (e.acceptTime IS NOT NULL)  AND (e.reviewWardTime IS NOT NULL) AND (e.reviewProvinceTime IS NOT NULL)"
            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.earlyHivTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :from OR :from IS NULL OR :from = '') "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :to OR :to IS NULL OR :to = '') "
            //            + " AND e.earlyHiv = '8' "
            + " AND e.permanentProvinceID = :provinceID "
            + " AND e.permanentDistrictID = :districtID "
            + " AND e.permanentWardID = :wardID "
    )
    public int countProvinceMoiNhiemThuongTru(
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("from") String from,
            @Param("to") String to);
}
