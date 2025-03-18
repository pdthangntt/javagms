package com.gms.repository;

import com.gms.entity.db.HtcConfirmLogEntity;
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
public interface HtcConfirmLogRepository extends CrudRepository<HtcConfirmLogEntity, Long> {
    
    @Query("SELECT e FROM HtcConfirmLogEntity e WHERE e.htcConfirmID = :confirmID ORDER BY e.createAt asc")
    public List<HtcConfirmLogEntity> findByConfirmID(@Param("confirmID") Long confirmID);
    
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
    @Query("SELECT e FROM HtcConfirmLogEntity e WHERE 1 = 1 "
            + " AND (e.htcConfirmID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.staffID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAt, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " ORDER BY e.createAt DESC "
    )
    public Page<HtcConfirmLogEntity> findAll(
            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            Pageable pageable);
}
