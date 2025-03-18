package com.gms.repository;

import com.gms.entity.db.PqmDataEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface PqmDataRepository extends CrudRepository<PqmDataEntity, Long> {

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.indicatorCode = :indicatorCode "
            + "AND e.siteID = :siteID "
            + "AND e.sexID = :sexID "
            + "AND e.ageGroup = :ageGroup "
            + "AND e.objectGroupID = :objectGroupID ")
    public PqmDataEntity findOne(
            @Param("month") int month,
            @Param("year") int year,
            @Param("indicatorCode") String indicatorCode,
            @Param("siteID") Long siteID,
            @Param("sexID") String sexID,
            @Param("ageGroup") String ageGroup,
            @Param("objectGroupID") String objectGroupID);

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.indicatorCode = :indicatorCode "
            + "AND e.siteID = :siteID "
    )
    public List<PqmDataEntity> findByIndicator(
            @Param("month") int month,
            @Param("year") int year,
            @Param("indicatorCode") String indicatorCode,
            @Param("siteID") Long siteID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.siteID = :siteID "
    )
    public List<PqmDataEntity> findBySiteID(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Long siteID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.siteID IN (:siteID) "
    )
    public List<PqmDataEntity> findByIndicator(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_data e WHERE e.month = :month AND e.year = :year "
            + "AND e.site_id IN (:siteID) "
            + "AND (e.key_early IS NULL OR e.key_early <> 'true' )",
             nativeQuery = true)
    public void deleteBySiteIDAndMonthAndYear(
            @Param("siteID") List<Long> siteID,
            @Param("month") int month,
            @Param("year") int year
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_data e WHERE 1 = 1  "
            + "AND e.site_id IN (:siteID) "
            + "AND (e.key_early IS NULL OR e.key_early <> 'true' )",
             nativeQuery = true)
    public void deleteBySites(
            @Param("siteID") List<Long> siteID
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_data e WHERE e.province_id = :provinceID   ",
             nativeQuery = true)
    public void deleteByProvinceID(
            @Param("provinceID") String provinceID
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_data e WHERE e.month = :month AND e.year = :year "
            + "AND e.site_id = :siteID "
            + "AND e.key_early IS NOT NULL "
            + "AND e.key_early = 'true' ",
             nativeQuery = true)
    public void deleteBySiteIDAndMonthAndYearConfirmSite(
            @Param("siteID") Long siteID,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.siteID IN (:siteID) "
    )
    public Page<PqmDataEntity> findData(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID,
            Pageable pageable);

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.siteID IN (:siteID) "
    )
    public List<PqmDataEntity> findBySiteIDs(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.siteID IN (:siteID) "
            + "ORDER BY e.siteID ASC "
    )
    public List<PqmDataEntity> findBySiteIDsOrderBySiteID(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID IN (:siteID) OR coalesce(:siteID, null) IS NULL) "
            + "AND ( e.siteID IN (:siteSendID) OR coalesce(:siteSendID, null) IS NULL) "
    )
    public List<PqmDataEntity> findByIndicator(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID,
            @Param("siteSendID") Set<Long> siteSendID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID IN (:siteID) OR coalesce(:siteID, null) IS NULL) "
            + "AND ( e.siteID IN (:siteSendID) OR coalesce(:siteSendID, null) IS NULL)"
            + "AND e.indicatorCode = 'HTS_TST_RECENCY' "
    )
    public List<PqmDataEntity> findByIndicatorConfirm(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID,
            @Param("siteSendID") Set<Long> siteSendID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID IN (:siteID) OR coalesce(:siteID, null) IS NULL) "
            + "AND ( e.siteID IN (:siteSendID) OR coalesce(:siteSendID, null) IS NULL)"
            + "AND e.indicatorCode <> 'HTS_TST_RECENCY' "
    )
    public List<PqmDataEntity> findByIndicatorNotConfirm(
            @Param("month") int month,
            @Param("year") int year,
            @Param("siteID") Set<Long> siteID,
            @Param("siteSendID") Set<Long> siteSendID
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.ID = :ID")
    public PqmDataEntity findOne(@Param("ID") Long ID);

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID = :siteID ) "
            + "AND ( e.indicatorCode = :indicator ) "
            + "AND ( e.sexID = :gender ) "
            + "AND ( e.objectGroupID = :object ) "
            + "AND ( e.ageGroup = :ageGroup ) "
    )
    public PqmDataEntity findOne(
            @Param("siteID") Long siteID,
            @Param("indicator") String indicator,
            @Param("month") int month,
            @Param("year") int year,
            @Param("gender") String gender,
            @Param("object") String object,
            @Param("ageGroup") String ageGroup
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID = :siteID ) "
    )
    public List<PqmDataEntity> findByIndicator(
            @Param("siteID") Long siteID,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT e FROM PqmDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND ( e.siteID IN (:siteID) ) "
    )
    public List<PqmDataEntity> findByIndicators(
            @Param("siteID") List<Long> siteID,
            @Param("month") int month,
            @Param("year") int year
    );

}
