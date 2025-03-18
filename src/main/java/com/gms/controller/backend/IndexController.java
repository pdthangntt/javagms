package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmLogEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffNotificationEntity;
import com.gms.entity.form.PqmIndexTable;
import com.gms.service.PqmLogService;
import com.gms.service.PqmVctRecencyService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Văn Thành
 */
@Controller(value = "backend_index")
public class IndexController extends BaseController {

    @Autowired
    private PqmLogService pqmLogService;
    @Autowired
    private PqmVctRecencyService recencyService;

    @RequestMapping(value = {"/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestHeader(name = "X-Forwarded-Host", required = false) String host,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DataPage<StaffNotificationEntity> dataPage = staffService.getNotifications(currentUser.getUser().getID(), page, size);
            model.addAttribute("dataPage", dataPage);
        }
        model.addAttribute("host", host);

        Date time = TextUtils.getFirstDayOfMonth(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);

        String to = TextUtils.formatDate(TextUtils.getLastDayOfMonth(cal.getTime()), "yyyy-MM-dd");
        String from = TextUtils.formatDate(TextUtils.getFirstDayOfMonth(cal.getTime()), "yyyy-MM-dd");

        String month = TextUtils.formatDate(cal.getTime(), "MM");
        String year = TextUtils.formatDate(cal.getTime(), "yyyy");

        System.out.println("Start 6 : " + from + "  " + to);
        String province = getCurrentUser().getSite().getProvinceID();
        List<PqmIndexTable> items = new LinkedList<>();
        Set<Long> siteHtc = new HashSet<>();
        Set<Long> siteOpc = new HashSet<>();
        Set<Long> sitePrep = new HashSet<>();
        Set<Long> siteConfirm = new HashSet<>();
        Map<Long, String> siteName = new HashMap<>();
        for (SiteEntity siteEntity : siteService.getSiteHtc(province)) {
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                siteName.put(siteEntity.getID(), siteEntity.getName());
                siteHtc.add(siteEntity.getID());
            }
        }
        for (SiteEntity siteEntity : siteService.getSiteOpc(province)) {
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                siteName.put(siteEntity.getID(), siteEntity.getName());
                siteOpc.add(siteEntity.getID());
            }
        }
        for (SiteEntity siteEntity : siteService.getSitePrEP(province)) {
            if (siteEntity.getHub() != null && siteEntity.getHub().equals("1")) {
                siteName.put(siteEntity.getID(), siteEntity.getName());
                sitePrep.add(siteEntity.getID());
            }
        }
        for (SiteEntity siteEntity : siteService.getSiteConfirm(province)) {
            siteName.put(siteEntity.getID(), siteEntity.getName());
            siteConfirm.add(siteEntity.getID());
        }

        //Xử lý
        //htc
        Map<String, Set<Long>> htcs = site(siteHtc, "Tư vấn & xét nghiệm", from, to);
        PqmIndexTable table = new PqmIndexTable();
        table.setService("Xét nghiệm sàng lọc");
        table.setImported(htcs.get("ok").size() + "/" + siteHtc.size());
        table.setSiteImported(getSiteName(htcs.get("ok"), siteName));
        table.setNotImport(htcs.get("not").size() + "/" + siteHtc.size());
        table.setSiteNotImported(getSiteName(htcs.get("not"), siteName));
        items.add(table);
        //confirms
        table = new PqmIndexTable();
        Map<String, Set<Long>> confirms = site(siteConfirm, "Xét nghiệm khẳng định", from, to);
        table.setService("Xét nghiệm nhiễm mới");
        table.setImported(confirms.get("ok").size() + "/" + siteConfirm.size());
        table.setSiteImported(getSiteName(confirms.get("ok"), siteName));
        table.setNotImport(confirms.get("not").size() + "/" + siteConfirm.size());
        table.setSiteNotImported(getSiteName(confirms.get("not"), siteName));
        items.add(table);
        //preps
        table = new PqmIndexTable();
        Map<String, Set<Long>> preps = site(sitePrep, "Dịch vụ PrEP", from, to);
        table.setService("PrEP");
        table.setImported(preps.get("ok").size() + "/" + sitePrep.size());
        table.setSiteImported(getSiteName(preps.get("ok"), siteName));
        table.setNotImport(preps.get("not").size() + "/" + sitePrep.size());
        table.setSiteNotImported(getSiteName(preps.get("not"), siteName));
        items.add(table);
        //opc
        table = new PqmIndexTable();
        Map<String, Set<Long>> opcs = site(siteOpc, "Điều trị ARV", from, to);
        table.setService("Điều trị");
        table.setImported(opcs.get("ok").size() + "/" + siteOpc.size());
        table.setSiteImported(getSiteName(opcs.get("ok"), siteName));
        table.setNotImport(opcs.get("not").size() + "/" + siteOpc.size());
        table.setSiteNotImported(getSiteName(opcs.get("not"), siteName));
        items.add(table);

        //THUỐC
        Set<Long> siteDrug = new HashSet<>();
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince("drug", province)) {
            siteName.put(siteEntity.getID(), siteEntity.getName());
            siteDrug.add(siteEntity.getID());
        }
         table = new PqmIndexTable();
        Map<String, Set<Long>> drugNew = site(siteOpc, "Tình hình sử dụng và tồn kho thuốc ARV (HMED)", from, to);
        table.setService("Tình hình sử dụng và tồn kho thuốc ARV (HMED)");
        table.setImported(drugNew.get("ok").size() + "/" + siteOpc.size());
        table.setSiteImported(getSiteName(drugNew.get("ok"), siteName));
        table.setNotImport(drugNew.get("not").size() + "/" + siteOpc.size());
        table.setSiteNotImported(getSiteName(drugNew.get("not"), siteName));
        items.add(table);
        
        table = new PqmIndexTable();
        Map<String, Set<Long>> drugs = site(siteDrug, "Kế hoạch cung ứng thuốc ARV", from, to);
        table.setService("Kế hoạch cung ứng thuốc ARV (HMED)");
        table.setImported(drugs.get("ok").size() + "/" + siteDrug.size());
        table.setSiteImported(getSiteName(drugs.get("ok"), siteName));
        table.setNotImport(drugs.get("not").size() + "/" + siteDrug.size());
        table.setSiteNotImported(getSiteName(drugs.get("not"), siteName));
        items.add(table);

        table = new PqmIndexTable();
        Map<String, Set<Long>> drugsPlan = site(siteDrug, "Tình hình sử dụng thuốc ARV", from, to);
        table.setService("Tình hình sử dụng thuốc ARV (HMED)");
        table.setImported(drugsPlan.get("ok").size() + "/" + siteDrug.size());
        table.setSiteImported(getSiteName(drugsPlan.get("ok"), siteName));
        table.setNotImport(drugsPlan.get("not").size() + "/" + siteDrug.size());
        table.setSiteNotImported(getSiteName(drugsPlan.get("not"), siteName));
        items.add(table);

        table = new PqmIndexTable();
        Map<String, Set<Long>> drugsELMIS = site(siteDrug, "Tình hình cung ứng, sử dụng thuốc thuốc ARV (eLMIS)", from, to);
        table.setService("Tình hình cung ứng, sử dụng thuốc thuốc ARV (eLMIS)");
        table.setImported(drugsELMIS.get("ok").size() + "/" + siteDrug.size());
        table.setSiteImported(getSiteName(drugsELMIS.get("ok"), siteName));
        table.setNotImport(drugsELMIS.get("not").size() + "/" + siteDrug.size());
        table.setSiteNotImported(getSiteName(drugsELMIS.get("not"), siteName));
        items.add(table);

        //SHI
        Set<Long> siteShi = new HashSet<>();
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince("shi", province)) {
            siteName.put(siteEntity.getID(), siteEntity.getName());
            siteShi.add(siteEntity.getID());
        }
        table = new PqmIndexTable();
        Map<String, Set<Long>> shis = site(siteShi, "Bệnh nhân nhận thuốc ARV", from, to);
        table.setService("Bệnh nhân nhận thuốc ARV (eLMIS)");
        table.setImported(shis.get("ok").size() + "/" + siteShi.size());
        table.setSiteImported(getSiteName(shis.get("ok"), siteName));
        table.setNotImport(shis.get("not").size() + "/" + siteShi.size());
        table.setSiteNotImported(getSiteName(shis.get("not"), siteName));
        items.add(table);

        table = new PqmIndexTable();
        Map<String, Set<Long>> mmds = site(siteShi, "Tình hình kê đơn thuốc ARV", from, to);
        table.setService("Tình hình kê đơn thuốc ARV (eLMIS)");
        table.setImported(mmds.get("ok").size() + "/" + siteShi.size());
        table.setSiteImported(getSiteName(mmds.get("ok"), siteName));
        table.setNotImport(mmds.get("not").size() + "/" + siteShi.size());
        table.setSiteNotImported(getSiteName(mmds.get("not"), siteName));
        items.add(table);

        List<PqmIndexTable> itemConfirms = new LinkedList<>();
        for (Long long1 : siteConfirm) {
            List<PqmVctRecencyEntity> entitys = recencyService.findBySiteID(long1);
            if (entitys == null) {
                continue;
            }
            int x = 0;
            int y = 0;
            for (PqmVctRecencyEntity entity : entitys) {
                if (entity.getPqmVctID() != null && entity.getPqmVctID() != 0) {
                    x++;
                } else {
                    y++;
                }
            }
            table = new PqmIndexTable();
            table.setService(siteName.get(long1));
            table.setImported(x + "");
            table.setSiteImported(y + "");
            itemConfirms.add(table);
        }

        model.addAttribute("title", "Theo dõi tình trạng nhập dữ liệu");
        model.addAttribute("items", items);
        model.addAttribute("itemConfirms", itemConfirms);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("isPAC", isPAC());

        return "backend/index";
    }

    private String getSiteName(Set<Long> siteIDs, Map<Long, String> siteName) {
        String name = "";
        for (Long siteID : siteIDs) {
            name = name + siteName.get(siteID) + "; ";
        }
        if (name.contains(";")) {
            name = name.substring(0, name.length() - 2);
        }
        return name;
    }

    private Map<String, Set<Long>> site(Set<Long> allSite, String service, String from, String to) {
        Map<String, Set<Long>> map = new HashMap<>();
        List<PqmLogEntity> logs = pqmLogService.findBySiteIDAndService(allSite, service, from, to);
        Set<Long> siteSend = new HashSet<>();
        if (logs != null) {
            for (PqmLogEntity log : logs) {
                if (log.getSiteID() != null) {
                    siteSend.add(log.getSiteID());
                }
            }
        }
        //đã import
        map.put("ok", siteSend);
        //Chưa import
        Set<Long> siteNotSend = new HashSet<>();
        for (Long siteID : allSite) {
            if (siteSend.isEmpty() || !siteSend.contains(siteID)) {
                siteNotSend.add(siteID);
            } else if (siteSend.contains(siteID)) {

            }
        }
        map.put("not", siteNotSend);
        return map;
    }

}
