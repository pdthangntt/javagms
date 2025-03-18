package com.gms.scheduled;

import com.gms.components.GoogleUtils;
import com.gms.components.TextUtils;
import com.gms.components.hivinfo.CallUtils;
import com.gms.components.rabbit.RabbitMQSender;
import com.gms.config.RabbitConfig;
import com.gms.entity.constant.AidsStatusEnum;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfChangeTreatmentEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacLocationEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.json.hivinfo.patient.Data;
import com.gms.entity.json.hivinfo.patient.Gsph;
import com.gms.entity.json.hivinfo.patient.GsphAids;
import com.gms.entity.json.hivinfo.patient.GsphHiv;
import com.gms.entity.json.hivinfo.patient.GsphHivDieuTri;
import com.gms.entity.json.hivinfo.patient.GsphInput;
import com.gms.entity.json.hivinfo.patient.GsphTuVong;
import com.gms.entity.rabbit.PacPatientMessage;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.google.maps.model.GeocodingResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class PacScheduled extends BaseScheduled {

    private final String FORMATDATE = "yyyy-MM-dd";

    private static HashMap<String, HashMap<String, String>> sOptions;

    @Value("${app.hivinfo.province}")
    private String provinceFilter;

    @Autowired
    private GoogleUtils googleUtils;
    @Autowired
    private RabbitMQSender rabbitSender;
    @Autowired
    private CallUtils callUtils;
    @Autowired
    private PacPatientService patientService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;

    private Set<String> getParameterTypes() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        return parameterTypes;
    }

    /**
     * Option hiv info -> app ims
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        if (sOptions != null) {
            return sOptions;
        }
        Set<String> parameterTypes = getParameterTypes();
        sOptions = new HashMap<>();
        for (String item : getParameterTypes()) {
            if (sOptions.get(item) == null) {
                sOptions.put(item, new LinkedHashMap<String, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getHivInfoCode() == null || entity.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(entity.getType()).put(entity.getHivInfoCode(), entity.getCode());
        }

        String key = "province";
        sOptions.put(key, new HashMap<String, String>());
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getHivInfoCode(), item.getID());
        }

        key = "district";
        sOptions.put(key, new HashMap<String, String>());
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getHivInfoCode(), item.getID());
        }

        //Fixed đường lây
//        sOptions.get(ParameterEntity.MODE_OF_TRANSMISSION).put("4", "1"); //Lây qua đường tiêm chích ma túy -> Lây qua đường máu
//        sOptions.get(ParameterEntity.MODE_OF_TRANSMISSION).put("5", "1"); //Truyền máu -> Lây qua đường máu
//        sOptions.get(ParameterEntity.MODE_OF_TRANSMISSION).put("6", "1"); //Tai nạn nghề nghiệp -> không rõ
//        sOptions.get(ParameterEntity.MODE_OF_TRANSMISSION).put("7", "2"); //Tình dục đồng giới -> Lây qua đường tình dục
//        sOptions.get(ParameterEntity.MODE_OF_TRANSMISSION).put("8", "2"); //Tình dục khác giới -> Lây qua đường tình dục
        return sOptions;
    }

    /**
     * Lấy thông tin người nhiễm quản lý từ GSPH
     *
     * @auth vvThành
     * @job [Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week]
     * [Year]
     */
    @Async
//    @Scheduled(cron = "*/10 * * * * ?")
    public void actionPatientGet() {
        try {
            PacPatientMessage msg;
            Map<String, Data> data = callUtils.getPatient(100, provinceFilter);
            if (data == null || data.isEmpty()) {
                getLogger().debug("[HivInfo] Hivinfo Patient empty");
                return;
            }
            for (Map.Entry<String, Data> entry : data.entrySet()) {
                Data item = entry.getValue();
                if (item.getGsph() == null) {
                    getLogger().debug("[HivInfo] GSPH is null");
                    continue;
                }
                getLogger().info("[HivInfo] process patient ... {}", item.getGsph().getCode());
                msg = new PacPatientMessage();
                msg.setOptions(getOptions());
                msg.setData(item);
                rabbitSender.sendPacPatient(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("GET: {}", ex.getMessage());
        }
    }

    /**
     * Lấy thông tin để cập nhật latlong
     *
     * @auth vvThành
     * @job 1hour 60 * 60 * 1000
     */
    @Async
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
    public void actionUpdateLatlng() {
        List<PacPatientInfoEntity> items = patientService.getForUpdateLatlng(50);
        if (items == null || items.isEmpty()) {
            getLogger().debug("[IMS] Patient empty update lat-lng");
        }
        GeocodingResult geocode;
        PacLocationEntity location;
        for (PacPatientInfoEntity item : items) {
            try {
                item.setLatlngTime(new Date());
                List<PacPatientInfoEntity> models = new ArrayList<>();
                models.add(item);
                patientService.saveAll(models);

                location = patientService.findOneLocation(item.getID());
                if (location == null) {
                    location = new PacLocationEntity();
                    location.setID(item.getID());
                }
                geocode = googleUtils.geocode(item.getPermanentAddressFull());
                if (geocode != null) {
                    location.setPermanentAddress(geocode.formattedAddress);
                    location.setPermanentLat(geocode.geometry.location.lat);
                    location.setPermanentLng(geocode.geometry.location.lng);
                    location.setPermanentPlace(geocode.placeId);
                }
                getLogger().info("Pid {} adds {}", item.getID(), item.getPermanentAddressFull());
                patientService.save(location);
            } catch (Exception ex) {
                ex.printStackTrace();
                getLogger().error("GET error: {}", ex.getMessage());
            }
        }
    }

    /**
     * Nhận thông tin từ hiv update về ims
     *
     * @param message
     */
//    @RabbitListener(queues = RabbitConfig.QUEUE_HIVINFO_PATIENT)
    public void receiveSyncPatient(final PacPatientMessage message) {
        HashMap<String, HashMap<String, String>> options = message.getOptions();
        Gsph gsph = message.getData().getGsph();
        GsphHivDieuTri opc = message.getData().getOpc();
        GsphHiv hiv = message.getData().getHiv();
        GsphInput input = message.getData().getInput();
        GsphTuVong deal = message.getData().getDeal();
        GsphAids aids = message.getData().getAids();
        if (gsph == null || StringUtils.isEmpty(gsph.getCode())) {
            return;
        }
        getLogger().debug("[Rabbit] Job sync HIV-info patient: {}", gsph.getCode());
        try {
            PacHivInfoEntity hivInfo = patientService.findByHivCode(gsph.getCode());
            String msg = null;
            PacPatientInfoEntity patientInfo = null;
            PacPatientInfoEntity cl = null;
            if (hivInfo == null) {
                msg = "Thêm mới API Sync. Mã Hivinfo " + gsph.getCode();
                patientInfo = new PacPatientInfoEntity();
                patientInfo.setRiskBehaviorID(new ArrayList<String>());
                patientInfo.setParentID(Long.valueOf(0));
                patientInfo.setSourceServiceID(ServiceEnum.NONE.getKey());

                hivInfo = new PacHivInfoEntity();
                hivInfo.setCode(gsph.getCode());
            } else {
                msg = "Cập nhật API Sync. Mã Hivinfo " + gsph.getCode();
                patientInfo = patientService.findOne(hivInfo.getID());
                if (patientInfo == null) {
                    patientInfo = new PacPatientInfoEntity();
                    patientInfo.setRiskBehaviorID(new ArrayList<String>());
                    patientInfo.setParentID(Long.valueOf(0));
                    patientInfo.setSourceServiceID(ServiceEnum.NONE.getKey());
                }
                cl = (PacPatientInfoEntity) patientInfo.clone();
            }
            patientInfo.setNote(gsph.getNote());
            patientInfo.setFullname(gsph.getFullname());
            patientInfo.setRaceID(options.get(ParameterEntity.RACE).getOrDefault(String.valueOf(gsph.getRaceID()), RaceEnum.KINH.getKey()));
            patientInfo.setGenderID(options.get(ParameterEntity.GENDER).getOrDefault(String.valueOf(gsph.getGenderID()), GenderEnum.NONE.getKey()));
            patientInfo.setJobID(options.get(ParameterEntity.JOB).getOrDefault(String.valueOf(gsph.getJobID()), null));
            patientInfo.setModeOfTransmissionID(options.get(ParameterEntity.MODE_OF_TRANSMISSION).getOrDefault(String.valueOf(gsph.getModeOfTransmission()), null));
            patientInfo.setStatusOfResidentID(options.get(ParameterEntity.STATUS_OF_RESIDENT).getOrDefault(String.valueOf(gsph.getStatusID()), null));
            patientInfo.setObjectGroupID(options.get(ParameterEntity.TEST_OBJECT_GROUP).getOrDefault(String.valueOf(gsph.getObjectID()), null));
            patientInfo.setYearOfBirth(gsph.getYearOfBirth());
            patientInfo.setIdentityCard(gsph.getPatientID());
            patientInfo.setPermanentAddressNo(gsph.getPermanentHomeNo());
            patientInfo.setPermanentAddressStreet(gsph.getPermanentStreet());
            patientInfo.setPermanentAddressGroup(gsph.getPermanentGroup());
            patientInfo.setPermanentProvinceID(options.get("province").getOrDefault(String.valueOf(gsph.getPermanentProvinceID()), null));
            patientInfo.setPermanentDistrictID(options.get("district").getOrDefault(String.valueOf(gsph.getPermanentDistrictID()), null));

            patientInfo.setCurrentAddressNo(gsph.getCurrentHomeNo());
            patientInfo.setCurrentAddressStreet(gsph.getCurrentStreet());
            patientInfo.setCurrentAddressGroup(gsph.getCurrentGroup());
            patientInfo.setCurrentProvinceID(options.get("province").getOrDefault(String.valueOf(gsph.getCurrentProviceID()), null));
            patientInfo.setCurrentDistrictID(options.get("district").getOrDefault(String.valueOf(gsph.getCurrentDistrictID()), null));
            //--thiếu loại bệnh nhân

            if (patientInfo.getPermanentDistrictID() != null) {
                for (WardEntity item : locationsService.findWardByDistrictID(patientInfo.getPermanentDistrictID())) {
                    if (item.getHivInfoCode() != null && !item.getHivInfoCode().equals("") && item.getHivInfoCode().equals(String.valueOf(gsph.getPermanentWardID()))) {
                        patientInfo.setPermanentWardID(item.getID());
                        break;
                    }
                }
            }

            if (patientInfo.getCurrentDistrictID() != null) {
                for (WardEntity item : locationsService.findWardByDistrictID(patientInfo.getCurrentDistrictID())) {
                    if (item.getHivInfoCode() != null && !item.getHivInfoCode().equals("") && item.getHivInfoCode().equals(String.valueOf(gsph.getCurrentWardID()))) {
                        patientInfo.setCurrentWardID(item.getID());
                        break;
                    }
                }
            }

            String risID = options.get(ParameterEntity.RISK_BEHAVIOR).getOrDefault(String.valueOf(gsph.getRiskBehaviorID()), null);
            if (risID != null) {
                patientInfo.getRiskBehaviorID().add(risID);
            }

            if (opc != null) {
                //Thiếu Loại đăng ký, lý do kết thúc
                patientInfo.setHealthInsuranceNo(opc.getHealthInsuranceNo());
//                patientInfo.setStartTreatmentTime(TextUtils.convertDate(opc.getStartDateTherapy(), FORMATDATE));
                patientInfo.setStartTreatmentTime(TextUtils.convertDate(opc.getStartDate(), FORMATDATE));
                patientInfo.setTreatmentRegimenID(options.get(ParameterEntity.TREATMENT_REGIMEN).getOrDefault(String.valueOf(opc.getTherapyRegimen()), null));
                patientInfo.setSiteTreatmentFacilityID(options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(String.valueOf(opc.getSiteID()), null));
            }

            if (hiv != null) {
                patientInfo.setConfirmTime(TextUtils.convertDate(hiv.getTestDate(), FORMATDATE));
                patientInfo.setSiteConfirmID(options.get(ParameterEntity.PLACE_TEST).getOrDefault(String.valueOf(hiv.getTestSiteID()), null));
                patientInfo.setBloodBaseID(options.get(ParameterEntity.BLOOD_BASE).getOrDefault(String.valueOf(hiv.getSourceBloodSiteID()), null));

                //Mapping giữ trạng thái điều trị và trạng thái biến động điều trị
                String sot = options.get(ParameterEntity.STATUS_OF_TREATMENT).getOrDefault(String.valueOf(hiv.getHivTherapyStatusID()), null);
                patientInfo.setStatusOfTreatmentID(sot);
                if (sot != null && sot.equals('5')) {
                    patientInfo.setStatusOfTreatmentID(null);
                    patientInfo.setStatusOfChangeTreatmentID(StatusOfChangeTreatmentEnum.CHUYEN_DI.getKey());
                }
            }

            if (input != null) {
                patientInfo.setRemove(input.getDeleteDate() != null);
                patientInfo.setRemoteAT(TextUtils.convertDate(input.getDeleteDate(), FORMATDATE));
                patientInfo.setCreateAT(TextUtils.convertDate(input.getCreateAt(), FORMATDATE));
                patientInfo.setAcceptTime(patientInfo.getAcceptTime() == null ? TextUtils.convertDate(input.getReceiveDate(), FORMATDATE) : patientInfo.getAcceptTime());
                patientInfo.setReviewWardTime(patientInfo.getReviewWardTime() == null ? TextUtils.convertDate(input.getReceiveDate(), FORMATDATE) : patientInfo.getReviewWardTime());
                patientInfo.setReviewProvinceTime(patientInfo.getReviewProvinceTime() == null ? TextUtils.convertDate(input.getReceiveDate(), FORMATDATE) : patientInfo.getReviewProvinceTime());
            }

            if (deal != null) {
                patientInfo.setDeathTime(TextUtils.convertDate(deal.getDeathDate(), FORMATDATE));
                patientInfo.setCauseOfDeath(new ArrayList<String>());
            }
            if (deal != null && message.getData().getDeadCause() != null) {
                for (Integer item : message.getData().getDeadCause()) {
                    String caseID = options.get(ParameterEntity.CAUSE_OF_DEATH).getOrDefault(String.valueOf(item), null);
                    if (caseID == null) {
                        continue;
                    }
                    patientInfo.getCauseOfDeath().add(caseID);
                }
            }

            //aids
            if (aids != null && aids.getAidsDiagnosticDate() != null) {
                patientInfo.setAidsStatusDate(aids.getAidsDiagnosticDate());
                patientInfo.setAidsStatus(AidsStatusEnum.AIDS.getKey());
            }

            patientInfo.setDetectProvinceID(patientInfo.getPermanentProvinceID());
            patientInfo.setDetectDistrictID(patientInfo.getPermanentDistrictID());
            patientInfo.setProvinceID(patientInfo.getPermanentProvinceID());
            patientInfo.setDistrictID(patientInfo.getPermanentDistrictID());
            patientInfo.setWardID(patientInfo.getPermanentWardID());
            if (patientInfo.getWardID() == null) {
                getLogger().error("WardID not exits: {} - {}", gsph.getCode());
            }
            patientInfo = patientService.save(patientInfo);

            hivInfo.setID(patientInfo.getID());
            patientService.save(hivInfo);

//            if (patientInfo.getID() != null && cl != null) {
//                msg += ". " + patientInfo.changeToString(cl, true);
//            }
            patientService.log(patientInfo.getID(), msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("QUEUE: {} - {}", gsph.getCode(), ex.getMessage());
        }
    }
}
