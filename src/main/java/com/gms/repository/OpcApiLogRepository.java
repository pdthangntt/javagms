package com.gms.repository;

import com.gms.entity.db.OpcApiLogEntity;
import java.util.List;
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
public interface OpcApiLogRepository extends CrudRepository<OpcApiLogEntity, Long> {

    @Query("SELECT e FROM OpcApiLogEntity e WHERE e.sourceSiteID = :id "
            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
    )
    public Page<OpcApiLogEntity> findAll(
            @Param("id") String id,
            @Param("status") String status,
            @Param("from") String from,
            @Param("to") String to,
            Pageable pageable);
    
    @Query("SELECT e FROM OpcApiLogEntity e WHERE e.sourceSiteID = :id "
            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " group by e.arvCode "
    )
    public List<OpcApiLogEntity> findARVs(
            @Param("id") String id,
            @Param("status") String status,
            @Param("from") String from,
            @Param("to") String to);
    
    @Query("SELECT count(e.ID) FROM OpcApiLogEntity e WHERE e.sourceSiteID = :id "
            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
    )
    public int countAllLogs(
            @Param("id") String id,
            @Param("status") String status,
            @Param("from") String from,
            @Param("to") String to);

}
