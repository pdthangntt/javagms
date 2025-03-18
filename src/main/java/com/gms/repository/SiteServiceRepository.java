package com.gms.repository;

import com.gms.entity.db.SiteServiceEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author My PC
 */
@Repository
public interface SiteServiceRepository extends CrudRepository<SiteServiceEntity, Long> {

    public List<SiteServiceEntity> findBySiteID(Long siteID);

    @Query("SELECT e FROM SiteServiceEntity e WHERE e.siteID IN :siteID")
    public List<SiteServiceEntity> findBySiteID(@Param("siteID") Set<Long> siteID);

    public void removeBySiteID(Long siteID);

}
