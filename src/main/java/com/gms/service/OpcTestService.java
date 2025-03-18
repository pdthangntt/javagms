package com.gms.service;

import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.db.OpcTestEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class OpcTestService extends OpcBaseService {

    /**
     * Tham số
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcTestEntity> getParameters(List<OpcTestEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<Long> ids = new HashSet<>(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getID")));
        List<OpcParameterEntity> parameters = opcParameterRepository.findByTypeAndObjectID(OpcParameterTypeEnum.TEST.getKey(), ids);
        Map<Long, Map<String, List<String>>> mParameters = new HashMap<>();
        for (OpcParameterEntity parameter : parameters) {
            if (mParameters.get(parameter.getObjectID()) == null) {
                mParameters.put(parameter.getObjectID(), new HashMap<String, List<String>>());
            }
            if (mParameters.get(parameter.getObjectID()).get(parameter.getAttrName()) == null) {
                mParameters.get(parameter.getObjectID()).put(parameter.getAttrName(), new ArrayList<String>());
            }
            mParameters.get(parameter.getObjectID()).get(parameter.getAttrName()).add(parameter.getParameterID());
        }
        for (OpcTestEntity entity : entities) {
            Map<String, List<String>> mAttrs = mParameters.getOrDefault(entity.getID(), null);
            if (mAttrs == null) {
                continue;
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.CD4_CAUSE.getKey(), null) != null) {
                entity.setCd4Causes(mAttrs.getOrDefault(OpcParameterAttributeEnum.CD4_CAUSE.getKey(), null));
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE.getKey(), null) != null) {
                entity.setCotrimoxazoleEndCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE.getKey(), null));
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.INH_END_CAUSE.getKey(), null) != null) {
                entity.setInhEndCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.INH_END_CAUSE.getKey(), null));
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.LAO_SYMPTOM.getKey(), null) != null) {
                entity.setLaoSymptoms(mAttrs.getOrDefault(OpcParameterAttributeEnum.LAO_SYMPTOM.getKey(), null));
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.NTCH_SYMPTOM.getKey(), null) != null) {
                entity.setNtchSymptoms(mAttrs.getOrDefault(OpcParameterAttributeEnum.NTCH_SYMPTOM.getKey(), null));
            }
        }
        return entities;
    }

    /**
     * @auth vvThành
     * @param ID
     * @return
     */
    public OpcTestEntity findOne(Long ID) {
        Optional<OpcTestEntity> optional = testRepository.findById(ID);
        OpcTestEntity model = null;
        if (optional.isPresent()) {
            List<OpcTestEntity> entitys = new ArrayList<>();
            entitys.add(optional.get());
            getParameters(entitys);
            model = entitys.get(0);
        }
        return model;
    }

    /**
     * Lưu cho thông tin khác
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcTestEntity save(OpcTestEntity model) throws Exception {
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
            testRepository.save(model);

            //Chọn nhiều
            updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.LAO_SYMPTOM, model.getID(), model.getLaoSymptoms());
            updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.INH_END_CAUSE, model.getID(), model.getInhEndCauses());
            updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.NTCH_SYMPTOM, model.getID(), model.getNtchSymptoms());
            updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, model.getID(), model.getCotrimoxazoleEndCauses());
            updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.CD4_CAUSE, model.getID(), model.getCd4Causes());

            updateArvFromTest(oArv.get());
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Xoá thông tin xét nghiệm khỏi hệ thống
     *
     * @auth vvThành
     * @param testID
     * @return
     * @throws Exception
     */
    public boolean del(Long testID) throws Exception {
        Optional<OpcTestEntity> oTest = testRepository.findById(testID);
        if (!oTest.isPresent()) {
            getLogger().warn("Không tìm thấy thông tin xét nghiệm: " + testID);
            return false;
        }
        OpcTestEntity test = oTest.get();
        Optional<OpcArvEntity> oArv = arvRepository.findById(test.getArvID());
        if (!oArv.isPresent()) {
            getLogger().warn("Không tìm thấy bệnh án: " + test.getArvID());
            return false;
        }

        testRepository.delete(test);
        updateArvFromTest(oArv.get());
        return true;
    }

    /**
     * Tìm theo ID bệnh án
     *
     * @param id
     * @param isRemove
     * @return
     */
    public List<OpcTestEntity> findByArvID(Long id, boolean isRemove) {
        List<OpcTestEntity> entities = testRepository.findByArvID(id, isRemove);
        getParameters(entities);
        return entities;
    }
    
    /**
     * Tìm theo ID bệnh án
     *
     * @param id
     * @param isRemove
     * @return
     */
    public List<OpcTestEntity> findByPatientID(Long id, boolean isRemove) { 
        List<OpcTestEntity> entities = testRepository.findByPatientID(id, isRemove);
        getParameters(entities);
        return entities;
    }
    
    public List<OpcTestEntity> findByPatientIDorderByCD4Time(Long id, boolean isRemove, Long siteID) {
        List<OpcTestEntity> entities = testRepository.findByPatientIDorderByCD4TestTime(id, isRemove, siteID);
        getParameters(entities);
        return entities;
    }

    /**
     * Lịch sử sử dụng
     *
     * @param arvID
     * @param ID
     * @return
     */
    public List<OpcArvLogEntity> getLogs(Long arvID, Long ID) {
        return logRepository.findByArvIDAndTestID(arvID, ID);
    }
    
    /**
     * Kiểm tra nếu tồn tại bản ghi XN và dự phòng
     * 
     * @param stageID
     * @return 
     */
    public boolean existByStageID(Long stageID) {
        return testRepository.countByStageID(stageID);
    }
}
