package com.gms.repository;

import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.input.PqmArvSearch;
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
public interface PqmArvRepository extends CrudRepository<PqmArvEntity, Long> {

    @Override
    public List<PqmArvEntity> findAll();

    @Query(" SELECT e FROM PqmArvEntity e WHERE"
            + " (e.code LIKE CONCAT('%',:#{#param.keyword}, '%') OR e.fullName LIKE CONCAT('%',:#{#param.keyword}, '%') OR e.identityCard LIKE CONCAT('%',:#{#param.keyword}, '%') OR :#{#param.keyword} IS NULL)"
            + " AND (e.genderID =:#{#param.genderID} OR :#{#param.genderID} IS NULL)"
            + "AND (e.siteID IN :#{#param.siteID}  OR coalesce(:#{#param.siteID}, null) IS NULL) "
            + " AND (e.objectGroupID =:#{#param.objectGroupID} OR :#{#param.objectGroupID} IS NULL)"
            + " AND (e.statusOfTreatmentID =:#{#param.statusOfTreatmentID} OR :#{#param.statusOfTreatmentID} IS NULL)"
            + " AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') >=:#{#param.treatmentTimeFrom} OR :#{#param.treatmentTimeFrom} IS NULL)"
            + " AND (DATE_FORMAT(e.treatmentTime, '%Y-%m-%d') <=:#{#param.treatmentTimeTo} OR :#{#param.treatmentTimeTo} IS NULL)"
    )
    public Page<PqmArvEntity> find(@Param("param") PqmArvSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.siteID = :siteID ")
    public List<PqmArvEntity> findBySiteID(@Param("siteID") Long siteID);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.siteID IN (:siteID) AND e.insuranceNo = :insuranceNo ")
    public List<PqmArvEntity> findBySiteAndInsuranceNo(@Param("siteID") Set<Long> siteID, @Param("insuranceNo") String insuranceNo);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.code IN (:code) ")
    public List<PqmArvEntity> findByCodes(@Param("code") Set<String> code);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.code IN (:code) AND e.siteID = :siteID")
    public List<PqmArvEntity> findBySiteIDAndCodes(@Param("siteID") Long siteID, @Param("code") Set<String> code);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.code IN (:code) AND e.siteID IN (:siteID)")
    public List<PqmArvEntity> findBySiteIDsAndCodes(@Param("siteID") Set<Long> siteIDs, @Param("code") Set<String> code);

    @Query(" SELECT e FROM PqmArvEntity e WHERE e.code = :code AND e.siteID = :siteID")
    public PqmArvEntity findBySiteIDAndCode(@Param("siteID") Long siteID, @Param("code") String code);
    
    @Query(" SELECT e FROM PqmArvEntity e WHERE e.customer_system_id = :customer_system_id")
    public List<PqmArvEntity> findBySiteIDAndCustomer_system_id(@Param("customer_system_id") String code);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_arv e WHERE e.site_id = :site_id " ,
            nativeQuery = true)
    public void resetDataBySite(
            @Param("site_id") Long site_id
    );
    

}
