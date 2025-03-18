package com.gms.controller.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.PqmSendEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HubScheduledEntity;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmDataExcel;
import com.gms.entity.excel.opc.PqmReportExcel;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmForm;
import com.gms.entity.form.opc_arv.PqmReportTable;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.HubScheduledService;
import com.gms.service.LocationsService;
import com.gms.service.PqmDataService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
 * BC PQM HTC
 *
 * @author pdThang
 */
@Controller(value = "pqm_report_controllers")
public class PqmReportController extends BaseController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    @Autowired
    private PqmDataService pqmService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private HubUtils hubUtils;
    @Autowired
    private HubScheduledService hubScheduledService;

    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        Set<Long> siteIds = getSiteIds();
        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));

        options.put("years", new LinkedHashMap<String, String>());
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    private HashMap<String, List<SiteEntity>> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, List<SiteEntity>> option = new LinkedHashMap<>();

        option.put(ServiceEnum.HTC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.OPC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.PREP.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.HTC_CONFIRM.getKey(), new ArrayList<SiteEntity>());

        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.HTC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.OPC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.PREP.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.PREP.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.HTC_CONFIRM.getKey()).add(siteEntity);
        }

        return option;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, List<SiteEntity>> entry : getSites().entrySet()) {
            for (SiteEntity siteEntity : entry.getValue()) {
                ids.add(siteEntity.getID());
            }
        }
        return ids;
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

        map.put(PqmIndicatorEnum.HTS_TST_POS.getKey(), PqmIndicatorEnum.HTS_TST_POS.getLabel());
        map.put(PqmIndicatorEnum.HTS_TST_Recency.getKey(), PqmIndicatorEnum.HTS_TST_Recency.getLabel());
        map.put(PqmIndicatorEnum.POS_TO_ART.getKey(), PqmIndicatorEnum.POS_TO_ART.getLabel());
        map.put(PqmIndicatorEnum.TX_New.getKey(), PqmIndicatorEnum.TX_New.getLabel());
        map.put(PqmIndicatorEnum.TX_CURR.getKey(), PqmIndicatorEnum.TX_CURR.getLabel());
        map.put(PqmIndicatorEnum.MMD.getKey(), PqmIndicatorEnum.MMD.getLabel());
        map.put(PqmIndicatorEnum.IIT.getKey(), PqmIndicatorEnum.IIT.getLabel());
        map.put(PqmIndicatorEnum.VL_detectable.getKey(), PqmIndicatorEnum.VL_detectable.getLabel());
        map.put(PqmIndicatorEnum.TB_PREV.getKey(), PqmIndicatorEnum.TB_PREV.getLabel());
        map.put(PqmIndicatorEnum.PrEP_New.getKey(), PqmIndicatorEnum.PrEP_New.getLabel());
        map.put(PqmIndicatorEnum.PrEP_CURR.getKey(), PqmIndicatorEnum.PrEP_CURR.getLabel());
        map.put(PqmIndicatorEnum.PrEP_3M.getKey(), PqmIndicatorEnum.PrEP_3M.getLabel());
//        map.put(PqmIndicatorEnum.PrEP_Over_90.getKey(), PqmIndicatorEnum.PrEP_Over_90.getLabel());

        return map;
    }

    private List<String> getItem0() {
        List<String> list = new LinkedList<>();
        list.add(PqmIndicatorEnum.HTS_TST_POS.getKey());
        list.add(PqmIndicatorEnum.HTS_TST_Recency.getKey());
        list.add(PqmIndicatorEnum.POS_TO_ART.getKey());

        return list;
    }

    private List<String> getItem1() {
        List<String> list = new LinkedList<>();
        list.add(PqmIndicatorEnum.TX_New.getKey());
        list.add(PqmIndicatorEnum.TX_CURR.getKey());
        list.add(PqmIndicatorEnum.MMD.getKey());
        list.add(PqmIndicatorEnum.IIT.getKey());
        list.add(PqmIndicatorEnum.VL_detectable.getKey());
        list.add(PqmIndicatorEnum.TB_PREV.getKey());

        return list;
    }

    private List<String> getItem2() {
        List<String> list = new LinkedList<>();
        list.add(PqmIndicatorEnum.PrEP_New.getKey());
        list.add(PqmIndicatorEnum.PrEP_CURR.getKey());
        list.add(PqmIndicatorEnum.PrEP_3M.getKey());
//        list.add(PqmIndicatorEnum.PrEP_Over_90.getKey());

        return list;
    }

    private LinkedList<String> ageGroups() {
        LinkedList< String> list = new LinkedList<>();
        list.add("m10");
        list.add("m15");
        list.add("m20");
        list.add("m25");
        list.add("m30");
        list.add("m35");
        list.add("m40");
        list.add("m45");
        list.add("m50");

        return list;
    }

    private Map<String, String> mapAgeGroups() {
        Map<String, String> map = new HashMap<>();
        map.put("m10", "10-14");
        map.put("m15", "15-19");
        map.put("m20", "20-24");
        map.put("m25", "25-29");
        map.put("m30", "30-34");
        map.put("m35", "35-39");
        map.put("m40", "40-44");
        map.put("m45", "45-49");
        map.put("m50", "50+");

        return map;
    }

    private PqmForm getData(String month, String year, String sites, String siteSend, String tab) {
        LoggedUser currentUser = getCurrentUser();
        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        List<String> siteSends = siteSend.equals("") ? null : new ArrayList<>(Arrays.asList(siteSend.split(",", -1)));
        List<String> siteSearch = new ArrayList<>();
        Set<Long> siteIDHub = new HashSet<>();
        if (siteList != null) {
            for (String s : siteList) {
                if (!StringUtils.isEmpty(s)) {
                    siteSearch.add(s);
                    siteIDHub.add(Long.valueOf(s));
                }
            }
        } else {
            siteIDHub.addAll(getSiteIds());
        }
        List<String> siteSendSearch = new ArrayList<>();
        if (siteSends != null) {
            for (String s : siteSends) {
                if (!StringUtils.isEmpty(s)) {
                    siteSendSearch.add(s);
                }
            }
        }

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        Date firstDay = TextUtils.getFirstDayOfMonth(m, y);
        Date lastDay = TextUtils.getLastDayOfMonth(m, y);

        PqmForm form = new PqmForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("BC_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Kết quả chỉ số Xét nghiệm - Điều trị - PrEP");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setMonth(m);
        form.setYear(y);
        form.setSites(siteSearch.isEmpty() ? null : siteSearch);
        form.setSiteSends(siteSendSearch.isEmpty() ? null : siteSendSearch);
        form.setOpcManager(isOpcManager());

        form.setUpdate(false);
        form.setHtc(isHTC());
        form.setPac(isPAC());
        form.setOpc(isOPC());
        form.setHtcConfirm(isConfirm());
        form.setPrep(isPrEP());
        form.setEdit(false);
        form.setSend(false);

        String hub = currentUser.getSite().getHub();
        form.setHub(StringUtils.isEmpty(hub) ? "" : hub);

        HashMap<String, String> allsiteOfProvince = new LinkedHashMap<>();
        allsiteOfProvince.put("", "Chọn cơ sở");
        HashMap<String, String> siteOptions = new LinkedHashMap<>();

        HashMap<String, String> siteSendOption = new LinkedHashMap<>();
        Set<Long> allSiteOfProvinces = new HashSet<>();
        siteOptions.put("", "Tất cả");
        siteSendOption.put("", "Tất cả");
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());

        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());

        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.OPC.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());
        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.PREP.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());
        }

        int currentYear = StringUtils.isEmpty(year) ? Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")) : Integer.valueOf(year);
        int currentMonth = StringUtils.isEmpty(month) ? Integer.valueOf(TextUtils.formatDate(new Date(), "MM")) : Integer.valueOf(month);

        Set<Long> siteSendIDs = new HashSet<>();
        if (isPAC()) {
            for (Long allSiteOfProvince : allSiteOfProvinces) {
                int x = 0;
                int z = 0;
                Date sendDate = null;
                List<PqmDataEntity> listSiteData = pqmService.findBySiteID(currentMonth, currentYear, allSiteOfProvince);
                if (listSiteData != null) {
                    for (PqmDataEntity p : listSiteData) {
                        if (p.getStatus() == Long.valueOf(PqmSendEnum.DA_GUI.getKey())) {
                            sendDate = p.getSendDate();
                            x++;
                        } else {
                            z++;
                        }
                    }
                    if (x != 0 && z == 0) {
                        siteSendIDs.add(allSiteOfProvince);
                        siteOptions.put(allSiteOfProvince.toString(), allsiteOfProvince.get(allSiteOfProvince.toString()) + " - Đã gửi (" + (sendDate == null ? "" : TextUtils.formatDate(sendDate, FORMATDATE)) + ")");
                    }
                }

            }

            List<PqmDataEntity> allDataOfProvinces = pqmService.findByIndicator(currentMonth, currentYear, allSiteOfProvinces);
            if (allDataOfProvinces != null && allDataOfProvinces.size() > 0) {
                for (PqmDataEntity a : allDataOfProvinces) {
                    if (a.getStatus() == Long.valueOf(PqmSendEnum.DA_TONG_HOP.getKey())) {
                        siteOptions.put(a.getSiteID().toString(), allsiteOfProvince.get(a.getSiteID().toString()) + " - Đã tổng hợp (" + (a.getSendDate() == null ? "" : TextUtils.formatDate(a.getUpdateAt(), FORMATDATE)) + ")");
                        siteSendIDs.add(a.getSiteID());
                    }
                    if (a.getStatus() == Long.valueOf("3")) {
                        siteOptions.put(a.getSiteID().toString(), allsiteOfProvince.get(a.getSiteID().toString()) + " - Đã tổng hợp (" + (a.getSendDate() == null ? "" : TextUtils.formatDate(a.getUpdateAt(), FORMATDATE)) + ")");
                    }
                }
            }
            form.setSiteOptions(siteOptions);
            form.setSiteSearchOptions(allsiteOfProvince);
            List<SiteEntity> listSiteSend = siteService.findByIDs(siteSendIDs);
            if (listSiteSend != null) {
                for (SiteEntity site : listSiteSend) {
                    siteSendOption.put(site.getID().toString(), site.getName());
                }
            }

        }
        form.setSiteSendOptions(siteSendOption);
        form.setSiteNameOptions(allsiteOfProvince);
        String donvi = "";
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            for (String site : form.getSites()) {
                donvi = donvi + form.getSiteNameOptions().getOrDefault(site, "") + ", ";
            }
            StringBuilder sb = new StringBuilder(donvi);
            sb.deleteCharAt(sb.length() - 2);
            donvi = sb.toString();
        }
        form.setDonvi(donvi);

        Set<Long> siteIDs = new HashSet<>();
        Set<Long> siteSendID = new HashSet<>();
        if (!isPAC()) {
            siteIDs.add(currentUser.getSite().getID());
            form.setUpdate(true);
        } else {
            if (form.getSites() != null) {
                for (String site : form.getSites()) {
                    siteIDs.add(Long.valueOf(site));
                }
            }
            if (form.getSiteSends() != null) {
                for (String site : form.getSiteSends()) {
                    siteSendID.add(Long.valueOf(site));
                }
            }
            if (form.getSites() == null && form.getSiteSends() == null) {
                siteIDs.addAll(allSiteOfProvinces);
//                form.setUpdate(true);
            }
        }
        HashMap<String, String> monthKey = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            if (isPAC()) {
                List<PqmDataEntity> data = pqmService.findByIndicator(i, currentYear, siteIDs, siteSendID);
                if (data != null) {
                    int t = 0;
                    for (PqmDataEntity p : data) {
                        if (p.getStatus() == 3) {
                            monthKey.put(String.valueOf(i), "3");
                            break;
                        }
                        if (p.getStatus() == 2) {
                            monthKey.put(String.valueOf(i), "2");
                            break;
                        }
                    }
                }
            }
        }
        List<PqmDataEntity> datas = pqmService.findByIndicator(currentMonth, currentYear, siteIDs, siteSendID);
        List<PqmDataEntity> data = new ArrayList<>();
        List<PqmDataEntity> dataSynthetic = new ArrayList<>();

        List<PqmReportTable> items = new ArrayList<>();
        List<PqmReportTable> items0 = new ArrayList<>();
        List<PqmReportTable> items1 = new ArrayList<>();
        List<PqmReportTable> items2 = new ArrayList<>();
        PqmReportTable row;
        form.setDataSiteMonth(new ArrayList<PqmDataEntity>());
        for (Map.Entry<String, String> entry : indicator().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            row = new PqmReportTable();
            row.setIndicators(indicator().get(key));
            row.setIndicatorKey(key);
            if (datas != null && !datas.isEmpty()) {
                long totalQuantity = Long.valueOf("0");
                int isSend = 0;
                for (PqmDataEntity item : datas) {
                    if (item.getIndicatorCode() != null && item.getIndicatorCode().equals(key)) {
                        if (isPAC()) {
                            if (tab != null && tab.equals("detail")) {
                                if (item.getStatus() == 2 || item.getStatus() == 3) {
                                    if (item.getIndicatorCode().equals("HTS_TST_RECENCY")) {
                                        row = getRow(row, item);//set dữ liệu
                                    } else {
                                        if (StringUtils.isNotEmpty(sites)) {
                                            if (!item.getSiteID().equals(currentUser.getSite().getID()) && form.getSites().contains(item.getSiteID().toString())) {//bỏ cơ sở hiện tại và chỉ lấy các site tim kiem
                                                dataSynthetic.add(item);//data để tổng hợp
                                            }
                                            row = getRow(row, item);//set dữ liệu
                                        } else {
                                            if (!item.getSiteID().equals(currentUser.getSite().getID()) && item.getStatus() != 0 && item.getStatus() != 1) {
                                                row = getRow(row, item);//set dữ liệu
                                            }

                                        }
                                    }
                                }
                            } else {
                                if (item.getIndicatorCode().equals("HTS_TST_RECENCY")) {
                                    row = getRow(row, item);//set dữ liệu
                                } else {
                                    if (StringUtils.isNotEmpty(sites)) {
                                        if (!item.getSiteID().equals(currentUser.getSite().getID()) && form.getSites().contains(item.getSiteID().toString())) {//bỏ cơ sở hiện tại và chỉ lấy các site tim kiem
                                            dataSynthetic.add(item);//data để tổng hợp
                                        }
                                        row = getRow(row, item);//set dữ liệu
                                    } else {
                                        if (!item.getSiteID().equals(currentUser.getSite().getID()) && item.getStatus() != 0 && item.getStatus() != 1) {
                                            row = getRow(row, item);//set dữ liệu
                                        }

                                    }
                                }
                            }

                        } else {
                            row = getRow(row, item);
                            form.getDataSiteMonth().add(item);
                        }

                        totalQuantity = totalQuantity + item.getQuantity();
                        if (item.getStatus() == 2) {
                            isSend = isSend + 1;
                        }
                        data.add(item);
                    }
                }
                if (isSend != 0) {
                    form.setUpdate(false);
                    form.setSend(true);
                }
                if (totalQuantity > Long.valueOf(0)) {
                    form.setEdit(true);
                }
            }
            items.add(row);
            form.setData(data);
            form.setDataSynthetic(dataSynthetic);
        }
        for (String string : getItem0()) {
            for (PqmReportTable item : items) {
                if (item.getIndicatorKey().equals(string)) {
                    if (item.getIndicatorKey().equals("HTS_TST_RECENCY")) {
                        item.setKeyEarly("x");
                    }
                    items0.add(item);
                }
            }
        }
        for (String string : getItem1()) {
            for (PqmReportTable item : items) {
                if (item.getIndicatorKey().equals(string)) {
                    items1.add(item);
                }
            }
        }
        for (String string : getItem2()) {
            for (PqmReportTable item : items) {
                if (item.getIndicatorKey().equals(string)) {
                    items2.add(item);
                }
            }
        }

        form.setItems0(items0);
        form.setItems1(items1);
        form.setItems2(items2);

        List<PqmDataEntity> listDataHub = pqmService.findBySiteIDsOrderBySiteID(m, y, siteIDHub);
        Map<String, List<PqmDataEntity>> mapDataHub = new HashMap<>();
        if (listDataHub != null) {
            for (PqmDataEntity e : listDataHub) {
                if (e.getQuantity().equals(Long.valueOf("0")) || (e.getStatus() != 2 && e.getStatus() != 3)) {
                    continue;
                }
                String key = e.getSiteID().toString();
                if (mapDataHub.getOrDefault(key, null) == null) {
                    mapDataHub.put(key, new ArrayList<PqmDataEntity>());
                }
                mapDataHub.get(key).add(e);

            }
        }
        form.setMapDataHub(mapDataHub);

        if (!isPAC()) {
            if (form.getDataSiteMonth() != null) {
                for (PqmDataEntity p : form.getDataSiteMonth()) {
                    if (isConfirm()) {
                        if (p.getIndicatorCode().equals("HTS_TST_RECENCY")) {
                            if (p.getStatus() == 1) {
                                form.setDocMonth("1");
                                break;
                            }
                            if (p.getStatus() == 2) {
                                form.setDocMonth("2");
                                break;
                            }
                            if (p.getStatus() == 0) {
                                form.setDocMonth("0");
                                break;
                            }
                            if (p.getStatus() == 3) {
                                form.setDocMonth("3");
                                break;
                            }
                        }
                    } else {
                        if (p.getStatus() == 1) {
                            form.setDocMonth("1");
                            break;
                        }
                        if (p.getStatus() == 2) {
                            form.setDocMonth("2");
                            break;
                        }
                        if (p.getStatus() == 0) {
                            form.setDocMonth("0");
                            break;
                        }
                        if (p.getStatus() == 3) {
                            form.setDocMonth("3");
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("ccccccc " + month + "xxx" + form.getDocMonth());
        form.setKeyMonth(monthKey);
        return form;
    }

    private PqmReportTable getRow(PqmReportTable row, PqmDataEntity item) {

        if (StringUtils.isEmpty(item.getAgeGroup()) || item.getAgeGroup().equals("none")) {
            row.setIna(row.getIna() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("10-14")) {
            row.setI10x14(row.getI10x14() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("15-19")) {
            row.setI15x19(row.getI15x19() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("20-24")) {
            row.setI20x24(row.getI20x24() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("25-29")) {
            row.setI25x29(row.getI25x29() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("30-34")) {
            row.setI30x34(row.getI30x34() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("35-39")) {
            row.setI35x39(row.getI35x39() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("40-44")) {
            row.setI40x44(row.getI40x44() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("45-49")) {
            row.setI45x49(row.getI45x49() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getAgeGroup().equals("50+")) {
            row.setI50x(row.getI50x() + Integer.valueOf(item.getQuantity().toString()));
        }

        if (item.getSexID().equals("nam")) {
            row.setMale(row.getMale() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getSexID().equals("nu")) {
            row.setFemale(row.getFemale() + Integer.valueOf(item.getQuantity().toString()));
        } else {
            row.setGna(row.getGna() + Integer.valueOf(item.getQuantity().toString()));
        }

        if (StringUtils.isEmpty(item.getObjectGroupID())) {
            row.setOna(row.getOna() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("1")) {
            row.setNtmt(row.getNtmt() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("2")) {
            row.setMd(row.getMd() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("5") || item.getObjectGroupID().equals("5.1") || item.getObjectGroupID().equals("5.2")) {
            row.setPnmt(row.getPnmt() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("9")) {
            row.setNhm(row.getNhm() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("10")) {
            row.setAids(row.getAids() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("6")) {
            row.setLao(row.getLao() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("25") || item.getObjectGroupID().equals("11")) {
            row.setLtqdtd(row.getLtqdtd() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("12")) {
            row.setNvqs(row.getNvqs() + Integer.valueOf(item.getQuantity().toString()));
        } else if (item.getObjectGroupID().equals("3")) {
            row.setMsm(row.getMsm() + Integer.valueOf(item.getQuantity().toString()));
        } else {
            row.setOther(row.getOther() + Integer.valueOf(item.getQuantity().toString()));
        }
        return row;
    }

    @GetMapping(value = {"/pqm-report/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        PqmForm form = getData(month, year, sites, siteSend, tab);

        model.addAttribute("parent_title", "Báo cáo quốc gia");
        model.addAttribute("title", "Kết quả chỉ số Xét nghiệm - Điều trị - PrEP");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        //Thêm
        model.addAttribute("indicator", indicator());
        model.addAttribute("objectGroups", objectGroups());
        model.addAttribute("ageGroups", ageGroups());
        model.addAttribute("mapAgeGroups", mapAgeGroups());

        HashMap<String, SiteEntity> listSites = new HashMap<>();
        for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
            listSites.put(item.getID().toString(), item);
        }

        model.addAttribute("sites", listSites);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isHTC", isHTC());
        model.addAttribute("isConfirm", isConfirm());
        model.addAttribute("isPrEP", isPrEP());
        model.addAttribute("isOPC", isOPC());
        model.addAttribute("tab", tab);
        model.addAttribute("hub", getCurrentUser().getSite().getHub());

        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentSite", getCurrentUser().getSite().getName());
        model.addAttribute("key", true);

        if (tab != null && tab.equals("detail")) {
            List<String> siteSynthetics = new ArrayList<>();
            for (Map.Entry<String, String> entry : form.getSiteOptions().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.contains("Đã tổng hợp")) {
                    siteSynthetics.add(key);
                }
            }
            if (!siteSynthetics.contains(sites)) {
                model.addAttribute("key", false);
            }
        }

        Map<String, Map<String, String>> mapSites = new HashMap<>();
        mapSites.put(ServiceEnum.HTC.getKey(), new HashMap<String, String>());
        mapSites.put(ServiceEnum.HTC_CONFIRM.getKey(), new HashMap<String, String>());
        mapSites.put(ServiceEnum.OPC.getKey(), new HashMap<String, String>());
        mapSites.put(ServiceEnum.PREP.getKey(), new HashMap<String, String>());
//        mapSites.get(ServiceEnum.HTC.getKey()).put("", "Chọn cơ sở");
//        mapSites.get(ServiceEnum.HTC_CONFIRM.getKey()).put("", "Chọn cơ sở");
//        mapSites.get(ServiceEnum.OPC.getKey()).put("", "Chọn cơ sở");
//        mapSites.get(ServiceEnum.PREP.getKey()).put("", "Chọn cơ sở");
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC.getKey())) {
            mapSites.get(ServiceEnum.HTC.getKey()).put(siteEntity.getID().toString(), siteEntity.getName());

        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
            mapSites.get(ServiceEnum.HTC_CONFIRM.getKey()).put(siteEntity.getID().toString(), siteEntity.getName());
        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.OPC.getKey())) {
            mapSites.get(ServiceEnum.OPC.getKey()).put(siteEntity.getID().toString(), siteEntity.getName());
        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.PREP.getKey())) {
            mapSites.get(ServiceEnum.PREP.getKey()).put(siteEntity.getID().toString(), siteEntity.getName());
        }
        model.addAttribute("mapSites", mapSites);

        Map<String, String> services = new HashMap<>();
        Map<String, String> site = new HashMap<>();
        site.put("", "Chọn cơ sở");
        services.put("", "Chọn dịch vụ");
        services.put("1", "Tư vấn xét nghiệm");
        services.put("2", "Xét nghiệm khẳng định");
        services.put("3", "Điều trị ARV");
        services.put("4", "Dịch vụ PrEP");

        model.addAttribute("services", services);
        model.addAttribute("site", site);

        if (!isPAC()) {
            form.setKeyMonth(new HashMap<String, String>());
            for (int i = 1; i <= 12; i++) {
                PqmForm monthForm = getData(String.valueOf(i), year, sites, siteSend, tab);
                form.getKeyMonth().put(String.valueOf(i), monthForm.getDocMonth());
            }
        }
        model.addAttribute("monthKey", form.getKeyMonth());

        return isPAC() ? "report/pqm/pqm_index_pac" : "report/pqm/pqm_index";
    }

    @GetMapping(value = {"/pqm-report/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend
    ) throws Exception {
        PqmForm form = getData(month, year, sites, siteSend, tab);
        return exportExcel(new PqmReportExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-report/excel-data.html"})
    public ResponseEntity<InputStreamResource> actionExportExcelData(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend) throws Exception {

        PqmForm form = getData(month, year, sites, siteSend, tab);
        form.setFileName("DuLieuPQM_" + "Thang" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(getCurrentUser().getSiteProvince().getName()));
        HashMap<String, String> provincePQM = new HashMap<>();
        HashMap<String, String> districtPQM = new HashMap<>();
        HashMap<String, String> provinceNamePQM = new HashMap<>();
        HashMap<String, String> districtNamePQM = new HashMap<>();
        for (ProvinceEntity p : locationsService.findAllProvince()) {
            provincePQM.put(p.getID(), p.getPqmCode());
            provinceNamePQM.put(p.getID(), p.getName());
        }
        for (DistrictEntity d : locationsService.findAllDistrict()) {
            districtPQM.put(d.getID(), d.getPqmCode());
            districtNamePQM.put(d.getID(), d.getName());
        }
        form.setDistrictPQM(districtPQM);
        form.setProvincePQM(provincePQM);
        form.setDistrictNamePQM(districtNamePQM);
        form.setProvinceNamePQM(provinceNamePQM);

        return exportExcel(new PqmDataExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-report/send.html"})
    public String actionSend(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend,
            RedirectAttributes redirectAttributes
    ) {
        PqmForm form = getData(month, year, sites, siteSend, tab);
        Date currentDate = new Date();
        if (form.isSend()) {
            redirectAttributes.addFlashAttribute("error", String.format("Báo cáo đã được tỉnh tổng hợp, không thể gửi lại"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }

        if (form.getData() != null && !form.getData().isEmpty()) {
            for (PqmDataEntity entity : form.getData()) {
                entity.setStatus(1);
                entity.setSendDate(currentDate);
                pqmService.save(entity);
            }
            redirectAttributes.addFlashAttribute("success", String.format("Đã gửi báo cáo thành công cho cơ sở tuyến tỉnh"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Báo cáo chưa có dữ liệu, chưa thể gửi đi"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }

    }

    @GetMapping(value = {"/pqm-report/synthetic.html"})
    public String actionSynthetic(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Long siteID = getCurrentUser().getSite().getID();

            //reset
            pqmService.resetDataSite(siteID, Integer.valueOf(month), Integer.valueOf(year));
            if (isHTC()) {
                pqmService.getHTS_TST_POS(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPOS_TO_ART(Integer.valueOf(month), Integer.valueOf(year), siteID);
            }
            if (isOPC()) {
                pqmService.getTX_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTX_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getMMD(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getIIT(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getVL_detectable(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTB_PREV(Integer.valueOf(month), Integer.valueOf(year), siteID);
            }
            if (isPrEP()) {
                pqmService.getPrEP_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_3M(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_Over_90(Integer.valueOf(month), Integer.valueOf(year), siteID);
            }

            redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp thành công báo cáo."));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", String.format("Đã xảy ra lỗi khi tổng hợp báo cáo."));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }
    }

    public void actionSyntheticsx(String month, String year, List<Long> siteIDs) {
        Set<Long> sites = new HashSet<>();
        for (Long siteID : siteIDs) {
            sites.add(siteID);
        }
        try {
            //reset
            pqmService.resetDataSites(siteIDs, Integer.valueOf(month), Integer.valueOf(year));
            for (Long siteID : sites) {
//            if (isHTC()) {
                pqmService.getHTS_TST_POS(Integer.valueOf(month), Integer.valueOf(year), siteID);
//                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPOS_TO_ART(Integer.valueOf(month), Integer.valueOf(year), siteID);
//            if (isOPC()) {
                pqmService.getTX_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTX_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getMMD(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getIIT(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getVL_detectable(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTB_PREV(Integer.valueOf(month), Integer.valueOf(year), siteID);
//            if (isPrEP()) {
                pqmService.getPrEP_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_3M(Integer.valueOf(month), Integer.valueOf(year), siteID);
//                pqmService.getPrEP_Over_90(Integer.valueOf(month), Integer.valueOf(year), siteID);
            }
            for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
            }
            for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteEntity.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = {"/pqm-report/synthetics.html"})
    public String actionSynthetic(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend,
            RedirectAttributes redirectAttributes
    ) {

        int currentTime = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyyMM"));
        int paramTime = Integer.valueOf(year + (Integer.valueOf(month) < 10 ? "0" + month : month));

        if (currentTime == paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo!"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }
        if (currentTime < paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của tháng sau tháng hiện tại. Vui lòng kiểm tra lại!"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }

        LoggedUser currentUser = getCurrentUser();
        List<String> allSiteOfProvinces = new ArrayList<>();
        List<Long> allSiteImport = new ArrayList<>();
        Set<Long> siteIDsx = new HashSet<>();
        Set<Long> siteIDhub0 = new HashSet<>();

        HashMap<String, List<SiteEntity>> siteOptions = getSites();

        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.HTC.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }

        }
        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.OPC.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }

        }
//        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.HTC_CONFIRM.getKey())) {
//            allSiteOfProvinces.add(siteEntity.getID().toString());
//            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
//                allSiteImport.add(siteEntity.getID());
//                siteIDsx.add(siteEntity.getID());
//            }
//            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
//                siteIDhub0.add(siteEntity.getID());
//            }
//
//        }
        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.PREP.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }
        }
        //tổng hợp tại cơ sở
        if (!allSiteImport.isEmpty()) {
            actionSyntheticsx(month, year, allSiteImport);
        }

        siteIDsx.addAll(siteIDhub0);

        List<PqmDataEntity> datas = pqmService.findBySiteIDs(Integer.valueOf(month), Integer.valueOf(year), siteIDsx);

        Date currentDate = new Date();
        Set<Long> currentSite = new HashSet<>();
        currentSite.add(currentUser.getSite().getID());
        List<PqmDataEntity> listData = pqmService.findByIndicator(Integer.valueOf(month), Integer.valueOf(year), currentSite);
        List<String> listKeyData = new ArrayList<>();
        Map<String, PqmDataEntity> mapDatas = new HashMap<>();

        if (listData != null) {
            List<PqmDataEntity> listDatas = new ArrayList<>();
            for (PqmDataEntity e : listData) {
                listKeyData.add(e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID());
                mapDatas.put(e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID(), e);
                e.setQuantity(Long.valueOf("0"));
                listDatas.add(e);
            }
            pqmService.saveAlls(listDatas);

        }
        try {

            List<PqmDataEntity> items = new ArrayList<>();
            if (datas != null) {
                Map<String, List<PqmDataEntity>> map = new HashMap<>();
                for (PqmDataEntity e : datas) {
                    if (siteIDhub0.contains(e.getSiteID()) && e.getStatus() == 0) {
                        continue;
                    }
                    String key = e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID();
                    if (map.get(key) == null) {
                        map.put(key, new ArrayList<PqmDataEntity>());
                    }
                    map.get(key).add(e);
                    e.setStatus(2);
//               save
                    items.add(e);

                }

                PqmDataEntity nam;
                for (Map.Entry<String, List<PqmDataEntity>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    List<PqmDataEntity> value = entry.getValue();

                    nam = mapDatas.getOrDefault(key, null);
                    if (nam == null) {
                        nam = new PqmDataEntity();
                        nam.setCreateAT(currentDate);
                        nam.setMonth(Integer.valueOf(month));
                        nam.setYear(Integer.valueOf(year));
                        nam.setSiteID(currentUser.getSite().getID());
                        nam.setStatus(3);
                        nam.setSendDate(null);
                    }
                    nam.setQuantity(Long.valueOf("0"));
                    for (PqmDataEntity pqmDataEntity : value) {
                        nam.setIndicatorCode(pqmDataEntity.getIndicatorCode());
                        nam.setSexID(pqmDataEntity.getSexID());
                        nam.setAgeGroup(pqmDataEntity.getAgeGroup());
                        nam.setObjectGroupID(pqmDataEntity.getObjectGroupID());
                        nam.setQuantity(nam.getQuantity() + pqmDataEntity.getQuantity());
                    }
                    items.add(nam);

                }

            }

            pqmService.saveAlls(items);

            return redirect(UrlUtils.pqmApiHub() + String.format("?year=%s&month=%s", year, month));
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", String.format("Đã xảy ra lỗi khi tổng hợp"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }

    }

    @GetMapping(value = {"/pqm-report/api.html"})
    public String actionApiHub(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "siteSend", required = false, defaultValue = "") String siteSend,
            RedirectAttributes redirectAttributes) throws Exception {

        LoggedUser currentUser = getCurrentUser();

        PqmForm form = getData(month, year, sites, siteSend, tab);
        form.setFileName("DuLieuPQM_" + "Thang" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(currentUser.getSiteProvince().getName()));
        HashMap<String, String> provincePQM = new HashMap<>();
        HashMap<String, String> districtPQM = new HashMap<>();
        HashMap<String, String> provinceNamePQM = new HashMap<>();
        HashMap<String, String> districtNamePQM = new HashMap<>();
        for (ProvinceEntity p : locationsService.findAllProvince()) {
            provincePQM.put(p.getID(), p.getPqmCode());
            provinceNamePQM.put(p.getID(), p.getName());
        }
        for (DistrictEntity d : locationsService.findAllDistrict()) {
            districtPQM.put(d.getID(), d.getPqmCode());
            districtNamePQM.put(d.getID(), d.getName());
        }
        form.setDistrictPQM(districtPQM);
        form.setProvincePQM(provincePQM);
        form.setDistrictNamePQM(districtNamePQM);
        form.setProvinceNamePQM(provinceNamePQM);
        form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
        form.setCurrentProvinceID(currentUser.getSite().getProvinceID());

        try {
            Map<String, Object> result = hubUtils.sendToPqm(form);
            Gson g = new Gson();
            MainForm p = g.fromJson(g.toJson(result), MainForm.class);
            boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
            if (succeed) {
                redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
            } else {
                List<String> errors = new ArrayList<>();
                if (p.getError() != null && StringUtils.isNotEmpty(p.getError().getErrorMessage())) {
                    errors.add(p.getError().getErrorMessage());
                } else if (p.getData() != null && p.getData().getError_rows() != null && !p.getData().getError_rows().isEmpty()) {
                    for (error_rows error_row : p.getData().getError_rows()) {
                        errors.add(error_row.getError());
                    }
                }
                Set<String> errorx = new HashSet<>();
                for (String error : errors) {
                    if (error.equals("Year is not defined")) {
                        errorx.add("Dữ liệu không có năm");
                    } else if (error.equals("Month is not defined")) {
                        errorx.add("Dữ liệu không có tháng");
                    } else if (error.contains("Site")) {
                        errorx.add("Không tìm thấy cơ sở #" + error.replaceAll("\\D+", "") + " trên Provincial Dashboards");
                    } else {
                        errorx.add(error);
                    }
                }

                redirectAttributes.addFlashAttribute("false_key", errorx);
                redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp và gửi số liệu báo cáo lên PQM không thành công"));
                return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
            }

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp và gửi số liệu báo cáo lên PQM không thành công"));
            return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
        }
    }

    @GetMapping(value = {"/pqm-report/synthetics-all.html"})
    public String actionSyntheticAll(
            RedirectAttributes redirectAttributes
    ) {

        LoggedUser currentUser = getCurrentUser();
        pqmService.deleteByProvinceID(currentUser.getSite().getProvinceID());
        HubScheduledEntity entity = new HubScheduledEntity();
        entity.setIsRun(true);
        entity.setCreateAt(new Date());
        entity.setStaffID(currentUser.getUser().getID());
        entity.setProvinceID(currentUser.getSite().getProvinceID());
        entity.setCurrentSiteID(currentUser.getSite().getID());
        hubScheduledService.save(entity);

        redirectAttributes.addFlashAttribute("success", String.format("Đã gửi yêu cầu tổng hợp lại toàn bộ và gửi số liệu báo cáo lên PQM. Vui lòng kiểm tra lại sau vài giờ!"));
        return redirect(UrlUtils.pqmReportIndex());

    }

    @GetMapping(value = {"/pqm-report/synthetics-all-bu.html"})
    public String actionSyntheticAllBU(
            RedirectAttributes redirectAttributes
    ) {

        int currentMonth = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));

        LoggedUser currentUser = getCurrentUser();
        List<String> allSiteOfProvinces = new ArrayList<>();
        List<Long> allSiteImport = new ArrayList<>();
        Set<Long> siteIDsx = new HashSet<>();
        Set<Long> siteIDhub0 = new HashSet<>();

        HashMap<String, List<SiteEntity>> siteOptions = getSites();

        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.HTC.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }

        }
        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.OPC.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }

        }
        for (SiteEntity siteEntity : siteOptions.get(ServiceEnum.PREP.getKey())) {
            allSiteOfProvinces.add(siteEntity.getID().toString());
            if (siteEntity.getHub() != null && (siteEntity.getHub().equals("1") || siteEntity.getHub().equals("3"))) {
                allSiteImport.add(siteEntity.getID());
                siteIDsx.add(siteEntity.getID());
            }
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("0")) {
                siteIDhub0.add(siteEntity.getID());
            }
        }
        //RESSET
        pqmService.deleteByProvinceID(getCurrentUser().getSite().getProvinceID());

        int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        for (int year = 1990; year <= currentYear; year++) {
            for (int month = 1; month < 13; month++) {
                if (year == currentYear && month == currentMonth) {
                    break;
                }

                //Dữ liệu Tổng hợp
                if (!allSiteImport.isEmpty()) {
                    actionSyntheticsAll(month + "", year + "", allSiteImport);
                }

                siteIDsx.addAll(siteIDhub0);

                List<PqmDataEntity> datas = pqmService.findBySiteIDs(Integer.valueOf(month), Integer.valueOf(year), siteIDsx);

                Date currentDate = new Date();
                Set<Long> currentSite = new HashSet<>();
                currentSite.add(currentUser.getSite().getID());
                List<PqmDataEntity> listData = pqmService.findByIndicator(Integer.valueOf(month), Integer.valueOf(year), currentSite);
                List<String> listKeyData = new ArrayList<>();
                Map<String, PqmDataEntity> mapDatas = new HashMap<>();

                if (listData != null) {
                    List<PqmDataEntity> listDatas = new ArrayList<>();
                    for (PqmDataEntity e : listData) {
                        listKeyData.add(e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID());
                        mapDatas.put(e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID(), e);
                        e.setQuantity(Long.valueOf("0"));
                        listDatas.add(e);
                    }
                    pqmService.saveAlls(listDatas);

                }
                try {
                    List<PqmDataEntity> items = new ArrayList<>();
                    if (datas != null) {
                        Map<String, List<PqmDataEntity>> map = new HashMap<>();
                        for (PqmDataEntity e : datas) {
                            if (siteIDhub0.contains(e.getSiteID()) && e.getStatus() == 0) {
                                continue;
                            }
                            String key = e.getIndicatorCode() + e.getObjectGroupID() + e.getAgeGroup() + e.getSexID();
                            if (map.get(key) == null) {
                                map.put(key, new ArrayList<PqmDataEntity>());
                            }
                            map.get(key).add(e);
                            e.setStatus(2);
//               save
                            items.add(e);

                        }

                        PqmDataEntity nam;
                        for (Map.Entry<String, List<PqmDataEntity>> entry : map.entrySet()) {
                            String key = entry.getKey();
                            List<PqmDataEntity> value = entry.getValue();

                            nam = mapDatas.getOrDefault(key, null);
                            if (nam == null) {
                                nam = new PqmDataEntity();
                                nam.setCreateAT(currentDate);
                                nam.setMonth(Integer.valueOf(month));
                                nam.setYear(Integer.valueOf(year));
                                nam.setSiteID(currentUser.getSite().getID());
                                nam.setStatus(3);
                                nam.setSendDate(null);
                            }
                            nam.setQuantity(Long.valueOf("0"));
                            for (PqmDataEntity pqmDataEntity : value) {
                                nam.setIndicatorCode(pqmDataEntity.getIndicatorCode());
                                nam.setSexID(pqmDataEntity.getSexID());
                                nam.setAgeGroup(pqmDataEntity.getAgeGroup());
                                nam.setObjectGroupID(pqmDataEntity.getObjectGroupID());
                                nam.setQuantity(nam.getQuantity() + pqmDataEntity.getQuantity());
                            }
                            items.add(nam);

                        }

                    }

                    pqmService.saveAlls(items);

                    //Gui API
                    PqmForm form = getData(month + "", year + "", "", "", "");
                    form.setFileName("DuLieuPQM_" + "Thang" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(currentUser.getSiteProvince().getName()));
                    HashMap<String, String> provincePQM = new HashMap<>();
                    HashMap<String, String> districtPQM = new HashMap<>();
                    HashMap<String, String> provinceNamePQM = new HashMap<>();
                    HashMap<String, String> districtNamePQM = new HashMap<>();
                    for (ProvinceEntity p : locationsService.findAllProvince()) {
                        provincePQM.put(p.getID(), p.getPqmCode());
                        provinceNamePQM.put(p.getID(), p.getName());
                    }
                    for (DistrictEntity d : locationsService.findAllDistrict()) {
                        districtPQM.put(d.getID(), d.getPqmCode());
                        districtNamePQM.put(d.getID(), d.getName());
                    }
                    form.setDistrictPQM(districtPQM);
                    form.setProvincePQM(provincePQM);
                    form.setDistrictNamePQM(districtNamePQM);
                    form.setProvinceNamePQM(provinceNamePQM);
                    form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
                    form.setCurrentProvinceID(currentUser.getSite().getProvinceID());

                    Map<String, Object> result = hubUtils.sendToPqm(form);
                    Gson g = new Gson();
                    MainForm p = g.fromJson(g.toJson(result), MainForm.class);
                    boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
                    if (succeed) {
                        redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                        return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
                    } else {
                        List<String> errors = new ArrayList<>();
                        if (p.getError() != null && StringUtils.isNotEmpty(p.getError().getErrorMessage())) {
                            errors.add(p.getError().getErrorMessage());
                        } else if (p.getData() != null && p.getData().getError_rows() != null && !p.getData().getError_rows().isEmpty()) {
                            for (error_rows error_row : p.getData().getError_rows()) {
                                errors.add(error_row.getError());
                            }
                        }
                        Set<String> errorx = new HashSet<>();
                        for (String error : errors) {
                            if (error.equals("Year is not defined")) {
                                errorx.add("Dữ liệu không có năm");
                            } else if (error.equals("Month is not defined")) {
                                errorx.add("Dữ liệu không có tháng");
                            } else if (error.contains("Site")) {
                                errorx.add("Không tìm thấy cơ sở #" + error.replaceAll("\\D+", "") + " trên Provincial Dashboards");
                            } else {
                                errorx.add(error);
                            }
                        }

                    }

                } catch (Exception e) {
//                    e.getMessage();
//                    e.printStackTrace();
//                    redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp lại và gửi số liệu báo cáo lên PQM không thành công"));
//                    return redirect(UrlUtils.pqmReportIndex() + String.format("?year=%s&month=%s", year, month));
                }

            }
        }
        redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp lại toàn bộ và gửi số liệu báo cáo lên PQM thành công"));
        return redirect(UrlUtils.pqmReportIndex());

    }

    public void actionSyntheticsAll(String month, String year, List<Long> siteIDs) {
        Set<Long> sites = new HashSet<>();
        for (Long siteID : siteIDs) {
            sites.add(siteID);
        }
        try {
            //reset

            for (Long siteID : sites) {
//            if (isHTC()) {
                pqmService.getHTS_TST_POS(Integer.valueOf(month), Integer.valueOf(year), siteID);
//                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPOS_TO_ART(Integer.valueOf(month), Integer.valueOf(year), siteID);
//            if (isOPC()) {
                pqmService.getTX_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTX_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getMMD(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getIIT(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getVL_detectable(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getTB_PREV(Integer.valueOf(month), Integer.valueOf(year), siteID);
//            if (isPrEP()) {
                pqmService.getPrEP_New(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_CURR(Integer.valueOf(month), Integer.valueOf(year), siteID);
                pqmService.getPrEP_3M(Integer.valueOf(month), Integer.valueOf(year), siteID);
//                pqmService.getPrEP_Over_90(Integer.valueOf(month), Integer.valueOf(year), siteID);
            }
            for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
            }
            for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteEntity.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
