package com.gms.repository;

import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugNewSearch;
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
public interface PqmDrugNewDataRepository extends CrudRepository<PqmDrugNewDataEntity, Long> {

    @Override
    public List<PqmDrugNewDataEntity> findAll();

    @Query(" SELECT e FROM PqmDrugNewDataEntity e WHERE"
            + " (e.siteID = :#{#param.siteID} OR :#{#param.siteID} IS NULL ) "
            + "AND (e.year = :#{#param.year} ) "
            + "AND (e.quarter = :#{#param.quarter} ) "
            + "AND (e.provinceID = :#{#param.provinceID} ) "
    )
    public Page<PqmDrugNewDataEntity> find(@Param("param") PqmDrugNewSearch param,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_drug_new_data e WHERE e.quarter = :quarter AND e.year = :year "
            + "AND e.site_id = :siteID ",
            nativeQuery = true)
    public void deleteBySiteIDAndMonthAndYear(
            @Param("siteID") Long siteID,
            @Param("quarter") int quarter,
            @Param("year") int year
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_drug_new_data e WHERE e.quarter = :quarter AND e.year = :year "
            + "AND e.province_id = :province_id ",
            nativeQuery = true)
    public void deleteByProvinceIDAndMonthAndYear(
            @Param("province_id") String province_id,
            @Param("quarter") int quarter,
            @Param("year") int year
    );

    @Query(" SELECT e FROM PqmDrugNewDataEntity e WHERE "
            + " e.siteID = :siteID "
            + " AND e.year = :year "
            + " AND e.quarter = :quarter "
            + " AND e.provinceID = :provinceID "
            + " AND e.source = :source "
            + " AND e.drugName = :drugName "
    )
    public PqmDrugNewDataEntity findByUniqueConstraints(
            @Param("siteID") Long siteID,
            @Param("year") int year,
            @Param("quarter") int quarter,
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("source") String source
    );

    @Query(" SELECT e FROM PqmDrugNewDataEntity e WHERE "
            + " ( e.siteID = :siteID OR :siteID is null ) "
            + " AND e.year = :year "
            + " AND e.quarter = :quarter "
            + " AND e.provinceID = :provinceID "
    )
    public List<PqmDrugNewDataEntity> findByIndex(
            @Param("siteID") Long siteID,
            @Param("year") int year,
            @Param("quarter") int quarter,
            @Param("provinceID") String provinceID
    );
    
    @Query(" SELECT e FROM PqmDrugNewDataEntity e WHERE 1 = 1 "
            + " AND e.year = :year "
            + " AND e.quarter = :quarter "
            + " AND e.provinceID = :provinceID "
    )
    public List<PqmDrugNewDataEntity> findByExport(
            @Param("year") int year,
            @Param("quarter") int quarter,
            @Param("provinceID") String provinceID
    );

    @Query(" SELECT e FROM PqmDrugNewDataEntity e WHERE e.provinceID = :provinceID ")
    public List<PqmDrugNewDataEntity> findAllByProvinceID(
            @Param("provinceID") String provinceID
    );

}
