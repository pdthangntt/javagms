package com.gms.repository;

import com.gms.entity.db.DistrictEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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
public interface DistrictRepository extends CrudRepository<DistrictEntity, String> {

    @Override
    @Cacheable(value = "district_cache")
    public List<DistrictEntity> findAll();

    @Cacheable(value = "district_cache_findAll")
    public List<DistrictEntity> findAll(Sort sort);

    @Override
    @Cacheable(value = "district_cache_findById")
    public Optional<DistrictEntity> findById(String ID);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM DistrictEntity e WHERE e.ID = :ID ")
    public boolean existsByID(@Param("ID") String ID);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM DistrictEntity e INNER JOIN ProvinceEntity p on e.provinceID = p.ID WHERE e.ID = :ID AND e.provinceID = :provinceID")
    public boolean existsByProvinceID(@Param("ID") String ID, @Param("provinceID") String provinceID);

    @Cacheable(value = "district_cache_findByProvinceID")
    @Query("SELECT e FROM DistrictEntity e WHERE e.provinceID = :provinceID")
    public List<DistrictEntity> findByProvinceID(@Param("provinceID") String districtID, Sort by);

    @Cacheable(value = "district_cache_findDistrictByProvinceId")
    @Query("SELECT d FROM DistrictEntity d INNER JOIN ProvinceEntity p ON d.provinceID = p.ID WHERE p.ID = :pID AND d.ID = :dID")
    public DistrictEntity findDistrictByProvinceId(@Param("pID") String provinceID, @Param("dID") String districtID);

    @Cacheable(value = "district_cache_findByIDs")
    @Query("SELECT e FROM DistrictEntity e WHERE e.ID IN :IDs")
    public List<DistrictEntity> findByIDs(@Param("IDs") Set<String> IDs);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "district_cache", allEntries = true)
        ,@CacheEvict(value = "district_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "district_cache_findById", allEntries = true)
        ,@CacheEvict(value = "district_cache_findByProvinceID", allEntries = true)
        ,@CacheEvict(value = "district_cache_findDistrictByProvinceId", allEntries = true)
        ,@CacheEvict(value = "district_cache_findByIDs", allEntries = true)
    })
    public <S extends DistrictEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "district_cache", allEntries = true)
        ,@CacheEvict(value = "district_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "district_cache_findById", allEntries = true)
        ,@CacheEvict(value = "district_cache_findByProvinceID", allEntries = true)
        ,@CacheEvict(value = "district_cache_findDistrictByProvinceId", allEntries = true)
        ,@CacheEvict(value = "district_cache_findByIDs", allEntries = true)
    })
    public void delete(DistrictEntity t);

}
