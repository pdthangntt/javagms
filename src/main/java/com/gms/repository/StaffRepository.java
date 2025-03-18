package com.gms.repository;

import com.gms.entity.db.StaffEntity;
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
 * @author My PC
 */
@Repository
public interface StaffRepository extends CrudRepository<StaffEntity, Long> {

    @Cacheable(value = "staff_find")
    @Query("SELECT e FROM StaffEntity e WHERE (e.email = :email OR :email IS NULL) "
            + "AND (e.siteID = :siteID OR :siteID IS 0) "
            + "AND e.isActive = :active")
    public Page<StaffEntity> find(@Param("siteID") Long siteID, @Param("email") String email, @Param("active") boolean isActive, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN 'true' ELSE 'false' END FROM StaffEntity e WHERE e.email = :email")
    public Boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN 'true' ELSE 'false' END FROM StaffEntity e WHERE e.username = :username")
    public Boolean existsByUsername(@Param("username") String username);

    @Query("SELECT e FROM StaffEntity e WHERE e.username = :username")
    public StaffEntity getByUsername(@Param("username") String username);

    @Query("SELECT e FROM StaffEntity e WHERE e.email = :email")
    public List<StaffEntity> getByEmail(@Param("email") String email);

    @Cacheable(value = "staff_findBySiteID")
    @Query("SELECT e FROM StaffEntity e WHERE e.siteID = :siteID ORDER BY name ASC")
    public List<StaffEntity> findBySiteID(@Param("siteID") Long siteID);

    @Override
    @Cacheable(value = "staff_findAllById")
    public List<StaffEntity> findAllById(Iterable<Long> ids);

    public List<StaffEntity> findByEmail(String email);

    public StaffEntity findByToken(String token);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_find", allEntries = true)
        ,@CacheEvict(value = "findAllAdmin", allEntries = true)
        ,@CacheEvict(value = "staff_findBySiteID", allEntries = true)
        ,@CacheEvict(value = "staff_findAllById", allEntries = true)
    })
    public void delete(StaffEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_find", allEntries = true)
        ,@CacheEvict(value = "findAllAdmin", allEntries = true)
        ,@CacheEvict(value = "staff_findBySiteID", allEntries = true)
        ,@CacheEvict(value = "staff_findAllById", allEntries = true)
    })
    public void deleteById(Long id);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_find", allEntries = true)
        ,@CacheEvict(value = "findAllAdmin", allEntries = true)
        ,@CacheEvict(value = "staff_findBySiteID", allEntries = true)
        ,@CacheEvict(value = "staff_findAllById", allEntries = true)
    })
    public <S extends StaffEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_find", allEntries = true)
        ,@CacheEvict(value = "findAllAdmin", allEntries = true)
        ,@CacheEvict(value = "staff_findBySiteID", allEntries = true)
        ,@CacheEvict(value = "staff_findAllById", allEntries = true)
    })
    public <S extends StaffEntity> S save(S s);

    /**
     * @author pdThang
     *
     * @param siteID
     * @param email
     * @param isActive
     * @param id
     * @param username
     * @param name
     * @param phone
     * @param siteIDs
     * @param pageable
     * @return
     */
    @Cacheable(value = "findAllAdmin")
    @Query("SELECT e FROM StaffEntity e WHERE e.isActive = :active "
            + " AND ( e.ID = :id OR :id = 0) "
            + " AND ( e.username LIKE CONCAT('%',:username, '%') OR :username IS '' OR :username IS NULL ) "
            + " AND ( e.email LIKE CONCAT('%',:email, '%') OR :email IS '' OR :email IS NULL ) "
            + " AND ( e.phone LIKE CONCAT('%',:phone, '%') OR :phone IS '' OR :phone IS NULL ) "
            + " AND ( e.name LIKE CONCAT('%',:name, '%') OR :name IS '' OR :name IS NULL ) "
            + " AND ( e.siteID IN (:siteIDs) OR coalesce(:siteIDs, null) IS NULL) "
    )
    public Page<StaffEntity> findAllAdmin(
            @Param("email") String email,
            @Param("active") boolean isActive,
            @Param("id") Long id,
            @Param("username") String username,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("siteIDs") Set<Long> siteIDs,
            Pageable pageable);

}
