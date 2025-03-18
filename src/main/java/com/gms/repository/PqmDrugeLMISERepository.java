package com.gms.repository;

import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugSearch;
import com.gms.entity.input.PqmDrugeLMISESearch;
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
public interface PqmDrugeLMISERepository extends CrudRepository<PqmDrugeLMISEEntity, Long> {

    @Override
    public List<PqmDrugeLMISEEntity> findAll();

    @Query(" SELECT e FROM PqmDrugeLMISEEntity e WHERE"
            + " (e.siteID = :#{#param.siteID} OR :#{#param.siteID} IS NULL ) "
            + "AND (e.year = :#{#param.year} ) "
            + "AND (e.quarter = :#{#param.quarter} ) "
            + "AND (e.provinceID = :#{#param.provinceID} ) "
    )
    public Page<PqmDrugeLMISEEntity> find(@Param("param") PqmDrugeLMISESearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugeLMISEEntity e WHERE e.siteCode = :siteCode "
            + " AND e.year = :year "
            + " AND e.quarter = :quarter "
            + " AND e.provinceID = :provinceID "
            + " AND e.drugCode = :drugCode "
            + " AND e.drugName = :drugName "
            + " AND e.ttThau = :ttThau "
    )
    public PqmDrugeLMISEEntity findByUniqueConstraints(
            @Param("year") int year,
            @Param("quarter") int quarter,
            @Param("provinceID") String provinceID,
            @Param("siteCode") String siteCode,
            @Param("drugName") String drugName,
            @Param("drugCode") String drugCode,
            @Param("ttThau") String ttThau
    );

    @Query(" SELECT e FROM PqmDrugeLMISEEntity e WHERE e.provinceID = :provinceID ")
    public List<PqmDrugeLMISEEntity> findAllByProvinceID(
            @Param("provinceID") String provinceID
    );

}
