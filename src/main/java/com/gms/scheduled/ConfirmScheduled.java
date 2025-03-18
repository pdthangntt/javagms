package com.gms.scheduled;

import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.PacPatientService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class ConfirmScheduled extends BaseScheduled {

    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private PacPatientService patientService;

    /**
     * Tự động đẩy dữ liệu sang PQM
     */
//    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
//    @Scheduled(cron = "0 */10 * * * ?")
    public void updateToQLNNAndVCT() {
        List<HtcConfirmEntity> items = confirmService.findByEarlyJob(true);
        if (items == null || items.isEmpty()) {
            getLogger().info("[Confirm-update] data empty");
            return;
        }
        for (HtcConfirmEntity item : items) {
            item.setEarlyJob(false);
            confirmService.update(item);

            if (item.getSourceSiteID() != null && item.getSourceID() != null) {
                HtcVisitEntity visit = visitService.findByCodeAndSite(item.getSourceID(), item.getSourceSiteID(), false);
                if (visit != null) {
                    //Cập nhật thông tin nhiễm mới cho VCT
                    visit.setEarlyHiv(item.getEarlyHiv());
                    visit.setEarlyHivDate(item.getEarlyHivDate());
                    visit.setEarlyDiagnose(item.getEarlyDiagnose());
                    visit.setEarlyBioName(item.getEarlyBioName());
                    visit.setVirusLoadDate(item.getVirusLoadDate());
                    visit.setVirusLoadNumber(item.getVirusLoadNumber());
                    visit.setVirusLoad(item.getVirusLoad());
                    visit.setUpdateAt(new Date());
                    visit.setEarlyJob(true);
                    visitService.update(visit);
                    visitService.log(visit.getID(), "Tự động cập nhật thông tin nhiễm mới từ khẳng định");
                }
            }
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
                        patientService.log(patientInfo.getID(), "Tự động cập nhật thông tin nhiễm mới từ khẳng định. " + patientInfo.changeToString(old, true));
                    } catch (Exception e) {
                        getLogger().error(e.getMessage());
                    }
                }
            }
        }
    }
}
