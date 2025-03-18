package com.gms.repository;

import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.input.PqmVctRecencySearch;
import java.util.Date;
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
 * @author otHoa
 */
@Repository
public interface PqmVctRecencyRepository extends CrudRepository<PqmVctRecencyEntity, Long> {

    @Override
    public List<PqmVctRecencyEntity> findAll();

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE"
            + " (e.code LIKE CONCAT('%',:#{#param.keyword}, '%') "
            + " OR e.patientName LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.yearOfBirth LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR e.identityCard LIKE CONCAT('%',:#{#param.keyword}, '%')"
            + " OR :#{#param.keyword} IS NULL)"
            + " AND (e.genderID =:#{#param.genderID} OR :#{#param.genderID} IS NULL)"
            + " AND (e.siteID IN :#{#param.siteID}  OR coalesce(:#{#param.siteID}, null) IS NULL) "
            + " AND (e.objectGroupID =:#{#param.objectGroupID} OR :#{#param.objectGroupID} IS NULL)"
            + " AND (e.provinceID =:#{#param.provinceID} OR :#{#param.provinceID} IS NULL)"
            + " AND (e.earlyDiagnose =:#{#param.earlyDiagnose} OR :#{#param.earlyDiagnose} IS NULL)"
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') >=:#{#param.earlyHivDateFrom} OR :#{#param.earlyHivDateFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.earlyHivDate, '%Y-%m-%d') <=:#{#param.earlyHivDateTo} OR :#{#param.earlyHivDateTo} IS NULL)"
            + " AND ( (:#{#param.tab} = 'notSame' AND ("
            + " e.pqmVctID is null"
            + ")  ) OR :#{#param.tab} IS NULL )"
    )
    public Page<PqmVctRecencyEntity> find(@Param("param") PqmVctRecencySearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE e.siteID = :siteID ")
    public List<PqmVctRecencyEntity> findBySiteID(@Param("siteID") Long siteID);

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE e.provinceID = :provinceID AND e.pqmVctID is null ")
    public List<PqmVctRecencyEntity> findByProvinceIDAndVctID(@Param("provinceID") Long provinceID);

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE e.siteID = :siteID AND e.code = :code ")
    public PqmVctRecencyEntity findBySiteAndCode(@Param("siteID") Long siteID, @Param("code") String code);

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE e.siteID = :siteID AND e.confirmTestNo = :confirmTestNo AND e.earlyHivDate = :earlyHivDate")
    public PqmVctRecencyEntity findByCodeAndSiteIDAndEarlyHivDate(@Param("confirmTestNo") String confirmTestNo, @Param("siteID") Long siteID, @Param("earlyHivDate") Date earlyHivDate);

    @Query(" SELECT e FROM PqmVctRecencyEntity e WHERE e.siteID IN (:siteID) ")
    public List<PqmVctRecencyEntity> findBySiteIDs(@Param("siteID") Set<Long> siteID);

    public PqmVctRecencyEntity findByCodeAndSiteID(String code, Long siteID);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_vct_recency e WHERE e.site_id = :site_id " ,
            nativeQuery = true)
    public void resetDataBySite(
            @Param("site_id") Long site_id
    );
    
}
