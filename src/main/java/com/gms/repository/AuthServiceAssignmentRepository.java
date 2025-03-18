package com.gms.repository;

import com.gms.entity.db.AuthServiceAssignmentEntity;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author My PC
 */
@Repository
public interface AuthServiceAssignmentRepository extends CrudRepository<AuthServiceAssignmentEntity, Long> {

    @Override
    @Cacheable(value = "auth_service_assignment_cache_findAll")
    public List<AuthServiceAssignmentEntity> findAll();

    @Cacheable(value = "auth_service_assignment_cache_findByServiceID")
    public List<AuthServiceAssignmentEntity> findByServiceID(String serviceID);

    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public void removeByServiceID(String serviceID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public void deleteAll();

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public void deleteAll(Iterable<? extends AuthServiceAssignmentEntity> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public void delete(AuthServiceAssignmentEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public void deleteById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public <S extends AuthServiceAssignmentEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "auth_service_assignment_cache_findAll", allEntries = true)
        ,@CacheEvict(value = "auth_service_assignment_cache_findByServiceID", allEntries = true)
    })
    public <S extends AuthServiceAssignmentEntity> S save(S s);

}
