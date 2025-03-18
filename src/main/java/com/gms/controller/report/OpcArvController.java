package com.gms.controller.report;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcArvNewForm;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcVisitService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author DSNAnh
 */
@Controller(value = "opc_arv_new")
public class OpcArvController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private LocationsService locationService;
    @Autowired
    HtcVisitService htcService;
    @Autowired
    OpcVisitService opcVisitService;

    /**
     * Cần sửa lại chỉ dùng để in phiếu chuyển gửi
     *
     * @author DSNAnh
     * @param model
     * @param ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/opc-arv/send-print.html"})
    public String actionPrint(Model model,
            @RequestParam(name = "arv_id", defaultValue = "") Long ID) throws Exception {
//        HashMap<String, Object> context = new HashMap<>();
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(ID);
        OpcArvNewForm form = new OpcArvNewForm(arvEntity);
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        String transferSiteName = StringUtils.isEmpty(form.getTransferSiteID()) ? "" : ("-1".equals(form.getTransferSiteID()) ? form.getTransferSiteName() : siteService.findOne(Long.parseLong(form.getTransferSiteID())).getName());
        model.addAttribute("transferSiteName", transferSiteName);
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("arv", arvEntity);
        model.addAttribute("baseUrl", view.getURLBase());
        model.addAttribute("config", parameterService.getSiteConfig(currentUser.getSite().getID()));
//        return exportPdf("PhieuChuyenGuiDieuTri", "report/opc/sample-transfer.html", context);
        return print("report/opc/sample-transfer.html", model);
    }

    /**
     * In phiếu phản hồi
     *
     * @param htcID
     * @param fromArvID
     * @param arvID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/opc-from-arv/print-grid.html", "/opc-from-htc/print-grid.html", "/opc-arv/print.html"})
    public ResponseEntity<InputStreamResource> actionPrintFeedback(
            @RequestParam(name = "htc_id", defaultValue = "0") Long htcID,
            @RequestParam(name = "from_arv_id", defaultValue = "0") Long fromArvID,
            @RequestParam(name = "arv_id", defaultValue = "0") Long arvID) throws Exception {
        HashMap<String, Object> context = new HashMap<>();
        LoggedUser currentUser = getCurrentUser();

        OpcArvEntity arv = null;
        if (!arvID.equals(Long.valueOf(0))) {
            arv = opcArvService.findOne(arvID);
        } else if (!fromArvID.equals(Long.valueOf(0))) {
            arv = opcArvService.findBySourceID(fromArvID, ServiceEnum.OPC.getKey());
        } else if (!htcID.equals(Long.valueOf(0))) {
            arv = opcArvService.findBySourceID(htcID, ServiceEnum.HTC.getKey());
        }

        if (arv == null) {
            throw new Exception("Không tìm thấy bệnh án");
        }
        String sourceSiteName = arv.getSourceSiteID() == null || arv.getSourceSiteID() == 0L ? "" : (arv.getSourceSiteID() == -1L ? arv.getSourceArvSiteName() : siteService.findOne(arv.getSourceSiteID()).getName());
        context.put("sourceSiteName", sourceSiteName);
        context.put("arv", arv);
        context.put("siteAgency", getSiteAgency(currentUser.getSite()));
        context.put("siteName", currentUser.getSite().getName());
        context.put("options", getOptions());
        return exportPdf("PhieuPhanHoiTiepNhanDieuTri", "report/opc/feedback.html", context);
    }

    @GetMapping(value = {"/opc-arv/qr.html"})
    public String actionQR(Model model,
            @RequestParam(name = "arv_id", defaultValue = "") String IDs) throws Exception {
        List<Long> arvIDs = new ArrayList<>();
        for (String ID : IDs.split(",", -1)) {
            if (StringUtils.isEmpty(ID)) {
                continue;
            }
            arvIDs.add(Long.parseLong(ID));
        }
        List<OpcArvEntity> arvs = opcArvService.findAllByIds(arvIDs);

        model.addAttribute("options", getOptions());
        model.addAttribute("arvs", arvs);
        model.addAttribute("siteName", getCurrentUser().getSite().getShortName());
        model.addAttribute("baseUrl", view.getURLBase());
        return print("report/opc/qr.html", model);
    }

    @GetMapping(value = {"/opc-arv/transfer-lao.html"})
    public ResponseEntity<InputStreamResource> actionPrintLao(@RequestParam(name = "arv_id", defaultValue = "") Long ID) throws Exception {
        HashMap<String, Object> context = new HashMap<>();
        LoggedUser currentUser = getCurrentUser();
        SiteEntity site = currentUser.getSite();
        OpcArvEntity arvEntity = opcArvService.findOne(ID);
        String address = StringUtils.isNotEmpty(site.getAddress()) ? String.format("%s, ", site.getAddress().trim()) : "";
        String location = String.format("%s%s, %s, %s", address, locationService.findWard(site.getWardID()).getName(), locationService.findDistrict(site.getDistrictID()).getName(), locationService.findProvince(site.getProvinceID()).getName());
        context.put("site", site);
        context.put("address", site.getAddress().contains(",") ? site.getAddress() : location);
        context.put("options", getOptions());
        context.put("arv", arvEntity);
        context.put("baseUrl", view.getURLBase());
        context.put("config", parameterService.getSiteConfig(currentUser.getSite().getID()));
        return exportPdf("PhieuChuyenGui", "report/opc/lao-transfer.html", context);
    }
    
    /**
     * DSNAnh - Phieu chuyen tuyen bao hiem
     * @param ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/opc-arv/transit-temp.html"})
    public ResponseEntity<InputStreamResource> actionTransitTemp(@RequestParam(name = "arv_id", defaultValue = "") Long ID) throws Exception {
        HashMap<String, Object> context = new HashMap<>();
        LoggedUser currentUser = getCurrentUser();
        SiteEntity site = currentUser.getSite();
        OpcArvEntity arvEntity = opcArvService.findOne(ID);
        List<OpcVisitEntity> list = opcVisitService.findByArvID(ID, false);
        SiteEntity confirmSite = arvEntity.getPatient().getConfirmSiteID() == null ? null : siteService.findOne(arvEntity.getPatient().getConfirmSiteID());
        SiteEntity transferSite = arvEntity.getTransferSiteID() == null ? null : siteService.findOne(arvEntity.getTransferSiteID());
        OpcVisitEntity lastVisit = list == null || list.isEmpty() ? null : list.get(0);
        context.put("transferSiteName", transferSite == null ? arvEntity.getTransferSiteName() : transferSite.getName());
        context.put("confirmSiteName", confirmSite == null ? arvEntity.getPatient().getConfirmSiteName(): confirmSite.getName());
        context.put("lastVisit", lastVisit);
        context.put("site", site);
        context.put("options", getOptions());
        context.put("arv", arvEntity);
        return exportPdf("PhieuChuyenTuyen", "report/opc/transit-temp.html", context);
    }
}
