package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcLaytestLogEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.HtcLaytestForm;
import com.gms.entity.input.LaytestMERSearch;
import com.gms.entity.input.LaytestSearch;
import com.gms.service.CommonService;
import com.gms.service.ParameterService;
import com.gms.service.HtcLaytestService;
import com.gms.service.StaffService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TrangBN
 */
@RestController
public class HtcLaytestController extends BaseController {

    @Autowired
    private SiteService siteService;
    @Autowired
    ParameterService parameterService;
    @Autowired
    CommonService commonService;
    @Autowired
    StaffService staffService;

    @Autowired
    HtcLaytestService laytestService;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        
        // Lấy các cơ sở chuyển gửi cố định theo cấu hình cán bộ không chuyên
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(getCurrentUser().getUser().getID());
        //Nhóm đối tượng
        if (staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null) != null && !staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).equals("")) {
            options.put(ParameterEntity.TEST_OBJECT_GROUP, findOptions(options.get(ParameterEntity.TEST_OBJECT_GROUP), staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).split(",")));
        }
        HashMap<String, String> siteConfirms = new HashMap<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteConfirms.put(String.valueOf(site.getID()), site.getName());
        }
        options.put("siteConfirms",siteConfirms);
        return options;
    }

    /**
     * Tìm cơ sở xét nghiệm khẳng định theo tỉnh và dịch vụ tư vấn xét nghiệm
     *
     * @return
     */
    @RequestMapping(value = "/laytest/get-site-htc.json", method = RequestMethod.GET)
    public Response<?> actionGetByServiceAndProvince() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(getCurrentUser().getUser().getID());
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());

        if (staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null) != null && !staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null).equals("")) {
            List<String> skeys = Arrays.asList(staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null).split(","));
            List<SiteEntity> data = new ArrayList<>();
            for (SiteEntity site : sites) {
                if (skeys.contains(String.valueOf(site.getID()))) {
                    data.add(site);
                }
            }
            return new Response<>(true, null, data);
        }
        return new Response<>(true, null, sites);
    }

    @RequestMapping(value = "/laytest/log.json", method = RequestMethod.GET)
    public Response<?> actionLaytestLog(@RequestParam(name = "oid") Long oID) {
        List<HtcLaytestLogEntity> logs = laytestService.getLogs(oID);
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", logs);
        data.put("staffs", staffs);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/laytest/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody HtcLaytestLogEntity form, BindingResult bindingResult) {
        laytestService.log(form.getHtcLaytestID(), form.getContent());
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }

    @RequestMapping(value = "/laytest/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") Long oID) {
        LoggedUser currentUser = getCurrentUser();
        HtcLaytestEntity laytestEntity = laytestService.findOne(oID);
        if (laytestEntity == null || laytestEntity.isRemove() || !laytestEntity.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        Map<String, Object> data = new HashMap();
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(getCurrentUser().getUser().getID());
        
        // Lấy thông tin cơ sở cố định đã cấu hình cho cb không chuyên
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        List<SiteEntity> siteEntities = new ArrayList<>(sites);
        if (staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null) != null && !staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null).equals("")) {
            siteEntities = new ArrayList<>();
            List<String> skeys = Arrays.asList(staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null).split(","));
            for (SiteEntity site : sites) {
                if (skeys.contains(String.valueOf(site.getID()))) {
                    siteEntities.add(site);
                }
            }
        } 
        
        HashMap<String, HashMap<String, String>> options = getOptions();
        String visitSite = "visitSite";
        options.put(visitSite, new HashMap<String, String>());
        options.get(visitSite).put("", "Chọn cơ sở chuyển đến");
        for (SiteEntity site : siteEntities) {
            options.get(visitSite).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("options", options);
        data.put("model", laytestEntity);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/laytest/transferred.json", method = RequestMethod.POST)
    public Response<?> actionTransferred(@RequestParam(name = "oid") Long oID,
            @RequestBody HtcLaytestForm form) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();

        HtcLaytestEntity laytest = laytestService.findOne(oID);
        if (laytest == null || laytest.isRemove() || !laytest.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        if (form.getSiteVisitID() == null || "".equals(form.getSiteVisitID())) {
            errors.put("siteVisitID", "Cơ sở chuyển đến không được để trống");
        }

        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }
        laytest.setSampleSentDate(TextUtils.convertDate(form.getSampleSentDate(), FORMATDATE));
        laytest.setSiteVisitID(form.getSiteVisitID());

        //Lấy tên site
        SiteEntity sites = siteService.findOne(Long.parseLong(laytest.getSiteVisitID()));

        try {
            commonService.Transferred(laytest);
            laytestService.log(laytest.getID(), "Đã gửi thông tin khách hàng sang " + sites.getName());
            // gửi mail và thông báo
            String email = parameterService.getSiteConfig(Long.valueOf(laytest.getSiteVisitID()), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());
            StaffEntity staff = staffService.findOne(laytest.getUpdatedBY());
            if (staff != null) {
                sendEmailNotify(email,
                        String.format("Cán bộ xét nghiệm tại cộng đồng %s chuyển KH mã %s", staff.getName(), laytest.getCode()),
                        String.format(
                                "Khách hàng mã %s được chuyển gửi ngày %s",
                                laytest.getCode(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                        ));
//                staffService.sendMessage(laytest.getUpdatedBY(), "Chuyển gửi thông tin khách hàng", String.format("Mã khách hàng: %s", laytest.getCode()), String.format("%s?code=%s", UrlUtils.htcLaytestIndex(), laytest.getCode()));
            }

            return new Response<>(true, "Khách hàng đã được chuyển đến cơ sở xét nghiệm.", laytest);
        } catch (Exception e) {
            laytestService.log(laytest.getID(), "Lỗi gửi thông tin xét nghiệm. " + e.getMessage());
            return new Response<>(false, e.getMessage());
        }
    }

    /**
     * @auth pdThang
     * @param siteIDs
     * @return
     */
    public Map<String, String> getSite(Set<Long> siteIDs) {
        Map<String, String> options = new HashMap<>();
        if (siteIDs != null && siteIDs.size() > 0) {
            List<SiteEntity> sites = siteService.findByIDs(siteIDs);
            for (SiteEntity site : sites) {
                options.put(String.valueOf(site.getID()), site.getName());
            }
        }
        return options;
    }
    
    /**
     * Get data cho từng dòng (BC hoạt động KC)
     * @author DSNAnh
     * @param type
     * @param search
     * @return
     */
    @RequestMapping(value = {"/laytest/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearch(
            @RequestParam(name = "type", defaultValue = "", required = false) String type,
            @RequestBody LaytestSearch search) {
        search.setPageSize(search.getPageSize() == 0 ? 9999999 : search.getPageSize());
        search.setPageIndex(search.getPageIndex() == 0 ? 1 : search.getPageIndex());
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(getCurrentUser().getSite().getID());
        search.setRemove(false);
        search.setStaffID(getCurrentUser().getUser().getID());
        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<HtcLaytestEntity> models = null;
        switch (type) {
            case "tuvantruocxetnghiem":
                models = laytestService.findSoNguoiTuVanTruocXN(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getObjectGroupID());
                break;
            case "duocxetnghiemtong":
                models = laytestService.findSoNguoiTuVanTruocXN(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getObjectGroupID());
                break;
            case "duocxetnghiemduongtinh":
                models = laytestService.findSoNguoiDuocXNDuongTinh(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getConfirmTimeFrom(),
                        search.getConfirmTimeTo(),
                        search.getObjectGroupID());
                break;
            case "nhanketquatong":
                models = laytestService.findSoNguoiTuVanTruocXN(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getObjectGroupID());
                break;
            case "nhanketquaduongtinh":
                models = laytestService.findSoNguoiDuocXNDuongTinh(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getConfirmTimeFrom(),
                        search.getConfirmTimeTo(),
                        search.getObjectGroupID());
                break;
            case "gioithieubanchich":
                models = laytestService.findSoNguoiGioiThieuBanChich(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getObjectGroupID());
                break;
            case "chuyenguidieutri":
                models = laytestService.findSoNguoiChuyenGuiDieuTri(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getExchangeTimeFrom(),
                        search.getExchangeTimeTo(),
                        search.getObjectGroupID());
                break;
            default:
                models = laytestService.find(search).getData();
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }
    
    @RequestMapping(value = {"/laytest-mer/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearchMer(
            @RequestParam(name = "type", defaultValue = "", required = false) String type,
            @RequestBody LaytestMERSearch search) {
        search.setPageSize(search.getPageSize() == 0 ? 9999999 : search.getPageSize());
        search.setPageIndex(search.getPageIndex() == 0 ? 1 : search.getPageIndex());
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(getCurrentUser().getSite().getID());
        search.setStaffID(getCurrentUser().getUser().getID());
        if(search.getAge() != null && !"".equals(search.getAge())){
            search.setAge(search.getAge().replace(" tuổi", ""));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<HtcLaytestEntity> models = null;
        switch (type) {
            case "age":
                Integer minAge;
                Integer maxAge;
                if(search.getAge().equals("")){
                    models = laytestService.findUndefined(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),
                        search.getGenderID());
                }
                if(search.getAge().equals("< 1")){
                    models = laytestService.findUnderOne(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),
                        search.getGenderID());
                }
                if(!search.getAge().equals("") && !search.getAge().equals("< 1") && !search.getAge().equals("50+")){
                    minAge = Integer.parseInt(search.getAge().split("-")[0]);
                    maxAge = Integer.parseInt(search.getAge().split("-")[1]);
                    models = laytestService.findAgeRange(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),
                        search.getGenderID(),minAge , maxAge);
                }
                if(search.getAge().equals("50+")){
                    models = laytestService.findAgeRange(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),
                        search.getGenderID(),50 , 200);
                }
                break;
            case "ageSum":
                Integer min = 0;
                Integer max = 1;
                if(!search.getAge().equals("") && !search.getAge().equals("< 1") && !search.getAge().equals("50+")){
                    min = Integer.parseInt(search.getAge().split("-")[0]);
                    max = Integer.parseInt(search.getAge().split("-")[1]);
                }
                if(search.getAge().equals("50+")){
                    min = 50;
                    max = 200;
                }
                models = laytestService.findAgeRangeSum(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),min , max);
                break;
            case "objectGroup":
                if(search.getObjectGroupID().size() == 1 && search.getObjectGroupID().contains("")){
                    models = laytestService.findTable02MEROther(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID(),
                        search.getGenderID());
                } else{
                    models = laytestService.findTable02MER(
                            search.getSiteID(),
                            search.getStaffID(),
                            search.getAdvisoryeTimeFrom(),
                            search.getAdvisoryeTimeTo(),
                            search.getObjectGroupID(),
                            search.getConfirmResultsID(),
                            search.getGenderID());
                }
                break;
            case "objectGroupSum":
                if(search.getObjectGroupID().size() == 1 && search.getObjectGroupID().contains("")){
                    models = laytestService.findTable02MEROtherSum(
                        search.getSiteID(),
                        search.getStaffID(),
                        search.getAdvisoryeTimeFrom(),
                        search.getAdvisoryeTimeTo(),
                        search.getConfirmResultsID());
                } else{
                    models = laytestService.findTable02MERSum(
                            search.getSiteID(),
                            search.getStaffID(),
                            search.getAdvisoryeTimeFrom(),
                            search.getAdvisoryeTimeTo(),
                            search.getObjectGroupID(),
                            search.getConfirmResultsID());
                }
                break;
            default:
                models = null;
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }
}
