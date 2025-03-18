package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.form.htc_confirm.ConfirmReceiveBookForm;
import com.gms.entity.form.htc_confirm.ConfirmReceiveBookTableForm;
import com.gms.service.HtcConfirmService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Nhận mẫu và trả kết quả theo QD2674
 * 
 * @author TrangBN
 */
@Controller(value = "htc_confirm_receive_send_book")
public class HtcConfirmReceiveBookController extends BaseController {
    
    @Autowired
    private HtcConfirmService htcConfirmService;
    
    /**
     * Trang index hiển thị sổ nhận và trả kết quả HIV
     * 
     * @param model
     * @param receiveStartDate
     * @param receiveEndDate
     * @param testStartDate
     * @param testEndDate
     * @param returnStartDate
     * @param returnEndDate
     * @param sourceSiteID
     * @return
     * @throws Exception 
     */
//    @GetMapping(value = {"/confirm-receive-book/index.html"})
//    public String actionIndex(Model model,
//            @RequestParam(name = "receive_date_start", defaultValue = "") String receiveStartDate,
//            @RequestParam(name = "receive_date_end", defaultValue = "") String receiveEndDate,
//            @RequestParam(name = "test_date_start", defaultValue = "") String testStartDate,
//            @RequestParam(name = "test_date_end", defaultValue = "") String testEndDate,
//            @RequestParam(name = "return_date_start", defaultValue = "") String returnStartDate,
//            @RequestParam(name = "return_date_end", defaultValue = "") String returnEndDate,
//            @RequestParam(name = "source_site_id", defaultValue = "") String sourceSiteID) throws Exception {
//        
//        ConfirmReceiveBookForm form = getData(receiveStartDate, receiveEndDate, 
//                                               testStartDate, testEndDate, returnStartDate, returnEndDate, sourceSiteID);
//        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
//        model.addAttribute("title", "Sổ xét nghiệm khẳng định");
//        model.addAttribute("title_small", "Sổ tư vấn xét nghiệm");
//        model.addAttribute("form", form); 
//        
//        return "report/htc_confirm/confirm-receive-book";
//    }
    
    /**
     * Lấy data sổ nhận mẫu và trả kết quả HIV
     * 
     * @param receiveStartDate
     * @param receiveEndDate
     * @param testStartDate
     * @param testEndDate
     * @param returnStartDate
     * @param returnEndDate
     * @param sourceSiteID
     * @return
     * @throws ParseException 
     */
//    private ConfirmReceiveBookForm getData(String receiveStartDate, String receiveEndDate, String testStartDate, 
//                                             String testEndDate, String returnStartDate, String returnEndDate, String sourceSiteID) throws ParseException {
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
//        
//        // Set mặc định ngày đầu tháng và cuối tháng
//        Date receiveStartDateConvert = StringUtils.isEmpty(receiveStartDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(receiveStartDate);
//        Date receiveEndDateConvert = StringUtils.isEmpty(receiveEndDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(receiveEndDate);
//        Date testStartDateConvert = StringUtils.isEmpty(testStartDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(testStartDate);
//        Date testEndDateConvert = StringUtils.isEmpty(testEndDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(testEndDate);
//        Date returnStartDateConvert = StringUtils.isEmpty(returnStartDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(returnStartDate);
//        Date returnEndDateConvert = StringUtils.isEmpty(returnEndDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(returnEndDate);
//        
//        LoggedUser currentUser = getCurrentUser();
//        
//        ConfirmReceiveBookForm form  = new ConfirmReceiveBookForm();
//        form.setSiteID(currentUser.getSite().getID());
//        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
//        form.setSiteName(currentUser.getSite().getName());
//        form.setStaffName(getCurrentUser().getUser().getName());
//        form.setSiteProvince(getProvinceName(currentUser.getSite()));
//        form.setTitle("SỐ NHẬN MẪU VÀ TRẢ KẾT QUẢ XÉT NGHIỆM HIV");
//        form.setFileName("SoXN_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
//        
//        List<HtcConfirmEntity> confirmEntites = htcConfirmService.findConfirmReceiveBook(receiveStartDateConvert, receiveEndDateConvert, testStartDateConvert, testEndDateConvert, returnStartDateConvert, returnEndDateConvert, sourceSiteID, Long.MIN_VALUE);
//        
//        if (confirmEntites.isEmpty()) {
//            form.setTable(null);
//            return form;
//        }
//        
//        List<ConfirmReceiveBookTableForm> tables = new ArrayList<>();
//        ConfirmReceiveBookTableForm tableForm = new ConfirmReceiveBookTableForm();
//        
//        for (HtcConfirmEntity entity : confirmEntites) {
//            tableForm = new ConfirmReceiveBookTableForm();
//            
//            tableForm.setID(entity.getID());
//            tableForm.setConfirmTime(entity.getConfirmTime() != null ? TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE): null);
//            
//            
//        }
//        return null;
//    }
    
}
