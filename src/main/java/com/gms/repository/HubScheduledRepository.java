package com.gms.repository;

import com.gms.entity.db.HubScheduledEntity;
import com.gms.entity.input.PqmLogSearch;
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
 * @author pdthang
 */
@Repository
public interface HubScheduledRepository extends CrudRepository<HubScheduledEntity, Long> {

    @Override
    public List<HubScheduledEntity> findAll();

    @Query(" SELECT e FROM HubScheduledEntity e WHERE e.isRun = true ")
    public List<HubScheduledEntity> findRun();

     

}
