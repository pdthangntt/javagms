package com.gms.repository;

import com.gms.entity.db.StaffActivityEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface StaffActivityRepository extends CrudRepository<StaffActivityEntity, Long> {

    @Query("SELECT e FROM StaffActivityEntity e WHERE e.createAT < :create_at")
    public List<StaffActivityEntity> findActivitiesOlderThanAMonth(@Param("create_at") Date date, Sort by);

    @Query("SELECT e FROM StaffActivityEntity e WHERE e.userID  =:userID")
    public Page<StaffActivityEntity> find(@Param("userID") Long userID, Pageable pageable);

    /**
     * pdThang 
     * Lấy toàn bô ls
     * @param ID
     * @param from
     * @param to
     * @param staffID
     * @param requestMethod
     * @param pageable
     * @return 
     */
    @Query("SELECT e FROM StaffActivityEntity e WHERE 1 = 1 "
            + " AND (e.ID = :ID OR :ID is 0 OR :ID IS NULL) "
            + " AND (e.userID = :staffID OR :staffID is 0 OR :staffID IS NULL) "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " AND ((:requestMethod = '1' AND e.requestMethod = 'GET') OR (:requestMethod = '2' AND e.requestMethod = 'POST') OR :requestMethod IS NULL OR :requestMethod IS '') "
            + " ORDER BY e.createAT DESC "
    )
    public Page<StaffActivityEntity> findAll(
            @Param("ID") Long ID, 
            @Param("from") String from, 
            @Param("to") String to,
            @Param("staffID") Long staffID,
            @Param("requestMethod") String requestMethod,
            Pageable pageable);

}
