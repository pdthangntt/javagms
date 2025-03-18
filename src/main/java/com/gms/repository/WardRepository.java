package com.gms.repository;

import com.gms.entity.db.WardEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
 * @author vvthành
 */
@Repository
public interface WardRepository extends CrudRepository<WardEntity, String> {

    @Override
    public List<WardEntity> findAll();

    @Cacheable(value = "ward_cache")
    public List<WardEntity> findAll(Sort sort);

    @Override
    @Cacheable(value = "ward_cache_findById")
    public Optional<WardEntity> findById(String ID);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM WardEntity e WHERE e.ID = :ID")
    public boolean existsByID(@Param("ID") String ID);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM WardEntity e INNER JOIN DistrictEntity d ON e.districtID  = d.ID WHERE e.ID = :ID and e.districtID = :districtID")
    public boolean existsByDistrictID(@Param("ID") String ID, @Param("districtID") String districtID);

    @Cacheable(value = "ward_cache_findByDistrictID")
    @Query("SELECT e FROM WardEntity e WHERE e.districtID = :districtID")
    public List<WardEntity> findByDistrictID(@Param("districtID") String districtID, Sort by);

    @Query("SELECT e FROM WardEntity e WHERE e.active is true AND e.districtID = :districtID")
    public List<WardEntity> findByDistrictIDAndIsActive(@Param("districtID") String districtID, Sort by);
    
    @Query("SELECT e FROM WardEntity e WHERE e.districtID IN :districtID")
    public List<WardEntity> findByDistrictID(@Param("districtID") Set<String> districtID);

    @Cacheable(value = "ward_cache_findWardByProvinceIdAndDistrictId")
    @Query("SELECT w FROM DistrictEntity d INNER JOIN ProvinceEntity p ON d.provinceID = p.ID INNER JOIN WardEntity w ON w.districtID = d.ID WHERE p.ID = :pID AND d.ID = :dID AND w.ID = :wID")
    public WardEntity findWardByProvinceIdAndDistrictId(@Param("pID") String provinceID, @Param("dID") String districtID, @Param("wID") String wardID);

    @Cacheable(value = "ward_cache_findByIDs")
    @Query("SELECT e FROM WardEntity e WHERE e.ID IN :IDs")
    public List<WardEntity> findByIDs(@Param("IDs") Set<String> IDs);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "ward_cache", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findById", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findByDistrictID", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findWardByProvinceIdAndDistrictId", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findByIDs", allEntries = true)
    })
    public <S extends WardEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "ward_cache", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findById", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findByDistrictID", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findWardByProvinceIdAndDistrictId", allEntries = true)
        ,@CacheEvict(value = "ward_cache_findByIDs", allEntries = true)
    })
    public void delete(WardEntity t);

}
