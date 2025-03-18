package com.gms.repository;

import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.input.PqmDrugEstimateSearch;
import java.util.List;
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
public interface PqmShiDataRepository extends CrudRepository<PqmShiDataEntity, Long> {

    @Override
    public List<PqmShiDataEntity> findAll();
//
//    @Query(" SELECT e FROM PqmShiDataEntity e WHERE"
//            + " (e.provinceID = :#{#param.provinceID} OR :#{#param.provinceID} IS NULL ) "
//            + "AND (e.month = :#{#param.month}  ) "
//            + "AND (e.year = :#{#param.year} ) "
//    )
//    public Page<PqmShiDataEntity> find(@Param("param") PqmDrugEstimateSearch param,
//            Pageable pageable);

    @Query(" SELECT e FROM PqmShiDataEntity e WHERE  "
            + " e.month = :month"
            + " AND e.year = :year "
            + " AND e.siteID = :siteID "
            + " AND e.provinceID = :provinceID "
    )
    public PqmShiDataEntity findByUniqueConstraints(
            @Param("siteID") Long siteID,
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_shi_data e WHERE e.month = :month AND e.year = :year "
            + "AND e.province_id = :province_id ",
            nativeQuery = true)
    public void resetDataProvince(
            @Param("month") int month,
            @Param("year") int year,
            @Param("province_id") String province_id
    );

    @Query(" SELECT e FROM PqmShiDataEntity e WHERE  "
            + " e.month = :month"
            + " AND e.year = :year "
            + " AND e.provinceID = :provinceID "
    )
    public List<PqmShiDataEntity> findData(
            @Param("month") int month,
            @Param("year") int year,
            @Param("provinceID") String provinceID
    );

}
