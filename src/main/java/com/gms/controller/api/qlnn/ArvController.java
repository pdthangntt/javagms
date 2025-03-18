package com.gms.controller.api.qlnn;

import com.gms.entity.bean.Response;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.VirusLoadResultEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.components.TextUtils;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.PacPatientService;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "qlnn_arv_api")
public class ArvController extends BaseController {

    @Autowired
    private PacPatientService patientService;

    private Map<String, Map<Long, String>> getSiteOptions(String provinceID) {
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

        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(provinceID);
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(site.getID(), site.getName());
        }
        return options;
    }

    private HashMap<String, HashMap<String, String>> buildFinalParameter() {
        HashMap<String, HashMap<String, String>> dataModels = new LinkedHashMap<>();
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);

        Map<String, String> finalParameter = new LinkedHashMap<>();
        finalParameter = new LinkedHashMap<>();

        finalParameter.put(ParameterEntity.BLOOD_BASE, "Nơi lấy máu");
        finalParameter.put(ParameterEntity.TREATMENT_FACILITY, "Cơ sở điều trị");
        finalParameter.put(ParameterEntity.PLACE_TEST, "Nơi xét nghiệm");

        for (Map.Entry<String, String> entry : finalParameter.entrySet()) {
            String key = entry.getKey();
            if (dataModels.get(key) == null) {
                dataModels.put(key, new HashMap<String, String>());
                dataModels.get(key).put("title", "");
                dataModels.get(key).put("icon", "");
                dataModels.get(key).put("description", "");
                dataModels.get(key).put("hivinfo", "");
                dataModels.get(key).put("elog", "");
                dataModels.get(key).put("sitemap", "");
                dataModels.get(key).put("useparent", "");
            }
        }

        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2) {
                continue;
            }
            if (dataModels.get(splitCode[0]) == null) {
                continue;
            }
            dataModels.get(splitCode[0]).put(splitCode[1], parameter.getValue());
        }
        return dataModels;
    }

    public ParameterEntity addConfig(String type, SiteEntity site) {
        HashMap<String, String> buildTypeParameter = buildFinalParameter().get(type);

        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            return null;
        }

        try {
            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setType(type);
            parameterEntity.setStatus(BooleanEnum.TRUE.getKey());
            parameterEntity.setCode(String.format("s-%s", site.getID()));
            parameterEntity.setPosition(1);
            parameterEntity.setParentID(0L);
            parameterEntity.setSiteID(site.getID());
            parameterEntity.setValue(site.getName());

            parameterEntity = parameterService.save(parameterEntity);
            if (parameterEntity == null) {
                throw new Exception("Thêm dữ liệu tham số thất bại!");
            }
            return parameterEntity;
        } catch (Exception e) {
            return null;
        }
    }

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
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        if (currentSite != null) {
            List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentSite.getProvinceID());

            for (SiteEntity site : siteConfirm) {
                options.get(key).put(site.getID(), site.getName());
            }
        }

        return options;
    }

    @RequestMapping(value = "/v1/pac-arv.api", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum, @RequestBody String content) {
        try {
            String json = new String(Base64.getDecoder().decode(content.trim()));
            OpcArvEntity arv = gson.fromJson(json, OpcArvEntity.class);
            if (!checksum(checksum, String.valueOf(arv.getID()), String.valueOf(arv.getSiteID()))) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat}/{ma_confirm}/{ma_site})");
            }

            SiteEntity site = siteService.findOne(arv.getSiteID());
            if (site == null) {
                throw new Exception("Không tìm thấy cơ sở chuyển gửi");
            }

            List<PacPatientInfoEntity> ppies = patientService.findByTransfer(arv.getSiteID(), arv.getID(), ServiceEnum.OPC.getKey());
            if (ppies != null && !ppies.isEmpty()) {
                String body = Base64.getEncoder().encodeToString(gson.toJson(ppies.get(0)).getBytes());
                return new Response<>(true, "Ca điều trị " + arv.getCode() + " đã có trên hệ thống", body);
            }

            Map<String, Map<Long, String>> options = getSiteOptions(site.getProvinceID());
            Long confirmSiteID = arv.getPatient().getConfirmSiteID() == null ? Long.valueOf("-9") : arv.getPatient().getConfirmSiteID();
            SiteEntity confirmSite = siteService.findOne(confirmSiteID);

            PacPatientInfoEntity patient = new PacPatientInfoEntity();
            String confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
            String treatmentSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(arv.getSiteID() + "", null);

            // Kiểm tra mapping thông tin tham số bên quản lý người nhiễm
            StringBuilder errorMsg = new StringBuilder();
            String errorFlg = "";
            errorMsg.append(String.format("Chuyển gửi không thành công. Cơ sở [%s] chưa cấu hình các thông tin quản lý người nhiễm dưới đây: ", siteService.findOne(arv.getSiteID()).getName()));

            if (StringUtils.isEmpty(confirmTestSite) && confirmSiteID != null && !confirmSiteID.equals(-1L)) {
                if (arv.getSiteID() != null && confirmSite != null) {
                    addConfig(ParameterEntity.PLACE_TEST, confirmSite);
                    options = getSiteOptionsConfig(confirmSite);
                    confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
                }
            }

            if (StringUtils.isEmpty(treatmentSite) && arv.getSiteID() != null) {
                addConfig(ParameterEntity.TREATMENT_FACILITY, site);
                options = getSiteOptionsConfig(confirmSite);
                confirmTestSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(confirmSiteID, null);
            }

            OpcPatientEntity patientSource = arv.getPatient();

            patient.setOpcCode(arv.getCode());
            patient.setRaceID(patientSource.getRaceID());
            patient.setJobID(arv.getJobID());
            patient.setHealthInsuranceNo(arv.getInsuranceNo());
            patient.setHasHealthInsurance(arv.getInsurance());

//        patient.setBloodBaseID(bloodBase);
            patient.setSiteConfirmID(patientSource.getConfirmSiteID() == null ? "" : patientSource.getConfirmSiteID() == -1 ? "-1" : confirmTestSite);
            patient.setYearOfBirth(patientSource.getDob() != null ? Integer.parseInt(TextUtils.formatDate(patientSource.getDob(), "YYYY")) : null);
            patient.setStatusOfTreatmentID(arv.getStatusOfTreatmentID());
            patient.setTreatmentRegimenID(arv.getTreatmentRegimenID());
            patient.setCdFourResultLastestDate(arv.getCd4Time());
            patient.setCdFourResultDate(arv.getFirstCd4Time());
            patient.setStartTreatmentTime(arv.getTreatmentTime());
            patient.setSiteTreatmentFacilityID(treatmentSite);
            patient.setChangeTreatmentDate(arv.getTreatmentStageTime());
            patient.setVirusLoadArv("6".equals(arv.getFirstViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getFirstViralLoadResult());
            patient.setVirusLoadArvDate(arv.getFirstViralLoadTime());
            patient.setVirusLoadArvCurrent("6".equals(arv.getViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getViralLoadResult());
            patient.setVirusLoadArvLastestDate(arv.getViralLoadTime());
            patient.setClinicalStage(arv.getClinicalStage());
            patient.setStatusOfChangeTreatmentID(arv.getTreatmentStageID());

            patient.setFullname(patientSource.getFullName());
            patient.setGenderID(patientSource.getGenderID());
            patient.setIdentityCard(patientSource.getIdentityCard());
            patient.setObjectGroupID(arv.getObjectGroupID());

            patient.setPermanentAddressNo(arv.getPermanentAddress());
            patient.setPermanentAddressStreet(arv.getPermanentAddressStreet());
            patient.setPermanentAddressGroup(arv.getPermanentAddressGroup());
            patient.setPermanentProvinceID(arv.getPermanentProvinceID());
            patient.setPermanentDistrictID(arv.getPermanentDistrictID());
            patient.setPermanentWardID(arv.getPermanentWardID());
            patient.setChangeTreatmentDate(arv.getTreatmentStageTime());

            patient.setCurrentAddressNo(arv.getCurrentAddress());
            patient.setCurrentAddressStreet(arv.getCurrentAddressStreet());
            patient.setCurrentAddressGroup(arv.getCurrentAddressGroup());
            patient.setCurrentProvinceID(arv.getCurrentProvinceID());
            patient.setCurrentDistrictID(arv.getCurrentDistrictID());
            patient.setCurrentWardID(arv.getCurrentWardID());

            patient.setSourceServiceID(ServiceEnum.OPC.getKey());
            patient.setSourceSiteID(patientSource.getSiteID());
            patient.setSourceID(arv.getID());
            patient.setDetectProvinceID(site.getProvinceID());
            patient.setDetectDistrictID(site.getDistrictID());
            patient.setProvinceID(site.getProvinceID());
            patient.setDistrictID(site.getDistrictID());
            patient.setConfirmTime(patientSource.getConfirmTime() == null ? null : patientSource.getConfirmTime());
            patient.setConfirmCode(patientSource.getConfirmCode());

            patient.setPatientPhone(arv.getPatientPhone());
            patient.setDeathTime(arv.getDeathTime());
            if (patient.getRequestDeathTime() == null) {
                patient.setRequestDeathTime(arv.getDeathTime());
            }
            try {
                patient.setCdFourResult(Long.parseLong(arv.getFirstCd4Result()));
            } catch (NumberFormatException e) {
            }
            try {
                patient.setCdFourResultCurrent(Long.parseLong(arv.getCd4Result()));
            } catch (NumberFormatException e) {
            }

            //Bổ sung
            patient.setRegistrationTime(arv.getRegistrationTime());
            patient.setRegistrationType(arv.getRegistrationType());
            patient.setFirstTreatmentTime(arv.getFirstTreatmentTime());
            patient.setFirstTreatmentRegimenId(arv.getFirstTreatmentRegimenID());
            patient.setEndTime(arv.getEndTime());
            patient.setEndCase(arv.getEndCase());
            
            patientService.save(patient);
            patientService.log(patient.getID(), "Thêm mới từ chuyển gửi GSPH điều trị");

            String body = Base64.getEncoder().encodeToString(gson.toJson(patient).getBytes());
            return new Response<>(true, "Chuyển sang báo cáo giám sát phát hiện thành công", body);
        } catch (Exception e) {
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

}
