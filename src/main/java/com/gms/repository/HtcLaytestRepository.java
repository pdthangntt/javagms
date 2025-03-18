package com.gms.repository;

import com.gms.entity.db.HtcLaytestEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
public interface HtcLaytestRepository extends CrudRepository<HtcLaytestEntity, Long> {

    /**
     *
     * @author pdThang
     * @param siteID
     * @param code
     * @param patientName
     * @param isRemove
     * @param visitRemove
     * @param staffID
     * @param siteVisitID
     * @param advisoryeTime
     * @param status
     * @param sendStatus
     * @param advisoryeTimeFrom
     * @param advisoryeTimeTo
     * @param isSampleSentDate
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = :remove "
            + "AND (e.siteVisitID = :siteVisitID OR :siteVisitID IS '' OR :siteVisitID IS NULL) "
            + "AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.patientName LIKE CONCAT('%',:patient_name, '%') OR :patient_name IS '' OR :patient_name IS NULL) "
            + "AND (e.createdBY = :staffID OR :staffID IS '' OR :staffID IS NULL) "
            + "AND (( :visitRemove = 1 AND e.visitRemove = false) OR ( :visitRemove = 2 AND e.visitRemove = true) OR :visitRemove = 0) "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') = :advisoryeTime OR :advisoryeTime IS NULL) "
            + "AND ((:sendStatus = '1' AND (e.sampleSentDate IS NULL OR e.sampleSentDate = '') AND e.isAgreeTest = '1' AND e.testResultsID = '2') OR "
            + "(:sendStatus = '2' AND (e.sampleSentDate IS NOT NULL OR e.sampleSentDate <> '') AND (e.siteVisitID IS NOT NULL OR e.siteVisitID <> '') AND e.isAgreeTest = '1' AND e.testResultsID = '2' AND e.acceptDate IS NULL) OR "
            + "(:sendStatus = '3' AND (e.sampleSentDate IS NOT NULL OR e.sampleSentDate <> '') AND (e.siteVisitID IS NOT NULL OR e.siteVisitID <> '') AND (e.acceptDate IS NOT NULL OR e.acceptDate <> '') AND e.isAgreeTest = '1' AND e.testResultsID = '2') OR :sendStatus IS '' OR :sendStatus IS NULL) "
            + "AND ((:status = '1' AND e.acceptDate IS NOT NULL) OR (:status = '0' AND e.acceptDate IS NULL) OR :status IS NULL) "
            + "AND (DATE_FORMAT(e.sampleSentDate, '%Y-%m-%d') >= :advisoryeTimeFrom OR :advisoryeTimeFrom IS NULL) "
            + "AND (DATE_FORMAT(e.sampleSentDate, '%Y-%m-%d') <= :advisoryeTimeTo OR :advisoryeTimeTo IS NULL) "
            + "AND ((:isSampleSentDate = true AND e.sampleSentDate IS NOT NULL) OR :isSampleSentDate = false) "
    )
    public Page<HtcLaytestEntity> find(
            @Param("siteID") Set<Long> siteID,
            @Param("code") String code,
            @Param("patient_name") String patientName,
            @Param("remove") boolean isRemove,
            @Param("visitRemove") int visitRemove,
            @Param("staffID") Long staffID,
            @Param("siteVisitID") String siteVisitID,
            @Param("advisoryeTime") String advisoryeTime,
            @Param("sendStatus") String sendStatus,
            @Param("status") String status,
            @Param("advisoryeTimeFrom") String advisoryeTimeFrom,
            @Param("advisoryeTimeTo") String advisoryeTimeTo,
            @Param("isSampleSentDate") boolean isSampleSentDate,
            Pageable pageable);

    /**
     * Check if exist code in specific site in case add new
     *
     * @param siteID
     * @param code
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM HtcLaytestEntity e WHERE e.siteID=:siteID AND e.code = :code")
    public Boolean existByCodeAndSiteID(@Param("siteID") Long siteID, @Param("code") String code);

    /**
     * Lấy mã của khách hàng được nhân viên tạo theo mã tự tăng lớn nhất
     *
     * @param siteID
     * @param createdBy
     * @param staffCode
     * @auth TrangBN
     * @return
     */
    @Query(value = "SELECT v.* FROM htc_laytest v INNER JOIN "
            + "(SELECT id, "
            + "   SUBSTRING_INDEX(e.code, '-', -1) as tutang "
            + "   FROM htc_laytest as e "
            + "	WHERE e.site_id= :siteID "
            + "   AND e.code LIKE CONCAT(:staffCode, '%') "
            + "   AND e.created_by  = :createdBy "
            + "   ORDER BY tutang + 0 DESC LIMIT 1 ) a "
            + " ON v.id = a.id;", nativeQuery = true)
    public HtcLaytestEntity findLastBysiteIDCreateBy(@Param("siteID") Long siteID, @Param("createdBy") Long createdBy, @Param("staffCode") String staffCode);

    /**
     * Get test laytest book information
     *
     * @param start
     * @param end
     * @param siteID
     * @param staffID
     * @param objects
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND e.advisoryeTime >= :start "
            + "AND e.advisoryeTime <= :end "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND e.createdBY = :staffID "
            + "AND (e.objectGroupID IN :objects OR coalesce(:objects, null) IS NULL) "
            + "ORDER BY e.advisoryeTime DESC, e.patientName DESC "
    )
    public List<HtcLaytestEntity> findLaytestBook(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("objects") List<String> objects);

    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.ID IN :ids")
    public List<HtcLaytestEntity> findAllByIDs(@Param("ids") Set<Long> ids);

    //30/10/2019
    /**
     * DS KH tư vấn trước XN theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
    )
    public List<HtcLaytestEntity> findSoNguoiTuVanTruocXN(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID
    );

    /**
     * DS KH dược XN dương tính theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND e.confirmResultsID = 2 "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
    )
    public List<HtcLaytestEntity> findSoNguoiDuocXNDuongTinh(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID);

    /**
     * DS KH XN được giới thiệu bởi bạn tình bạn chích theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND e.referralSource LIKE CONCAT('%','2', '%')  "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
    )
    public List<HtcLaytestEntity> findSoNguoiGioiThieuBanChich(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID);

    /**
     * DS KH được chuyển gửi điều trị thành công theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND e.exchangeStatus = 1 "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
    )
    public List<HtcLaytestEntity> findSoNguoiChuyenGuiDieuTri(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID);

    @Query(value = "SELECT * FROM htc_laytest e WHERE e.is_remove = false "
            + "AND (DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= :end) "
            + "AND e.created_by = :staffID "
            + "AND (e.site_id = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.year_of_birth IS NULL)"
            + "AND (e.confirm_results_id IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND (e.gender_id IN :genderID OR coalesce(:genderID, null) IS NULL) ",
             nativeQuery = true)
    public List<HtcLaytestEntity> findUndefined(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("genderID") Set<String> genderID
    );

    @Query(value = "SELECT * FROM "
            + "(SELECT * FROM htc_laytest e WHERE e.is_remove = false "
            + "AND (DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= :end) "
            + "AND e.created_by = :staffID "
            + "AND (e.site_id = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.confirm_results_id IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND e.gender_id IN ('1', '2')) as tbl "
            + "WHERE (tbl.gender_id IN :genderID OR coalesce(:genderID, null) IS NULL) "
            + "AND (YEAR(tbl.advisory_time) - tbl.year_of_birth) < 1 ",
             nativeQuery = true)
    public List<HtcLaytestEntity> findUnderOne(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("genderID") Set<String> genderID
    );

    @Query(value = "SELECT * FROM "
            + "(SELECT * FROM htc_laytest e WHERE e.is_remove = false "
            + "AND (DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= :end) "
            + "AND e.created_by = :staffID "
            + "AND (e.site_id = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.confirm_results_id IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND e.gender_id IN ('1', '2')) as tbl "
            + "WHERE (tbl.gender_id IN :genderID OR coalesce(:genderID, null) IS NULL) "
            + "AND (YEAR(tbl.advisory_time) - tbl.year_of_birth) >= :minAge "
            + "AND (YEAR(tbl.advisory_time) - tbl.year_of_birth) <= :maxAge ",
             nativeQuery = true)
    public List<HtcLaytestEntity> findAgeRange(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("genderID") Set<String> genderID,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge
    );

    @Query(value = "SELECT * FROM "
            + "(SELECT * FROM htc_laytest e WHERE e.is_remove = false "
            + "AND (DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= :end) "
            + "AND e.created_by = :staffID "
            + "AND (e.site_id = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.confirm_results_id IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND e.gender_id IN ('1', '2')) as tbl "
            + "WHERE (YEAR(tbl.advisory_time) - tbl.year_of_birth) >= :minAge "
            + "AND (YEAR(tbl.advisory_time) - tbl.year_of_birth) <= :maxAge ",
             nativeQuery = true)
    public List<HtcLaytestEntity> findAgeRangeSum(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge
    );

    /**
     *
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param objectGroupID
     * @param confirmResultsID
     * @param genderID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
            + "AND (e.confirmResultsID IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND (e.genderID IN :genderID OR coalesce(:genderID, null) IS NULL) "
    )
    public List<HtcLaytestEntity> findTable02MER(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("genderID") Set<String> genderID
    );

    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.objectGroupID IN :objectGroupID OR coalesce(:objectGroupID, null) IS NULL)"
            + "AND (e.confirmResultsID IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND e.genderID IN ('1', '2') "
    )
    public List<HtcLaytestEntity> findTable02MERSum(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("objectGroupID") Set<String> objectGroupID,
            @Param("confirmResultsID") Set<String> confirmResultsID
    );
    
    /**
     * Hiển thị DS theo các nhóm đối tượng còn lại (nam or nữ)
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param confirmResultsID
     * @param genderID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.objectGroupID NOT IN (1,2,5,9,10,6,11,12,3,7))"
            + "AND (e.confirmResultsID IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND (e.genderID IN :genderID OR coalesce(:genderID, null) IS NULL) "
    )
    public List<HtcLaytestEntity> findTable02MEROther(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID,
            @Param("genderID") Set<String> genderID
    );
    
    /**
     * Hiển thị DS theo các nhóm đối tượng còn lại (tổng)
     * @author DSNAnh
     * @param siteID
     * @param staffID
     * @param start
     * @param end
     * @param confirmResultsID
     * @return
     */
    @Query("SELECT e FROM HtcLaytestEntity e WHERE e.remove = false "
            + "AND (DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') >= :start AND DATE_FORMAT(e.advisoryeTime, '%Y-%m-%d') <= :end) "
            + "AND e.createdBY = :staffID "
            + "AND (e.siteID = :siteID OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.objectGroupID NOT IN (1,2,5,9,10,6,11,12,3,7))"
            + "AND (e.confirmResultsID IN :confirmResultsID OR coalesce(:confirmResultsID, null) IS NULL) "
            + "AND (e.genderID IN ('1','2')) "
    )
    public List<HtcLaytestEntity> findTable02MEROtherSum(
            @Param("siteID") Set<Long> siteID,
            @Param("staffID") Long staffID,
            @Param("start") String start,
            @Param("end") String end,
            @Param("confirmResultsID") Set<String> confirmResultsID
    );
    
    @Override
    @Caching(evict = {
        @CacheEvict(value = "getDashboardChart01", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart02", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart03", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart04", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
    })
    public void delete(HtcLaytestEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "getDashboardChart01", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart02", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart03", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart04", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
    })
    public void deleteById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "getDashboardChart01", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart02", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart03", allEntries = true)
        ,@CacheEvict(value = "getDashboardChart04", allEntries = true)
        ,@CacheEvict(value = "getMerTable01", allEntries = true)
        ,@CacheEvict(value = "getMerTable02", allEntries = true)
    })
    public <S extends HtcLaytestEntity> S save(S s);

}
