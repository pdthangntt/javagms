package com.gms.repository;

import com.gms.entity.db.HtcVisitLogEntity;
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
public interface HtcVisitLogRepository extends CrudRepository<HtcVisitLogEntity, Long> {

    @Query("SELECT e FROM HtcVisitLogEntity e WHERE e.htcVisitID = :visitID ORDER BY e.createAt asc")
    public List<HtcVisitLogEntity> findByVisitID(@Param("visitID") Long visitID);
    
    /**
     * pdThang 
     * Lấy toàn bô ls
     * @param ID
     * @param from
     * @param to
     * @param staffID
     * @param pageable
     * @return 
     */
    @Query("SELECT e FROM HtcVisitLogEntity e WHERE 1 = 1 "
            + " AND (e.htcVisitID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.staffID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " ORDER BY e.createAt DESC "
    )
    public Page<HtcVisitLogEntity> findAll(
            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            Pageable pageable);

}
