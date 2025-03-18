package com.gms.repository;

import com.gms.entity.db.PqmApiTokenEntity;
import com.gms.entity.input.PqmApiTokenSearch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdthang
 */
@Repository
public interface PqmApiTokenRepository extends CrudRepository<PqmApiTokenEntity, Long> {

    @Override
    public List<PqmApiTokenEntity> findAll();

    @Query(" SELECT e FROM PqmApiTokenEntity e WHERE 1 = 1 "
            + " AND (e.siteID = :#{#param.siteID} OR :#{#param.siteID} IS NULL) "
    )
    public Page<PqmApiTokenEntity> find(@Param("param") PqmApiTokenSearch param,
            Pageable pageable);

    @Query(" SELECT e FROM PqmApiTokenEntity e WHERE e.siteID = :siteID ")
    public List<PqmApiTokenEntity> findBySiteID(@Param("siteID") Long siteID);

    @Query(" SELECT e FROM PqmApiTokenEntity e WHERE e.keyToken = :keyToken ")
    public PqmApiTokenEntity findByKeyToken(@Param("keyToken") String keyToken);

}
