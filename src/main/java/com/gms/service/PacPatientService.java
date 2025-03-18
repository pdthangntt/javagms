package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.MetaDataEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacLocationEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.PacPatientLogEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.ql.Local;
import com.gms.entity.input.EarlyHivSearch;
import com.gms.entity.input.LogSearch;
import com.gms.entity.input.PacAidsReportSearch;
import com.gms.entity.input.PacDeadSearch;
import com.gms.entity.input.PacHivDetectSearch;
import com.gms.entity.input.PacLocalSearch;
import com.gms.entity.input.PacOutProvinceFilter;
import com.gms.entity.input.PacOutProvinceSearch;
import com.gms.entity.input.PacPatientAcceptSearch;
import com.gms.entity.input.PacPatientDeadSearch;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.entity.input.PacPatientReviewSearch;
import com.gms.repository.DistrictRepository;
import com.gms.repository.MetaDataRepository;
import com.gms.repository.PacHivInfoRepository;
import com.gms.repository.PacLocationRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.gms.repository.PacPatientInfoRepository;
import com.gms.repository.PacPatientLogRepository;
import com.gms.repository.ParameterRepository;
import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 *
 * @author vvthanh
 */
@Service
public class PacPatientService extends BaseService {

    @Autowired
    private PacPatientInfoRepository patientInfoRepository;
    @Autowired
    private PacPatientLogRepository patientLogRepository;
    @Autowired
    private MetaDataRepository metaDataRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private PacHivInfoRepository hivInfoRepository;
    @Autowired
    private PacLocationRepository pacLocationRepository;
    @Autowired
    private ParameterRepository parameterRepository;

    /**
     * Tạo log hệ thống
     *
     * @param patientID
     * @param content
     * @return
     */
    public PacPatientLogEntity log(Long patientID, String content) {
        PacPatientLogEntity model = new PacPatientLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setTime(new Date());
        model.setPatientID(patientID);
        model.setContent(content);
        return patientLogRepository.save(model);
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

    /**
     * Save HIV info
     *
     * @auth vvThanh
     * @param model
     * @return
     * @throws Exception
     */
    public PacHivInfoEntity save(PacHivInfoEntity model) throws Exception {
        model.setUpdateAt(new Date());
        hivInfoRepository.save(model);
        return model;
    }

    public List<PacPatientInfoEntity> findViralLoads(int year, String provinceID) {
        List<PacPatientInfoEntity> items = patientInfoRepository.findViralLoads(year, provinceID);
        return items == null || items.isEmpty() ? null : items;
    }

    /**
     * Lưu bệnh nhân
     *
     * @param model
     * @return
     * @throws java.lang.Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public PacPatientInfoEntity save(PacPatientInfoEntity model) throws Exception {
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
                model.setRequestDeathTime(model.getDeathTime());
            }
            if (model.getDeathTime() == null) {
                model.setRequestDeathTime(null);
            }

            model = patientInfoRepository.save(model);

            //Update kết nối điều trị
            if (model.getParentID() != null && model.getSourceServiceID() != null && model.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) {
                PacPatientInfoEntity item = findOne(model.getParentID());
                if (item != null) {
                    PacPatientInfoEntity cl = (PacPatientInfoEntity) item.clone();
                    item.setStatusOfTreatmentID(model.getStatusOfTreatmentID());
                    item.setStartTreatmentTime(model.getStartTreatmentTime());
                    item.setSiteTreatmentFacilityID(model.getSiteTreatmentFacilityID());
                    item.setTreatmentRegimenID(model.getTreatmentRegimenID());
                    item.setCdFourResult(model.getCdFourResult());
                    item.setCdFourResultDate(model.getCdFourResultDate());
                    item.setCdFourResultCurrent(model.getCdFourResultCurrent());
                    item.setCdFourResultLastestDate(model.getCdFourResultLastestDate());
                    item.setVirusLoadArv(model.getVirusLoadArv());
                    item.setVirusLoadArvDate(model.getVirusLoadArvDate());
                    item.setVirusLoadArvCurrent(model.getVirusLoadArvCurrent());
                    item.setVirusLoadArvLastestDate(model.getVirusLoadArvLastestDate());
                    item.setClinicalStage(model.getClinicalStage());
                    item.setSymptomID(model.getSymptomID());
                    item.setClinicalStageDate(model.getClinicalStageDate());
                    item.setAidsStatus(model.getAidsStatus());
                    item.setStatusOfChangeTreatmentID(model.getStatusOfChangeTreatmentID());

                    item.setOpcCode(model.getOpcCode());
                    item.setDeathTime(item.getDeathTime() == null ? model.getDeathTime() : item.getDeathTime());
                    item.setRequestDeathTime(item.getRequestDeathTime() != null ? item.getRequestDeathTime() : model.getRequestDeathTime());
                    item.setChangeTreatmentDate(model.getChangeTreatmentDate());
                    item.setStatusOfChangeTreatmentID(model.getStatusOfChangeTreatmentID());

                    //Bổ sung thông tin điều trị
                    item.setRegistrationTime(model.getRegistrationTime());
                    item.setRegistrationType(model.getRegistrationType());
                    item.setFirstTreatmentTime(model.getFirstTreatmentTime());
                    item.setFirstTreatmentRegimenId(model.getFirstTreatmentRegimenId());
                    item.setEndTime(model.getEndTime());
                    item.setEndCase(model.getEndCase());

                    patientInfoRepository.save(item);
                    log(item.getID(), "Tự động cập nhật từ kết nối." + item.changeToString(cl, true));
                }
            }
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Chi tiết thông tin bệnh nhân
     *
     * @auth vvThành
     * @param ID
     * @return
     */
    public PacPatientInfoEntity findOne(Long ID) {
        Optional<PacPatientInfoEntity> optional = patientInfoRepository.findById(ID);
        PacPatientInfoEntity model = null;
        if (optional.isPresent()) {
            List<PacPatientInfoEntity> entitys = new ArrayList<>();
            entitys.add(optional.get());
            entitys = getAddress(entitys);
            entitys = getMetaData(entitys);
            model = entitys.get(0);
        }
        return model;
    }

    /**
     * Lấy full thông tin địa chỉ
     *
     * @param entities
     * @return
     */
    public List<PacPatientInfoEntity> getAddress(List<PacPatientInfoEntity> entities) {
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

        for (PacPatientInfoEntity entity : entities) {
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
     * Convert địa chỉ full
     *
     * @auth vvThành
     * @param no
     * @param street
     * @param group
     * @param province
     * @param district
     * @param ward
     * @return
     */
    public String buildAddress(String no, String street, String group, ProvinceEntity province, DistrictEntity district, WardEntity ward) {
        String address = StringUtils.isEmpty(no) ? "" : no;
        if (street != null && !"".equals(street)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + street;
        }
        if (group != null && !"".equals(group)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + group;
        }
        return buildAddress(address, province, district, ward);
    }

    /**
     * Lấy thông tin meta
     *
     * @param entities
     * @return
     */
    public List<PacPatientInfoEntity> getMetaData(List<PacPatientInfoEntity> entities) {
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
        for (PacPatientInfoEntity entity : entities) {
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
     * Lấy log theo ID bệnh nhân
     *
     * @auth vvThành
     * @param patientID
     * @return
     */
    public List<PacPatientLogEntity> getPatientLog(Long patientID) {
        return patientLogRepository.findByPatientID(patientID);
    }

    /**
     *
     * @page Phát hiện ca bênh:
     * @Tab Phát hiện trong tỉnh
     * @Tab Tỉnh khác phát hiện
     * @Tab Phát hiện ngoại tỉnh
     *
     * @auth pdThang
     *
     * @param tab
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findNew(PacPatientNewSearch search, String tab) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findNew(
                search.getFullname(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.isRemove(),
                search.getYearOfBirth(),
                search.getCurrentPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                search.getDetectProvinceID(),
                search.getProvinceID(),
                search.getPermanentProvince(),
                search.getDetectProvince(),
                search.getAcceptPermanentTime(),
                search.getSearchProvinceID(),
                search.getGenderID(),
                search.getSearchDetectProvinceID(),
                search.getPermanentProvinceID(),
                search.getIdentityCard(),
                search.getService(),
                search.getBloodBase(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    public DataPage<PacPatientInfoEntity> findVAACNew(PacPatientNewSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"createAT"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findVAACNew(
                search.getProvinceID(),
                search.getFullname(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.getYearOfBirth(),
                search.getPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                search.getGenderID(),
                search.getIdentityCard(),
                search.getService(),
                search.getBloodBase(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     *
     * @page Cục vaac Người nhiễm phát hiện
     *
     * @auth TrangBN
     *
     * @param tab
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findNewVAAC(PacPatientNewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findNewVAAC(
                search.getFullname(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.isRemove(),
                search.getYearOfBirth(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                search.getPermanentProvince(),
                search.getGenderID(),
                search.getPermanentProvinceID(),
                search.getIdentityCard(),
                search.getService(),
                search.getBloodBase(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Xoá vĩnh viễn ca bệnh
     *
     * @author vvThành
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(PacPatientInfoEntity entity) {
        try {
            Optional<PacHivInfoEntity> optional = hivInfoRepository.findById(entity.getID());
            if (optional.isPresent()) {
                hivInfoRepository.delete(optional.get());
            }
            patientInfoRepository.delete(entity);
            return true;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    /**
     * Danh sách log theo bệnh nhân
     *
     * @auth pdThang
     * @param oID
     * @return
     */
    public List<PacPatientLogEntity> getLogs(Long oID) {
        return patientLogRepository.findByPatientID(oID);
    }

    /**
     * Xoá tạm thời
     *
     * @param ID
     */
    public void delete(Long ID) {
        PacPatientInfoEntity e = findOne(ID);
        if (e != null) {
            e.setRemove(true);
            e.setUpdateAt(new Date());
            e.setRemoteAT(new Date());
        }
        patientInfoRepository.save(e);
    }

    /**
     * Xoá vĩnh viễn
     *
     * @param ID
     */
    public void remove(Long ID) {
        PacPatientInfoEntity entity = findOne(ID);
        PacLocationEntity locationEntity = findOneLocation(ID);
        if (locationEntity != null) {
            pacLocationRepository.deleteById(ID);
        }
        if (entity != null) {
            patientInfoRepository.delete(entity);
        }

    }

    public DataPage<PacPatientInfoEntity> findChange(PacPatientNewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"confirmTime"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findChange(
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                //                search.getSourceSiteID(),
                Integer.valueOf(search.getConnectStatus()),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                search.getHealthInsuranceNo(),
                search.getPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getStartTreatmentTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEndTreatmentTime()),
                search.getOpcStatus(),
                search.getAddressFilter(),
                search.getFacility(),
                search.getOpcCode(),
                search.getConfirmCode(),
                StringUtils.isEmpty(search.getStatus()) ? null : search.getStatus(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     *
     * @page Người nhiễm cần rà soát
     * @auth pdThang
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findReview(PacPatientReviewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        List<String> pIDs = new ArrayList<>();
        if (search.getPermanentProvinceID() != null && !"".equals(search.getPermanentProvinceID())) {
            pIDs.add(search.getPermanentProvinceID());
        }

        List<String> dIDs = new ArrayList<>();
        if (search.getPermanentDistrictID() != null && !"".equals(search.getPermanentDistrictID())) {
            dIDs.add(search.getPermanentDistrictID());
        }
        List<String> wIDs = new ArrayList<>();
        if (search.getPermanentWardID() != null && !"".equals(search.getPermanentWardID())) {
            wIDs.add(search.getPermanentWardID());
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findReview(
                search.getAddressFilter(),
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                pIDs.isEmpty() ? null : pIDs,
                dIDs.isEmpty() ? null : dIDs,
                wIDs.isEmpty() ? null : wIDs,
                search.getHasReview(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Trung ương yêu cầu rà soát
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findReviewVaac(PacPatientReviewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        List<String> pIDs = new ArrayList<>();
        if (search.getPermanentProvinceID() != null && !"".equals(search.getPermanentProvinceID())) {
            pIDs.add(search.getPermanentProvinceID());
        }

        List<String> dIDs = new ArrayList<>();
        if (search.getPermanentDistrictID() != null && !"".equals(search.getPermanentDistrictID())) {
            dIDs.add(search.getPermanentDistrictID());
        }
        List<String> wIDs = new ArrayList<>();
        if (search.getPermanentWardID() != null && !"".equals(search.getPermanentWardID())) {
            wIDs.add(search.getPermanentWardID());
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findReviewVaac(
                search.getAddressFilter(),
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                pIDs.isEmpty() ? null : pIDs,
                dIDs.isEmpty() ? null : dIDs,
                wIDs.isEmpty() ? null : wIDs,
                search.getHasReview(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * @page Người nhiễm đã rà soát
     * @auth pdThang
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findAccept(PacPatientAcceptSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        List<String> pIDs = new ArrayList<>();
        if (search.getPermanentProvinceID() != null && !"".equals(search.getPermanentProvinceID())) {
            pIDs.add(search.getPermanentProvinceID());
        }

        List<String> dIDs = new ArrayList<>();
        if (search.getPermanentDistrictID() != null && !"".equals(search.getPermanentDistrictID())) {
            dIDs.add(search.getPermanentDistrictID());
        }
        List<String> wIDs = new ArrayList<>();
        if (search.getPermanentWardID() != null && !"".equals(search.getPermanentWardID())) {
            wIDs.add(search.getPermanentWardID());
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findAccept(
                search.getAddressFilter(),
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                search.getHealthInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                pIDs.isEmpty() ? null : pIDs,
                dIDs.isEmpty() ? null : dIDs,
                wIDs.isEmpty() ? null : wIDs,
                search.getStatusOfResidentID(),
                search.getHasReview(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Tim kiếm thông tin theo HIV code
     *
     * @auth vvThành
     * @param code
     * @return
     */
    public PacHivInfoEntity findByHivCode(String code) {
        return hivInfoRepository.findByCode(code);
    }

    /**
     * Tim kiếm thông tin HIV ID
     *
     * @param ID
     * @auth vvThành
     * @return
     */
    public PacHivInfoEntity findOneHivInfo(Long ID) {
        Optional<PacHivInfoEntity> optional = hivInfoRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * @page Người nhiễm quản lý
     * @auth pdThang
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findPatient(PacPatientNewSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findPatient(
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                search.getHealthInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestDeathTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestDeathTimeTo()),
                search.getPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                search.getStatusOfResidentIDs(),
                search.getStatusOfTreatmentID(),
                search.getDead(),
                search.getEarlyHiv(),
                search.getHasRequest(),
                search.getHasReview(),
                search.getOther(),
                search.getAddressFilter(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getID(),
                search.getHivID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * @page Xuất Người nhiễm quản lý
     * @auth pdThang
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> exportPatient(PacPatientNewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findExportPatient(
                search.getTab(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getReviewProvinceTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getReviewProvinceTimeTo()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    public DataPage<PacPatientInfoEntity> findExportOutProvince(PacPatientNewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findExportOutProvince(
                search.getProvinceID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    public Long countExportPatient(PacPatientNewSearch search) {
        Long total = patientInfoRepository.countExportPatient(search.getTab(), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()), search.getProvinceID());
        return total == null ? 0 : total;
    }

    /**
     * Lấy danh sách người nhiễm theo danh sách ID
     *
     * @author DSNAnh
     * @param ids
     * @return
     */
    public List<PacPatientInfoEntity> findAllByIds(List<Long> ids) {
        List<PacPatientInfoEntity> models = patientInfoRepository.findAllById(ids);
        getMetaData(models);
        getAddress(models);
        return models;
    }

    /**
     * Lưu tất cả khi chọn nhiều
     *
     * @author vvthanh
     * @param entities
     * @return
     */
    public List<PacPatientInfoEntity> saveAll(List<PacPatientInfoEntity> entities) {
        Date currentDate = new Date();
        for (PacPatientInfoEntity model : entities) {
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
        }
        Iterable<PacPatientInfoEntity> iterable = patientInfoRepository.saveAll(entities);
        return ImmutableList.copyOf(iterable);
    }

    public List<PacPatientInfoEntity> findPatientConnectOPC(String provinceID, String cmnd) {
        return patientInfoRepository.findPatientConnectOPC(false, provinceID, cmnd);
    }

    /**
     * @return @auth pdThang
     * @param IDs
     */
    public List<PacHivInfoEntity> findHivInfoByIDs(Set<Long> IDs) {
        if (IDs == null || IDs.isEmpty()) {
            return null;
        }
        return hivInfoRepository.findByIDs(IDs);
    }

    public PacHivInfoEntity findHivInfoByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return hivInfoRepository.findByCode(code);
    }

    /**
     * Lấy dữ liệu để get toạ độ địa lý
     *
     * @auth vvThành
     * @param size
     * @return
     */
    public List<PacPatientInfoEntity> getForUpdateLatlng(int size) {
        List<PacPatientInfoEntity> items = patientInfoRepository.getForUpdateLatlng(PageRequest.of(0, size));
        if (items != null && !items.isEmpty()) {
            getMetaData(items);
            getAddress(items);
        }
        return items;
    }

    /**
     * Tìm kiếm trùng lắp dữ liệu
     *
     * @param ID
     * @param provinceID
     * @param districtID
     * @param fullname
     * @param genderID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param identityCard
     * @param healthInsuranceNo
     * @return
     */
    public List<PacPatientInfoEntity> duplicateFilter(Long ID, String provinceID, String fullname, String genderID,
            String permanentProvinceID, String permanentDistrictID,
            String permanentWardID, String identityCard, String healthInsuranceNo) {
        List<PacPatientInfoEntity> items = patientInfoRepository.duplicateFilter(ID, provinceID, fullname, genderID, permanentProvinceID, permanentDistrictID, permanentWardID, identityCard, healthInsuranceNo);
        getMetaData(items);
        getAddress(items);
        return items;
    }

    public List<PacPatientInfoEntity> duplicateFilterTTYT(Long ID, String provinceID, String districtID, String fullname, String genderID,
            String permanentProvinceID, String permanentDistrictID,
            String permanentWardID, String identityCard, String healthInsuranceNo) {
        List<PacPatientInfoEntity> items = patientInfoRepository.duplicateFilterTTYT(ID, provinceID, districtID.isEmpty() ? null : districtID, fullname, genderID, permanentProvinceID, permanentDistrictID, permanentWardID, identityCard, healthInsuranceNo);
        getMetaData(items);
        getAddress(items);
        return items;
    }

    /**
     * DSNAnh Xuất danh sách người nhiễm phát hiện
     *
     * @param search
     * @return list PacPatientInfoEntity
     */
    public DataPage<PacPatientInfoEntity> findNewExport(PacPatientNewSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Page<PacPatientInfoEntity> pages = patientInfoRepository.findNewExport(
                search.getAddressFilter(),
                search.getPatientStatus().contains(",") ? "" : search.getPatientStatus(),
                search.getDateFilter(),
                StringUtils.isEmpty(search.getAgeFrom()) ? null : Integer.parseInt(search.getAgeFrom()),
                StringUtils.isEmpty(search.getAgeTo()) ? null : Integer.parseInt(search.getAgeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.isRemove(),
                search.getPermanentDistrictID() == null || "null".equals(search.getPermanentDistrictID()) || "".equals(search.getPermanentDistrictID()) ? null : Arrays.asList(search.getPermanentDistrictID().split(",")),
                search.getPermanentWardID() == null || "null".equals(search.getPermanentWardID()) || "".equals(search.getPermanentWardID()) ? null : Arrays.asList(search.getPermanentWardID().split(",")),
                search.getGenderID() == null || "null".equals(search.getGenderID()) ? null : Arrays.asList(search.getGenderID().split(",")),
                search.getPermanentProvinceID(),
                search.getStatusOfResidentID() == null || "null".equals(search.getStatusOfResidentID()) ? null : Arrays.asList(search.getStatusOfResidentID().split(",")),
                search.getStatusOfTreatmentID() == null || "null".equals(search.getStatusOfTreatmentID()) ? null : Arrays.asList(search.getStatusOfTreatmentID().split(",")),
                search.getRaceID() == null || "null".equals(search.getRaceID()) ? null : Arrays.asList(search.getRaceID().split(",")),
                search.getModeOfTransmissionID() == null || "null".equals(search.getModeOfTransmissionID()) ? null : Arrays.asList(search.getModeOfTransmissionID().split(",")),
                search.getObjectGroupID() == null || "null".equals(search.getObjectGroupID()) ? null : Arrays.asList(search.getObjectGroupID().split(",")),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * DSNAnh DS người nhiễm tử vong
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findDead(PacPatientDeadSearch search) {

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"deathTime"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Long code = Long.valueOf("0");
        try {
            code = Long.valueOf(StringUtils.isEmpty(search.getCode()) ? "0" : search.getCode());
        } catch (Exception e) {
            code = Long.valueOf("-999");
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findDead(
                code,
                search.getName(),
                search.getBlood(),
                search.getAddressFilter(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestDeathTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestDeathTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeTo()),
                search.getpIDs().isEmpty() ? null : search.getpIDs(),
                search.getdIDs().isEmpty() ? null : search.getdIDs(),
                search.getwIDs().isEmpty() ? null : search.getwIDs(),
                search.getGenderIDs().isEmpty() ? null : search.getGenderIDs(),
                search.getRaceIDs().isEmpty() ? null : search.getRaceIDs(),
                search.getTransmisionIDs().isEmpty() ? null : search.getTransmisionIDs(),
                search.getTestObjectIDs().isEmpty() ? null : search.getTestObjectIDs(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getAgeFrom() == null || "".equals(search.getAgeFrom()) ? null : Integer.parseInt(search.getAgeFrom()),
                search.getAgeTo() == null || "".equals(search.getAgeTo()) ? null : Integer.parseInt(search.getAgeTo()),
                search.getManageStatus(),
                search.isIsVAAC(),
                search.isIsTTYT(),
                search.isIsTYT(),
                search.getPlaceTest(),
                search.getFacility(),
                pageable
        );

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * pdThang Danh sach nguoi nhiem phat hien
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findNewExports(PacPatientNewSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"deathTime"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        if (!StringUtils.isEmpty(search.getSortName())) {
            sortable = Sort.by(!StringUtils.isEmpty(search.getSortType()) && search.getSortType().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, new String[]{search.getSortName()});
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Long code = Long.valueOf("0");
        try {
            code = Long.valueOf(StringUtils.isEmpty(search.getCode()) ? "0" : search.getCode());
        } catch (Exception e) {
            code = Long.valueOf("-999");
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findNewExports(
                code,
                search.getName(),
                search.getBlood(),
                search.getAddressFilter(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAidsFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAidsTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeTo()),
                search.getpIDs().isEmpty() ? null : search.getpIDs(),
                search.getdIDs().isEmpty() ? null : search.getdIDs(),
                search.getwIDs().isEmpty() ? null : search.getwIDs(),
                search.getGenderIDs().isEmpty() ? null : search.getGenderIDs(),
                search.getRaceIDs().isEmpty() ? null : search.getRaceIDs(),
                search.getTransmisionIDs().isEmpty() ? null : search.getTransmisionIDs(),
                search.getTestObjectIDs().isEmpty() ? null : search.getTestObjectIDs(),
                search.getPatientStatus(),
                search.getResidentIDs().isEmpty() ? null : search.getResidentIDs(),
                search.getTreamentIDs().isEmpty() ? null : search.getTreamentIDs(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getAgeFrom() == null || "".equals(search.getAgeFrom()) ? null : Integer.parseInt(search.getAgeFrom()),
                search.getAgeTo() == null || "".equals(search.getAgeTo()) ? null : Integer.parseInt(search.getAgeTo()),
                search.getCurrentAgeFrom() == null || "".equals(search.getCurrentAgeFrom()) ? null : Integer.parseInt(search.getCurrentAgeFrom()),
                search.getCurrentAgeTo() == null || "".equals(search.getCurrentAgeTo()) ? null : Integer.parseInt(search.getCurrentAgeTo()),
                search.getManageStatus(),
                search.isIsVAAC(),
                search.isIsPAC(),
                search.getPlaceTest(),
                search.getFacility(),
                search.getStatusTreatmentRule(),
                pageable
        );

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * pdThang
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientLogEntity> findAllLog(LogSearch search) {
        DataPage<PacPatientLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"patientID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientLogEntity> pages = patientLogRepository.findAll(search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * Ngày đầu tiên có dữ liệu theo ngày xét nghiệm
     *
     * pdThang
     *
     * @param provinceID
     * @return
     */
    public PacPatientInfoEntity getFirst(String provinceID) {
        Page<PacPatientInfoEntity> page = patientInfoRepository.getFirst(provinceID, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    /**
     * Ngày đầu tiên có dữ liệu theo ngày xét nghiệm VAAC
     *
     * TrangBN
     *
     * @return
     */
    public PacPatientInfoEntity getFirst() {
        Page<PacPatientInfoEntity> page = patientInfoRepository.getFirst(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    public PacPatientInfoEntity getFirstAids() {
        Page<PacPatientInfoEntity> page = patientInfoRepository.getFirstAids(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"aidsStatusDate"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    /**
     * Tìm người nhiễm đầu tiên của Tỉnh/ Huyện/ Xã
     *
     * @DSNAnh
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     */
    public PacPatientInfoEntity getFirst(String provinceID, String districtID, String wardID) {
        Page<PacPatientInfoEntity> page = patientInfoRepository.getFirst(provinceID, districtID, wardID, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    public PacPatientInfoEntity getFirstDead(boolean isTTYT, boolean isTYT, String manageStatus, String addressFilter, String provinceID, String districtID, String wardID, List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID, String deathTimeFrom, String deathTimeTo, String aidsFrom, String aidsTo, String requestDeathTimeFrom, String requestDeathTimeTo, String updateTimeFrom, String updateTimeTo, String ageFrom, String ageTo, String job, String gender, String testObject, String statusResident, String statusTreatment) {
        Page<PacPatientInfoEntity> page = patientInfoRepository.getFirstDead(isTTYT, isTYT, manageStatus, addressFilter,
                provinceID, districtID, wardID,
                permanentProvinceID.isEmpty() ? null : permanentProvinceID,
                permanentDistrictID.isEmpty() ? null : permanentDistrictID,
                permanentWardID.isEmpty() ? null : permanentWardID, deathTimeFrom, deathTimeTo, aidsFrom, aidsTo, requestDeathTimeFrom, requestDeathTimeTo, updateTimeFrom, updateTimeTo,
                "".equals(ageFrom) ? null : Integer.parseInt(ageFrom), "".equals(ageTo) ? null : Integer.parseInt(ageTo),
                job, gender, testObject, statusResident, statusTreatment, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"deathTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author pdthang
     * @param searchInput
     * @return
     * @throws java.text.ParseException
     */
    public List<PacPatientInfoEntity> findPacDeadHivs(PacDeadSearch searchInput) throws ParseException {
        List<PacPatientInfoEntity> dataPage;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fromTimeConvert = StringUtils.isEmpty(searchInput.getFromTime()) ? null : sdf.parse(searchInput.getFromTime());
        Date endTimeConvert = StringUtils.isEmpty(searchInput.getToTime()) ? null : sdf.parse(searchInput.getToTime());

        dataPage = patientInfoRepository.findPatientDeadHiv(fromTimeConvert, endTimeConvert, searchInput.getBlood(), searchInput.getTestObject(),
                searchInput.getGenderID(), getCurrentUser().getSite().getProvinceID(), searchInput.getPermanentDistrictID(),
                searchInput.getPermanentWardID(), searchInput.getStatusTreatment(),
                searchInput.getStatusResident(), searchInput.getYearOld(), searchInput.getJob());
        return dataPage;
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author TrangBN
     * @param searchInput
     * @return
     */
    public List<PacPatientInfoEntity> findPacEarlyHiv(EarlyHivSearch searchInput) throws ParseException {
        List<PacPatientInfoEntity> dataPage;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fromTimeConvert = StringUtils.isEmpty(searchInput.getFromTime()) ? null : sdf.parse(searchInput.getFromTime());
        Date endTimeConvert = StringUtils.isEmpty(searchInput.getToTime()) ? null : sdf.parse(searchInput.getToTime());

        dataPage = patientInfoRepository.findEarlyHivPatient(fromTimeConvert, endTimeConvert, searchInput.getBlood(), searchInput.getTestObject(), searchInput.getGenderID(), getCurrentUser().getSite().getProvinceID(), searchInput.getPermanentDistrictID(), searchInput.getPermanentWardID(), searchInput.getStatusTreatment(), searchInput.getStatusResident(), searchInput.getYearOld(), searchInput.getJob());
        return dataPage;
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author pdThang
     * @param searchInput
     * @return
     * @throws java.text.ParseException
     */
    public List<PacPatientInfoEntity> findPacLocals(PacLocalSearch searchInput) throws ParseException {
        List<PacPatientInfoEntity> dataPage;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fromTime = StringUtils.isEmpty(searchInput.getFromTime()) ? null : sdf.parse(searchInput.getFromTime());
        Date toTime = StringUtils.isEmpty(searchInput.getToTime()) ? null : sdf.parse(searchInput.getToTime());

        dataPage = patientInfoRepository.findLocalPatient(fromTime, toTime, getCurrentUser().getSite().getProvinceID(),
                searchInput.getPermanentDistrictID(),
                searchInput.getPermanentWardID(), searchInput.getAlive());
        return dataPage;
    }

    /**
     * Save all toạ độ địa lý
     *
     * @auth vvthành
     * @param latlng
     */
    public void saveAllLocation(List<PacLocationEntity> latlng) {
        pacLocationRepository.saveAll(latlng);
    }

    /**
     * Lưu local địa chỉ latlng
     *
     * @auth vvThành
     * @param location
     */
    public void save(PacLocationEntity location) {
        pacLocationRepository.save(location);
    }

    /**
     * @auth vvthanh
     * @param ID
     * @return
     */
    public PacLocationEntity findOneLocation(Long ID) {
        Optional<PacLocationEntity> optional = pacLocationRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public List<PacPatientInfoEntity> findPacDetectHivObject(String fromDate, String toDate, String testObject, Set<String> testObjects, String statusTreatment, String statusResident, String job, String addressFilter, String dateFilter, String patientStatus, String provinceID, String otherProvinceID, String districtID, String wardID, String flag) {
        List<PacPatientInfoEntity> models = patientInfoRepository.findPacDetectHivObject(fromDate, toDate, testObject, testObjects, statusTreatment, statusResident, job, addressFilter, dateFilter, patientStatus, provinceID, otherProvinceID, districtID, wardID, flag);
        getAddress(models);
        getMetaData(models);
        return models;
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author TrangBN
     * @param searchInput
     * @return
     */
    public List<PacPatientInfoEntity> findDetectHivGender(PacHivDetectSearch searchInput) throws ParseException {
        List<PacPatientInfoEntity> dataPage;

        String fromTimeConvert = StringUtils.isEmpty(searchInput.getFromTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getFromTime());
        String toTimeConvert = StringUtils.isEmpty(searchInput.getToTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getToTime());

        dataPage = patientInfoRepository.findDetectHivGender(
                fromTimeConvert,
                toTimeConvert,
                searchInput.getAddressType(),
                searchInput.getStatusAlive().contains(",") ? StringUtils.EMPTY : searchInput.getStatusAlive(),
                searchInput.getSearchTime(),
                searchInput.getProvinceID(),
                searchInput.getDistrictID(),
                searchInput.getWardID(),
                searchInput.getJob(),
                searchInput.getTestObject(),
                searchInput.getStatusTreatment(),
                searchInput.getStatusResident(),
                searchInput.getGender(),
                searchInput.getOtherProvinceID()
        );
        return dataPage;
    }

    public List<PacPatientInfoEntity> findPacDetectHivTransmission(String fromDate, String toDate, Set<String> modeOfTransmisson, String statusTreatment, String statusResident, String job, String testObject, String addressFilter, String dateFilter, String patientStatus, String provinceID, String otherProvinceID, String districtID, String wardID, String flag) {
        List<PacPatientInfoEntity> models = patientInfoRepository.findPacDetectHivTransmisson(fromDate, toDate, modeOfTransmisson, statusTreatment, statusResident, job, testObject, addressFilter, dateFilter, patientStatus, provinceID, otherProvinceID, districtID, wardID, flag);
        getAddress(models);
        getMetaData(models);
        return models;
    }

    public List<PacPatientInfoEntity> findPacDetectHivResident(String fromDate, String toDate, String statusTreatment, String statusResident, String job, String testObject, String addressFilter, String dateFilter, String patientStatus, String provinceID, String otherProvinceID, String districtID, String wardID, String flag, List<Long> siteLocals) {
        List<PacPatientInfoEntity> models = patientInfoRepository.findPacDetectHivResident(fromDate, toDate, statusTreatment, statusResident, job, testObject, addressFilter, dateFilter, patientStatus, provinceID, otherProvinceID, districtID, wardID, flag, siteLocals);
        getAddress(models);
        getMetaData(models);
        return models;
    }

    /**
     * Lấy thông tin click detail người nhiễm phát hiện theo nhóm tuổi
     *
     * @param searchInput
     * @return
     * @throws ParseException
     */
    public List<PacPatientInfoEntity> findDetectHivAge(PacHivDetectSearch searchInput) throws ParseException {
        List<PacPatientInfoEntity> dataPage;

        String fromTimeConvert = StringUtils.isEmpty(searchInput.getFromTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getFromTime());
        String toTimeConvert = StringUtils.isEmpty(searchInput.getToTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getToTime());

        Integer ageFrom = 0;
        Integer ageTo = 0;

        if ("<15".equals(searchInput.getAge())) {
            ageFrom = 0;
            ageTo = 14;
        } else if (">49".equals(searchInput.getAge())) {
            ageFrom = 50;
            ageTo = 99;
        } else {
            String[] split = searchInput.getAge().split("-", -1);
            try {
                ageFrom = Integer.parseInt(split[0]);
                ageTo = Integer.parseInt(split[1]);
            } catch (NumberFormatException e) {
                ageFrom = 0;
                ageTo = 0;
            }
        }

        if (StringUtils.isEmpty(searchInput.getAge())) {
            ageFrom = null;
        }

        dataPage = patientInfoRepository.findDetectHivAge(
                fromTimeConvert,
                toTimeConvert,
                searchInput.getAddressType(),
                searchInput.getStatusAlive().contains(",") ? StringUtils.EMPTY : searchInput.getStatusAlive(),
                searchInput.getSearchTime(),
                searchInput.getProvinceID(),
                searchInput.getDistrictID(),
                searchInput.getWardID(),
                searchInput.getJob(),
                searchInput.getTestObject(),
                searchInput.getStatusTreatment(),
                searchInput.getStatusResident(),
                searchInput.getOtherProvinceID(),
                ageFrom,
                ageTo
        );
        return dataPage;
    }

    /**
     * @page Sổ A10/YTCS
     * @auth DSNAnh
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findPatientA10(PacPatientNewSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Page<PacPatientInfoEntity> pages = patientInfoRepository.findPatientA10(
                search.isRemove(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getFullname(),
                search.getYearOfBirth(),
                search.getGenderID(),
                search.getIdentityCard(),
                search.getHealthInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.getObjectGroupID(),
                pageable);
        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Lấy theo sourceID
     *
     * @param sourceID
     * @param sourceService
     * @return
     */
    public List<PacPatientInfoEntity> findBySourceIDSourceService(Long sourceID, String sourceService) {
        return patientInfoRepository.findBySourceIDSourceService(sourceID, sourceService);
    }

    /**
     *
     * @param siteID
     * @param sourceID
     * @param sourceService
     * @return
     */
    public List<PacPatientInfoEntity> findByTransfer(Long siteID, Long sourceID, String sourceService) {
        return patientInfoRepository.findByTransfer(siteID, sourceID, sourceService);
    }

    public List<PacPatientInfoEntity> findByConfirm(String confirmCode, String identityCard, String province) {
        return patientInfoRepository.findByConfirm(confirmCode, identityCard, province);
    }

    public List<PacPatientInfoEntity> findByConfirms(String confirmCode, String province) {
        return patientInfoRepository.findByConfirms(confirmCode, province);
    }

    /**
     * DSNAnh DS bệnh nhân AIDS
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findAidsReport(PacAidsReportSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"aidsStatusDate"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Long code = Long.valueOf("0");
        try {
            code = Long.valueOf(StringUtils.isEmpty(search.getCode()) ? "0" : search.getCode());
        } catch (Exception e) {
            code = Long.valueOf("-999");
        }

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findAidsReport(
                code,
                search.getName(),
                search.getBlood(),
                search.getAddressFilter(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getUpdateTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAidsFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAidsTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInputTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDeathTimeTo()),
                search.getpIDs().isEmpty() ? null : search.getpIDs(),
                search.getdIDs().isEmpty() ? null : search.getdIDs(),
                search.getwIDs().isEmpty() ? null : search.getwIDs(),
                search.getGenderIDs().isEmpty() ? null : search.getGenderIDs(),
                search.getRaceIDs().isEmpty() ? null : search.getRaceIDs(),
                search.getTransmisionIDs().isEmpty() ? null : search.getTransmisionIDs(),
                search.getTestObjectIDs().isEmpty() ? null : search.getTestObjectIDs(),
                search.getProvinceID(),
                search.getDistrictID(),
                search.getWardID(),
                search.getAgeFrom() == null || "".equals(search.getAgeFrom()) ? null : Integer.parseInt(search.getAgeFrom()),
                search.getAgeTo() == null || "".equals(search.getAgeTo()) ? null : Integer.parseInt(search.getAgeTo()),
                search.getManageStatus(),
                search.isIsVAAC(),
                search.isIsTTYT(),
                search.isIsTYT(),
                search.getPlaceTest(),
                search.getFacility(),
                pageable
        );

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Danh sách ngoại tỉnh
     *
     * @param search
     * @return
     */
    public DataPage<PacPatientInfoEntity> findOutProvince(PacOutProvinceSearch search) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PacPatientInfoEntity> pages = patientInfoRepository.findOutProvince(
                //                search.isRemove(),
                search.getTab(),
                search.getProvinceID(),
                search.getPermanentProvinceID(),
                search.getCurrentProvinceID(),
                search.getDetectProvinceID(),
                search.getFullname(),
                Integer.valueOf(StringUtils.isEmpty(search.getYearOfBirth()) ? "0" : search.getYearOfBirth()),
                search.getGenderID(),
                search.getIdentityCard(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                search.getBloodBaseID(),
                search.getSourceServiceID(),
                search.getSiteConfirmID(),
                search.getSiteTreatmentFacilityID(),
                search.getStatus(),
                search.getStatusPatient() == null ? "" : search.getStatusPatient(),
                search.getStatusManage() == null ? "" : search.getStatusManage(),
                search.getTestObject() == null ? "" : search.getTestObject(),
                search.getTransmision() == null ? "" : search.getTransmision(),
                search.getStatusTreatment() == null ? "" : search.getStatusTreatment(),
                search.getStatusResident() == null ? "" : search.getStatusResident(),
                search.getHivID(),
                pageable
        );
        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Tổng số ca Phát hiện trong tỉnh
     *
     * @param provinceID
     * @return
     */
    public Long countInProvince(String provinceID) {
        return patientInfoRepository.countInProvince(provinceID);
    }

    public Long countPacOutProvince(String tab) {
        return patientInfoRepository.countPacOutProvince(tab);
    }

    public Long countVAACNew(String provinceID) {
        return patientInfoRepository.countVAACNew(provinceID);
    }

    /**
     * Tổng số ca Tỉnh khác phát hiện
     *
     * @param provinceID
     * @return
     */
    public Long countOtherProvince(String provinceID) {
        return patientInfoRepository.countOtherProvince(provinceID);
    }

    /**
     * Tổng số ca Phát hiện ngoại tỉnh
     *
     * @param provinceID
     * @return
     */
    public Long countOutProvince(String provinceID) {
        return patientInfoRepository.countOutProvince(provinceID);
    }

    /**
     * Tìm kiếm trùng lắp ngoại tỉnh
     *
     * @param model
     * @param filter
     * @return
     */
    public DataPage<PacPatientInfoEntity> duplicateFilter(PacPatientInfoEntity model, PacOutProvinceFilter filter) {
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(filter.getPageIndex());
        dataPage.setMaxResult(filter.getPageSize());
        Pageable pageable = PageRequest.of(filter.getPageIndex() - 1, filter.getPageSize());

        Page<PacPatientInfoEntity> pages = patientInfoRepository.duplicateFilterOutprovince(
                model.getID(),
                model.getRefID(),
                filter.isFullname() ? (model.getFullname() == null || "".equals(model.getFullname()) ? "-1" : model.getFullname()) : null,
                filter.isGender() ? (model.getGenderID() == null || "".equals(model.getGenderID()) ? "-1" : model.getGenderID()) : null,
                filter.isAddress() ? (model.getPermanentProvinceID() == null || "".equals(model.getPermanentProvinceID()) ? "-1" : model.getPermanentProvinceID()) : null,
                filter.isAddress() ? (model.getPermanentDistrictID() == null || "".equals(model.getPermanentDistrictID()) ? "-1" : model.getPermanentDistrictID()) : null,
                filter.isAddress() ? (model.getPermanentWardID() == null || "".equals(model.getPermanentWardID()) ? "-1" : model.getPermanentWardID()) : null,
                filter.isIdentity() ? (model.getIdentityCard() == null || "".equals(model.getIdentityCard()) ? "-1" : model.getIdentityCard()) : null,
                filter.isInsurance() ? (model.getHealthInsuranceNo() == null || "".equals(model.getHealthInsuranceNo()) ? "-1" : model.getHealthInsuranceNo()) : null,
                filter.isPhone() ? (model.getPatientPhone() == null || "".equals(model.getPatientPhone()) ? "-1" : model.getPatientPhone()) : null,
                pageable);
        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        getMetaData(dataPage.getData());
        getAddress(dataPage.getData());
        return dataPage;
    }

    /**
     * Find all record
     *
     * @return list
     */
    public List<PacHivInfoEntity> findAll() {
        return (List<PacHivInfoEntity>) hivInfoRepository.findAll();
    }

    /**
     * Find all record
     *
     * @return list
     */
    public List<PacPatientInfoEntity> findAllPatient() {
        return (List<PacPatientInfoEntity>) patientInfoRepository.findAll();
    }

    /**
     * Find all record by ProvinceId
     *
     * @param provinceID
     * @return list
     */
    public List<PacPatientInfoEntity> findAllByProvinceId(String provinceID) {
        return patientInfoRepository.findAllByProvinceId(provinceID);
    }

    public boolean existCode(Long ID, String code) {
        if (ID == null || StringUtils.isEmpty(code)) {
            return false;
        }
        return hivInfoRepository.countByCode(ID, code.trim()) > 0;
    }

    public List<Local> findProvinceNhiemMoi(String from, String to) {
        return patientInfoRepository.findProvinceNhiemMoi(from, to);
    }

    public int countProvinceNhiemMoiThuongTru(String provinceID, String districtID, String wardID, String from, String to) {
        return patientInfoRepository.countProvinceNhiemMoiThuongTru(provinceID, districtID, wardID, from, to);
    }

//    public int countProvinceNhiemMoiTamtru(String provinceID, String districtID, String from, String to) {
//        return patientInfoRepository.countProvinceNhiemMoiTamtru(provinceID, districtID, from, to);
//    }
    public List<Local> findProvincePhathien(String from, String to) {
        return patientInfoRepository.findProvincePhathien(from, to);
    }

    public int countProvincePhatHienThuongTru(String provinceID, String districtID, String wardID, String from, String to) {
        return patientInfoRepository.countProvincePhatHienThuongTru(provinceID, districtID, wardID, from, to);
    }

    public int countProvincePhatHienConSong(String provinceID, String to) {
        return patientInfoRepository.countProvincePhatHienConSong(provinceID, to);
    }

//    public int countProvincePhatHienTamtru(String provinceID, String districtID, String from, String to) {
//        return patientInfoRepository.countProvincePhatHienTamtru(provinceID, districtID, from, to);
//    }
    public List<Local> findProvinceTuVong(String from, String to) {
        return patientInfoRepository.findProvinceTuVong(from, to);
    }

    public int countProvinceTuVongThuongTru(String provinceID, String districtID, String wardID, String from, String to) {
        return patientInfoRepository.countProvinceTuVongThuongTru(provinceID, districtID, wardID, from, to);
    }

    public List<Local> findProvinceMoiNhiem(String from, String to) {
        return patientInfoRepository.findProvinceMoiNhiem(from, to);
    }

    public int countProvinceMoiNhiemThuongTru(String provinceID, String districtID, String wardID, String from, String to) {
        return patientInfoRepository.countProvinceMoiNhiemThuongTru(provinceID, districtID, wardID, from, to);
    }
}
