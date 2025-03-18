package com.gms.controller.api.qlnn;

import com.gms.entity.bean.Response;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcConfirmEntity;
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

@RestController
public class ConfirmController extends BaseController {

    @Autowired
    private PacPatientService patientService;

    /**
     * Convert dữ liệu sang định dạng phục vụ page define
     *
     * @author vvthanh
     * @return
     */
    private HashMap<String, HashMap<String, String>> buildFinalParameter() {
        HashMap<String, HashMap<String, String>> dataModels = new LinkedHashMap<>();
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);

        for (Map.Entry<String, String> entry : getFinalParameter().entrySet()) {
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

    /**
     * Tự động thêm cấu hình khi chưa có
     *
     * @param type
     * @param siteID
     * @return
     */
    private ParameterEntity addConfig(String type, SiteEntity site) {

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

    /**
     * Lấy thông tin mapping của cơ sở hiện tại với thông tin quản lý người
     * nhiẽm
     *
     * @auth TrangBN
     * @return
     */
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

    @RequestMapping(value = "/v1/pac-confirm.api", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum, @RequestBody String content) {
        try {
            Map<String, Map<Long, String>> options = getSiteOptions();
            String json = new String(Base64.getDecoder().decode(content.trim()));
            HtcConfirmEntity confirm = gson.fromJson(json, HtcConfirmEntity.class);
            if (!checksum(checksum, String.valueOf(confirm.getID()), String.valueOf(confirm.getSiteID()))) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat}/{ma_confirm}/{ma_site})");
            }

            SiteEntity site = siteService.findOne(confirm.getSiteID());
            if (site == null) {
                throw new Exception("Không tìm thấy cơ sở chuyển gửi");
            }

            PacPatientInfoEntity patient = new PacPatientInfoEntity();
            String bloodBase = options.get(ParameterEntity.BLOOD_BASE).getOrDefault(confirm.getSiteID(), null);
            String confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirm.getSiteID(), null);
            String treatmentSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(confirm.getSiteID(), null);

            // Kiểm tra mapping thông tin tham số bên quản lý người nhiễm
            StringBuilder errorMsg = new StringBuilder();
            String errorFlg = "";
            errorMsg.append(String.format("Chuyển gửi không thành công. Cơ sở [%s] chưa cấu hình các thông tin quản lý người nhiễm dưới đây: ", siteService.findOne(confirm.getSiteID()).getName()));

            if (StringUtils.isEmpty(bloodBase)) {
                if (confirm.getSiteID() != null) {
                    ParameterEntity configOk = addConfig(ParameterEntity.BLOOD_BASE, site);
                    if (configOk == null) {
                        errorFlg = "a";
                        errorMsg.append("<br> - Nơi lấy mẫu xét nghiêm.");
                    }
                } else {
                    errorFlg = "a";
                    errorMsg.append("<br> - Nơi lấy mẫu xét nghiêm.");
                }

            }

            if (StringUtils.isEmpty(confirmTestSite)) {
                if (confirm.getSiteID() != null) {
                    ParameterEntity configOk = addConfig(ParameterEntity.PLACE_TEST, site);
                    if (configOk == null) {
                        errorFlg = "a";
                        errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
                    }
                } else {
                    errorFlg = "a";
                    errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
                }
            }

            if (StringUtils.isEmpty(treatmentSite)) {
                if (confirm.getSiteID() != null) {
                    ParameterEntity configOk = addConfig(ParameterEntity.TREATMENT_FACILITY, site);
                    if (configOk == null) {
                        errorFlg = "a";
                        errorMsg.append("<br> - Cơ sở điều trị");
                    }
                } else {
                    errorFlg = "a";
                    errorMsg.append("<br> - Cơ sở điều trị");
                }

            }

            if (StringUtils.isNotEmpty(errorFlg)) {
                return new Response<>(false, errorMsg.toString(), "ERROR");
            }

            patient.setBloodBaseID(bloodBase);
            patient.setSiteConfirmID(confirmTestSite);
            patient.setYearOfBirth(confirm.getYear());

            patient.setFullname(confirm.getFullname());
            patient.setGenderID(confirm.getGenderID());
            patient.setIdentityCard(confirm.getPatientID());
            patient.setObjectGroupID(confirm.getObjectGroupID());

            patient.setPermanentAddressNo(confirm.getAddress());
            patient.setPermanentAddressGroup(confirm.getPermanentAddressGroup());
            patient.setPermanentAddressStreet(confirm.getPermanentAddressStreet());
            patient.setPermanentProvinceID(confirm.getProvinceID());
            patient.setPermanentDistrictID(confirm.getDistrictID());
            patient.setPermanentWardID(confirm.getWardID());

            patient.setCurrentAddressNo(confirm.getCurrentAddress());
            patient.setCurrentAddressGroup(confirm.getCurrentAddressGroup());
            patient.setCurrentAddressStreet(confirm.getCurrentAddressStreet());
            patient.setCurrentProvinceID(confirm.getCurrentProvinceID());
            patient.setCurrentDistrictID(confirm.getCurrentDistrictID());
            patient.setCurrentWardID(confirm.getCurrentWardID());

            patient.setSourceServiceID(ServiceEnum.HTC_CONFIRM.getKey());
            patient.setSourceSiteID(confirm.getSiteID());
            patient.setSourceID(confirm.getID());
            patient.setDetectProvinceID(site.getProvinceID());
            patient.setDetectDistrictID(site.getDistrictID());
            patient.setProvinceID(site.getProvinceID());
            patient.setDistrictID(site.getDistrictID());
            patient.setConfirmTime(confirm.getConfirmTime() == null ? null : confirm.getConfirmTime());
            patient.setConfirmCode(confirm.getCode());
            patient.setHealthInsuranceNo(confirm.getInsuranceNo());
            patient.setHasHealthInsurance(confirm.getInsurance());
            patient.setModeOfTransmissionID(confirm.getModeOfTransmission());

            patient.setEarlyHiv(confirm.getEarlyHiv());
            patient.setEarlyHivTime(confirm.getEarlyHivDate() == null ? null : confirm.getEarlyHivDate());
            patient.setVirusLoadConfirm(confirm.getVirusLoad());
            patient.setVirusLoadConfirmDate(confirm.getVirusLoadDate() == null ? null : confirm.getVirusLoadDate());

            patient.setEarlyBioName(confirm.getEarlyBioName());
            patient.setEarlyDiagnose(confirm.getEarlyDiagnose());
            patient.setVirusLoadNumber(confirm.getVirusLoadNumber());

            //them ngay 07/04/2021
            patient.setRaceID(confirm.getRaceID());
            patient.setPatientPhone(confirm.getPatientPhone());
            patient.setJobID(confirm.getJobID());
            patient.setRiskBehaviorID(confirm.getRiskBehaviorID());

            List<PacPatientInfoEntity> transfer = patientService.findByTransfer(patient.getSourceSiteID(), patient.getSourceID(), patient.getSourceServiceID());
            if (transfer != null && !transfer.isEmpty()) {
//                return new Response<>(false, "EXCEPTION", "Thông tin đã được chuyển gửi");
            }

            patientService.save(patient);
            patientService.log(patient.getID(), "Thêm mới từ chuyển gửi GSPH khẳng định");

            String body = Base64.getEncoder().encodeToString(gson.toJson(patient).getBytes());
            return new Response<>(true, "Đã được chuyển sang báo cáo giám sát phát hiện thành công", body);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

}
