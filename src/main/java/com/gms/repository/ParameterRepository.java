package com.gms.repository;

import com.gms.entity.db.ParameterEntity;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface ParameterRepository extends CrudRepository<ParameterEntity, Long> {

    @Cacheable(value = "parameter_findByTypeAndCode")
    @Query("SELECT e FROM ParameterEntity e WHERE e.type = :type AND e.code = :code ORDER BY position ASC")
    public ParameterEntity findByTypeAndCode(@Param("type") String type, @Param("code") String code);

    @Cacheable(value = "parameter_findByTypes")
    @Query("SELECT e FROM ParameterEntity e WHERE e.type IN :types ORDER BY position ASC")
    public List<ParameterEntity> findByTypes(@Param("types") Set<String> types);

    @Cacheable(value = "parameter_findByType")
    @Query("SELECT e FROM ParameterEntity e WHERE e.type = :type ORDER BY position ASC")
    public List<ParameterEntity> findByType(@Param("type") String type);

    @Cacheable(value = "parameter_findByType_search")
    @Query("SELECT e FROM ParameterEntity e WHERE e.status = '1' AND e.type = :type "
            + " AND ((e.attribute01 = :attribute01 OR :attribute01 IS '' OR :attribute01 IS NULL) "
            + " OR (e.attribute02 = :attribute02 OR :attribute02 IS '' OR :attribute02 IS NULL)) "
            + "ORDER BY position ASC")
    public List<ParameterEntity> findByType(
            @Param("type") String type,
            @Param("attribute01") String attribute01,
            @Param("attribute02") String attribute02);

    @Cacheable(value = "parameter_findByType_search")
    @Query("SELECT e FROM ParameterEntity e WHERE e.status = '1' AND e.type = :type "
            + " AND ((e.attribute03 = :attribute03 OR :attribute03 IS '' OR :attribute03 IS NULL) "
            + " OR (e.attribute04 = :attribute04 OR :attribute04 IS '' OR :attribute04 IS NULL)"
            + " OR (e.attribute05 = :attribute05 OR :attribute05 IS '' OR :attribute05 IS NULL)"
            + ") "
            + "ORDER BY position ASC")
    public List<ParameterEntity> findByType(
            @Param("type") String type,
            @Param("attribute03") String attribute03,
            @Param("attribute04") String attribute04,
            @Param("attribute05") String attribute05
    );

    @Cacheable(value = "parameter_findByType_search")
    @Query("SELECT e FROM ParameterEntity e WHERE "
            + "e.type = :type "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " AND (e.value LIKE CONCAT('%',:value, '%') OR :value IS '' OR :value IS NULL) "
            + " AND (e.siteID IS :siteID OR :siteID IS '' OR :siteID IS NULL OR :siteID IS 0) "
            + " AND (e.attribute01 = :attribute01 OR :attribute01 IS '' OR :attribute01 IS NULL) "
            + " AND (e.attribute02 = :attribute02 OR :attribute02 IS '' OR :attribute02 IS NULL) "
            + " AND (e.attribute03 = :attribute03 OR :attribute03 IS '' OR :attribute03 IS NULL) "
            + " AND (e.attribute04 = :attribute04 OR :attribute04 IS '' OR :attribute04 IS NULL) "
            + " AND (e.attribute05 = :attribute05 OR :attribute05 IS '' OR :attribute05 IS NULL) "
            + "ORDER BY position ASC")
    public Page<ParameterEntity> findByType(
            @Param("code") String code,
            @Param("value") String value,
            @Param("siteID") Long siteID,
            @Param("type") String type,
            @Param("attribute01") String attribute01,
            @Param("attribute02") String attribute02,
            @Param("attribute03") String attribute03,
            @Param("attribute04") String attribute04,
            @Param("attribute05") String attribute05,
            Pageable pageable);

    @Cacheable(value = "parameter_findByTypeAndCode")
    @Query("SELECT e FROM ParameterEntity e WHERE e.type = :type AND e.code IN :codes ORDER BY position ASC")
    public List<ParameterEntity> findByTypeAndCode(@Param("type") String type, @Param("codes") Set<String> codes);

    @Cacheable(value = "parameter_findByType_cache")
    @Query("SELECT e FROM ParameterEntity e WHERE "
            + "e.type = :type and e.parentID = :parentID "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " AND (e.value LIKE CONCAT('%',:value, '%') OR :value IS '' OR :value IS NULL) "
            + " AND (e.siteID IS :siteID OR :siteID IS '' OR :siteID IS NULL OR :siteID IS 0) "
            + "ORDER BY position ASC")
    public Page<ParameterEntity> findByType(
            @Param("code") String code,
            @Param("value") String value,
            @Param("siteID") Long siteID,
            @Param("parentID") Long parentID,
            @Param("type") String type,
            Pageable pageable);

    @Cacheable(value = "parameter_existsByTypeAndCode")
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ParameterEntity e WHERE e.type = :type AND e.code = :code")
    public boolean existsByTypeAndCode(@Param("type") String type, @Param("code") String code);

    @Cacheable(value = "parameter_findStaffCode")
    @Query("SELECT e FROM ParameterEntity e WHERE e.type LIKE 'staff_%' AND e.code=:code AND LOWER(e.value) = :value")
    public List<ParameterEntity> findStaffCode(@Param("code") String code, @Param("value") String value);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypes", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_cache", allEntries = true)
        ,@CacheEvict(value = "parameter_existsByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findStaffCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
    })
    public <S extends ParameterEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypes", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_cache", allEntries = true)
        ,@CacheEvict(value = "parameter_existsByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findStaffCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
    })
    public <S extends ParameterEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypes", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
        ,@CacheEvict(value = "parameter_findByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_cache", allEntries = true)
        ,@CacheEvict(value = "parameter_existsByTypeAndCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findStaffCode", allEntries = true)
        ,@CacheEvict(value = "parameter_findByType_search", allEntries = true)
    })
    public void delete(ParameterEntity t);
}
