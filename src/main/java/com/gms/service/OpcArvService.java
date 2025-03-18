package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InitCodeEnum;
import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.input.LogSearch;
import com.gms.entity.input.OpcArvBookSearch;
import com.gms.entity.input.OpcArvSearch;
import com.gms.entity.input.OpcReceiveSearch;
import com.gms.entity.input.OpcRevisionSearch;
import com.gms.repository.OpcArvRepository;
import com.gms.repository.OpcArvRevisionRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
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

/**
 *
 * @author vvthanh
 */
@Service
public class OpcArvService extends OpcBaseService {

    @Autowired
    private OpcArvRepository opcArvRepository;
    @Autowired
    private OpcArvRevisionRepository arvRevisionRepository;
    @Autowired
    private QrCodeService qrCodeService;

    /**
     * Lấy thông tin cơ bản của bệnh nhân
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcArvEntity> getPatients(List<OpcArvEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<Long> pIDs = new HashSet<>(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPatientID")));
        Map<Long, OpcPatientEntity> patients = getPatients(pIDs);
        for (OpcArvEntity entity : entities) {
            entity.setPatient(patients.getOrDefault(entity.getPatientID(), null));
        }
        return entities;
    }

    /**
     * Lấy địa chỉ full
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcArvEntity> getAddress(List<OpcArvEntity> entities) {
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

        for (OpcArvEntity entity : entities) {
            //Địa chỉ thường trú
            entity.setPermanentAddressFull(buildAddress(
                    entity.getPermanentAddress(),
                    entity.getPermanentAddressStreet(),
                    entity.getPermanentAddressGroup(),
                    provinces.get(entity.getPermanentProvinceID()),
                    districts.get(entity.getPermanentDistrictID()),
                    wards.get(entity.getPermanentWardID())
            ));
            //Địa chỉ hiện tại
            entity.setCurrentAddressFull(buildAddress(
                    entity.getCurrentAddress(),
                    entity.getCurrentAddressStreet(),
                    entity.getCurrentAddressGroup(),
                    provinces.get(entity.getCurrentProvinceID()),
                    districts.get(entity.getCurrentDistrictID()),
                    wards.get(entity.getCurrentWardID())
            ));
        }
        return entities;
    }

    /**
     * Tham số
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcArvEntity> getParameters(List<OpcArvEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<Long> ids = new HashSet<>(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getID")));

        List<OpcParameterEntity> parameters = opcParameterRepository.findByTypeAndObjectID(OpcParameterTypeEnum.ARV.getKey(), ids);
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
        for (OpcArvEntity entity : entities) {
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
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.FIRST_CD4_CAUSE.getKey(), null) != null) {
                entity.setFirstCd4Causes(mAttrs.getOrDefault(OpcParameterAttributeEnum.FIRST_CD4_CAUSE.getKey(), null));
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE.getKey(), null) != null) {
                entity.setFirstViralLoadCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE.getKey(), null));
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
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null) != null) {
                entity.setViralLoadCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null));
            }
        }
        return entities;
    }

    /**
     * Tìm kiếm phân trang
     *
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> find(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.find(
                search.isRemove(),
                search.getSiteID(),
                search.getCode(),
                search.getName(),
                search.getIdentityCard(),
                search.getInsurance(),
                search.getInsuranceType(),
                search.getInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDateOfArrivalFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getDateOfArrivalTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferToTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferToTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferFromTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferFromTimeTo()),
                search.getStatusOfTreatmentIDs(),
                search.getTreatmentStageIDs(),
                search.getPermanentDistrictID(),
                search.getPermanentProvinceID(),
                search.getTreatmentRegimenIDs(),
                search.getInsuranceExpiry(),
                StringUtils.isEmpty(search.getGsph()) ? null : search.getGsph(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    public List<OpcArvEntity> findConnectHtcVisit(Set<Long> siteID, String identityCard) {
        List<OpcArvEntity> pages = arvRepository.findConnectHtcVisit(
                siteID,
                identityCard);
        return pages;
    }

    /**
     * Tìm kiếm ds tải lượng virus
     *
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findViral(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"viralLoadRetryTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findViral(
                search.getTab(),
                search.isRemove(),
                search.getSiteID(),
                search.getCode(),
                search.getName(),
                search.getIdentityCard(),
                search.getInsurance(),
                search.getInsuranceType(),
                search.getInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralTimeForm()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralRetryForm()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralRetryTo()),
                search.getResultID(),
                search.getStatusOfTreatmentID(),
                search.getTreatmentStageID(),
                search.getPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Tìm kiếm ds tải lượng virus
     *
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findVisit(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by("0".equals(search.getTab()) ? Sort.Direction.DESC : Sort.Direction.ASC, new String[]{"0".equals(search.getTab()) ? "updateAt" : "appointmentTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findVisit(
                search.getTab(),
                search.isRemove(),
                search.getSiteID(),
                search.getCode(),
                search.getName(),
                search.getIdentityCard(),
                search.getInsurance(),
                search.getInsuranceType(),
                search.getInsuranceNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralTimeForm()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getViralTimeTo()),
                search.getResultID(),
                search.getStatusOfTreatmentID(),
                search.getTreatmentStageID(),
                search.getPermanentProvinceID(),
                search.getPermanentDistrictID(),
                search.getPermanentWardID(),
                TextUtils.formatDate(new Date(), FORMATDATE),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getLastExaminationTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getLastExaminationTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTime84()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTime30()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTime90()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Tìm kiếm phân trang
     *
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findReveice(OpcReceiveSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findReceive(
                search.isRemove(),
                search.getCode(),
                search.getName(),
                search.getConfirmCode(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferTime()),
                search.getCurrentSiteID(),
                search.getSourceSiteID(),
                search.getStatus(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * @auth vvThành
     * @param ID
     * @return
     */
    public OpcArvEntity findOne(Long ID) {
        Optional<OpcArvEntity> optional = arvRepository.findById(ID);
        OpcArvEntity model = null;
        if (optional.isPresent()) {
            List<OpcArvEntity> entitys = new ArrayList<>();
            entitys.add(optional.get());
            getAddress(entitys);
            getParameters(entitys);
            getPatients(entitys);
            model = entitys.get(0);
        }
        return model;
    }

    /**
     * Find by code
     *
     * @auth vvThành
     * @param siteID
     * @param code
     * @return
     */
    public OpcArvEntity findByCode(Long siteID, String code) {
        OpcArvEntity model = arvRepository.findByCode(siteID, code);
        if (model != null) {
            List<OpcArvEntity> entitys = new ArrayList<>();
            entitys.add(model);
            getAddress(entitys);
            getParameters(entitys);
            getPatients(entitys);
            model = entitys.get(0);
        }
        return model;
    }

    public OpcArvEntity findBySiteAndCode(Long siteID, String code) {
        OpcArvEntity model = arvRepository.findByCode(siteID, code);
        return model;
    }

    /**
     * pdThang
     *
     * @param ID
     * @param sourceServiceID
     * @return
     */
    public OpcArvEntity findBySourceID(Long ID, String sourceServiceID) {
        List<OpcArvEntity> optional = arvRepository.findBySourceIDAndSourceServiceID(ID, sourceServiceID);
        OpcArvEntity model = null;
        if (optional != null && !optional.isEmpty()) {
            List<OpcArvEntity> entitys = new ArrayList<>();
            entitys.add(optional.get(0));
            getAddress(entitys);
            getParameters(entitys);
            getPatients(entitys);
            model = entitys.get(0);
        }
        return model;
    }

    /**
     * Thêm mới bệnh án
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcArvEntity create(OpcArvEntity model) throws Exception {
        try {
            Date currentDate = new Date();
            if (model.getID() != null) {
                getLogger().warn("Method chỉ sử dụng để thêm mới, Tìm thấy ID có mã " + model.getID());
                throw new Exception("Có lỗi xảy ra khi cập nhật");
            }

            if (model.getPatient() != null && model.getPatientID() == null) {
                OpcPatientEntity patient = model.getPatient();
                if (patient.getID() == null) {
                    patient.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                    patient.setCreatedBY(getCurrentUserID());
                }
                patient.setUpdateAt(currentDate);
                patient.setUpdatedBY(getCurrentUserID());
                patient.setFullName(TextUtils.toFullname(patient.getFullName()));
//                if (StringUtils.isNotEmpty(patient.getIdentityCard()) && StringUtils.isNotEmpty(patient.getIdentityCard())) {
                patient.setQrcode(qrCodeService.generate(patient.getIdentityCard(), patient.getFullName(), patient.getGenderID(), patient.getDob(), patient.getRaceID()));
//                }
                patientRepository.save(patient);
                model.setPatientID(patient.getID());
            }

            model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
            model.setCreatedBY(getCurrentUserID());
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            model.setCode(model.getCode());
            if (model.getTransferTimeGSPH() != null) {
                model.setUpdateTimeGSPH(new Date());
            }
            arvRepository.save(model);

            //Update chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.LAO_SYMPTOM, model.getID(), model.getLaoSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.INH_END_CAUSE, model.getID(), model.getInhEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.NTCH_SYMPTOM, model.getID(), model.getNtchSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, model.getID(), model.getCotrimoxazoleEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_CD4_CAUSE, model.getID(), model.getFirstCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.CD4_CAUSE, model.getID(), model.getCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE, model.getID(), model.getFirstViralLoadCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getViralLoadCauses());

            //Thêm mới giai đoạn điều trị
            OpcStageEntity stage = new OpcStageEntity();
            Long stageID = stage.getID();

            //Thông tin chung stage
            stage.setArvID(model.getID());
            stage.setPatientID(model.getPatientID());
            stage.setSiteID(model.getSiteID());
            stage.setCreateAT(currentDate);
            stage.setUpdateAt(currentDate);
            stage.setCreatedBY(getCurrentUserID());
            stage.setUpdatedBY(getCurrentUserID());

            stage.setAppointmentTime(model.getAppointmentTime());
            stage.setCd4(model.getCd4());
            stage.setClinicalStage(model.getClinicalStage());
            stage.setDaysReceived(model.getDaysReceived());
            stage.setExaminationNote(model.getExaminationNote());
            stage.setLastExaminationTime(model.getLastExaminationTime());
            stage.setExaminationNote(model.getExaminationNote());
            stage.setRegistrationTime(model.getRegistrationTime());
            stage.setRegistrationType(model.getRegistrationType());
            stage.setSourceCode(model.getSourceArvCode());
            stage.setSourceSiteID(model.getSourceSiteID());
            stage.setSourceSiteName(model.getSourceArvSiteName());
            stage.setStatusOfTreatmentID(model.getStatusOfTreatmentID());
            stage.setTransferCase(model.getTransferCase());
            stage.setTransferSiteID(model.getTransferSiteID());
            stage.setTransferSiteName(model.getTransferSiteName());
            stage.setTreatmentRegimenStage(model.getTreatmentRegimenStage());
            stage.setMedicationAdherence(model.getMedicationAdherence());
            stage.setStatusOfTreatmentID(model.getStatusOfTreatmentID());
            stage.setObjectGroupID(model.getObjectGroupID());
            stage.setReceivedWardDate(model.getReceivedWardDate());
            stage.setTranferFromTime(model.getTranferFromTime());
            stage.setSupplyMedicinceDate(model.getSupplyMedicinceDate());
            stage.setPregnantStartDate(model.getPregnantStartDate());
            stage.setPregnantEndDate(model.getPregnantEndDate());
            stage.setBirthPlanDate(model.getJoinBornDate());

            OpcStageEntity stage2 = stage.clone();
            //Nếu không nhập E4. Ngày điều trị ARV; hoặc chỉ nhập E4. Ngày điều trị ARV và không nhập E2.Ngày ARV đầu tiên; hoặc nhập E2.Ngày ARV đầu tiên = E4.Ngày điều trị ARV 
            if (model.getTreatmentTime() == null || (model.getTreatmentTime() != null && model.getFirstTreatmentTime() == null)
                    || TextUtils.formatDate(model.getFirstTreatmentTime(), FORMATDATE).equals(TextUtils.formatDate(model.getTreatmentTime(), FORMATDATE))) {
                OpcStageEntity fStage = stage.clone();
                fStage.setTreatmentStageID(model.getTreatmentStageID());
                fStage.setTreatmentStageTime(model.getTreatmentStageTime());
                fStage.setTreatmentTime(model.getTreatmentTime());
                fStage.setTreatmentRegimenID(model.getTreatmentRegimenID());
                fStage.setEndCase(model.getEndCase());
                fStage.setEndTime(model.getEndTime());
                updateToTreatmentStage(stage);
                if (validateStage(fStage)) {
                    OpcStageEntity save = stageRepository.save(fStage);
                    trackChangeStage(null, save);
                    stageID = save.getID();
                    logStage(model.getID(), model.getPatientID(), fStage.getID(), "Tự động tạo Thông tin điều trị khi thêm mới bệnh án");
                } else {
                    logArv(model.getID(), model.getPatientID(), "Giai đoạn điều trị đã tồn tại");
                }
            }

            //Nếu nhập E2. Ngày ARV đầu tiên <> E4. Ngày điều trị ARV, không nhập Ngày kết thúc
            if (model.getTreatmentTime() != null && model.getFirstTreatmentTime() != null && model.getEndTime() == null
                    && !TextUtils.formatDate(model.getFirstTreatmentTime(), FORMATDATE).equals(TextUtils.formatDate(model.getTreatmentTime(), FORMATDATE))) {
//                OpcStageEntity fStage = stage.clone();
                OpcStageEntity presentStage = stage2.clone();
                //Đợt điều trị đã kết thúc
                Calendar c = Calendar.getInstance();
                c.setTime(model.getTreatmentTime());
                c.add(Calendar.DATE, -1);
                //Đợt điều trị hiện tại
                presentStage.setTreatmentRegimenID(model.getTreatmentRegimenID());
                presentStage.setTreatmentTime(model.getTreatmentTime());
                presentStage.setTreatmentStageID(model.getTreatmentStageID());
                presentStage.setTreatmentStageTime(model.getTreatmentStageTime());
                presentStage.setEndTime(model.getEndTime());
                presentStage.setEndCase(model.getEndCase());
                updateToTreatmentStage(stage);
                updateToTreatmentStage(stage2);
                if (validateStage(presentStage)) {
                    OpcStageEntity save = stageRepository.save(presentStage);
                    trackChangeStage(null, save);
                    stageID = save.getID();
                    logStage(model.getID(), model.getPatientID(), presentStage.getID(), "Tự động tạo khi thêm mới bệnh án");
                } else {
                    logArv(model.getID(), model.getPatientID(), "Giai đoạn điều trị đã tồn tại");
                }

            }
            //Nếu nhập E2. Ngày ARV đầu tiên <> E4. Ngày điều trị ARV, nhập Ngày kết thúc và Lý do kết thúc
            if (model.getTreatmentTime() != null && model.getFirstTreatmentTime() != null
                    && StringUtils.isNotEmpty(model.getEndCase()) && model.getEndTime() != null
                    && !TextUtils.formatDate(model.getFirstTreatmentTime(), FORMATDATE).equals(TextUtils.formatDate(model.getTreatmentTime(), FORMATDATE))) {
//                OpcStageEntity fStage = stage.clone();
                OpcStageEntity presentStage = stage2.clone();
                Calendar c = Calendar.getInstance();
                c.setTime(model.getTreatmentTime());
                c.add(Calendar.DATE, -1);

                //Đợt điều trị hiện tại
                presentStage.setTreatmentTime(model.getTreatmentTime());
                presentStage.setEndTime(model.getEndTime());
                presentStage.setEndCase(model.getEndCase());
                presentStage.setTreatmentStageID(model.getTreatmentStageID());
                presentStage.setTreatmentStageTime(model.getTreatmentStageTime());
                presentStage.setTreatmentRegimenID(model.getTreatmentRegimenID());

                updateToTreatmentStage(stage2);
                if (validateStage(presentStage)) {
                    OpcStageEntity save = stageRepository.save(presentStage);
                    trackChangeStage(null, save);
                    stageID = save.getID();
                    logStage(model.getID(), model.getPatientID(), presentStage.getID(), "Tự động tạo khi thêm mới bệnh án");
                } else {
                    logArv(model.getID(), model.getPatientID(), "Giai đoạn điều trị đã tồn tại");
                }
            }

            //Thêm mới xét nghiệm TLVR
            OpcViralLoadEntity viralLoad = new OpcViralLoadEntity();
            viralLoad.setStageID(stageID);
            viralLoad.setArvID(model.getID());
            viralLoad.setPatientID(model.getPatientID());
            viralLoad.setSiteID(model.getSiteID());
            viralLoad.setCreateAT(currentDate);
            viralLoad.setUpdateAt(currentDate);
            viralLoad.setCreatedBY(getCurrentUserID());
            viralLoad.setUpdatedBY(getCurrentUserID());

            //TLVR lần đầu - bỏ
//            if (model.getFirstViralLoadTime() != null && model.getFirstViralLoadTime() != model.getViralLoadTime()) {
//                OpcViralLoadEntity fViralLoad = viralLoad.clone();
//
//                fViralLoad.setTestTime(model.getFirstViralLoadTime());
//                fViralLoad.setResult(model.getFirstViralLoadResult());
//                fViralLoad.setCauses(model.getFirstViralLoadCauses());
//                fViralLoad = viralLoadRepository.save(fViralLoad);
//                logViralLoad(model.getID(), model.getPatientID(), fViralLoad.getID(), "Tự động tạo khi thêm mới bệnh án");
//                if (model.getFirstViralLoadCauses() != null) {
//                    updateToParameter(OpcParameterTypeEnum.VIRAL_LOAD, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, fViralLoad.getID(), model.getFirstViralLoadCauses());
//                }
//            }
            //TLVR hiện tại
            if (model.getViralLoadTime() != null) {
                viralLoad.setTestTime(model.getViralLoadTime());
                viralLoad.setResult(model.getViralLoadResult());
                viralLoad.setResultNumber(model.getViralLoadResultNumber());
                viralLoad.setCauses(model.getViralLoadCauses());
                viralLoad = viralLoadRepository.save(viralLoad);
                logViralLoad(model.getID(), model.getPatientID(), viralLoad.getID(), "Tự động tạo Thông tin TLVR khi thêm mới bệnh án");
                if (viralLoad.getCauses() != null) {
                    updateToParameter(OpcParameterTypeEnum.VIRAL_LOAD, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, viralLoad.getID(), viralLoad.getCauses());
                }
            }

            //Thêm mới xét nghiệm
            boolean isTest = false;
            OpcTestEntity test = new OpcTestEntity();
            test.setStageID(stageID);
            test.setArvID(model.getID());
            test.setPatientID(model.getPatientID());
            test.setSiteID(model.getSiteID());
            test.setCreateAT(currentDate);
            test.setUpdateAt(currentDate);
            test.setCreatedBY(getCurrentUserID());
            test.setUpdatedBY(getCurrentUserID());
            test.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
            //Cd lần đầu tiên - bỏ
//            if (model.getFirstCd4Time() != null && model.getFirstCd4Time() != model.getCd4Time()) {
//                OpcTestEntity cd4 = test.clone();
//                cd4.setCd4TestTime(model.getFirstCd4Time());
//                cd4.setCd4Result(model.getFirstCd4Result());
//                cd4.setCd4Causes(model.getFirstCd4Causes());
//                testRepository.save(cd4);
//                logTest(model.getID(), model.getPatientID(), cd4.getID(), "Tự động tạo khi thêm mới bệnh án");
//                if (model.getFirstCd4Causes() != null) {
//                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.CD4_CAUSE, cd4.getID(), model.getFirstCd4Causes());
//                }
//            }
            //Cd4 hiện tại
            if (model.getCd4Time() != null) {
                isTest = true;
                test.setCd4TestTime(model.getCd4Time());
                test.setCd4Result(model.getCd4Result());
                test.setCd4Causes(model.getCd4Causes()); //Ở dưới sẽ update cho param - set tạm
            }
            //ntch
            if (model.isNtch()) {
                isTest = true;
                test.setNtch(model.isNtch());
                test.setNtchOtherSymptom(model.getNtchOtherSymptom());
                test.setNtchSymptoms(model.getNtchSymptoms());//Ở dưới sẽ update cho param - set tạm
            }

            //Cotrimoxazole
            if (model.getCotrimoxazoleToTime() != null || model.getCotrimoxazoleFromTime() != null) {
                isTest = true;
                test.setCotrimoxazoleFromTime(model.getCotrimoxazoleFromTime());
                test.setCotrimoxazoleToTime(model.getCotrimoxazoleToTime());
                if (test.getCotrimoxazoleToTime() != null) {
                    test.setCotrimoxazoleEndCauses(model.getCotrimoxazoleEndCauses());//Ở dưới sẽ update cho param - set tạm
                }
                test.setCotrimoxazoleOtherEndCause(model.getCotrimoxazoleOtherEndCause());
            }

            //lao
            if (model.isLao()) {
                isTest = true;
                test.setLao(model.isLao());
                test.setLaoOtherSymptom(model.getLaoOtherSymptom());
                test.setLaoSymptoms(model.getLaoSymptoms());//Ở dưới sẽ update cho param - set tạm
                test.setLaoTestTime(model.getLaoTestTime());
                test.setLaoResult(model.getLaoResult());

                test.setSuspiciousSymptoms(model.getSuspiciousSymptoms());
                test.setExaminationAndTest(model.isExaminationAndTest());
                test.setLaoTestDate(model.getLaoTestDate());
                test.setLaoDiagnose(model.getLaoDiagnose());

            }
            test.setLaoTreatment(model.isLaoTreatment());
            if (test.isLaoTreatment()) {
                isTest = true;
                test.setLaoStartTime(model.getLaoStartTime());
                test.setLaoEndTime(model.getLaoEndTime());
            }

            //Điều trị INH
            if (model.isInh()) {
                isTest = true;
                test.setInh(model.isInh());
                test.setInhEndCauses(model.getInhEndCauses());//Ở dưới sẽ update cho param - set tạm
                test.setInhFromTime(model.getInhFromTime());
                test.setInhToTime(model.getInhToTime());
            }

            //hbv
            if (model.isHbv()) {
                isTest = true;
                test.setHbv(model.isHbv());
                test.setHbvCase(model.getHbvCase());//Ở dưới sẽ update cho param - set tạm
                test.setHbvResult(model.getHbvResult());
                test.setHbvTime(model.getHbvTime());
            }

            //hcv
            if (model.isHcv()) {
                isTest = true;
                test.setHcv(model.isHcv());
                test.setHcvCase(model.getHcvCase());//Ở dưới sẽ update cho param - set tạm
                test.setHcvResult(model.getHcvResult());
                test.setHcvTime(model.getHcvTime());
            }

            if (isTest) {
                testRepository.save(test);
                logTest(model.getID(), model.getPatientID(), test.getID(), "Tự động tạo  Xét nghiệm và điều trị dự phòng khi thêm mới bệnh án");
                if (test.getCd4Causes() != null) {
                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.CD4_CAUSE, test.getID(), test.getCd4Causes());
                }
                if (test.getNtchSymptoms() != null) {
                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.NTCH_SYMPTOM, test.getID(), test.getNtchSymptoms());
                }
                if (test.getCotrimoxazoleEndCauses() != null) {
                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, test.getID(), test.getCotrimoxazoleEndCauses());
                }
                if (test.getLaoSymptoms() != null) {
                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.LAO_SYMPTOM, test.getID(), test.getLaoSymptoms());
                }
                if (test.getInhEndCauses() != null) {
                    updateToParameter(OpcParameterTypeEnum.TEST, OpcParameterAttributeEnum.INH_END_CAUSE, test.getID(), test.getInhEndCauses());
                }
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin bệnh án
     *
     * @auth vvThành
     * @param model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public OpcArvEntity update(OpcArvEntity model) throws Exception {
        try {
            LoggedUser currentUser = getCurrentUser();
            Date currentDate = new Date();
            //xử lý patient: không phải OPC, không có patientID và có object patient
            if (model.getPatient() != null && model.getPatientID() != null) {
                Optional<OpcPatientEntity> oPatient = patientRepository.findById(model.getPatientID());
                if (!oPatient.isPresent()) {
                    throw new Exception("Không thể cập nhật bệnh án, không tìm thấy bệnh nhân");
                }
                OpcPatientEntity patient = oPatient.get();
                //Chỉ cơ sở tạo ra hoặc pac-opc mới đc sửa
                if (currentUser.getSite().getID().equals(patient.getSiteID())
                        || currentUser.getSite().getServiceIDs().contains(ServiceEnum.OPC_MANAGEMENT.getKey())) {
                    patient.setConfirmCode(model.getPatient().getConfirmCode());
                    patient.setConfirmSiteID(model.getPatient().getConfirmSiteID());
                    patient.setConfirmSiteName(model.getPatient().getConfirmSiteName());
                    patient.setConfirmTime(model.getPatient().getConfirmTime());
                    patient.setDob(model.getPatient().getDob());
                    patient.setFullName(TextUtils.toFullname(model.getPatient().getFullName()));
                    patient.setGenderID(model.getPatient().getGenderID());
                    patient.setIdentityCard(model.getPatient().getIdentityCard());
                    patient.setRaceID(model.getPatient().getRaceID());
                    patient.setUpdateAt(currentDate);
                    patient.setUpdatedBY(getCurrentUserID());
//                    if (StringUtils.isNotEmpty(patient.getIdentityCard()) && StringUtils.isNotEmpty(patient.getIdentityCard())) {
                    patient.setQrcode(qrCodeService.generate(patient.getIdentityCard(), patient.getFullName(), patient.getGenderID(), patient.getDob(), patient.getRaceID()));
//                    }
                    patientRepository.save(patient);
                    model.setPatientID(patient.getID());
                }
            }

            if (model.getID() == null) {
                getLogger().warn("Method chỉ sử dụng để cập nhật, không tìm thấy ID bệnh án");
                throw new Exception("Có lỗi xảy ra khi cập nhật");
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            if (model.getTransferTimeGSPH() != null) {
                model.setUpdateTimeGSPH(new Date());
            }

            arvRepository.save(model);
            //Update chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.LAO_SYMPTOM, model.getID(), model.getLaoSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.INH_END_CAUSE, model.getID(), model.getInhEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.NTCH_SYMPTOM, model.getID(), model.getNtchSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, model.getID(), model.getCotrimoxazoleEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_CD4_CAUSE, model.getID(), model.getFirstCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.CD4_CAUSE, model.getID(), model.getCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE, model.getID(), model.getFirstViralLoadCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getViralLoadCauses());

            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    public OpcArvEntity updateVNPT(OpcArvEntity model) throws Exception {
        try {
            Date currentDate = new Date();
            //xử lý patient: không phải OPC, không có patientID và có object patient
            if (model.getPatient() != null && model.getPatientID() != null) {
                Optional<OpcPatientEntity> oPatient = patientRepository.findById(model.getPatientID());
                if (!oPatient.isPresent()) {
                    throw new Exception("Không thể cập nhật bệnh án, không tìm thấy bệnh nhân");
                }
                OpcPatientEntity patient = oPatient.get();
                //Chỉ cơ sở tạo ra hoặc pac-opc mới đc sửa
                patient.setConfirmCode(model.getPatient().getConfirmCode());
                patient.setConfirmSiteID(model.getPatient().getConfirmSiteID());
                patient.setConfirmSiteName(model.getPatient().getConfirmSiteName());
                patient.setConfirmTime(model.getPatient().getConfirmTime());
                patient.setDob(model.getPatient().getDob());
                patient.setFullName(TextUtils.toFullname(model.getPatient().getFullName()));
                patient.setGenderID(model.getPatient().getGenderID());
                patient.setIdentityCard(model.getPatient().getIdentityCard());
                patient.setRaceID(model.getPatient().getRaceID());
                patient.setUpdateAt(currentDate);
                patient.setUpdatedBY(Long.valueOf("0"));
                if (StringUtils.isNotEmpty(patient.getIdentityCard()) && StringUtils.isNotEmpty(patient.getIdentityCard())) {
                    patient.setQrcode(qrCodeService.generate(patient.getIdentityCard(), patient.getFullName(), patient.getGenderID(), patient.getDob(), patient.getRaceID()));
                }
                patientRepository.save(patient);
                model.setPatientID(patient.getID());
            }

            model.setUpdateAt(currentDate);
            model.setUpdatedBY(Long.valueOf("0"));
            if (model.getTransferTimeGSPH() != null) {
                model.setUpdateTimeGSPH(new Date());
            }

            arvRepository.save(model);
            //Update chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.LAO_SYMPTOM, model.getID(), model.getLaoSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.INH_END_CAUSE, model.getID(), model.getInhEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.NTCH_SYMPTOM, model.getID(), model.getNtchSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, model.getID(), model.getCotrimoxazoleEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_CD4_CAUSE, model.getID(), model.getFirstCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.CD4_CAUSE, model.getID(), model.getCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE, model.getID(), model.getFirstViralLoadCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getViralLoadCauses());

            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public OpcArvEntity createVNPT(OpcArvEntity model) throws Exception {
        try {
            Date currentDate = new Date();

            if (model.getPatient() != null && model.getPatientID() == null) {
                OpcPatientEntity patient = model.getPatient();
                if (patient.getID() == null) {
                    patient.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
                    patient.setCreatedBY(Long.valueOf("0"));
                }
                patient.setUpdateAt(currentDate);
                patient.setUpdatedBY(Long.valueOf("0"));
                patient.setFullName(TextUtils.toFullname(patient.getFullName()));
                if (StringUtils.isNotEmpty(patient.getIdentityCard()) && StringUtils.isNotEmpty(patient.getIdentityCard())) {
                    patient.setQrcode(qrCodeService.generate(patient.getIdentityCard(), patient.getFullName(), patient.getGenderID(), patient.getDob(), patient.getRaceID()));
                }
                patientRepository.save(patient);
                model.setPatientID(patient.getID());
            }

            model.setCreateAT(model.getCreateAT() == null ? currentDate : model.getCreateAT());
            model.setCreatedBY(Long.valueOf("0"));
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(Long.valueOf("0"));
            model.setCode(model.getCode());
            if (model.getTransferTimeGSPH() != null) {
                model.setUpdateTimeGSPH(new Date());
            }
            arvRepository.save(model);

            //Update chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.LAO_SYMPTOM, model.getID(), model.getLaoSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.INH_END_CAUSE, model.getID(), model.getInhEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.NTCH_SYMPTOM, model.getID(), model.getNtchSymptoms());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, model.getID(), model.getCotrimoxazoleEndCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_CD4_CAUSE, model.getID(), model.getFirstCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.CD4_CAUSE, model.getID(), model.getCd4Causes());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE, model.getID(), model.getFirstViralLoadCauses());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, model.getID(), model.getViralLoadCauses());

            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public boolean checkExistBySiteIDAndCode(Long siteID, String code) {
        return arvRepository.existsByCode(siteID, code) > 0;
    }

    /**
     * Tìm kiếm bệnh án theo sơ sở và code
     *
     * @auth vvThành
     * @param siteID
     * @param code
     * @return
     */
    public OpcArvEntity findBySiteIDAndCode(Long siteID, String code) {
        OpcArvEntity arv = arvRepository.findBySiteIDAndCode(siteID, code);
        if (arv != null) {
            List<OpcArvEntity> arvs = new ArrayList();
            arvs.add(arv);
            getPatients(arvs);
            return arvs.get(0);
        }
        return arv;
    }

    public List<OpcArvEntity> findBySiteIDs(Set<Long> siteIDs) {
        List<OpcArvEntity> arvs = arvRepository.findBySiteIDs(siteIDs);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    public OpcArvEntity findBySiteIDAndCodeAndDataID(Long siteID, String code, String dataID) {
        OpcArvEntity arv = arvRepository.findBySiteIDAndCodeAndDataID(siteID, code, dataID);
        if (arv != null) {
            List<OpcArvEntity> arvs = new ArrayList();
            arvs.add(arv);
            getPatients(arvs);
            return arvs.get(0);
        }
        return arv;
    }

    /**
     * Xóa bệnh nhân vĩnh viễn
     *
     * @author DSNAnh
     * @param ID
     */
    public void remove(Long ID) {
        OpcArvEntity arv = findOne(ID);
        arvRepository.delete(arv);
    }

    /**
     * Danh sách lịch sử theo bệnh án
     *
     * @param oID
     * @return
     */
    public List<OpcArvLogEntity> getLogs(Long oID) {
        return logRepository.findByArvID(oID);
    }

    /**
     * DS biến động điều trị
     *
     * @author DSNAnh
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findTreatmentFluctuations(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"treatmentStageTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findTreatmentFluctuations(
                search.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentStageTimeTo()),
                search.getTreatmentStageIDs(),
                search.getStatusOfTreatmentIDs(),
                search.getPermanentDistrictID(),
                search.getPermanentProvinceID(),
                search.getKeywords(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Danh sách lịch sử viral theo bệnh án
     *
     * @param oID
     * @return
     */
    public List<OpcArvLogEntity> getViralLogs(Long oID) {
        return logRepository.findViralByArvID(oID);
    }

    /**
     * Danh sách lịch sử viral theo bệnh án
     *
     * @param oID
     * @return
     */
    public List<OpcArvLogEntity> getVisitLogs(Long oID) {
        return logRepository.findVisitByArvID(oID);
    }

    /**
     * DSBN quản lý
     *
     * @author DSNAnh
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findOpcPatient(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"registrationTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findOpcPatient(
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInhFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getInhTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getCotriFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getCotriTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getLaoFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getLaoTo()),
                search.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                search.getStatusOfTreatmentID(),
                search.getPermanentDistrictID(),
                search.getPermanentProvinceID(),
                search.getKeywords(),
                search.getRegistrationType(),
                search.getTreatmentRegimenID(),
                StringUtils.isEmpty(search.getGsph()) ? null : search.getGsph(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Sổ ARV
     *
     * @author DSNAnh
     * @param search
     * @return
     */
    public DataPage<OpcArvEntity> findArvBook(OpcArvSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"treatmentTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findArvBook(
                search.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    public OpcArvEntity getFirst() {
        OpcArvEntity arv = null;
        List<OpcArvEntity> list = arvRepository.getOpcArv();
        if (list != null && !list.isEmpty()) {
            arv = list.get(0);
        }
        return arv;
    }

    public DataPage<OpcArvEntity> findArvBooks(OpcArvBookSearch search) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"treatmentTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvEntity> pages = arvRepository.findArvBooks(
                search.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTimeTo()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Tìm kiếm phân trang
     *
     * @param siteID
     * @param start
     * @param end
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public DataPage<OpcArvEntity> findPreArv(Long siteID, String start, String end, int pageIndex, int pageSize) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);
//        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"registration_time"});
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Page<OpcArvEntity> pages = arvRepository.findPreArv(
                siteID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Lấy ds OPC cần cập nhật sang PAC
     *
     * @param size
     * @return
     */
    public List<OpcArvEntity> getForUpdateOpcToPac(int size) {
        List<OpcArvEntity> arvs = opcArvRepository.getForUpdateOpcToPac(PageRequest.of(0, size));
        if (arvs != null) {
            getAddress(arvs);
            getParameters(arvs);
            getPatients(arvs);
        }
        return arvs;
    }

    /**
     * Những ca chưa đẩy lên QLNN
     *
     * @param size
     * @return
     */
    public List<OpcArvEntity> getNoForPac(int size) {
        List<OpcArvEntity> arvs = opcArvRepository.getNoForPac(PageRequest.of(0, size));
        if (arvs != null) {
            getAddress(arvs);
            getParameters(arvs);
            getPatients(arvs);
        }
        return arvs;
    }

    public List<OpcArvRevisionEntity> findByArvIDAndSite(Long arvID, Long siteID) {
        return arvRevisionRepository.findByArvIDAndSite(arvID, siteID);
    }

    /**
     * Tìm thông tin log theo arvID, siteID, stageID
     *
     * @param arvID
     * @param siteID
     * @param stageID
     * @return
     */
    public List<OpcArvRevisionEntity> findByArvIDAndStageAndSite(Long arvID, Long siteID, Long stageID) {
        return arvRevisionRepository.findByArvIDAndSiteAndStage(arvID, siteID, stageID);
    }

    /**
     * find import excel visit
     *
     * @param name
     * @param age
     * @param examinationTime
     * @param gender
     * @param insurance
     * @return
     */
    public List<OpcArvEntity> findArvImportExcelVisit(String name, Integer month, String examinationTime, String gender, String insurance) {
        LoggedUser currentUser = getCurrentUser();
        List<OpcArvEntity> items;
        if (StringUtils.isNotBlank(insurance)) {
            items = arvRepository.findArvImportExcelVisit(name, month, examinationTime, gender, insurance, currentUser.getSite().getID());
            items = items == null || items.isEmpty() ? arvRepository.findArvImportExcelVisit(name, month, examinationTime, gender, null, currentUser.getSite().getID()) : items;
        } else {
            items = arvRepository.findArvImportExcelVisit(name, month, examinationTime, gender, null, currentUser.getSite().getID());
        }

        return items.isEmpty() ? null : items;
    }

    /**
     * find import excel viral
     *
     * @param name
     * @param yearOfBirth
     * @param gender
     * @param insurance
     * @return
     */
    public List<OpcArvEntity> findArvImportExcelViral(String name, Integer yearOfBirth, String gender, String insurance) {
        LoggedUser currentUser = getCurrentUser();
        List<OpcArvEntity> items;
        if (StringUtils.isNotBlank(insurance)) {
            items = arvRepository.findArvImportExcelViral(name, yearOfBirth, gender, insurance, currentUser.getSite().getID());
            items = items == null || items.isEmpty() ? arvRepository.findArvImportExcelViral(name, yearOfBirth, gender, null, currentUser.getSite().getID()) : items;
        } else {
            items = arvRepository.findArvImportExcelViral(name, yearOfBirth, gender, null, currentUser.getSite().getID());
        }

        return items.isEmpty() ? null : items;
    }

    /**
     * Lưu thông tin bệnh án
     *
     * @param model
     * @return
     */
    public OpcArvEntity save(OpcArvEntity model) {
        model.setUpdateAt(new Date());
        model.setUpdatedBY(getCurrentUserID());
        return arvRepository.save(model);
    }

    public Iterable<OpcArvEntity> saveAll(List<OpcArvEntity> models) {
        for (OpcArvEntity model : models) {
            model.setUpdateAt(new Date());
            model.setUpdatedBY(getCurrentUserID());
        }
        return arvRepository.saveAll(models);
    }

    /**
     * @auth vvThành
     * @param siteIds
     * @return
     */
    public OpcArvEntity getFirst(Set<Long> siteIds) {
        Page<OpcArvEntity> page = arvRepository.getFirst(siteIds, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"registrationTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    public DataPage<OpcArvRevisionEntity> findArvRevision(OpcRevisionSearch search) {
        DataPage<OpcArvRevisionEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"createAT", "ID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvRevisionEntity> pages = arvRevisionRepository.findArvRevision(
                search.getSiteID(),
                search.getArvID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTimeChangeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTimeChangeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRegistrationTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTreatmentTime()),
                StringUtils.isEmpty(search.getStatusOfTreatmentID()) ? null : search.getStatusOfTreatmentID(),
                StringUtils.isEmpty(search.getEndCase()) ? null : search.getEndCase(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEndTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTranferFromTime()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    /**
     * @auth vvThành
     * @param patientID
     * @return
     */
    public List<OpcArvEntity> findByPatient(Long patientID) {
        List<OpcArvEntity> arvs = arvRepository.findByPatient(patientID);
        getAddress(arvs);
        getParameters(arvs);
        getPatients(arvs);
        return arvs;
    }

    /**
     * Tìm mã BA cuối cùng
     *
     * @auth DSNAnh
     * @param siteID
     * @param code
     * @return
     */
    public OpcArvEntity findLastBySiteID(Long siteID, String code) {
        return arvRepository.findLastCodeBySiteID(siteID, code);
    }

    /**
     * Generate code
     *
     * @return
     */
    public String getCode() {
        LoggedUser currentUser = getCurrentUser();
        String code = currentUser.getSite().getCode();
        OpcArvEntity opc = arvRepository.findLastCodeBySiteID(currentUser.getSite().getID(), code);
        try {
            String[] split = opc.getCode().split("-", -1);
            Long identity = Long.valueOf(split[split.length - 1]);
            if (identity.toString().length() < 5) {
                String leftPad = StringUtils.leftPad(String.valueOf(identity + 1), 5, "0");
                return String.format("%s-%s", code, leftPad).toUpperCase();
            } else {
                return String.format("%s-%s", code, String.valueOf((identity + 1))).toUpperCase();
            }
        } catch (Exception e) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING_OPC.getKey()).toUpperCase();
        }
    }

    /**
     * Tìm kiếm phân trang
     *
     * @param siteID
     * @param start
     * @param end
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public DataPage<OpcArvEntity> findBookVirus(Long siteID, String start, String end, int pageIndex, int pageSize) {
        DataPage<OpcArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);
//        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"registration_time"});
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Page<OpcArvEntity> pages = arvRepository.findPreArv(
                siteID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, start),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, end),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        //Get info
        getAddress(dataPage.getData());
        getParameters(dataPage.getData());
        getPatients(dataPage.getData());
        return dataPage;
    }

    /**
     * Danh sách theo ids
     *
     * @auth vvThành
     * @param arvIDs
     * @return
     */
    public List<OpcArvEntity> findAllByIds(List<Long> arvIDs) {
        List<OpcArvEntity> arvs = arvRepository.findAllById(arvIDs);
        if (arvs != null && !arvs.isEmpty()) {
            getAddress(arvs);
            getParameters(arvs);
            getPatients(arvs);
        }
        return arvs;
    }

    /**
     * pdThang
     *
     * @param search
     * @return
     */
    public DataPage<OpcArvLogEntity> findAllLog(LogSearch search) {
        DataPage<OpcArvLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"patientID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<OpcArvLogEntity> pages = logRepository.findAll(
                //                search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                StringUtils.isEmpty(search.getCategory()) ? null : search.getCategory(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * lấy danh sách patient
     *
     * @auth vvThành
     * @param patients
     * @return
     */
    public List<OpcPatientEntity> getPatientsByIds(Set<Long> patients) {
        List<OpcPatientEntity> items = patientRepository.findAllByIds(patients);
        return items;
    }

    public OpcArvEntity updateJob(OpcArvEntity model) throws Exception {
        return opcArvRepository.save(model);
    }

}
