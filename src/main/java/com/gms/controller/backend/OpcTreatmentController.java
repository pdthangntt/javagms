package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.OpcRevisionSearch;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdThang
 */
@Controller(value = "backend_opc_arv_treatment")
public class OpcTreatmentController extends OpcController {

    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcStageService opcStageService;

    @RequestMapping(value = {"/opc-treatment/update.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "id") Long arvID,
            @RequestParam(name = "timeChangeFrom", required = false, defaultValue = "") String timeChangeFrom,
            @RequestParam(name = "timeChangeTo", required = false, defaultValue = "") String timeChangeTo,
            @RequestParam(name = "registrationTime", required = false, defaultValue = "") String registrationTime,
            @RequestParam(name = "treatmentTime", required = false, defaultValue = "") String treatmentTime,
            @RequestParam(name = "statusOfTreatmentID", required = false, defaultValue = "") String statusOfTreatmentID,
            @RequestParam(name = "endCase", required = false, defaultValue = "") String endCase,
            @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(name = "tranferFromTime", required = false, defaultValue = "") String tranferFromTime,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }
        OpcArvEntity arv = arvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }
        } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcIndex());
        }

        OpcRevisionSearch search = new OpcRevisionSearch();
        //Phân trang
        search.setPageIndex(page);
        search.setPageSize(size);
        //Điều kiện lọc
        search.setSiteID(arv.getSiteID());
        search.setArvID(arv.getID());

        search.setTimeChangeFrom(TextUtils.validDate(timeChangeFrom) ? timeChangeFrom : null);
        search.setTimeChangeTo(TextUtils.validDate(timeChangeTo) ? timeChangeTo : null);
        search.setRegistrationTime(TextUtils.validDate(registrationTime) ? registrationTime : null);
        search.setTreatmentTime(TextUtils.validDate(treatmentTime) ? treatmentTime : null);

        search.setStatusOfTreatmentID(statusOfTreatmentID);

        search.setEndCase(endCase);
        search.setEndTime(TextUtils.validDate(endTime) ? endTime : null);
        search.setTranferFromTime(TextUtils.validDate(tranferFromTime) ? tranferFromTime : null);

        DataPage<OpcArvRevisionEntity> dataPage = arvService.findArvRevision(search);

        HashMap<String, String> stagePatients = new HashMap<>();
        List<OpcStageEntity> stagePatient = opcStageService.findByPatientID(arv.getPatientID());
        if (stagePatient != null && !stagePatient.isEmpty()) {
            for (OpcStageEntity stage : stagePatient) {
                stagePatients.put(String.valueOf(stage.getID()), stage.getName());
            }
        }

        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("options", getOptions(true, arv));
        model.addAttribute("arvID", arvID);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("stagePatients", stagePatients);
        model.addAttribute("arv", arv);

        return "backend/opc_arv/trearment";
    }

}
