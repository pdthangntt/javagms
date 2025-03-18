package com.gms.repository;

import com.gms.entity.db.SiteEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface SiteRepository extends CrudRepository<SiteEntity, Long> {

    @Cacheable(value = "site_cache")
    public List<SiteEntity> findAll(Sort sort);

    @Override
    @Cacheable(value = "site_cache_findById")
    public Optional<SiteEntity> findById(Long ID);

    @Cacheable(value = "site_cache_findByparent")
    @Query("SELECT e FROM SiteEntity e WHERE e.parentID = :parentID")
    public List<SiteEntity> findByparent(@Param("parentID") Long parentID);

    @Cacheable(value = "site_cache_findByCode")
    @Query("SELECT e FROM SiteEntity e WHERE e.code = :code")
    public SiteEntity findByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM SiteEntity e WHERE e.code = :code")
    public boolean existsByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM SiteEntity e WHERE e.code = :code and (e.ID = :ID OR :ID IS NULL)")
    public boolean existsByCode(@Param("code") String code, @Param("ID") Long objectID);

    @Query("SELECT CASE WHEN COUNT(e.ID) > 0 THEN 'true' ELSE 'false' END FROM SiteEntity e WHERE e.ID = :ID")
    public boolean existsByID(@Param("ID") Long ID);

    @Cacheable(value = "site_cache_findByServiceAndProvince")
    @Query("SELECT e FROM SiteEntity e "
            + "WHERE e.provinceID IN :provinceID "
            + "AND e.ID IN (SELECT s.siteID FROM SiteServiceEntity s WHERE s.serviceID IN :serviceID)"
            + "AND e.isActive IS TRUE "
    )
    public List<SiteEntity> findByServiceAndProvince(@Param("serviceID") Set<String> serviceID, @Param("provinceID") Set<String> provinceID);

    @Cacheable(value = "site_cache_findByServiceAndSiteID")
    @Query("SELECT e FROM SiteEntity e "
            + "WHERE e.ID IN :IDs "
            + "AND e.ID IN (SELECT s.siteID FROM SiteServiceEntity s WHERE s.serviceID IN :serviceID)"
            + "AND e.isActive IS TRUE "
    )
    public List<SiteEntity> findByServiceAndSiteID(@Param("serviceID") Set<String> serviceID, @Param("IDs") Set<Long> IDs);

    @Cacheable(value = "site_cache_findByIDs")
    @Query("SELECT e FROM SiteEntity e WHERE ID IN :ids")
    public List<SiteEntity> findByIDs(@Param("ids") Set<Long> siteIDs);

    @Cacheable(value = "site_cache_findChildByName")
    @Query("SELECT e FROM SiteEntity e WHERE "
            + "(e.name LIKE CONCAT('%',:name, '%') OR e.code LIKE CONCAT('%',:name, '%') OR e.shortName LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL) "
            + "AND e.ID IN (SELECT s.siteID FROM SitePathEntity s WHERE s.ancestorId = :siteID)")
    public List<SiteEntity> findChildByName(@Param("name") String k, @Param("siteID") Long siteID, Pageable pageable);

    @Cacheable(value = "site_cache_findByService")
    @Query("SELECT e FROM SiteEntity e WHERE"
            + " e.ID IN (SELECT s.siteID FROM SiteServiceEntity s WHERE s.serviceID = :serviceID)")
    public List<SiteEntity> findByService(@Param("serviceID") String serviceID);

    @Cacheable(value = "site_cache_findByLocation")
    @Query("SELECT e FROM SiteEntity e WHERE e.ID IN :siteID "
            + "AND e.provinceID = :provinceID "
            + "AND (e.districtID = :districtID OR :districtID IS '' OR :districtID IS NULL) "
            + "AND (e.wardID = :wardID OR :wardID IS '' OR :wardID IS NULL) ")
    public List<SiteEntity> findByLocation(@Param("siteID") Set<Long> siteID,
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID);

    //Tìm cơ sở theo mã tỉnh
    /**
     * @author DSNAnh
     * @param provinceID
     * @return
     */
    @Cacheable(value = "site_cache_findByProvince")
    @Query("SELECT e FROM SiteEntity e WHERE e.provinceID = :provinceID")
    public List<SiteEntity> findByProvinceID(@Param("provinceID") String provinceID);

    @Cacheable(value = "site_cache_findByProvinceIDAndDistrictID")
    @Query("SELECT e FROM SiteEntity e WHERE e.provinceID = :provinceID AND e.districtID = :districtID")
    public List<SiteEntity> findByProvinceIDAndDistrictID(@Param("provinceID") String provinceID, @Param("districtID") String districtID);

    @Cacheable(value = "site_cache_findByProvinceIDAndDistrictIDAndWardID")
    @Query("SELECT e FROM SiteEntity e WHERE e.provinceID = :provinceID AND e.districtID = :districtID AND e.wardID = :wardID")
    public List<SiteEntity> findByProvinceIDAndDistrictIDAndWardID(@Param("provinceID") String provinceID, @Param("districtID") String districtID, @Param("wardID") String wardID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "site_cache", allEntries = true),
        @CacheEvict(value = "site_cache_findById", allEntries = true),
        @CacheEvict(value = "site_cache_findByparent", allEntries = true),
        @CacheEvict(value = "site_cache_findByCode", allEntries = true),
        @CacheEvict(value = "site_cache_findByServiceAndProvince", allEntries = true),
        @CacheEvict(value = "site_cache_findByIDs", allEntries = true),
        @CacheEvict(value = "site_cache_findChildByName", allEntries = true),
        @CacheEvict(value = "site_cache_findByService", allEntries = true),
        @CacheEvict(value = "site_cache_findByLocation", allEntries = true),
        @CacheEvict(value = "site_cache_findByProvinceIDAndDistrictID", allEntries = true),
        @CacheEvict(value = "site_cache_findByProvinceIDAndDistrictIDAndWardID", allEntries = true),
        @CacheEvict(value = "site_cache_findByServiceAndSiteID", allEntries = true)
    })
    public <S extends SiteEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "site_cache", allEntries = true),
        @CacheEvict(value = "site_cache_findById", allEntries = true),
        @CacheEvict(value = "site_cache_findByparent", allEntries = true),
        @CacheEvict(value = "site_cache_findByCode", allEntries = true),
        @CacheEvict(value = "site_cache_findByServiceAndProvince", allEntries = true),
        @CacheEvict(value = "site_cache_findByIDs", allEntries = true),
        @CacheEvict(value = "site_cache_findChildByName", allEntries = true),
        @CacheEvict(value = "site_cache_findByService", allEntries = true),
        @CacheEvict(value = "site_cache_findByLocation", allEntries = true),
        @CacheEvict(value = "site_cache_findByProvinceIDAndDistrictID", allEntries = true),
        @CacheEvict(value = "site_cache_findByProvinceIDAndDistrictIDAndWardID", allEntries = true),
        @CacheEvict(value = "site_cache_findByServiceAndSiteID", allEntries = true)
    })
    public void delete(SiteEntity t);

    @Query(value = "SELECT * FROM site as e WHERE e.elog_site_code = :code LIMIT 1", nativeQuery = true)
    public SiteEntity findByElogSiteCode(@Param("code") String code);
    
    @Query(value = "SELECT * FROM site as e WHERE e.arv_site_code = :code LIMIT 1", nativeQuery = true)
    public SiteEntity findByArvSiteCode(@Param("code") String code);
    
    @Query(value = "SELECT * FROM site as e WHERE e.prep_site_code = :code LIMIT 1", nativeQuery = true)
    public SiteEntity findByPrepSiteCode(@Param("code") String code);
    
    @Query(value = "SELECT * FROM site as e WHERE e.hmed_site_code = :code LIMIT 1", nativeQuery = true)
    public SiteEntity findByHmedSiteCode(@Param("code") String code);
}
