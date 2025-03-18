package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
public abstract class DashboardController extends BaseController {

    private static Map<String, HashMap<String, String>> options;

    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private PacPatientService patientService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private OpcArvService arvService;

    protected Map<String, HashMap<String, String>> getOptions(DashboardForm form) {
        Calendar c = Calendar.getInstance();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        if (form.getIndicatorName().contains("pac")) {
            parameterTypes.add(ParameterEntity.GENDER);
            parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        } else if (form.getIndicatorName().equals("opc")) {
            parameterTypes.add(ParameterEntity.GENDER);
        }

        options = parameterService.getOptionsByTypes(parameterTypes, null);
        options.put("indicator", new LinkedHashMap<String, String>());
        if (form.getIndicatorName().contains("htc")) {
            options.put("sites", new HashMap<String, String>());
            Set<Long> siteIds = new HashSet<>();
            //Cơ sở tư vấn xét nghiệm
            for (SiteEntity item : siteService.getSiteHtc(form.getProvinceID())) {
                siteIds.add(item.getID());
            }
            for (SiteEntity item : siteService.getSiteConfirm(form.getProvinceID())) {
                siteIds.add(item.getID());
            }

            //Lấy 5 quý gần nhất
            if (form.getIndicatorName().equals("htc")) {
                options.put("quarter", new LinkedHashMap<String, String>());
                while (options.get("quarter").size() < 5) {
                    int quarter = TextUtils.getQuarter(c.getTime()) + 1;
                    options.get("quarter").put(String.format("%s/%s", quarter, c.get(Calendar.YEAR)), String.format("Quý 0%s/%s", quarter, c.get(Calendar.YEAR)));
                    c.set(Calendar.MONDAY, c.get(Calendar.MONDAY) - 3);
                }
            } else {
                //Năm từ lúc có dữ liệu
                HtcVisitEntity visit = visitService.getFirst(siteIds);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                if (visit != null) {
                    c.setTime(visit.getAdvisoryeTime());
                }
                options.put("year", new LinkedHashMap<String, String>());
                for (int i = c.get(Calendar.YEAR); i <= year; i++) {
                    options.get("year").put(String.valueOf(i), String.format("Năm %s", i));
                }
            }

            //Chỉ sổ htc
            options.get("indicator").put("htc", "Tổng quan");
            options.get("indicator").put("htc-01", "Chỉ số: Số xét nghiệm và nhận kết quả");
            options.get("indicator").put("htc-02", "Chỉ số: Số dương tính và nhận kết quả");
            options.get("indicator").put("htc-03", "Chỉ số: Chuyển gửi điều trị");
            options.get("indicator").put("htc-04", "Chỉ số: Xét nghiệm nhiễm mới");
            options.get("indicator").put("htc-05", "Chỉ số: Xét nghiệm tải lượng virus");
        } else if (form.getIndicatorName().contains("pac")) {
            options.remove(ParameterEntity.SERVICE_TEST);
            options.remove(ParameterEntity.TEST_OBJECT_GROUP);
            options.put("district", new LinkedHashMap<String, String>());
            for (DistrictEntity item : locationsService.findDistrictByProvinceID(getCurrentUser().getSiteProvince().getID())) {
                options.get("district").put(item.getID(), item.getName());
            }

            options.get("indicator").put("pac", "Tổng quan");
            options.get("indicator").put("pac-01", "Chỉ số: Luỹ tích số người nhiễm");
            options.get("indicator").put("pac-02", "Chỉ số: Số người nhiễm HIV phát hiện mới");
            options.get("indicator").put("pac-03", "Chỉ số: Số người nhiễm còn sống");
            options.get("indicator").put("pac-04", "Chỉ số: Số người nhiễm tử vong");

            options.put(ParameterEntity.TEST_OBJECT_GROUP, new LinkedHashMap<String, String>());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey(), TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey(), TestObjectGroupEnum.PHU_NU_BAN_DAM.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.MSM.getKey(), TestObjectGroupEnum.MSM.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey(), TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getKey(), TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.PREGNANT_PERIOD.getKey(), TestObjectGroupEnum.PREGNANT_PERIOD.getLabel());
            options.get(ParameterEntity.TEST_OBJECT_GROUP).put(TestObjectGroupEnum.GIVING_BIRTH.getKey(), TestObjectGroupEnum.GIVING_BIRTH.getLabel());

            //Pac dùng năm
            if (form.getIndicatorName().equals("pac") || form.getIndicatorName().equals("pac-02")) {
                //Năm từ lúc có dữ liệu
                PacPatientInfoEntity patient = patientService.getFirst(getCurrentUser().getSite().getProvinceID());
                int year = Calendar.getInstance().get(Calendar.YEAR);
                if (patient != null) {
                    c.setTime(patient.getConfirmTime());
                }
                options.put("year", new LinkedHashMap<String, String>());
                if (form.getIndicatorName().equals("pac-02")) {
                } else if (form.getYear().equals("-1")) {
                    form.setYear(String.valueOf(year));
                }
                for (int i = c.get(Calendar.YEAR); i <= year; i++) {
                    options.get("year").put(String.valueOf(i), String.format("Năm %s", i));
                }
            }
        } else if (form.getIndicatorName().contains("opc")) {
            options.remove(ParameterEntity.SERVICE_TEST);
            options.remove(ParameterEntity.TEST_OBJECT_GROUP);
            options.get("indicator").put("opc", "Tổng quan");
            options.get("indicator").put("opc-01", "Chỉ số: Số lũy tích");
            options.get("indicator").put("opc-02", "Chỉ số: Số đang quản lý");
            options.get("indicator").put("opc-03", "Chỉ số: Số đang điều trị");
            options.get("indicator").put("opc-04", "Chỉ số: Số đăng ký mới");
            options.get("indicator").put("opc-05", "Chỉ số: Số xét nghiệm TLVR");
//            options.get("indicator").put("opc-06", "Chỉ số: Số bảo hiểm y tế");

            if (form.getIndicatorName().equals("opc")) {
                options.put("ages", new LinkedHashMap<String, String>());
                options.get("ages").put("1", "Trẻ em (<15 tuổi)");
                options.get("ages").put("2", "Người lớn (≥15 tuổi)");
            } else {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                if (form.getYear() == null || "".equals(form.getYear())) {
                    form.setYear(String.valueOf(year));
                }

                Set<Long> siteIds = new HashSet<>();
                for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
                    siteIds.add(item.getID());
                }

                OpcArvEntity arv = arvService.getFirst(siteIds);
                if (arv != null) {
                    c.setTime(arv.getRegistrationTime());
                }
                options.put("year", new LinkedHashMap<String, String>());
                for (int i = c.get(Calendar.YEAR); i <= year; i++) {
                    options.get("year").put(String.valueOf(i), String.format("Năm %s", i));
                }
            }

        } else if (form.getIndicatorName().contains("ql")) {
            options.get("indicator").put("ql", "Tổng quan");

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            if (form.getYear() == null || "".equals(form.getYear())) {
                form.setYear(String.valueOf(year));
            }
            if (form.getMonth()== null || "".equals(form.getMonth())) {
                form.setMonth(String.valueOf(month));
            }
           
            c.setTime(new Date());
            options.put("year", new LinkedHashMap<String, String>());
            for (int i = c.get(Calendar.YEAR); i > 1900; i--) {
                options.get("year").put(String.valueOf(i), String.format("Năm %s", i));
            }

            options.put("month", new LinkedHashMap<String, String>());
            for (int i = 1; i < 13; i++) {
                options.get("month").put(String.valueOf(i), String.format("Tháng %s", i));
            }

        }
        return options;
    }

    protected Map<String, Long> getTestObjects(Map<String, Long> item) {
        Map<String, Long> items = new LinkedHashMap<>();
        List<String> testObjectTT03 = new LinkedList<>();
        testObjectTT03.add(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
        testObjectTT03.add(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
        testObjectTT03.add(TestObjectGroupEnum.MSM.getKey());
        testObjectTT03.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        testObjectTT03.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getKey());
//        testObjectTT03.add(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey());
        Long tong = Long.valueOf("0");
        Long tong2 = Long.valueOf("0");
        Long pnmt = Long.valueOf("0");
        Long tanbinh = Long.valueOf("0");
        for (Map.Entry<String, Long> entry : item.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            tong = tong + value;
            if (key != null && !"".equals(key) && (key.equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey()) || key.equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey()) || key.equals(TestObjectGroupEnum.GIVING_BIRTH.getKey()))) {
                pnmt = pnmt + value;
                continue;
            }
            if (key != null && !"".equals(key) && (key.equals(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey()) || key.equals(TestObjectGroupEnum.TAN_BINH.getKey()))) {
                tanbinh = tanbinh + value;
                continue;
            }
            for (String string : testObjectTT03) {
                if (key != null && !"".equals(key) && string.equals(key)) {
                    items.put(options.get(ParameterEntity.TEST_OBJECT_GROUP).get(key), value);
                    tong2 = tong2 + value;
                }
            }
        }

        items.put(options.get(ParameterEntity.TEST_OBJECT_GROUP).get(TestObjectGroupEnum.PREGNANT_WOMEN.getKey()), pnmt);
        items.put(options.get(ParameterEntity.TEST_OBJECT_GROUP).get(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey()), tanbinh);
        items.put(options.get(ParameterEntity.TEST_OBJECT_GROUP).get(TestObjectGroupEnum.KHAC.getKey()), tong - pnmt - tong2 - tanbinh);
        return items;
    }

    protected List<String> getParameter(List<String> params, Map<String, String> lib) {
        if (params.size() == lib.values().size()) {
            return null;
        }
        return params;
    }
}
