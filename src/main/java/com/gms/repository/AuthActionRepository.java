package com.gms.repository;

import com.gms.entity.db.AuthActionEntity;
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
 * @author My PC
 */
@Repository
public interface AuthActionRepository extends CrudRepository<AuthActionEntity, Long> {

    @Cacheable(value = "action_cache_findByName")
    @Query("SELECT e FROM AuthActionEntity e WHERE e.name = :name")
    public AuthActionEntity findByName(@Param("name") String name);

    @Cacheable(value = "action_cache_findAll")
    public List<AuthActionEntity> findAll(Sort by);

    @Cacheable(value = "action_cache_findByIds")
    @Query("SELECT e FROM AuthActionEntity e WHERE e.ID IN :ID")
    public List<AuthActionEntity> findByIds(@Param("ID") Set<Long> ids);

    @Override
    @Cacheable(value = "action_cache_findById")
    public Optional<AuthActionEntity> findById(Long ID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "action_cache_findByName", allEntries = true)
        ,@CacheEvict(value = "action_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "action_cache_findByIds", allEntries = true)
        ,@CacheEvict(value = "action_cache_findById", allEntries = true)
    })
    public <S extends AuthActionEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "action_cache_findByName", allEntries = true)
        ,@CacheEvict(value = "action_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "action_cache_findByIds", allEntries = true)
        ,@CacheEvict(value = "action_cache_findById", allEntries = true)
    })
    public void delete(AuthActionEntity t);

}
