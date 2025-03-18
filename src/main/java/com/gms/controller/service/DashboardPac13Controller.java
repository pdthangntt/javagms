package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardPacImpl;
import com.gms.service.PacPatientService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
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
 * D1.3_Còn sống
 *
 * @author vvthanh
 */
@RestController
public class DashboardPac13Controller extends DashboardController {

    @Autowired
    LocationsService locationsService;
    @Autowired
    private DashboardPacImpl dashboardPacImpl;
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;

    @RequestMapping(value = "/dashboard-pac/d13-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date toDateConvert;
        try {
            toDateConvert = sdf.parse(form.getDay());
        } catch (Exception e) {
            toDateConvert = sdf.parse(TextUtils.formatDate(new Date(), FORMATDATE));
        }
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfYear(toDateConvert), FORMATDATE);

        PacPatientInfoEntity pacPatientInfoEntity = pacPatientService.getFirst(form.getProvinceID());
        Date fromDate = new Date();
        if (pacPatientInfoEntity != null && pacPatientInfoEntity.getConfirmTime() != null) {
            fromDate = pacPatientInfoEntity.getConfirmTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        Map<String, HashMap<String, String>> options = getOptions(form);
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPac13Chart01(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")),fromDate, toDateConvert, getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))
        );
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        LinkedList<Map<String, Object>> result = new LinkedList<>();
        LinkedList<Map<String, Object>> results = new LinkedList<>();

        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = TextUtils.formatDate(beginCalendar.getTime(), "yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("nam", "0");
                item.put("luytich", Long.parseLong("0"));
            }

            item.put("nam", String.format("%s", key));
            result.add(item);
            beginCalendar.add(Calendar.YEAR, 1);
        }
//        Map<String, Object> item1;
//        int luytich = 0;
//        for (Map<String, Object> entry : result) {
//            item1 = new HashMap<>();
//            for (Map.Entry<String, Object> entry1 : entry.entrySet()) {
//
//                String key = entry1.getKey();
//                Object value = entry1.getValue();
//                if ("nam".equals(key)) {
//                    item1.put(key, value);
//                }
//                if ("luytich".equals(key)) {
//                    item1.put(key, Long.valueOf(value.toString()) + luytich);
//                    luytich += Long.valueOf(value.toString());
//
//                }
//
//            }
//            results.add(item1);
//        }
        return new Response<>(true, result);
    }

    /**
     * @author DSNAnh
     * Get data d13 chart02
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/d13-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        String now = TextUtils.formatDate(new Date(), FORMATDATE);
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());

        Calendar fromTime = Calendar.getInstance();
        if (entity != null) {
            fromTime.setTime(entity.getConfirmTime());
        }

        Calendar toTime = Calendar.getInstance();
        toTime.setTime(TextUtils.convertDate(form.getDay().isEmpty() ? now : form.getDay(), FORMATDATE));

        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacD13Chart02(form.getProvinceID(), form.getDistrict(), toTime.getTime(),
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;

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
        return new Response<>(true, result);
    }

    /**
     * Số người nhiễm HIV còn sống theo giới tính và nhóm tuổi
     * 
     * @auth TrangBN
     * @param form
     * @return
     * @throws ParseException 
     */
    @RequestMapping(value = "/dashboard-pac/d13-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) throws ParseException {
        Map<String, HashMap<String, String>> options = getOptions(form);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date toDate;
        try {
            toDate = sdf.parse(form.getDay());
        } catch (ParseException e) {
            toDate = sdf.parse(TextUtils.formatDate(new Date(), FORMATDATE));
        }
        
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacD13Chart03(form.getProvinceID(), 
                getParameter(form.getDistrict(), options.get("district")), toDate,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));
        return new Response<>(true, new ArrayList<>(data.values()));
    }

    @RequestMapping(value = "/dashboard-pac/d13-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date toDateConvert;
        try {
            toDateConvert = sdf.parse(form.getDay());
        } catch (Exception e) {
            toDateConvert = sdf.parse(TextUtils.formatDate(new Date(), FORMATDATE));
        }
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfYear(toDateConvert), FORMATDATE);

        PacPatientInfoEntity pacPatientInfoEntity = pacPatientService.getFirst(form.getProvinceID());
        Date fromDate = new Date();
        if (pacPatientInfoEntity != null && pacPatientInfoEntity.getConfirmTime() != null) {
            fromDate = pacPatientInfoEntity.getConfirmTime();
        }
        Map<String, HashMap<String, String>> options = getOptions(form);
        Map<String, String> testObjectOption = new HashMap<>();
        for (ParameterEntity item : parameterService.getTestObjectGroup()) {
            testObjectOption.put(item.getCode(), item.getValue());
        }
        Map<String, Long> data = dashboardPacImpl.getPac13Chart04(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")), toDateConvert, getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))
        );
        Map<String, Long> datas = new LinkedMap<>();
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

        return new Response<>(true, datas);
    }

    /**
     * @author DSNAnh
     * Get data d13 chart05
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/d13-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {
        String now = TextUtils.formatDate(new Date(), FORMATDATE);
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());

        Calendar fromTime = Calendar.getInstance();
        if (entity != null) {
            fromTime.setTime(entity.getConfirmTime());
        }

        Calendar toTime = Calendar.getInstance();
        toTime.setTime(TextUtils.convertDate(form.getDay().isEmpty() ? now : form.getDay(), FORMATDATE));

        Map<String, Long> data = dashboardPacImpl.getPacD13Chart05(form.getProvinceID(), getParameter(form.getDistrict(), options.get("district")), fromTime.getTime(), toTime.getTime(),
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));

        Map<String, Long> result = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            
            String key = entry.getKey();
            Long value = entry.getValue();
            if (key != null && !"".equals(key)) {
                if("0".equals(key)){
                    result.put("Không có thông tin", value);
                    continue;
                }
                if(options.get(ParameterEntity.MODE_OF_TRANSMISSION).get(key) == null){
                    continue;
                }
                result.put(options.get(ParameterEntity.MODE_OF_TRANSMISSION).get(key), value);
            }
        }
        return new Response<>(true, result);
    }
}
