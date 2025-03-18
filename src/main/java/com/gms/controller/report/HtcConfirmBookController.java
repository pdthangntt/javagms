/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.htc_confirm.ConfirmBookExcel;
import com.gms.entity.form.htc_confirm.ConfirmBookForm;
import com.gms.entity.form.htc_confirm.ConfirmBookTableForm;
import com.gms.service.HtcConfirmService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DSNAnh
 */
@Controller(value = "htc_confirm_book")
public class HtcConfirmBookController extends BaseController {
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    SiteService siteService;
    
    private static final String TEMPLATE_REPORT = "report/htc_confirm/book-pdf.html";
    private static final String EXCEL_EXTENSION = ".xlsx";

    /**
     * Convert to form for display table
     *
     * @param findHtcPositive
     * @return
     */
    private ConfirmBookTableForm convertTableBookForm(HtcConfirmEntity entity) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ConfirmBookTableForm form = new ConfirmBookTableForm();
        form.setCode(entity.getCode());
        form.setFullname(entity.getFullname());
        form.setYearOfBirth(entity.getYear());
        form.setGender(entity.getGenderID());
        form.setAddressFull(entity.getAddressFull());
        form.setObjectGroupID(options.get(ParameterEntity.TEST_OBJECT_GROUP).getOrDefault(entity.getObjectGroupID(), null));
        form.setSourceSiteID(entity.getSourceSiteID() == null ? "" : options.get("siteTests").getOrDefault(String.valueOf(entity.getSourceSiteID()), ""));
        if(entity.getConfirmTime() != null){
            form.setConfirmTime(TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE));
        }
        form.setResultID(options.get(ParameterEntity.TEST_RESULTS_CONFIRM).getOrDefault(entity.getResultsID(), ""));
        form.setBioName1("".equals(entity.getBioName1()) ? "" : options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST).getOrDefault(entity.getBioName1(), ""));
        form.setBioNameResult1("".equals(entity.getBioNameResult1()) ? "" : options.get(ParameterEntity.BIO_NAME_RESULT).getOrDefault(entity.getBioNameResult1(), ""));
        if(entity.getFirstBioDate() != null){
            form.setFirstBioDate(TextUtils.formatDate(entity.getFirstBioDate(), FORMATDATE));
        }
        form.setBioName2("".equals(entity.getBioName2()) ? "" : options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST).getOrDefault(entity.getBioName2(), ""));
        form.setBioNameResult2("".equals(entity.getBioNameResult2()) ? "" : options.get(ParameterEntity.BIO_NAME_RESULT).getOrDefault(entity.getBioNameResult2(), ""));
        if(entity.getSecondBioDate() != null){
            form.setSecondBioDate(TextUtils.formatDate(entity.getSecondBioDate(), FORMATDATE));
        }
        form.setBioName3("".equals(entity.getBioName3()) ? "" : options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST).getOrDefault(entity.getBioName3(), ""));
        form.setBioNameResult3("".equals(entity.getBioNameResult3()) ? "" : options.get(ParameterEntity.BIO_NAME_RESULT).getOrDefault(entity.getBioNameResult3(), ""));
        if(entity.getThirdBioDate() != null){
            form.setThirdBioDate(TextUtils.formatDate(entity.getThirdBioDate(), FORMATDATE));
        }
        form.setSampleSaveCode(entity.getSampleSaveCode());
        form.setNote("");
        form.setSourceID(entity.getSourceID());
        form.setVirusLoad("".equals(entity.getVirusLoad()) ? "" : options.get(ParameterEntity.VIRUS_LOAD).getOrDefault(entity.getVirusLoad(), ""));
        if(entity.getVirusLoadDate() != null){
            form.setVirusLoadDate(TextUtils.formatDate(entity.getVirusLoadDate(), FORMATDATE));
        }
        form.setEarlyHiv("".equals(entity.getEarlyHiv()) ? "" : options.get(ParameterEntity.EARLY_HIV).getOrDefault(entity.getEarlyHiv(), ""));
        if(entity.getEarlyHivDate() != null){
            form.setEarlyHivDate(TextUtils.formatDate(entity.getEarlyHivDate(), FORMATDATE));
        }
        if(entity.getAcceptDate()!= null){
            form.setAcceptDate(TextUtils.formatDate(entity.getAcceptDate(), FORMATDATE));
        }
        return form;
    }
    
    
    
    /**
     * Load all options to display name
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
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), getCurrentUser().getSite().getProvinceID());//Sắp xếp để hiển thị tìm kiếm Đơn vị gửi mẫu
        Map<String, String> sitesMap = new HashMap<>();
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
        sortedMap.put("", "Tất cả");
        ArrayList<String> list = new ArrayList<>();
        for (SiteEntity site : sites) {
            sitesMap.put(String.valueOf(site.getID()), site.getShortName());
        }
        for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String str, String str1) {
                return (str).compareTo(str1);
            }
        });
        for (String str : list) {
            for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }
        options.put("siteTests", sortedMap);
        return options;
    }
    
    /**
     * Search list of the positive
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return
     */
    private ConfirmBookForm getData(String startDate, String endDate, String fullname, String code, String sourceID, Long sourceSiteID, String resultsID) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);
        
        LoggedUser currentUser = getCurrentUser();
        ConfirmBookForm form = new ConfirmBookForm();
        form.setOptions(getOptions());
        form.getOptions().remove(ParameterEntity.GENDER);
        form.getOptions().remove(ParameterEntity.RACE);
        
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Sổ xét nghiệm HIV");
        form.setFileName("SoXN_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        
        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));
        if (fullname != null && !"".equals(fullname)) {
            form.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (code != null && !"".equals(code)) {
            form.setCode(code.trim());
        }
        if (sourceID != null && !"".equals(sourceID)) {
            form.setSourceID(sourceID.trim());
        }
        form.setSourceSiteID(sourceSiteID);
        form.setResultID(resultsID);
        
        List<HtcConfirmEntity> models = htcConfirmService.findConfirmBook(
                startDateConvert,
                endDateConvert,
                form.getSiteID(),
                form.getFullname(),
                form.getCode(),
                form.getSourceID(),
                form.getSourceSiteID(),
                form.getResultID()
    );            
        
        if (models != null && !models.isEmpty()) {
            List<ConfirmBookTableForm> data = new ArrayList<>();
            for (HtcConfirmEntity model : models) {
                data.add(convertTableBookForm(model));
            }
            form.setTable(data);
        }
        return form;
    }
    
    
    /**
     * Home page xuất sổ xét nghiệm HIV
     * @param model
     * @param startDate
     * @param endDate
     * @param fullname
     * @param code
     * @param sourceID
     * @param sourceSiteID
     * @param resultsID
     * @return
     * @throws Exception 
     */
    
    @GetMapping(value = {"/confirm-book/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "fullname", defaultValue = "", required = false) String fullname,
            @RequestParam(name = "code", defaultValue = "", required = false) String code,
            @RequestParam(name = "sourceID", defaultValue = "", required = false) String sourceID,
            @RequestParam(name = "sourceSiteID", required = false) Long sourceSiteID,
            @RequestParam(name = "resultID", required = false, defaultValue = "") String resultsID) throws Exception {
        
        ConfirmBookForm form = getData(startDate, endDate, fullname, code, sourceID, sourceSiteID, resultsID);
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("title", "Sổ xét nghiệm khẳng định");
        model.addAttribute("title_small", "Sổ tư vấn xét nghiệm");
        model.addAttribute("form", form); 
        return "report/htc_confirm/confirmbook";
        
    }
    
    @GetMapping(value = {"/confirm-book/excel.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookExcel(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "fullname", defaultValue = "", required = false) String fullname,
            @RequestParam(name = "code", defaultValue = "", required = false) String code,
            @RequestParam(name = "sourceID", defaultValue = "", required = false) String sourceID,
            @RequestParam(name = "sourceSiteID", required = false) Long sourceSiteID,
            @RequestParam(name = "resultID", required = false, defaultValue = "") String resultsID) throws Exception {
        return exportExcel(new ConfirmBookExcel(getData(startDate, endDate, fullname, code, sourceID, sourceSiteID, resultsID), EXCEL_EXTENSION));
    }
    
    @GetMapping(value = {"/confirm-book/email.html"})
    public String actionBookEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "fullname", defaultValue = "", required = false) String fullname,
            @RequestParam(name = "code", defaultValue = "", required = false) String code,
            @RequestParam(name = "sourceID", defaultValue = "", required = false) String sourceID,
            @RequestParam(name = "sourceSiteID", required = false) Long sourceSiteID,
            @RequestParam(name = "resultID", required = false, defaultValue = "") String resultsID) throws Exception {
        
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcConfirmBook());
        }
        ConfirmBookForm form = getData(startDate, endDate, fullname, code, sourceID, sourceSiteID, resultsID);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_POSITIVE,
                context,new ConfirmBookExcel(form, EXTENSION_EXCEL));
        
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcConfirmBook(startDate, endDate, fullname, code, sourceID, sourceSiteID, resultsID));
    }
    
    @GetMapping(value = {"/confirm-book/pdf.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookPdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "fullname", defaultValue = "", required = false) String fullname,
            @RequestParam(name = "code", defaultValue = "", required = false) String code,
            @RequestParam(name = "sourceID", defaultValue = "", required = false) String sourceID,
            @RequestParam(name = "sourceSiteID", required = false) Long sourceSiteID,
            @RequestParam(name = "resultID", required = false, defaultValue = "") String resultsID) throws Exception {
        
        ConfirmBookForm form = getData(startDate, endDate, fullname, code, sourceID, sourceSiteID, resultsID);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        
        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }
}
