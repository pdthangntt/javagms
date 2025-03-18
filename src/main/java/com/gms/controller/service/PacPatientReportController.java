package com.gms.controller.service;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.input.EarlyHivSearch;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.input.PacDeadSearch;
import com.gms.entity.input.PacLocalSearch;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.util.Map;

/**
 *
 * @author TrangBN
 */
@RestController
public class PacPatientReportController extends BaseController {

    @Autowired
    private LocationsService locationsService;
    @Autowired 
    private ParameterService parameterService;
    @Autowired 
    private PacPatientService pacPatientService;

    /**
     * Get data khi click vào bảng BC nhiễm mới
     * 
     * @author TrangBN
     * @param search
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = {"/pac-early-hiv/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearch( @RequestBody EarlyHivSearch search) throws ParseException {
        
        Map<String, Object> data = new HashMap<>();
        List<PacPatientInfoEntity> models = pacPatientService.getAddress(pacPatientService.getMetaData(pacPatientService.findPacEarlyHiv(search)));
        
        data.put("options", getOptions());
        if (models == null || models.isEmpty()) {
            return new Response<>(false,  "Không có dữ liệu", data);
        }
        
        data.put("models", models);
        return new Response<>(true,  null, data);
    }
    
    /**
     * Get data khi click vào bảng BC tử vong
     * 
     * @author TrangBN
     * @param search
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = {"/pac-dead/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearchDead( @RequestBody PacDeadSearch search) throws ParseException {
        
        Map<String, Object> data = new HashMap<>();
        List<PacPatientInfoEntity> models = pacPatientService.getAddress(pacPatientService.getMetaData(pacPatientService.findPacDeadHivs(search)));
        
        data.put("options", getOptions());
        if (models == null || models.isEmpty()) {
            return new Response<>(false,  "Không có dữ liệu", data);
        }
        
        data.put("models", models);
        return new Response<>(true,  null, data);
    }
    
    /**
     * Get data khi click vào bảng BC huyện xã
     * 
     * @author TrangBN
     * @param search
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = {"/pac-local/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearchLocal( @RequestBody PacLocalSearch search) throws ParseException {
        
        Map<String, Object> data = new HashMap<>();
        List<PacPatientInfoEntity> models = pacPatientService.getAddress(pacPatientService.getMetaData(pacPatientService.findPacLocals(search)));
        
        data.put("options", getOptions());
        
        if (models == null || models.isEmpty()) {
            return new Response<>(false,  "Không có dữ liệu", data);
        }
        data.put("models", models);
        return new Response<>(true,  null, data);
    }
    
    /**
     * Cơ quan chủ quản
     *
     * @param site
     * @return
     */
    protected String getSiteAgency(SiteEntity site) {
        String agency = site.getParentAgency();
        if (agency == null || agency.isEmpty()) {
            String provinceName = getProvinceName(site);
            if (provinceName != null && !provinceName.equals("")) {
                agency = String.format("SỞ Y TẾ %s", provinceName);
            }
        }
        return agency;
    }
    
     /**
     * Kiểm tra cơ sở hiện tại có phải Trung tâm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_DISTRICT
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TTYT.getKey());
    }
    
    /**
     * Kiểm tra cơ sở hiện tại có phải PAC không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isPAC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getType() == SiteEntity.TYPE_PROVINCE
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.PAC.getKey());
    }    
    
    /**
     * 
     * Lấy danh sách options
     * @return 
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        
        // Lấy các cơ sở chuyển gửi cố định theo cấu hình cán bộ không chuyên
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(getCurrentUser().getUser().getID());
        //Nhóm đối tượng
        if (staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null) != null && !staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).equals("")) {
            options.put(ParameterEntity.TEST_OBJECT_GROUP, findOptions(options.get(ParameterEntity.TEST_OBJECT_GROUP), staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).split(",")));
        }
//        HashMap<String, String> siteConfirms = new HashMap<>();
//        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), getCurrentUser().getSite().getProvinceID());
//        for (SiteEntity site : sites) {
//            siteConfirms.put(String.valueOf(site.getID()), site.getName());
//        }
//        options.put("siteConfirms",siteConfirms);
        return options;
    }
    
    /**
     * Lấy tên tỉnh theo ID
     *
     * @param site
     * @return
     */
    protected String getProvinceName(SiteEntity site) {
        ProvinceEntity province = locationsService.findProvince(site.getProvinceID());
        return province == null ? "" : province.getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim();
    }
    
    /**
     * Kiểm tra cơ sở hiện tại có phải Trạm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_WARD
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TYT.getKey());
    }
    
    
    
}
