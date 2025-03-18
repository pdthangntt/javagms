package com.gms.controller.api.qlnn;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.PacPatientService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
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

@RestController
public class VisitController extends BaseController {

    @Autowired
    private PacPatientService patientService;

    private ParameterEntity addConfig(String type, SiteEntity site) {
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

    private Map<String, Map<Long, String>> getSiteOptions() {
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
        return options;
    }

    @RequestMapping(value = "/v1/pac-visit.api", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum, @RequestBody String content) {
        try {
            String json = new String(Base64.getDecoder().decode(content.trim()));
            HtcVisitEntity htc = gson.fromJson(json, HtcVisitEntity.class);
            if (!checksum(checksum, String.valueOf(htc.getID()), String.valueOf(htc.getSiteID()))) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat}/{ma_visit}/{ma_site})");
            }

            SiteEntity siteHtc = siteService.findOne(htc.getSiteID());
            if (siteHtc == null) {
                throw new Exception("Không tìm thấy cơ sở chuyển gửi");
            }

            Map<String, Map<Long, String>> options = getSiteOptions();
            Set<Long> siteIDs = new HashSet<>();
            siteIDs.addAll(new ArrayList<>(
                    Arrays.asList(htc.getSiteID(),
                            StringUtils.isNotEmpty(htc.getSiteConfirmTest()) ? Long.parseLong(htc.getSiteConfirmTest()) : null,
                            htc.getArrivalSiteID())));

            List<SiteEntity> findByIDs = siteService.findByIDs(siteIDs);
            HashMap<String, SiteEntity> sites = new HashMap<>();
            for (SiteEntity site : findByIDs) {
                sites.put(site.getID().toString(), site);
            }

            PacPatientInfoEntity patient = new PacPatientInfoEntity();
            String bloodBase = options.get(ParameterEntity.BLOOD_BASE).getOrDefault(htc.getSiteID(), null);
            String confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(StringUtils.isNotEmpty(htc.getSiteConfirmTest()) ? Long.parseLong(htc.getSiteConfirmTest()) : "", null);

            // Kiểm tra mapping thông tin tham số bên quản lý người nhiễm
            StringBuilder errorMsg = new StringBuilder();
            String errorFlg = "";
            errorMsg.append(String.format("Chuyển gửi không thành công. Cơ sở [%s] chưa cấu hình các thông tin quản lý người nhiễm dưới đây: ", siteService.findOne(htc.getSiteID()).getName()));

            ParameterEntity configBloodBase = new ParameterEntity();
            if (StringUtils.isEmpty(bloodBase)) {
                // Thêm cấu hình tự động
                configBloodBase = addConfig(ParameterEntity.BLOOD_BASE, sites.get(htc.getSiteID().toString()));
                if (configBloodBase == null) {
                    errorFlg = "a";
                    errorMsg.append("<br> - Nơi lấy mẫu xét nghiêm.");
                }
            }

            ParameterEntity configConfirmTestSite = new ParameterEntity();
            if (StringUtils.isEmpty(confirmTestSite)) {
                if (StringUtils.isNotEmpty(htc.getSiteConfirmTest())) {
                    // Thêm cấu hình tự động
                    configConfirmTestSite = addConfig(ParameterEntity.PLACE_TEST, sites.get(htc.getSiteConfirmTest()));
                    if (configConfirmTestSite == null) {
                        errorFlg = "a";
                        errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
                    }
                } else {
                    errorFlg = "a";
                    errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
                }
            }

            if (StringUtils.isNotEmpty(errorFlg)) {
                return new Response<>(false, errorMsg.toString(), "ERROR");
            }

            patient.setBloodBaseID(configBloodBase != null && StringUtils.isNotEmpty(configBloodBase.getCode()) ? configBloodBase.getCode() : bloodBase);
            patient.setSiteConfirmID(configConfirmTestSite != null && StringUtils.isNotEmpty(configConfirmTestSite.getCode()) ? configConfirmTestSite.getCode() : confirmTestSite);

            // Kiểm tra thông tin điều trị trước khi chuyển GSPH
            if (StringUtils.isNotEmpty(htc.getTherapyNo())) {
                patient.setSiteTreatmentFacilityID(options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(htc.getSiteID(), null));
            }

            try {
                patient.setYearOfBirth(StringUtils.isNotBlank(htc.getYearOfBirth()) ? Integer.valueOf(htc.getYearOfBirth()) : 0);
            } catch (NumberFormatException e) {
            }

            patient.setFullname(htc.getPatientName());
            patient.setGenderID(htc.getGenderID());
            patient.setRaceID(htc.getRaceID());
            patient.setJobID(htc.getJobID());
            patient.setHealthInsuranceNo(htc.getHealthInsuranceNo());
            patient.setHasHealthInsurance(htc.getHasHealthInsurance());
            patient.setIdentityCard(htc.getPatientID());
            patient.setObjectGroupID(htc.getObjectGroupID());
            patient.setModeOfTransmissionID(htc.getModeOfTransmission());
            patient.setRiskBehaviorID(htc.getRiskBehaviorID());
            patient.setPermanentAddressNo(htc.getPermanentAddress());
            patient.setPermanentAddressGroup(htc.getPermanentAddressGroup());
            patient.setPermanentAddressStreet(htc.getPermanentAddressStreet());
            patient.setPermanentProvinceID(htc.getPermanentProvinceID());
            patient.setPermanentDistrictID(htc.getPermanentDistrictID());
            patient.setPermanentWardID(htc.getPermanentWardID());
            patient.setSourceServiceID(ServiceEnum.HTC.getKey()); // Set nguồn sử dụngdiịch vụ là HTC
            patient.setSourceSiteID(htc.getSiteID());
            patient.setSourceID(htc.getID());
            patient.setCurrentAddressNo(htc.getCurrentAddress());
            patient.setCurrentAddressGroup(htc.getCurrentAddressGroup());
            patient.setCurrentAddressStreet(htc.getCurrentAddressStreet());
            patient.setCurrentProvinceID(htc.getCurrentProvinceID());
            patient.setCurrentDistrictID(htc.getCurrentDistrictID());
            patient.setCurrentWardID(htc.getCurrentWardID());

            patient.setDetectProvinceID(siteHtc.getProvinceID());
            patient.setDetectDistrictID(siteHtc.getDistrictID());
            patient.setProvinceID(siteHtc.getProvinceID());
            patient.setDistrictID(siteHtc.getDistrictID());

            patient.setConfirmTime(htc.getConfirmTime() == null ? null : htc.getConfirmTime());
            patient.setConfirmCode(htc.getConfirmTestNo());
            patient.setEarlyHiv(htc.getEarlyHiv());
            patient.setEarlyHivTime(htc.getEarlyHivDate() == null ? null : htc.getEarlyHivDate());
            patient.setVirusLoadConfirm(htc.getVirusLoad());
            patient.setVirusLoadConfirmDate(htc.getVirusLoadDate() == null ? null : htc.getVirusLoadDate());
            patient.setPatientPhone(htc.getPatientPhone());
            patient.setVirusLoadNumber(htc.getVirusLoadNumber());
            patient.setEarlyBioName(htc.getEarlyBioName());
            patient.setEarlyDiagnose(htc.getEarlyDiagnose());

            patient = patientService.save(patient);
            patientService.log(patient.getID(), "Chuyển gửi từ tư vấn xét nghiệm, cơ sở " + siteHtc.getName());

//            commonService.sendToPacPatient(htc, patient);
            // Gửi email cho GSPH nhận
            List<SiteEntity> pacSite = siteService.findByServiceAndProvince(ServiceEnum.PAC.getKey(), siteHtc.getProvinceID());
            Map<String, Object> variables = new HashMap<>();
            variables.put("code", htc.getCode());
            variables.put("sentMessage", "Chuyển gửi thông tin khách hàng qua quản lý người nhiễm");
            variables.put("pacSentDate", TextUtils.formatDate(htc.getPacSentDate(), "dd/MM/yyyy"));
            variables.put("sourceSentSite", siteHtc.getName());
            for (SiteEntity siteEntity : pacSite) {
                String email = parameterService.getSiteConfig(siteEntity.getID(), SiteConfigEnum.ALERT_GSPH_EMAIL.getKey());
                if (email != null && !"".equals(email)) {
                    sendEmail(email, "[Thông báo] Chuyển gửi GSPH khách hàng(" + htc.getCode() + ")", EmailoutboxEntity.TEMPLATE_SENT_PAC, variables);
                }
            }
            String body = Base64.getEncoder().encodeToString(gson.toJson(patient).getBytes());
            return new Response<>(true, "Khách hàng đã được chuyển sang báo cáo giám sát phát hiện thành công", body);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

}
