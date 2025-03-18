package com.gms.repository;

import com.gms.entity.db.OpcStageEntity;
import java.util.Date;
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
public interface OpcStageRepository extends CrudRepository<OpcStageEntity, Long> {

    @Override
    public Optional<OpcStageEntity> findById(Long ID);

    @Override
    public OpcStageEntity save(OpcStageEntity model);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.remove = :remove ORDER BY e.treatmentTime asc, e.ID asc")
    public List<OpcStageEntity> findByArvID(@Param("arvID") Long arvID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.remove = :remove ORDER BY e.registrationTime desc, e.ID desc")
    public List<OpcStageEntity> findByArvIDOrderRegis(@Param("arvID") Long arvID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.remove = false AND e.siteID = :siteID ORDER BY e.treatmentTime asc")
    public List<OpcStageEntity> findByArvIDAndSiteID(@Param("arvID") Long arvID, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.siteID = :siteID ORDER BY e.treatmentTime asc")
    public List<OpcStageEntity> findByArvIDAndSite(@Param("arvID") Long arvID, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.siteID = :siteID AND e.dataID = :dataID ORDER BY e.treatmentTime asc")
    public List<OpcStageEntity> findByArvIDAndDataIDIDAndSite(@Param("arvID") Long arvID, @Param("dataID") String dataID, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.patientID=:patientID AND e.remove = false AND e.siteID = :siteID ORDER BY e.treatmentTime asc")
    public List<OpcStageEntity> findByPatientIDIDAndSiteID(@Param("patientID") Long patientID, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.patientID=:patientID AND e.remove = false ORDER BY e.treatmentTime asc")
    public List<OpcStageEntity> findByPatientID(@Param("patientID") Long patientID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.patientID=:patientID AND e.remove = false AND e.siteID = :siteID ORDER BY e.endTime desc")
    public List<OpcStageEntity> findByPatientIDAndSiteIDOrderByEndTime(@Param("patientID") Long patientID, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.patientID=:patientID AND e.remove = false AND e.siteID = :siteID and e.endTime  is null")
    public List<OpcStageEntity> findByPatientIDAndSiteIDAndEndTimeNull(@Param("patientID") Long patientID, @Param("siteID") Long siteID);

    @Query(value = "SELECT e.* FROM opc_stage e WHERE e.patient_id=:patientID AND e.is_remove = :remove ORDER BY e.registration_time desc, e.ID desc", nativeQuery = true)
    public List<OpcStageEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.patientID=:patientID AND e.remove = :remove AND e.siteID = :siteID ORDER BY e.registrationTime asc, e.ID asc")
    public List<OpcStageEntity> findByPatientID(@Param("patientID") Long patientID, @Param("remove") boolean isRemove, @Param("siteID") Long siteID);

    @Query("SELECT e FROM OpcStageEntity e WHERE e.arvID=:arvID AND e.remove = false ORDER BY e.registrationTime desc, e.ID desc")
    public List<OpcStageEntity> findByArvIDExcelVisit(@Param("arvID") Long arvID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM opc_stage WHERE arv_id = :arvID", nativeQuery = true)
    public void deleteByArvID(@Param("arvID") Long arvID);

    @Query("SELECT e FROM OpcStageEntity as e WHERE e.arvID IN ( SELECT t.ID FROM OpcArvEntity as t WHERE t.code = :code and t.remove = false ) AND e.siteID = :siteID ORDER BY e.registrationTime desc, e.ID desc")
    public List<OpcStageEntity> findByArvCode(@Param("code") String code,@Param("siteID") Long siteID);

    /**
     * Kiểm tra tồn tại stage
     *
     * @param isUpdate
     * @param id
     * @param isRemove
     * @param patientID
     * @param treatmentTime
     * @param endTime
     * @return
     */
    @Query("SELECT count(e) FROM OpcStageEntity e WHERE e.patientID=:patientId AND e.remove = :remove and treatmentTime = :treatmentTime and endTime = :endTime AND ((:isUpdate = true and e.id <> :id) OR :isUpdate = false)")
    public int existStage(@Param("isUpdate") boolean isUpdate, @Param("id") Long id, @Param("remove") boolean isRemove, @Param("patientId") Long patientID, @Param("treatmentTime") Date treatmentTime, @Param("endTime") Date endTime);
}
