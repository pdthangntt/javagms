package com.gms.repository;

import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugSearch;
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
 * @author pdThang
 */
@Repository
public interface PqmDrugPlanDataRepository extends CrudRepository<PqmDrugPlanDataEntity, Long> {

    @Override
    public List<PqmDrugPlanDataEntity> findAll();

    @Query(" SELECT e FROM PqmDrugPlanDataEntity e WHERE"
            + " (e.provinceID = :#{#param.provinceID} OR :#{#param.provinceID} IS NULL ) "
            + "AND (e.month = :#{#param.month}  ) "
            + "AND (e.year = :#{#param.year} ) "
    )
    public Page<PqmDrugPlanDataEntity> find(@Param("param") PqmDrugSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugPlanDataEntity e WHERE  e.month = :month"
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
            + " AND e.siteCode = :siteCode "
            + " AND e.drugName = :drugName "
            + " AND e.unit = :unit "
    )
    public PqmDrugPlanDataEntity findByUniqueConstraints(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID,
            @Param("siteCode") String siteCode,
            @Param("drugName") String drugName,
            @Param("unit") String unit
    );
    
    @Query(" SELECT e FROM PqmDrugPlanDataEntity e WHERE  e.month = :month"
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
    )
    public List<PqmDrugPlanDataEntity>  findByEx(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );
    
    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_drug_plan_data e WHERE e.month = :month AND e.year = :year "
            + "AND e.province_id = :provinceID "
            , nativeQuery = true)
    public void resetDataProvince(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );
    
    @Query(" SELECT e FROM PqmDrugPlanDataEntity e WHERE e.month = :month AND e.year = :year "
            + "AND e.provinceID = :provinceID ")
    public List<PqmDrugPlanDataEntity> getDataDetail(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );



}
