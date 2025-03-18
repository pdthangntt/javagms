package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardHtcImpl;
import com.gms.repository.impl.DashboardPacImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.ProvinceService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardPacController extends DashboardController {
    
    @Autowired
    private DashboardPacImpl dashboardPacImpl;
    
    @Autowired
    private PacPatientService pacPatientService;
    
    @Autowired
    private LocationsService locationsService;
    
    @RequestMapping(value = "/dashboard-pac/indicator.json", method = RequestMethod.POST)
    public Response<?> actionIndicator(@RequestBody DashboardForm form) {
        LoggedUser currentUser = getCurrentUser();
        
        form.setIndicatorName(form.getIndicatorName() == null ? "pac" : form.getIndicatorName());
        form.setYear(form.getYear() == null ? (form.getIndicatorName().equals("pac-02") ? "-1" : String.valueOf(Calendar.getInstance().get(Calendar.YEAR))) : form.getYear());
        form.setProvinceID(form.getProvinceID() == null ? currentUser.getSite().getProvinceID() : form.getProvinceID());
        
        Map<String, Object> result = new HashMap<>();
        result.put("options", getOptions(form));
        result.put("form", form);
        if (currentUser.getSiteProvince().getID().equals("82")) {
            result.put("quantityPatient", 2091);
        } else if (currentUser.getSiteProvince().getID().equals("72")) {
            result.put("quantityPatient", 3326);
        } else {
            result.put("quantityPatient", 5000);
        }
        return new Response<>(true, result);
    }

    /**
     * d1 char01: bản đồ nhiệt theo huyện đến năm theo đk tìm kiếm
     *
     * @auth vvThành
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/pac-chartgeo.json", method = RequestMethod.POST)
    public Response<?> actionChartGeo(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        int year = Integer.valueOf(form.getYear());
        return new Response<>(true, dashboardPacImpl.getPacChartGeo(form.getProvinceID(),
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP))));
    }
    
    /**
     * Biểu đồ 90-90-90
     * 
     * @param form
     * @return 
     */
    @RequestMapping(value = "/dashboard-pac/pac-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        
        int year = 0;
        int numPatient = 0;
        Map<String, HashMap<String, String>> options = getOptions(form);
        try {
            year = Integer.parseInt(form.getYear());
        } catch (NumberFormatException e) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            year = cal.get(Calendar.YEAR);
        }
        
        try {
            numPatient = Integer.parseInt(form.getQuantityPatient());
        } catch (NumberFormatException e) {
            numPatient = 0;
        }
        List<PacPatientInfoEntity> items =  pacPatientService.findViralLoads(year, form.getProvinceID());
        int totalViral = items == null || items.isEmpty() ? 0 : items.size();
        
        List<Map<String, Object>> data = dashboardPacImpl.getPacChart02(form.getProvinceID(), 
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)),
                numPatient, totalViral);
        
        String provinceName = locationsService.findProvince(form.getProvinceID()).getName();
        if (provinceName.contains("Tỉnh")) {
            provinceName = provinceName.replace("Tỉnh", "tỉnh");
        }
        
        return new Response<>(true, provinceName ,data);
    }
    
    /**
     * @author DSNAnh
     * Get data d1 chart04
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/pac-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        Calendar firstTime = Calendar.getInstance();
        if (entity != null) {
            firstTime.setTime(entity.getConfirmTime());
        }
        
        String now = TextUtils.formatDate(calendar.getTime(), "MM/yyyy");
        
        int year = Integer.valueOf(form.getYear());
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacChart04(form.getProvinceID(), 
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        Map<String, Object> lastItem;
        for (int i = 9; i >= 1; i--) {
            if (firstTime.get(Calendar.YEAR) > (year - i)) {
                continue;
            }
            String key = String.valueOf(year - i);
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new LinkedHashMap<>();
                item.put("nam", key);
                item.put("dangdieutri", Long.valueOf("0"));
                item.put("moidieutri", Long.valueOf("0"));
            }
            
            item.put("nam", key);
            result.add(item);
        }
        if (data.get(now) != null) {
            lastItem = data.get(now);
            lastItem.put("nam", now);
            result.add(lastItem);
        } else if (data.get(String.valueOf(year)) != null) {
            lastItem = data.get(String.valueOf(year));
            lastItem.put("nam", calendar.get(Calendar.YEAR) == year ? now : String.valueOf(year));
            result.add(lastItem);
        } else {
            lastItem = new LinkedHashMap<>();
            lastItem.put("nam", calendar.get(Calendar.YEAR) == year ? now : String.valueOf(year));
            lastItem.put("dangdieutri", Long.valueOf("0"));
            lastItem.put("moidieutri", Long.valueOf("0"));
            result.add(lastItem);
        }
        return new Response<>(true, result);
    }
    
    /**
     * @author DSNAnh
     * Get data d1 chart03
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-pac/pac-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        PacPatientInfoEntity entity = pacPatientService.getFirst(form.getProvinceID());
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        Calendar firstTime = Calendar.getInstance();
        if (entity != null) {
            firstTime.setTime(entity.getConfirmTime());
        }
        
        String now = TextUtils.formatDate(calendar.getTime(), "MM/yyyy");
        
        int year = Integer.valueOf(form.getYear());
        Map<String, Map<String, Object>> data = dashboardPacImpl.getPacChart03(form.getProvinceID(), 
                getParameter(form.getDistrict(), options.get("district")), year,
                getParameter(form.getGender(), options.get(ParameterEntity.GENDER)),
                getParameter(form.getModesOfTransmision(), options.get(ParameterEntity.MODE_OF_TRANSMISSION)),
                getParameter(form.getTestObjectGroup(), options.get(ParameterEntity.TEST_OBJECT_GROUP)));
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        Map<String, Object> lastItem;
        for (int i = 9; i >= 1; i--) {
            if (firstTime.get(Calendar.YEAR) > (year - i)) {
                continue;
            }
            String key = String.valueOf(year - i);
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new LinkedHashMap<>();
                item.put("nam", key);
                item.put("tichluy", Long.valueOf("0"));
                item.put("consong", Long.valueOf("0"));
                item.put("phathien", Long.valueOf("0"));
                item.put("tuvong", Long.valueOf("0"));
            }
            
            item.put("nam", key);
            result.add(item);
        }
        if (data.get(now) != null) {
            lastItem = data.get(now);
            lastItem.put("nam", now);
            result.add(lastItem);
        } else if (data.get(String.valueOf(year)) != null) {
            lastItem = data.get(String.valueOf(year));
            lastItem.put("nam", calendar.get(Calendar.YEAR) == year ? now : String.valueOf(year));
            result.add(lastItem);
        } else {
            lastItem = new LinkedHashMap<>();
            lastItem.put("nam", calendar.get(Calendar.YEAR) == year ? now : String.valueOf(year));
            lastItem.put("tichluy", Long.valueOf("0"));
            lastItem.put("consong", Long.valueOf("0"));
            lastItem.put("phathien", Long.valueOf("0"));
            lastItem.put("tuvong", Long.valueOf("0"));
            result.add(lastItem);
        }
        return new Response<>(true, result);
    }
    
}
