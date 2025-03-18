package com.gms.scheduled;

import com.gms.components.HubUtils;
import com.gms.components.UrlUtils;
import com.gms.config.RabbitConfig;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import com.gms.entity.constant.PqmHubEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.rabbit.HtcConfirmMessage;
import com.gms.service.HtcVisitService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class HtcScheduled extends BaseScheduled {

    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private HubUtils hubUtils;
    @Autowired
    private Gson gson;
    @Autowired
    private PacPatientService patientService;

    /**
     * Cập nhật trạng thái xét nghiệm khẳng định, sử dụng rabbit
     *
     * @author vvThành
     * @param message
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_UPDATE_STATUS_CONFIRM_TO_HTC)
    public void receiveSyncHTCFromConfirm(final HtcConfirmMessage message) {
        getLogger().info("[Rabbit] Job update status htc visit from confirm");

        HashMap<String, String> option = parameterService.getOptionsByType(ParameterEntity.CONFIRM_TEST_STATUS, true);
        try {
            HtcConfirmEntity confirm = message.getConfirm();
            if (confirm == null || confirm.getSourceID() == null) {
                throw new Exception("Confirm or source ID is null");
            }
            HtcVisitEntity visit = visitService.findByCodeAndSite(message.getConfirm().getSourceID(), message.getConfirm().getSourceSiteID(), false);

            if (visit == null || visit.isIsRemove()) {
                throw new Exception(String.format("Visit code %s not found or remove", message.getConfirm().getSourceID()));
            }

            if (visit.getConfirmTestStatus() == null || StringUtils.isEmpty(visit.getConfirmTestStatus())
                    || !visit.getSiteConfirmTest().equals(String.valueOf(message.getConfirm().getSiteID()))) {
                throw new Exception(String.format("Visit code %s not found or remove in source site", message.getConfirm().getSourceID()));
            }

            for (Map.Entry<String, String> entry : option.entrySet()) {
//                 && StringUtils.isEmpty(visit.getConfirmResultsID())
                if (entry.getValue().equals(confirm.getLabelConfirmTestStatus())) {
                    visit.setConfirmTestStatus(entry.getKey().equals(HtcConfirmStatusEnum.PAID.getKey()) ? HtcConfirmStatusEnum.DONE.getKey() : entry.getKey());
                    String msg = "[Tự động] Cập nhật trạng thái khẳng định " + confirm.getLabelConfirmTestStatus();
                    String alert = "";

                    if (entry.getKey().equals(HtcConfirmStatusEnum.PAID.getKey())) {
                        visit.setConfirmTestNo(confirm.getCode());
                        visit.setConfirmResultsID(confirm.getResultsID());
                        visit.setConfirmTime(confirm.getConfirmTime());
                        visit.setResultsSiteTime(confirm.getResultsReturnTime());
                        visit.setEarlyHiv(confirm.getEarlyHiv());
                        visit.setEarlyHivDate(confirm.getEarlyHivDate());
                        visit.setVirusLoad(confirm.getVirusLoad());
                        visit.setVirusLoadDate(confirm.getVirusLoadDate());
                        visit.setEarlyBioName(confirm.getEarlyBioName());
                        visit.setVirusLoadNumber(confirm.getVirusLoadNumber());
                        visit.setEarlyDiagnose(confirm.getEarlyDiagnose());
                        visit.setSampleSentDate(confirm.getSourceSampleDate()); // Cập nhật ngày người gửi mẫu bên sàng lọc 
                        msg += ". Kèm kết quả";
                        alert = "Kèm kết quả";
                    }
                    visit.setConfirmResutl(true);
                    visitService.save(visit);
                    visitService.log(visit.getID(), msg);

                    staffService.sendMessage(visit.getCreatedBY(), visit.getUpdatedBY(), "Thay đổi trạng thái xét nghiệm khẳng định", String.format("Khách hàng %s. %s", visit.getCode(), alert), String.format("%s?code=%s", UrlUtils.htcIndex(), visit.getCode()));
                    break;
                }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
    }

    /**
     * Tự động đẩy dữ liệu sang PQM
     */
//    @Scheduled(cron = "0 */5 * * * ?")
    public void appVctToPqm() {
        Set<String> services = new HashSet<>();
        services.add(ServiceEnum.HTC.getKey());

        Set<String> provinces = new HashSet<>();
        provinces.add("82"); //Tiền Giang
        provinces.add("75"); //Đồng Nai
        provinces.add("72"); //Tây Ninh
        Set<Long> siteIds = new HashSet<>();
        for (SiteEntity site : siteService.findByServiceAndProvince(services, provinces)) {
            if (!PqmHubEnum.FROM_IMS.getKey().equals(site.getHub())) {
                continue;
            }
            siteIds.add(site.getID());
        }
        if (siteIds.isEmpty()) {
            getLogger().info("[HTC-PQM] Site empty use role ims import");
        } else {
            List<HtcVisitEntity> vists = visitService.findByHub(siteIds, 20);
            if (vists == null || vists.isEmpty()) {
                getLogger().info("[HTC-PQM] Empty data find");
            }
            for (HtcVisitEntity item : vists) {
                try {
                    PqmVctEntity vct = hubUtils.visit(item);
                    item.setPqmCode(vct.getID());
                    getLogger().info("[HTC-PQM] Push " + item.getID() + " => hub id" + vct.getID());
                } catch (Exception e) {
                    item.setPqmCode(Long.valueOf("-1"));
                    getLogger().info("[HTC-PQM] Push " + item.getID() + " => Error " + e.getMessage());
                }
                visitService.update(item);
            }
        }
    }

//    @Scheduled(cron = "*/30 * * * * ?")
//    @Scheduled(cron = "0 */10 * * * ?")
    public void VCTtoQLNN() {
        List<HtcVisitEntity> items = visitService.findByEarlyJob(true);
        if (items == null || items.isEmpty()) {
            getLogger().info("[HTC-update-QLNN] data empty");
            return;
        }
        for (HtcVisitEntity item : items) {
            item.setEarlyJob(false);
            visitService.update(item);

            if (item.getPacSentDate() != null && item.getPacPatientID() != null) {
                PacPatientInfoEntity patientInfo = patientService.findOne(item.getPacPatientID());
                if (patientInfo != null) {
                    try {
                        PacPatientInfoEntity old = (PacPatientInfoEntity) patientInfo.clone();
                        //Cập nhật thông tin nhiễm mới cho QLNN
                        patientInfo.setEarlyHiv(item.getEarlyHiv());
                        patientInfo.setEarlyHivTime(item.getEarlyHivDate());
                        patientInfo.setEarlyDiagnose(item.getEarlyDiagnose());
                        patientInfo.setEarlyBioName(item.getEarlyBioName());
                        patientInfo.setVirusLoadConfirmDate(item.getVirusLoadDate());
                        patientInfo.setVirusLoadNumber(item.getVirusLoadNumber());
                        patientInfo.setVirusLoadConfirm(item.getVirusLoad());
                        patientService.save(patientInfo);
                        patientService.log(patientInfo.getID(), "Tự động cập nhật thông tin nhiễm mới từ sành lọc. " + patientInfo.changeToString(old, true));
                    } catch (Exception e) {
                        getLogger().error(e.getMessage());
                    }
                }
            }
        }
    }
}
