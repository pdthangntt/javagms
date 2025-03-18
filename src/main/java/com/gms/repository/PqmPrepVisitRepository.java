package com.gms.repository;

import com.gms.entity.db.PqmPrepVisitEntity;
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
public interface PqmPrepVisitRepository extends CrudRepository<PqmPrepVisitEntity, Long> {

    @Override
    public List<PqmPrepVisitEntity> findAll();
    
    @Query("SELECT s FROM PqmPrepVisitEntity s WHERE s.prepID = :prepID ORDER BY s.examinationTime DESC")
    public List<PqmPrepVisitEntity> findMaxByPrepID(@Param("prepID") Long prepID);
    
    @Query(" SELECT e FROM PqmPrepVisitEntity e WHERE e.prepID IN (:prepID) ")
    public List<PqmPrepVisitEntity> findByPrepIDs(@Param("prepID") Set<Long> prepID);
    
    @Query(" SELECT e FROM PqmPrepVisitEntity e WHERE e.prepID = :prepID "
            + " AND ( DATE_FORMAT(e.examinationTime, '%Y-%m-%d')  = :examinationTime  )")
    public List<PqmPrepVisitEntity> findByPrepIDAndExaminationTime(@Param("prepID") Long prepID, @Param("examinationTime") String examinationTime);
    
    
}
