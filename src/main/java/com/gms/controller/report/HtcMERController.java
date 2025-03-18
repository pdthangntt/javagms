package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.htc.MerExcel;
import com.gms.entity.form.htc.MerForm;
import com.gms.entity.form.htc.MerTable01Form;
import com.gms.entity.form.htc.MerTable02Form;
import com.gms.entity.form.htc.MerTable03Form;
import com.gms.entity.form.htc.MerTable04Form;
import com.gms.entity.form.htc.MerTable05Form;
import com.gms.repository.impl.HtcMerRepositoryImpl;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
//@RestController
@Controller(value = "htc_export_mer")
public class HtcMERController extends BaseController {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private HtcMerRepositoryImpl htcMerRepositoryImpl;

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }
    
    /**
     * Danh sách dịch vụ thuộc cơ sở xét nghiệm theo ids
     *
     * @param serviceIDs
     * @return
     */
    private HashMap<String, String> getServiceOption(String[] serviceIDs) {
        List<String> lServiceIDs = null;
        if (serviceIDs != null && serviceIDs.length >= 1 && !serviceIDs[0].isEmpty()) {
            lServiceIDs = Arrays.asList(serviceIDs);
        }
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        List<ParameterEntity> serviceTest = parameterService.getServiceTest();
        for (ParameterEntity entity : serviceTest) {
            if (entity.getStatus() == 0 || (lServiceIDs != null && !lServiceIDs.contains(entity.getCode()))) {
                continue;
            }
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }
    
    /**
     * Danh sách đối tượng thuộc cơ sở xét nghiệm theo ids
     *
     * @param objectIDs
     * @return
     */
    private HashMap<String, String> getObjectOption(String[] objectIDs) {
        List<String> lobjectIDs = null;
        if (objectIDs != null && objectIDs.length >= 1 && !objectIDs[0].isEmpty()) {
            lobjectIDs = Arrays.asList(objectIDs);
        }
        HashMap<String, String> list = new HashMap<>();
        List<ParameterEntity> objectTest = parameterService.getTestObjectGroup();
        for (ParameterEntity entity : objectTest) {
            if (entity.getStatus() == 0 || (lobjectIDs != null && !lobjectIDs.contains(entity.getCode()))) {
                continue;
            }
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }
    
    /**
     * Hàm lấy dữ liệu cho MerForm
     * 
     * @param fromTime
     * @param toTime
     * @param serviceIDs
     * @return 
     */
    private MerForm getData(String fromTime, String toTime, String services, String objects) {
        Date date = new Date();
        fromTime = fromTime == null || fromTime.equals("") ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(date), FORMATDATE) : toTime;
        
        LoggedUser currentUser = getCurrentUser();
        MerForm form = new MerForm();
        form.setServices(getServiceOption(services.split(",", -1)));
        form.setObjects(getObjectOption(objects.split(",", -1)));
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setTitle("BÁO CÁO DỰ ÁN PEPFAR");
        form.setFileName("Bao_cao_PEPFAR_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteDistrict(getDistrictName(currentUser.getSite()));
        form.setSiteWard(getWardName(currentUser.getSite()));
        ArrayList<String> servicesResult = new ArrayList<>(form.getServices().keySet());
        HashMap<String, HashMap<String, MerTable01Form>> merTable01 = htcMerRepositoryImpl.getMerTable01(currentUser.getSite().getID(), servicesResult, fromTime, toTime, form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        HashMap<String, HashMap<String, MerTable02Form>> merTable02 = htcMerRepositoryImpl.getMerTable02(currentUser.getSite().getID(), servicesResult, fromTime, toTime, form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        HashMap<String, HashMap<String, MerTable03Form>> merTable03 = htcMerRepositoryImpl.getMerTable03(currentUser.getSite().getID(), servicesResult, fromTime, toTime, form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        HashMap<String, HashMap<String, MerTable04Form>> merTable04 = htcMerRepositoryImpl.getMerTable04(currentUser.getSite().getID(), servicesResult, fromTime, toTime, form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        HashMap<String, HashMap<String, MerTable05Form>> merTable05 = htcMerRepositoryImpl.getMerTable05(currentUser.getSite().getID(), servicesResult, fromTime, toTime, form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        
        form.setTable01Forms(merTable01);
        form.setTable02Forms(merTable02);
        form.setTable03Forms(merTable03);
        form.setTable04Forms(merTable04);
        form.setTable05Forms(merTable05);
        
        return form;
    }

    /**
     * Hiển thị trang danh sách báo cáo
     *
     * @author TrangBN
     * @param model
     * @param redirectAttributes
     * @param fromTime
     * @param toTime
     * @param services
     * @param objects
     * @return
     */
    @RequestMapping(value = {"/htc-mer/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "service", required = false, defaultValue = "") String services,
            @RequestParam(name = "objects", required = false, defaultValue = "") String objects) {
        List<ParameterEntity> objectTestActive = new ArrayList<>();
        for (ParameterEntity entity : parameterService.getTestObjectGroup()) {
            if(entity.getStatus() == 1){
                objectTestActive.add(entity);
            }
        }
        
        MerForm data = getData(fromTime, toTime, services, objects);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Báo cáo dự án PEPFAR");
        model.addAttribute("serviceOptions", getServiceOption(null));
        model.addAttribute("options", getOptions());
        model.addAttribute("search_service", data.getServices().keySet().toArray(new String[data.getServices().size()]));
        model.addAttribute("search_object", data.getObjects().keySet().size() == objectTestActive.size() ? "" : data.getObjects().keySet().toArray(new String[data.getObjects().size()]));
        model.addAttribute("form", data);
        return "report/htc/mer";
    }
    
    @GetMapping(value = {"/htc-mer/excel.html"})
    public ResponseEntity<InputStreamResource> actionMerExcel(
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        MerForm data = getData(fromTime, toTime, service, objects);
        return exportExcel(new MerExcel(data, EXTENSION_EXCEL, data.getServices()));
//        districtService.findByIDs(data.getDistrictID())
    }
    
    @GetMapping(value = {"/htc-mer/email.html"})
    public String actionMerEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcPositiveView());
        }
        MerForm form = getData(startDate, endDate, services, objects);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian từ ( %s - %s )", form.getTitle(), form.getStartDate(), form.getEndDate()),
                "mer_mail",
                context,
                new MerExcel(form, EXTENSION_EXCEL, form.getServices()));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcMER(services, startDate, endDate, objects));
    }
    
    /**
     * Lấy danh sách id huyện
     * (temporary ignore this method)
     * 
     * @param ids
     * @param tb1
     * @param tb2
     * @return 
     */
    private Set<String> getListDistrictID(HashMap<String, HashMap<String, MerTable04Form>>  tb1, 
            HashMap<String, HashMap<String, MerTable05Form>>  tb2){
        
        Set<String> ids = new HashSet<>();
        if (tb1 != null) {
            for (Map.Entry<String, HashMap<String, MerTable04Form>> entry : tb1.entrySet()) {
                ids.add(entry.getKey().split("-", -1)[0]);
            }
        }
        if (tb2 != null) {
            for (Map.Entry<String, HashMap<String, MerTable05Form>> entry : tb2.entrySet()) {
                ids.add(entry.getKey().split("-", -1)[0]);
            }
        }
        return ids;
    }
}
