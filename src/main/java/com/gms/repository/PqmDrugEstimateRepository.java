package com.gms.repository;

import com.gms.entity.db.PqmDrugEstimateEntity;
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
public interface PqmDrugEstimateRepository extends CrudRepository<PqmDrugEstimateEntity, Long> {

    @Override
    public List<PqmDrugEstimateEntity> findAll();

    @Query(" SELECT e FROM PqmDrugEstimateEntity e WHERE"
            + " (e.siteCode = :#{#param.siteID} OR :#{#param.siteID} IS NULL ) "
            + "AND (e.year = :#{#param.year} ) "
            + "AND (e.provinceID = :#{#param.provinceID} ) "
    )
    public Page<PqmDrugEstimateEntity> find(@Param("param") PqmDrugSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmDrugEstimateEntity e WHERE e.siteCode = :siteCode "
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
            + " AND e.source = :source "
            + " AND e.drugName = :drugName "
    )
    public PqmDrugEstimateEntity findByUniqueConstraints(
            @Param("year") int year,
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("siteCode") String siteCode,
            @Param("source") String source
    );

    @Query(" SELECT e FROM PqmDrugEstimateEntity e WHERE e.siteCode = :siteCode "
            + " AND e.provinceID = :provinceID "
            + " AND e.drugName = :drugName "
            + " AND e.year = :year "
    )
    public List<PqmDrugEstimateEntity> findUpdateUnit(
            @Param("provinceID") String provinceID,
            @Param("drugName") String drugName,
            @Param("siteCode") String siteCode,
            @Param("year") int year
    );

    @Query(" SELECT e FROM PqmDrugEstimateEntity e WHERE e.provinceID = :provinceID ")
    public List<PqmDrugEstimateEntity> findAllByProvinceID(
            @Param("provinceID") String provinceID
    );

}
