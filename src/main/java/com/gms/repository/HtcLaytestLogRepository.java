package com.gms.repository;

import com.gms.entity.db.HtcConfirmLogEntity;
import com.gms.entity.db.HtcLaytestLogEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author TrangBN
 */
public interface HtcLaytestLogRepository extends CrudRepository<HtcLaytestLogEntity, Long>  {
    
    @Query("SELECT e FROM HtcLaytestLogEntity e WHERE e.htcLaytestID = :layestID ORDER BY e.createAt asc")
    public List<HtcConfirmLogEntity> findByConfirmID(@Param("layestID") Long confirmID);
    
    @Query("SELECT e FROM HtcLaytestLogEntity e WHERE e.htcLaytestID = :laytestID ORDER BY e.createAt asc")
    public List<HtcLaytestLogEntity> findByLaytestID(@Param("laytestID") Long laytestID);
    
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
    @Query("SELECT e FROM HtcLaytestLogEntity e WHERE 1 = 1 "
            + " AND (e.htcLaytestID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.staffID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " ORDER BY e.createAt DESC "
    )
    public Page<HtcLaytestLogEntity> findAll(
            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            Pageable pageable);
}
