package com.gms.repository;

import com.gms.entity.db.FeedbackEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author NamAnh_HaUI
 */

@Repository
public interface FeedbackRepository extends CrudRepository<FeedbackEntity, Long>{
    
    @Cacheable(value = "htc_target_cache")
    @CacheEvict(value = "htc_target_cache", allEntries = true)
    @Query("SELECT e FROM FeedbackEntity e WHERE e.isRemove = :isRemove "
            + "AND (e.status = :status OR :status IS '' OR :status IS NULL) ")
    public Page<FeedbackEntity> findAll(
            @Param("status") String status,
            @Param("isRemove") boolean isRemove,
            Pageable pageable);
    
    @Query("SELECT e FROM FeedbackEntity e WHERE e.ID = :ID")
    public FeedbackEntity findOne(@Param("ID") Long ID);
    
}
