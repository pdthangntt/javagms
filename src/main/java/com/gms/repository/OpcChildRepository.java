package com.gms.repository;

import com.gms.entity.db.OpcChildEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdThang
 */
@Repository
public interface OpcChildRepository extends CrudRepository<OpcChildEntity, Long> {

    @Override
    public Optional<OpcChildEntity> findById(Long ID);

    @Override
    public OpcChildEntity save(OpcChildEntity model);

    @Query("SELECT e FROM OpcChildEntity e WHERE e.arvID=:arvID AND e.stageID = :stageID ORDER BY e.dob desc")
    public List<OpcChildEntity> findByArvIDAndStageID(@Param("arvID") Long arvID, @Param("stageID") Long stageID);
    
    @Query("SELECT e FROM OpcChildEntity e WHERE e.arvID=:arvID AND e.stageID = :stageID AND e.visitID = :visitID ORDER BY e.dob desc")
    public List<OpcChildEntity> findByArvIDAndStageIDAndVisitID(@Param("arvID") Long arvID, @Param("stageID") Long stageID, @Param("visitID") Long visitID);
    
    @Query("SELECT e FROM OpcChildEntity e WHERE e.arvID=:arvID ORDER BY e.dob desc")
    public List<OpcChildEntity> findByArvID(@Param("arvID") Long arvID);

    public long deleteByStageID(@Param("id") Long id);
    
    @Modifying
    @Query("DELETE FROM OpcChildEntity e WHERE e.ID IN :ids")
    public void deleteByIds(@Param("ids")List<Long> ids);
    
    public long deleteByStageIDAndVisitID(@Param("stageID") Long stageID, @Param("visitID") Long visitID);

}
