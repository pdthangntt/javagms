package com.gms.repository;

import com.gms.entity.db.HtcTargetEntity;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author NamAnh_HaUI
 */
@Repository
public interface HtcTargetRepository extends CrudRepository<HtcTargetEntity, Long> {

    @Cacheable(value = "htc_target_cache")
    @Query("SELECT e FROM HtcTargetEntity e WHERE e.siteID = :siteID")
    public List<HtcTargetEntity> findAll(@Param("siteID") Long siteID, Sort sort);

    @Cacheable(value = "htc_target_findOne")
    @Query("SELECT e FROM HtcTargetEntity e WHERE e.ID = :ID")
    public HtcTargetEntity findOne(@Param("ID") Long ID);

    @Cacheable(value = "htc_target_existsByYearsAndSiteID")
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM HtcTargetEntity e WHERE e.siteID = :siteID AND e.years = :years ")
    public boolean existsByYearsAndSiteID(@Param("years") Long years, @Param("siteID") Long siteID);

    @Cacheable(value = "htc_target_findByYearsAndSiteID")
    @Query("SELECT e FROM HtcTargetEntity e WHERE e.siteID = :siteID AND e.years = :years")
    public HtcTargetEntity findByYearsAndSiteID(@Param("years") Long years, @Param("siteID") Long siteID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "htc_target_cache", allEntries = true)
        ,@CacheEvict(value = "htc_target_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_target_existsByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_target_findByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
    })
    public void delete(HtcTargetEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "htc_target_cache", allEntries = true)
        ,@CacheEvict(value = "htc_target_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_target_existsByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_target_findByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
    })
    public void deleteById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "htc_target_cache", allEntries = true)
        ,@CacheEvict(value = "htc_target_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_target_existsByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_target_findByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
    })
    public <S extends HtcTargetEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "htc_target_cache", allEntries = true)
        ,@CacheEvict(value = "htc_target_findOne", allEntries = true)
        ,@CacheEvict(value = "htc_target_existsByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "htc_target_findByYearsAndSiteID", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardVisit", allEntries = true)
        ,@CacheEvict(value = "getDashboardTarget", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveTransfer", allEntries = true)
        ,@CacheEvict(value = "getDashboardPositiveObjectGroup", allEntries = true)
        ,@CacheEvict(value = "getDashboardObjectGroupPercent", allEntries = true)
    })
    public <S extends HtcTargetEntity> S save(S s);

}
