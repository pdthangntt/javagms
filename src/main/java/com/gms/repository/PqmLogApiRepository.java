package com.gms.repository;

import com.gms.entity.db.PqmLogApiEntity;
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
public interface PqmLogApiRepository extends CrudRepository<PqmLogApiEntity, Long> {

    @Query("SELECT e FROM PqmLogApiEntity e WHERE 1 = 1 "
            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
    )
    public Page<PqmLogApiEntity> find(
            @Param("status") String status,
            @Param("from") String from,
            @Param("to") String to,
            Pageable pageable);
    
//    @Query("SELECT e FROM PqmLogApiEntity e WHERE e.sourceSiteID = :id "
//            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
//            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
//            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
//            + " group by e.arvCode "
//    )
//    public List<PqmLogApiEntity> findARVs(
//            @Param("id") String id,
//            @Param("status") String status,
//            @Param("from") String from,
//            @Param("to") String to);
//    
//    @Query("SELECT count(e.ID) FROM PqmLogApiEntity e WHERE e.sourceSiteID = :id "
//            + "AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) "
//            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= :from OR :from IS NULL) "
//            + "AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= :to OR :to IS NULL) "
//    )
//    public int countAllLogs(
//            @Param("id") String id,
//            @Param("status") String status,
//            @Param("from") String from,
//            @Param("to") String to);

}
