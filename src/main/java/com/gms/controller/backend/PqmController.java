package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.PqmPrepResultEnum;
import com.gms.entity.constant.PqmPrepStatusEnum;
import com.gms.entity.constant.PqmPrepTreatmentEnum;
import com.gms.entity.constant.PqmPrepTypeEnum;
import com.gms.entity.constant.RegisterTherapyStatusEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vvthanh
 */
public abstract class PqmController extends BaseController {

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        addEnumOption(options, ParameterEntity.ARV_END_CASE, ArvEndCaseEnum.values(), ""); //Lý do kết thúc đợt điều trị
        addEnumOption(options, ParameterEntity.LAO_END_CASE, ArvEndCaseEnum.values(), "");
        addEnumOption(options, "registerTherapyStatus", RegisterTherapyStatusEnum.values(), "Tất cả");
        addEnumOption(options, ParameterEntity.REGISTRATION_TYPE, RegistrationTypeEnum.values(), "Chọn loại đăng ký");

        options.get(ParameterEntity.EARLY_DIAGNOSE).put("3", "Không xét nghiệm");

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        options.get("siteOpc").put("", "Tất cả");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> sitePrEP = siteService.getSitePrEP(getCurrentUser().getSite().getProvinceID());
        options.put("sitePrEP", new HashMap<String, String>());
        options.get("sitePrEP").put("", "Tất cả");
        for (SiteEntity site : sitePrEP) {
            options.get("sitePrEP").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtc", new HashMap<String, String>());
        options.get("siteHtc").put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get("siteHtc").put(String.valueOf(site.getID()), site.getName());
        }

        List<SiteEntity> siteHtcConfirm = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtcConfirm", new HashMap<String, String>());
        options.get("siteHtcConfirm").put("", "Tất cả");
        for (SiteEntity site : siteHtcConfirm) {
            options.get("siteHtcConfirm").put(String.valueOf(site.getID()), site.getName());
        }

        options.put("type", new HashMap<String, String>());
        options.get("type").put("", "Tất cả");
        options.get("type").put(PqmPrepTypeEnum.MOI.getKey(), PqmPrepTypeEnum.MOI.getLabel());
        options.get("type").put(PqmPrepTypeEnum.CU.getKey(), PqmPrepTypeEnum.CU.getLabel());

        options.put("treatment", new HashMap<String, String>());
        options.get("treatment").put(PqmPrepTreatmentEnum.HANG_NGAY.getKey(), PqmPrepTreatmentEnum.HANG_NGAY.getLabel());
        options.get("treatment").put(PqmPrepTreatmentEnum.TINH_HUONG.getKey(), PqmPrepTreatmentEnum.TINH_HUONG.getLabel());

        options.put("result", new HashMap<String, String>());
        options.get("result").put(PqmPrepResultEnum.DUONG_TINH.getKey(), PqmPrepResultEnum.DUONG_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.AM_TINH.getKey(), PqmPrepResultEnum.AM_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.KHONG_RO.getKey(), PqmPrepResultEnum.KHONG_RO.getLabel());

        options.put("prep-status", new HashMap<String, String>());
        options.get("prep-status").put(PqmPrepStatusEnum.TIEP_TUC.getKey(), PqmPrepStatusEnum.TIEP_TUC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getKey(), PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getKey(), PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_KHAC.getKey(), PqmPrepStatusEnum.DUNG_DO_KHAC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getKey(), PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.MAT_DAU.getKey(), PqmPrepStatusEnum.MAT_DAU.getLabel());

        options.put("blocked", new HashMap<String, String>());
        options.get("blocked").put("1", "Không được ghi đè");
        options.get("blocked").put("0", "Được phép ghi đè");

        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        options.put("years", new LinkedHashMap<String, String>());
//        options.get("years").put("0", "Tất cả");
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
//        int month = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        options.put("months", new LinkedHashMap<String, String>());
//        options.get("months").put("0", "Tất cả");
        for (int i = 1; i <= 12; i++) {
            options.get("months").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        options.put("month", new LinkedHashMap<String, String>());
        for (int i = 1; i <= 12; i++) {
            options.get("month").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }

}
