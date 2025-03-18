package com.gms.service;

import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcStageEntity;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class OpcStageService extends OpcBaseService {

    @Autowired
    private OpcStageService opcStageService;

    /**
     * Lưu cho điều trị
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcStageEntity save(OpcStageEntity model) throws Exception {
        try {
            Optional<OpcArvEntity> oArv = arvRepository.findById(model.getArvID());
            if (!oArv.isPresent()) {
                throw new Exception(String.format("Không tìm thấy bệnh án có mã %s", model.getArvID()));
            }

            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            //Validate giai đoạn điều trị
            if (!validateStage(model)) {
                throw new Exception("Giai đoạn điều trị đã tồn tại");
            }

            OpcStageEntity oldStage = null;
            if (model.getID() != null) {
                Optional<OpcStageEntity> optional = stageRepository.findById(model.getID());
                oldStage = optional.get().clone();
            }

            updateToTreatmentStage(model);
            stageRepository.save(model);
            updateArvFromStage(oArv.get());

            // Kiểm tra có phải là giai đoạn duy nhất của 1 bệnh án
//            List<OpcStageEntity> stageEntities = opcStageService.findByPatientID(model.getPatientID(), false);
//            if (stageEntities != null && stageEntities.size() > 0) {
//                oldStage = new OpcStageEntity();
//            }
            // Lưu log REVISION nếu có thay đổi hoặc thêm mới trạng thái điều trị
            trackChangeStage(oldStage, model);
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public OpcStageEntity saveVNPT(OpcStageEntity model) throws Exception {
        try {
            Optional<OpcArvEntity> oArv = arvRepository.findById(model.getArvID());
            if (!oArv.isPresent()) {
                throw new Exception(String.format("Không tìm thấy bệnh án có mã %s", model.getArvID()));
            }

            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            //Validate giai đoạn điều trị
//            if (!validateStage(model)) {
//                throw new Exception("Giai đoạn điều trị đã tồn tại");
//            }

            OpcStageEntity oldStage = null;
            if (model.getID() != null) {
                Optional<OpcStageEntity> optional = stageRepository.findById(model.getID());
                oldStage = optional.get().clone();
            }

//            updateToTreatmentStage(model);
            if (model.getEndTime() != null && !StringUtils.isEmpty(model.getEndCase())) {
                model.setTreatmentStageTime(model.getEndTime());
                model.setTreatmentStageID(model.getEndCase());
            } else {
                model.setTreatmentStageTime(model.getRegistrationTime());
                model.setTreatmentStageID(model.getRegistrationType().equals("4") || model.getRegistrationType().equals("5") ? "1" : model.getRegistrationType());
            }

            if (StringUtils.isEmpty(model.getEndCase())) {
                model.setTreatmentStageID("1");
            }
            if (model.getTreatmentStageTime() == null) {
                model.setTreatmentStageTime(new Date());
            }

            stageRepository.save(model);
            updateArvFromStage(oArv.get());

            // Kiểm tra có phải là giai đoạn duy nhất của 1 bệnh án
//            List<OpcStageEntity> stageEntities = opcStageService.findByPatientID(model.getPatientID(), false);
//            if (stageEntities != null && stageEntities.size() > 0) {
//                oldStage = new OpcStageEntity();
//            }
            // Lưu log REVISION nếu có thay đổi hoặc thêm mới trạng thái điều trị
            trackChangeStage(oldStage, model);
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Xoá stage khỏi hệ thống
     *
     * @throws java.lang.Exception
     * @auth vvThành
     * @param stageID
     * @return
     */
    public boolean del(Long stageID) throws Exception {
        Optional<OpcStageEntity> oStage = stageRepository.findById(stageID);
        if (!oStage.isPresent()) {
            getLogger().warn("Không tìm thấy thông tin điều trị: " + stageID);
            return false;
        }
        OpcStageEntity stage = oStage.get();
        Optional<OpcArvEntity> oArv = arvRepository.findById(stage.getArvID());
        if (!oArv.isPresent()) {
            getLogger().warn("Không tìm thấy bệnh án: " + stage.getArvID());
            return false;
        }

        stageRepository.delete(stage);
        updateArvFromStage(oArv.get());
        return true;
    }

    /**
     * Tìm theo ID bệnh án
     *
     * @param id
     * @param isRemove
     * @return
     */
    public List<OpcStageEntity> findByArvID(Long id, boolean isRemove) {
        return stageRepository.findByArvID(id, isRemove);
    }

    public List<OpcStageEntity> findByArvIDExcelVisit(Long id) {
        List<OpcStageEntity> stages = stageRepository.findByArvIDExcelVisit(id);
        return stages.isEmpty() ? null : stages;
    }

    public List<OpcStageEntity> findByArvIDAndSiteID(Long arvID, Long siteID) {
        return stageRepository.findByArvIDAndSiteID(arvID, siteID);
    }

    public List<OpcStageEntity> findByArvIDAndSite(Long arvID, Long siteID) {
        return stageRepository.findByArvIDAndSite(arvID, siteID);
    }

    public OpcStageEntity findByArvIDAndDataIDIDAndSite(Long arvID, String dataID, Long siteID) {
        List<OpcStageEntity> stages = stageRepository.findByArvIDAndDataIDIDAndSite(arvID, dataID, siteID);
        return stages.isEmpty() ? null : stages.get(0);
    }

    public List<OpcStageEntity> findByPatientID(Long patientID) {
        return stageRepository.findByPatientID(patientID);
    }

    public List<OpcStageEntity> findByPatientIDIDAndSiteID(Long patientID, Long siteID) {
        return stageRepository.findByPatientIDIDAndSiteID(patientID, siteID);
    }

    public OpcStageEntity findByPatientIDAndSiteID(Long patientID, Long siteID) {
        List<OpcStageEntity> list = stageRepository.findByPatientIDAndSiteIDAndEndTimeNull(patientID, siteID);
        return list.isEmpty() ? stageRepository.findByPatientIDAndSiteIDOrderByEndTime(patientID, siteID).get(0) : list.get(0);
    }

    /**
     * Tìm theo
     *
     * @param id
     * @param isRemove
     * @param siteID
     * @return
     */
    public List<OpcStageEntity> findByPatientID(Long id, boolean isRemove, Long siteID) {
        return stageRepository.findByPatientID(id, isRemove, siteID);
    }

    /**
     * Danh sách log của đợt điều trị
     *
     * @param arvID
     * @param stageID
     * @return
     */
    public List<OpcArvLogEntity> getLogs(Long arvID, Long stageID) {
        return logRepository.findByArvIDAndStageID(arvID, stageID);
    }

    public List<OpcStageEntity> findByArvCode(String arvCode) {
        return stageRepository.findByArvCode(arvCode, getCurrentUser().getUser().getSiteID());
    }

    /**
     * Lấy thông tin theo ID
     *
     * @param ID
     * @return
     */
    public OpcStageEntity findOne(Long ID) {
        Optional<OpcStageEntity> optional = stageRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Tìm theo ID của bệnh nhân
     *
     * @param id
     * @param isRemove
     * @return
     */
    public List<OpcStageEntity> findByPatientID(Long id, boolean isRemove) {
        return stageRepository.findByPatientID(id, isRemove);
    }

    /**
     * Kiểm tra tồn tại thông tin Stage
     *
     * @param entity
     * @param isUpdate
     * @return
     */
    public boolean existStage(OpcStageEntity entity, boolean isUpdate) {
        return stageRepository.existStage(isUpdate, entity.getID(), entity.isRemove(), entity.getPatientID(), entity.getTreatmentTime(), entity.getEndTime()) > 0;
    }
}
