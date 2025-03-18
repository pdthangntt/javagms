package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ParameterDefineForm;
import com.gms.entity.form.ql.Local;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.QlReportService;
import com.gms.service.SiteService;
import com.ibm.icu.util.Calendar;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteController extends BaseController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private ParameterService parameterService;

    /**
     * Lưu tham số khi định nghĩa
     *
     * @param form
     * @param bindingResult
     * @return
     * @throws java.lang.CloneNotSupportedException
     */
    @RequestMapping(value = "/site-config/save.json", method = RequestMethod.POST)
    public Response<?> actionSaveConfig(@Valid @RequestBody ParameterDefineForm form,
            BindingResult bindingResult) throws CloneNotSupportedException {

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }
        form.setType(String.format("site_%s", getCurrentUser().getSite().getID()));
        String code = form.getKey();
        ParameterEntity siteConfig = parameterService.findOne(form.getType(), code);
        if (siteConfig == null) {
            siteConfig = new ParameterEntity();
            siteConfig.setType(form.getType());
            siteConfig.setCode(code);
        }
        siteConfig.setValue(form.getValue());
        siteConfig = parameterService.save(siteConfig);
        if (siteConfig == null) {
            return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
        }

        //Chức năng cấu hình của tỉnh
        if (siteConfig.getCode().equals(SiteConfigEnum.PAC_FROM_HTC.getKey()) || siteConfig.getCode().equals(SiteConfigEnum.PAC_FROM_CONFIRM.getKey())) {
            String type = String.format("province_%s", getCurrentUser().getSite().getProvinceID());
            ParameterEntity provinceConfig = parameterService.findOne(type, siteConfig.getCode());
            if (provinceConfig == null) {
                provinceConfig = siteConfig.clone();
                provinceConfig.setType(type);
                provinceConfig.setID(null);
            }
            provinceConfig.setValue(siteConfig.getValue());
            parameterService.save(provinceConfig);
        }

        return new Response<>(true, "Cập nhật thông tin thành công.", siteConfig);
    }

    @Autowired
    private QlReportService qlReportService;
    @Autowired
    private PacPatientService patientService;

    @RequestMapping(value = "/test.json", method = RequestMethod.GET)
    public Response<?> actionTest() throws CloneNotSupportedException {

        List<Local> list = patientService.findProvinceNhiemMoi("01/11/2019", "01/11/2021");
        for (Local info : list) {
            

        }

        return new Response<>(true, "Cập nhật thông tin thành công.");
    }

    private void report(Local local) {
        LocalDate month = LocalDate.of(local.getYear(), local.getMonth(), 1);
        LocalDate back30month = month.minusMonths(30); //Lùi 30 tháng
        System.out.println("currentMonth : " + month);
        System.out.println("currentDateMinus30Month : " + back30month);

//        int nhiemmoi = patientService.countNhiemMoi(local);
//        int tb30thang = patientService.countPhathien(local);
//        int xn = patientService.countPhathien(local, TextUtils.getFirstDayOfMonth(local.getMonth(), local.getYear()));
    }

}
