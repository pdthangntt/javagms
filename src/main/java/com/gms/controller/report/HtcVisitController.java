package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.LaoVariableEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.htc.AgreeDiscloseForm;
import com.gms.entity.form.htc.AgreeTestForm;
import com.gms.entity.form.htc.Appendix02Form;
import com.gms.entity.form.htc.SampleSentTableForm;
import com.gms.entity.form.htc.TicketResultForm;
import com.gms.entity.form.htc.TicketSampleSentForm;
import com.gms.entity.form.htc.TransferOPCForm;
import com.gms.entity.form.htc.VisitAnswerResultForm;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
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
 * @author vvthanh
 */
//@RestController
@Controller(value = "htc_export_visit")
public class HtcVisitController extends BaseController {

    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private LocationsService locationService;

    private final String INTRODUCTION_REASON = "Đăng ký chương trình chăm sóc điều trị";
    private final String PREFIX_SITE_NAME = "Cơ sở xét nghiệm";

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.LAO_VARIABLE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        addEnumOption(options, ParameterEntity.LAO_VARIABLE, LaoVariableEnum.values(), "Chọn thể lao"); 

        return options;
    }

    /**
     *
     * @author DSNAnh
     * @return gender list
     */
    private HashMap<String, String> getGenderOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.GENDER)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * @author DSNAnh
     * @param dID
     * @param pID
     * @return reception address
     */
    private String getReceptionAddress(String dID, String pID) {
        StringBuilder address = new StringBuilder();
        if (pID != null && dID != null && locationService.findDistrict(dID) != null && locationService.findProvince(pID) != null) {
            address.append(locationService.findDistrict(dID).getName());
            address.append(", ");
            address.append(locationService.findProvince(pID).getName());
        } else {
            return StringUtils.EMPTY;
        }
        return address.toString();
    }

    /**
     * In phiếu chuyển gửi
     *
     * @param model
     * @param oID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc/transfer-opc.html"})
    public String actionTransferOPC(Model model,
            @RequestParam(name = "oid") Long oID) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        TransferOPCForm form = new TransferOPCForm();
        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(oID);
        form.setSiteID(htcVisitEntity.getSiteID());
        form.setIntroduceSite(String.format(PREFIX_SITE_NAME + " - %s", currentUser.getSite().getName()));
        form.setPatientName(htcVisitEntity.getPatientName());
        form.setYearOfBirth(htcVisitEntity.getYearOfBirth());
        form.setPatientID(htcVisitEntity.getCode());
        form.setGenderID(getGenderOption().get(htcVisitEntity.getGenderID()));
        form.setAddress(htcVisitEntity.getPermanentAddressFull());
        form.setIntroductionReason(INTRODUCTION_REASON);
        form.setArrivalSite(htcVisitEntity.getArrivalSite());
        form.setReceptionAddress(getReceptionAddress(htcVisitEntity.getExchangeDistrictID(), htcVisitEntity.getExchangeProvinceID()));
        form.setContactName("");
        form.setPhone("");

        form.setTitle("GIẤY GIỚI THIỆU");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(siteService.findOne(currentUser.getSite().getID()).getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setFileName("GiayGioiThieu_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
//        HashMap<String, Object> context = new HashMap<>();
//        context.put("baseUrl", view.getURLBase());
//        context.put("form", form);
//        context.put("visit", htcVisitEntity);
//        context.put("config", parameterService.getSiteConfig(currentUser.getSite().getID()));
//
//        htcVisitService.log(htcVisitEntity.getID(), "In phiếu chuyển gửi điều trị");
//        return exportPdf(form.getFileName(), "report/htc/transfer-opc.html", context);
        HashMap<String, Object> context = new HashMap<>();
        model.addAttribute("baseUrl", view.getURLBase());
        model.addAttribute("form", form);
        model.addAttribute("visit", htcVisitEntity);
        model.addAttribute("config", parameterService.getSiteConfig(currentUser.getSite().getID()));

        htcVisitService.log(htcVisitEntity.getID(), "In phiếu chuyển gửi điều trị");
        return print("report/htc/transfer-opc.html", model);
    }

    /**
     * In phiếu hẹn
     *
     * @author pdThang
     * @param model
     * @param oID
     * @return
     * @throws Exception
     */
    @GetMapping("/htc/ticket-result.html")
    public String actionTicketResult(Model model,
            @RequestParam(name = "oid") Long oID) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(oID);

        TicketResultForm form = new TicketResultForm();
        form.setSiteID(htcVisitEntity.getSiteID());
        form.setPatientName(htcVisitEntity.getPatientName());
        form.setPatientCode(htcVisitEntity.getCode());
        if (htcVisitEntity.getPreTestTime() != null) {
            form.setPreTestTime(TextUtils.formatDate(htcVisitEntity.getPreTestTime(), FORMATDATE));
            //Cộng ngày trong java
            String receiveTime = parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_RETURN_DAY.getKey());
            if (!"".equals(receiveTime)) {
                form.setReceiveTime(TextUtils.formatDate(TextUtils.getDay(htcVisitEntity.getPreTestTime(), Integer.valueOf(receiveTime)), FORMATDATE));
            }
        }
        htcVisitService.log(htcVisitEntity.getID(), "In phiếu nhận kết quả xét nghiệm sàng lọc");

        form.setTitle("PHIẾU HẸN TRẢ LỜI KẾT QUẢ XÉT NGHIỆM");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setFileName("PhieuHen_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(currentUser.getUser().getID());
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_ADDTESS.getKey(), "").equals("")) {
            form.setSiteAddress(staffConfig.get(StaffConfigEnum.SITE_ADDTESS.getKey()));
        } else {
            form.setSiteAddress(siteService.getAddress(currentUser.getSite()));
        }
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_PHONE.getKey(), "").equals("")) {
            form.setSitePhone(staffConfig.get(StaffConfigEnum.SITE_PHONE.getKey()));
        } else {
            form.setSitePhone(currentUser.getSite().getPhone());
        }
        model.addAttribute("form", form);
        model.addAttribute("visit", htcVisitEntity);
        return print("report/htc/ticket-result.html", model);
    }

    /**
     * In phiếu gửi mẫu XN
     *
     * @param model
     * @param oids
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc/ticket-sample-sent.html"})
    public ResponseEntity<InputStreamResource> actionTicketSampleSent(Model model,
            @RequestParam(name = "oid") String oids) throws Exception {

        TicketSampleSentForm form = getData(oids);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(form.getFileName(), "report/htc/transfer-sample-sent-pdf.html", context);
    }

    /**
     * Lấy thông tin khách hảng gửi mẫu XN
     *
     * @author TrangBN
     * @param ids
     * @return
     */
    private TicketSampleSentForm getData(String ids) {
        LoggedUser currentUser = getCurrentUser();

        TicketSampleSentForm ticketSampleSentForm = new TicketSampleSentForm();

        ticketSampleSentForm.setTitle("PHIẾU GỬI MẪU XÉT NGHIỆM HIV");
        ticketSampleSentForm.setFileName("PhieuGuiMauXNHIV_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        ticketSampleSentForm.setSiteName(currentUser.getSite().getName());
        ticketSampleSentForm.setStaffName(currentUser.getUser().getName());
        ticketSampleSentForm.setSiteAgency(getSiteAgency(currentUser.getSite()));
        ticketSampleSentForm.setSiteProvince(getProvinceName(currentUser.getSite()));

        // Lấy danh sách đối tượng gửi mẫu xét nghiệm 
        Set<Long> htcIds = new HashSet<>();

        String[] split = ids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            htcIds.add(Long.parseLong(string));
        }

        if (htcIds.isEmpty()) {
            return ticketSampleSentForm;
        }

        List<HtcVisitEntity> entities = htcVisitService.findAllByIDs(htcIds);

        if (entities.isEmpty()) {
            return ticketSampleSentForm;
        }

        Set<Long> siteIds = new HashSet<>();
        entities = htcVisitService.getAddress(entities);
        List<HtcVisitEntity> results = new ArrayList<>();
        for (HtcVisitEntity entity : entities) {
            if (((entity.getIsAgreeTest() != null && entity.getIsAgreeTest() == true
                    && StringUtils.isNotEmpty(entity.getSiteConfirmTest())))
                    && entity.getSampleSentDate() != null) {
                results.add(entity);
                if (StringUtils.isNotEmpty(entity.getSiteConfirmTest())) {
                    siteIds.add(Long.parseLong(entity.getSiteConfirmTest()));
                }
            }
        }

        if (results.isEmpty()) {
            return ticketSampleSentForm;
        }

        // Setting options for sites
        HashMap<String, String> siteOptionsParam = new HashMap<>();
        for (SiteEntity findByID : siteService.findByIDs(siteIds)) {
            siteOptionsParam.put(findByID.getID().toString(), findByID.getName());
        }
        if (!siteOptionsParam.isEmpty()) {
            ticketSampleSentForm.setSiteOptions(siteOptionsParam);
        }

        HashMap<String, List<SampleSentTableForm>> resultForms = new HashMap<>();

        if (entities != null) {
            SampleSentTableForm sampleSentTableForm = new SampleSentTableForm();
            for (HtcVisitEntity object : results) {
                if (resultForms.containsKey(object.getSiteConfirmTest())) {
                    resultForms.get(object.getSiteConfirmTest()).add(setFromHtcVisitEntity(sampleSentTableForm, object));
                } else {
                    resultForms.put(object.getSiteConfirmTest(), new ArrayList<SampleSentTableForm>());
                    resultForms.get(object.getSiteConfirmTest()).add(setFromHtcVisitEntity(sampleSentTableForm, object));
                }
            }
        }

        // Sort by ngày lấy mẫu (sourceReceiveSampleTime)
        for (Map.Entry<String, List<SampleSentTableForm>> entry : resultForms.entrySet()) {
            Collections.sort(entry.getValue(), new Comparator<SampleSentTableForm>() {
                @Override
                public int compare(SampleSentTableForm o1, SampleSentTableForm o2) {
                    return o1.getSourceReceiveSampleTime().compareTo(o2.getSourceReceiveSampleTime());
                }
            });
            resultForms.put(entry.getKey(), entry.getValue());
        }

        ticketSampleSentForm.setListSampleSent(resultForms);
        return ticketSampleSentForm;
    }

    /**
     * Lấy option kết quả xét nghiệm sàng lọc
     *
     * @return
     */
    private HashMap<String, String> getResultsOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.getTestResult()) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách các nhóm đối tượng
     *
     * @author TrangBN
     * @return list
     */
    private HashMap<String, String> getObjectGroupOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.getTestObjectGroup()) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Set dữ liệu cho SampleSentTableForm
     *
     * @param sampleSentTableForm
     * @param object
     * @return
     */
    public SampleSentTableForm setFromHtcVisitEntity(SampleSentTableForm sampleSentTableForm, HtcVisitEntity object) {
        sampleSentTableForm = new SampleSentTableForm();
        sampleSentTableForm.setID(object.getID());
        sampleSentTableForm.setAddress(StringUtils.isNotEmpty(object.getPermanentAddressFull()) ? object.getPermanentAddressFull() : "");
        sampleSentTableForm.setFullname(StringUtils.isNotEmpty(object.getPatientName()) ? object.getPatientName() : "");
        sampleSentTableForm.setResultsID(StringUtils.isNotEmpty(object.getTestResultsID()) ? (object.getTestResultsID()) : "");
        sampleSentTableForm.setRoomTestNo(object.getCode());
        sampleSentTableForm.setSourceReceiveSampleTime(object.getPreTestTime() != null ? object.getPreTestTime() : null);
        sampleSentTableForm.setYear(StringUtils.isNotEmpty(object.getYearOfBirth()) ? object.getYearOfBirth() : "");
        sampleSentTableForm.setGender(getGenderOption().get(object.getGenderID()));
        sampleSentTableForm.setObjectGroupID(getObjectGroupOption().get(object.getObjectGroupID()));
        sampleSentTableForm.setBioName("".equals(object.getBioName()) ? null : object.getBioName());
        sampleSentTableForm.setPatientID(object.getPatientID());
        sampleSentTableForm.setCode(object.getCode());
        return sampleSentTableForm;
    }

    /**
     * Phiếu trả kết quả không phản ứng XNSL
     *
     * @auth TrangBN
     * @param model
     * @param oid
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc/answer-result.html"})
    public ResponseEntity<InputStreamResource> actionVisitAnswerResult(Model model,
            @RequestParam(name = "oid") Long oid,
            RedirectAttributes redirectAttributes) throws Exception {

        VisitAnswerResultForm form = getVisitData(oid);
        if (form == null) {
            return null;
        }
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), "report/htc/answer-result.html", context);
    }

    /**
     * Lấy thông tin người xét nghiệm sàng lọc kết quả âm tính
     *
     * @param ids
     * @return
     */
    private VisitAnswerResultForm getVisitData(Long id) {
        // Lấy thông tin khách hàng âm tính sàng lọc HIV
        HtcVisitEntity entity = htcVisitService.findOne(id);

        // Set thông tin địa chỉ full
        if (entity != null) {
            List<HtcVisitEntity> entities = new ArrayList<>();
            entities.add(entity);
            entity = htcVisitService.getAddress(entities).get(0);
        } else {
            entity = new HtcVisitEntity();
        }

        LoggedUser currentUser = getCurrentUser();
        HashMap<String, String> siteConfig = parameterService.getSiteConfig(currentUser.getSite().getID());

        VisitAnswerResultForm visit = new VisitAnswerResultForm();
        visit.setConfig(parameterService.getSiteConfig(currentUser.getSite().getID()));
        visit.setTitle("KẾT QUẢ XÉT NGHIỆM KHÁNG THỂ KHÁNG HIV");
        visit.setFileName("PhieuKetQuaAmTinhXNHIV_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        visit.setSiteName(currentUser.getSite().getName());
        visit.setSiteAgency(getSiteAgency(currentUser.getSite()));

        String siteName = siteConfig.getOrDefault(SiteConfigEnum.HTC_CSCQ_ANSWER_RESULT.getKey(), currentUser.getSite().getName());
        if (siteName != null && !StringUtils.isEmpty(siteName)) {
            visit.setSiteName(siteName);
        }

        HashMap<String, String> staffConfig = parameterService.getStaffConfig(currentUser.getUser().getID());
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_ADDTESS.getKey(), "").equals("")) {
            visit.setSiteAddress(staffConfig.get(StaffConfigEnum.SITE_ADDTESS.getKey()));
        } else {
            visit.setSiteAddress(siteService.getAddress(currentUser.getSite())); 
        }
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_PHONE.getKey(), "").equals("")) {
            visit.setSitePhone(staffConfig.get(StaffConfigEnum.SITE_PHONE.getKey()));
        } else {
            visit.setSitePhone(currentUser.getSite().getPhone());
        }

        visit.setPatientName(entity.getPatientName());
        visit.setGenderID(StringUtils.isNotEmpty(entity.getGenderID()) ? getOptions().get(ParameterEntity.GENDER).get(entity.getGenderID()) : StringUtils.EMPTY);
        visit.setFullAddress(entity.getPermanentAddressFull());
        visit.setYearOfBirth(entity.getYearOfBirth());
        visit.setPreTestTime(entity.getPreTestTime() != null ? TextUtils.formatDate(entity.getPreTestTime(), FORMATDATE) : StringUtils.EMPTY);
        visit.setIdentityNo(StringUtils.isNotEmpty(entity.getPatientID()) ? entity.getPatientID() : StringUtils.EMPTY);
        visit.setBioName(StringUtils.isNotEmpty(entity.getBioName()) ? getOptions().get(ParameterEntity.BIOLOGY_PRODUCT_TEST).get(entity.getBioName()) : StringUtils.EMPTY);
        visit.setTestResult(StringUtils.isNotEmpty(entity.getTestResultsID()) && TestResultEnum.KHONG_PHAN_UNG.getKey().equals(entity.getTestResultsID()) ? "ÂM TÍNH" : null);
        visit.setCode(entity.getCode());
        visit.setPatientIDAuthen(entity.getPatientIDAuthen());
        visit.setObjectGroupID(entity.getObjectGroupID());
        return visit;
    }

    @GetMapping(value = {"/htc/agree-test.html"})
    public ResponseEntity<InputStreamResource> actionAgreeTest(Model model,
            @RequestParam(name = "oid") String oid,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        AgreeTestForm form = new AgreeTestForm();

        form.setTitle("PHIẾU XÁC NHẬN ĐỒNG Ý XÉT NGHIỆM HIV");
        form.setFileName("PhieuXacNhanDongYXN_HIV_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteAddress(currentUser.getSite().getAddress());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        HtcVisitEntity item = htcVisitService.findOne(Long.parseLong(oid));

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("item", item);
        List<String> riskBehaviors = new ArrayList<>();
        for (String o : item.getRiskBehaviorID()) {
            riskBehaviors.add(getOptions().get(ParameterEntity.RISK_BEHAVIOR).get(o));
        }
        context.put("risk", StringUtils.join(riskBehaviors, ", "));
        context.put("options", getOptions());
        context.put("is_cscq", parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_TICKET_CSCQ.getKey()));
        context.put("is_code", parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_TICKET_CODE.getKey()));

        boolean date = TextUtils.formatDate(new Date(), FORMATDATE).equals(TextUtils.formatDate(item.getAdvisoryeTime(), FORMATDATE));
        context.put("date", date);

        return exportPdf(form.getFileName(), "report/htc/agree-test.html", context);
    }

    @GetMapping(value = {"/htc/agree-test-shift.html"})
    public String actionAgreeTestShift(Model model,
            @RequestParam(name = "oid") String oid,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        AgreeTestForm form = new AgreeTestForm();
        form.setTitle("PHIẾU XÁC NHẬN ĐỒNG Ý XÉT NGHIỆM HIV - SHIFT");
        form.setFileName("PhieuXacNhanDongYXN_HIV_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteAddress(currentUser.getSite().getAddress());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setPatientCodeDsp(parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_TICKET_CODE.getKey()));

        HtcVisitEntity item = htcVisitService.findOne(Long.parseLong(oid));
        if (StringUtils.isNotEmpty(item.getSiteConfirmTest())) {
            try {
                SiteEntity site = siteService.findOne(Integer.parseInt(item.getSiteConfirmTest()));
                item.setSiteConfirmTestName(site == null ? null : site.getName());
                item.setExchangeProvinceName(StringUtils.isNotEmpty(item.getExchangeProvinceID()) ? locationService.findProvince(item.getExchangeProvinceID()).getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim() : null);
                item.setExchangeDistrictName(StringUtils.isNotEmpty(item.getExchangeDistrictID()) ? locationService.findDistrict(item.getExchangeDistrictID()).getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim() : null);
                item.setTherapyRegistProvinceName(StringUtils.isNotEmpty(item.getTherapyRegistProvinceID()) ? locationService.findProvince(item.getTherapyRegistProvinceID()).getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim() : null);
                item.setTherapyRegistDistrictName(StringUtils.isNotEmpty(item.getTherapyRegistProvinceID()) ? locationService.findDistrict(item.getTherapyRegistDistrictID()).getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim() : null);
            } catch (NumberFormatException e) {
            }
        }

        model.addAttribute("form", form);
        model.addAttribute("item", item);
        List<String> riskBehaviors = new ArrayList<>();
        for (String o : item.getRiskBehaviorID()) {
            riskBehaviors.add(getOptions().get(ParameterEntity.RISK_BEHAVIOR).get(o));
        }

        HashMap<String, String> siteConfirmTests = new HashMap<>();
        for (SiteEntity entity : siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID())) {
            siteConfirmTests.put(entity.getID().toString(), entity.getName());
        }
        model.addAttribute("risk", StringUtils.join(riskBehaviors, ", "));
        model.addAttribute("siteConfirmTests", siteConfirmTests);
        model.addAttribute("options", getOptions());
        model.addAttribute("is_code", parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_TICKET_CODE.getKey()));

        boolean date = TextUtils.formatDate(new Date(), FORMATDATE).equals(TextUtils.formatDate(item.getAdvisoryeTime(), FORMATDATE));
        model.addAttribute("date", date);

        return print("report/htc/agree-test-shift.html", model);
    }

    /**
     * In phụ lục 02 TT09
     *
     * @param oids
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc/appendix02.html"})
    public ResponseEntity<InputStreamResource> actionAppendix02Sent(@RequestParam(name = "oid") String oids) throws Exception {
        Appendix02Form form = getDataAppendix(oids);
        HashMap<String, Object> context = new HashMap<>();
        Set<Long> siteIDs = new HashSet<>();
        if (!form.getListPatient().isEmpty()) {

            for (HtcVisitEntity model : form.getListPatient()) {
                if (model.getSiteConfirmTest() == null || "".equals(model.getSiteConfirmTest())) {
                    continue;
                }
                try {
                    siteIDs.add(Long.valueOf(model.getSiteConfirmTest()));
                } catch (NumberFormatException e) {
                }
            }
        }
        context.put("sites", getSite(siteIDs));
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(form.getFileName(), "report/htc/appendix-02-sent-pdf.html", context);
    }

    @GetMapping(value = {"/htc/agree-disclose.html"})
    public ResponseEntity<InputStreamResource> actionAgreeDisclose(Model model,
            @RequestParam(name = "oid") String oid,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        AgreeDiscloseForm form = new AgreeDiscloseForm();

        form.setTitle("PHIẾU ĐỒNG Ý TIẾT LỘ THÔNG TIN");
        form.setFileName("PhieuDongYTietLoThongTin_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteAddress(currentUser.getSite().getAddress());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        HtcVisitEntity item = htcVisitService.findOne(Long.parseLong(oid));

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("item", item);
        context.put("options", getOptions());

        return exportPdf(form.getFileName(), "report/htc/agree-to-disclose.html", context);
    }

    /**
     * Lấy thông tin in phụ lục 02
     *
     * @author DSNAnh
     * @param ids
     * @return
     */
    private Appendix02Form getDataAppendix(String ids) {
        LoggedUser currentUser = getCurrentUser();

        Appendix02Form form = new Appendix02Form();
        form.setTitle("BÁO CÁO GIÁM SÁT CA BỆNH ");
        form.setFileName("BCGiamSatCaBenh_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setShortName(currentUser.getSite().getShortName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        if (ids == null || ids.isEmpty() || ids.equals("null")) {
            return form;
        }

        Set<Long> pIDs = new HashSet<>();
        String[] split = ids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pIDs.add(Long.parseLong(string));
        }
        List<HtcVisitEntity> items = htcVisitService.findAllByIDs(pIDs);
        List<HtcVisitEntity> models = new ArrayList<>();
        if (!items.isEmpty()) {
            for (HtcVisitEntity item : items) {
                if (item.getConfirmResultsID() != null && !"".equals(item.getConfirmResultsID())) {
                    if ("2".equals(item.getConfirmResultsID())) {
                        models.add(item);
                    }
                }
            }
        }
        form.setListPatient(htcVisitService.getAddress(models));

        return form;
    }

    public Map<String, String> getSite(Set<Long> siteIDs) {
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        if (siteIDs != null && siteIDs.size() > 0) {
            List<SiteEntity> sites = siteService.findByIDs(siteIDs);
            for (SiteEntity site : sites) {
                options.put(String.valueOf(site.getID()), site.getName());
            }
        }
        return options;
    }
}
