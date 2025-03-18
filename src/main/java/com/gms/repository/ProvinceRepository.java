package com.gms.repository;

import com.gms.entity.db.ProvinceEntity;
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
public interface ProvinceRepository extends CrudRepository<ProvinceEntity, String> {

    @Override
    @Cacheable(value = "province_cache_all")
    public List<ProvinceEntity> findAll();

    @Cacheable(value = "province_cache")
    public List<ProvinceEntity> findAll(Sort sort);

    @Override
    public Optional<ProvinceEntity> findById(String ID);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ProvinceEntity e WHERE e.ID = :ID")
    public boolean existsByID(@Param("ID") String ID);

    @Cacheable(value = "province_cache_findByIDs")
    @Query("SELECT e FROM ProvinceEntity e WHERE e.ID IN :IDs")
    public List<ProvinceEntity> findByIDs(@Param("IDs") Set<String> IDs);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "province_cache_all", allEntries = true)
        ,@CacheEvict(value = "province_cache", allEntries = true)
        ,@CacheEvict(value = "province_cache_findByIDs", allEntries = true)
    })
    public <S extends ProvinceEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "province_cache_all", allEntries = true)
        ,@CacheEvict(value = "province_cache", allEntries = true)
        ,@CacheEvict(value = "province_cache_findByIDs", allEntries = true)
    })
    public void delete(ProvinceEntity t);
}
