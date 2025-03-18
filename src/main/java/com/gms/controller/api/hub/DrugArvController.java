package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.bean.ResponseHub;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.drug_new_api.PqmDrugNewApiForm;
import com.gms.entity.form.drug_new_api.PqmDrugNewApiMainForm;
import com.gms.service.LocationsService;
import com.gms.service.PqmDrugNewService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_drug_arv_api")
public class DrugArvController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmDrugNewService drugNewService;

    @RequestMapping(value = "/hub/v1/drug-arv.api", method = RequestMethod.POST)
    public ResponseHub<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            PqmDrugNewApiMainForm form = g.fromJson(content.trim(), PqmDrugNewApiMainForm.class);

            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})", form.getDatas().size());
            }

            int created = 0;
            int updated = 0;
            int errored = 0;

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (PqmDrugNewApiForm item : form.getDatas()) {
                    SiteEntity site = siteService.findByArvSiteCode(item.getSiteCode());
                    if (site == null) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(item.getProvinceID()
                                + "-" + item.getQuarter()
                                + "-" + item.getYear()
                                + "-" + item.getDrugName()
                                + "-" + item.getSource(), errors);
                        errored++;
                        continue;
                    }

                    item.setQuarter(String.valueOf(form.getQuarter()));
                    item.setYear(String.valueOf(form.getYear()));
                    item.setProvinceID(form.getProvinceID());

                    HashMap<String, String> errors = validateCustom(item, siteService.findOne(site.getID()).getProvinceID());
                    if (!errors.isEmpty()) {
                        masterError.put(item.getProvinceID()
                                + "-" + item.getQuarter()
                                + "-" + item.getYear()
                                + "-" + item.getDrugName()
                                + "-" + item.getSource(),
                                errors);
                        errored++;
                    } else {
                        PqmDrugNewEntity model = drugNewService.findByUniqueConstraints(
                                site.getID().toString(),
                                Integer.valueOf(item.getYear()),
                                Integer.valueOf(item.getQuarter()),
                                item.getProvinceID(),
                                item.getDrugName(),
                                item.getSource());

                        if (model == null) {
                            model = new PqmDrugNewEntity();
                            created++;
                        } else {
                            updated++;
                        }
                        model.setProvinceID(form.getProvinceID());
                        model.setQuantity(form.getQuantity());
                        model.setSiteID(site.getID());
//                        model.setProvinceID(item.getProvinceID());
                        model.setYear(Integer.valueOf(item.getYear()));
                        model.setQuarter(Integer.valueOf(item.getQuarter()));
                        model.setDrugName(item.getDrugName());
                        model.setSource(item.getSource());

                        model.setUnit(item.getUnit());

                        model.setTdk(Long.valueOf(item.getTdk()));
                        model.setNdk(Long.valueOf(item.getNdk()));
                        model.setNk(Long.valueOf(item.getNk()));
                        model.setXcbntk(Long.valueOf(item.getXcbntk()));
                        model.setXdctk(Long.valueOf(item.getXdctk()));
                        model.setHh(Long.valueOf(item.getHh()));
                        model.setTck(Long.valueOf(item.getTck()));

                        drugNewService.save(model);
                    }

                }
            } else {
                return new ResponseHub<>(false, "EXCEPTION", "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu");
            }

            if (!masterError.isEmpty()) {
                return new ResponseHub<>(false, "VALIDATION_ERROR", masterError, form.getDatas().size(), updated);
            }

            return new ResponseHub<>(true, "SUCCESS", form.getDatas().size(), updated);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }
    }

    @RequestMapping(value = "/hub/v1/drug-arv.api", method = RequestMethod.DELETE)
    public Response<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("year") String year,
            @RequestParam("quarter") String quarter,
            @RequestParam("provinceID") String provinceID,
            @RequestParam("drugName") String drugName,
            @RequestParam("source") String source
    ) {
        try {
            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByArvSiteCode(siteCode);
            if (site == null) {
                return new Response<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmDrugNewEntity model = drugNewService.findByUniqueConstraints(
                    site.getID().toString(),
                    Integer.valueOf(year),
                    Integer.valueOf(quarter),
                    provinceID,
                    drugName,
                    source);
            if (model == null) {
                return new Response<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            drugNewService.deleteByID(model.getID());

            return new Response<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(PqmDrugNewApiForm item, String provinceID) {
        HashMap<String, String> errors = new HashMap<>();

        //tỉnh
        List<String> provinces = new ArrayList<>();
        for (ProvinceEntity provinceEntity : locationService.findAllProvince()) {
            provinces.add(provinceEntity.getID());
        }
        if (StringUtils.isBlank(item.getProvinceID())) {
            errors.put("provinceID", "Tỉnh không được để trống");
        } else {
            if (!provinceID.equals(item.getProvinceID())) {
                errors.put("provinceID", "Tỉnh không đúng với tỉnh của cơ sở nhận API");
            }
        }
        if (!provinces.contains(item.getProvinceID())) {
            errors.put("provinceID", "Mã tỉnh không tồn tại trong hệ thống ");
        }

        //tháng năm
        if (StringUtils.isBlank(item.getQuarter())) {
            errors.put("quarter", "Quý không được để trống");
        }
        if (StringUtils.isBlank(item.getYear())) {
            errors.put("year", "Năm không được để trống");
        }

        if (!StringUtils.isBlank(item.getYear()) && !StringUtils.isBlank(item.getQuarter())) {
            try {
                int year = Integer.valueOf(item.getYear());
                int quarter = Integer.valueOf(item.getQuarter());
                if (year < 1000 || year > 9999) {
                    errors.put("year", "Năm nằm trong khoảng 1000 - 9999");
                }
                if (quarter < 1 || quarter > 4) {
                    errors.put("quarter", "Quý nằm trong khoảng 1 - 4");
                }

                int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
                int currentQuater = TextUtils.getQuarter(new Date()) + 1;

                int currentTime = Integer.valueOf(currentYear + "" + currentQuater);
                int reportTime = Integer.valueOf(year + "" + quarter);
                if (reportTime > currentTime) {
                    errors.put("quarter-year", "Thời gian báo cáo không được lớn hơn quý hiện tại");
                }

            } catch (Exception e) {
                errors.put("quarter-year", "Quý/Năm không đúng");
            }

        }

        if (StringUtils.isBlank(item.getDrugName())) {
            errors.put("drugName", "Tên thuốc không được để trống");
        }
        if (StringUtils.isBlank(item.getSource())) {
            errors.put("source", "Nguồn thuốc không được để trống");
        }
        if (StringUtils.isBlank(item.getUnit())) {
            errors.put("unit", "Đơn vị tính không được để trống");
        }
        if (!checkNumber(item.getTdk())) {
            errors.put("tdk", "Tồn đầu kỳ chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getNdk())) {
            errors.put("ndk", "Nhập định kỳ chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getNk())) {
            errors.put("nk", "Nhập khác chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getXcbntk())) {
            errors.put("xcbntk", "Xuất cho bệnh nhân trong kỳ chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getXdctk())) {
            errors.put("xdctk", "Xuất điều chuyển trong kỳ chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getHh())) {
            errors.put("hh", "Hư hao chỉ được nhập số nguyên dương");
        }
        if (!checkNumber(item.getTck())) {
            errors.put("tck", "Tồn cuối kỳ chỉ được nhập số nguyên dương");
        }
        return errors;
    }

    private boolean checkNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        try {
            Integer.valueOf(number);
            if (Integer.valueOf(number) < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //check valid date
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
