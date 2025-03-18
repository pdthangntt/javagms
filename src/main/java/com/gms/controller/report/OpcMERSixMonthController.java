package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.MERSixMonthExcel;
import com.gms.entity.excel.pac.DeadExcel;
import com.gms.entity.form.opc_arv.OpcMerSixMonthForm;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTable;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTablePercent;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTableSum;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.repository.impl.OpcMerRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
 * BC MER
 *
 * @author vvthành
 */
@Controller(value = "opc_mer_six_month_report")
public class OpcMERSixMonthController extends OpcController {

    @Autowired
    private OpcMerRepositoryImpl merRepositoryImpl;
    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;

    private OpcMerSixMonthForm getData(String month, String year, String siteID) {
        month = StringUtils.isEmpty(month) ? TextUtils.formatDate(new Date(), "M") : month;
        year = StringUtils.isEmpty(year) ? TextUtils.formatDate(new Date(), "yyyy") : year;
        LoggedUser currentUser = getCurrentUser();
        OpcMerSixMonthForm form = new OpcMerSixMonthForm();
        //Giá trị cơ bản
        form.setFileName("BaoCaoPEPFAR_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("BÁO CÁO DỰ ÁN PEPFAR");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSearchSiteID(siteID);

        form.setMonth(month);
        form.setYear(year);
        Date date = new Date();

        Set<Long> siteIDs = new HashSet<>();
        if (StringUtils.isEmpty(siteID)) {
            if (isOpcManager()) {
                siteIDs.addAll(getSiteManager());
            } else {
                siteIDs.add(currentUser.getSite().getID());
            }
        } else {
            siteIDs.add(Long.valueOf(siteID));
        }

        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE);

        Calendar start = Calendar.getInstance();
        start.setTime(TextUtils.getFirstDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)));
        start.add(Calendar.MONTH, -5);

        form.setStartDate(TextUtils.formatDate(start.getTime(), FORMATDATE));
        form.setEndDate(toTime);

        Calendar end6before = Calendar.getInstance();
        end6before.setTime(start.getTime());
        end6before.add(Calendar.DATE, -1);

        form.setEndDateSixMonthBefore(TextUtils.formatDate(end6before.getTime(), FORMATDATE));

        Calendar start6before = Calendar.getInstance();
        start6before.setTime(end6before.getTime());
        start6before.add(Calendar.MONTH, -6);
        start6before.setTime(TextUtils.getLastDayOfMonth(start6before.getTime()));
        start6before.add(Calendar.DATE, +1);
        form.setStartDateSixMonthBefore(TextUtils.formatDate(start6before.getTime(), FORMATDATE));

        form.setTable(arvRepositoryImpl.getMerSixMonthTable(siteIDs, start6before.getTime(), TextUtils.convertDate(form.getStartDate(), FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        //dòng tổng
        form.setTable02(new HashMap<String, OpcMerSixMonthTableSum>());
        form.getTable02().put("mauSo", getSum(form.getTable().get("mauSoMoi"), form.getTable().get("mauSoCu")));
        form.getTable02().put("tuSo", getSum(form.getTable().get("tuSoMoi"), form.getTable().get("tuSoCu")));
        form.getTable02().put("itNhat1Lan", getSum(form.getTable().get("itNhat1LanMoi"), form.getTable().get("itNhat1LanCu")));
        form.getTable02().put("sangLocNghiLao", getSum(form.getTable().get("sangLocNghiLaoMoi"), form.getTable().get("sangLocNghiLaoCu")));
        form.getTable02().put("chuanDoanLao", getSum(form.getTable().get("chuanDoanLaoMoi"), form.getTable().get("chuanDoanLaoCu")));
        form.getTable02().put("dieuTriLao", getSum(form.getTable().get("dieuTriLaoMoi"), form.getTable().get("dieuTriLaoCu")));
        //dòng %
        form.setTable03(new HashMap<String, OpcMerSixMonthTablePercent>());
        form.getTable03().put("tyLeHoanThanh", getPercent(form.getTable02().get("tuSo"), form.getTable02().get("mauSo")));
        form.getTable03().put("tyLeBatDau", getPercent(form.getTable02().get("dieuTriLao"), form.getTable02().get("itNhat1Lan")));

        return form;
    }

    private OpcMerSixMonthTableSum getSum(HashMap<String, OpcMerSixMonthTable> table01, HashMap<String, OpcMerSixMonthTable> table02) {
        OpcMerSixMonthTableSum table = new OpcMerSixMonthTableSum();
        if (table01 == null || table02 == null || table01.isEmpty() || table02.isEmpty()) {
            return table;
        }

        for (Map.Entry<String, OpcMerSixMonthTable> entry : table01.entrySet()) {
            String key = entry.getKey();
            OpcMerSixMonthTable value = entry.getValue();

            if (key.equals(GenderEnum.MALE.getKey())) {
                table.setUnderOneAgeNam(table.getUnderOneAgeNam() + value.getUnderOneAge());
                table.setOneToFourteenNam(table.getOneToFourteenNam() + value.getOneToFourteen());
                table.setOverFifteenNam(table.getOverFifteenNam() + value.getOverFifteen());

            }
            if (key.equals(GenderEnum.FEMALE.getKey())) {
                table.setUnderOneAgeNu(table.getUnderOneAgeNu() + value.getUnderOneAge());
                table.setOneToFourteenNu(table.getOneToFourteenNu() + value.getOneToFourteen());
                table.setOverFifteenNu(table.getOverFifteenNu() + value.getOverFifteen());

            }

        }
        for (Map.Entry<String, OpcMerSixMonthTable> entry : table02.entrySet()) {
            String key = entry.getKey();
            OpcMerSixMonthTable value = entry.getValue();

            if (key.equals(GenderEnum.MALE.getKey())) {
                table.setUnderOneAgeNam(table.getUnderOneAgeNam() + value.getUnderOneAge());
                table.setOneToFourteenNam(table.getOneToFourteenNam() + value.getOneToFourteen());
                table.setOverFifteenNam(table.getOverFifteenNam() + value.getOverFifteen());

            }
            if (key.equals(GenderEnum.FEMALE.getKey())) {
                table.setUnderOneAgeNu(table.getUnderOneAgeNu() + value.getUnderOneAge());
                table.setOneToFourteenNu(table.getOneToFourteenNu() + value.getOneToFourteen());
                table.setOverFifteenNu(table.getOverFifteenNu() + value.getOverFifteen());

            }

        }

        return table;
    }

    private OpcMerSixMonthTablePercent getPercent(OpcMerSixMonthTableSum table01, OpcMerSixMonthTableSum table02) {
        OpcMerSixMonthTablePercent table = new OpcMerSixMonthTablePercent();
        if (table01 == null || table02 == null) {
            return table;
        }

        table.setUnderOneAgeNam(TextUtils.toPercent(table01.getUnderOneAgeNam(), table02.getUnderOneAgeNam()));
        table.setOneToFourteenNam(TextUtils.toPercent(table01.getOneToFourteenNam(), table02.getOneToFourteenNam()));
        table.setOverFifteenNam(TextUtils.toPercent(table01.getOverFifteenNam(), table02.getOverFifteenNam()));
        table.setUnderOneAgeNu(TextUtils.toPercent(table01.getUnderOneAgeNu(), table02.getUnderOneAgeNu()));
        table.setOneToFourteenNu(TextUtils.toPercent(table01.getOneToFourteenNu(), table02.getOneToFourteenNu()));
        table.setOverFifteenNu(TextUtils.toPercent(table01.getOverFifteenNu(), table02.getOverFifteenNu()));
        table.setSum(TextUtils.toPercent(table01.getSum(), table02.getSum()));

        return table;
    }

    /**
     * Lấy thông tin báo cáo MER
     *
     * @param model
     * @param redirectAttributes
     * @param month
     * @param year
     * @param siteID
     * @return
     */
    @GetMapping(value = {"/opc-mer-six-month/index.html"})
    public String actionIndex(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");

        OpcMerSixMonthForm form = getData(month, year, siteID);

        model.addAttribute("title", "Báo cáo dự án PEPFAR");
        model.addAttribute("options", options);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("form", form);
        model.addAttribute("siteOPC", isOPC() && !isOpcManager() ? currentUser.getSite().getID() + "" : null);
        return "report/opc/mer-six-month";
    }

    @GetMapping(value = {"/opc-mer-six-month/mail.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcMERSixMonthIndex());
        }
        OpcMerSixMonthForm form = getData(month, year, siteID);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo dự án PEPFAR"),
                EmailoutboxEntity.TEMPLATE_REPORT_MER_SIX_MONTH,
                context,
                new MERSixMonthExcel(getData(month, year, siteID), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcMERSixMonthIndex());
    }

    @GetMapping(value = {"/opc-mer-six-month/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID) throws Exception {
        return exportExcel(new MERSixMonthExcel(getData(month, year, siteID), getOptions(), EXTENSION_EXCEL_X));
    }

    /**
     * check valid date
     *
     * @param dateToValidate
     * @return
     */
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
