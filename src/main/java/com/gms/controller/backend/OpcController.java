package com.gms.controller.backend;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.Cd4SymtomEnum;
import com.gms.entity.constant.HBVSymtomEnum;
import com.gms.entity.constant.HCVSymtomEnum;
import com.gms.entity.constant.InsurancePayEnum;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.LaoDiagnoseEnum;
import com.gms.entity.constant.LaoEndCaseEnum;
import com.gms.entity.constant.LaoSymtomEnum;
import com.gms.entity.constant.MedicationAdherenceEnum;
import com.gms.entity.constant.NTCHEndCaseEnum;
import com.gms.entity.constant.NTCHSymtomEnum;
import com.gms.entity.constant.OpcTestResultEnum;
import com.gms.entity.constant.QuickTreatmentEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.SuspiciousSymptomsEnum;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author vvthanh
 */
public abstract class OpcController extends BaseController {

    protected HashMap<String, HashMap<String, String>> getOptions(boolean config, OpcArvEntity arv) {
        return getOptions(config, arv, null, null);
    }

    /**
     * Danh sách tham số từ thư viện
     *
     * @param config
     * @param arv
     * @param stage
     * @param visit
     * @return
     * @auth vvThành
     */
    protected HashMap<String, HashMap<String, String>> getOptions(boolean config, OpcArvEntity arv, OpcStageEntity stage, OpcVisitEntity visit) {
        HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());

        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE); //Câu hỏi thẻ BHYT
        parameterTypes.add(ParameterEntity.SUPPORTER_RELATION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_PCR); //Kết quả XN PCR
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT); //Biến động điều trị
        parameterTypes.add(ParameterEntity.REGISTRATION_TYPE); //Loại đăng ký
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE); //Giai đoạn lâm sàng
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT); //Trạng thái điều trị
        parameterTypes.add(ParameterEntity.PLACE_TEST); //Phác đồ điều trị
        parameterTypes.add(ParameterEntity.STOP_REGISTRATION_REASON);
        parameterTypes.add(ParameterEntity.ROUTE);
        parameterTypes.add(ParameterEntity.ARV_REGISTRATION_STATUS);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        //Lấy phác đồ điều trị phù hợp
        List<ParameterEntity> treatmentRegimen = parameterService.getTreatmentRegimen(true, true);
        options.put(ParameterEntity.TREATMENT_REGIMEN, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_REGIMEN).put("", "Chọn phác đồ điều trị");
        for (ParameterEntity param : treatmentRegimen) {
            options.get(ParameterEntity.TREATMENT_REGIMEN).put(param.getCode(), param.getValue());
        }
        String regimenConfig = siteConfig.getOrDefault(SiteConfigEnum.OPC_REGIMEN.getKey(), null);
        if (config && regimenConfig != null && !regimenConfig.equals("")) {
            List<String> arrlist = new ArrayList<>(Arrays.asList(regimenConfig.split(",")));
            if (arv != null && arv.getTreatmentRegimenID() != null) {
                arrlist.add(arv.getTreatmentRegimenID());
            }
            if (arv != null && arv.getFirstTreatmentRegimenID() != null) {
                arrlist.add(arv.getFirstTreatmentRegimenID());
            }
            if (arv != null && stage != null && stage.getOldTreatmentRegimenID() != null) {
                arrlist.add(stage.getOldTreatmentRegimenID());
            }
            if (arv != null && stage != null && stage.getTreatmentRegimenID() != null) {
                arrlist.add(stage.getTreatmentRegimenID());
            }
            if (arv != null && visit != null && visit.getOldTreatmentRegimenID() != null) {
                arrlist.add(visit.getOldTreatmentRegimenID());
            }
            if (arv != null && visit != null && visit.getTreatmentRegimenID() != null) {
                arrlist.add(visit.getTreatmentRegimenID());
            }
            options.put(ParameterEntity.TREATMENT_REGIMEN, findOptions(options.get(ParameterEntity.TREATMENT_REGIMEN), arrlist.toArray(new String[arrlist.size()])));
        }

        addEnumOption(options, ParameterEntity.ANSWER, BooleanEnum.values(), null); //Sử dụng cho câu hỏi có/không
        addEnumOption(options, "has", BooleanEnum.values(), "Chọn câu trả lời"); //Sử dụng enum boolean
        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");
        addEnumOption(options, ParameterEntity.INSURANCE_PAY, InsurancePayEnum.values(), "Chọn tỷ lệ thanh toán BHYT"); // Chọn tỉ lệ thanh toán BHYT
        addEnumOption(options, ParameterEntity.LAO_SYMTOM, LaoSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.LAO_END_CASE, LaoEndCaseEnum.values(), null); //Lý do kết thúc điều trị Lao
        addEnumOption(options, ParameterEntity.NTCH_SYMTOM, NTCHSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.NTCH_END_CASE, NTCHEndCaseEnum.values(), null); //Lý do kết thúc điều trị NTCH
        addEnumOption(options, ParameterEntity.TREATMENT_REGINMEN_STAGE, TreatmentRegimenStageEnum.values(), "Chọn bậc phác đồ điều trị");
        addEnumOption(options, ParameterEntity.MEDICATION_ADHERENCE, MedicationAdherenceEnum.values(), "Chọn mức độ tuân thủ");
        addEnumOption(options, ParameterEntity.CD4_SYMTOM, Cd4SymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.VIRAL_LOAD_SYMTOM, ViralLoadSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.ARV_END_CASE, ArvEndCaseEnum.values(), "Chọn lý do"); //Lý do kết thúc đợt điều trị
        addEnumOption(options, ParameterEntity.TEST_RESULTS, OpcTestResultEnum.values(), "Chọn kết quả"); //Kết quả xét nghiệm
        addEnumOption(options, ParameterEntity.HBV_SYMTOM, HBVSymtomEnum.values(), "Chọn lý Xét nghiệm HBV");
        addEnumOption(options, ParameterEntity.HCV_SYMTOM, HCVSymtomEnum.values(), "Chọn lý Xét nghiệm HCV");
        addEnumOption(options, ParameterEntity.REGISTRATION_TYPE, RegistrationTypeEnum.values(), "Chọn loại đăng ký");
        addEnumOption(options, ParameterEntity.QUICK_TREATMENT, QuickTreatmentEnum.values(), "Chọn thời gian điều trị nhanh");
        addEnumOption(options, ParameterEntity.SUSPICIOUS_SYMPTOMS, SuspiciousSymptomsEnum.values(), "Chọn triệu chứng");
        addEnumOption(options, ParameterEntity.LAO_DIAGNOSE, LaoDiagnoseEnum.values(), "Chọn loại hình Lao");

        //Loại bỏ Chưa được điều trị
        options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        //Kết quả XN đầu tiên - Bỏ Không xét nghiệm
//        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        //Sửa theo tltk 21/05/2020
        options.put(ParameterEntity.VIRUS_LOAD, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.VIRUS_LOAD).put("", "Chọn kết quả xn tải lượng virus");
        options.get(ParameterEntity.VIRUS_LOAD).put("1", "Không phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("6", "Dưới ngưỡng phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("2", "Từ ngưỡng phát hiện đến < 200 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("3", "Từ 200 - < 1000 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("4", ">= 1000 bản sao/ml");

        //Lấy thêm htc
        List<SiteEntity> siteHtc = siteService.getSiteHtc(currentUser.getSite().getProvinceID());
        //Nơi xét nghiệm khẳng định
        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentUser.getSite().getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Chọn nơi xét nghiệm");
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(String.valueOf(site.getID()), site.getShortName());
        }
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getShortName());
        }
        options.get(key).put("-1", "Cơ sở khác");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        options.put(ParameterEntity.TREATMENT_FACILITY, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_FACILITY).put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get(ParameterEntity.TREATMENT_FACILITY).put(String.valueOf(site.getID()), site.getShortName());
        }
        options.get(ParameterEntity.TREATMENT_FACILITY).put("-1", "Cơ sở khác");

        //Cơ sở chuyển đi
        key = "siteOpcFrom";
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            map.put(String.valueOf(site.getID()), site.getShortName());
        }
        map.put("-1", "Cơ sở khác");
        options.put(key, map);

        //Cơ sở chuyển đến
        key = "siteOpcTo";
        HashMap<String, String> mSiteOpcTo = new LinkedHashMap<>();
        mSiteOpcTo.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getShortName());
        }
        for (SiteEntity site : siteHtc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getShortName());
        }
        mSiteOpcTo.put("-1", "Cơ sở khác");
        options.put(key, mSiteOpcTo);

        //Cơ sở xét nghiệm CD4
        options.put("siteCd4", new LinkedHashMap<String, String>());
        options.get("siteCd4").put("", "Chọn nơi xét nghiệm");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key1 = entry.getKey();
            String value = entry.getValue();
            if (!StringUtils.isEmpty(key1) && !key1.equals("-1")) {
                options.get("siteCd4").put(key1, value);
            }

        }

        for (SiteEntity site : siteHtc) {
            options.get("siteCd4").put(String.valueOf(site.getID()), site.getName());
        }
        options.get("siteCd4").put("-1", "Cơ sở khác");

        //Nơi xét nghiệm sàng lọc
        key = "siteHtc";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }

        key = "transferGSPH";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        options.get(key).put("0", "Chưa chuyển");
        options.get(key).put("1", "Đã chuyển");

        //Lấy phác đồ điều trị theo bậc
        List<ParameterEntity> treatmentRegimenLevel = parameterService.getTreatmentRegimen(true, true, true);
        options.put(ParameterEntity.TREATMENT_REGIMEN + "-level1", new LinkedHashMap<String, String>());
        options.put(ParameterEntity.TREATMENT_REGIMEN + "-level2", new LinkedHashMap<String, String>());
        options.put(ParameterEntity.TREATMENT_REGIMEN + "-level3", new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level1").put("", "Chọn phác đồ điều trị");
        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level2").put("", "Chọn phác đồ điều trị");
        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level3").put("", "Chọn phác đồ điều trị");

        for (ParameterEntity param : treatmentRegimenLevel) {
            if (regimenConfig != null && !regimenConfig.equals("")) {
                List<String> arrlists = new ArrayList<>(Arrays.asList(regimenConfig.split(",")));
                if (arrlists.contains(param.getCode())) {
                    if (param.getAttribute03() != null && param.getAttribute03().equals("1")) {
                        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level1").put(param.getCode(), param.getValue());
                    }
                    if (param.getAttribute04() != null && param.getAttribute04().equals("1")) {
                        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level2").put(param.getCode(), param.getValue());
                    }
                    if (param.getAttribute05() != null && param.getAttribute05().equals("1")) {
                        options.get(ParameterEntity.TREATMENT_REGIMEN + "-level3").put(param.getCode(), param.getValue());
                    }
                }

            } else {
                if (param.getAttribute03() != null && param.getAttribute03().equals("1")) {
                    options.get(ParameterEntity.TREATMENT_REGIMEN + "-level1").put(param.getCode(), param.getValue());
                }
                if (param.getAttribute04() != null && param.getAttribute04().equals("1")) {
                    options.get(ParameterEntity.TREATMENT_REGIMEN + "-level2").put(param.getCode(), param.getValue());
                }
                if (param.getAttribute05() != null && param.getAttribute05().equals("1")) {
                    options.get(ParameterEntity.TREATMENT_REGIMEN + "-level3").put(param.getCode(), param.getValue());
                }
            }
        }

        return options;
    }

}
