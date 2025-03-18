package com.gms.repository;

import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugEstimateSearch;
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
public interface PqmDrugEstimateDataRepository extends CrudRepository<PqmDrugEstimateDataEntity, Long> {

    @Override
    public List<PqmDrugEstimateDataEntity> findAll(); 

    @Query(" SELECT e FROM PqmDrugEstimateDataEntity e WHERE"
            + " (e.provinceCode = :#{#param.provinceCode} OR :#{#param.provinceCode} IS NULL ) "
            + "AND (e.quarter = :#{#param.quarter}  ) "
            + "AND (e.year = :#{#param.year} ) "
    )
    public Page<PqmDrugEstimateDataEntity> find(@Param("param") PqmDrugEstimateSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugEstimateDataEntity e WHERE  "
            + " e.quarter = :quarter"
            + " AND e.year = :year "
            + " AND e.dataCode = :dataCode "
            + " AND e.provinceCode = :provinceCode "
            + " AND e.drugNameLowercase = :drugName "
            + " AND e.drugUomLowercase = :unit "
            + " AND e.facilityCode = :facilityCode "
    )
    public PqmDrugEstimateDataEntity findByUniqueConstraints(
            @Param("dataCode") String dataCode,
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("provinceCode") String provinceCode,
            @Param("drugName") String drugName,
            @Param("unit") String unit,
            @Param("facilityCode") String facilityCode
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_drug_estimate_data e WHERE e.quarter = :quarter AND e.year = :year "
            + "AND e.data_code = :data_code "
            + "AND e.province_code = :province_code "
            , nativeQuery = true)
    public void resetDataProvince(
            @Param("data_code") String data_code,
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("province_code") String province_code
    );
    
    
    @Query(" SELECT e FROM PqmDrugEstimateDataEntity e WHERE  "
            + " e.quarter = :quarter"
            + " AND e.year = :year "
            + " AND e.dataCode = :dataCode "
            + " AND e.provinceCode = :provinceCode "
    )
    public List<PqmDrugEstimateDataEntity> findData(
            @Param("dataCode") String dataCode,
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("provinceCode") String provinceCode
    );

}
