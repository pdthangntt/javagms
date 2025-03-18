package com.gms.service;

import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.db.OpcViralLoadEntity;
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
public class OpcViralLoadService extends OpcBaseService {

    /**
     * Tham số
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcViralLoadEntity> getParameters(List<OpcViralLoadEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<Long> ids = new HashSet<>(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getID")));

        List<OpcParameterEntity> parameters = opcParameterRepository.findByTypeAndObjectID(OpcParameterTypeEnum.VIRAL_LOAD.getKey(), ids);
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
        for (OpcViralLoadEntity entity : entities) {
            Map<String, List<String>> mAttrs = mParameters.getOrDefault(entity.getID(), null);
            if (mAttrs == null) {
                continue;
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null) != null) {
                entity.setCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null));
            }
        }
        return entities;
    }

    /**
     * @auth vvThành
     * @param ID
     * @return
     */
    public OpcViralLoadEntity findOne(Long ID) {
        Optional<OpcViralLoadEntity> optional = viralLoadRepository.findById(ID);
        OpcViralLoadEntity model = null;
        if (optional.isPresent()) {
            List<OpcViralLoadEntity> entitys = new ArrayList<>();
            entitys.add(optional.get());
            getParameters(entitys);
            model = entitys.get(0);
        }
        return model;
    }
    
    public OpcViralLoadEntity findByArvIDAndStageIDAndDataID(Long arvID, Long stageID, String dataID) {
        List<OpcViralLoadEntity> virals = viralLoadRepository.findByArvIDAndStageIDAndDataID(arvID, stageID, dataID);
        return virals.isEmpty() ? null : virals.get(0);
    }

    /**
     * Lưu tải lượng virut
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcViralLoadEntity save(OpcViralLoadEntity model) throws Exception {
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
            viralLoadRepository.save(model);

            updateToParameter(OpcParameterTypeEnum.VIRAL_LOAD, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getCauses());

            updateArvFromViralLoad(oArv.get());
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }
    
    
    public OpcViralLoadEntity saveVNPT(OpcViralLoadEntity model) throws Exception {
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
            viralLoadRepository.save(model);

            updateToParameter(OpcParameterTypeEnum.VIRAL_LOAD, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getCauses());
            updateArvFromViralLoad(oArv.get());
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Xoá thông tin xét nghiệm khỏi hệ thống
     *
     * @auth vvThành
     * @param viralLoadID
     * @return
     * @throws Exception
     */
    public boolean delViralLoad(Long viralLoadID) throws Exception {
        Optional<OpcViralLoadEntity> oViralLoad = viralLoadRepository.findById(viralLoadID);
        if (!oViralLoad.isPresent()) {
            getLogger().warn("Không tìm thấy thông tin TLVR: " + viralLoadID);
            return false;
        }
        OpcViralLoadEntity viralLoad = oViralLoad.get();
        Optional<OpcArvEntity> oArv = arvRepository.findById(viralLoad.getArvID());
        if (!oArv.isPresent()) {
            getLogger().warn("Không tìm thấy bệnh án: " + viralLoad.getArvID());
            return false;
        }

        viralLoadRepository.delete(viralLoad);
        updateArvFromViralLoad(oArv.get());
        return true;
    }
    
    /**
     * Tìm theo mã bệnh án
     * 
     * @param id
     * @param deleted
     * @return
     */
    public List<OpcViralLoadEntity> findByArvID(Long id, boolean deleted) {
        List<OpcViralLoadEntity> entities = viralLoadRepository.findByArvID(id, deleted);
        getParameters(entities);
        return entities;
    } 
    
    /**
     * Tìm theo mã bệnh án và cơ sở
     * 
     * @param id
     * @param deleted
     * @param siteID
     * @return
     */
    public List<OpcViralLoadEntity> findByArvIDandSiteID(Long id, boolean deleted, Long siteID) {
        List<OpcViralLoadEntity> entities = viralLoadRepository.findByArvIDandSiteID(id, deleted, siteID);
        getParameters(entities);
        return entities;
    } 
    
    /**
     * đếm lượt khám mã bệnh án
     * 
     * @param id
     * @return
     */
    public int countByArvID(Long id) {
        List<OpcViralLoadEntity> entities = viralLoadRepository.findByArvID(id, getCurrentUser().getSite().getID(), false);
        return entities == null ? 0 : entities.size();
    }   
    
    /**
     * Tìm theo mã bệnh án
     * 
     * @param id
     * @param deleted
     * @return
     */
    public List<OpcViralLoadEntity> findByPatientID(Long id, boolean deleted) {
        List<OpcViralLoadEntity> entities = viralLoadRepository.findByPatientID(id, deleted);
        getParameters(entities);
        return entities;
    }  
    public List<OpcViralLoadEntity> findByPatientID(Long id, boolean deleted, Long siteID) {
        List<OpcViralLoadEntity> entities = viralLoadRepository.findByPatientID(id, deleted, siteID);
        getParameters(entities);
        return entities;
    }  
    
    /**
     * Danh sách log của thông tin tải lượng virus
     *
     * @param arvID
     * @param stageID
     * @return
     */
    public List<OpcArvLogEntity> getLogs(Long arvID, Long stageID) {
        return logRepository.findByArvIDAndViralID(arvID, stageID);
    }
    
    /**
     * Lưu thông tin ds sách tải lượng virus
     * 
     * @param viralload
     * @return 
     */
    public Iterable<OpcViralLoadEntity> saveAll(List<OpcViralLoadEntity> viralload) {
        return viralLoadRepository.saveAll(viralload);
    }
    
    /**
     * Tồn tại một bản ghi TLVR
     * 
     * @param stageID
     * @return 
     */
    public boolean existByStageID(Long stageID) {
        return viralLoadRepository.countByStageID(stageID);
    }
    
    public List<OpcViralLoadEntity> findByTestTimeAndStageID(String date, Long stageID) {
        List<OpcViralLoadEntity> list = viralLoadRepository.findByTestTimeAndStageID(date, stageID);
        return list == null || list.isEmpty() ? null : list;
    }
}
