package com.gms.repository;

import com.gms.entity.db.OpcTestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcTestRepository extends CrudRepository<OpcTestEntity, Long> {

    @Override
    public Optional<OpcTestEntity> findById(Long ID);

    @Override
    public OpcTestEntity save(OpcTestEntity model);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.cd4TestTime is not null AND e.remove = false ORDER BY e.cd4TestTime asc, e.ID asc")
    public List<OpcTestEntity> findCd4ByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.lao is true AND e.remove = false ORDER BY e.laoTestTime asc, e.ID asc")
    public List<OpcTestEntity> findLaoByArvID(@Param("arvID") Long arvID);
    
    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.laoTreatment is true AND e.remove = false ORDER BY e.laoStartTime asc, e.ID asc")
    public List<OpcTestEntity> findLaoTreatmentByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.hbv is true AND e.remove = false ORDER BY e.hbvTime asc, e.ID asc")
    public List<OpcTestEntity> findHbvByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.hcv is true AND e.remove = false ORDER BY e.hbvTime asc, e.ID asc")
    public List<OpcTestEntity> findHcvByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.inh is true AND e.remove = false ORDER BY e.inhFromTime asc, e.ID asc")
    public List<OpcTestEntity> findInhByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.ntch is true AND e.remove = false ORDER BY e.createAT asc, e.ID asc")
    public List<OpcTestEntity> findNtchByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.cotrimoxazoleFromTime is not null AND e.remove = false ORDER BY e.cotrimoxazoleFromTime asc, e.ID asc")
    public List<OpcTestEntity> findCotrimoxazoleByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID=:arvID AND e.remove = :remove ORDER BY e.createAT desc")
    public List<OpcTestEntity> findByArvID(@Param("arvID") Long arvID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.remove = :remove ORDER BY e.updateAt desc")
    public List<OpcTestEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.remove = :remove AND e.siteID = :siteID ORDER BY e.cd4TestTime asc")
    public List<OpcTestEntity> findByPatientIDorderByCD4TestTime(@Param("patientID") Long patientID, @Param("remove") boolean isRemove, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.cd4TestTime is not null AND e.remove = false ORDER BY e.cd4TestTime asc")
    public List<OpcTestEntity> findCd4ByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.lao is true AND e.remove = false ORDER BY e.laoTestTime asc")
    public List<OpcTestEntity> findLaoByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.hbv is true AND e.remove = false ORDER BY e.hbvTime asc")
    public List<OpcTestEntity> findHbvByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.hcv is true AND e.remove = false ORDER BY e.hcvTime asc")
    public List<OpcTestEntity> findHcvByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.inh is true AND e.remove = false ORDER BY e.inhFromTime asc")
    public List<OpcTestEntity> findInhByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.ntch is true AND e.remove = false ORDER BY e.cotrimoxazoleFromTime asc")
    public List<OpcTestEntity> findNtchByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcTestEntity e WHERE e.patientID=:patientID AND e.cotrimoxazoleFromTime is not null AND e.remove = false ORDER BY e.cotrimoxazoleFromTime asc")
    public List<OpcTestEntity> findCotrimoxazoleByPatientID(@Param("patientID") Long patientID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM opc_test WHERE arv_id = :arvID", nativeQuery = true)
    public void deleteByArvID(@Param("arvID") Long arvID);
    
    @Query("SELECT count(e) > 0 FROM OpcTestEntity e WHERE e.stageID = :stageID")
    public boolean countByStageID(@Param("stageID") Long stageID);
    
    @Query("SELECT e FROM OpcTestEntity e WHERE e.arvID IN (:arvID) "
            + "AND e.siteID = :siteID "
            + "AND DATE_FORMAT(e.createAT, '%Y-%m-%d') BETWEEN :createdAtFrom AND :createdAtTo "
            + "AND e.remove = 0 ")
    public List<OpcTestEntity> findVisitPreArv(
            @Param("arvID") List<Long> arvID,
            @Param("siteID") Long siteID,
            @Param("createdAtFrom") String createdAtFrom,
            @Param("createdAtTo") String createdAtTo);
}
