package com.gms.scheduled;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PqmLogEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.EmailService;
import com.gms.service.PqmLogService;
import com.gms.service.PqmService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class PqmScheduled extends BaseScheduled {

    @Autowired
    private PqmService pqmService;
    @Autowired
    private PqmLogService pqmLogService;
    @Autowired
    private SiteService siteService;
    @Autowired
    protected EmailService emailService;

    private static String FORMATDATES = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Chỉ số về HTC Luôn lấy thông tin chỉ số tính đến thời điểm hiện tại
     */
//    @Scheduled(cron = "0 47 14 * * *")
    public void indicatorHTS_TST_POS() {
        System.out.println("Start: " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getHTS_TST_POS(i, 2020, "31");
            System.out.println("month: " + i);
        }
        pqmService.getHTS_TST_POS(1, 2021, "31");
        System.out.println("END: " + TextUtils.formatDate(new Date(), FORMATDATES));

    }

//    @Scheduled(cron = "0 48 14 * * *")
    public void indicatorHTS_TST_RECENCY() {
        System.out.println("Start 2 : " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getHTS_TST_RECENCY(i, 2020, "31");
            System.out.println("month: " + i);

        }
        pqmService.getHTS_TST_RECENCY(1, 2021, "31");
        System.out.println("END 2 : " + TextUtils.formatDate(new Date(), FORMATDATES));

    }

//    @Scheduled(cron = "0 49 14 * * *")
    public void indicatorPOS_TO_ART() {
        System.out.println("Start 3 : " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getPOS_TO_ART(i, 2020, "31");
            System.out.println("month: " + i);
        }
        pqmService.getPOS_TO_ART(1, 2021, "31");
        System.out.println("END 3 : " + TextUtils.formatDate(new Date(), FORMATDATES));

    }

//    @Scheduled(cron = "0 50 14 * * *")
    public void indicatorIMS_TST_POS() {
        System.out.println("Start 4 : " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getIMS_TST_POS(i, 2020, "31");
            System.out.println("month: " + i);
        }
        pqmService.getIMS_TST_POS(1, 2021, "31");
        System.out.println("END 4 : " + TextUtils.formatDate(new Date(), FORMATDATES));

    }

//    @Scheduled(cron = "0 51 14 * * *")
    public void indicatorIMS_POS_ART() {
        System.out.println("Start 5 : " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getIMS_POS_ART(i, 2020, "31");
            System.out.println("month: " + i);
        }
        pqmService.getIMS_POS_ART(1, 2021, "31");
        System.out.println("END 5 : " + TextUtils.formatDate(new Date(), FORMATDATES));

    }

//    @Scheduled(cron = "0 52 14 * * *")
    public void indicatorIMS_TST_RECENCY() {
        System.out.println("Start 6 : " + TextUtils.formatDate(new Date(), FORMATDATES));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //Chỉ số HIV dương tính
        for (int i = 1; i < 13; i++) {
            pqmService.getIMS_TST_RECENCY(i, 2020, "31");
            System.out.println("month: " + i);
        }
        pqmService.getIMS_TST_RECENCY(1, 2021, "31");
        System.out.println("END 6 : " + TextUtils.formatDate(new Date(), FORMATDATES));
    }

    @Scheduled(cron = "0 0 0 15 * *")
    public void sendMailRemind() {

        String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(new Date()), "yyyy-MM-dd");
        String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(new Date()), "yyyy-MM-dd");

        String month = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(new Date()), "MM/yyyy");

        System.out.println("Start 6 : " + from + "  " + to);
        System.out.println("Start 7 : " + month);

        Set<String> provinces = new HashSet<>();
        provinces.add("82"); //Tiền Giang
        provinces.add("75"); //Đồng Nai
        provinces.add("72"); //Tây Ninh

        for (String province : provinces) {
            Set<Long> siteIDs = new HashSet<>();
            for (SiteEntity siteEntity : siteService.getSiteHtc(province)) {
                if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                    siteIDs.add(siteEntity.getID());
                }
            }
            for (SiteEntity siteEntity : siteService.getSiteOpc(province)) {
                if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                    siteIDs.add(siteEntity.getID());
                }
            }
            for (SiteEntity siteEntity : siteService.getSitePrEP(province)) {
                if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                    siteIDs.add(siteEntity.getID());
                }
            }
            for (SiteEntity siteEntity : siteService.getSiteConfirm(province)) {
                siteIDs.add(siteEntity.getID());
            }
            for (SiteEntity siteEntity : siteService.findByServiceAndProvince("drug", province)) {
                siteIDs.add(siteEntity.getID());
            }
            for (SiteEntity siteEntity : siteService.findByServiceAndProvince("shi", province)) {
                siteIDs.add(siteEntity.getID());
            }

            List<PqmLogEntity> logs = pqmLogService.findBySiteID(siteIDs, from, to);
            Set<Long> siteSend = new HashSet<>();
            if (logs != null) {
                for (PqmLogEntity log : logs) {
                    if (log.getSiteID() != null) {
                        siteSend.add(log.getSiteID());
                    }
                }
            }
            Set<Long> siteNotSend = new HashSet<>();
            for (Long siteID : siteIDs) {
                if (siteSend.isEmpty() || !siteSend.contains(siteID)) {
                    siteNotSend.add(siteID);
                }
            }
            for (Long long1 : siteNotSend) {
                System.out.println("province " + province + "site site " + long1 + " month " + month);

                SiteEntity site = siteService.findOne(long1);

                // Gửi mail thông báo
                String email = site.getEmail();
                if (StringUtils.isNotEmpty(email)) {
                    sendEmailNotify(email,
                            String.format("Thông báo về việc nhập dữ liệu Excel vào DataHub (ngày %s)", TextUtils.formatDate(new Date(), "dd/MM/yyyy")),
                            String.format(
                                    "<span class='text-danger text-bold'>Tháng %s</span>, cơ sở chưa thực hiện nhập dữ liệu Excel vào DataHub"
                                    + "<br> Rất mong cơ sở thực hiện nhập dữ liệu Excel vào DataHub theo định kỳ hàng tháng."
                                    + "<br> Trân trọng gửi thông tin đến cơ sở!"
                                    + "<br> Quản trị hệ thống"
                                    + "<br>", month)
                    );
                }

            }
        }

    }

    public void sendEmailNotify(String to, String subject, String message) {
        if (to == null || "".equals(to)) {
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("contents", message);
        sendEmail(to, String.format("%s", subject), EmailoutboxEntity.TEMPLATE_NOTIFY, variables);
    }

    public void sendEmail(String to, String subject, String type, Map<String, Object> variables) {
        if (to != null && !"".equals(to)) {
            emailService.send(to, subject, type, variables);
        }
    }

}
