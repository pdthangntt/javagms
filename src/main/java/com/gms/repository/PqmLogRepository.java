package com.gms.repository;

import com.gms.entity.db.PqmLogEntity;
import com.gms.entity.input.PqmLogSearch;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface PqmLogRepository extends CrudRepository<PqmLogEntity, Long> {

    @Override
    public List<PqmLogEntity> findAll();

    @Query(" SELECT e FROM PqmLogEntity e WHERE"
            + " (e.staffName LIKE CONCAT('%',:#{#param.keyword}, '%') "
            + " OR e.action LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR :#{#param.keyword} IS NULL)"
            + " AND (e.service =:#{#param.service} OR :#{#param.service} IS NULL)"
            + " AND (e.siteID IN :#{#param.site}  OR coalesce(:#{#param.site}, 99999) = 99999) "
            + " AND (e.object =:#{#param.object} OR :#{#param.object} IS NULL)"
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') >=:#{#param.from} OR :#{#param.from} IS NULL)"
            + " AND (DATE_FORMAT(e.time, '%Y-%m-%d') <=:#{#param.to} OR :#{#param.to} IS NULL)"
            + " AND (e.provinceID = :#{#param.provinceID})"
    )
    public Page<PqmLogEntity> find(@Param("param") PqmLogSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmLogEntity e WHERE e.provinceID = :provinceID ")
    public List<PqmLogEntity> findByProvinceID(@Param("provinceID") String provinceID);

    @Query(" SELECT e FROM PqmLogEntity e WHERE e.siteID IN (:siteID) "
            + " AND DATE_FORMAT(e.time, '%Y-%m-%d') >= :from "
            + " AND DATE_FORMAT(e.time, '%Y-%m-%d') <= :to "
            + " AND e.service = :service "
    )
    public List<PqmLogEntity> findBySiteIDAndService(@Param("siteID") Set<Long> siteID, @Param("service") String service, @Param("from") String from, @Param("to") String to);

    @Query(" SELECT e FROM PqmLogEntity e WHERE e.siteID IN (:siteID) "
            + " AND DATE_FORMAT(e.time, '%Y-%m-%d') >= :from "
            + " AND DATE_FORMAT(e.time, '%Y-%m-%d') <= :to "
    )
    public List<PqmLogEntity> findBySiteID(@Param("siteID") Set<Long> siteID, @Param("from") String from, @Param("to") String to);

}
