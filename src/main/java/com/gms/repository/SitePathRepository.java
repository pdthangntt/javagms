package com.gms.repository;

import com.gms.entity.db.SitePathEntity;
import java.util.HashSet;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface SitePathRepository extends CrudRepository<SitePathEntity, Long> {

    public long removeBySiteID(Long siteID);

    @Query("SELECT e.ancestorId FROM SitePathEntity e WHERE e.siteID = :siteID")
    public List<Long> findBySiteID(@Param("siteID") Long siteID);

    @Query("SELECT e.siteID FROM SitePathEntity e WHERE e.ancestorId = :siteID")
    public List<Long> findProgenyID(@Param("siteID") Long siteID);

    @Override
    @CacheEvict(value = "sitepath_cache", allEntries = true)
    public void deleteAll();

    @Override
    @CacheEvict(value = "sitepath_cache", allEntries = true)
    public void delete(SitePathEntity t);

    @Override
    @CachePut(value = "sitepath_cache")
    public <S extends SitePathEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @CachePut(value = "sitepath_cache")
    public <S extends SitePathEntity> S save(S s);

}
