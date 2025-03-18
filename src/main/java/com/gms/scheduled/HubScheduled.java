package com.gms.scheduled;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.PqmSendEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HubScheduledEntity;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmForm;
import com.gms.entity.form.opc_arv.PqmReportTable;
import com.gms.service.HubScheduledService;
import com.gms.service.LocationsService;
import com.gms.service.PqmDataService;
import com.gms.service.SiteService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author pdthang
 */
@Component
public class HubScheduled extends BaseScheduled {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    protected static final String FORMATDATETIME = "hh:mm:ss dd/MM/yyyy";

    @Autowired
    private HubScheduledService hubScheduledService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PqmDataService pqmService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private HubUtils hubUtils;

    @Async
    @Scheduled(cron = "0 0/5 * * * *")
    public void pqmHub() {
        System.out.println("START API HUB " + TextUtils.formatDate(new Date(), FORMATDATETIME));
        List<HubScheduledEntity> list = hubScheduledService.findRun();
        if (list != null) {
            for (HubScheduledEntity hubScheduledEntity : list) {
                String provinceID = hubScheduledEntity.getProvinceID();
                Long currentSiteID = hubScheduledEntity.getCurrentSiteID();

                int currentMonth = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));

                List<String> allSiteOfProvinces = new ArrayList<>();
                List<Long> allSiteImport = new ArrayList<>();
                Set<Long> siteIDsx = new HashSet<>();
                Set<Long> siteIDhub0 = new HashSet<>();

                HashMap<String, List<SiteEntity>> siteOptions = getSites(provinceID);

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
                pqmService.deleteByProvinceID(provinceID);

                int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
                for (int year = 1990; year <= currentYear; year++) {
                    for (int month = 1; month < 13; month++) {
                        if (year == currentYear && month == currentMonth) {
                            break;
                        }

                        //Dữ liệu Tổng hợp
                        if (!allSiteImport.isEmpty()) {
                            actionSyntheticsAll(month + "", year + "", allSiteImport, provinceID);
                        }

                        siteIDsx.addAll(siteIDhub0);

                        List<PqmDataEntity> datas = pqmService.findBySiteIDs(Integer.valueOf(month), Integer.valueOf(year), siteIDsx);

                        Date currentDate = new Date();
                        Set<Long> currentSite = new HashSet<>();
                        currentSite.add(currentSiteID);
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
                                        nam.setSiteID(currentSiteID);
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
                            PqmForm form = getData(month + "", year + "", "", "", "", provinceID, currentSiteID);
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
                            form.setProvincePQMcode(provincePQM.getOrDefault(provinceID, ""));
                            form.setCurrentProvinceID(provinceID);

                            Map<String, Object> result = hubUtils.sendToPqm(form);
                            Gson g = new Gson();
                            MainForm p = g.fromJson(g.toJson(result), MainForm.class);
                            boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
                            if (succeed) {
                                System.out.println("API HUB SUCCESS");
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
                            e.printStackTrace();
                            System.out.println("API HUB FALSE");
                        }

                    }
                }
                hubScheduledEntity.setIsRun(false);
                hubScheduledService.save(hubScheduledEntity);

            }

        }
        System.out.println("END API HUB " + TextUtils.formatDate(new Date(), FORMATDATETIME));

    }

    @Async
    @Scheduled(cron = "0 0 1 * * SAT")
    public void pqmHubSAT() {
        Set<String> provinces = new HashSet<>();
        provinces.add("75");
        provinces.add("72");
        provinces.add("82");
        
        Map<String, Long> currentSite = new HashMap<>();
        currentSite.put("75", Long.valueOf(706));
        currentSite.put("72", Long.valueOf(600));
        currentSite.put("82", Long.valueOf(892));

        for (String province : provinces) {
            pqmService.deleteByProvinceID(province);
            HubScheduledEntity entity = new HubScheduledEntity();
            entity.setIsRun(true);
            entity.setCreateAt(new Date());
            entity.setStaffID(Long.valueOf(0));
            entity.setProvinceID(province);
            entity.setCurrentSiteID(currentSite.getOrDefault(province, Long.valueOf(0)));
            hubScheduledService.save(entity);
        }

    }

    private HashMap<String, List<SiteEntity>> getSites(String provinceID) {
        HashMap<String, List<SiteEntity>> option = new LinkedHashMap<>();

        option.put(ServiceEnum.HTC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.OPC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.PREP.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.HTC_CONFIRM.getKey(), new ArrayList<SiteEntity>());

        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), provinceID)) {
            option.get(ServiceEnum.HTC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), provinceID)) {
            option.get(ServiceEnum.OPC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.PREP.getKey(), provinceID)) {
            option.get(ServiceEnum.PREP.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), provinceID)) {
            option.get(ServiceEnum.HTC_CONFIRM.getKey()).add(siteEntity);
        }

        return option;
    }

    public void actionSyntheticsAll(String month, String year, List<Long> siteIDs, String provinceID) {
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
            for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
            }
            for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.HTC_CONFIRM.getKey())) {
                pqmService.resetDataSiteConfirm(siteEntity.getID(), Integer.valueOf(month), Integer.valueOf(year));
                pqmService.getHTS_TST_Recency(Integer.valueOf(month), Integer.valueOf(year), siteEntity.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<Long> getSiteIds(String provinceID) {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, List<SiteEntity>> entry : getSites(provinceID).entrySet()) {
            for (SiteEntity siteEntity : entry.getValue()) {
                ids.add(siteEntity.getID());
            }
        }
        return ids;
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

    private PqmForm getData(String month, String year, String sites, String siteSend, String tab, String provinceID, Long currentSiteID) {
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
            siteIDHub.addAll(getSiteIds(provinceID));
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
        form.setFileName("BC_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Kết quả chỉ số Xét nghiệm - Điều trị - PrEP");
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setMonth(m);
        form.setYear(y);
        form.setSites(siteSearch.isEmpty() ? null : siteSearch);
        form.setSiteSends(siteSendSearch.isEmpty() ? null : siteSendSearch);

        form.setUpdate(false);
        form.setEdit(false);
        form.setSend(false);

        HashMap<String, String> allsiteOfProvince = new LinkedHashMap<>();
        allsiteOfProvince.put("", "Chọn cơ sở");
        HashMap<String, String> siteOptions = new LinkedHashMap<>();

        HashMap<String, String> siteSendOption = new LinkedHashMap<>();
        Set<Long> allSiteOfProvinces = new HashSet<>();
        siteOptions.put("", "Tất cả");
        siteSendOption.put("", "Tất cả");
        for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.HTC.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());

        }
        for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.HTC_CONFIRM.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());

        }
        for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.OPC.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());
        }
        for (SiteEntity siteEntity : getSites(provinceID).get(ServiceEnum.PREP.getKey())) {
            allsiteOfProvince.put(siteEntity.getID().toString(), siteEntity.getName());
            siteOptions.put(siteEntity.getID().toString(), siteEntity.getName() + " - Chưa gửi");
            allSiteOfProvinces.add(siteEntity.getID());
        }

        int currentYear = StringUtils.isEmpty(year) ? Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")) : Integer.valueOf(year);
        int currentMonth = StringUtils.isEmpty(month) ? Integer.valueOf(TextUtils.formatDate(new Date(), "MM")) : Integer.valueOf(month);

        Set<Long> siteSendIDs = new HashSet<>();
        if (true) {
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
        if (!true) {
            siteIDs.add(currentSiteID);
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
                        if (true) {
                            if (tab != null && tab.equals("detail")) {
                                if (item.getStatus() == 2 || item.getStatus() == 3) {
                                    if (item.getIndicatorCode().equals("HTS_TST_RECENCY")) {
                                        row = getRow(row, item);//set dữ liệu
                                    } else {
                                        if (StringUtils.isNotEmpty(sites)) {
                                            if (!item.getSiteID().equals(currentSiteID) && form.getSites().contains(item.getSiteID().toString())) {//bỏ cơ sở hiện tại và chỉ lấy các site tim kiem
                                                dataSynthetic.add(item);//data để tổng hợp
                                            }
                                            row = getRow(row, item);//set dữ liệu
                                        } else {
                                            if (!item.getSiteID().equals(currentSiteID) && item.getStatus() != 0 && item.getStatus() != 1) {
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
                                        if (!item.getSiteID().equals(currentSiteID) && form.getSites().contains(item.getSiteID().toString())) {//bỏ cơ sở hiện tại và chỉ lấy các site tim kiem
                                            dataSynthetic.add(item);//data để tổng hợp
                                        }
                                        row = getRow(row, item);//set dữ liệu
                                    } else {
                                        if (!item.getSiteID().equals(currentSiteID) && item.getStatus() != 0 && item.getStatus() != 1) {
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

}
