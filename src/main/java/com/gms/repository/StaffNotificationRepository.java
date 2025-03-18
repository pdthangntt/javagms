package com.gms.repository;

import com.gms.entity.db.StaffNotificationEntity;
import java.util.Date;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface StaffNotificationRepository extends CrudRepository<StaffNotificationEntity, Long> {

    @Cacheable(value = "staff_notifi_find")
    @Query("SELECT e FROM StaffNotificationEntity e WHERE e.staffID  =:staffID")
    public Page<StaffNotificationEntity> find(@Param("staffID") Long staffID, Pageable pageable);

    @Cacheable(value = "staff_notifi_findReadByStaffID")
    @Query("SELECT e FROM StaffNotificationEntity e WHERE e.staffID = :staffID AND e.read = false")
    public List<StaffNotificationEntity> findReadByStaffID(@Param("staffID") Long staffID);

    @Cacheable(value = "staff_notifi_findOne")
    @Query("SELECT e FROM StaffNotificationEntity e WHERE e.ID = :ID")
    public StaffNotificationEntity findOne(@Param("ID") Long ID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_notifi_find", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findReadByStaffID", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findOne", allEntries = true)
    })
    public void delete(StaffNotificationEntity t);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_notifi_find", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findReadByStaffID", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findOne", allEntries = true)
    })
    public <S extends StaffNotificationEntity> Iterable<S> saveAll(Iterable<S> itrbl);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_notifi_find", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findReadByStaffID", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findOne", allEntries = true)
    })
    public <S extends StaffNotificationEntity> S save(S s);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "staff_notifi_find", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findReadByStaffID", allEntries = true)
        ,@CacheEvict(value = "staff_notifi_findOne", allEntries = true)
    })
    public void deleteAll(Iterable<? extends StaffNotificationEntity> itrbl);

    @Query("SELECT e FROM StaffNotificationEntity e WHERE e.time < :time")
    public List<StaffNotificationEntity> findActivitiesOlderThanAMonth(@Param("time") Date time, Sort by);

}
