package com.gms.repository;

import com.gms.entity.db.PacHivInfoEntity;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface PacHivInfoRepository extends CrudRepository<PacHivInfoEntity, Long> {

    @Cacheable(value = "pac_hivinfo_cache_findByCode")
    public PacHivInfoEntity findByCode(String code);

    @Cacheable(value = "pac_hivinfo_cache_findByIDs")
    @Query("SELECT e FROM PacHivInfoEntity e WHERE ID IN :ids")
    public List<PacHivInfoEntity> findByIDs(@Param("ids") Set<Long> siteIDs);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_hivinfo_cache_findByCode", allEntries = true)
        ,@CacheEvict(value = "pac_hivinfo_cache_findByIDs", allEntries = true)
    })
    public void deleteAll();

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_hivinfo_cache_findByCode", allEntries = true)
        ,@CacheEvict(value = "pac_hivinfo_cache_findByIDs", allEntries = true)
    })
    public void delete(PacHivInfoEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_hivinfo_cache_findByCode", allEntries = true)
        ,@CacheEvict(value = "pac_hivinfo_cache_findByIDs", allEntries = true)
    })
    public <S extends PacHivInfoEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "pac_hivinfo_cache_findByCode", allEntries = true)
        ,@CacheEvict(value = "pac_hivinfo_cache_findByIDs", allEntries = true)
    })
    public <S extends PacHivInfoEntity> S save(S s);
    
    @Query("SELECT COUNT(e) FROM PacHivInfoEntity e WHERE e.ID <> :ID AND e.code = :code")
    public Long countByCode(@Param("ID") Long ID,@Param("code") String code);

}
