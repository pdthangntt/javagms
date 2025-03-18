package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.entity.form.pac.PacQLTableForm;
import com.gms.entity.form.pac.PacQLTablePForm;
import com.gms.repository.impl.DashboardQlImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.ibm.icu.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardQLController extends DashboardController {

    @Autowired
    private DashboardQlImpl dashboardQlImpl;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService patientService;

    @RequestMapping(value = "/dashboard-ql/indicator.json", method = RequestMethod.POST)
    public Response<?> actionIndicator(@RequestBody DashboardForm form) {
        form.setIndicatorName(form.getIndicatorName() == null ? "ql" : form.getIndicatorName());
        form.setYear(form.getYear() == null || form.getYear().equals("") || form.getYear().equals("-1") ? String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) : form.getYear());

        Map<String, Object> result = new HashMap<>();
        result.put("options", getOptions(form));
        result.put("form", form);
        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-ql/ql-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) {
        List<ProvinceEntity> provinces = locationsService.findAllProvince();

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;

        Map<String, Map<String, String>> data = dashboardQlImpl.getChart01(1, Integer.valueOf(form.getMonth()), Integer.valueOf(form.getYear()));

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.add(java.util.Calendar.MONTH, Integer.valueOf(form.getMonth()));
        c.add(java.util.Calendar.YEAR, Integer.valueOf(form.getYear()));
        String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(c.getTime()), "yyyy-MM-dd");

        for (ProvinceEntity province : provinces) {
            item = new HashMap<>();
            item.put("id", province.getID());
            item.put("name", province.getName());
            item.put("htc", 0);
            item.put("early", 0);
            item.put("quantity", 0);

            Map<String, String> map = data.get(province.getID());
            if (map != null) {
                int htc = Integer.valueOf(map.getOrDefault("htc", "0"));
                int early = Integer.valueOf(map.getOrDefault("early", "0"));
                item.put("htc", htc);
                item.put("early", early);
                item.put("quantity", patientService.countProvincePhatHienConSong(province.getID(), to));
            }
            result.add(item);
        }
        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-ql/ql-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        List<ProvinceEntity> provinces = locationsService.findAllProvince();

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;

        Map<String, Map<String, String>> data = dashboardQlImpl.getChart04(1, Integer.valueOf(form.getMonth()), Integer.valueOf(form.getYear()));

        for (ProvinceEntity province : provinces) {
            item = new HashMap<>();
            item.put("id", province.getID());
            item.put("name", province.getName());
            item.put("phat_hien", 0);
            item.put("xn_moi_nhiem", 0);
            item.put("ty_le_xn_moi_nhiem", 0);
            item.put("co_phan_ung", 0);
            item.put("ti_le_duong_tinh", 0);

            Map<String, String> map = data.get(province.getID());
            if (map != null) {
                int phat_hien = Integer.valueOf(map.getOrDefault("phat_hien", "0"));
                int xn_moi_nhiem = Integer.valueOf(map.getOrDefault("xn_moi_nhiem", "0"));
                int co_phan_ung = Integer.valueOf(map.getOrDefault("co_phan_ung", "0"));

                item.put("phat_hien", phat_hien);
                item.put("xn_moi_nhiem", xn_moi_nhiem);
                item.put("co_phan_ung", co_phan_ung);
                if (phat_hien > 0) {
                    //  Tỷ lệ XN nhiễm mới  = [Số làm XN nhiễm mới] / [Số phát hiện mới]  * 100
                    item.put("ty_le_xn_moi_nhiem", Math.round((Double.valueOf(xn_moi_nhiem) / Double.valueOf(phat_hien)) * 100 * 100.0) / 100.0);
                }
                if (xn_moi_nhiem > 0) {
                    //    Tỷ lệ dương tính  = [Số có phản ứng]  /  [Số làm xét nghiệm nhiễm mới]  * 100
                    item.put("ti_le_duong_tinh", Math.round((Double.valueOf(co_phan_ung) / Double.valueOf(xn_moi_nhiem)) * 100 * 100.0) / 100.0);
                }
            }

            result.add(item);
        }

        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-ql/ql-chart02-detail.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "province_id") String provinceID,
            @RequestParam(name = "time") String time,
            @RequestParam(name = "level") String level) {

        Map<String, PacQLTableForm> mapDs = new HashMap<>();
        List<DistrictEntity> districts = locationsService.findDistrictByProvinceID(provinceID);
        PacQLTableForm table;
        for (DistrictEntity district : districts) {
            table = new PacQLTableForm();
            table.setDistrictID(district.getID());
            table.setDistrictName(district.getName());
            table.setMonth1(0);
            table.setMonth2(0);
            table.setMonth3(0);
            table.setValueD(0);
            table.setValueE(0);
            mapDs.put(district.getID(), table);
        }

        List<PacQLTableForm> data = dashboardQlImpl.getChart02(provinceID, "district", level.equals("1") ? time : null, level.equals("2") ? time : null, level.equals("3") ? time : null);

        Map<String, Integer> colors = dashboardQlImpl.getChart02_colorOfDistrict(provinceID, level.equals("1") ? time : null, level.equals("2") ? time : null, level.equals("3") ? time : null, level);
        Map<String, Integer> earlys = dashboardQlImpl.getChart02_EarlyDistrict(provinceID, level.equals("1") ? time : null, level.equals("2") ? time : null, level.equals("3") ? time : null, level);

        for (PacQLTableForm p : data) {
            if (mapDs.getOrDefault(p.getDistrictID(), null) != null) {
                mapDs.get(p.getDistrictID()).setMonth1(mapDs.get(p.getDistrictID()).getMonth1() + p.getMonth1());
                mapDs.get(p.getDistrictID()).setMonth2(mapDs.get(p.getDistrictID()).getMonth2() + p.getMonth2());
                mapDs.get(p.getDistrictID()).setMonth3(mapDs.get(p.getDistrictID()).getMonth3() + p.getMonth3());

                mapDs.get(p.getDistrictID()).setValueD(mapDs.get(p.getDistrictID()).getValueD() + (level.equals("1") ? p.getMonth1() : level.equals("2") ? p.getMonth2() : p.getMonth3()));

                mapDs.get(p.getDistrictID()).setColorD(colors.getOrDefault(p.getDistrictID(), 1));
                mapDs.get(p.getDistrictID()).setValueE(earlys.getOrDefault(p.getDistrictID(), 0));

            }
        }

        Map<String, Object> datas = new HashMap<>();
        datas.put("data", mapDs);
        return new Response<>(true, datas);
    }

    @RequestMapping(value = "/dashboard-ql/ql-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {

        String levelDisplay = isVAAC() ? "province" : "district";
        String provinceID = getCurrentUser().getSite().getProvinceID();

        Date referenceDate = TextUtils.convertDate("02/" + (Integer.valueOf(form.getMonth()) > 9 ? form.getMonth() : '0' + form.getMonth()) + "/" + form.getYear(), FORMATDATE);
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        Date month1 = c.getTime();
        c.add(Calendar.MONTH, -1);
        Date month2 = c.getTime();
        c.add(Calendar.MONTH, -1);
        Date month3 = c.getTime();
        System.out.println("cccc " + form.getMonth() + "Cccc" + form.getYear());
        System.out.println("month " + TextUtils.formatDate(month1, FORMATDATE) + " : " + TextUtils.formatDate(month2, FORMATDATE) + " : " + TextUtils.formatDate(month3, FORMATDATE));
        Map<String, String> months = new HashMap<>();
        months.put("1", TextUtils.formatDate(month3, "M/yyyy"));
        months.put("2", TextUtils.formatDate(month2, "M/yyyy"));
        months.put("3", TextUtils.formatDate(month1, "M/yyyy"));

        List<ProvinceEntity> provinces = locationsService.findAllProvince();
        PacQLTableForm table;
        Map<String, PacQLTableForm> map = new HashMap<>();
        for (ProvinceEntity province : provinces) {
            table = new PacQLTableForm();
            table.setProvinceID(province.getID());
            table.setProvinceName(province.getName().replaceAll("Thành phố", "TP"));
            if (table.getProvinceName().equals("Tỉnh Thừa Thiên Huế") || table.getProvinceName().equals("Tỉnh Bà Rịa - Vũng Tàu")) {
                table.setProvinceName(table.getProvinceName().replaceAll("Tỉnh", ""));
            }

            table.setMonth1(0);
            table.setMonth2(0);
            table.setMonth3(0);
            map.put(province.getID(), table);
        }

        List<PacQLTableForm> data = dashboardQlImpl.getChart02(provinceID, levelDisplay, months.get("1"), months.get("2"), months.get("3"));
        for (PacQLTableForm p : data) {
            //đếm bôi màu
            if (map.getOrDefault(p.getProvinceID(), null) != null) {
                map.get(p.getProvinceID()).setMonth1(map.get(p.getProvinceID()).getMonth1() + p.getMonth1());
                map.get(p.getProvinceID()).setMonth2(map.get(p.getProvinceID()).getMonth2() + p.getMonth2());
                map.get(p.getProvinceID()).setMonth3(map.get(p.getProvinceID()).getMonth3() + p.getMonth3());

            }
        }

        Map<String, PacQLTableForm> mIDs = new HashMap<>();
        List<PacQLTableForm> items = new ArrayList<>();
        for (Map.Entry<String, PacQLTableForm> entry : map.entrySet()) {
            String key = entry.getKey();
            PacQLTableForm value = entry.getValue();
            //Set màu cho tỉnh
            Map<String, Integer> colors = dashboardQlImpl.getChart02_colorOfprovince(value.getProvinceID(), months.get("1"), months.get("2"), months.get("3"));
            value.setColor1(colors.get("1"));
            value.setColor2(colors.get("2"));
            value.setColor3(colors.get("3"));
            items.add(value);
        }

        List<PacQLTablePForm> datas = new ArrayList<>();

        int targetSize = 3;
        PacQLTablePForm tb;
        List<List<PacQLTableForm>> output = ListUtils.partition(items, targetSize);
        for (List<PacQLTableForm> list : output) {
            tb = new PacQLTablePForm();
            tb.setProvinceName1(list.get(0).getProvinceName());
            tb.setProvinceName2(list.get(1).getProvinceName());
            tb.setProvinceName3(list.get(2).getProvinceName());

            tb.setProvinceID1(list.get(0).getProvinceID());
            tb.setProvinceID2(list.get(1).getProvinceID());
            tb.setProvinceID3(list.get(2).getProvinceID());

            tb.setMonth1(list.get(0).getMonth1());
            tb.setMonth2(list.get(0).getMonth2());
            tb.setMonth3(list.get(0).getMonth3());

            tb.setMonth4(list.get(1).getMonth1());
            tb.setMonth5(list.get(1).getMonth2());
            tb.setMonth6(list.get(1).getMonth3());

            tb.setMonth7(list.get(2).getMonth1());
            tb.setMonth8(list.get(2).getMonth2());
            tb.setMonth9(list.get(2).getMonth3());

            tb.setColor1(list.get(0).getColor1());
            tb.setColor2(list.get(0).getColor2());
            tb.setColor3(list.get(0).getColor3());
            tb.setColor4(list.get(1).getColor1());
            tb.setColor5(list.get(1).getColor2());
            tb.setColor6(list.get(1).getColor3());
            tb.setColor7(list.get(2).getColor1());
            tb.setColor8(list.get(2).getColor2());
            tb.setColor9(list.get(2).getColor3());

            datas.add(tb);
        }

        //Huyện
        Map<String, PacQLTableForm> mapDs = new HashMap<>();
        List<DistrictEntity> districts = locationsService.findDistrictByProvinceID(provinceID);
        for (DistrictEntity district : districts) {
            table = new PacQLTableForm();
            table.setDistrictID(district.getID());
            table.setDistrictName(district.getName());
            table.setMonth1(0);
            table.setMonth2(0);
            table.setMonth3(0);
            mapDs.put(district.getID(), table);
        }

        for (PacQLTableForm p : data) {
            if (mapDs.getOrDefault(p.getDistrictID(), null) != null) {
                mapDs.get(p.getDistrictID()).setMonth1(mapDs.get(p.getDistrictID()).getMonth1() + p.getMonth1());
                mapDs.get(p.getDistrictID()).setMonth2(mapDs.get(p.getDistrictID()).getMonth2() + p.getMonth2());
                mapDs.get(p.getDistrictID()).setMonth3(mapDs.get(p.getDistrictID()).getMonth3() + p.getMonth3());
            }
        }
        List<PacQLTableForm> lisstDs = new ArrayList<>();

        Map<String, Integer> colors1 = dashboardQlImpl.getChart02_colorOfDistrict(provinceID, months.get("1"), null, null, "1");
        Map<String, Integer> colors2 = dashboardQlImpl.getChart02_colorOfDistrict(provinceID, null, months.get("2"), null, "2");
        Map<String, Integer> colors3 = dashboardQlImpl.getChart02_colorOfDistrict(provinceID, null, null, months.get("3"), "3");

        for (Map.Entry<String, PacQLTableForm> entry : mapDs.entrySet()) {
            String key = entry.getKey();
            PacQLTableForm value = entry.getValue();
            value.setColor1(colors1.getOrDefault(value.getDistrictID(), 1));
            value.setColor2(colors2.getOrDefault(value.getDistrictID(), 1));
            value.setColor3(colors3.getOrDefault(value.getDistrictID(), 1));

            lisstDs.add(value);

        }

        months.put("1", months.get("1").length() == 6 ? ("0" + months.get("1")) : months.get("1"));
        months.put("2", months.get("2").length() == 6 ? ("0" + months.get("2")) : months.get("2"));
        months.put("3", months.get("3").length() == 6 ? ("0" + months.get("3")) : months.get("3"));

        Map<String, Object> item = new HashMap<>();
        item.put("provinces", datas);
        item.put("districts", lisstDs);
        item.put("levelDisplay", levelDisplay);
        item.put("months", months);
        return new Response<>(true, item);

    }

    @RequestMapping(value = "/dashboard-ql/ql-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03() {
        String province_id = null;
        if (!isVAAC()) {
            province_id = getCurrentUser().getSiteProvince().getID();
        }
        Map<String, Map<String, String>> data = dashboardQlImpl.getChart03(province_id, null, null);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            item = new HashMap<>();
            item.put("nam", entry.getKey());
            item.put("nhiem_moi", entry.getValue().get("nhiem_moi"));
            item.put("tu_vong", entry.getValue().get("tu_vong"));
            result.add(item);
        }
        return new Response<>(true, result);
    }

}
