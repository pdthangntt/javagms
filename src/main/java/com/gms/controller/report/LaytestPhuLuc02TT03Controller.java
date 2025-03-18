package com.gms.controller.report;


import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.laytest.TT03Excel;
import com.gms.entity.form.laytest.LaytestTT03Form;
import com.gms.repository.impl.HtcLaytestRepositoryImpl;
import java.util.Date;
import java.util.HashMap;
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
 * @author NamAnh_HaUI
 */
@Controller(value = "laytest_export_tt03")
public class LaytestPhuLuc02TT03Controller extends BaseController {

    private static String TEMPLATE_PHULUC02 = "report/laytest/phuluc02.html";

    @Autowired
    private HtcLaytestRepositoryImpl htcLaytestRepository;


    /**
     * Data Form report
     *
     * @param quarter
     * @param year
     * @param serviceIDs
     * @return
     */
    private LaytestTT03Form getData(String fromTime, String toTime) {
        Date date = new Date();
        fromTime = fromTime == null || fromTime.equals("") ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(date), FORMATDATE) : toTime;

        LoggedUser currentUser = getCurrentUser();
        LaytestTT03Form form = new LaytestTT03Form();
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setTitle("BÁO CÁO HOẠT ĐỘNG");
        form.setFileName("PhuLuc02_TT032015TTBYT_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        form.setTable02(htcLaytestRepository.getTable02TT03Laytest(currentUser.getSite().getID(),
                currentUser.getUser().getID(),
                TextUtils.convertDate(fromTime, FORMATDATE),
                TextUtils.convertDate(toTime, FORMATDATE)));
        return form;
    }

    /**
     * Hiển thị trang danh sách báo cáo
     * @param model
     * @param fromTime
     * @param toTime
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/laytest-tt03/index.html"})
    public String actionPhuluc02Index(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        LaytestTT03Form form = getData(fromTime, toTime);
        model.addAttribute("parent_title", "Xét nghiệm tại cộng đồng");
        model.addAttribute("title", "Báo cáo hoạt động");
        model.addAttribute("title_small", "TT03/2015/TT-BYT");
        model.addAttribute("form", form);
        return "report/laytest/phuluc02tt03";
    }

    @GetMapping(value = {"/laytest-tt03/email.html"})
    public String actionPhuluc02email(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        LoggedUser currentUser = getCurrentUser();
       if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.laytestPhuluc02());
        }
        LaytestTT03Form form = getData(fromTime, toTime);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo hoạt động (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_REPORT_03,
                context,
                new TT03Excel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.laytestPhuluc02(fromTime, toTime));
    }

    @GetMapping(value = {"/laytest-tt03/excel.html"})
    public ResponseEntity<InputStreamResource> actionPhuluc02Excel(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        return exportExcel(new TT03Excel(getData(fromTime, toTime), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/laytest-tt03/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPhuluc02Pdf(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        LaytestTT03Form form = getData(fromTime, toTime);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), TEMPLATE_PHULUC02, context);
    }
}
