package com.gms.controller.report;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.Cd4SymtomEnum;
import com.gms.entity.constant.HBVSymtomEnum;
import com.gms.entity.constant.HCVSymtomEnum;
import com.gms.entity.constant.InsurancePayEnum;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.LaoEndCaseEnum;
import com.gms.entity.constant.LaoSymtomEnum;
import com.gms.entity.constant.MedicationAdherenceEnum;
import com.gms.entity.constant.NTCHEndCaseEnum;
import com.gms.entity.constant.NTCHSymtomEnum;
import com.gms.entity.constant.OpcTestResultEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthành
 */
public class OpcController extends BaseController {

    @Autowired
    private OpcArvService arvService;
    @Autowired
    private LocationsService locationsService;

    /**
     * Danh sách tham số từ thư viện
     *
     * @return
     * @auth vvThành
     */
    protected HashMap<String, HashMap<String, String>> getOptions() {
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
//        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN); //Phác đồ điều trị
        parameterTypes.add(ParameterEntity.PLACE_TEST); //Phác đồ điều trị
        parameterTypes.add(ParameterEntity.STOP_REGISTRATION_REASON);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        //Lấy phác đồ điều trị phù hợp
        List<ParameterEntity> treatmentRegimen = parameterService.getTreatmentRegimen(true, true);
        options.put(ParameterEntity.TREATMENT_REGIMEN, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_REGIMEN).put("", "Chọn phác đồ điều trị");
        for (ParameterEntity param : treatmentRegimen) {
            options.get(ParameterEntity.TREATMENT_REGIMEN).put(param.getCode(), param.getValue());
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
        options.get(key).put("-1", "Khác");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        options.put(ParameterEntity.TREATMENT_FACILITY, new HashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_FACILITY).put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get(ParameterEntity.TREATMENT_FACILITY).put(String.valueOf(site.getID()), site.getShortName());
        }

        HashMap<String, String> map = new HashMap<>(options.get(ParameterEntity.TREATMENT_FACILITY));
        map.put("", "Chọn cơ sở");
        map.put("-1", "Cơ sở khác");
        //Cơ sở chuyển đi
        options.put("siteOpcFrom", map);

        //Cơ sở chuyển đến
        key = "siteOpcTo";
        HashMap<String, String> mSiteOpcTo = new HashMap<>(map);
        for (SiteEntity site : siteHtc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getShortName());
        }
        options.put(key, mSiteOpcTo);

        //Cơ sở xét nghiệm CD4
        HashMap<String, String> mapCd4 = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key1 = entry.getKey();
            String value = entry.getValue();
            if (!StringUtils.isEmpty(key1)) {
                mapCd4.put(key1, value);
            }

        }
        options.put("siteCd4", mapCd4);
        options.get("siteCd4").put("", "Chọn nơi xét nghiệm");
        options.get("siteCd4").put("-1", "Cơ sở khác");
        for (SiteEntity site : siteHtc) {
            options.get("siteCd4").put(String.valueOf(site.getID()), site.getShortName());
        }

        //Nơi xét nghiệm sàng lọc
        key = "siteHtc";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getShortName());
        }

        // Lấy năm từ khi có dữ liệu đến năm hiện tại OPC
        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        OpcArvEntity arv = arvService.getFirst(siteIds);
        if (arv != null) {
            c.setTime(arv.getRegistrationTime());
        }
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }

        // Set opciton quý
        options.put("quarters", new LinkedHashMap<String, String>());
        options.get("quarters").put("0", "Quý 01");
        options.get("quarters").put("1", "Quý 02");
        options.get("quarters").put("2", "Quý 03");
        options.get("quarters").put("3", "Quý 04");

        key = "transferGSPH";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        options.get(key).put("0", "Chưa chuyển");
        options.get(key).put("1", "Đã chuyển");

        HashMap<String, String> provinces = new LinkedHashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);
        HashMap<String, String> districts = new LinkedHashMap<>();
        for (DistrictEntity entity : locationsService.findAllDistrict()) {
            districts.put(entity.getID(), entity.getName());
        }
        options.put("districts", districts);
        HashMap<String, String> wards = new LinkedHashMap<>();
        for (WardEntity entity : locationsService.findAllWard()) {
            wards.put(entity.getID(), entity.getName());
        }
        options.put("wards", wards);

        return options;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }

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

    /**
     * Lấy id cơ sở điều trị trong tỉnh
     *
     * @return
     */
    protected Set<Long> getSiteManager() {
        Set<Long> sites = new HashSet<>();
        for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
            sites.add(item.getID());
        }
        return sites;
    }
}
