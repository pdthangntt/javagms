package com.gms.repository;

import com.gms.entity.db.HtcVisitLogEntity;
import com.gms.entity.db.PqmReportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface PqmReportRepository extends CrudRepository<PqmReportEntity, Long> {

    @Query("SELECT e FROM PqmReportEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.indicatorCode = :indicatorCode "
            + "AND e.siteID = :siteID "
            + "AND e.sexID = :sexID "
            + "AND e.ageGroup = :ageGroup "
            + "AND e.keyPopulationID = :keyPopulationID")
    public PqmReportEntity findOne(@Param("month") int month,
            @Param("year") int year,
            @Param("indicatorCode") String indicatorCode,
            @Param("siteID") Long siteID,
            @Param("sexID") String sexID,
            @Param("ageGroup") String ageGroup,
            @Param("keyPopulationID") String keyPopulationID);
}
