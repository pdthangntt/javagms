package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.ArvBookExcel;
import com.gms.entity.form.opc_arv.ArvBookChildForm;
import com.gms.entity.form.opc_arv.ArvBookForm;
import com.gms.entity.form.opc_arv.ArvBookTableForm;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 *
 * @author DSNAnh
 */
//@Controller(value = "opc_arv_book_report")
public class OpcArvBookController extends OpcController {

//    protected static final String PDF_FILE_NAME = "So Arv_";
//
//    @Autowired
//    private OpcArvRepositoryImpl opcArvRepositoryImpl;
//
//    private ArvBookForm getData(String word, String treatmentTimeFrom, String treatmentTimeTo, int page, int size) {
//        HashMap<String, HashMap<String, String>> options = getOptions();
//        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
//        treatmentTimeFrom = StringUtils.isEmpty(treatmentTimeFrom) ? TextUtils.formatDate(TextUtils.getFirstDayOfYear(new Date()), FORMATDATE) : treatmentTimeFrom;
//        treatmentTimeTo = StringUtils.isEmpty(treatmentTimeTo) ? TextUtils.formatDate(TextUtils.getLastDayOfYear(new Date()), FORMATDATE) : treatmentTimeTo;
//
//        //Khởi tạo biến
//        LoggedUser currentUser = getCurrentUser();
//        ArvBookForm form = new ArvBookForm();
//        form.setTitle("SỔ ĐIỀU TRỊ BẰNG THUỐC KHÁNG HIV");
//        form.setFileName("SoARV_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
//        form.setStaffName(currentUser.getUser().getName());
//        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
//        form.setSiteName(currentUser.getSite().getName());
//        form.setSiteProvince(getProvinceName(currentUser.getSite()));
//        form.setOptions(options);
//        form.setFrom(treatmentTimeFrom);
//        form.setTo(treatmentTimeTo);
//        form.setWord(StringUtils.isEmpty(word) || word.equals("undefined") ? "" : word);
//
//        Set<Long> siteIDs = new HashSet<>();
//        siteIDs.add(currentUser.getSite().getID());
//
//        //Get child data
//        List<ArvBookTableForm> datas = new ArrayList<>();
//        Map<String, ArvBookChildForm> maps;
//        Map<String, ArvBookChildForm> maps2;
//        Map<String, ArvBookChildForm> months;
//        List<ArvBookChildForm> childs;
//        List<String> month;
//        ArvBookChildForm item2;
//        List<ArvBookTableForm> data = opcArvRepositoryImpl.getArvBook(siteIDs, word, TextUtils.convertDate(treatmentTimeFrom, FORMATDATE), TextUtils.convertDate(treatmentTimeTo, FORMATDATE));
//        
//        
//        for (ArvBookTableForm arvBookTableForm : data) {
//            maps = new HashMap<>();
//            maps2 = new HashMap<>();
//            months = new HashMap<>();
//            for (int i = 0; i <= 60; i++) {
//                maps.put(i + "", new ArvBookChildForm());
//            }
//            childs = new ArrayList<>();
//            month = new ArrayList<>();
//            for (ArvBookChildForm c : arvBookTableForm.getChilds()) {
//                c.setFlag(true);
//                childs.add(c);
//                if (StringUtils.isNotEmpty(c.getMonth()) && !c.getType().equals("ctx") && !c.getType().equals("inh") && !c.getType().equals("lao") && !c.getType().equals("cd4")) {
//                    months.put(c.getMonth(), c);
//                    month.add(c.getMonth());
//
//                }
//            }
//
//            List<ArvBookChildForm> listNode = new ArrayList<>();
//            ArvBookChildForm child = new ArvBookChildForm();
//            for (Map.Entry<String, ArvBookChildForm> entry1 : months.entrySet()) {
//                
//                child = entry1.getValue();
//                listNode.add(child);
//                
//                String key1 = entry1.getKey();
//                ArvBookChildForm value1 = entry1.getValue();
//                for (Map.Entry<String, ArvBookChildForm> entry : maps.entrySet()) {
//                    String key = entry.getKey();
//                    if (Long.valueOf(key) > Long.valueOf(key1)) {
//                        maps2.put(key, value1);
//                    }
//                }
//            }
//
//            for (Map.Entry<String, ArvBookChildForm> entry : maps2.entrySet()) {
//                String key = entry.getKey();
//                ArvBookChildForm value = entry.getValue();
//
//                item2 = new ArvBookChildForm();
//
//                item2.setFlag(false);
//                item2.setMonth(key);
//                item2.setTreatmentRegimentID(value.getTreatmentRegimentID());
//                item2.setStatusOftreatment(value.getStatusOftreatment());
//                item2.setType(value.getType());
//                item2.setTime(value.getTime());
//                if (!month.contains(item2.getMonth())) {
//                    childs.add(item2);
//                }
//            }
//            
//            // Set lại các tháng sau khi kết thúc và dtl
//            ArvBookChildForm t = null;
//            Collections.sort(childs);
//            
//            for (ArvBookChildForm childBook : childs) {
//                for (ArvBookChildForm node : listNode) {
//                    if (node.getMonth().equals(childBook.getMonth()) && (node.getType().equals("CĐ") || node.getType().equals("TV") || node.getType().equals("B"))) {
//                        t = node;
//                    }
//                    if (node.getMonth().equals(childBook.getMonth()) && (!node.getType().equals("CĐ") && !node.getType().equals("TV") && !node.getType().equals("B"))) {
//                        t = node;
//                    }
//                }
//                if (t != null && (t.getType().equals("CĐ") || t.getType().equals("TV") || t.getType().equals("B"))) {
//                    childBook.setTime(t.getTime());
//                    childBook.setTreatmentRegimentID(t.getTreatmentRegimentID());
//                    childBook.setType(t.getType());
//                    childBook.setFlag(true);
//                }
//            }
//
//            arvBookTableForm.getChilds().clear();
//            arvBookTableForm.getChilds().addAll(childs);
//            datas.add(arvBookTableForm);
//        }
//        Collections.sort(datas);
//        //Add empty row behind per row
//        List<ArvBookTableForm> table = new LinkedList<>();
//        List<Integer> order = new LinkedList<>();
//        ArvBookTableForm emptyRow;
//        for (ArvBookTableForm e : datas) {
//            emptyRow = new ArvBookTableForm();
//            table.add(e);
//            emptyRow.setPatientHeight(e.getPatientHeight());
//            emptyRow.setChilds(e.getChilds());
//            table.add(emptyRow);
//        }
//        int count = 0 + 50 * (page - 1);
//        for (ArvBookTableForm item : table) {
//            if (item.getArvID() != null) {
//                count++;
//                order.add(count);
//            } else {
//                order.add(count);
//            }
//        }
//        form.setOrder(order);
//        form.setTable(table);
//
//        //Convert from list to Page
//        DataPage<ArvBookTableForm> dataPage = new DataPage<>();
//        Pageable paging = PageRequest.of(page - 1, size);
//        int start = Math.min((int) paging.getOffset(), form.getTable().size());
//        int end = Math.min((start + paging.getPageSize()), form.getTable().size());
//        Page<ArvBookTableForm> pages = new PageImpl<>(form.getTable().subList(start, end), paging, form.getTable().size());
//        dataPage.setCurrentPage(page);
//        dataPage.setMaxResult(size);
//        dataPage.setTotalPages(pages.getTotalPages());
//        dataPage.setTotalRecords((int) pages.getTotalElements());
//        dataPage.setData(pages.getContent());
//        form.setDataPage(dataPage);
//
//        return form;
//    }
//
//    /**
//     *
//     * @param model
//     * @param treatmentTimeFrom
//     * @param treatmentTimeTo
//     * @param word
//     * @param page
//     * @param size
//     * @return
//     */
//    @GetMapping(value = {"/opc-book/index.html"})
//    public String actionIndex(Model model,
//            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
//            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
//            @RequestParam(name = "word", required = false) String word,
//            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
//            @RequestParam(name = "size", required = false, defaultValue = "100") int size) {
//        HashMap<String, HashMap<String, String>> options = getOptions();
//        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
//        ArvBookForm form = getData(word, treatmentTimeFrom, treatmentTimeTo, page, size);
//        model.addAttribute("parent_title", "Điều trị ngoại trú");
//        model.addAttribute("title", "Sổ điều trị bằng thuốc kháng HIV");
//        model.addAttribute("small_title", "Sổ điều trị ARV");
//        model.addAttribute("isOpcManager", isOpcManager());
//        model.addAttribute("options", options);
//        model.addAttribute("form", form);
//        return "report/opc/arv-book";
//    }
//
//    @GetMapping(value = {"/opc-book/email.html"})
//    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
//            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
//            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
//            @RequestParam(name = "word", required = false) String word,
//            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
//            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
//        LoggedUser currentUser = getCurrentUser();
//        HashMap<String, HashMap<String, String>> options = getOptions();
//        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
//        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
//            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
//            return redirect(UrlUtils.opcBook());
//        }
//        ArvBookForm form = getData(word, treatmentTimeFrom, treatmentTimeTo, page, size);
//        HashMap<String, Object> context = new HashMap<>();
//        context.put("options", options);
//        context.put("form", form);
//        context.put("isOpc", isOPC());
//        context.put("isOpcManager", isOpcManager());
//        context.put("currentUser", getCurrentUser());
//
//        sendEmailAtachmentExcel(
//                currentUser.getUser().getEmail(),
//                String.format("Sổ điều trị bằng thuốc kháng HIV"),
//                EmailoutboxEntity.TEMPLATE_ARV_BOOK,
//                context,
//                new ArvBookExcel(form, EXTENSION_EXCEL_X));
//        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
//        return redirect(UrlUtils.opcBook());
//    }
//
//    @GetMapping(value = {"/opc-book/excel.html"})
//    public ResponseEntity<InputStreamResource> actionExportExcel(
//            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
//            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
//            @RequestParam(name = "word", required = false) String word,
//            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
//            @RequestParam(name = "size", required = false, defaultValue = "9999999") int size) throws Exception {
//        return exportExcel(new ArvBookExcel(getData(word, treatmentTimeFrom, treatmentTimeTo, page, size), EXTENSION_EXCEL_X));
//    }
//
//    /**
//     * check valid date
//     *
//     * @param dateToValidate
//     * @return
//     */
//    private boolean isThisDateValid(String dateToValidate) {
//        if (dateToValidate == null) {
//            return false;
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        sdf.setLenient(false);
//        try {
//            sdf.parse(dateToValidate);
//        } catch (ParseException e) {
//            return false;
//        }
//        return true;
//    }
}
