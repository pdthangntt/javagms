package com.gms.repository;

import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugSearch;
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
 * @author pdThang
 */
@Repository
public interface PqmDrugPlanRepository extends CrudRepository<PqmDrugPlanEntity, Long> {

    @Override
    public List<PqmDrugPlanEntity> findAll();

    @Query(" SELECT e FROM PqmDrugPlanEntity e WHERE"
            + " (e.siteCode = :#{#param.siteID} OR :#{#param.siteID} IS NULL ) "
            + "AND (e.month = :#{#param.month}  ) "
            + "AND (e.year = :#{#param.year} ) "
            + "AND (e.currentProvinceID = :#{#param.provinceID} ) "
    )
    public Page<PqmDrugPlanEntity> find(@Param("param") PqmDrugSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugPlanEntity e WHERE e.siteCode = :siteCode "
            + " AND e.month = :month "
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
            + " AND e.source = :source "
            + " AND e.lotNumber = :lotNumber "
            + " AND e.drugName = :drugName "
            + " AND e.unit = :unit "
    )
    public PqmDrugPlanEntity findByUniqueConstraints(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("siteCode") String siteCode,
            @Param("source") String source,
            @Param("lotNumber") String lotNumber,
            @Param("unit") String unit
    );

    @Query(" SELECT e FROM PqmDrugPlanEntity e WHERE e.siteCode = :siteCode "
            + " AND e.currentProvinceID = :provinceID "
            + " AND e.drugName = :drugName "
            + " AND e.year = :year ORDER BY e.year DESC, e.month DESC "
    )
    public List<PqmDrugPlanEntity> findUpdateUnitToEstimate(
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("siteCode") String siteCode,
            @Param("year") int year
    );

    @Query(" SELECT e FROM PqmDrugPlanEntity e WHERE e.currentProvinceID = :provinceID ")
    public List<PqmDrugPlanEntity> findAllByProvinceID(
            @Param("provinceID") String provinceID
    );

}
