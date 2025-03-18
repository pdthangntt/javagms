package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TreatmentStageEnum;
import com.gms.entity.db.OpcApiLogEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.repository.DistrictRepository;
import com.gms.repository.OpcApiLogRepository;
import com.gms.repository.OpcArvLogRepository;
import com.gms.repository.OpcArvRepository;
import com.gms.repository.OpcArvRevisionRepository;
import com.gms.repository.OpcParameterRepository;
import com.gms.repository.OpcPatientRepository;
import com.gms.repository.OpcStageRepository;
import com.gms.repository.OpcTestRepository;
import com.gms.repository.OpcViralLoadRepository;
import com.gms.repository.OpcVisitRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
public abstract class OpcBaseService extends BaseService {

    @Autowired
    protected OpcArvLogRepository logRepository;
    @Autowired
    protected OpcApiLogRepository apiLogRepository;
    @Autowired
    protected OpcPatientRepository patientRepository;
    @Autowired
    protected OpcArvRepository arvRepository;
    @Autowired
    protected OpcTestRepository testRepository;
    @Autowired
    protected OpcViralLoadRepository viralLoadRepository;
    @Autowired
    protected OpcStageRepository stageRepository;
    @Autowired
    protected OpcParameterRepository opcParameterRepository;
    @Autowired
    protected OpcVisitRepository visitRepository;
    @Autowired
    protected ProvinceRepository provinceRepository;
    @Autowired
    protected DistrictRepository districtRepository;
    @Autowired
    protected WardRepository wardRepository;
    @Autowired
    protected OpcTestService opcTestService;
    @Autowired
    protected OpcArvRevisionRepository revisionRepository;
    @Autowired
    protected Gson gson;
    @Autowired
    private OpcViralLoadService viralLoadService;

    /**
     * @auth vvThành
     * @param arvID
     * @param patientID
     * @param stageID
     * @param testID
     * @param viralLoadID
     * @param content
     * @return
     */
    protected OpcArvLogEntity log(Long arvID, Long patientID, Long stageID, Long testID, Long viralLoadID, Long visitID, String content) {
        OpcArvLogEntity model = new OpcArvLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setTime(new Date());
        model.setArvID(arvID);
        model.setPatientID(patientID);
        model.setStageID(stageID);
        model.setTestID(testID);
        model.setViralLoadID(viralLoadID);
        model.setVisitID(visitID);
        model.setContent(content);
        return logRepository.save(model);
    }

    public OpcArvLogEntity logArv(Long arvID, Long patientID, String content) {
        return log(arvID, patientID, null, null, null, null, content);
    }

    public OpcArvLogEntity logStage(Long arvID, Long patientID, Long stageID, String content) {
        return log(arvID, patientID, stageID, null, null, null, content);
    }

    public OpcArvLogEntity logTest(Long arvID, Long patientID, Long testID, String content) {
        return log(arvID, patientID, null, testID, null, null, content);
    }

    public OpcArvLogEntity logViralLoad(Long arvID, Long patientID, Long viralLoadID, String content) {
        return log(arvID, patientID, null, null, viralLoadID, null, content);
    }

    public OpcArvLogEntity logVisit(Long arvID, Long patientID, Long visitID, String content) {
        return log(arvID, patientID, null, null, null, visitID, content);
    }

    /**
     * Log API
     *
     * @param type
     * @param checksum
     * @param status
     * @param message
     * @param data
     * @param arvID
     * @param arvCode
     * @param siteID
     * @param sourceSiteID
     * @return
     */
    public OpcApiLogEntity logAPI(String type, String checksum, boolean status, String message, Object data, Long arvID, String arvCode, Long siteID, String sourceSiteID) {
        OpcApiLogEntity log = new OpcApiLogEntity();
        log.setTime(new Date());
        log.setType(type);
        log.setChecksum(checksum);
        log.setStatus(status);
        log.setMessage(message);
        log.setData(data != null ? gson.toJson(data) : null);
        log.setArvID(arvID);
        log.setArvCode(arvCode);
        log.setSourceSiteID(sourceSiteID);
        log.setSiteID(siteID);
        return apiLogRepository.save(log);
    }

    /**
     * Log API lỗi
     *
     * @param type
     * @param checksum
     * @param message
     * @param data
     * @param sourceSiteID
     * @return
     */
    public OpcApiLogEntity logAPIError(String type, String checksum, String message, Object data, String sourceSiteID) {
        return logAPI(type, checksum, false, message, data, null, null, null, sourceSiteID);
    }

    /**
     * Lưu tham số chọn nhiều
     *
     * @param type
     * @param attributeName
     * @param objectID
     * @param parameters
     * @throws Exception
     */
    protected void updateToParameter(OpcParameterTypeEnum type, OpcParameterAttributeEnum attributeName, Long objectID, List<String> parameters) throws Exception {
        opcParameterRepository.removeByObjectID(type.getKey(), attributeName.getKey(), objectID);
        if (parameters == null || parameters.isEmpty()) {
            return;
        }
        Set<String> sParameters = Sets.newHashSet(parameters);
        List<OpcParameterEntity> items = new ArrayList<>();
        OpcParameterEntity item;
        for (String parameter : sParameters) {
            item = new OpcParameterEntity();
            item.setObjectID(objectID);
            item.setType(type.getKey());
            item.setAttrName(attributeName.getKey());
            item.setParameterID(parameter);
            items.add(item);
        }
        opcParameterRepository.saveAll(items);
    }

    /**
     * Cập nhật bệnh án khi thay đổi thông tin điều trị update 11/05/2020
     * vvthanh - Bỏ cập nhật đợt điều trị đầu tiên
     *
     * @auth vvThành
     * @param arv
     * @throws Exception
     */
    public void updateArvFromStage(OpcArvEntity arv) throws Exception {
        List<OpcStageEntity> cStages = stageRepository.findByPatientID(arv.getPatientID(), false);
        List<OpcStageEntity> stages = new ArrayList<>();
        for (OpcStageEntity item : cStages) {
            if (item.getStatusOfTreatmentID() == null || item.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey()) || item.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey())) {
                continue;
            }
            //Chỉ lấy đợt điều trị của bệnh án
            if (!item.getArvID().equals(arv.getID())) {
                continue;
            }
            stages.add(item);
        }
        if (stages.isEmpty()) {
            return;
        }
        OpcStageEntity lastArv = stages.get(0);
//        OpcStageEntity firstArv = stages.get(stages.size() - 1);
        OpcArvEntity oldArv = arv.clone();

        //thông tin đăng ký điều trị
        arv.setRegistrationTime(lastArv.getRegistrationTime());
        arv.setRegistrationType(lastArv.getRegistrationType());
        arv.setSourceArvSiteID(lastArv.getSourceSiteID());
        arv.setSourceArvSiteName(lastArv.getSourceSiteName());
        arv.setSourceArvCode(lastArv.getSourceCode());
        arv.setClinicalStage(lastArv.getClinicalStage());
        arv.setCd4(lastArv.getCd4());
        arv.setSourceSiteID(lastArv.getSourceSiteID());

        //Thông tin điều trị ARV
        arv.setStatusOfTreatmentID(lastArv.getStatusOfTreatmentID());
//        arv.setFirstTreatmentTime(firstArv.getTreatmentTime());
//        arv.setFirstTreatmentRegimenID(firstArv.getTreatmentRegimenID());
        arv.setTreatmentTime(lastArv.getTreatmentTime());
        arv.setTreatmentRegimenStage(lastArv.getTreatmentRegimenStage());
        arv.setTreatmentRegimenID(lastArv.getTreatmentRegimenID());
        arv.setLastExaminationTime(lastArv.getLastExaminationTime());
        arv.setMedicationAdherence(lastArv.getMedicationAdherence());
        arv.setDaysReceived(lastArv.getDaysReceived());
        arv.setAppointmentTime(lastArv.getAppointmentTime()); //ngày hẹn khám
        arv.setExaminationNote(lastArv.getExaminationNote());
        arv.setQualifiedTreatmentTime(lastArv.getQualifiedTreatmentTime());
        arv.setSupplyMedicinceDate(lastArv.getSupplyMedicinceDate());
        arv.setReceivedWardDate(lastArv.getReceivedWardDate());
        arv.setObjectGroupID(lastArv.getObjectGroupID());

        //Trạng thái biến động điều trị
        arv.setTreatmentStageID(lastArv.getTreatmentStageID());
        arv.setTreatmentStageTime(lastArv.getTreatmentStageTime());
        arv.setEndTime(lastArv.getEndTime());
        arv.setEndCase(lastArv.getEndCase());
        arv.setTransferSiteID(lastArv.getTransferSiteID() == null ? arv.getTransferSiteID() : lastArv.getTransferSiteID());
        arv.setTransferSiteName(StringUtils.isEmpty(lastArv.getTransferSiteName()) ? arv.getTransferSiteName() : lastArv.getTransferSiteName());
        arv.setTransferCase(lastArv.getTransferCase());

        if (lastArv.getEndTime() != null && lastArv.getEndCase().equals(ArvEndCaseEnum.DEAD.getKey())) {
            arv.setDeathTime(lastArv.getEndTime());
        }

        //Update thông tin chuyển gửi
        arv.setTranferBY(lastArv.getTranferBY());

        if (StringUtils.isEmpty(lastArv.getEndCase()) || !lastArv.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
            arv.setTranferFromTime(null);
            arv.setTransferSiteID(null);
        } else {
            arv.setTranferFromTime(lastArv.getTranferFromTime());
            arv.setTransferSiteID(lastArv.getTransferSiteID());
        }

        if (arv.getTransferTimeGSPH() != null) {
            arv.setUpdateTimeGSPH(new Date());
        }

        arv.setPregnantStartDate(lastArv.getPregnantStartDate());
        arv.setPregnantEndDate(lastArv.getPregnantEndDate());
        arv.setJoinBornDate(lastArv.getBirthPlanDate());
        if (arv.getTransferTimeGSPH() != null) {
            arv.setUpdateTimeGSPH(new Date());
        }

        arvRepository.save(arv);
        logArv(arv.getID(), arv.getPatientID(), "Cập nhật bệnh án từ điều trị:" + arv.changeToString(oldArv, true));
    }

    /**
     * Cập nhật bệnh án khi thay đổi thông tin xét nghiệm Update vvthanh: bỏ
     * ngày đầu tiên
     *
     * @auth vvThành
     * @param arv
     * @throws Exception
     */
    public void updateArvFromTest(OpcArvEntity arv) throws Exception {
        OpcArvEntity oldArv = arv.clone();
        List<OpcTestEntity> cd4s = testRepository.findCd4ByArvID(arv.getID());
        opcTestService.getParameters(cd4s);
        if (cd4s != null && !cd4s.isEmpty()) {
            OpcTestEntity lastTest = cd4s.get(cd4s.size() - 1);
            arv.setCd4Time(lastTest.getCd4TestTime());
            arv.setCd4Result(lastTest.getCd4Result());
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.CD4_CAUSE, arv.getID(), lastTest.getCd4Causes());
        }

        //ntch
        List<OpcTestEntity> ntchs = testRepository.findNtchByArvID(arv.getID());
        opcTestService.getParameters(ntchs);
        if (ntchs != null && !ntchs.isEmpty()) {
            OpcTestEntity ntch = ntchs.get(ntchs.size() - 1);

            arv.setNtch(ntch.isNtch());
            arv.setNtchOtherSymptom(ntch.getNtchOtherSymptom());

            //Chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.NTCH_SYMPTOM, arv.getID(), ntch.getNtchSymptoms());
        }

        List<OpcTestEntity> cotrimoxazos = testRepository.findCotrimoxazoleByArvID(arv.getID());
        opcTestService.getParameters(cotrimoxazos);
        if (cotrimoxazos != null && !cotrimoxazos.isEmpty()) {
            OpcTestEntity cotrimoxazo = cotrimoxazos.get(cotrimoxazos.size() - 1);

            arv.setCotrimoxazoleFromTime(cotrimoxazo.getCotrimoxazoleFromTime());
            arv.setCotrimoxazoleToTime(cotrimoxazo.getCotrimoxazoleToTime());
            arv.setCotrimoxazoleOtherEndCause(cotrimoxazo.getCotrimoxazoleOtherEndCause());
            //Chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.COTRIMOXAZOLE_END_CAUSE, arv.getID(), cotrimoxazo.getCotrimoxazoleEndCauses());
        }

        //Cập nhật lao
        List<OpcTestEntity> laos = testRepository.findLaoByArvID(arv.getID());
        opcTestService.getParameters(laos);
        if (laos != null && !laos.isEmpty()) {
            OpcTestEntity lao = laos.get(laos.size() - 1);
            arv.setLao(lao.isLao());
            arv.setLaoOtherSymptom(lao.getLaoOtherSymptom());
            arv.setLaoTestTime(lao.getLaoTestTime());
            arv.setLaoResult(lao.getLaoResult());

            arv.setSuspiciousSymptoms(lao.getSuspiciousSymptoms());
            arv.setExaminationAndTest(lao.isExaminationAndTest());
            arv.setLaoTestDate(lao.getLaoTestDate());
            arv.setLaoDiagnose(lao.getLaoDiagnose());

            //Chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.LAO_SYMPTOM, arv.getID(), lao.getLaoSymptoms());
        }

        //Cập nhật ngày điều trị lao
        List<OpcTestEntity> laoTreatments = testRepository.findLaoTreatmentByArvID(arv.getID());
        if (laoTreatments != null && !laoTreatments.isEmpty()) {
            OpcTestEntity laoTreatment = laoTreatments.get(laoTreatments.size() - 1);
            arv.setLaoTreatment(laoTreatment.isLaoTreatment());
            arv.setLaoStartTime(laoTreatment.getLaoStartTime());
            arv.setLaoEndTime(laoTreatment.getLaoEndTime());
        }

        //Điều trị INH
        List<OpcTestEntity> inhs = testRepository.findInhByArvID(arv.getID());
        opcTestService.getParameters(inhs);
        if (inhs != null && inhs.size() >= 1) {
            OpcTestEntity inh = inhs.get(inhs.size() - 1);
            arv.setInh(inh.isInh());
            arv.setInhFromTime(inh.getInhFromTime());
            arv.setInhToTime(inh.getInhToTime());

            //Chọn nhiều
            updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.INH_END_CAUSE, arv.getID(), inh.getInhEndCauses());
        }

        //Cập nhật hbv
        List<OpcTestEntity> hbvs = testRepository.findHbvByArvID(arv.getID());
        opcTestService.getParameters(hbvs);
        if (hbvs != null && hbvs.size() >= 1) {
            OpcTestEntity hbv = hbvs.get(hbvs.size() - 1);
            arv.setHbv(hbv.isHbv());
            arv.setHbvCase(hbv.getHbvCase());
            arv.setHbvResult(hbv.getHbvResult());
            arv.setHbvTime(hbv.getHbvTime());
        }

        //Cập nhật hcv
        List<OpcTestEntity> hcvs = testRepository.findHcvByArvID(arv.getID());
        opcTestService.getParameters(hcvs);
        if (hcvs != null && hcvs.size() >= 1) {
            OpcTestEntity hcv = hcvs.get(hcvs.size() - 1);
            arv.setHcv(hcv.isHcv());
            arv.setHcvCase(hcv.getHcvCase());
            arv.setHcvResult(hcv.getHcvResult());
            arv.setHcvTime(hcv.getHcvTime());
        }

        if (arv.getTransferTimeGSPH() != null) {
            arv.setUpdateTimeGSPH(new Date());
        }
        arvRepository.save(arv);
        logArv(arv.getID(), arv.getPatientID(), "Cập nhật bệnh án từ xét nghiệm:" + arv.changeToString(oldArv, true));
    }

    /**
     * Cập nhật bệnh án khi thay đổi thông tin TLVR
     *
     * @auth vvThành
     * @param arv
     * @throws Exception
     */
    public void updateArvFromViralLoad(OpcArvEntity arv) throws Exception {
        List<OpcViralLoadEntity> viralLoads = viralLoadRepository.findByArvID(arv.getID(), false);
        if (viralLoads == null || viralLoads.isEmpty()) {
            return;
        }

        viralLoadService.getParameters(viralLoads);

        OpcArvEntity oldArv = arv.clone();
        OpcViralLoadEntity lastArv = viralLoads.get(0);

//        arv.setFirstViralLoadTime(firstArv.getTestTime());
//        arv.setFirstViralLoadResult(firstArv.getResult());
        arv.setViralLoadTime(lastArv.getTestTime());
        arv.setViralLoadResult(lastArv.getResult());
        arv.setViralLoadRetryTime(lastArv.getRetryTime());
        arv.setViralLoadResultNumber(lastArv.getResultNumber());

        //Chọn nhiều
//        updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.FIRST_VIRAL_LOAD_CAUSE, arv.getID(), firstArv.getCauses());
        updateToParameter(OpcParameterTypeEnum.ARV, OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE, arv.getID(), lastArv.getCauses());

        if (arv.getTransferTimeGSPH() != null) {
            arv.setUpdateTimeGSPH(new Date());
        }
        arvRepository.save(arv);
        logArv(arv.getID(), arv.getPatientID(), "Cập nhật bệnh án từ tải lượng virus:" + arv.changeToString(oldArv, true));
    }

    /**
     * Lấy danh sách thông tin cơ bản bệnh nhân
     *
     * @auth vvThành
     * @param patients
     * @return
     */
    public Map<Long, OpcPatientEntity> getPatients(Set<Long> patients) {
        List<OpcPatientEntity> items = patientRepository.findByIds(patients);
        Map<Long, OpcPatientEntity> result = new HashMap<>();
        if (items == null || items.isEmpty()) {
            return result;
        }
        for (OpcPatientEntity item : items) {
            result.put(item.getID(), item);
        }
        return result;
    }

    /**
     * Cập nhật trạng thái biến động điều trị
     *
     * @auth vvThành
     * @param arv
     */
    public void updateToTreatmentStage(OpcStageEntity arv) {
        String startCase = arv.getRegistrationType(); //Lý do đăng ký
        String endCase = arv.getEndCase(); //Lý do kết thúc điều trị
        String oldTreatmentStage = arv.getTreatmentStageID();
        //Ưu tiên lý do kết thúc 
        if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
            //Chuyển đi
            arv.setTreatmentStageID(TreatmentStageEnum.MOVED_AWAY.getKey());
        } else if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.LOSE_TRACK.getKey())) {
            //Mất dấu
            arv.setTreatmentStageID(TreatmentStageEnum.LOSE_TRACK.getKey());
        } else if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.CANCELLED.getKey())) {
            //Bỏ trị
            arv.setTreatmentStageID(TreatmentStageEnum.CANCELLED.getKey());
        } else if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.DEAD.getKey())) {
            //Tử vong
            arv.setTreatmentStageID(TreatmentStageEnum.DEAD.getKey());
        } else if (StringUtils.isNotEmpty(endCase) && endCase.equals(ArvEndCaseEnum.END.getKey())) {
            //Kết thúc
            arv.setTreatmentStageID(TreatmentStageEnum.END.getKey());
        } else if (StringUtils.isNotEmpty(startCase) && startCase.equals(RegistrationTypeEnum.NEW.getKey())) {
            //Đăng ký mới
            arv.setTreatmentStageID(TreatmentStageEnum.NEW.getKey());
        } else if (StringUtils.isNotEmpty(startCase) && startCase.equals(RegistrationTypeEnum.REPLAY.getKey())) {
            //Điều trị lại
            arv.setTreatmentStageID(TreatmentStageEnum.REPLAY.getKey());
        } else if (StringUtils.isNotEmpty(startCase) && startCase.equals(RegistrationTypeEnum.TRANSFER.getKey())) {
            //Chuyến đến
            arv.setTreatmentStageID(TreatmentStageEnum.TRANSFER.getKey());
        } else if (StringUtils.isNotEmpty(startCase) && startCase.equals(RegistrationTypeEnum.CHILDREN_UNDER_18.getKey())) {
            //Trẻ em dưới 18 tuổi
            arv.setTreatmentStageID(TreatmentStageEnum.NEW.getKey());
        } else if (StringUtils.isNotEmpty(startCase) && startCase.equals(RegistrationTypeEnum.EXPOSURE.getKey())) {
            //Phơi nhiễm
            arv.setTreatmentStageID(TreatmentStageEnum.NEW.getKey());
        }

        //Set ngày biến động điều trị
        if (arv.getTreatmentStageTime() == null && StringUtils.isNotEmpty(oldTreatmentStage) && !oldTreatmentStage.equals(arv.getTreatmentStageID())) {
            arv.setTreatmentStageTime(new Date());
        }
    }

    /**
     * Cập nhật thông tin lượt khám gần nhất cho bệnh án và giai đoạn điều trị
     *
     * @param arv
     * @param stage Giai đoạn điều trị cập nhật, có thể null
     * @throws Exception
     */
    public void updateArvAndStageFromVisit(OpcArvEntity arv, OpcStageEntity stage) throws Exception {
        List<OpcVisitEntity> visits = visitRepository.findByArvID(arv.getID(), false);
        List<OpcStageEntity> stagelist = stageRepository.findByArvID(arv.getID(), false);

        //
        if (visits == null || visits.isEmpty()) {
            return;
        }
        OpcVisitEntity lastArv = visits.get(0);
        OpcArvEntity oldArv = arv.clone();

        if (stagelist != null && !stagelist.isEmpty()) {
            if (stage != null && Objects.equals(stagelist.get(stagelist.size() - 1).getID(), stage.getID())) {
                //
                if(StringUtils.isNotEmpty(lastArv.getInsuranceNo())){
                    arv.setInsurance(lastArv.getInsurance());
                    arv.setInsuranceExpiry(lastArv.getInsuranceExpiry());
                    arv.setInsuranceNo(lastArv.getInsuranceNo());
                    arv.setInsurancePay(lastArv.getInsurancePay());
                    arv.setInsuranceSite(lastArv.getInsuranceSite());
                    arv.setInsuranceType(lastArv.getInsuranceType());
                    arv.setRouteID(lastArv.getRouteID());
                }
                
                arv.setLastExaminationTime(lastArv.getExaminationTime());
                arv.setTreatmentRegimenStage(lastArv.getTreatmentRegimenStage());
                arv.setTreatmentRegimenID(lastArv.getTreatmentRegimenID());
                arv.setMedicationAdherence(lastArv.getMedicationAdherence());
                arv.setDaysReceived(lastArv.getDaysReceived());
                arv.setAppointmentTime(lastArv.getAppointmentTime());
                
                arv.setExaminationNote(lastArv.getNote());
                arv.setSupplyMedicinceDate(lastArv.getSupplyMedicinceDate());
                arv.setReceivedWardDate(lastArv.getReceivedWardDate());
                arv.setObjectGroupID(lastArv.getObjectGroupID());
                arv.setPregnantStartDate(lastArv.getPregnantStartDate());
                arv.setPregnantEndDate(lastArv.getPregnantEndDate());
                if (lastArv.getPatientHeight() != 0.0) {
                    arv.setPatientHeight(lastArv.getPatientHeight());
                }
                if (lastArv.getPatientWeight() != 0.0) {
                    arv.setPatientWeight(lastArv.getPatientWeight());
                }
            }
        }

        if (arv.getTransferTimeGSPH() != null) {
            arv.setUpdateTimeGSPH(new Date());
        }
        arvRepository.save(arv);
        logArv(arv.getID(), arv.getPatientID(), "Cập nhật bệnh án từ lượt khám:" + arv.changeToString(oldArv, true));

        //Cập nhật lại giai đoạn điều trị Cuối cùng đang diễn ra
        if (stage == null) {
            List<OpcStageEntity> stages = stageRepository.findByArvID(arv.getID(), false);
            if (stages != null && !stages.isEmpty()) {
                stage = stages.get(stages.size() - 1);
            }
        }
        //Còn đang diễn ra
        if (stage != null) {
            OpcStageEntity oldStage = stage.clone();
            if (stage.getEndTime() == null) {
                stage.setTreatmentRegimenStage(lastArv.getTreatmentRegimenStage());
                stage.setTreatmentRegimenID(lastArv.getTreatmentRegimenID());
                stage.setLastExaminationTime(lastArv.getExaminationTime());
                stage.setMedicationAdherence(lastArv.getMedicationAdherence());
                stage.setDaysReceived(lastArv.getDaysReceived());
                stage.setAppointmentTime(lastArv.getAppointmentTime());
                stage.setExaminationNote(lastArv.getNote());
                stage.setSupplyMedicinceDate(lastArv.getSupplyMedicinceDate());
                stage.setReceivedWardDate(lastArv.getReceivedWardDate());
                stage.setObjectGroupID(lastArv.getObjectGroupID());
                stage.setPregnantStartDate(lastArv.getPregnantStartDate());
                stage.setPregnantEndDate(lastArv.getPregnantEndDate());
                stage.setRegimenDate(lastArv.getRegimenDate());
                stage.setRegimenStageDate(lastArv.getRegimenStageDate());
                stage.setCausesChange(lastArv.getCausesChange());
                stage.setOldTreatmentRegimenID(lastArv.getOldTreatmentRegimenID());
                stage.setOldTreatmentRegimenStage(lastArv.getOldTreatmentRegimenStage());
                stageRepository.save(stage);
                trackChangeStage(oldStage, stage);
                logStage(arv.getID(), arv.getPatientID(), stage.getID(), "Cập nhật bệnh án từ lượt khám:" + stage.changeToString(oldStage, true));
            }
        }
    }

    /**
     * Kiểm tra giai đoạn điều trị có phù hợp không
     *
     * @param stage
     * @return
     * @throws java.text.ParseException
     */
    public boolean validateStage(OpcStageEntity stage) throws ParseException {
        List<OpcStageEntity> stages = stageRepository.findByPatientID(stage.getPatientID(), false);
        if (stages == null || stages.isEmpty()) {
            return true;
        }

        List<OpcStageEntity> stageChecked = new ArrayList<>();
        for (OpcStageEntity elm : stages) {
            if (elm.getID().equals(stage.getID())
                    || elm.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
                continue;
            }
            stageChecked.add(elm);
        }

        if (stageChecked.isEmpty()) {
            return true;
        }

        List<Date> startDates = new ArrayList<>();
        List<Date> endDates = new ArrayList<>();

        startDates.addAll(CollectionUtils.collect(stageChecked, TransformerUtils.invokerTransformer("getRegistrationTime")));
        endDates.addAll(CollectionUtils.collect(stageChecked, TransformerUtils.invokerTransformer("getEndTime")));
        Collections.reverse(startDates);
        Collections.reverse(endDates);
        Map<String, List<Date>> stageListOne = new HashMap<>(); // Thêm mới hoặc sửa chỉ có 1 ngày điều trị hoặc kết thúc
        List<Date> listDate;

        // Lấy ds các khoảng thời gian hợp lệ cho thêm mới
        if (!startDates.isEmpty() && startDates.size() > 1) {
            for (int i = 0; i < startDates.size() - 1; i++) {

                if (endDates.get(i) == null || startDates.get(i + 1) == null) {
                    continue;
                }

                listDate = new ArrayList<>();
                listDate.add(endDates.get(i));
                listDate.add(startDates.get(i + 1));
                stageListOne.put(String.valueOf(i), listDate);
            }
        }

        Date maxDate = new Date((stageChecked.get(0).getEndTime() == null ? stageChecked.get(0).getRegistrationTime() : stageChecked.get(0).getEndTime()).getTime());
        Date minDate = new Date((stageChecked.get(stageChecked.size() - 1).getRegistrationTime() != null ? stageChecked.get(stageChecked.size() - 1).getRegistrationTime() : stageChecked.get(stageChecked.size() - 1).getEndTime()).getTime());
        Date lastEndDate = stageChecked.get(0).getEndTime() != null ? new Date((stageChecked.get(0).getEndTime()).getTime()) : null;
        // Convert to 00h timestamp
        maxDate = TextUtils.getDateWithoutTime(maxDate);
        minDate = TextUtils.getDateWithoutTime(minDate);
        lastEndDate = lastEndDate == null ? null : TextUtils.getDateWithoutTime(lastEndDate);

        // Không validate Chưa có thông tin
        if (StringUtils.isNotEmpty(stage.getStatusOfTreatmentID()) && stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
            return true;
        }

        // Thêm mới hoặc cập nhật trong quá khứ phải có ngày kết thúc
        if (stage.getRegistrationTime() != null && stage.getEndTime() == null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(maxDate) < 0) {
            return false;
        }

        // TH thêm mới
        if (stage.getID() == null) {
            // Đang hoạt động không được thêm mới trong tương lai
            if (lastEndDate == null && ((stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(maxDate) > 0) || (stage.getRegistrationTime() == null && stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) > 0))) {
                return false;
            }

            // Kiểm tra 1 ds không có 2 giai đoạn trống ngày kết thúc
            int count = 0;
            if (stage.getEndTime() == null && StringUtils.isNotEmpty(stage.getStatusOfTreatmentID()) && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
                count++;
                for (OpcStageEntity stageEntity : stageChecked) {
                    if (stageEntity.getEndTime() == null) {
                        count++;
                    }
                    if (count > 1) {
                        return false;
                    }
                }
            }

            // Ngày lớn nhất gồm có 1 trong 2 hoặc cả 2 ngày điều trị và kết thúc Có 1 trong 2 ngày
            if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(maxDate) >= 0) {
                return true;
            }
            if (stage.getRegistrationTime() == null && stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) > 0) {
                return true;
            }

            // Thêm vào trước ngày đầu tiên trong quá khứ
            if (stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(minDate) <= 0) {
                return true;
            }

            // Thêm mới khoảng giữa
            if (stage.getEndTime() != null && startDates.size() == 1 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(minDate) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) < 0) {
                return false;
            }

            if (stage.getEndTime() != null && startDates.size() > 1 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(minDate) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) < 0) {
                boolean result = false;

                if (stageListOne.entrySet().isEmpty()) {
                    return true;
                }
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }

            // Cập nhật giai đoạn đầu hoặc cuối có ngày kết thúc hoặc bắt đầu điều trị nằm trong các khoảng 
            // 12/04/2020 - 20/04/2020  << invalid
            // 12/03/2020 - 15/04/2020
            if (stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) >= 0) {
                boolean result = false;
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }

            if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(minDate) < 0) {
                boolean result = false;
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }

        }

        // TH Cập nhật
        if (stage.getID() != null) {

            // Kiểm tra 1 ds không có 2 giai đoạn trống ngày kết thúc
            int count = 0;
            if (stage.getEndTime() == null && StringUtils.isNotEmpty(stage.getStatusOfTreatmentID()) && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
                count++;
                for (OpcStageEntity stageEntity : stageChecked) {
                    if (stageEntity.getEndTime() == null) {
                        count++;
                    }
                    if (count > 1) {
                        return false;
                    }
                }
            }

            // Cập nhật bản ghi cuối ds ( ngày đăng ký sớm nhất)
            if (stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(minDate) <= 0) {
                return true;
            }

            // Cập nhật bản ghi đầu ds
            if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(maxDate) >= 0) {
                return true;
            }

            if (stage.getEndTime() != null && stage.getRegistrationTime() == null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) > 0) {
                return true;
            }

            // Cập nhật giai đoạn giữa
            if (stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(minDate) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) < 0) {
                boolean result = false;
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }

            // Cập nhật giai đoạn đầu hoặc cuối có ngày kết thúc hoặc bắt đầu điều trị nằm trong các khoảng 
            // 12/04/2020 - 20/04/2020  << invalid
            // 12/03/2020 - 15/04/2020
            if (stage.getEndTime() != null && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(maxDate) >= 0) {
                boolean result = false;
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }

            if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(minDate) < 0) {
                boolean result = false;
                for (Map.Entry<String, List<Date>> entry : stageListOne.entrySet()) {
                    if (stage.getRegistrationTime() != null && TextUtils.getDateWithoutTime(stage.getRegistrationTime()).compareTo(entry.getValue().get(0)) >= 0 && TextUtils.getDateWithoutTime(stage.getEndTime()).compareTo(entry.getValue().get(1)) <= 0) {
                        result = true;
                    }
                }
                return result;
            }
        }
        return true;
    }

    /**
     * Lưu lịch sử thay đổi điều trị
     *
     * @param oldItem
     * @param newItem
     * @throws CloneNotSupportedException
     */
    protected void trackChangeStage(OpcStageEntity oldItem, OpcStageEntity newItem) throws CloneNotSupportedException {
        
        // Kiểm tra nếu cập nhật giai đoạn trong quá khứ
        boolean updatePast = false;
        OpcStageEntity lastStage = new OpcStageEntity();
        try {
            List<OpcStageEntity> stages = stageRepository.findByArvIDOrderRegis(newItem.getArvID(), false);
            lastStage = stages.get(0);
            updatePast = newItem.getRegistrationTime().compareTo(lastStage.getRegistrationTime()) < 0;
        } catch (Exception e) {
        }
        
        OpcStageEntity model = (OpcStageEntity) newItem.clone();
        if (oldItem == null || !oldItem.equals(model)) {
            Date currentDate = new Date();
            Long currentUserID = getCurrentUserID();
            OpcArvRevisionEntity log = new OpcArvRevisionEntity();
            log.setCreateAT(oldItem == null ? newItem.getRegistrationTime() : updatePast && newItem.getEndTime() != null ? newItem.getEndTime() : updatePast && newItem.getEndTime() == null ? newItem.getRegistrationTime() : currentDate);
            log.setCreatedBY(currentUserID);
            log.setUpdateAt(currentDate);
            log.setUpdatedBY(currentUserID);
            log.setArvID(newItem.getArvID());
            log.setPatientID(newItem.getPatientID());
            log.setSiteID(newItem.getSiteID());
            log.setStageID(model.getID());
            log.setRegistrationTime(newItem.getRegistrationTime());
            log.setTreatmentTime(newItem.getTreatmentTime());
            log.setEndTime(newItem.getEndTime());
            log.setPregnantStartDate(newItem.getPregnantStartDate());
            log.setPregnantEndDate(newItem.getPregnantEndDate());
            log.setTranferFromTime(newItem.getTranferFromTime());
            log.setPatientID(newItem.getPatientID());
            log.setRegistrationType(newItem.getRegistrationType());
            log.setStatusOfTreatmentID(newItem.getStatusOfTreatmentID());
            log.setTreatmentStageID(newItem.getTreatmentStageID());
            log.setTreatmentRegimenStage(newItem.getTreatmentRegimenStage());
            log.setTreatmentRegimenID(newItem.getTreatmentRegimenID());
            log.setEndCase(newItem.getEndCase());
            log.setObjectGroupID(newItem.getObjectGroupID());
            log.setDaysReceived(newItem.getDaysReceived());
            log.setReceivedWardDate(newItem.getReceivedWardDate());
            log.setRemove(newItem.isRemove());
            log.setSupplyMedicinceDate(newItem.getSupplyMedicinceDate());
            log.setRegimenDate(newItem.getRegimenDate());

            revisionRepository.save(log);
        }
    }
}
