package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.controller.WebController;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.IExportExcel;
import com.gms.service.LocationsService;
import com.gms.service.QrCodeService;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author vvthanh
 */
@RequestMapping("report")
public abstract class BaseController extends WebController {

    protected static final String EXTENSION_EXCEL = ".xls";
    protected static final String EXTENSION_EXCEL_X = ".xlsx";

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    protected QrCodeService qrCodeService;

    /**
     * Gửi email đính kèm báo cáo PDF
     *
     * @param to
     * @param subject
     * @param exportFileName
     * @param emailTemplateName
     * @param htmlTemplateName
     * @param variables
     * @throws Exception
     */
    protected void sendEmailAtachmentPdf(String to, String subject, String exportFileName, String emailTemplateName, String htmlTemplateName, HashMap<String, Object> variables) throws Exception {
        emailService.send(to, subject, emailTemplateName, variables, exportFileName + ".pdf", "application/pdf", buildPdf(htmlTemplateName, variables).toByteArray());
    }

    /**
     * Gửi email đính kèm báo cáo excel
     *
     * @param to
     * @param subject
     * @param emailTemplateName
     * @param variables
     * @param export
     * @throws Exception
     */
    protected void sendEmailAtachmentExcel(String to,
            String subject,
            String emailTemplateName,
            HashMap<String, Object> variables,
            IExportExcel export) throws Exception {
        emailService.send(to, subject, emailTemplateName, variables, export.getFileName(), "application/vnd.ms-excel", export.run());
    }

    /**
     * Export pdf file
     *
     * @param exportFileName
     * @param templateName
     * @param variables
     * @return
     * @throws Exception
     */
    protected ResponseEntity<InputStreamResource> exportPdf(String exportFileName, String templateName, HashMap<String, Object> variables) throws Exception {
        ByteArrayOutputStream outputStream = buildPdf(templateName, variables);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + TextUtils.removeDiacritical(exportFileName) + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    private ByteArrayOutputStream buildPdf(String templateName, HashMap<String, Object> variables) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(new ClassPathResource("static/fonts/timesnewroman/times.ttf").getURL().toString(), BaseFont.IDENTITY_H, true);
        renderer.getFontResolver().addFont(new ClassPathResource("static/fonts/timesnewroman/timesbd.ttf").getURL().toString(), BaseFont.IDENTITY_H, true);
        renderer.getFontResolver().addFont(new ClassPathResource("static/fonts/timesnewroman/timesbi.ttf").getURL().toString(), BaseFont.IDENTITY_H, true);
        renderer.getFontResolver().addFont(new ClassPathResource("static/fonts/timesnewroman/timesi.ttf").getURL().toString(), BaseFont.IDENTITY_H, true);
//        renderer.getFontResolver().addFont(new ClassPathResource("static/fonts/arial/arial.ttf").getURL().toString(), UTF_8, true);
        variables.put("print", false);
        renderer.setDocumentFromString(render(templateName, variables));
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return outputStream;
    }

    /**
     * export excel file
     *
     * @param export
     * @return
     * @throws Exception
     */
    protected ResponseEntity<InputStreamResource> exportExcel(IExportExcel export) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(export.run());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=%s", export.getFileName()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * Render html
     *
     * @param templateName
     * @param variables
     * @return
     */
    protected String render(String templateName, HashMap<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            return convertToXhtml(templateEngine.process(templateName, context));
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Convert css,js
     *
     * @param html
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }

    /**
     * Cơ quan chủ quản
     *
     * @param site
     * @return
     */
    protected String getSiteAgency(SiteEntity site) {
        String agency = site.getParentAgency();
        if (agency == null || agency.isEmpty()) {
            String provinceName = getProvinceName(site);
            if (provinceName != null && !provinceName.equals("")) {
                agency = String.format("SỞ Y TẾ %s", provinceName);
            }
        }
        return agency;
    }

    /**
     * Lấy tên tỉnh theo ID
     *
     * @param site
     * @return
     */
    protected String getProvinceName(SiteEntity site) {
        ProvinceEntity province = locationsService.findProvince(site.getProvinceID());
        return province == null ? "" : province.getName().replaceAll("Thành phố", "").replaceAll("Tỉnh", "").trim();
    }

    /**
     * Lấy tên huyện theo ID
     *
     * @param site
     * @return
     */
    protected String getDistrictName(SiteEntity site) {
        DistrictEntity district = locationsService.findDistrict(site.getDistrictID());
        return district == null ? "" : district.getName().replaceAll("Huyện", "").replaceAll("Quận", "").trim();
    }

    /**
     * Lấy tên xã theo ID
     *
     * @param site
     * @return
     */
    protected String getWardName(SiteEntity site) {
        WardEntity ward = locationsService.findWard(site.getWardID());
        return ward == null ? "" : ward.getName().replaceAll("Phường", "").trim();
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải PAC không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isPAC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getType() == SiteEntity.TYPE_PROVINCE
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.PAC.getKey());
    }

    /**
     * Kiểm tra dịch vụ prep có ở cơ sở hiện tại
     *
     * @auth pdTahng
     * @return
     */
    protected boolean isPrEP() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getServiceIDs().contains(ServiceEnum.PREP.getKey());
    }

    protected boolean isOPC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getServiceIDs().contains(ServiceEnum.OPC.getKey());
    }

    protected boolean isHTC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getServiceIDs().contains(ServiceEnum.HTC.getKey());
    }
    protected boolean isConfirm() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getServiceIDs().contains(ServiceEnum.HTC_CONFIRM.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải Trung tâm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_DISTRICT
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TTYT.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải Trạm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_WARD
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TYT.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải VAAC không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isVAAC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getType() == SiteEntity.TYPE_VAAC
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.VAAC.getKey());
    }

    protected String print(String templateName, Model model) {
        model.addAttribute("print", true);
        model.addAttribute("init", "document.onload = function() {window.print();}");
        return templateName;
    }

    /**
     * Lấy tiêu đề cho search nhiều huyện, xã
     *
     * @param pIDs
     * @param dIDs
     * @param wIDs
     * @return
     */
    protected String getTitleAddress(List<String> pIDs, List<String> dIDs, List<String> wIDs) {
        Map<String, String> provinces = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item.getName());
        }
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }

        StringBuilder str = new StringBuilder();
        Map<String, String> result = new HashMap<>();
        DistrictEntity district;
        String key;
        if (wIDs != null && wIDs.size() > 0) {
            for (WardEntity item : locationsService.findWardByIDs(new HashSet<>(wIDs))) {
                district = districts.getOrDefault(item.getDistrictID(), null);
                if (district == null) {
                    continue;
                }
                key = district.getID() + district.getProvinceID();
                if (result.get(key) == null) {
                    result.put(key, result.getOrDefault(key, district.getName() + ", " + provinces.getOrDefault(district.getProvinceID(), "/")));
                    result.put(key, item.getName() + ", " + result.get(key));
                } else {
                    result.put(key, item.getName() + " - " + result.get(key));
                }
            }
        } else if (dIDs != null && dIDs.size() > 0) {
            for (String dID : dIDs) {
                district = districts.getOrDefault(dID, null);
                if (district == null) {
                    continue;
                }
                key = district.getProvinceID();
                if (result.get(key) == null) {
                    result.put(key, result.getOrDefault(key, provinces.getOrDefault(district.getProvinceID(), "/")));
                    result.put(key, district.getName() + ", " + result.get(key));
                } else {
                    result.put(key, district.getName() + " - " + result.get(key));
                }
            }
        } else if (pIDs != null && pIDs.size() > 0) {
            for (String pID : pIDs) {
                result.put(pID, provinces.getOrDefault(pID, pID + "/"));
            }
        }
        return String.join("; ", new ArrayList<>(result.values()));
    }

    /**
     *
     * @param pID
     * @param dIDs
     * @param wIDs
     * @return
     */
    protected String getTitleAddress(String pID, List<String> dIDs, List<String> wIDs) {
        List<String> pIDs = new ArrayList<>();
        pIDs.add(pID);
        return getTitleAddress(pIDs, dIDs, wIDs);
    }
}
