/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.htc_confirm.PositiveDistinctExcel;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveDistinctForm;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveDistinctTableForm;
import com.gms.service.HtcConfirmService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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
@Controller(value = "htc_confirm_positive_distinct")
public class HtcConfirmPositiveDistinctController extends BaseController {
    private static final String TEMPLATE_REPORT = "report/htc_confirm/positive-distinct-pdf.html";
    protected static final String EXTENSION_EXCEL = ".xls";

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private HtcConfirmService htcConfirmService;

    @Autowired
    SiteService siteService;

    /**
     * Lấy danh sách các nhóm đối tượng
     *
     * @return list
     */
    private HashMap<String, String> getObjectGroupOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.TEST_OBJECT_GROUP)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách kết quả XN sàng lọc
     *
     * @return list
     */
    private HashMap<String, String> getTestResultOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.TEST_RESULTS)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách cơ sở
     * @param entities
     * @return list
     */
    private HashMap<Long, String> getSite(List<HtcConfirmEntity> entities) {
        HashMap<Long, String> list = new HashMap<>();
        Set<Long> siteIDs = new HashSet<>();
        siteIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getSourceSiteID")));
        List<SiteEntity> sites = siteService.findByIDs(siteIDs);
        for (SiteEntity site : sites) {
            list.put(site.getID(), site.getName());
        }
        return list;
    }
    
    //Đếm số lần xuất hiện của phần tử trong mảng
    public Map<String, Long> countFrequencies(ArrayList<String> list) { 
        Map<String, Long> hm = new HashMap<>(); 
        for (String i : list) { 
            Long j = hm.get(i); 
            hm.put(i, (j == null) ? 1 : j + 1); 
        } 
        return hm;
    } 
    /**
     * Set giá trị cho form
     * @param startDate
     * @param endDate
     * @return form
     */
    private HtcConfirmPositiveDistinctForm getData(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmPositiveDistinctForm form = new HtcConfirmPositiveDistinctForm();
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Danh sách khách hàng dương tính phát hiện");
        form.setFileName("DSKHDuongTinhPhatHien_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));
        List<HtcConfirmEntity> models = htcConfirmService.findPositive(
                startDateConvert,
                endDateConvert,
                form.getSiteID());
        if (models != null && !models.isEmpty()) {
            // Lấy full địa chỉ
            models = htcConfirmService.getAddress(models);
            form.setTable(convertHtcConfirmPositiveTableForm(models));
        }
        return form;
    }

    /**
     * Set dữ liệu vào bảng trong form
     *
     * @param entitys
     * @return form
     */
    private List<HtcConfirmPositiveDistinctTableForm> convertHtcConfirmPositiveTableForm(List<HtcConfirmEntity> entitys) {
        List<HtcConfirmPositiveDistinctTableForm> data = new ArrayList<>();
        List<HtcConfirmPositiveDistinctTableForm> dataDistinct = new ArrayList<>();
        HtcConfirmPositiveDistinctTableForm form;
        ArrayList<String> identityList = new ArrayList<>();
        Map<String, HtcConfirmPositiveDistinctTableForm> identities = new HashMap<>();
        StringBuilder sb = null;
        for (HtcConfirmEntity model : entitys) {
            if(StringUtils.isNotEmpty(model.getFullname()) && StringUtils.isNotEmpty(model.getPatientID())){
                identityList.add(String.format("%s-%s",StringUtils.normalizeSpace(TextUtils.removeDiacritical(model.getFullname()).trim()), model.getPatientID().trim()));
            }
            form = new HtcConfirmPositiveDistinctTableForm();
            form.setFullName(model.getFullname());
            form.setYear(model.getYear());
            form.setGenderID(model.getGenderID());
            form.setIdentityCard(model.getPatientID());
            form.setAddress(model.getAddressFull());
            if (StringUtils.isNotEmpty(model.getObjectGroupID())) {
                sb = new StringBuilder();
                sb.append(getObjectGroupOption().getOrDefault(model.getObjectGroupID(), null));
                if ("5.1".equals(model.getObjectGroupID()) || "5.2".equals(model.getObjectGroupID())) {
                    sb.replace(0, 4, "");
                }
            }
            form.setObjectGroupID(StringUtils.isEmpty(model.getObjectGroupID()) ? StringUtils.EMPTY : model.getObjectGroupID() + ": " + sb.toString());
            form.setSourceReceiveSampleTime(model.getSourceReceiveSampleTime() == null ? "" : TextUtils.formatDate(model.getSourceReceiveSampleTime(), FORMATDATE));
            form.setTestResultsID(getTestResultOption().get(model.getTestResultsID()));
            form.setVisitTestCode(model.getSourceID());
            form.setSourceSiteID(getSite(entitys).get(model.getSourceSiteID()));
            form.setSampleReceiveTime(model.getSampleReceiveTime() == null ? "" : TextUtils.formatDate(model.getSampleReceiveTime(), FORMATDATE));
            form.setConfirmTime(model.getConfirmTime() == null ? "" : TextUtils.formatDate(model.getConfirmTime(), FORMATDATE));
            form.setCode(model.getCode());
            
            form.setSourceID(model.getSourceID());
            form.setEarlyBioName(StringUtils.isEmpty(model.getEarlyBioName()) ? "" : getOptions().get(ParameterEntity.BIOLOGY_PRODUCT_TEST).get(model.getEarlyBioName()));
            form.setEarlyHivDate(model.getEarlyHivDate() == null ? "" : TextUtils.formatDate(model.getEarlyHivDate(), FORMATDATE));
            form.setEarlyHiv(StringUtils.isEmpty(model.getEarlyHiv()) ? "" : getOptions().get(ParameterEntity.EARLY_HIV).get(model.getEarlyHiv()));
            form.setVirusLoadDate(model.getVirusLoadDate() == null ? "" : TextUtils.formatDate(model.getVirusLoadDate(), FORMATDATE));
            form.setVirusLoadNumber(model.getVirusLoadNumber());
            form.setVirusLoad(StringUtils.isEmpty(model.getVirusLoad()) ? "" : getOptions().get(ParameterEntity.VIRUS_LOAD).get(model.getVirusLoad()));
            form.setEarlyDiagnose(StringUtils.isEmpty(model.getEarlyDiagnose()) ? "" : getOptions().get(ParameterEntity.EARLY_DIAGNOSE).get(model.getEarlyDiagnose()));
            data.add(form);
        }
        Map<String, Long> recordNum = countFrequencies(identityList);
        for(HtcConfirmPositiveDistinctTableForm item : data){
            if(item.getIdentityCard() != null && item.getFullName() != null){
                if(item.getIdentityCard().equals("")){
                    continue;
                }
//                 && item.getFullName().equals("")
                String key = String.format("%s-%s",StringUtils.normalizeSpace(TextUtils.removeDiacritical(item.getFullName()).trim()), item.getIdentityCard().trim());
                if(!identities.containsKey(key)){
                    identities.put(key, item);
                }
            }
        }
        for(Map.Entry<String, HtcConfirmPositiveDistinctTableForm> entry: identities.entrySet()) {
            dataDistinct.add(entry.getValue());
        }
        for(HtcConfirmPositiveDistinctTableForm item : data){
            if(item.getIdentityCard() != null && item.getFullName() != null){
                if(item.getIdentityCard().equals("") ){
//                    || item.getFullName().equals("")
                    dataDistinct.add(item);
                }
            }
        }
        for(HtcConfirmPositiveDistinctTableForm item : dataDistinct){
            for(Map.Entry<String, Long> entry: recordNum.entrySet()) {
                if(StringUtils.isNotEmpty(item.getIdentityCard()) && StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(item.getFullName())){
                    if(entry.getKey().equalsIgnoreCase(String.format("%s-%s",StringUtils.normalizeSpace(TextUtils.removeDiacritical(item.getFullName()).trim()), item.getIdentityCard().trim()))){
                        if(entry.getValue() - 1 > 0){
                            item.setNote(String.format("%s: %s", "Số bản ghi trùng lắp", String.valueOf(entry.getValue() - 1)));
                        }
                    }
                }
            }
        }
        return dataDistinct;
    }
    
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }
    
    
    /**
     * Tab số người XN dương tính (DS dương tính khẳng định)
     * @param model
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-confirm-positive-distinct/index.html"})
    public String actionIndexDistinct(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        HtcConfirmPositiveDistinctForm form = getData(startDate, endDate);
        model.addAttribute("bread_crumb", "Khách hàng khẳng định dương tính");
        model.addAttribute("title", "Danh sách khách hàng dương tính phát hiện");
        model.addAttribute("form", form);
        return "report/htc_confirm/positive-distinct";
    }

    @GetMapping(value = {"/htc-confirm-positive-distinct/pdf.html"})
    public ResponseEntity<InputStreamResource> actionHtcConfirmPositivePdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        HtcConfirmPositiveDistinctForm form = getData(startDate, endDate);
        form.setSiteAgency(form.getSiteAgency());
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }

    @GetMapping(value = {"/htc-confirm-positive-distinct/excel.html"})
    public ResponseEntity<InputStreamResource> actionHtcConfirmPositiveExcel(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        return exportExcel(new PositiveDistinctExcel(getData(startDate, endDate), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc-confirm-positive-distinct/email.html"})
    public String actionHtcConfirmPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcConfirmPositiveDistinct());
        }
        HtcConfirmPositiveDistinctForm form = getData(startDate, endDate);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_HTC_CONFIRM_POSITIVE_DISTINCT,
                context,
                new PositiveDistinctExcel(form, EXTENSION_EXCEL));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcConfirmPositiveDistinct(startDate, endDate));
    }
}
