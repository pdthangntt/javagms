package com.gms.repository;

import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdThang
 */
@Repository
public interface HtcConfirmRepository extends CrudRepository<HtcConfirmEntity, Long> {

    @Override
    public List<HtcConfirmEntity> findAll();

    /**
     *
     * @author pdThang
     * @param siteID
     * @param code
     * @param fullname
     * @param sourceID
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param acceptDateFrom
     * @param acceptDateTo
     * @param sourceSampleDateFrom
     * @param sourceSampleDateTo
     * @param requestHtcTimeFrom
     * @param requestHtcTimeTo
     * @param resultsID
     * @param sourceSiteID
     * @param confirmStatus
     * @param isWait
     * @param isReceived
     * @param isResult
     * @param isRemove
     * @param isUpdate
     * @param statusFeedback
     * @param gsphStatus
     * @param statusRequest
     * @param isRequestAdditional
     * @param pageable
     * @return
     */
    @Query(" SELECT e FROM HtcConfirmEntity e WHERE e.remove = :isRemove "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " AND (e.sourceID LIKE CONCAT('%',:sourceID, '%') OR :sourceID IS '' OR :sourceID IS NULL) "
            + " AND (e.fullname LIKE CONCAT('%',:fullname, '%') OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :confirmTimeFrom OR :confirmTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :confirmTimeTo OR :confirmTimeTo IS NULL) "
            + " AND (DATE_FORMAT(e.acceptDate, '%Y-%m-%d') >= :acceptDateFrom OR :acceptDateFrom IS NULL) "
            + " AND (DATE_FORMAT(e.acceptDate, '%Y-%m-%d') <= :acceptDateTo OR :acceptDateTo IS NULL) "
            + " AND (DATE_FORMAT(e.sourceSampleDate, '%Y-%m-%d') >= :sourceSampleDateFrom OR :sourceSampleDateFrom IS NULL OR :sourceSampleDateFrom IS '') "
            + " AND (DATE_FORMAT(e.sourceSampleDate, '%Y-%m-%d') <= :sourceSampleDateTo OR :sourceSampleDateTo IS NULL OR :sourceSampleDateTo IS '') "
            + " AND (DATE_FORMAT(e.requestHtcTime, '%Y-%m-%d') >= :requestHtcTimeFrom OR :requestHtcTimeFrom IS NULL OR :requestHtcTimeFrom IS '') "
            + " AND (DATE_FORMAT(e.requestHtcTime, '%Y-%m-%d') <= :requestHtcTimeTo OR :requestHtcTimeTo IS NULL OR :requestHtcTimeTo IS '') "
            + " AND (e.resultsID = :resultsID OR :resultsID IS '' OR :resultsID IS NULL) "
            + " AND (e.sourceSiteID = :sourceSiteID OR :sourceSiteID IS 0 OR :sourceSiteID IS NULL) "
            + " AND (e.siteID = :siteID) "
            + " AND ( (:statusRequest = '1' AND e.requestConfirmTime is not null and e.updateConfirmTime is null) OR (:statusRequest = '2' AND e.requestConfirmTime is not null and e.updateConfirmTime is not null ) OR :statusRequest is null ) "
            + " AND ((:confirmStatus = '3' AND e.resultsID IS NOT NULL AND e.resultsReturnTime IS NOT NULL ) OR coalesce(:confirmStatus, NULL) IS NULL OR :confirmStatus <> '3') "
            + " AND ((:confirmStatus = '2' AND e.resultsID IS NOT NULL AND e.resultsReturnTime IS NULL ) OR coalesce(:confirmStatus, NULL) IS NULL OR :confirmStatus <> '2') "
            + " AND ((:confirmStatus = '1' AND e.acceptDate IS NOT NULL AND e.resultsID IS NULL AND e.resultsReturnTime IS NULL ) OR coalesce(:confirmStatus, NULL) IS NULL OR :confirmStatus <> '1' ) "
            + " AND ((:confirmStatus = '0' AND e.acceptDate IS NULL AND e.resultsID IS NULL AND e.resultsReturnTime IS NULL ) OR coalesce(:confirmStatus, NULL) IS NULL OR :confirmStatus <> '0' ) "
            + " AND ((:isWait = true AND e.acceptDate IS NULL AND e.resultsID IS NULL) OR (:isWait = false AND (e.acceptDate IS NOT NULL OR e.resultsID IS NOT NULL))) "
            + " AND ((:isReceived = true AND e.acceptDate IS NOT NULL AND e.resultsID IS NULL) OR :isReceived = false) "
            + " AND ((:isResult = true AND e.resultsID IS NOT NULL) OR :isResult = false) "
            + " AND ((:isUpdate = true AND e.feedbackStatus IS NOT NULL) OR :isUpdate = false) "
            + " AND (e.feedbackStatus = :statusFeedback OR :statusFeedback IS '' OR :statusFeedback IS NULL) "
            + " AND ((:isResult = true AND (:confirmStatus = '2' OR :confirmStatus = '3') AND ((:gsphStatus = '0' AND e.pacSentDate IS NULL) OR (:gsphStatus = '1' AND e.pacSentDate IS NOT NULL))) OR :isResult = false OR :gsphStatus IS NULL OR :gsphStatus IS '') "
            + " AND (e.virusLoad = :virusLoad OR :virusLoad IS NULL OR :virusLoad = '') "
            + " AND (:earlyDiagnose = '0' and (e.earlyDiagnose is null OR e.earlyDiagnose = '') OR e.earlyDiagnose = :earlyDiagnose OR :earlyDiagnose IS NULL OR :earlyDiagnose = '') "
            + " AND ((:earlyHivStatus = '0' AND e.earlyHivDate is null) OR (:earlyHivStatus = '1' AND e.earlyHivDate is not null) OR (:earlyHivStatus is null ) OR (:earlyHivStatus = ''))  "
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >= :earlyHivDateFrom OR :earlyHivDateFrom IS NULL) "
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <= :earlyHivDateTo OR :earlyHivDateTo IS NULL) "
            + " AND (e.earlyHiv = :earlyHiv OR :earlyHiv IS NULL OR :earlyHiv = '') "
            + " AND ((:isRequestAdditional = true AND e.requestHtcTime IS NOT NULL AND e.verifyHtcTime IS NULL) OR :isRequestAdditional = false) "
    )
    public Page<HtcConfirmEntity> find(
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("fullname") String fullname,
            @Param("sourceID") String sourceID,
            @Param("confirmTimeFrom") String confirmTimeFrom,
            @Param("confirmTimeTo") String confirmTimeTo,
            @Param("acceptDateFrom") String acceptDateFrom,
            @Param("acceptDateTo") String acceptDateTo,
            @Param("sourceSampleDateFrom") String sourceSampleDateFrom,
            @Param("sourceSampleDateTo") String sourceSampleDateTo,
            @Param("requestHtcTimeFrom") String requestHtcTimeFrom,
            @Param("requestHtcTimeTo") String requestHtcTimeTo,
            @Param("resultsID") String resultsID,
            @Param("sourceSiteID") Long sourceSiteID,
            @Param("confirmStatus") String confirmStatus,
            @Param("isWait") boolean isWait,
            @Param("isReceived") boolean isReceived,
            @Param("isResult") boolean isResult,
            @Param("isRemove") boolean isRemove,
            @Param("isUpdate") boolean isUpdate,
            @Param("statusFeedback") String statusFeedback,
            @Param("gsphStatus") String gsphStatus,
            @Param("statusRequest") String statusRequest,
            @Param("isRequestAdditional") boolean isRequestAdditional,
            @Param("virusLoad") String virusLoad,
            @Param("earlyDiagnose") String earlyDiagnose,
            @Param("earlyHivStatus") String earlyHivStatus,
            @Param("earlyHivDateFrom") String earlyHivDateFrom,
            @Param("earlyHivDateTo") String earlyHivDateTo,
            @Param("earlyHiv") String earlyHiv,
            Pageable pageable);

    @Query(" SELECT e FROM HtcConfirmEntity e WHERE e.remove = false "
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >= :earlyHivTimeFrom OR :earlyHivTimeFrom IS NULL) "
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <= :earlyHivTimeTo OR :earlyHivTimeTo IS NULL) "
            + " AND e.resultsID = '2' "
            + " AND (e.sourceSiteID IN (:sourceSiteID) OR coalesce(:sourceSiteID, null) IS NULL) "
            + " AND (e.siteID = :siteID) "
    )
    public Page<HtcConfirmEntity> findEarlyHIV(
            @Param("siteID") Long siteID,
            @Param("earlyHivTimeFrom") String earlyHivTimeFrom,
            @Param("earlyHivTimeTo") String earlyHivTimeTo,
            @Param("sourceSiteID") List<Long> sourceSiteID,
            Pageable pageable);

    /**
     * @author DSNAnh Check tồn tại mã xn tại cơ sở xn
     * @param siteID
     * @param sourceID
     * @return
     */
    @Query("SELECT COUNT(e) FROM HtcConfirmEntity e WHERE e.siteID=:siteID AND e.sourceID = :sourceID")
    public Long countBySourceIDAndSiteID(@Param("siteID") Long siteID, @Param("sourceID") String sourceID);

    /**
     * Check tồn tại mã KH sàng lọc tại update khẳng định
     *
     * @param siteID
     * @param sourceID
     * @param id
     * @return
     */
    @Query("SELECT COUNT(e) FROM HtcConfirmEntity e WHERE e.id <> :id AND e.siteID=:siteID AND e.sourceID = :sourceID")
    public Long countBySourceIDSiteID(@Param("siteID") Long siteID, @Param("sourceID") String sourceID, @Param("id") Long id);

    /**
     * Check tồn tại mã KH khẳng định tại thêm mới khẳng định
     *
     * @param code
     * @return
     */
    @Query("SELECT COUNT(e) FROM HtcConfirmEntity e WHERE e.code = :code")
    public Long countByCodes(@Param("code") String code);

    /**
     * Check if exist code in specific site in case update
     *
     * @param siteID
     * @param code
     * @param id
     * @return
     */
    @Query("SELECT COUNT(e) FROM HtcConfirmEntity e WHERE e.id <> :id  AND e.siteID=:siteID AND e.code = :code")
    public Long countByCodeSiteID(@Param("siteID") Long siteID, @Param("code") String code, @Param("id") Long id);

    @Override
    public void delete(HtcConfirmEntity t);

    /**
     * Lấy danh sách KHXNKĐ dương tính
     *
     * @auth DSNAnh
     * @param start
     * @param end
     * @param siteID
     * @return list
     */
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.remove = false "
            + "AND e.resultsID = 2 "
            + "AND e.siteID = :siteID "
            + "AND e.confirmTime >= :start "
            + "AND e.confirmTime <= :end "
            + "ORDER BY confirmTime ASC")
    public List<HtcConfirmEntity> findPositive(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("siteID") Long siteID);

    public HtcConfirmEntity findBySourceIDAndSourceSiteID(String sourceID, Long sourceSiteID);

    /**
     * find source
     *
     * @auth vvThanh
     * @param siteID
     * @param sourceID
     * @param sourceSiteID
     * @return
     */
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.siteID=:siteID AND e.sourceID = :sourceID AND e.sourceSiteID=:sourceSiteID")
    public HtcConfirmEntity findBySiteIDAndSourceIDAndSourceSiteID(@Param("siteID") Long siteID, @Param("sourceID") String sourceID, @Param("sourceSiteID") Long sourceSiteID);

    /**
     * Tìm theo mã xét nghiệm khẳng định
     *
     * @param siteID
     * @param sourceID
     * @param sourceSiteID
     * @param confirmCode
     * @return
     */
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.siteID=:siteID AND e.sourceID = :sourceID AND e.sourceSiteID=:sourceSiteID AND e.code = :confirmCode")
    public HtcConfirmEntity findBySiteIDAndSourceIDAndSourceSiteID(@Param("siteID") Long siteID, @Param("sourceID") String sourceID, @Param("sourceSiteID") Long sourceSiteID, @Param("confirmCode") String confirmCode);

    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.sourceID = :sourceID AND e.sourceSiteID=:sourceSiteID")
    public HtcConfirmEntity findBySourceIDSourceSiteID(@Param("sourceID") String sourceID, @Param("sourceSiteID") Long sourceSiteID);

    /**
     *
     * @author pdThang
     * @return
     */
    @Override
    public List<HtcConfirmEntity> findAllById(Iterable<Long> ids);

    /**
     * Trả kết quả cho cơ sở gửi mẫu
     *
     * @author pdThang
     * @param ids
     * @return
     */
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.ID IN (:ID)"
            + "ORDER BY e.confirmTime")
    public List<HtcConfirmEntity> findAllByIdOrderByConfirmTime(@Param("ID") Iterable<Long> ids);

    /**
     * Tìm khách hàng được tạo gần nhất theo cơ sở, mã tự tăng lớn nhất
     *
     * @param siteID
     * @param siteCode
     * @auth TrangBN
     * @return
     */
    @Query(value = "SELECT v.* FROM htc_confirm v INNER JOIN "
            + "(SELECT id, "
            + "   SUBSTRING_INDEX(e.code, '-', -1) as tutang "
            + "   FROM htc_confirm as e "
            + "	WHERE e.site_id= :siteID "
            + "   AND e.code LIKE CONCAT(:siteCode, '%') "
            + "    ORDER BY tutang + 0 DESC LIMIT 1 ) a "
            + " ON v.id = a.id;", nativeQuery = true)
    public HtcConfirmEntity findLastCodeBySiteID(@Param("siteID") Long siteID, @Param("siteCode") String siteCode);

    /**
     * Lấy dữ liệu sổ xét nghiệm HIV
     *
     * @author DSNAnh
     * @param start
     * @param end
     * @param siteid
     * @param fullname
     * @param code
     * @param sourceID
     * @param sourceSiteID
     * @param resultsID
     * @return
     */
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.remove = false "
            + "AND e.confirmTime >= :start "
            + "AND e.confirmTime <= :end "
            + "AND e.siteID = :siteID "
            + "AND (e.fullname LIKE CONCAT('%',:fullname, '%') OR :fullname IS '' OR :fullname IS NULL) "
            + "AND (e.code LIKE CONCAT('%',:code, '%')  OR :code IS '' OR :code IS NULL) "
            + "AND (e.sourceID LIKE CONCAT('%',:sourceID, '%')  OR :sourceID IS '' OR :sourceID IS NULL) "
            + "AND (e.sourceSiteID = :sourceSiteID  OR :sourceSiteID IS '' OR :sourceSiteID IS NULL) "
            + "AND (e.resultsID = :resultsID  OR :resultsID IS '' OR :resultsID IS NULL) "
            + "ORDER BY e.confirmTime ASC, e.code ASC"
    )
    public List<HtcConfirmEntity> findConfirmBook(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("siteID") Long siteid,
            @Param("fullname") String fullname,
            @Param("code") String code,
            @Param("sourceID") String sourceID,
            @Param("sourceSiteID") Long sourceSiteID,
            @Param("resultsID") String resultsID
    );

    @Cacheable(value = "htc_confirm_getFirst")
    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.siteID IN :siteID AND e.confirmTime is not null AND e.remove = false")
    public Page<HtcConfirmEntity> getFirst(@Param("siteID") Set<Long> siteIds, Pageable pageable);

    @Query("SELECT e FROM HtcConfirmEntity e WHERE e.qrcode = :code AND e.remove = false")
    public List<HtcConfirmEntity> findByQR(@Param("code") String code);

    public List<HtcConfirmEntity> findByEarlyJob(boolean earlyJob);
}
