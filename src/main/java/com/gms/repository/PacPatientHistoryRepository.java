package com.gms.repository;

import com.gms.entity.db.PacPatientHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdThang
 */
@Repository
public interface PacPatientHistoryRepository extends CrudRepository<PacPatientHistoryEntity, Long> {

    @Query(" SELECT e FROM PacPatientHistoryEntity e WHERE e.parentID = :parentID "
            + " ORDER BY e.confirmTime ASC "
    )
    public List<PacPatientHistoryEntity> findByPatientID(
            @Param("parentID") Long parentID
    );
    
    /**
     * Lấy thông tin lịch sử theo mã người nhiễm
     * @param detectRefID
     * @return 
     */
    public List<PacPatientHistoryEntity> findByDetectRefID(Long detectRefID);

}
