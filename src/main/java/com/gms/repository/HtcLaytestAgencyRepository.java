package com.gms.repository;

import com.gms.entity.db.HtcLaytestAgencyEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for HtcLaytestAgencyRepository 
 * Danh sách người được giới thiệu xét nghiệm không chuyên
 * 
 * @author TrangBN
 */
@Repository
public interface HtcLaytestAgencyRepository extends CrudRepository<HtcLaytestAgencyEntity, Long> {
    
    /**
     * Tìm thông tin người được giới thiệu bởi ID người xét nghiệm không chuyên
     * 
     * @param id
     * @return 
     */
    
    public List<HtcLaytestAgencyEntity> findByLaytestID(Long id);
    
    /**
     * Xóa thông tin dựa trên người được giới thiệu dựa trên ID người giới thiệu
     * 
     * @param id
     * @return 
     */
    public long deleteByLaytestID(@Param("id") Long id);
    
    @Query("SELECT e FROM HtcLaytestAgencyEntity e WHERE e.laytestID IN :ids")
    public List<HtcLaytestAgencyEntity> findAllByLaytestIDs(@Param("ids") Set<Long> ids);
    
    
    @Query("SELECT a FROM HtcLaytestEntity e INNER JOIN HtcLaytestAgencyEntity a ON a.laytestID = e.ID WHERE e.remove = false AND e.createdBY = :createdBy "
            + " AND (e.code LIKE CONCAT('%',:code, '%') OR :code IS '' OR :code IS NULL) "
            + " AND (e.patientName LIKE CONCAT('%',:fullname, '%') OR a.fullname LIKE CONCAT('%',:fullname, '%') OR :fullname IS '' OR :fullname IS NULL) "
            + " AND (a.isAgreePreTest = :status OR :status IS '' OR :status IS NULL) "
            + "ORDER BY e.updatedBY desc ")   
    public Page<HtcLaytestAgencyEntity> find( 
            @Param("code") String code,
            @Param("fullname") String fullname,
            @Param("status") String status,
            @Param("createdBy") Long createdBy,
            Pageable pageable);
    
}