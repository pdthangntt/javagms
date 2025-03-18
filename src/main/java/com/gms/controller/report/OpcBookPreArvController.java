package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.opc.PreArvExcel;
import com.gms.entity.form.opc_arv.OpcBookPreArvForm;
import com.gms.entity.form.opc_arv.OpcPreArvChildForm;
import com.gms.entity.form.opc_arv.OpcPreArvTableForm;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * Số ĐKĐT trước ARV
 *
 * @author vvthành
 */
@Controller(value = "opc_book_pre_arv_report")
public class OpcBookPreArvController extends OpcController {

    private static final String TEMPLATE_REPORT = "report/opc/opc-book-pre-arv-pdf.html";
    protected static final String PDF_FILE_NAME = "So preArv_";
    
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcArvRepositoryImpl opcArvRepositoryImpl;
    @Autowired
    private LocationsService locationsService;

    private HashMap<String, SiteEntity> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        option.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite());
        List<Long> ids = siteService.getProgenyID(currentUser.getSite().getID());
        Set<String> services = new HashSet<>();
        services.add(ServiceEnum.OPC.getKey());
        for (SiteEntity site : siteService.findByServiceAndSiteID(services, new HashSet<>(ids))) {
            option.put(String.valueOf(site.getID()), site);
        }
        return option;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }
    
    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options;
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE);
        parameterTypes.add(ParameterEntity.ARV_REGISTRATION_STATUS); 
        options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        OpcArvEntity arv = opcArvService.getFirst(siteIds);
        if (arv != null) {
            c.setTime(arv.getRegistrationTime());
        }
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }
    
    private OpcBookPreArvForm getData(String year, String word, int pageSize, int pageIndex) {
        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        Set<String> provinceIDs = new HashSet<>();
        Set<String> districtIDs = new HashSet<>();
        HashMap<String, String> location = new HashMap<>();
        OpcBookPreArvForm form = new OpcBookPreArvForm();
        form.setTitle("Sổ đăng ký trước điều trị bằng thuốc kháng HIV");
        form.setFileName("SoDangKyDieuTriBangThuocKhangHIV_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getOptions());

        //Lấy danh sách ID cơ sở
        Set<Long> siteIDs = new HashSet<>();
        siteIDs.add(currentUser.getSite().getID());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(StringUtils.isEmpty(year) || year.equals("undefined") ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(year), 0, 1);
        form.setYear(cal.get(Calendar.YEAR) + "");
        form.setWord(StringUtils.isEmpty(word) || word.equals("undefined") ? "" : word);
        Date startTime = cal.getTime();
        cal.add(Calendar.YEAR, 2);
        Date endTime = cal.getTime();
        
        //Get child data
        List<OpcPreArvTableForm> data = opcArvRepositoryImpl.getPreArv(siteIDs, Integer.parseInt(form.getYear()), word, TextUtils.getFirstDayOfYear(startTime), TextUtils.getLastDayOfYear(endTime));
        Collections.sort(data);
        
        //get address full
        for (OpcPreArvTableForm item : data) {
            provinceIDs.add(item.getPermanentProvinceID());
            districtIDs.add(item.getPermanentDistrictID());
        }
        for(ProvinceEntity province : locationsService.findProvinceByIDs(provinceIDs)){
            location.put(province.getID(), province.getName());
        }
        for(DistrictEntity district : locationsService.findDistrictByIDs(districtIDs)){
            location.put(district.getID(), district.getName());
        }
        for(WardEntity ward : locationsService.findByDistrictID(districtIDs)){
            location.put(ward.getID(), ward.getName());
        }
        for(OpcPreArvTableForm item : data){
            item.setPermanentAddressFull(String.format("%s%s%s%s, %s, %s", 
                    StringUtils.isEmpty(item.getPermanentAddress()) ? "" : String.format("%s, ", item.getPermanentAddress()), 
                    StringUtils.isEmpty(item.getPermanentAddressStreet()) ? "" : String.format("%s, ", item.getPermanentAddressStreet()), 
                    StringUtils.isEmpty(item.getPermanentAddressGroup()) ? "" : String.format("%s, ", item.getPermanentAddressGroup()), 
                    location.get(item.getPermanentWardID()), 
                    location.get(item.getPermanentDistrictID()), 
                    location.get(item.getPermanentProvinceID())));
        }
        
        //Add empty row behind per row
        List<OpcPreArvTableForm> table = new LinkedList<>();
        List<Integer> order = new LinkedList<>();
        OpcPreArvTableForm emptyRow;
        for(OpcPreArvTableForm e : data){
            emptyRow = new OpcPreArvTableForm();
            table.add(e);
            emptyRow.setPatientHeight(e.getPatientHeight());
            emptyRow.setChilds(e.getChilds());
            table.add(emptyRow);
        }
        int count = 0;
        for (OpcPreArvTableForm item : table) {
            if (item.getArvID() != null) {
                count++;
                order.add(count);
            } else {
                order.add(count);
            }
        }
        form.setOrder(order);
        form.setTable(table);
        
        //Convert from list to Page
        DataPage<OpcPreArvTableForm> dataPage = new DataPage<>();
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize);
        int start = Math.min((int)paging.getOffset(), form.getTable().size());
        int end = Math.min((start + paging.getPageSize()), form.getTable().size());
        Page<OpcPreArvTableForm> page = new PageImpl<>(form.getTable().subList(start, end), paging, form.getTable().size());
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);
        dataPage.setTotalPages(page.getTotalPages());
        dataPage.setTotalRecords((int) page.getTotalElements());
        dataPage.setData(page.getContent());
        form.setDataPage(dataPage);
        
        return form;
    }

    @GetMapping(value = {"/opc-book-pre-arv/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size) throws Exception {

        OpcBookPreArvForm form = getData(year, word, size, page);
        
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("small_title", "Sổ ĐKĐT trước ARV");
        model.addAttribute("title", "Sổ đăng ký trước điều trị bằng thuốc kháng HIV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        return "report/opc/opc-book-pre-arv";
    }

    @GetMapping(value = {"/opc-book-pre-arv/email.html"})
    public String actionSendEmail(Model model,
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.arvPreBook());
        }
        OpcBookPreArvForm form = getData(year, word, size, page);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s%s", "Sổ đăng ký trước điều trị bằng thuốc kháng HIV (", "Năm " + form.getYear() + ")"),
                EmailoutboxEntity.TEMPLATE_OPC_PRE_ARV,
                context,
                new PreArvExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.arvPreBook());
    }

    @GetMapping(value = {"/opc-book-pre-arv/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size
    ) throws Exception {
        OpcBookPreArvForm form = getData(year, word, size, page);
        return exportExcel(new PreArvExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-book-pre-arv/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "word", defaultValue = "") String word,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size) throws Exception {
        OpcBookPreArvForm form = getData(year, word, size, page);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isOpcManager", isOpcManager());
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);
    }
}
