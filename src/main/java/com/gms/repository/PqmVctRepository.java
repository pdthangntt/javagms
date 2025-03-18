package com.gms.repository;

import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.input.PqmVctSearch;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvThành
 */
@Repository
public interface PqmVctRepository extends CrudRepository<PqmVctEntity, Long> {

    @Override
    public List<PqmVctEntity> findAll();

    @Query(" SELECT e FROM PqmVctEntity e WHERE"
            + " (e.code LIKE CONCAT('%',:#{#param.keyword}, '%') "
            + " OR e.patientName LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.patientID LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.yearOfBirth LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.identityCard LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.insuranceNo LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.confirmTestNo LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.therapyNo LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.patientPhone LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR :#{#param.keyword} IS NULL)"
            + " AND (e.genderID =:#{#param.genderID} OR :#{#param.genderID} IS NULL)"
            + " AND (e.siteID IN :#{#param.siteID}  OR coalesce(:#{#param.siteID}, null) IS NULL) "
            + " AND (e.objectGroupID =:#{#param.objectGroupID} OR :#{#param.objectGroupID} IS NULL)"
            + " AND (e.earlyDiagnose =:#{#param.earlyDiagnose} OR :#{#param.earlyDiagnose} IS NULL)"
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') >=:#{#param.confirmTimeFrom} OR :#{#param.confirmTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.confirmTime, '%Y-%m-%d') <=:#{#param.confirmTimeTo} OR :#{#param.confirmTimeTo} IS NULL)"
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >=:#{#param.earlyHivDateFrom} OR :#{#param.earlyHivDateFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <=:#{#param.earlyHivDateTo} OR :#{#param.earlyHivDateTo} IS NULL)"
            + " AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') >=:#{#param.exchangeTimeFrom} OR :#{#param.exchangeTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.exchangeTime, '%Y-%m-%d') <=:#{#param.exchangeTimeTo} OR :#{#param.exchangeTimeTo} IS NULL)"
            + " AND (DATE_FORMAT(e.registerTherapyTime, '%Y-%m-%d') >=:#{#param.registerTherapyTimeFrom} OR :#{#param.registerTherapyTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.registerTherapyTime, '%Y-%m-%d') <=:#{#param.registerTherapyTimeTo} OR :#{#param.registerTherapyTimeTo} IS NULL)"
            + " AND ((:#{#param.registerTherapyStatus} = '1' AND e.registerTherapyTime is null )  OR (:#{#param.registerTherapyStatus} = '2' AND e.registerTherapyTime is not null ) OR :#{#param.registerTherapyStatus} IS NULL)"
            + " AND ( (:#{#param.tab} = 'lack' AND ("
            + " e.confirmTime is null OR "
            + " e.confirmTestNo is null OR "
            + " e.confirmTestNo = '' OR "
            + " ( e.earlyHivDate is null AND "
            + " ( e.earlyDiagnose = '1' OR  e.earlyDiagnose = '2') OR e.earlyDiagnose is null OR e.earlyDiagnose = '' )OR "
            + " e.exchangeTime is null OR "
            + " e.registerTherapyTime is null OR "
            + " e.therapyNo is null OR "
            + " e.therapyNo = '' OR "
            + " e.registeredTherapySite is null OR "
            + " e.registeredTherapySite = ''  "
            + ")  ) OR :#{#param.tab} IS NULL )"
    )
    public Page<PqmVctEntity> find(@Param("param") PqmVctSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmVctEntity e WHERE e.siteID = :siteID ")
    public List<PqmVctEntity> findBySiteID(@Param("siteID") Long siteID);

    @Query(" SELECT e FROM PqmVctEntity e WHERE e.siteID = :siteID AND e.code = :code ")
    public PqmVctEntity findBySiteAndCode(@Param("siteID") Long siteID, @Param("code") String code);

    @Query(" SELECT e FROM PqmVctEntity e WHERE e.customer_id = :customer_id")
    public List<PqmVctEntity> findByCustomerId(@Param("customer_id") String customer_id);

    @Query(" SELECT e FROM PqmVctEntity e WHERE e.siteID IN (:siteID) ")
    public List<PqmVctEntity> findBySiteIDs(@Param("siteID") Set<Long> siteID);

    @Query(" SELECT e FROM PqmVctEntity e WHERE e.code = :code AND  e.siteID IN (:siteID) ")
    public List<PqmVctEntity> findByCodeAndSiteIDs(@Param("code") String code, @Param("siteID") Set<Long> siteID);

    public PqmVctEntity findByCodeAndSiteID(String code, Long siteID);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_vct e WHERE e.site_id = :site_id ",
            nativeQuery = true)
    public void resetDataBySite(
            @Param("site_id") Long site_id
    );

}
