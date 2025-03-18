package com.gms.repository;

import com.gms.entity.db.SiteVnptFkEntity;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthanh
 */
@Repository
public interface SiteVnptFkRepository extends CrudRepository<SiteVnptFkEntity, Long> {
    
    @Query("SELECT e FROM SiteVnptFkEntity e WHERE 1 = 1 "
            + "AND (e.ID IN :siteID  OR coalesce(:siteID, null) IS NULL) "
            + "AND (e.ID = :id OR :id = 0 ) "
            + "AND (e.ID = :site OR :site = 0 ) "
            + "AND (e.vpntSiteID = :vnptID OR :vnptID is '' OR :vnptID IS NULL) "
            + "AND ( :active = '2' OR ( :active = '1' AND e.active = false ) OR ( :active = '0' AND e.active = true ) ) "
    )
    public Page<SiteVnptFkEntity> findAll(
            @Param("id") Long id,
            @Param("site") Long site,
            @Param("vnptID") String vnptID,
            @Param("active") String active,
            @Param("siteID") Set<Long> siteID,
            Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM SiteVnptFkEntity e WHERE e.ID = :ID")
    public Long countByID(@Param("ID") Long ID);
    
    @Query("SELECT COUNT(e) FROM SiteVnptFkEntity e WHERE e.vpntSiteID = :vpntSiteID")
    public Long countByVpntSiteID(@Param("vpntSiteID") String vpntSiteID);

//    @Cacheable(value = "SiteVnptFk_findByVnptSiteID")
    @Query("SELECT e FROM SiteVnptFkEntity e WHERE e.vpntSiteID=:vnptSiteID")
    public SiteVnptFkEntity findByVnptSiteID(@Param("vnptSiteID") String vnptSiteID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "SiteVnptFk_findByVnptSiteID", allEntries = true)
    })
    public void delete(SiteVnptFkEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "SiteVnptFk_findByVnptSiteID", allEntries = true)
    })
    public SiteVnptFkEntity save(SiteVnptFkEntity s);

}
