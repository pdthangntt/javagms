package com.gms.repository;

import com.gms.entity.db.AuthRoleEntity;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
public interface AuthRoleRepository extends CrudRepository<AuthRoleEntity, Long> {

    @Cacheable(value = "role_cache_findAll")
    public List<AuthRoleEntity> findAll(Sort sort);

    @Cacheable(value = "role_cache_findByName")
    @Query("SELECT e FROM AuthRoleEntity e WHERE e.name = :name")
    public AuthRoleEntity findByName(@Param("name") String name);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "role_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "role_cache_findByName", allEntries = true)
    })
    public <S extends AuthRoleEntity> S save(S s);

    @Override
    @Modifying
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "role_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "role_cache_findByName", allEntries = true)
    })
    public void delete(AuthRoleEntity t);

}
