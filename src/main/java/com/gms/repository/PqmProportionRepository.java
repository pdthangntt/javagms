package com.gms.repository;

import com.gms.entity.db.PqmProportionEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PqmVctSearch;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface PqmProportionRepository extends CrudRepository<PqmProportionEntity, Long> {

    @Override
    public List<PqmProportionEntity> findAll();

    @Query("SELECT e FROM PqmProportionEntity e "
            + "WHERE e.provinceID IN :provinceID "
    )
    public List<PqmProportionEntity> findByProvince(@Param("provinceID") Set<String> provinceID);
}
