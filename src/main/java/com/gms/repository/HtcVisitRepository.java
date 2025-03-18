package com.gms.repository;

import com.gms.entity.db.HtcVisitEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface HtcVisitRepository extends CrudRepository<HtcVisitEntity, Long> {

    /**
     *
     * @param siteID
     * @param code
     * @param advisoryeTime
     * @param advisoryeTimeFrom
     * @param advisoryeTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param exchangeTimeFrom
     * @param exchangeTimeTo
     * @param patientName
     * @param serviceID
     * @param searchServiceID
     * @param objectGroupID
     * @param therapyExchangeStatus
     * @param confirmTestStatus
     * @param feedbackStatus
     * @param confirmResultsID
     * @param testResultsID
     * @param isConfirm
     * @param isOpc
     * @param isRemove
     * @param gsphStatus
     * @param objects
     * @param filter
     * @param createdBY
     * @param statusRequest
     * @param customerType
     * @param pageable
     * @param notReceive
     * @param isConfirmCreate
     * @param earlyDiagnose
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = :remove "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') >= :exchangeTimeFrom OR :exchangeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') <= :exchangeTimeTo OR :exchangeTimeTo IS NULL) "
            + "AND ((:therapyExchangeStatus = '0' AND ((e.resultsPatienTime != null) AND (e.exchangeTime is null OR e.exchangeTime = '' ))) OR (e.therapyExchangeStatus = :therapyExchangeStatus OR :therapyExchangeStatus IS '' OR :therapyExchangeStatus IS NULL)) "
            + "AND (e.confirmTestStatus = :confirmTestStatus OR :confirmTestStatus IS '' OR :confirmTestStatus IS NULL) "
            + "AND (e.feedbackStatus = :feedbackStatus OR :feedbackStatus IS '' OR :feedbackStatus IS NULL) "
            + "AND ((:isConfirm = true AND e.confirmTestStatus IN ('0','1','2')) OR :isConfirm = false) "
//            + "AND ((:isOpc = true AND e.arrivalSite is not null AND e.exchangeTime is not null) OR (:isOpc = true AND e.arrivalSite is not null AND e.registerTherapyTime is not null AND e.therapyNo is not null) OR :isOpc = false) "
            + "AND ((:isOpc = true AND e.resultsPatienTime is not null AND e.confirmResultsID = '2')  OR :isOpc = false) "
            + "AND ((:isNotReceive = true AND e.resultsPatienTime is null OR e.resultsPatienTime = '') OR :isNotReceive = false) "
            + "AND ((:earlyDiagnose = '0' AND (e.earlyDiagnose is null OR e.earlyDiagnose = '')) OR (:earlyDiagnose IS NOT NULL AND :earlyDiagnose <> '' AND e.earlyDiagnose = :earlyDiagnose ) OR (:earlyDiagnose is  null OR :earlyDiagnose = '')) "
            + "AND ((:isConfirmCreate = true AND e.sourceCreated = '3') OR :isConfirmCreate = false) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (e.confirmResultsID = :confirmResultsID OR :confirmResultsID IS '' OR :confirmResultsID IS NULL) "
            + "AND (e.testResultsID = :testResultsID OR :testResultsID IS '' OR :testResultsID IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') = :advisoryeTime OR :advisoryeTime IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :advisoryeTimeFrom OR :advisoryeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :advisoryeTimeTo OR :advisoryeTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND ( (:statusRequest = '1' AND e.requestConfirmTime is not null and e.updateConfirmTime is null) OR (:statusRequest = '2' AND e.requestConfirmTime is not null and e.updateConfirmTime is not null ) OR :statusRequest is null ) "
            + "AND ((:filter = 0 AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL)) OR :filter = 1) "
            + "AND (e.serviceID IN :searchServiceID OR coalesce(:searchServiceID, null) IS NULL) "
            + "AND ((:filter = 1 AND (e.createdBY = :createdBY OR e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL)) OR :filter = 0) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.patientName LIKE CONCAT('%',:patient_name, '%') OR :patient_name IS '' OR :patient_name IS NULL) "
            + "AND ((:gsphStatus = '0' AND e.pacSentDate IS NULL) OR (e.confirmResultsID = '2' AND :gsphStatus = '1' AND e.pacSentDate IS NOT NULL) OR :gsphStatus IS NULL OR :gsphStatus IS '' ) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + "AND (e.customerType IN :customerType OR :customerType IS NULL OR :customerType IS '') "
            + "AND (((:isConfirm <> true AND :isOpc <> true AND :remove <> true ) AND ((:gsphStatus = '0' AND e.pacSentDate IS NULL) OR (:gsphStatus = '1' AND e.pacSentDate IS NOT NULL))) OR :gsphStatus IS NULL OR :gsphStatus IS '') ")
    @Cacheable(value = "htc_visit_find")
    public Page<HtcVisitEntity> find(
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("advisoryeTime") String advisoryeTime,
            @Param("advisoryeTimeFrom") String advisoryeTimeFrom,
            @Param("advisoryeTimeTo") String advisoryeTimeTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("exchangeTimeFrom") String exchangeTimeFrom,
            @Param("exchangeTimeTo") String exchangeTimeTo,
            @Param("patient_name") String patientName,
            @Param("serviceID") Set<String> serviceID,
            @Param("searchServiceID") Set<String> searchServiceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("therapyExchangeStatus") String therapyExchangeStatus,
            @Param("confirmTestStatus") String confirmTestStatus,
            @Param("feedbackStatus") String feedbackStatus,
            @Param("confirmResultsID") String confirmResultsID,
            @Param("testResultsID") String testResultsID,
            @Param("isConfirm") boolean isConfirm,
            @Param("isOpc") boolean isOpc,
            @Param("remove") boolean isRemove,
            @Param("gsphStatus") String gsphStatus,
            @Param("objects") Set<String> objects,
            @Param("filter") int filter,
            @Param("createdBY") Long createdBY,
            @Param("statusRequest") String statusRequest,
            @Param("customerType") String customerType,
            @Param("isNotReceive") boolean notReceive,
            @Param("isConfirmCreate") boolean isConfirmCreate,
            @Param("earlyDiagnose") String earlyDiagnose,
            Pageable pageable);

    @Cacheable(value = "htc_visit_countBysiteID")
    @Query("SELECT COUNT(e) FROM HtcVisitEntity e WHERE e.siteID=:siteID")
    public Long countBysiteID(@Param("siteID") Long siteID);

    @Cacheable(value = "htc_visit_countByCodeSiteID")
    @Query("SELECT COUNT(e) FROM HtcVisitEntity e WHERE e.siteID=:siteID AND e.code = :code")
    public Long countByCodeSiteID(@Param("siteID") Long siteID, @Param("code") String code);

    @Cacheable(value = "htc_visit_findLastBysiteID")
    @Query(value = "SELECT * FROM htc_visit as e WHERE e.site_id=:siteID AND e.is_remove = false ORDER BY e.created_at DESC LIMIT 1", nativeQuery = true)
    public HtcVisitEntity findLastBysiteID(@Param("siteID") Long siteID);

    public List<HtcVisitEntity> findAll(Sort sort);

    @Cacheable(value = "htc_visit_findOne")
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.ID=:ID")
    public HtcVisitEntity findOne(@Param("ID") Long ID);

    /**
     * Search the positive base on service and test result and range of time
     *
     * @param start
     * @param end
     * @param services
     * @param objects
     * @param siteid
     * @param code
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.confirmTime >= :start "
            + "AND e.confirmTime <= :end "
            + "AND e.siteID = :siteID "
            + "AND e.confirmResultsID = 2"
            + "AND (e.serviceID IN (:services) OR e.serviceID in ('')) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " ORDER BY e.confirmTime ASC")
    @Cacheable(value = "htc_visit_findPositive")
    public List<HtcVisitEntity> findPositive(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("services") List<String> services,
            @Param("objects") List<String> objects,
            @Param("siteID") Long siteid,
            @Param("code") String code);

    /**
     * DS KH được chuyển gửi điều trị thành công
     *
     * @param start
     * @param end
     * @param services
     * @param objects
     * @param siteid
     * @param code
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.confirmResultsID = 2 "
            + "AND e.arrivalSite IS NOT NULL "
            + "AND e.registerTherapyTime IS NOT NULL "
            + "AND e.therapyNo IS NOT NULL "
            + "AND e.siteID = :siteID "
            + "AND e.exchangeTime >= :start "
            + "AND e.exchangeTime <= :end "
            + "AND (e.serviceID IN (:services) OR e.serviceID in ('')) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "ORDER BY e.registerTherapyTime ASC")
    @Cacheable(value = "htc_visit_findTransferSuccess")
    public List<HtcVisitEntity> findTransferSuccess(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("services") List<String> services,
            @Param("objects") List<String> objects,
            @Param("siteID") Long siteid,
            @Param("code") String code);

    /**
     * Get test visit book information
     *
     * @param start
     * @param end
     * @param services
     * @param objects
     * @param siteid
     * @param code
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.advisoryeTime >= :start "
            + "AND e.advisoryeTime <= :end "
            + "AND e.siteID = :siteID "
            + "AND e.serviceID IN (:services) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " ORDER BY e.advisoryeTime ASC, e.code ASC "
    )
    @Cacheable(value = "htc_visit_findHtcVisitBook")
    public List<HtcVisitEntity> findHtcVisitBook(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("services") List<String> services,
            @Param("objects") List<String> objects,
            @Param("siteID") Long siteid,
            @Param("code") String code
    );

    //Sổ xét nghiệm sàng lọc
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.preTestTime >= :start "
            + "AND e.preTestTime <= :end "
            + "AND e.siteID = :siteID "
            + "AND e.serviceID IN (:services) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "ORDER BY e.preTestTime ASC, e.code ASC "
    )
    @Cacheable(value = "htc_visit_findHtcTestBook")
    public List<HtcVisitEntity> findHtcTestBook(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("services") List<String> services,
            @Param("objects") List<String> objects,
            @Param("siteID") Long siteid,
            @Param("code") String code
    );

    /**
     * Check existing code and site id when update customer ( if return 0 record
     * -> code available else existed value)
     *
     * @param code
     * @param id
     * @param siteID
     * @return
     */
    @Query("SELECT COUNT(*) FROM HtcVisitEntity e WHERE e.id <> :id AND e.code = :code AND e.siteID = :siteID")
    public Integer checkExistingCode(
            @Param("code") String code,
            @Param("id") Long id,
            @Param("siteID") Long siteID
    );

    /**
     * Tìm danh sách số người xét nghiệm hiv dương tính theo tt03
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param testResultsID
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (e.confirmResultsID = '2' AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :endTime) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.testResultsID IN :testResultsID OR coalesce(:testResultsID, null) IS NULL)"
            + "AND (e.customerType IN :customerType OR :customerType IS NULL OR :customerType IS '')")
    @Cacheable(value = "htc_visit_findSoNguoiDuocXetNghiemDuongTinhHIV")
    public List<HtcVisitEntity> findSoNguoiDuocXetNghiemDuongTinhHIV(
            @Param("siteID") Set<Long> siteID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("testResultsID") Set<String> testResultsID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("customerType") String customerType);

    /**
     * Tìm danh sách số người xét nghiệm hiv theo thông tư 03
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.isAgreePreTest = '1' "
            + "AND (DATE_FORMAT(e.preTestTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.preTestTime, '%Y-%m-%d') <= :endTime) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
            + "AND (e.customerType = :customerType OR :customerType IS NULL OR :customerType IS '')")
    @Cacheable(value = "htc_visit_findSoNguoiDuocXetNghiemHIV")
    public List<HtcVisitEntity> findSoNguoiDuocXetNghiemHIV(
            @Param("siteID") Set<Long> siteID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("customerType") String customerType);

    /**
     * Danh sách nhận kết quả xét nghiệm tổng tt03
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.customerType IN :customerType OR :customerType IS NULL OR :customerType IS '') "
            + "AND ( "
            + "        (DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') <= :endTime) "
            + "     OR (DATE_FORMAT(e.resultsTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.resultsTime, '%Y-%m-%d') <= :endTime)"
            + ") ")
    @Cacheable(value = "htc_visit_findNhanKetQuaXetNghiemHIV")
    public List<HtcVisitEntity> findNhanKetQuaXetNghiemHIV(
            @Param("siteID") Set<Long> siteID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("customerType") String customerType);

    /**
     * Danh sách nhận kết quả dương tính tt03
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.customerType IN :customerType OR :customerType IS NULL OR :customerType IS '') "
            + "AND (e.confirmResultsID = '2' AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') <= :endTime)")
    @Cacheable(value = "htc_visit_findNhanKetQuaXetNghiemDuongTinhHIV")
    public List<HtcVisitEntity> findNhanKetQuaXetNghiemDuongTinhHIV(
            @Param("siteID") Set<Long> siteID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("customerType") String customerType);

    /**
     * Danh sách khách hàng lấy theo tt09 phụ lục 02
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param confirmResultsID
     * @param yearOfBirthFrom
     * @param yearOfBirthTo
     * @param gender
     * @param startTime
     * @param endTime
     * @param customerType
     * @return
     */
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (e.confirmResultsID = :confirmResultsID OR :confirmResultsID IS NULL) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.genderID IN :genderID OR coalesce(:genderID, null) IS NULL) "
            + "AND (e.customerType IN :customerType OR :customerType IS NULL OR :customerType IS '') "
            + "AND (cast(e.yearOfBirth as int) >= :yearOfBirthFrom OR :yearOfBirthFrom = 0 OR (:yearOfBirthFrom = -1 AND (e.yearOfBirth is '' OR e.yearOfBirth IS NULL))) "
            + "AND (cast(e.yearOfBirth as int) <= :yearOfBirthTo OR :yearOfBirthFrom = 0 OR (:yearOfBirthFrom = -1 AND (e.yearOfBirth is '' OR e.yearOfBirth IS NULL))) "
            + "AND ( "
            + "     (:confirmResultsID is null AND e.testResultsID is not null AND e.preTestTime is not null AND DATE_FORMAT(e.preTestTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.preTestTime, '%Y-%m-%d') <= :endTime) "
            + "  OR (:confirmResultsID is not null AND e.confirmResultsID = '2' AND e.confirmTime is not null AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :endTime) "
            + ") ")
    @Cacheable(value = "htc_visit_findPhuLuc01TT09")
    public List<HtcVisitEntity> findPhuLuc01TT09(
            @Param("siteID") Set<Long> siteID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("confirmResultsID") String confirmResultsID,
            @Param("yearOfBirthFrom") int yearOfBirthFrom,
            @Param("yearOfBirthTo") int yearOfBirthTo,
            @Param("genderID") Set<String> gender,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("customerType") String customerType);

    /**
     * Lấy thông tin người xét nghiệm theo ids
     *
     * @author pdThang
     * @param ids
     * @return
     */
    @Cacheable(value = "htc_visit_findByIds")
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false AND e.ID IN (:ID)")
    public List<HtcVisitEntity> findByIds(@Param("ID") List<Long> ids);

    public HtcVisitEntity findByCode(String code);

    public HtcVisitEntity findByCodeAndSiteID(String code, Long ID);

    /**
     * @author DSNAnh
     * @param confirmTestNo
     * @param id
     * @param siteID
     * @return
     */
    @Cacheable(value = "htc_visit_countByConfirmTestNoAndSiteID")
    @Query("SELECT COUNT(*) FROM HtcVisitEntity e WHERE e.id <> :id AND e.confirmTestNo = :confirmTestNo AND e.siteID = :siteID")
    public Integer countByConfirmTestNoAndSiteID(
            @Param("confirmTestNo") String confirmTestNo,
            @Param("id") Long id,
            @Param("siteID") Long siteID
    );

    /**
     * Tìm khách hàng được tạo gần nhất theo cơ sở, mã tự tăng lớn nhất
     *
     * @param siteID
     * @param siteCode
     * @auth TrangBN
     * @return
     */
    @Query(value = "SELECT v.* FROM htc_visit v INNER JOIN "
            + "(SELECT id, "
            + "   SUBSTRING_INDEX(e.code, '-', -1) as tutang "
            + "   FROM htc_visit as e "
            + "	WHERE e.site_id= :siteID "
            + "   AND e.code LIKE CONCAT(:siteCode, '%') "
            + "    ORDER BY tutang + 0 DESC LIMIT 1 ) a "
            + " ON v.id = a.id;", nativeQuery = true)
    @Cacheable(value = "htc_visit_findLastCodeBySiteID")
    public HtcVisitEntity findLastCodeBySiteID(@Param("siteID") Long siteID, @Param("siteCode") String siteCode);

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :advisoryeTimeFrom OR :advisoryeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :advisoryeTimeTo OR :advisoryeTimeTo IS NULL) "
            + "AND (e.testResultsID = :testResultsID OR :testResultsID IS '' OR :testResultsID IS NULL) "
            + "AND  (e.serviceID = :serviceID OR :serviceID IS '' OR :serviceID is NULL) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) ")
    @Cacheable(value = "htc_visit_findElog")
    public Page<HtcVisitEntity> findElog(
            @Param("siteID") Set<Long> siteID,
            @Param("advisoryeTimeFrom") String advisoryeTimeFrom,
            @Param("advisoryeTimeTo") String advisoryeTimeTo,
            @Param("serviceID") String serviceID,
            @Param("testResultsID") String testResultsID,
            Pageable pageable);

    @Cacheable(value = "htc_getFirst")
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.siteID IN :siteID AND advisoryeTime is not null  AND e.isRemove = false")
    public Page<HtcVisitEntity> getFirst(@Param("siteID") Set<Long> siteIds, Pageable pageable);

    //Bảng 1 MER
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime "
            + "AND e.siteID = :siteID "
            + "AND e.genderID = :genderID "
            + "AND e.referralSource LIKE CONCAT('%','2', '%') "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND ((:flag = '1' AND (YEAR(e.advisoryeTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.advisoryeTime) - e.yearOfBirth) <= :toAge )) OR (:flag = '2' AND (e.yearOfBirth IS NULL OR e.yearOfBirth IS ''))  OR (:flag = '3'  AND (YEAR(e.advisoryeTime) - e.yearOfBirth) < :toAge) OR :flag IS NULL OR :flag IS '' ) "
    )
    @Cacheable(value = "htc_findTable01MERIntroduced")
    public List<HtcVisitEntity> findTable01MERIntroduced(
            @Param("siteID") Long siteID,
            @Param("genderID") String genderID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("fromAge") Integer fromAge,
            @Param("toAge") Integer toAge,
            @Param("flag") String flag
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.siteID = :siteID "
            + "AND e.genderID = :genderID "
            + "AND e.referralSource LIKE CONCAT('%','2', '%') "
            + "AND (e.isAgreePreTest IS NOT NULL AND DATE_FORMAT(e.preTestTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime) "
            + "AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND ((:flag = '1' AND YEAR(e.preTestTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.preTestTime) - e.yearOfBirth) <= :toAge) OR (:flag = '2' AND (e.yearOfBirth IS NULL OR e.yearOfBirth IS '')) OR (:flag = '3'  AND (YEAR(e.preTestTime) - e.yearOfBirth) < :toAge) OR :flag IS NULL OR :flag IS '') "
    )
    @Cacheable(value = "htc_findTable01MERAgreed")
    public List<HtcVisitEntity> findTable01MERAgreed(
            @Param("siteID") Long siteID,
            @Param("genderID") String genderID,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("fromAge") Integer fromAge,
            @Param("toAge") Integer toAge,
            @Param("flag") String flag
    );

    //Bảng 2 MER
    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <= :endTime) "
            + "AND e.siteID = :siteID "
            + "AND e.genderID = :genderID "
            + "AND e.earlyHiv = :earlyHiv "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND ((:flag = '1' AND YEAR(e.confirmTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.confirmTime) - e.yearOfBirth) <= :toAge) OR (:flag = '2'  AND e.yearOfBirth IS NULL) OR (:flag = '3'  AND (YEAR(e.confirmTime) - e.yearOfBirth) < :toAge) OR :flag IS NULL OR :flag IS '') "
    )
    @Cacheable(value = "htc_visit_findTable02MER")
    public List<HtcVisitEntity> findTable02MER(
            @Param("siteID") Long siteID,
            @Param("earlyHiv") String earlyHiv,
            @Param("genderID") String genderID,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("fromAge") Integer fromAge,
            @Param("toAge") Integer toAge,
            @Param("flag") String flag
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >= :startTime AND DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <= :endTime) "
            + "AND e.siteID = :siteID "
            + "AND e.genderID IN ('1','2') "
            + "AND e.earlyHiv = :earlyHiv "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID is NULL) "
            + "AND (e.objectGroupID IN :objectGroupIDs OR coalesce(:objectGroupIDs, null) IS NULL) "
    )
    @Cacheable(value = "htc_visit_findTable03MER")
    public List<HtcVisitEntity> findTable03MER(
            @Param("siteID") Long siteID,
            @Param("earlyHiv") String earlyHiv,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") String objectGroupID,
            @Param("objectGroupIDs") Set<String> objectGroupIDs,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.resultsPatienTime IS NOT NULL "
            + "AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime "
            + "AND e.siteID = :siteID "
            + "AND e.genderID = :genderID "
            + "AND e.confirmResultsID = 2 "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND ((:flag = '1' AND YEAR(e.confirmTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.confirmTime) - e.yearOfBirth) <= :toAge) OR (:flag = '2'  AND e.yearOfBirth IS NULL)  OR (:flag = '3'  AND (YEAR(e.confirmTime) - e.yearOfBirth) < :toAge) OR :flag IS NULL OR :flag IS '') "
    )
    @Cacheable(value = "htc_visit_findTable04MERPositive")
    public List<HtcVisitEntity> findTable04MERPositive(
            @Param("siteID") Long siteID,
            @Param("genderID") String genderID,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("fromAge") Integer fromAge,
            @Param("toAge") Integer toAge,
            @Param("flag") String flag
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND ((e.resultsTime IS NOT NULL AND e.testResultsID = '1') OR (e.resultsPatienTime IS NOT NULL AND e.confirmResultsID = 1)) "
            + "AND ((e.resultsTime IS NOT NULL AND DATE_FORMAT(e.resultsTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime) OR (e.resultsPatienTime IS NOT NULL AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime)) "
            + "AND e.siteID = :siteID "
            + "AND e.genderID = :genderID "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND ((:flag = '1' AND (e.resultsPatienTime IS NOT NULL AND YEAR(e.confirmTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.confirmTime) - e.yearOfBirth) <= :toAge)"
            + "OR (e.resultsTime IS NOT NULL AND YEAR(e.preTestTime) - e.yearOfBirth >= :fromAge AND (YEAR(e.preTestTime) - e.yearOfBirth) <= :toAge)) OR (:flag = '2'  AND e.yearOfBirth IS NULL)  OR (:flag = '3'  AND (e.resultsPatienTime IS NOT NULL  AND (YEAR(e.confirmTime) - e.yearOfBirth) < :toAge)"
            + "OR (e.resultsTime IS NOT NULL AND (YEAR(e.preTestTime) - e.yearOfBirth) < :toAge))  OR :flag IS NULL OR :flag IS '') "
    )
    @Cacheable(value = "htc_visit_findTable04MERNegative")
    public List<HtcVisitEntity> findTable04MERNegative(
            @Param("siteID") Long siteID,
            @Param("genderID") String genderID,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("fromAge") Integer fromAge,
            @Param("toAge") Integer toAge,
            @Param("flag") String flag
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND e.resultsPatienTime IS NOT NULL "
            + "AND DATE_FORMAT(e.resultsPatienTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime "
            + "AND e.siteID = :siteID "
            + "AND e.genderID IN ('1','2') "
            + "AND e.confirmResultsID = 2 "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID is NULL) "
            + "AND (e.objectGroupID IN :objectGroupIDs OR coalesce(:objectGroupIDs, null) IS NULL) "
    )
    @Cacheable(value = "htc_visit_findTable05MERPositive")
    public List<HtcVisitEntity> findTable05MERPositive(
            @Param("siteID") Long siteID,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") String objectGroupID,
            @Param("objectGroupIDs") Set<String> objectGroupIDs,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = false "
            + "AND ((e.resultsTime IS NOT NULL AND e.testResultsID = '1') OR (e.resultsPatienTime IS NOT NULL AND e.confirmResultsID = 1)) "
            + "AND ((e.resultsTime IS NOT NULL AND DATE_FORMAT(e.resultsTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime) "
            + "OR (e.resultsPatienTime IS NOT NULL AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') BETWEEN :startTime AND :endTime)) "
            + "AND e.siteID = :siteID "
            + "AND e.genderID IN ('1','2') "
            + "AND e.serviceID = :serviceID "
            + "AND (e.objectGroupID = :objectGroupID OR :objectGroupID IS '' OR :objectGroupID is NULL) "
            + "AND (e.objectGroupID IN :objectGroupIDs OR coalesce(:objectGroupIDs, null) IS NULL) "
    )
    @Cacheable(value = "htc_visit_findTable05MERNegative")
    public List<HtcVisitEntity> findTable05MERNegative(
            @Param("siteID") Long siteID,
            @Param("serviceID") String serviceID,
            @Param("objectGroupID") String objectGroupID,
            @Param("objectGroupIDs") Set<String> objectGroupIDs,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = :remove "
            + "AND e.removeTranfer = :removeTranfer "
            + "AND e.exchangeTime IS NOT NULL "
            + "AND e.arrivalSiteID = :arrivalSite "
            + "AND (e.patientName LIKE CONCAT('%',:patientName, '%') OR :patientName IS '' OR :patientName IS NULL) "
            + "AND (e.confirmTestNo LIKE CONCAT('%',:confirmTestNo, '%') OR :confirmTestNo IS '' OR :confirmTestNo IS NULL) "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') = :confirmTime OR :confirmTime IS NULL) "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') = :exchangeTime OR :exchangeTime IS NULL) "
            + "AND (e.siteID  IN :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND ((:receiveStatus = '1' AND e.arrivalTime IS NULL) "
            + "OR (:receiveStatus = '2' AND e.arrivalTime IS NOT NULL) "
            + "OR :receiveStatus IS '' OR :receiveStatus IS NULL) "
    )
    @Cacheable(value = "htc_visit_findReceiveHtc")
    public Page<HtcVisitEntity> findReceiveHtc(
            @Param("remove") boolean remove,
            @Param("removeTranfer") boolean removeTranfer,
            @Param("patientName") String patientName,
            @Param("confirmTestNo") String confirmTestNo,
            @Param("confirmTime") String confirmTime,
            @Param("exchangeTime") String exchangeTime,
            @Param("siteID") Set<Long> siteID,
            @Param("receiveStatus") String receiveStatus,
            @Param("arrivalSite") Long arrivalSite,
            Pageable pageable);

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.isRemove = :remove "
            + "AND e.removeTranfer = :removeTranfer "
            + "AND e.exchangeTime IS NOT NULL "
            + "AND e.arrivalSiteID = :arrivalSite "
            + "AND ((:receiveStatus = '1' AND e.arrivalTime IS NULL) "
            + "OR (:receiveStatus = '2' AND e.arrivalTime IS NOT NULL) "
            + "OR :receiveStatus IS '' OR :receiveStatus IS NULL) "
    )
    @Cacheable(value = "htc_visit_findReceiveHtc")
    public Page<HtcVisitEntity> findReceiveHtcConnect(
            @Param("remove") boolean remove,
            @Param("removeTranfer") boolean removeTranfer,
            @Param("receiveStatus") String receiveStatus,
            @Param("arrivalSite") Long arrivalSite,
            Pageable pageable);

    @Query("SELECT count(e) FROM HtcVisitEntity e WHERE e.isRemove = :remove "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') >= :exchangeTimeFrom OR :exchangeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') <= :exchangeTimeTo OR :exchangeTimeTo IS NULL) "
            + "AND (e.therapyExchangeStatus = :therapyExchangeStatus OR :therapyExchangeStatus IS '' OR :therapyExchangeStatus IS NULL) "
            + "AND (e.confirmTestStatus = :confirmTestStatus OR :confirmTestStatus IS '' OR :confirmTestStatus IS NULL) "
            + "AND (e.feedbackStatus = :feedbackStatus OR :feedbackStatus IS '' OR :feedbackStatus IS NULL) "
            + "AND ((:isConfirm = true AND e.confirmTestStatus IN ('0','1','2')) OR :isConfirm = false) "
//           + "AND ((:isOpc = true AND e.arrivalSite is not null AND e.exchangeTime is not null) OR (:isOpc = true AND e.arrivalSite is not null AND e.registerTherapyTime is not null AND e.therapyNo is not null) OR :isOpc = false) "
            + "AND ((:isOpc = true AND e.resultsPatienTime is not null AND e.confirmResultsID = '2')  OR :isOpc = false) "
            + "AND ((:isNotReceive = true AND e.resultsPatienTime is null OR e.resultsPatienTime = '') OR :isNotReceive = false) "
            + "AND ((:isConfirmCreate = true AND e.sourceCreated = '3') OR :isConfirmCreate = false) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (e.confirmResultsID = :confirmResultsID OR :confirmResultsID IS '' OR :confirmResultsID IS NULL) "
            + "AND (e.testResultsID = :testResultsID OR :testResultsID IS '' OR :testResultsID IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') = :advisoryeTime OR :advisoryeTime IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :advisoryeTimeFrom OR :advisoryeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :advisoryeTimeTo OR :advisoryeTimeTo IS NULL) "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND ((:filter = 0 AND (e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL)) OR :filter = 1) "
            + "AND ((:filter = 1 AND (e.createdBY = :createdBY OR e.serviceID IN :serviceID OR coalesce(:serviceID, null) IS NULL)) OR :filter = 0) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL) "
            + "AND (e.patientName LIKE CONCAT('%',:patient_name, '%') OR :patient_name IS '' OR :patient_name IS NULL) "
            + "AND ((:gsphStatus = '0' AND e.pacSentDate IS NULL) OR (e.confirmResultsID = '2' AND :gsphStatus = '1' AND e.pacSentDate IS NOT NULL) OR :gsphStatus IS NULL OR :gsphStatus IS '' ) "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + "AND (((:isConfirm <> true AND :isOpc <> true AND :remove <> true ) AND ((:gsphStatus = '0' AND e.pacSentDate IS NULL) OR (:gsphStatus = '1' AND e.pacSentDate IS NOT NULL))) OR :gsphStatus IS NULL OR :gsphStatus IS '') ")
    @Cacheable(value = "htc_visit_find2")
    public Long find(
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("advisoryeTime") String advisoryeTime,
            @Param("advisoryeTimeFrom") String advisoryeTimeFrom,
            @Param("advisoryeTimeTo") String advisoryeTimeTo,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("exchangeTimeFrom") String exchangeTimeFrom,
            @Param("exchangeTimeTo") String exchangeTimeTo,
            @Param("patient_name") String patientName,
            @Param("serviceID") Set<String> serviceID,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("therapyExchangeStatus") String therapyExchangeStatus,
            @Param("confirmTestStatus") String confirmTestStatus,
            @Param("feedbackStatus") String feedbackStatus,
            @Param("confirmResultsID") String confirmResultsID,
            @Param("testResultsID") String testResultsID,
            @Param("isConfirm") boolean isConfirm,
            @Param("isOpc") boolean isOpc,
            @Param("remove") boolean isRemove,
            @Param("isNotReceive") boolean isNotReceive,
            @Param("isConfirmCreate") boolean isConfirmCreate,
            @Param("gsphStatus") String gsphStatus,
            @Param("objects") Set<String> objects,
            @Param("filter") int filter,
            @Param("createdBY") Long createdBY);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "getTable02TT03", allEntries = true)
        ,@CacheEvict(value = "getTablePhuluc01TT09", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getTT09Review", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
        ,@CacheEvict(value = "htc_getFirst", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByCodeSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTransferSuccess", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcVisitBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcTestBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPhuLuc01TT09", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findByIds", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByConfirmTestNoAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastCodeBySiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findElog", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERIntroduced", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERAgreed", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable02MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable03MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findReceiveHtc", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find2", allEntries = true)
    })
    public void delete(HtcVisitEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "getTable02TT03", allEntries = true)
        ,@CacheEvict(value = "getTablePhuluc01TT09", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getTT09Review", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
        ,@CacheEvict(value = "htc_getFirst", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByCodeSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTransferSuccess", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcVisitBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcTestBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPhuLuc01TT09", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findByIds", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByConfirmTestNoAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastCodeBySiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findElog", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERIntroduced", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERAgreed", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable02MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable03MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findReceiveHtc", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find2", allEntries = true)
    })
    public <S extends HtcVisitEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "getTable02TT03", allEntries = true)
        ,@CacheEvict(value = "getTablePhuluc01TT09", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getTT09Review", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
        ,@CacheEvict(value = "htc_getFirst", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByCodeSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastBysiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTransferSuccess", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcVisitBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findHtcTestBook", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findSoNguoiDuocXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findNhanKetQuaXetNghiemDuongTinhHIV", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findPhuLuc01TT09", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findByIds", allEntries = true)
        ,@CacheEvict(value = "htc_visit_countByConfirmTestNoAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findLastCodeBySiteID", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findElog", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERIntroduced", allEntries = true)
        ,@CacheEvict(value = "htc_findTable01MERAgreed", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable02MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable03MER", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable04MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERPositive", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findTable05MERNegative", allEntries = true)
        ,@CacheEvict(value = "htc_visit_findReceiveHtc", allEntries = true)
        ,@CacheEvict(value = "htc_visit_find2", allEntries = true)
    })
    public <S extends HtcVisitEntity> S save(S s);

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.qrcode = :code AND e.isRemove = false")
    public List<HtcVisitEntity> findByQR(@Param("code") String code);

    @Query("SELECT e FROM HtcVisitEntity e WHERE e.siteID IN (:site) AND e.pqmCode is null AND e.confirmResultsID = '2' AND e.isRemove is false ORDER BY e.createAT asc")
    public List<HtcVisitEntity> getForUpdateHub(@Param("site") Set<Long> siteIds, Pageable of);

    public List<HtcVisitEntity> findByEarlyJob(boolean earlyJob);
}
