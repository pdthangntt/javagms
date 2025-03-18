package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.MetaDataEntity;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.repository.DistrictRepository;
import com.gms.repository.MetaDataRepository;
import com.gms.repository.PacPatientHistoryRepository;
import com.gms.repository.ParameterRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author pdthang
 */
@Service
public class PacPatientHistoryService extends BaseService {

    @Autowired
    private PacPatientHistoryRepository historyRepository;
    @Autowired
    private MetaDataRepository metaDataRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private ParameterRepository parameterRepository;

    public List<PacPatientHistoryEntity> findByParentID(Long parentID) {
        List<PacPatientHistoryEntity> models = historyRepository.findByPatientID(parentID);
        if (models == null || models.isEmpty()) {
            return null;
        }
        getMetaData(models);
        getAddress(models);
        return models;
    }
    
    /**
     * Tìm theo mã người nhiễm phát hiện ở tỉnh
     * 
     * @param id
     * @return 
     */
    public List<PacPatientHistoryEntity> findByDetectRefID(Long id) {
        List<PacPatientHistoryEntity> models = historyRepository.findByDetectRefID(id);
        if (models == null || models.isEmpty()) {
            return null;
        }
        getMetaData(models);
        getAddress(models);
        return models;
    }

    
    
    /**
     * Lấy thông tin meta
     *
     * @param entities
     * @return
     */
    public List<PacPatientHistoryEntity> getMetaData(List<PacPatientHistoryEntity> entities) {
        if (entities == null) {
            return entities;
        }
        Set<String> metaIDs = new HashSet<>();
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaName")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaIdentityCard")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaHealthInsuranceNo")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaPermanentAddressNo")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaPermanentAddressGroup")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaPermanentAddressStreet")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaCurrentAddressGroup")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaCurrentAddressStreet")));
        metaIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getMetaCurrentAddressNo")));
        //Lấy thông tin từ db
        Map<String, String> hMetas = new HashMap<>();
        List<MetaDataEntity> metas = IteratorUtils.toList(metaDataRepository.findAllById(metaIDs).iterator());
        for (MetaDataEntity model : metas) {
            hMetas.put(model.getID(), model.getContent());
        }
        for (PacPatientHistoryEntity entity : entities) {
            entity.setFullname(hMetas.getOrDefault(entity.getMetaName(), null));
            entity.setIdentityCard(hMetas.getOrDefault(entity.getMetaIdentityCard(), null));
            entity.setHealthInsuranceNo(hMetas.getOrDefault(entity.getMetaHealthInsuranceNo(), null));

            entity.setPermanentAddressGroup(hMetas.getOrDefault(entity.getMetaPermanentAddressGroup(), null));
            entity.setPermanentAddressStreet(hMetas.getOrDefault(entity.getMetaPermanentAddressStreet(), null));
            entity.setPermanentAddressNo(hMetas.getOrDefault(entity.getMetaPermanentAddressNo(), null));

            entity.setCurrentAddressGroup(hMetas.getOrDefault(entity.getMetaCurrentAddressGroup(), null));
            entity.setCurrentAddressStreet(hMetas.getOrDefault(entity.getMetaCurrentAddressStreet(), null));
            entity.setCurrentAddressNo(hMetas.getOrDefault(entity.getMetaCurrentAddressNo(), null));
        }
        return entities;
    }

    /**
     * Lấy full thông tin địa chỉ
     *
     * @param entities
     * @return
     */
    public List<PacPatientHistoryEntity> getAddress(List<PacPatientHistoryEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<String> pIDs = new HashSet<>();
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentProvinceID")));
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentProvinceID")));
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        for (ProvinceEntity model : provinceRepository.findByIDs(pIDs)) {
            provinces.put(model.getID(), model);
        }

        Set<String> dIDs = new HashSet<>();
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentDistrictID")));
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentDistrictID")));
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity model : districtRepository.findByIDs(dIDs)) {
            districts.put(model.getID(), model);
        }

        Set<String> wIDs = new HashSet<>();
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentWardID")));
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentWardID")));
        Map<String, WardEntity> wards = new HashMap<>();
        for (WardEntity model : wardRepository.findByIDs(wIDs)) {
            wards.put(model.getID(), model);
        }

        for (PacPatientHistoryEntity entity : entities) {
            //Địa chỉ thường trú
            entity.setPermanentAddressFull(buildAddress(
                    entity.getPermanentAddressNo(),
                    entity.getPermanentAddressGroup(),
                    entity.getPermanentAddressStreet(),
                    provinces.get(entity.getPermanentProvinceID()),
                    districts.get(entity.getPermanentDistrictID()),
                    wards.get(entity.getPermanentWardID())
            ));
            //Địa chỉ hiện tại
            entity.setCurrentAddressFull(buildAddress(
                    entity.getCurrentAddressNo(),
                    entity.getCurrentAddressGroup(),
                    entity.getCurrentAddressStreet(),
                    provinces.get(entity.getCurrentProvinceID()),
                    districts.get(entity.getCurrentDistrictID()),
                    wards.get(entity.getCurrentWardID())
            ));
        }

        return entities;
    }

    /**
     * Lưu thông tin lích sử
     * 
     * @param model
     * @return
     * @throws Exception 
     */
    @Transactional(rollbackFor = Exception.class)
    public PacPatientHistoryEntity save(PacPatientHistoryEntity model) throws Exception {
        model.setParentID(model.getParentID() == null ? Long.valueOf("0") : model.getParentID());
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            //tạo key metadata name
            MetaDataEntity meta = createKey(TextUtils.toFullname(model.getFullname()));
            model.setMetaName(meta == null ? null : meta.getID());
            meta = createKey(model.getIdentityCard());
            model.setMetaIdentityCard(meta == null ? null : meta.getID());
            meta = createKey(model.getHealthInsuranceNo());
            model.setMetaHealthInsuranceNo(meta == null ? null : meta.getID());

            meta = createKey(model.getPermanentAddressGroup());
            model.setMetaPermanentAddressGroup(meta == null ? null : meta.getID());
            meta = createKey(model.getPermanentAddressNo());
            model.setMetaPermanentAddressNo(meta == null ? null : meta.getID());
            meta = createKey(model.getPermanentAddressStreet());
            model.setMetaPermanentAddressStreet(meta == null ? null : meta.getID());

            meta = createKey(model.getCurrentAddressGroup());
            model.setMetaCurrentAddressGroup(meta == null ? null : meta.getID());
            meta = createKey(model.getCurrentAddressNo());
            model.setMetaCurrentAddressNo(meta == null ? null : meta.getID());
            meta = createKey(model.getCurrentAddressStreet());
            model.setMetaCurrentAddressStreet(meta == null ? null : meta.getID());

            model.setLatlngTime(null);

            //Lấy mã cơ sở điều trị
            if (model.getSiteTreatmentFacilityID() != null) {
                ParameterEntity param = parameterRepository.findByTypeAndCode(ParameterEntity.TREATMENT_FACILITY, model.getSiteTreatmentFacilityID());
                if (param != null && param.getSiteID() != null && !param.getSiteID().equals(Long.valueOf("0"))) {
                    model.setSiteIDTF(param.getSiteID());
                }
            }

            if (model.getDeathTime() != null && model.getRequestDeathTime() == null) {
                model.setRequestDeathTime(currentDate);
            } else if (model.getDeathTime() == null) {
                model.setRequestDeathTime(null);
            }

            model = historyRepository.save(model);

            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }
    
    /**
     * Tạo key
     *
     * @param name
     * @return
     */
    public MetaDataEntity createKey(String name) {
        if (name == null || "".equals(name)) {
            return null;
        }
        name = name.trim().replaceAll(" +", " ");
        MetaDataEntity model = new MetaDataEntity();
        model.setContent(name.trim());
        model.setSearch(TextUtils.removeDiacritical(name).toLowerCase().replaceAll(" ", "-"));
        model.setID(DigestUtils.md5Hex(name).toLowerCase());
        model.setTime(new Date());
        return metaDataRepository.save(model);
    }
}
