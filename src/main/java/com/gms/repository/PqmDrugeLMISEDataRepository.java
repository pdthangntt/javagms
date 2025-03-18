package com.gms.repository;

import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugeLMISEDataSearch;
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
public interface PqmDrugeLMISEDataRepository extends CrudRepository<PqmDrugeLMISEDataEntity, Long> {

    @Override
    public List<PqmDrugeLMISEDataEntity> findAll(); 

    @Query(" SELECT e FROM PqmDrugeLMISEDataEntity e WHERE"
            + " (e.provinceID = :#{#param.provinceID} OR :#{#param.provinceID} IS NULL ) "
            + "AND (e.quarter = :#{#param.quarter}  ) "
            + "AND (e.year = :#{#param.year} ) "
    )
    public Page<PqmDrugeLMISEDataEntity> find(@Param("param") PqmDrugeLMISEDataSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugeLMISEDataEntity e WHERE  "
            + " e.quarter = :quarter"
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
            + " AND e.drugName = :drugName "
            + " AND e.siteID = :siteID "
    )
    public PqmDrugeLMISEDataEntity findByUniqueConstraints(
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("siteID") Long siteID
    );
    
    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_drug_elmise_data e WHERE e.quarter = :quarter AND e.year = :year "
            + "AND e.province_id = :province_id "
            , nativeQuery = true)
    public void resetDataProvince(
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("province_id") String province_id
    );
    
    
    @Query(" SELECT e FROM PqmDrugeLMISEDataEntity e WHERE  "
            + " e.quarter = :quarter"
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
    )
    public List<PqmDrugeLMISEDataEntity> findData(
            @Param("quarter") int quarter,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );

}
