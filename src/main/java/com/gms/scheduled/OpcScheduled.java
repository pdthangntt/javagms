package com.gms.scheduled;

import com.gms.components.QLNNUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.VirusLoadResultEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.CommonService;
import com.gms.service.OpcArvService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class OpcScheduled extends BaseScheduled {

    @Autowired
    private PacPatientService patientService;
    @Autowired
    private ParameterService paramService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private QLNNUtils qlnnUtils;

    //Tự động chuyển lên Biến động điều trị
    @Async
//    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
    public void actionTransferPac() {
        List<OpcArvEntity> items = opcArvService.getNoForPac(10);
        if (items == null || items.isEmpty()) {
            getLogger().info("[OPC] OPC is empty");
            return;
        }
        for (OpcArvEntity arv : items) {
            if (arv.getTransferTimeGSPH() != null || arv.getStatusOfTreatmentID().equals("0")) {
                continue;
            }
            try {
                getLogger().info("Arvc id " + arv.getID() + " code " + arv.getCode());
                PacPatientInfoEntity patient = qlnnUtils.arv(arv);
                arv.setTransferTimeGSPH(new Date());
                arv.setUpdateTimeGSPH(new Date());
                arv.setGsphID(patient.getID());
                opcArvService.updateJob(arv);
                opcArvService.logArv(arv.getID(), arv.getPatientID(), "Chuyển gửi GSPH");
            } catch (Exception e) {
                getLogger().error(e.getMessage());
            }
        }
    }

    /**
     * Cập nhật thông tin từ OPC sang Quản lý nếu có thay đổi sau khi chuyển gửi
     *
     * @auth TrangBN
     * @job 1hour 60 * 60 * 1000
     */
    @Async
//    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
//    @Scheduled(cron = "*/10 * * * * ?")
    public void actionUpdateOpcToPac() {
        List<OpcArvEntity> opcItems = opcArvService.getForUpdateOpcToPac(51);
        if (opcItems == null || opcItems.isEmpty()) {
            getLogger().debug("[OPC] OPC is empty");
            return;
        }

        for (OpcArvEntity opcItem : opcItems) {
            opcItem.setUpdateTimeGSPH(null);
            opcItem.setUpdateAt(new Date());

            List<PacPatientInfoEntity> patientInfo = patientService.findBySourceIDSourceService(opcItem.getID(), ServiceEnum.OPC.getKey());
            if (patientInfo == null || patientInfo.isEmpty()) {
                getLogger().debug("[PAC] OPC is empty");
                continue;
            }
            PacPatientInfoEntity patient = setOpcToPac(opcItem, patientInfo.get(0));
            try {
                patientService.save(patient);
                patientService.log(patient.getID(), "Cập nhật thông tin điều trị");
            } catch (Exception e) {
                e.printStackTrace();
                patientService.log(patient.getID(), "Lỗi lấy thông tin điều trị. Error: " + e.getMessage());
            }
        }
        opcArvService.saveAll(opcItems);
    }

    /**
     * Set dữ liệu cần từ OPC sang PAC
     *
     * @param arv
     * @return
     */
    private PacPatientInfoEntity setOpcToPac(OpcArvEntity arv, PacPatientInfoEntity patient) {

        Map<String, Map<Long, String>> options = getSiteOptions(arv);
        Long confirmSiteID = arv.getPatient().getConfirmSiteID();
        SiteEntity confirmSite = confirmSiteID == null ? null : siteService.findOne(confirmSiteID);
//        SiteEntity siteArv = siteService.findOne(arv.getSiteID());
        SiteEntity site = siteService.findOne(arv.getSiteID());
        String confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
        String treatmentSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(arv.getSiteID(), null);

        if (StringUtils.isEmpty(confirmTestSite) && confirmSiteID != null && !confirmSiteID.equals(-1L)) {
            if (arv.getSiteID() != null && confirmSite != null) {
                commonService.addConfig(ParameterEntity.PLACE_TEST, confirmSite);
                options = getSiteOptionsConfig(confirmSite);
                confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
            }
        }

        if (StringUtils.isEmpty(treatmentSite) && arv.getSiteID() != null) {
            commonService.addConfig(ParameterEntity.TREATMENT_FACILITY, site);
            options = getSiteOptionsConfig(confirmSite);
            confirmTestSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(confirmSiteID, null);
        }

        OpcPatientEntity patientSource = arv.getPatient();

        // Bản ghi chuyển trực tiếp từ biến động điều trị sang quản lý chỉ cập nhật thông tin điều trị
        if (patient.getReviewProvinceTime() == null && StringUtils.isNotEmpty(patient.getSourceServiceID()) && patient.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) {
            // Thông tin chung, không phải thông tin điều trị
            patient.setRaceID(patientSource.getRaceID());
            patient.setFullname(patientSource.getFullName());
            patient.setJobID(arv.getJobID());
            patient.setHealthInsuranceNo(arv.getInsuranceNo());
            patient.setHasHealthInsurance(arv.getInsurance());
            patient.setSiteConfirmID(patientSource.getConfirmSiteID() != null ? patientSource.getConfirmSiteID() == -1 ? patientSource.getConfirmSiteName() : confirmTestSite : null);
            patient.setYearOfBirth(patientSource.getDob() != null ? Integer.parseInt(TextUtils.formatDate(patientSource.getDob(), "YYYY")) : null);
            patient.setGenderID(patientSource.getGenderID());
            patient.setIdentityCard(patientSource.getIdentityCard());
            patient.setObjectGroupID(arv.getObjectGroupID());
            patient.setPermanentAddressNo(arv.getPermanentAddress());
            patient.setPermanentAddressGroup(arv.getPermanentAddressGroup());
            patient.setPermanentAddressStreet(arv.getPermanentAddressStreet());
            patient.setPermanentProvinceID(arv.getPermanentProvinceID());
            patient.setPermanentDistrictID(arv.getPermanentDistrictID());
            patient.setPermanentWardID(arv.getPermanentWardID());
            patient.setCurrentAddressNo(arv.getCurrentAddress());
            patient.setCurrentAddressGroup(arv.getCurrentAddressGroup());
            patient.setCurrentAddressStreet(arv.getCurrentAddressStreet());
            patient.setCurrentProvinceID(arv.getCurrentProvinceID());
            patient.setCurrentDistrictID(arv.getCurrentDistrictID());
            patient.setCurrentWardID(arv.getCurrentWardID());
            patient.setConfirmTime(patientSource.getConfirmTime() == null ? null : patientSource.getConfirmTime());
            patient.setConfirmCode(patientSource.getConfirmCode());
            patient.setPatientPhone(arv.getPatientPhone());
        }

        // Thông tin điều trị
        patient.setStartTreatmentTime(arv.getTreatmentTime());
        patient.setStatusOfTreatmentID(arv.getStatusOfTreatmentID());
        patient.setSiteTreatmentFacilityID(treatmentSite);
        patient.setOpcCode(arv.getCode());
        patient.setCdFourResultDate(arv.getFirstCd4Time());
        patient.setCdFourResultLastestDate(arv.getCd4Time());
        patient.setVirusLoadArv("6".equals(arv.getFirstViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getFirstViralLoadResult());
        patient.setVirusLoadArvDate(arv.getFirstViralLoadTime());
        patient.setVirusLoadArvCurrent("6".equals(arv.getViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getViralLoadResult());
        patient.setVirusLoadArvLastestDate(arv.getViralLoadTime());
        patient.setClinicalStage(arv.getClinicalStage());
        patient.setTreatmentRegimenID(arv.getTreatmentRegimenID());
        patient.setStatusOfChangeTreatmentID(arv.getTreatmentStageID());
        patient.setChangeTreatmentDate(arv.getTreatmentStageTime());
        patient.setDeathTime(arv.getDeathTime());
        patient.setRequestDeathTime(arv.getDeathTime());

        patient.setRegistrationTime(arv.getRegistrationTime());
        patient.setRegistrationType(arv.getRegistrationType());
        patient.setFirstTreatmentTime(arv.getFirstTreatmentTime());
        patient.setFirstTreatmentRegimenId(arv.getFirstTreatmentRegimenID());
        patient.setEndTime(arv.getEndTime());
        patient.setEndCase(arv.getEndCase());

        try {
            patient.setCdFourResult(Long.parseLong(arv.getFirstCd4Result()));
        } catch (NumberFormatException e) {
        }
        try {
            patient.setCdFourResultCurrent(Long.parseLong(arv.getCd4Result()));
        } catch (NumberFormatException e) {
        }

        if (StringUtils.isEmpty(patient.getFullname())) {
            List<PacPatientInfoEntity> patientDecode = patientService.getMetaData(Arrays.asList(patient));
            patient = patientDecode.get(0);
        }

        return patient;
    }

    /**
     * Lấy thông tin mapping của cơ sở hiện tại với thông tin quản lý người
     * nhiẽm
     *
     * @auth TrangBN
     * @return
     */
    private Map<String, Map<Long, String>> getSiteOptions(OpcArvEntity arv) {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        Map<String, Map<Long, String>> options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<Long, String>());
            }
        }
        List<ParameterEntity> entities = paramService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getSiteID() == null || entity.getSiteID().equals(Long.valueOf("0"))) {
                continue;
            }
            options.get(entity.getType()).put(entity.getSiteID(), entity.getCode());
        }

        SiteEntity siteArv = siteService.findOne(arv.getSiteID());
        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(siteArv.getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(site.getID(), site.getName());
        }
        return options;
    }

    /**
     * Lấy thông tin mapping của cơ sở hiện tại với thông tin quản lý người
     * nhiễm
     *
     * @auth TrangBN
     * @return
     */
    private Map<String, Map<Long, String>> getSiteOptionsConfig(SiteEntity currentSite) {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        Map<String, Map<Long, String>> options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<Long, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getSiteID() == null || entity.getSiteID().equals(Long.valueOf("0"))) {
                continue;
            }
            options.get(entity.getType()).put(entity.getSiteID(), entity.getCode());
        }

        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentSite.getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(site.getID(), site.getName());
        }
        return options;
    }
}
