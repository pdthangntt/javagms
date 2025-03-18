package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.excel.laytest.MerExcel;
import com.gms.entity.form.laytest.MerForm;
import com.gms.entity.form.laytest.MerTableForm;
import com.gms.repository.impl.HtcLaytestRepositoryImpl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * @author NamAnh_HaUI
 */
@Controller(value = "laytest_export_mer")
public class LaytestMERController extends BaseController  {
    @Autowired
    private HtcLaytestRepositoryImpl laytestRepositoryImpl;

    /**
     * Hàm lấy dữ liệu cho MerForm
     * 
     * @param fromTime
     * @param toTime
     * @return 
     */
    private MerForm getData(String fromTime, String toTime) {
        Date date = new Date();
        fromTime = fromTime == null || fromTime.equals("") ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(date), FORMATDATE) : toTime;
        
        LoggedUser currentUser = getCurrentUser();
        MerForm form = new MerForm();
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setTitle("BÁO CÁO DỰ ÁN PEPFAR");
        form.setFileName("Bao_cao_MER_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteDistrict(getDistrictName(currentUser.getSite()));
        form.setSiteWard(getWardName(currentUser.getSite()));
        List<MerTableForm> merTable01 = laytestRepositoryImpl.getMerTable01(currentUser.getSite().getID(), currentUser.getUser().getID(), fromTime, toTime);
        List<MerTableForm> merTable02 = laytestRepositoryImpl.getMerTable02(currentUser.getSite().getID(), currentUser.getUser().getID(), fromTime, toTime);
        form.setTable01Forms(merTable01);
        form.setTable02Forms(merTable02);
        return form;
    }

    /**
     * Hiển thị trang báo cáo MER
     *
     * @param model
     * @param redirectAttributes
     * @param fromTime
     * @param toTime
     * @return
     */
    @RequestMapping(value = {"/laytest-mer/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) {
    
        MerForm data = getData(fromTime, toTime);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Báo cáo dự án PEPFAR");
        model.addAttribute("form", data);
        
        return "report/laytest/mer";
    }
    
    @GetMapping(value = {"/laytest-mer/excel.html"})
    public ResponseEntity<InputStreamResource> actionMerExcel(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        MerForm data = getData(fromTime, toTime);
        return exportExcel(new MerExcel(data, EXTENSION_EXCEL));
        
    }
    
    @GetMapping(value = {"/laytest-mer/email.html"})
    public String actionMerEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.laytestMER());
        }
        MerForm form = getData(startDate, endDate);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian từ ( %s - %s )", form.getTitle(), form.getStartDate(), form.getEndDate()),
                "mer_mail",
                context,
                new MerExcel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.laytestMER(startDate, endDate));
    }
    
}
