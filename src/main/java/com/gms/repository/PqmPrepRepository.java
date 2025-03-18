package com.gms.repository;

import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.input.PqmPrepSearch;
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
public interface PqmPrepRepository extends CrudRepository<PqmPrepEntity, Long> {

    @Override
    public List<PqmPrepEntity> findAll();

    @Query(" SELECT e FROM PqmPrepEntity e WHERE"
            + " (e.fullName LIKE CONCAT('%',:#{#param.keyword}, '%') OR e.code LIKE CONCAT('%',:#{#param.keyword}, '%') OR e.identityCard LIKE CONCAT('%',:#{#param.keyword}, '%') OR :#{#param.keyword} IS NULL)"
            + " AND (e.genderID =:#{#param.genderID} OR :#{#param.genderID} IS NULL)"
            + "AND (e.siteID IN :#{#param.siteID}  OR coalesce(:#{#param.siteID}, null) IS NULL) "
            + " AND (e.type =:#{#param.type} OR :#{#param.type} IS NULL)"
            + " AND (e.objectGroupID =:#{#param.objectGroupID} OR :#{#param.objectGroupID} IS NULL)"
            + " AND (DATE_FORMAT(e.startTime, '%Y-%m-%d') >=:#{#param.startTimeFrom} OR :#{#param.startTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.startTime, '%Y-%m-%d') <=:#{#param.startTimeTo} OR :#{#param.startTimeTo} IS NULL)"
            + " AND (DATE_FORMAT(e.examinationTime, '%Y-%m-%d') >=:#{#param.examinationTimeFrom} OR :#{#param.examinationTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.examinationTime, '%Y-%m-%d') <=:#{#param.examinationTimeTo} OR :#{#param.examinationTimeTo} IS NULL)"
    )
    public Page<PqmPrepEntity> find(@Param("param") PqmPrepSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmPrepEntity e WHERE e.siteID = :siteID ")
    public List<PqmPrepEntity> findBySiteID(@Param("siteID") Long siteID);

    @Query(" SELECT e FROM PqmPrepEntity e WHERE e.siteID IN (:siteID) ")
    public List<PqmPrepEntity> findBySiteIDs(@Param("siteID") Set<Long> siteID);

    @Query(" SELECT e FROM PqmPrepEntity e WHERE e.siteID = :siteID AND e.code = :code ")
    public PqmPrepEntity findBySiteAndCode(@Param("siteID") Long siteID, @Param("code") String code);

    @Query(" SELECT e FROM PqmPrepEntity e WHERE e.siteID = :siteID AND e.customer_id = :customer_id ")
    public List<PqmPrepEntity> findByCustomer_id(@Param("siteID") Long siteID, @Param("customer_id") String customer_id);

}
