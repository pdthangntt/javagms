package com.gms.service;

import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class OpcVisitService extends OpcBaseService {

    /**
     * Danh sách lượt khám theo bệnh án/xoá
     *
     * @param arvID
     * @auth DSNAnh
     * @param remove
     * @return
     */
    public List<OpcVisitEntity> findByArvID(Long arvID, boolean remove) {
        return visitRepository.findByArvID(arvID, remove);
    }

    public List<OpcVisitEntity> findByExaminationTimeAndStageIDImportVisit(String date, Long stageID) {
        List<OpcVisitEntity> list = visitRepository.findByExaminationTimeAndStageIDImportVisit(date, stageID);
        return list == null || list.isEmpty() ? null : list;
    }

    /**
     * Danh sách lượt khám theo bệnh nhân/xoá
     *
     * @auth vvThành
     * @param patientID
     * @param remove
     * @return
     */
    public List<OpcVisitEntity> find(Long patientID, boolean remove) {
        return visitRepository.findByPatientID(patientID, remove);
    }

    public OpcVisitEntity findByArvIDAndStageIDAndDataID(Long arvID, Long stageID, String dataID) {
        List<OpcVisitEntity> visits = visitRepository.findByArvIDAndStageIDAndDataID(arvID, stageID, dataID);
        return visits.isEmpty() ? null : visits.get(0);
    }

    /**
     * @auth vvThành
     * @param ID
     * @return
     */
    public OpcVisitEntity findOne(Long ID) {
        Optional<OpcVisitEntity> optional = visitRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Lưu lượt khám
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcVisitEntity save(OpcVisitEntity model) throws Exception {
        try {
            Optional<OpcArvEntity> oArv = arvRepository.findById(model.getArvID());
            if (!oArv.isPresent()) {
                throw new Exception(String.format("Không tìm thấy bệnh án có mã %s", model.getArvID()));
            }
            Optional<OpcStageEntity> oStage = stageRepository.findById(model.getStageID());

            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());

            visitRepository.save(model);
            updateArvAndStageFromVisit(oArv.get(), oStage.get());
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    public LinkedList<OpcVisitEntity> findByStageIDAndOderByExaminationTime(Long satgeID) {
        LinkedList<OpcVisitEntity> list = visitRepository.findByStageIDAndOderByExaminationTime(satgeID);
        return list == null || list.isEmpty() ? null : list;
    }

    public OpcVisitEntity saveVNPT(OpcVisitEntity model) throws Exception {
        try {
            Optional<OpcArvEntity> oArv = arvRepository.findById(model.getArvID());
            Optional<OpcStageEntity> oStage = stageRepository.findById(model.getStageID());

            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());

            visitRepository.save(model);
            if (StringUtils.isEmpty(model.getTreatmentRegimenID())) {
                List<OpcVisitEntity> visits = visitRepository.findByStageIDAndOderByExaminationTime(model.getStageID());
                if (visits != null && !visits.isEmpty()) {
                    String key1 = "";
                    String key2 = "";
                    for (OpcVisitEntity visit : visits) {
                        if (StringUtils.isNotEmpty(visit.getTreatmentRegimenID())) {
                            key1 = visit.getTreatmentRegimenID();
                            key2 = visit.getTreatmentRegimenStage();
                        } else {
                            visit.setTreatmentRegimenID(key1);
                            visit.setTreatmentRegimenStage(StringUtils.isEmpty(key2) ? "1" : key2);
                            visitRepository.save(visit);
                        }
                    }
                }
            }
            
            
            updateArvAndStageFromVisit(oArv.get(), oStage.get());
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Xoá thông tin xét nghiệm khỏi hệ thống
     *
     * @auth vvThành
     * @param visitID
     * @return
     * @throws Exception
     */
    public boolean del(Long visitID) throws Exception {
        Optional<OpcVisitEntity> optional = visitRepository.findById(visitID);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy thông tin lượt khám: " + visitID);
        }
        OpcVisitEntity model = optional.get();
        Optional<OpcArvEntity> oArv = arvRepository.findById(model.getArvID());
        if (!oArv.isPresent()) {
            throw new Exception("Không tìm thấy bệnh án: " + model.getArvID());
        }
        Optional<OpcStageEntity> oStage = stageRepository.findById(model.getStageID());
        visitRepository.delete(model);
        //Update lại bệnh án và điều trị
        updateArvAndStageFromVisit(oArv.get(), oStage.get());
        return true;
    }

    /**
     * Lịch sử sử dụng
     *
     * @param arvID
     * @param ID
     * @return
     */
    public List<OpcArvLogEntity> getLogs(Long arvID, Long ID) {
        return logRepository.findByArvIDAndVisitID(arvID, ID);
    }

    /**
     * Kiểm tra có thông tin lượt khám tương tứng với
     *
     * @
     * @param stageID
     * @return
     */
    public boolean existByStageID(Long stageID) {
        return visitRepository.countByStageID(stageID);
    }

    /**
     * Danh sách lượt khám theo bệnh nhân sắp xếp tăng đần
     *
     * @auth vvThành
     * @param patientID
     * @param remove
     * @return
     */
    public List<OpcVisitEntity> findByPatientAsc(Long patientID, boolean remove) {
        return visitRepository.findByPatientIDAsc(patientID, remove);
    }

    /**
     * Danh sách lượt khám trong ngày theo bệnh nhân sắp xếp giảm dần
     *
     * @auth DSNAnh
     * @param arvID
     * @param createdAtFrom
     * @param createdAtTo
     * @return list
     */
    public List<OpcVisitEntity> findByArvID(Long arvID, String createdAtFrom, String createdAtTo) {
        return visitRepository.findByArvID(arvID, createdAtFrom, createdAtTo);
    }
}
