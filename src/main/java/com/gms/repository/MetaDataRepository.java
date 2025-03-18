package com.gms.repository;

import com.gms.entity.db.MetaDataEntity;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface MetaDataRepository extends CrudRepository<MetaDataEntity, String> {

    @Override
    @Cacheable(value = "meta_data_cache", key = "#ID")
    public Optional<MetaDataEntity> findById(String ID);

    @Override
    @Cacheable(value = "meta_data_cache_ids")
    public Iterable<MetaDataEntity> findAllById(Iterable<String> IDs);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "meta_data_cache", allEntries = true)
        ,@CacheEvict(value = "meta_data_cache_ids", allEntries = true)
    })
    public MetaDataEntity save(MetaDataEntity s);

}
