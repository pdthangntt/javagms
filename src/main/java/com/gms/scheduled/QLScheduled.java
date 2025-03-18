package com.gms.scheduled;

import com.gms.components.TextUtils;
import com.gms.entity.db.QlReport;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.ql.Local;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.QlReportService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class QLScheduled extends BaseScheduled {
    
    private final String FORMATDATE = "yyyy-MM-dd";
    
    @Autowired
    private QlReportService qlReportService;
    @Autowired
    private PacPatientService patientService;
    @Autowired
    private LocationsService locationsService;

    /**
     * Chạy hàng ngày, tự động đồng bộ số liệu được tạo của ngày hôm đó
     *
     * @auth vvThành
     * @job [Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week]
     * [Year]
     */
    @Async
//    @Scheduled(cron = "0 10 3 * * *")
//    @Scheduled(cron = "0 30 22 * * *")
    public void actionNhiemMoi() {
        try {
            Date currentDate = new Date();
            List<Local> list = patientService.findProvinceNhiemMoi(TextUtils.formatDate(currentDate, FORMATDATE), TextUtils.formatDate(currentDate, FORMATDATE));
//            List<Local> list = patientService.findProvinceNhiemMoi("1907-01-01", TextUtils.formatDate(currentDate, FORMATDATE));
            for (Local local : list) {
                try {
                    String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(local.getMonth(), local.getYear()), FORMATDATE);
                    String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(local.getMonth(), local.getYear()), FORMATDATE);

                    //Lấy số nhiễm mới của tỉnh thường trú
                    QlReport pReport = qlReportService.findOne(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), local.getMonth(), local.getYear());
                    if (pReport == null) {
                        pReport = new QlReport();
                        pReport.setProvinceID(local.getPermanentProvinceID());
                        pReport.setDistrictID(local.getPermanentDistrictID());
                        pReport.setWardID(local.getPermanentWardID());
                        pReport.setMonth(local.getMonth());
                        pReport.setYear(local.getYear());
                    }
                    pReport.setPermanentEarly(patientService.countProvinceNhiemMoiThuongTru(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), from, to));
                    qlReportService.save(pReport);

                    //Lấy số nhiễm mới của tỉnh tạm trú
//                    QlReport cReport = qlReportService.findOne(local.getCurrentProvinceID(), local.getCurrentDistrictID(), local.getMonth(), local.getYear());
//                    if (cReport == null) {
//                        cReport = new QlReport();
//                        cReport.setProvinceID(local.getCurrentProvinceID());
//                        cReport.setDistrictID(local.getCurrentDistrictID());
//                        cReport.setMonth(local.getMonth());
//                        cReport.setYear(local.getYear());
//                    }
//                    cReport.setCurrentEarly(patientService.countProvinceNhiemMoiTamtru(local.getCurrentProvinceID(), local.getCurrentDistrictID(), from, to));
//                    qlReportService.save(pReport);
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger().error("Report {}", e.getMessage());
                }
                
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("Nhiễm mới {}", ex.getMessage());
        }
    }
    
    @Async
//    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(cron = "*/30 * * * * *")
//    @Scheduled(cron = "0 40 22 * * *")
    public void actionPhathien() {
        try {
            Date currentDate = new Date();
            List<Local> list = patientService.findProvincePhathien(TextUtils.formatDate(currentDate, FORMATDATE), TextUtils.formatDate(currentDate, FORMATDATE));
//            List<Local> list = patientService.findProvincePhathien("1907-01-01", TextUtils.formatDate(currentDate, FORMATDATE));

            for (Local local : list) {
                try {
                    Date date = TextUtils.getFirstDayOfMonth(local.getMonth(), local.getYear());
                    String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE);
                    String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE);
                    
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    c.add(Calendar.MONTH, -30);
                    Date date30 = c.getTime();
                    String from30 = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date30), FORMATDATE);
                    
                    c.setTime(date);
                    c.add(Calendar.MONTH, -1);
                    Date date1 = c.getTime();
                    String to30 = TextUtils.formatDate(TextUtils.getLastDayOfMonth(date1), FORMATDATE);
                    
                    List<WardEntity> wards = locationsService.findWardByDistrictID(local.getPermanentDistrictID());
                    for (WardEntity ward : wards) {
                        //Lấy số nhiễm mới của tỉnh thường trú
                        QlReport pReport = qlReportService.findOne(local.getPermanentProvinceID(), local.getPermanentDistrictID(), ward.getID(), local.getMonth(), local.getYear());
                        if (pReport == null) {
                            pReport = new QlReport();
                            pReport.setProvinceID(local.getPermanentProvinceID());
                            pReport.setDistrictID(local.getPermanentDistrictID()); 
                            pReport.setWardID(ward.getID());
                            pReport.setMonth(local.getMonth());
                            pReport.setYear(local.getYear());
                        }
                        //Số xetnghiem trong tháng
                        pReport.setPermanentHtc(patientService.countProvincePhatHienThuongTru(local.getPermanentProvinceID(), local.getPermanentDistrictID(), ward.getID(), from, to));
                        
                        int pAvg30Month = patientService.countProvincePhatHienThuongTru(local.getPermanentProvinceID(), local.getPermanentDistrictID(), ward.getID(), from30, to30);
                        pReport.setPermanentAvg30Month(Double.valueOf(pAvg30Month));
                        qlReportService.save(pReport);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger().error("Report {}", e.getMessage());
                }
                
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("Nhiễm mới {}", ex.getMessage());
        }
    }
    
    @Async
//    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(cron = "0 45 22 * * *")
    public void actionTuVong() {
        try {
            Date currentDate = new Date();
            List<Local> list = patientService.findProvinceTuVong(TextUtils.formatDate(currentDate, FORMATDATE), TextUtils.formatDate(currentDate, FORMATDATE));
//            List<Local> list = patientService.findProvinceTuVong("1907-01-01", TextUtils.formatDate(currentDate, FORMATDATE));
            for (Local local : list) {
                try {
                    Date date = TextUtils.getFirstDayOfMonth(local.getMonth(), local.getYear());
                    String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE);
                    String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE);

                    //Lấy số nhiễm mới của tỉnh thường trú
                    QlReport pReport = qlReportService.findOne(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), local.getMonth(), local.getYear());
                    if (pReport == null) {
                        pReport = new QlReport();
                        pReport.setProvinceID(local.getPermanentProvinceID());
                        pReport.setDistrictID(local.getPermanentDistrictID());
                        pReport.setWardID(local.getPermanentWardID());
                        pReport.setMonth(local.getMonth());
                        pReport.setYear(local.getYear());
                    }
                    //Số xetnghiem trong tháng
                    pReport.setPermanentDead(patientService.countProvinceTuVongThuongTru(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), from, to));
                    qlReportService.save(pReport);
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger().error("Report {}", e.getMessage());
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("Nhiễm mới {}", ex.getMessage());
        }
    }
    
    @Async
//    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(cron = "0 55 22 * * *")
    public void actionMoiNhiem() {
        try {
            Date currentDate = new Date();
            List<Local> list = patientService.findProvinceMoiNhiem(TextUtils.formatDate(currentDate, FORMATDATE), TextUtils.formatDate(currentDate, FORMATDATE));
//            List<Local> list = patientService.findProvinceMoiNhiem("1907-01-01", TextUtils.formatDate(currentDate, FORMATDATE));
            for (Local local : list) {
                try {
                    Date date = TextUtils.getFirstDayOfMonth(local.getMonth(), local.getYear());
                    String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE);
                    String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE);

                    //Lấy số nhiễm mới của tỉnh thường trú
                    QlReport pReport = qlReportService.findOne(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), local.getMonth(), local.getYear());
                    if (pReport == null) {
                        pReport = new QlReport();
                        pReport.setProvinceID(local.getPermanentProvinceID());
                        pReport.setDistrictID(local.getPermanentDistrictID());
                        pReport.setWardID(local.getPermanentWardID());
                        pReport.setMonth(local.getMonth());
                        pReport.setYear(local.getYear());
                    }
                    //Số mới nhiễm trong tháng
                    pReport.setPermanentNewEarly(patientService.countProvinceMoiNhiemThuongTru(local.getPermanentProvinceID(), local.getPermanentDistrictID(), local.getPermanentWardID(), from, to));
                    qlReportService.save(pReport);
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger().error("Report {}", e.getMessage());
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error("Nhiễm mới {}", ex.getMessage());
        }
    }
    
}
