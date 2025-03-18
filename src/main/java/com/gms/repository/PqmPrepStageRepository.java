package com.gms.repository;

import com.gms.entity.db.PqmPrepStageEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdThang
 */
@Repository
public interface PqmPrepStageRepository extends CrudRepository<PqmPrepStageEntity, Long> {

    @Override
    public List<PqmPrepStageEntity> findAll();

    @Query("SELECT s FROM PqmPrepStageEntity s WHERE s.prepID = :prepID ORDER BY s.startTime DESC")
    public List<PqmPrepStageEntity> findMaxByPrepID(@Param("prepID") Long prepID);

    @Query(" SELECT e FROM PqmPrepStageEntity e WHERE e.prepID IN (:prepID) ")
    public List<PqmPrepStageEntity> findByPrepIDs(@Param("prepID") Set<Long> prepID);

    @Query(" SELECT e FROM PqmPrepStageEntity e WHERE e.prepID = :prepID "
            + " AND ( DATE_FORMAT(e.startTime, '%Y-%m-%d')  = :startTime  )")
    public List<PqmPrepStageEntity> findByPrepIDAndStartTime(@Param("prepID") Long prepID, @Param("startTime") String startTime);
}
