package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PQMForm;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_report")
public class PqmReportController extends PqmController {

    private HashMap<String, SiteEntity> getSites(Set<String> services) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        List<Long> ids = siteService.getProgenyID(currentUser.getSite().getID());
        for (SiteEntity site : siteService.findByServiceAndSiteID(services, new HashSet<>(ids))) {
            option.put(String.valueOf(site.getID()), site);
        }
        return option;
    }

    private LinkedHashMap<String, String> objectGroups() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("1", "Nghiện chích ma túy (NCMT)");
        map.put("2", "Người hành nghề mại dâm");
        map.put("5", "Phụ nữ mang thai");
        map.put("9", "Người bán máu /hiến máu /cho máu");
        map.put("10", "Bệnh nhân nghi ngờ AIDS");
        map.put("6", "Bệnh nhân Lao");
        map.put("25", "Bệnh nhân mắc các nhiễm trùng LTQĐTD");
        map.put("12", "Thanh niên khám tuyển nghĩa vụ quân sự");
        map.put("3", "Nam quan hệ tình dục với nam (MSM)");
        map.put("7", "Các đối tượng khác");

        return map;
    }

    private LinkedHashMap<String, String> indicator() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("HTS_TST_POS", "HIV dương tính");
        map.put("HTS_TST_Recency", "Kết luận Nhiễm mới");
        map.put("POS_TO_ART", "Khách hàng Dương tính chuyển gửi điều trị thành công");
        map.put("IMS_TST_POS", "Dương tính nhận kết quả");
        map.put("IMS_POS_ART", "Dương tính đã chuyển gửi điều trị");
        map.put("IMS_TST_Recency", "Kết quả xét nghiệm ban đầu là nhiễm mới");
        map.put("PrEP_New", "Khách hàng mới điều trị PrEP lần đầu");
        map.put("PrEP_CURR", "Khách hàng đang điều trị PrEP");
        map.put("PrEP_3M", "%  khách hàng điều trị PrEP liên tục >= 3 tháng");
        map.put("TX_New", "Mới  điều trị ARV");
        map.put("TX_CURR", "Bệnh nhân đang điều trị ARV");
        map.put("MMD", "Bệnh nhân được cấp thuốc nhiều tháng");
        map.put("IIT", "Bệnh nhân gián đoạn điều trị");
        map.put("VL_detectable", "% Bệnh nhân có TLVR từ ngưỡng phát hiện");
        map.put("TB_PREV", "Bệnh nhân điều trị dự phòng Lao");
        map.put("ARV_2MoS", "% thuốc ARV dự trù đủ (hơn 2 tháng) tại cơ sở điều trị ");
        map.put("ARV_Consu_Plan", "% thuốc ARV đã cấp phát trên số lượng theo kế hoạch");
        map.put("No_SHI", "Không có thẻ BHYT");
        map.put("SHI_ART", "Điều trị ARV bằng thẻ BHYT");
        map.put("SHI_MMD", "Bệnh nhân được cấp phát thuốc qua BHYT từ 3 tháng trở lên");
        map.put("SHI_VL", "Bệnh nhân XN TLVR qua BHYT");
        map.put("clients_ExpStigma", "% khách hàng bị kỳ thị");
        map.put("clients_knowVL", "% khách hàng biết về TLVR của mình");
        map.put("clients_knowRECENCY", "% khách hàng biết kết quả nhiễm mới của mình ");
        map.put("clients_ExpIPV", "% khách hàng bị bạn tình/bạn chích bạo hành/ép buộc trong khi nhận dịch vụ xét nghiệm ");
        map.put("HTS_TST_1st_Time", "% khách hàng xét nghiệm HIV lần đầu");

        return map;
    }

    private List<String> objectGroup() {
        List<String> list = new LinkedList<>();
        list.add("1");
        list.add("2");
        list.add("5");
        list.add("9");
        list.add("10");
        list.add("6");
        list.add("25");
        list.add("12");
        list.add("3");
        list.add("7");

        return list;
    }

    private LinkedList<String> ageGroups() {
        LinkedList< String> list = new LinkedList<>();
        list.add("<15");
        list.add("15-<25");
        list.add("25-49");
        list.add(">49");

        return list;
    }

    private Set<Long> getSiteIds(Set<String> services) {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites(services).entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Calendar c = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    @GetMapping(value = {"/pqm-report.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", defaultValue = "") String sites) {
        LoggedUser currentUser = getCurrentUser();

        HashMap<String, SiteEntity> mSites = new HashMap<>();
        if (isPAC()) {
            //Trường hợp là tỉnh
            Set<String> serviceIDs = new HashSet<>();
            serviceIDs.add(ServiceEnum.HTC.getKey());
            serviceIDs.add(ServiceEnum.OPC.getKey());
            serviceIDs.add(ServiceEnum.PREP.getKey());
            mSites = getSites(serviceIDs);
        } else {
            //Trường hợp là cơ sở Test/PreP/ARV
            mSites.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite());
        }
        Set<Long> siteIDs = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : mSites.entrySet()) {
            if (entry.getKey() != null) {
                siteIDs.add(Long.valueOf(entry.getKey()));
            }
        }

        model.addAttribute("sites", mSites);
        model.addAttribute("options", getOptions());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("title", "Chỉ số PQM");
        return "backend/pqm/report";
    }

    @RequestMapping(value = {"/pqm/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            PQMForm form,
            RedirectAttributes redirectAttributes) {

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
//        if (!isPAC()) {
//            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng");
//            return redirect(UrlUtils.backendHome());
//        }
//        items = new LinkedList<>();
//        PQMForm item;
//        for (Map.Entry<String, String> entry : objectGroups().entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//
//            item = new PQMForm();
//            item.setMonth("x");
//            item.setYear("x");
//            item.setQuantity("0");
//            item.setIndicatorCode("x");
//            item.setIndicatorName("x");
//            item.setProvinceID("x");
//            item.setDistrictID("x");
//            item.setSiteID("x");
//
//            item.setObjectGroupID(key);
//            item.setObjectGroupName(value);
//            item.setMultiMonth("x");
//
//            for (String ageGroup : ageGroups()) {
//                item.setAgeGroup(ageGroup);
//                item.setSexID("1");
//                items.add(item);
//                item.setSexID("2");
//                items.add(item);
//            }
//        }
        model.addAttribute("title", "Thêm mới chỉ số");
        model.addAttribute("breadcrumbTitle", "Chỉ số PQM");
        model.addAttribute("breadcrumbUrl", UrlUtils.pqmNew());
        model.addAttribute("form", form);
        model.addAttribute("indicator", indicator());
        model.addAttribute("objectGroups", objectGroups());
        model.addAttribute("ageGroups", ageGroups());
        model.addAttribute("options", getOptions());
        return "backend/pqm/form";
    }

}
