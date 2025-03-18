package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.form.PqmVctForm;
import com.gms.entity.input.PqmVctSearch;
import com.gms.entity.validate.PqmVctValidate;
import com.gms.service.PqmVctService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_vct")
public class PqmVctController extends PqmController {

    @Autowired
    private PqmVctService vctService;
    @Autowired
    private PqmVctValidate pqmVctValidate;

    @GetMapping(value = {"/pqm-vct/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false) String tab,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "earlyDiagnose", required = false) String earlyDiagnose,
            @RequestParam(name = "confirmTimeFrom", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirmTimeTo", required = false) String confirmTimeTo,
            @RequestParam(name = "earlyHivDateFrom", required = false) String earlyHivDateFrom,
            @RequestParam(name = "earlyHivDateTo", required = false) String earlyHivDateTo,
            @RequestParam(name = "exchangeTimeFrom", required = false) String exchangeTimeFrom,
            @RequestParam(name = "exchangeTimeTo", required = false) String exchangeTimeTo,
            @RequestParam(name = "registerTherapyTimeFrom", required = false) String registerTherapyTimeFrom,
            @RequestParam(name = "registerTherapyTimeTo", required = false) String registerTherapyTimeTo,
            @RequestParam(name = "registerTherapyStatus", required = false) String registerTherapyStatus,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmVctSearch search) {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);

        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("siteHtc").entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        siteIDs.add(Long.valueOf(key));
                    }
                }
                if (siteIDs.isEmpty()) {
                    siteIDs.add(Long.valueOf("-2"));
                }
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }

        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setRegisterTherapyStatus(!StringUtils.isEmpty(registerTherapyStatus) ? registerTherapyStatus : null);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setEarlyDiagnose(StringUtils.isEmpty(earlyDiagnose) ? null : earlyDiagnose);
        search.setConfirmTimeFrom(isThisDateValid(confirmTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, confirmTimeFrom) : null);
        search.setConfirmTimeTo(isThisDateValid(confirmTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, confirmTimeTo) : null);

        search.setEarlyHivDateFrom(isThisDateValid(earlyHivDateFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateFrom) : null);
        search.setEarlyHivDateTo(isThisDateValid(earlyHivDateTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateTo) : null);
        search.setExchangeTimeFrom(isThisDateValid(exchangeTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, exchangeTimeFrom) : null);
        search.setExchangeTimeTo(isThisDateValid(exchangeTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, exchangeTimeTo) : null);
        search.setRegisterTherapyTimeFrom(isThisDateValid(registerTherapyTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, registerTherapyTimeFrom) : null);
        search.setRegisterTherapyTimeTo(isThisDateValid(registerTherapyTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, registerTherapyTimeTo) : null);

        DataPage<PqmVctEntity> dataPage = vctService.find(search);
        model.addAttribute("title", "Khách hàng xét nghiệm");
        model.addAttribute("parent_title", "Khách hàng xét nghiệm");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("tab", tab);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("options", getOptions());
        return "backend/pqm/vct";
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

    @RequestMapping(value = "/pqm-vct/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "id") Long oIDs,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        PqmVctEntity entity = vctService.findOne(oIDs);
        vctService.remove(entity.getID());
        redirectAttributes.addFlashAttribute("success", "Bản ghi đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pqmVct());
    }

    @RequestMapping(value = {"/pqm-vct/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long oIDs,
            PqmVctForm form,
            RedirectAttributes redirectAttributes) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        PqmVctEntity entity = vctService.findOne(oIDs);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", oIDs));
            return redirect(UrlUtils.pqmVct());
        }

        // Set dữ liệu hiển thị trang update
        form = setForm(entity);

        model.addAttribute("title", "Cập nhật thông tin");
        model.addAttribute("breadcrumbTitle", "Tư vấn & Xét nghiệm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pqmVct());
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("entity", entity);
        model.addAttribute("code", entity.getCode());
        model.addAttribute("name", entity.getPatientName());

        return "backend/pqm/form_vct";
    }

    @RequestMapping(value = {"/pqm-vct/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long oIDs,
            @ModelAttribute("form") @Valid PqmVctForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        PqmVctEntity entity = vctService.findOne(oIDs);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", oIDs));
            return redirect(UrlUtils.pqmVct());
        }

        model.addAttribute("title", "Cập nhật thông tin");
        model.addAttribute("breadcrumbTitle", "Tư vấn & Xét nghiệm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pqmVct());
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("entity", entity);
        model.addAttribute("code", entity.getCode());
        model.addAttribute("name", entity.getPatientName());

        pqmVctValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", " Cập nhật thông tin khách hàng không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/pqm/form_vct";
        }

        // Gán giá trị cho entity
        try {
            entity = form.getEntity(entity);
            entity = vctService.save(entity);
            redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());

            return redirect(UrlUtils.pqmVct());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pqm/form_vct";
        }
    }

    private PqmVctForm setForm(PqmVctEntity entity) {

        PqmVctForm form = new PqmVctForm();

        String formatDate = "dd/MM/yyyy";
        form.setID(entity.getID().toString());
        form.setEarlyHivDate(entity.getEarlyHivDate() != null ? TextUtils.formatDate(entity.getEarlyHivDate(), formatDate) : "");
        form.setEarlyDiagnose(entity.getEarlyDiagnose());
        form.setRegisterTherapyTime(entity.getRegisterTherapyTime() != null ? TextUtils.formatDate(entity.getRegisterTherapyTime(), formatDate) : "");
        form.setExchangeTime(entity.getExchangeTime() != null ? TextUtils.formatDate(entity.getExchangeTime(), formatDate) : "");
        form.setRegisteredTherapySite(entity.getRegisteredTherapySite());
        form.setTherapyNo(entity.getTherapyNo());
        form.setBlocked("1");
        form.setIdentityCard(entity.getIdentityCard());
        form.setConfirmTestNo(entity.getConfirmTestNo());
        form.setName(entity.getPatientName());
        form.setNote(entity.getNote());
        return form;
    }

}
