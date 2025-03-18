package com.gms.repository;

import com.gms.entity.db.PqmApiLogEntity;
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
public interface PqmApiRepository extends CrudRepository<PqmApiLogEntity, Long> {

    @Query(" SELECT e FROM PqmApiLogEntity e WHERE 1 = 1 "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') >= :from OR :from IS NULL) "
            + " AND (DATE_FORMAT(e.createAT, '%Y-%m-%d') <= :to OR :to IS NULL) "
            + " AND ( e.province = :province OR :province IS NULL ) "
    )
    public Page<PqmApiLogEntity> find(
            @Param("from") String from,
            @Param("to") String to,
            @Param("province") String province,
            Pageable pageable);

}
