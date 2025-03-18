package com.gms.repository;

import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmShiArtSearch;
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
public interface PqmShiArtRepository extends CrudRepository<PqmShiArtEntity, Long> {

    @Override
    public List<PqmShiArtEntity> findAll();

    @Query(" SELECT e FROM PqmShiArtEntity e WHERE"
            + "  ((:#{#param.month} != 0 AND e.month =:#{#param.month} ) OR :#{#param.month} = 0)"
            + " AND ((:#{#param.year} != 0 AND e.year =:#{#param.year}) OR :#{#param.year} =0)"
            + " AND (e.siteID = :#{#param.siteID}  OR :#{#param.siteID} is null) "
            + " AND (e.provinceID =:#{#param.provinceID} OR :#{#param.provinceID} IS NULL)"
    )
    public Page<PqmShiArtEntity> find(@Param("param") PqmShiArtSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmShiArtEntity e WHERE e.siteID = :siteID AND  e.provinceID = :provinceID AND e.month = :month AND e.year = :year ")
    public PqmShiArtEntity findByProvinceIdAndMonthAndYearAndSiteID(@Param("siteID") Long siteID, @Param("provinceID") String provinceID, @Param("month") Integer month, @Param("year") Integer year);

    @Query(" SELECT e FROM PqmShiArtEntity e WHERE e.provinceID = :provinceID AND e.month = :month AND e.year =:year")
    public List<PqmShiArtEntity> findByProvinceIdAndMonthAndYear(@Param("provinceID") String provinceID, @Param("month") Integer month, @Param("year") Integer year);

    @Modifying
    @Transactional
    @Query(value = "DELETE e FROM pqm_shi_art e WHERE e.month = :month AND e.year = :year "
            + "AND e.site_id = :siteID ",
            nativeQuery = true)
    public void deleteBySiteIDAndMonthAndYear(
            @Param("siteID") Long siteID,
            @Param("month") int month,
            @Param("year") int year
    );

}
