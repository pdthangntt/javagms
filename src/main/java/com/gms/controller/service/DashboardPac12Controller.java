package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardPacImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * D1.2_Phát hiện mới.
 *
 * @author vvthanh
 */
@RestController
public class DashboardPac12Controller extends DashboardController {

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;

    @Autowired
    private DashboardPacImpl dashboardPacImpl;

    @RequestMapping(value = "/dashboard-pac/d12-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date toDateConvert = sdf.parse(form.getDay());
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfYear(toDateConvert), FORMATDATE);
        PacPatientInfoEntity pacPatientInfoEntity = pacPatientService.getFirst(form.getProvinceID());
        Date fromDate = new Date();
        if (pacPatientInfoEntity != null && pacPatientInfoEntity.getConfirmTime() != null) {
            fromDate = pacPatientInfoEntity.getConfirmTime();

        }
        String firstYear = TextUtils.formatDate(fromDate, "yyyy");
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        Map<String, HashMap<String, String>> options = getOptions(form);

        int year = Integer.parseInt(form.getYear());
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPac12Chart01(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")), year, getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))
        );
        int newYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(year == -1 ? TextUtils.convertDate(fromTime, FORMATDATE) : TextUtils.getFirstDayOfMonth(1, year));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(year == -1 ? TextUtils.convertDate(toTime, FORMATDATE) : year == newYear ? TextUtils.getLastDayOfMonth(new Date()) : TextUtils.getLastDayOfMonth(12, year));

        LinkedList<Map<String, Object>> result = new LinkedList<>();
        LinkedList<Map<String, Object>> results = new LinkedList<>();

        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = year == -1 ? TextUtils.formatDate(beginCalendar.getTime(), "yyyy") : TextUtils.formatDate(beginCalendar.getTime(), "M/yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("nam", "0");
                item.put("luytich", Long.parseLong("0"));
            }

            item.put("nam", String.format("%s", key));
            result.add(item);
            beginCalendar.add(year == -1 ? Calendar.YEAR : Calendar.MONTH, 1);
        }
        Map<String, Object> datas = new HashMap();
        datas.put("data", result);
        datas.put("time", year == -1 ? (TextUtils.formatDate(new Date(), "yyyy").equals(firstYear) ? String.format("năm %s", firstYear) : String.format("từ %s đến %s", firstYear, TextUtils.formatDate(new Date(), "yyyy"))) : String.format("năm %s", year));
        return new Response<>(true, datas);
    }

    /**
     * @author DSNAnh Get data d12 chart02
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/d12-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());

        Calendar firstTime = Calendar.getInstance();
        if (entity != null) {
            firstTime.setTime(entity.getConfirmTime());
        }
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);

        int year = Integer.valueOf(form.getYear());
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacD12Chart02(form.getProvinceID(),
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        if ("-1".equals(form.getYear())) {
            for (int i = 9; i >= 0; i--) {
                if (firstTime.get(Calendar.YEAR) > (now.get(Calendar.YEAR) - i)) {
                    continue;
                }
                String key = String.valueOf(now.get(Calendar.YEAR) - i);
                item = data.getOrDefault(key, null);
                if (item == null) {
                    item = new LinkedHashMap<>();
                    item.put("nam", key);
                    item.put("male", Long.valueOf("0"));
                    item.put("female", Long.valueOf("0"));
                    item.put("unclear", Long.valueOf("0"));
                }

                item.put("nam", key);
                result.add(item);
            }
        } else {
            for (String districtID : form.getDistrict()) {
                String key = districtID;
                item = data.getOrDefault(key, null);
                if (data.get(districtID) == null) {
                    item = new LinkedHashMap<>();
                    item.put("tenhuyen", locationsService.findDistrict(districtID).getName());
                    item.put("sotichluy", Long.valueOf("0"));
                    data.put(item.get("tenhuyen").toString(), item);
                }
                result.add(item);
            }
        }
        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-pac/d12-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) throws ParseException {
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());
        Integer toYear = null;
        Integer fromYear = null;

        Calendar firstTime = Calendar.getInstance();
        if (entity != null) {
            firstTime.setTime(entity.getConfirmTime());
        }

        try {
            if ("-1".equals(form.getYear())) {
                toYear = Calendar.getInstance().get(Calendar.YEAR);
                fromYear = toYear - 4;
            } else {
                toYear = Integer.parseInt(form.getYear());
            }
        } catch (NumberFormatException e) {
            toYear = Calendar.getInstance().get(Calendar.YEAR);
            fromYear = toYear - 4;
        }

        Map<String, Map<String, Object>> data = new LinkedHashMap<>();

        if (fromYear != null) {
            for (int i = fromYear; i <= toYear; i++) {

                if (firstTime.get(Calendar.YEAR) > i) {
                    continue;
                }

                String key = String.valueOf(i);

                Map<String, Map<String, Object>> dataEachYear = dashboardPacImpl.getPacD12Chart03(form.getProvinceID(),
                        getParameter(form.getDistrict(), options.get("district")), i,
                        getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                        getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                        getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));

                data.put(key, new HashMap<String, Object>());

                for (Map.Entry<String, Map<String, Object>> entry : dataEachYear.entrySet()) {
                    for (Map.Entry<String, Object> entry1 : entry.getValue().entrySet()) {
                        if (entry1.getKey().equals("gender")) {
                            continue;
                        }
                        data.get(key).put(entry1.getKey(), data.get(key).containsKey(entry1.getKey())
                                ? Integer.parseInt(data.get(key).get(entry1.getKey()).toString())
                                + Integer.parseInt(entry1.getValue().toString())
                                : Integer.parseInt(entry1.getValue().toString()));
                    }
                }
                data.get(key).put("year", key);
            }

            if (data == null || data.values().isEmpty()) {
                return new Response<>(false, null);
            }
            return new Response<>(true, new ArrayList<>(data.values()));
        }

        data = dashboardPacImpl.getPacD12Chart03(form.getProvinceID(),
                getParameter(form.getDistrict(), options.get("district")), toYear,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));
        return new Response<>(true, new ArrayList<>(data.values()));
    }

    @RequestMapping(value = "/dashboard-pac/d12-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) throws ParseException {

        Map<String, HashMap<String, String>> options = getOptions(form);
        Map<String, String> testObjectOption = new HashMap<>();
        for (ParameterEntity item : parameterService.getTestObjectGroup()) {
            testObjectOption.put(item.getCode(), item.getValue());
        }
        PacPatientInfoEntity entity = pacPatientService.getFirst(getCurrentUser().getSite().getProvinceID());
        int currentYear = Integer.parseInt(TextUtils.formatDate(new Date(), "yyyy"));
        int y = entity == null || entity.getConfirmTime() == null ? 0 : Integer.parseInt(TextUtils.formatDate(entity.getConfirmTime(), "yyyy"));

        List<Map<String, Object>> result = new ArrayList<>();

        int year = Integer.parseInt(form.getYear());
        Long tong2 = Long.valueOf("0");
        Map<String, Object> datas = new LinkedMap<>();
        if (year != -1) {
            datas = new LinkedMap<>();
            Map<String, Long> data = dashboardPacImpl.getPac12Chart04(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")), year, getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                    getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                    getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))
            );

            Long khac = Long.valueOf("0");
            Long pnmt = Long.valueOf("0");
            for (Map.Entry<String, Long> entry : data.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue();
                if (key.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())
                        || key.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())
                        || key.equals(TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey())
                        || key.equals(TestObjectGroupEnum.NGHI_NGO_AIDS.getKey())
                        || key.equals(TestObjectGroupEnum.LAO.getKey())
                        || key.equals(TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey())
                        || key.equals(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey())
                        || key.equals(TestObjectGroupEnum.MSM.getKey())) {
                    datas.put(testObjectOption.get(key), value);
                }
                if (key.equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey())
                        || key.equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey())
                        || key.equals(TestObjectGroupEnum.GIVING_BIRTH.getKey())) {
                    pnmt = pnmt + value;
                }
                if (!key.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())
                        && !key.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())
                        && !key.equals(TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey())
                        && !key.equals(TestObjectGroupEnum.NGHI_NGO_AIDS.getKey())
                        && !key.equals(TestObjectGroupEnum.LAO.getKey())
                        && !key.equals(TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey())
                        && !key.equals(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey())
                        && !key.equals(TestObjectGroupEnum.MSM.getKey())
                        && !key.equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey())
                        && !key.equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey())
                        && !key.equals(TestObjectGroupEnum.GIVING_BIRTH.getKey())) {
                    khac = khac + value;
                }
            }
            datas.put(testObjectOption.get(TestObjectGroupEnum.PREGNANT_WOMEN.getKey()), pnmt);
            datas.put(testObjectOption.get(TestObjectGroupEnum.KHAC.getKey()), khac);
        } else {
            for (int i = currentYear - y < 9 ? currentYear - y : 9; i >= 0; i--) {
                int num = Integer.parseInt(TextUtils.formatDate(new Date(), "yyyy")) - i;
                datas = new LinkedMap<>();

                Map<String, Long> data = dashboardPacImpl.getPac12Chart04(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")), num, getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                        getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                        getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))
                );

                Long khac = Long.valueOf("0");
                Long pnmt = Long.valueOf("0");
                for (Map.Entry<String, Long> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Long value = entry.getValue();
                    if (key.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())
                            || key.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())
                            || key.equals(TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey())
                            || key.equals(TestObjectGroupEnum.NGHI_NGO_AIDS.getKey())
                            || key.equals(TestObjectGroupEnum.LAO.getKey())
                            || key.equals(TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey())
                            || key.equals(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey())
                            || key.equals(TestObjectGroupEnum.MSM.getKey())) {
                        datas.put(key, value);
                    }
                    if (key.equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey())
                            || key.equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey())
                            || key.equals(TestObjectGroupEnum.GIVING_BIRTH.getKey())) {
                        pnmt = pnmt + value;
                    }
                    if (!key.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())
                            && !key.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())
                            && !key.equals(TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey())
                            && !key.equals(TestObjectGroupEnum.NGHI_NGO_AIDS.getKey())
                            && !key.equals(TestObjectGroupEnum.LAO.getKey())
                            && !key.equals(TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey())
                            && !key.equals(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey())
                            && !key.equals(TestObjectGroupEnum.MSM.getKey())
                            && !key.equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey())
                            && !key.equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey())
                            && !key.equals(TestObjectGroupEnum.GIVING_BIRTH.getKey())) {
                        khac = khac + value;
                    }
                }
                datas.put(TestObjectGroupEnum.PREGNANT_WOMEN.getKey(), pnmt);
                datas.put(TestObjectGroupEnum.KHAC.getKey(), khac);
                for (Map.Entry<String, Object> entry : datas.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    tong2 = tong2 + Long.valueOf(String.valueOf(value));

                }

                datas.put("nam", String.valueOf(num));

//                item.put(String.valueOf(num), datas);
                result.add(datas);
            }
        }

        Long tong = Long.valueOf("0");
        if (year != -1) {
            for (Map.Entry<String, Object> entry : datas.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                tong = tong + Long.valueOf(String.valueOf(value));
            }
        }
        Map<String, Object> dataMain = new HashMap();
        dataMain.put("object", options.get(ParameterEntity.TEST_OBJECT_GROUP));
        dataMain.put("data", year != -1 ? datas : result);
        dataMain.put("tong", year != -1 ? tong : tong2);
        dataMain.put("time", year == -1 ? (currentYear - y == 0 ? String.format("năm %s", y) : String.format("từ %s đến %s", currentYear - y < 9 ? y : Integer.parseInt(TextUtils.formatDate(new Date(), "yyyy")) - 9, TextUtils.formatDate(new Date(), "yyyy"))) : String.format("năm %s", year));
        if (entity == null || entity.getConfirmTime() == null) {
            return new Response<>(false, null);
        }
        return new Response<>(true, dataMain);
    }

    /**
     * @author DSNAnh Get data d12 chart05
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/d12-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());

        Calendar firstTime = Calendar.getInstance();
        if (entity != null) {
            firstTime.setTime(entity.getConfirmTime());
        }

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        int year = Integer.valueOf(form.getYear());
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacD12Chart05(form.getProvinceID(),
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Long> result2 = new TreeMap<>(Collections.reverseOrder());
        Map<String, Object> item;
        Long tong1 = Long.valueOf("0");
        if (year == -1) {
            if (entity.getConfirmTime() == null) {
                return new Response<>(false, null);
            }
            for (int i = 9; i >= 0; i--) {
                if (firstTime.get(Calendar.YEAR) > (now.get(Calendar.YEAR) - i)) {
                    continue;
                }
                String key = String.valueOf(now.get(Calendar.YEAR) - i);
                item = data.getOrDefault(key, null);
                if (item == null) {
                    item = new LinkedHashMap<>();
                    item.put("nam", key);
                    item.put("mau", Long.valueOf("0"));
                    item.put("tinhduc", Long.valueOf("0"));
                    item.put("mesangcon", Long.valueOf("0"));
                    item.put("khongro", Long.valueOf("0"));
                    item.put("truyenmau", Long.valueOf("0"));
                    item.put("tainannghenghiep", Long.valueOf("0"));
                    item.put("tiemchich", Long.valueOf("0"));
                    item.put("tdkhacgioi", Long.valueOf("0"));
                    item.put("tddonggioi", Long.valueOf("0"));
                    item.put("khongcothongtin", Long.valueOf("0"));
                }
                for (Map.Entry<String, Object> entry : item.entrySet()) {
                    String key1 = entry.getKey();
                    Object value = entry.getValue();
                    if (!key1.equals("nam")) {
                        tong1 = tong1 + Long.valueOf(String.valueOf(value));
                    }
                }

                item.put("nam", key);
                result.add(item);
            }
        } else {
            for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
                if (entry.getKey().equals("0")) {
                    result2.put("Không có thông tin", Long.valueOf(entry.getValue().get("soluong").toString()));
                    tong1 = tong1 + Long.valueOf(entry.getValue().get("soluong").toString());
                    continue;
                }
                if (options.get(ParameterEntity.MODE_OF_TRANSMISSION).get(entry.getKey()) == null) {
                    continue;
                }
                if (form.getModesOfTransmision().contains(entry.getKey())) {
                    result2.put(options.get(ParameterEntity.MODE_OF_TRANSMISSION).get(entry.getKey()), Long.valueOf(entry.getValue().get("soluong").toString()));
                    tong1 = tong1 + Long.valueOf(entry.getValue().get("soluong").toString());
                }
            }
        }
        Map<String, Object> datasr = new HashMap();
        datasr.put("tong", tong1);
        datasr.put("data", year == -1 ? result : result2);

        return new Response<>(true, datasr);
    }

}
