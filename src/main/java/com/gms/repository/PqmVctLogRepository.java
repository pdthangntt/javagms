package com.gms.repository;

import com.gms.entity.db.PqmVctLogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface PqmVctLogRepository extends CrudRepository<PqmVctLogEntity, Long> {

    @Query("SELECT e FROM PqmVctLogEntity e WHERE e.targetID = :visitID ORDER BY e.createAt asc")
    public List<PqmVctLogEntity> findByTargetID(@Param("visitID") Long targetId);

}
