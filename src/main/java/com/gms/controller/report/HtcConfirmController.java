package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.HtcConfirmEntity;
import java.util.ArrayList;
import java.util.List;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.htc_confirm.AnswerResultForm;
import com.gms.entity.form.htc_confirm.AnswerResultTableForm;
import com.gms.entity.form.htc_confirm.TicketResultForm;
import com.gms.entity.form.htc_confirm.Appendix02Form;
import com.gms.service.HtcConfirmService;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
 * @author ThangPD
 */
@Controller(value = "htc_confirm_report")
public class HtcConfirmController extends BaseController {

    @Autowired
    private HtcConfirmService htcConfirmService;

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);

        String htcOptions = "htcOptions";
        HashMap<String, String> sites = new HashMap<>();
        for (SiteEntity site : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), getCurrentUser().getSite().getProvinceID())) {
            sites.put(site.getID().toString(), site.getName());
        }

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        options.put(htcOptions, sites);
        return options;
    }

    /**
     * Lấy gioi tinh
     *
     * @author pdThang
     * @return list
     */
    private HashMap<String, String> getGenderOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.getGenders()) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy ket qua sinh pham
     *
     * @author pdThang
     * @return list
     */
    private HashMap<String, String> getBioNameOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.BIOLOGY_PRODUCT_TEST)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy ket qua sinh pham
     *
     * @author pdThang
     * @return list
     */
    private HashMap<String, String> getBioNameResultOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.BIO_NAME_RESULT)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách các kết quả xn khang dinh
     *
     * @author pdThang
     * @return list
     */
    private HashMap<String, String> getResultsOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.TEST_RESULTS_CONFIRM)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách các nhóm đối tượng
     *
     * @author pdThang
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
     * In phiếu trả kết quả danh sach
     *
     * @author vvthanh
     * @param model
     * @param oids
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-confirm/answer-result-list.html"})
    public ResponseEntity<InputStreamResource> actionAnswerResultList(Model model,
            @RequestParam(name = "oid") String oids,
            RedirectAttributes redirectAttributes) throws Exception {
        AnswerResultForm form = getData(oids);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), "report/htc_confirm/answer-result-list.html", context);
    }

    /**
     * In phiếu trả kết quả
     *
     * @author pdThang
     * @param model
     * @param oids
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-confirm/answer-result.html"})
    public String actionAnswerResult(Model model,
            @RequestParam(name = "oid") String oids,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();

        AnswerResultForm form = getData(oids);
//        HashMap<String, Object> context = new HashMap<>();
//        context.put("form", form);
//        context.put("baseUrl", view.getURLBase());
        model.addAttribute("form", form);
        model.addAttribute("baseUrl", view.getURLBase());

        HashMap<String, String> config = parameterService.getSiteConfig(currentUser.getSite().getID());
        String template = config.get(SiteConfigEnum.CONFIRM_ANSWER_RESULT.getKey());
        if (template.equals("2")) {
            //Mẫu theo TT 02/2020/TT-BYT
//            return exportPdf(form.getFileName(), "report/htc_confirm/answer-result-tt022020.html", context);
            return print("report/htc_confirm/answer-result-tt022020.html", model);

        }
        //Mẫu số 6B
//        return exportPdf(form.getFileName(), "report/htc_confirm/answer-result.html", context);
        return print("report/htc_confirm/answer-result.html", model);
    }

    /**
     * Lấy thông tin khách hảng gửi mẫu XN
     *
     * @author pdThang
     * @param ids
     * @return
     */
    private AnswerResultForm getData(String ids) {
        LoggedUser currentUser = getCurrentUser();

        AnswerResultForm answerResultForm = new AnswerResultForm();
        answerResultForm.setConfig(parameterService.getSiteConfig(currentUser.getSite().getID()));
        answerResultForm.setTitle("PHIẾU TRẢ LỜI KẾT QUẢ XÉT NGHIỆM HIV");
        answerResultForm.setFileName("PhieuTraLoiKetquaXNHIV_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        answerResultForm.setSiteName(currentUser.getSite().getName());
        answerResultForm.setSiteAgency(getSiteAgency(currentUser.getSite()));

        HashMap<String, String> staffConfig = parameterService.getStaffConfig(currentUser.getUser().getID());
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_ADDTESS.getKey(), "").equals("")) {
            answerResultForm.setSiteAddress(staffConfig.get(StaffConfigEnum.SITE_ADDTESS.getKey()));
        } else {
            answerResultForm.setSiteAddress(siteService.getAddress(currentUser.getSite()));
        }
        if (!staffConfig.getOrDefault(StaffConfigEnum.SITE_PHONE.getKey(), "").equals("")) {
            answerResultForm.setSitePhone(staffConfig.get(StaffConfigEnum.SITE_PHONE.getKey()));
        } else {
            answerResultForm.setSitePhone(currentUser.getSite().getPhone());
        }

        // Lấy danh sách đối tượng gửi mẫu xét nghiệm 
        List<Long> confirmIds = new ArrayList<>();

        String[] split = ids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }

        if (confirmIds.isEmpty()) {
            return null;
        }

        List<HtcConfirmEntity> data = htcConfirmService.findAllByIds(confirmIds);
        if (data == null || data.isEmpty()) {
            return null;
        }

        List<HtcConfirmEntity> result = new ArrayList<>();
        List<Long> sID = new ArrayList<>();
        List<Long> acceptTime = new ArrayList<>();
        List<Long> testTime = new ArrayList<>();
        for (HtcConfirmEntity entity : data) {
            if (StringUtils.isNotEmpty(entity.getResultsID())) {
                result.add(entity);
                if (!sID.contains(entity.getSourceSiteID())) {
                    sID.add(entity.getSourceSiteID());
                }
                if (entity.getAcceptDate() != null && !acceptTime.contains(entity.getAcceptDate().getTime())) {
                    acceptTime.add(entity.getAcceptDate().getTime());
                }
                if (entity.getConfirmTime() != null && !testTime.contains(entity.getConfirmTime().getTime())) {
                    testTime.add(entity.getConfirmTime().getTime());
                }
            }
        }

        if (sID.size() == 1) {
            SiteEntity site = siteService.findOne(sID.get(0));
            answerResultForm.setSourceSiteID(site == null ? null : site.getName());
        }

        Collections.sort(acceptTime);
        if (acceptTime.size() >= 1) {
            String start = TextUtils.formatDate(new Date(acceptTime.get(0)), FORMATDATE);
            String end = TextUtils.formatDate(new Date(acceptTime.get(acceptTime.size() - 1)), FORMATDATE);
            answerResultForm.setAcceptTime(start.equals(end) ? start : String.format("%s - %s", start, end));
        }

        Collections.sort(testTime);
        if (testTime.size() >= 1) {
            String start = TextUtils.formatDate(new Date(testTime.get(0)), FORMATDATE);
            String end = TextUtils.formatDate(new Date(testTime.get(testTime.size() - 1)), FORMATDATE);
            answerResultForm.setTestTime(start.equals(end) ? start : String.format("%s - %s", start, end));
        }

        if (!result.isEmpty()) {
            answerResultForm.setListAnswer(new ArrayList<AnswerResultTableForm>());
            //create ticket-result
            answerResultForm.setTickets(new ArrayList<TicketResultForm>());

            result = htcConfirmService.getAddress(result);
            AnswerResultTableForm table = null;
            TicketResultForm ticket = null;
            for (HtcConfirmEntity object : result) {
                table = new AnswerResultTableForm();
                table.setID(object.getID());
                table.setCode(object.getCode());
                table.setPatientID(object.getPatientID());
                table.setFullname(object.getFullname());
                table.setYear(object.getYear());
                table.setGenderID(getGenderOption().get(object.getGenderID()));
                table.setAddress(object.getAddressFull());
                table.setObjectGroupID(getObjectGroupOption().get(object.getObjectGroupID()));
                table.setBioName1(getBioNameOption().get(object.getBioName1()));
                table.setBioName2(getBioNameOption().get(object.getBioName2()));
                table.setBioName3(getBioNameOption().get(object.getBioName3()));
                table.setBioNameResult1(getBioNameResultOption().get(object.getBioNameResult1()));
                table.setBioNameResult2(getBioNameResultOption().get(object.getBioNameResult2()));
                table.setBioNameResult3(getBioNameResultOption().get(object.getBioNameResult3()));
                table.setOtherTechnical(object.getOtherTechnical());
                table.setResultsID(getResultsOption().get(object.getResultsID()));
                table.setNote("3".equals(object.getResultsID()) ? "Hẹn xét nghiệm lại sau 14 ngày" : "");
                answerResultForm.getListAnswer().add(table);

                //Phieu
                ticket = new TicketResultForm();
                ticket.setObjectGroupID(object.getObjectGroupID());
                ticket.setSiteID(object.getSiteID());
                ticket.setSiteName(currentUser.getSite().getName());
                ticket.setSiteAddress(answerResultForm.getSiteAddress());
                ticket.setSitePhone(answerResultForm.getSitePhone());
                ticket.setPatientName(object.getFullname());
                ticket.setGenderID(getGenderOption().get(object.getGenderID()));
                ticket.setPatientCode(object.getCode());
                ticket.setSourceID(object.getSourceID());
                ticket.setYearOfBirth(String.valueOf(object.getYear()));
                ticket.setPatientID(object.getPatientID());
                ticket.setPatientAddress(object.getAddressFull());
                ticket.setConfirmTime(object.getConfirmTime() == null ? "" : TextUtils.formatDate(object.getConfirmTime(), FORMATDATE));
                ticket.setBioNameResult(new ArrayList<String>());
                ticket.setSampleReceiveTime(object.getSourceReceiveSampleTime() == null ? "" : TextUtils.formatDate(object.getSourceReceiveSampleTime(), FORMATDATE));

                if (!StringUtils.isEmpty(object.getBioName1())) {
                    ticket.getBioNameResult().add(getBioNameOption().get(object.getBioName1()));
                }
                if (!StringUtils.isEmpty(object.getBioName2())) {
                    ticket.getBioNameResult().add(getBioNameOption().get(object.getBioName2()));
                }
                if (!StringUtils.isEmpty(object.getBioName3())) {
                    ticket.getBioNameResult().add(getBioNameOption().get(object.getBioName3()));
                }
                ticket.setResultsID(getResultsOption().get(object.getResultsID()));
                ticket.setSampleSentSource(object.getSampleSentSource());
                ticket.setTitle("KẾT QUẢ XÉT NGHIỆM HIV");
                if (object.getQrcode() != null) {
                    ticket.setQrCode(qrCodeService.generateCode(object.getQrcode()));
                }
                answerResultForm.getTickets().add(ticket);
            }
        }
        return answerResultForm;
    }

    /**
     * In phụ lục 02 TT09
     *
     * @param oids
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-confirm/appendix02.html"})
    public ResponseEntity<InputStreamResource> actionAppendix02Sent(@RequestParam(name = "oid") String oids) throws Exception {
        Appendix02Form form = getDataAppendix(oids);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(form.getFileName(), "report/htc_confirm/appendix-02-sent-pdf.html", context);
    }

    /**
     * Lấy thông tin in phụ lục 02
     *
     * @author TrangBN
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

        List<Long> pIDs = new ArrayList<>();
        String[] split = ids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pIDs.add(Long.parseLong(string));
        }
        List<HtcConfirmEntity> items = htcConfirmService.findAllByIds(pIDs);
        List<HtcConfirmEntity> models = new ArrayList<>();
        if (!items.isEmpty()) {
            for (HtcConfirmEntity item : items) {
                if (item.getResultsID() != null && !"".equals(item.getResultsID())) {
                    if ("2".equals(item.getResultsID())) {
                        models.add(item);
                    }
                }
            }
        }
        form.setListPatient(htcConfirmService.getAddress(models));

        return form;
    }
}
