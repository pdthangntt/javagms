package com.gms.components;

import com.gms.entity.constant.PacTabEnum;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author vvthanh
 */
public class UrlUtils {

    private String baseUrl;

    public UrlUtils(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static String qrCode(String code) {
        return String.format("/api/qrcode/%s", code);
    }

    public static String error403() {
        return "/403.html";
    }

    public static String error() {
        return "/error.html";
    }

    private static String backend() {
        return "/backend";
    }

    public static String backendHome() {
        return backend() + "/index.html";
    }

    public static String backendDashboard() {
        return backend() + "/dashboard.html";
    }

    public static String backendPQMDashboard() {
        return backend() + "/pqm-link/index.html";
    }

    public static String dashboardHtc() {
        return backend() + "/dashboard-htc.html";
    }

    public static String dashboardQL() {
        return backend() + "/dashboard-ql.html";
    }

    public static String dashboardPac() {
        return backend() + "/dashboard-pac.html";
    }

    public static String dashboardOPC() {
        return backend() + "/dashboard-opc.html";
    }

    public static String importPqmViral() {
        return "/import-pqm-viral/index.html";
    }

    public static String importPqmViralBook() {
        return "/import-pqm-viral-book/index.html";
    }

    public static String backendIndicator() {
        return backend() + "/dashboard/indicator.html";
    }

    public static String backendOpcArvUpdateTreatment() {
        return backend() + "/opc-arv/update-treatment-stage.html";
    }

    public static String backend909090() {
        return backend() + "/dashboard/90-90-90.html";
    }

    public static String home() {
        return "/index.html";
    }

    public static String staff() {
        return backend() + "/staff";
    }

    public static String profile() {
        return "/profile.html";
    }

    public static String profileChangePwd() {
        return "/profile/password.html";
    }

    public static String profileSite() {
        return "/profile/site.html";
    }

    public static String profileStaff() {
        return "/profile/staff.html";
    }

    public static String profileEmail() {
        return backend() + "/mail/index.html";
    }

    public static String signin() {
        return "/signin.html";
    }

    public static String pqmIndex1() {
        return "/report/pqm/index1.html";
    }

    public static String pqmIndex() {
        return "/report/pqm/index.html";
    }

    public static String pqmLog() {
        return "/backend/pqm-log/index.html";
    }

    public static String pqmReportIndex() {
        return "/report/pqm-report/index.html";
    }

    public static String pqmIndexs() {
        return "/report/pqm-index.html";
    }

    public static String pqmReportDrugPlanIndex() {
        return "/report/pqm-drug-plan/index.html";
    }

    public static String pqmReportDrugPlanIndexView() {
        return "/report/pqm-drug-plan/index-view.html";
    }

    public static String pqmReportDrugEstimateIndex() {
        return "/report/pqm-drug-estimate/index.html";
    }

    public static String pqmReportDrugEstimateIndexView() {
        return "/report/pqm-drug-estimate/index-view.html";
    }

    public static String pqmShiIndex() {
        return "/report/pqm-shi/index.html";
    }

    public static String pqmDrugNew() {
        return "/report/drug-new/index.html";
    }
    public static String pqmDrugViewre() {
        return "/report/pqm/export.html";
    }

    public static String pqmDrugNewExcel() {
        return "/report/drug-new/excel.html";
    }
    public static String pqmDrugNewImport() {
        return "/drug-new/import.html";
    }

    public static String pqmDrugNewData() {
        return "/report/drug-new-data/index.html";
    }

    public static String pqmDrugNewDataSynthetic() {
        return "/report/drug-new-data/synthetic.html";
    }

    public static String pqmDrugNewDataExcel() {
        return "/report/drug-new-data/excel.html";
    }

    public static String pqmShiIndexView() {
        return "/report/pqm-shi/index-view.html";
    }

    public static String pqmReportDrugPlanSynthetic() {
        return "/report/pqm-drug-plan/synthetic.html";
    }

    public static String pqmReportDrugPlanExcel() {
        return "/report/pqm-drug-plan/excel.html";
    }

    public static String pqmReportExcel() {
        return "/report/pqm-report/excel.html";
    }

    public static String pqmReportDataExcel() {
        return "/report/pqm-report/excel-data.html";
    }

    public static String pqmShiDataExcel() {
        return "/report/pqm-shi/excel-data.html";
    }

    public static String pqmElmisExcelData() {
        return "/report/pqm-drug-elmise/excel-data.html";
    }

    public static String pqmEstimateExcelData() {
        return "/report/pqm-drug-estimate/excel-data.html";
    }

    public static String pqmPlanExcelData() {
        return "/report/pqm-drug-plan/excel-data.html";
    }

    public static String pqmApiHub() {
        return "/report/pqm-report/api.html";
    }

    public static String pqmReportSend() {
        return "/report/pqm-report/send.html";
    }

    public static String pqmReportSynthetics() {
        return "/report/pqm-report/synthetics.html";
    }
    public static String pqmReportSyntheticsALL() {
        return "/report/pqm-report/synthetics-all.html";
    }

    public static String pqmReportSynthetic() {
        return "/report/pqm-report/synthetic.html";
    }

    public static String pqmExcel() {
        return "/report/pqm/excel.html";
    }

    public static String pqmIndicatorsIndex() {
        return "/report/pqm/indicators-index.html";
    }

    public static String pqmIndicatorsExcel() {
        return "/report/pqm/indicators-excel.html";
    }

    public static String pqmHtcConfirmIndex() {
        return "/report/pqm/htc-confirm-index.html";
    }

    public static String pqmHtcConfirmExcel() {
        return "/report/pqm/htc-confirm-excel.html";
    }

    public static String earlyConfirmIndex() {
        return "/report/htc-confirm/early-index.html";
    }

    public static String earlyConfirmExcel() {
        return "/report/htc-confirm/early-excel.html";
    }

    public static String earlyConfirmPDF() {
        return "/report/htc-confirm/early-pdf.html";
    }

    public static String earlyConfirmEmail() {
        return "/report/htc-confirm/early-email.html";
    }

    public static String arvFixDublicate() {
        return "/fix-opc-arv/index.html";
    }

    public static String logout() {
        return "/logout.html";
    }

    public static String parameterIndex() {
        return backend() + "/parameter/index.html";
    }

    public static String parameterImport() {
        return "/import-parameter/index.html";
    }

    public static String opcRegimentImport() {
        return "/import-opc-regimen/index.html";
    }

    public static String parameterIndex(String type) {
        return String.format("%s?ptype=%s", parameterIndex(), type);
    }

    public static String parameterIndex(String type, Long parentID) {
        return String.format("%s?ptype=%s&pparent=%s", parameterIndex(), type, parentID);
    }

    public static String parameterIndex(String type, int pageIndex) {
        return String.format("%s&page=%s", parameterIndex(type), pageIndex);
    }

    public static String parameterDefine() {
        return backend() + "/parameter/define.html";
    }

    public static String parameterNew(String type) {
        return String.format("%s/parameter/new.html?ptype=%s", backend(), type);
    }

    public static String parameterUpdate(String type, String oID) {
        return String.format("%s/parameter/update.html?ptype=%s&oid=%s", backend(), type, oID);
    }

    public static String parameterRemove(String type, String oID) {
        return String.format("%s/parameter/remove.html?ptype=%s&oid=%s", backend(), type, oID);
    }

    public static String parameterSynchronize(String type) {
        return String.format("%s/parameter/synchronize-hivinfo.html?ptype=%s", backend(), type);
    }

    public static String siteIndex() {
        return String.format("%s/site/index.html", backend());
    }

    public static String opcIndex() {
        return String.format("%s/opc-arv/index.html", backend());
    }

    public static String pqmApiLog() {
        return String.format("%s/pqm-api-log/index.html", backend());
    }
    public static String pqmApiToken() {
        return String.format("%s/pqm-api-token/index.html", backend());
    }
    public static String pqmApiTokenEdit() {
        return String.format("%s/pqm-api-token/edit.html", backend());
    }
    
    public static String pqmApiTokenEdit(Long ID) {
        return String.format("%s/pqm-api-token/edit.html?oid=%s", backend(), ID);
    }

    public static String pqmApiTokenRemove(Long ID) {
        return String.format("%s/pqm-api-token/remove.html?oid=%s", backend(), ID);
    }
    

    public static String opcQr() {
        return String.format("%s/opc-qr/index.html", backend());
    }

    public static String adminStaffIndex(String tab) {
        if (StringUtils.isEmpty(tab)) {
            return String.format("%s/staff/index.html", backend());
        }
        return String.format("%s/staff/index.html?tab=%s", backend(), tab);
    }

    public static String adminStaffSwitch(String username) {
        return String.format("%s/staff/switch.html?username=%s", backend(), username);
    }

    public static String siteConfig() {
        return String.format("%s/site-config/index.html", backend());
    }

    public static String siteIndex(String tab) {
        return String.format("%s?tab=%s", siteIndex(), tab);
    }

    public static String siteIndex(String tab, Object oID) {
        return String.format("%s?tab=%s&oid=%s", siteIndex(), tab, oID);
    }

    public static String siteNew() {
        return String.format("%s/site/new.html", backend());
    }

    public static String pqmNew() {
        return String.format("%s/pqm/new.html", backend());
    }

    public static String siteUpdate(Long ID) {
        return String.format("%s/site/update.html?oid=%s", backend(), ID);
    }

    public static String requestConfirmTime(Long ID) {
        return String.format("%s/htc-confirm/wait-request.html?oid=%s", backend(), ID);
    }

    public static String siteRemove(Long ID) {
        return String.format("%s/site/remove.html?id=%s", backend(), ID);
    }

    public static String pacTrackChange(String ID) {
        return String.format("%s/pac-patient/track-change.html?id=%s", backend(), ID);
    }

    public static String siteStaff(Long siteID) {
        return String.format("%s/site/staff.html?sid=%s", backend(), siteID);
    }

    public static String siteStaff(Long siteID, String tab) {
        return String.format("%s/site/staff.html?sid=%s&tab=%s", backend(), siteID, tab);
    }

    public static String siteStaffNew(Long siteID) {
        return String.format("%s/site/staff-new.html?sid=%s", backend(), siteID);
    }

    public static String siteStaffUpdate(Long siteID, Long staffID) {
        return String.format("%s/site/staff-update.html?sid=%s&oid=%s", backend(), siteID, staffID);
    }

    public static String siteStaffChangePwd(Long siteID, Long staffID) {
        return String.format("%s/site/staff-change-password.html?sid=%s&oid=%s", backend(), siteID, staffID);
    }

    public static String siteStaffHistory(Long siteID, Long staffID) {
        return String.format("%s/site/staff-history.html?sid=%s&oid=%s", backend(), siteID, staffID);
    }

    public static String roleDefine() {
        return String.format("%s/role/define.html", backend());
    }

    public static String roleService() {
        return String.format("%s/role/service.html", backend());
    }

    public static String pacOpcConnect() {
        return "/job/pac-opc/connect.html";
    }

    public static String htcOpcConnect() {
        return "/job/opc-from-htc/connect.html";
    }

    public static String roleServiceUpdate(String serviceID) {
        return String.format("%s/role/service/update.html?oid=%s", backend(), serviceID);
    }

    public static String roleIndex() {
        return String.format("%s/role/index.html", backend());
    }

    public static String roleNew() {
        return String.format("%s/role/new.html", backend());
    }

    public static String roleUpdate(Long ID) {
        return String.format("%s/role/update.html?oid=%s", backend(), ID);
    }

    public static String roleRemove(Long ID) {
        return String.format("%s/role/remove.html?oid=%s", backend(), ID);
    }

    public static String currentSiteUpdate() {
        return "/profile/site.html";
    }

    public static String mailIndex() {
        return String.format("%s/mail/index.html", backend());
    }

    public static String mailIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/mail/index.html?tab=%s", backend(), tab);
    }

    public static String mailCompose() {
        return backend() + "/mail/compose.html";
    }

    public static String mailDetail(Long ID) {
        return String.format("%s/mail/detail.html?oid=%s", backend(), ID);
    }

    public static String locationIndex() {
        return backend() + "/location/index.html";
    }

    public static String locationSync(String type, String code) {
        return String.format("%s/location/sync.html?type=%s&code=%s", backend(), type, code);
    }

    public static String provinceDetail(String ID) {
        return String.format("%s/location/province-update.html?pid=%s", backend(), ID);
    }

    public static String provinceAdd() {
        return backend() + "/location/province-add.html";
    }

    public static String provinceUpdate() {
        return backend() + "/location/province-update.html";
    }

    public static String provinceDelete(String ID) {
        return String.format("%s/location/province-delete.html?pid=%s", backend(), ID);

    }

    public static String districtFindByProvince(String ID) {
        return String.format("%s/location/district.html?pid=%s", backend(), ID);
    }

    public static String districtDetail(String provinceID, String ditrictID) {
        return String.format("%s/location/district-update.html?pid=%s&did=%s", backend(), provinceID, ditrictID);
    }

    public static String districtAdd(String ID) {
        return String.format("%s/location/district-add.html?pid=%s", backend(), ID);
    }

    public static String districtIndex(String ID) {
        return String.format("%s/location/district.html?pid=%s", backend(), ID);
    }

    public static String districtDelete(String provinceID, String ditrictID) {
        return String.format("%s/location/district-delete.html?pid=%s&did=%s", backend(), provinceID, ditrictID);
    }

    public static String districtUpdate(String provinceID, String districtID) {
        return String.format("%s/location/district-update.html?pid=%s&did=%s", backend(), provinceID, districtID);
    }

    public static String wardFindByDistrict(String provinceID, String districtID) {
        return String.format("%s/location/ward.html?pid=%s&did=%s", backend(), provinceID, districtID);
    }

    public static String wardAdd(String provinceID, String districtID) {
        return String.format("%s/location/ward-add.html?pid=%s&did=%s", backend(), provinceID, districtID);
    }

    public static String wardDelete(String provinceID, String ditrictID, String wardID) {
        return String.format("%s/location/ward-delete.html?pid=%s&did=%s&wid=%s", backend(), provinceID, ditrictID, wardID);
    }

    public static String wardIndex(String provinceID, String districtID) {
        return String.format("%s/location/ward.html?pid=%s&did=%s", backend(), provinceID, districtID);
    }

    public static String wardUpdate(String provinceID, String ditrictID, String wardID) {
        return String.format("%s/location/ward-update.html?pid=%s&did=%s&wid=%s", backend(), provinceID, ditrictID, wardID);
    }

    public static String wardDetail(String provinceID, String ditrictID, String wardID) {
        return String.format("%s/location/ward-update.html?pid=%s&did=%s&wid=%s", backend(), provinceID, ditrictID, wardID);
    }

    public static String resend(Long emailID) {
        return String.format("%s/mail/resend.html?emailid=%s", backend(), emailID);
    }

    public static String htcDashboard() {
        return String.format("%s/htc-dashboard/index.html", backend());
    }

    public static String htcIndex() {
        return String.format("%s/htc/index.html", backend());
    }

    public static String htcElogIndex() {
        return "/report/htc-elog-grid/index.html";
    }

    public static String htcIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/htc/index.html?tab=%s", backend(), tab);
    }

    public static String htcNew() {
        return String.format("%s/htc/new.html", backend());
    }

    // Thêm mới từ tiếp nhận không chuyên
    public static String htcNewAccept(Long id) {
        return String.format("%s/htc/new.html?laytest_id=%s", backend(), id);
    }

    public static String htcUpdate(Long ID) {
        return String.format("%s/htc/update.html?oid=%s", backend(), ID);
    }

    public static String htcView(Long ID) {
        return String.format("%s/htc/view.html?oid=%s", backend(), ID);
    }

    public static String htcSavePrint(Long ID, String printable) {
        return String.format("%s/htc/update.html?oid=%s&printable=%s", backend(), ID, printable);
    }

    // In phiếu đồng ý xét nghiệm
    public static String htcSavePrintAgree(Long ID, String printable) {
        return String.format("%s/htc/index.html?oid=%s&printable=%s", backend(), ID, printable);
    }

    public static String htcExcelTemplate() {
        return String.format("%s/htc/import-template.html", backend());
    }

    public static String htcLog() {
        return "/service/htc/log.json";
    }

    public static String pacUpdateReport() {
        return "/service/pac-patient/report-update.json";
    }

    public static String laytestLog() {
        return "/service/laytest/log.json";
    }

    public static String logActivity() {
        return String.format("%s/log/index.html", backend());
    }

    public static String logVisit() {
        return String.format("%s/log/visit.html", backend());
    }

    public static String logConfirm() {
        return String.format("%s/log/confirm.html", backend());
    }

    public static String logLaytest() {
        return String.format("%s/log/laytest.html", backend());
    }

    public static String logPac() {
        return String.format("%s/log/pac.html", backend());
    }

    public static String logOpc() {
        return String.format("%s/log/opc.html", backend());
    }
    public static String logApiPqm() {
        return String.format("%s/log/pqm-api.html", backend());
    }

    public static String laytestGet() {
        return "/service/laytest/get.json";
    }

    public static String htcDataModel() {
        return "/service/htc/data-modal.json";
    }

    public static String transferGsph() {
        return "/service/htc/transfer-gsph.json";
    }

    public static String sendConfirm() {
        return "/service/htc/send-confirm.json";
    }

    public static String htcGetc() {
        return "/service/htc/get.json";
    }

    public static String htcGetUpdReceiveDate() {
        return "/service/htc/receive-date.json";
    }

    public static String confirmTransfer() {
        return "/service/htc-confirm/transfer-gsph.json";
    }

    public static String getConfirm() {
        return "/service/htc-confirm/get.json";
    }

    public static String htcLogCreate() {
        return "/service/htc/log-create.json";
    }

    public static String arvLogCreate() {
        return "/service/opc-arv/log-create.json";
    }

    public static String viralLogCreate() {
        return "/service/opc-viralload/log-creates.json";
    }

    public static String visitLogCreate() {
        return "/service/opc-visit/log-creates.json";
    }

    public static String htcConfirmLog() {
        return "/service/htc-confirm/log.json";
    }

    public static String emailDetail() {
        return "/service/mail/detail.json";
    }

    public static String htcConfirmLogCreate() {
        return "/service/htc-confirm/log-create.json";
    }

    public static String pacPatientLog() {
        return "/service/pac-patient/log.json";
    }

    public static String pacPatientLogCreate() {
        return "/service/pac-patient/log-create.json";
    }

    public static String pacNewLog() {
        return "/service/pac-new/log.json";
    }

    public static String pacNewLogCreate() {
        return "/service/pac-new/log-create.json";
    }

    public static String pacReviewLog() {
        return "/service/pac-review/log.json";
    }

    public static String pacReviewLogCreate() {
        return "/service/pac-review/log-create.json";
    }

    public static String pacAcceptLog() {
        return "/service/pac-accept/log.json";
    }

    public static String pacAcceptLogCreate() {
        return "/service/pac-accept/log-create.json";
    }

    public static String pacOpcLog() {
        return "/service/pac-opc/log.json";
    }

    public static String pacOpcLogCreate() {
        return "/service/pac-opc/log-create.json";
    }

    public static String pacOutProvinceIndex() {
        return "/backend/pac-out-province/index.html";
    }

    public static String pqmSiteIndex() {
        return "/backend/pqm-site/index.html";
    }

    public static String pqmProtortionIndex() {
        return "/backend/pqm-protortion/index.html";
    }

    public static String pqmVctImportIndex() {
        return "/pqm-vct-ealry/index.html";
    }

    public static String pacOutProvinceDelete(Long id) {
        return String.format("%s/pac-out-province/delete.html?oid=%s", backend(), id);
    }

    public static String pacOutProvinceRemove(Long id) {
        return String.format("%s/pac-out-province/remove.html?oid=%s", backend(), id);
    }

    public static String pacOutProvinceRestore(Long id) {
        return String.format("%s/pac-out-province/restore.html?oid=%s", backend(), id);
    }

    public static String pacOutProvinceMail() {
        return "/report/pac-out-province/mail.html";
    }

    public static String pacOutProvincePdf() {
        return "/report/pac-out-province/pdf.html";
    }

    public static String pacOutProvinceExcel() {
        return "/report/pac-out-province/excel.html";
    }

    public static String pacOutProvinceIndex(String tab) {
        return tab == null || tab.equals("") ? "/backend/pac-out-province/index.html" : String.format("/backend/pac-out-province/index.html?tab=%s", tab);
    }

    public static String pacOutProvinceManager(Long ID) {
        return String.format("%s/pac-out-province/manage.html?id=%s", backend(), ID);
    }

    public static String pacOutProvinceReview(Long ID) {
        return String.format("%s/pac-out-province/review.html?id=%s", backend(), ID);
    }

    // Kiểm tra trùng lắp toàn quốc
    public static String pacOutProvinceFilter(Long ID) {
        return String.format("%s/pac-out-province/filter.html?id=%s", backend(), ID);
    }

    //Xác nhận ca mới
    public static String pacOutProvinceFilterNew(Long ID) {
        return String.format("%s/pac-out-province/filter-new.html?oid=%s", backend(), ID);
    }

    public static String remove(String ID) {
        return String.format("%s/htc/remove.html?oid=%s", backend(), ID);

    }

    public static String removeLaytest(String ID) {
        return String.format("%s/htc-laytest/remove.html?oid=%s", backend(), ID);

    }

    /**
     * Xóa vĩnh viễn
     *
     * @param ID
     * @return
     */
    public static String deleteHtcLaytest(String ID) {
        return String.format("%s/htc-laytest/delete.html?oid=%s", backend(), ID);

    }

    /**
     * Khôi phục
     *
     * @param ID
     * @return
     */
    public static String restoreHtcLaytest(String ID) {
        return String.format("%s/htc-laytest/restore.html?oid=%s", backend(), ID);

    }

    public static String instructionIndex() {
        return String.format("%s/instruction/index.html", backend());
    }

    public static String instructionIndex(String uri) {
        return String.format("%s/instruction/index.html?uri=%s", backend(), uri);
    }

    /*-----------Report-----------*/
    public static String htcPhuluc02() {
        return String.format("/report/htc-tt03/index.html");
    }

    public static String htcPhuluc02(String start, String end, String services) {
        return String.format("/report/htc-tt03/index.html?from_time=%s&to_time=%s&service=%s", start, end, services);
    }

    public static String htcPhuluc02PDF() {
        return String.format("/report/htc-tt03/pdf.html");
    }

    public static String htcPhuluc02Excel() {
        return String.format("/report/htc-tt03/excel.html");
    }

    public static String htcPhuluc02Email() {
        return String.format("/report/htc-tt03/email.html");
    }

    public static String htcTT09() {
        return String.format("/report/htc-tt09/index.html");
    }

    public static String opcMERSixMonthIndex() {
        return String.format("/report/opc-mer-six-month/index.html");
    }

    public static String opcMERSixMonthExcel() {
        return String.format("/report/opc-mer-six-month/excel.html");
    }

    public static String opcMERSixMonthMail() {
        return String.format("/report/opc-mer-six-month/mail.html");
    }

    public static String treatmentRegimenIndex() {
        return String.format("/report/opc-treatment-regimen/index.html");
    }

    public static String treatmentRegimenPdf() {
        return String.format("/report/opc-treatment-regimen/pdf.html");
    }

    public static String treatmentRegimenMail() {
        return String.format("/report/opc-treatment-regimen/mail.html");
    }

    public static String treatmentRegimenExcel() {
        return String.format("/report/opc-treatment-regimen/excel.html");
    }

    public static String viralloadIndex() {
        return String.format("/report/opc-report-viralload/index.html");
    }

    public static String viralloadManagerIndex() {
        return String.format("/report/opc-report-viralload-manager/index.html");
    }

    public static String viralloadManagerPdf() {
        return String.format("/report/opc-report-viralload-manager/pdf.html");
    }

    public static String viralloadManagerExcel() {
        return String.format("/report/opc-report-viralload-manager/excel.html");
    }

    public static String viralloadManagerMail() {
        return String.format("/report/opc-report-viralload-manager/mail.html");
    }

    public static String viralloadPdf() {
        return String.format("/report/opc-report-viralload/pdf.html");
    }

    public static String viralloadMail() {
        return String.format("/report/opc-report-viralload/mail.html");
    }

    public static String viralloadExcel() {
        return String.format("/report/opc-report-viralload/excel.html");
    }

    public static String htcTT09(String service, int month, int year) {
        return String.format("/report/htc-tt09/index.html?service=%s&month=%s&year=%s", service, month, year);
    }

    public static String htcTT09PDF() {
        return String.format("/report/htc-tt09/pdf.html");
    }

    public static String htcTT09Excel() {
        return String.format("/report/htc-tt09/excel.html");
    }

    public static String htcElogExcel() {
        return String.format("/report/htc-elog-grid/excel.html");
    }

    public static String htcTT09Email() {
        return String.format("/report/htc-tt09/email.html");
    }

    //Laytest TT03
    /*-----------Report-----------*/
    public static String laytestPhuluc02() {
        return String.format("/report/laytest-tt03/index.html");
    }

    public static String laytestPhuluc02(String start, String end) {
        return String.format("/report/laytest-tt03/index.html?from_time=%s&to_time=%s", start, end);
    }

    public static String laytestPhuluc02PDF() {
        return String.format("/report/laytest-tt03/pdf.html");
    }

    public static String laytestPhuluc02Excel() {
        return String.format("/report/laytest-tt03/excel.html");
    }

    public static String laytestPhuluc02Email() {
        return String.format("/report/laytest-tt03/email.html");
    }

    /*-----------Report-----------*/
    public static String htcTarget() {
        return String.format("%s/htc-target/index.html", backend());
    }

    public static String targetUpdate(Long ID) {
        return String.format("%s/htc-target/update.html?oid=%s", backend(), ID);
    }

    public static String htcTargetRemove(Long ID) {
        return String.format("%s/htc-target/remove.html?oid=%s", backend(), ID);
    }

    /**
     * @pdThang @return
     */
    public static String targetNew() {
        return String.format("%s/htc-target/new.html", backend());
    }

    /**
     * Phiếu chuyển gửi điều trị từ cơ sở HTC
     *
     * @return
     */
    public static String htcTransferOPC() {
        return String.format("/report/htc/transfer-opc.html");
    }

    public static String htcTicketSampleSent() {
        return String.format("/report/htc/ticket-sample-sent.html");
    }

    public static String inPacAccept() {
        return String.format("/report/pac-accept/pdf.html");
    }

    public static String emailPacAccept() {
        return String.format("/report/pac-accept/email.html");
    }

    public static String excelPacAccept() {
        return String.format("/report/pac-accept/excel.html");
    }

    public static String inPacPatient() {
        return String.format("/report/pac-patient/pdf.html");
    }

    public static String emailPacPatient() {
        return String.format("/report/pac-patient/email.html");
    }

    public static String excelPacPatient() {
        return String.format("/report/pac-patient/excel.html");
    }

    public static String emailExportPacPatient() {
        return String.format("/report/pac-hivinfo-export/email.html");
    }

    public static String excelExportPacPatient() {
        return String.format("/report/pac-hivinfo-export/excel.html");
    }

    public static String inPacReview() {
        return String.format("/report/pac-review/pdf.html");
    }

    public static String emailPacReview() {
        return String.format("/report/pac-review/email.html");
    }

    public static String excelPacReview() {
        return String.format("/report/pac-review/excel.html");
    }

    public static String htcTicketResult() {
        return String.format("/report/htc/ticket-result.html");
    }

    public static String htcAgreeTest() {
        return String.format("/report/htc/agree-test.html");
    }

    public static String htcAgreeDisclose() {
        return String.format("/report/htc/agree-disclose.html");
    }

    public static String htcAgreeTestShift() {
        return String.format("/report/htc/agree-test-shift.html");
    }

    /**
     *
     * @author pdThang
     * @return
     */
    public static String htcAnswerResult() {
        return String.format("/report/htc-confirm/answer-result.html");
    }

    public static String htcAnswerResultList() {
        return String.format("/report/htc-confirm/answer-result-list.html");
    }

    /**
     * In phiếu trả kết quả không phản ứng sàng lọc
     *
     * @author TrangBN
     * @return
     */
    public static String htcVisitAnswerResult() {
        return String.format("/report/htc/answer-result.html");
    }

    /**
     *
     * @author pdThang
     * @return
     */
    public static String redirectConfirm() {
        return "/service/htc-confirm/redirect.json";

    }

    public static String urlTransfer() {
        return "/service/htc-confirm/transfer.json";

    }

    public static String urlTransferOPC() {
        return "/service/opc-arv/transfer.json";

    }

    /**
     *
     * @author DSNAnh
     * @return
     */
    public static String pacNewGet() {
        return "/service/pac-new/get.json";

    }

    public static String pacTransfer() {
        return "/service/pac-new/transfer.json";

    }

    public static String pacDoTransfer() {
        return "/service/pac-new/do-transfer.json";

    }

    /**
     *
     * @author pdThang
     * @return
     */
    public static String receivedConfirm() {
        return "/service/htc-confirm/received.json";

    }

    public static String feedbackIndex() {
        return String.format("%s/feedback/index.html", backend());
    }

    public static String hisHIVIndex() {
        return String.format("%s/vnpt-config/index.html", backend());
    }

    public static String hisHIVNew() {
        return String.format("%s/vnpt-config/new.html", backend());
    }

    public static String hisHIVUpdate(Long ID) {
        return String.format("%s/vnpt-config/update.html?id=%s", backend(), ID);
    }

    public static String hisHIVDeactive(String ID) {
        return String.format("%s/vnpt-config/deactive.html?id=%s", backend(), ID);
    }

    public static String hisHIVLog() {
        return String.format("%s/vnpt-config/log.html", backend());
    }

    public static String hisHIVLog(String ID) {
        return String.format("%s/vnpt-config/log.html?id=%s", backend(), ID);
    }

    public static String hisHIVLog(String ID, String status) {
        return String.format("%s/vnpt-config/log.html?id=%s&status=%s", backend(), ID, status);
    }

    public static String feedbackCompose() {
        return String.format("%s/feedback/compose.html", backend());
    }

    public static String feedbackDetail(Long ID) {
        return String.format("%s/feedback/update.html?oid=%s", backend(), ID);
    }

    public static String feedbackUpdate() {
        return String.format("%s/feedback/update.html", backend());
    }

    public static String feedbackRemove(Long ID) {
        return String.format("%s/feedback/remove.html?oid=%s", backend(), ID);
    }

    //-------- Danh sách dương tính ----------
    public static String htcPositivePDF() {
        return String.format("/report/htc-positive/pdf.html");
    }

    public static String htcPositiveView() {
        return String.format("/report/htc-positive/index.html");
    }

    public static String htcPositiveView(String start, String end, String services, String objects) {
        return String.format("/report/htc-positive/index.html?start=%s&end=%s&services=%s&objects=%s", start, end, services, objects);
    }

    public static String htcPositiveExcel() {
        return String.format("/report/htc-positive/excel.html");
    }

    public static String htcPositiveEmail() {
        return String.format("/report/htc-positive/email.html");
    }

    // Danh sách chuyển gửi điều trị thành công
    public static String htcTransferSuccessView() {
        return String.format("/report/htc/transfer-success.html");
    }

    public static String htcTransferSuccessView(String start, String end, String services, String objects) {
        return String.format("/report/htc/transfer-success.html?start=%s&end=%s&services=%s&objects=%s", start, end, services, objects);
    }

    public static String htcTransferSuccessPDF() {
        return String.format("/report/htc/transfer-success/pdf.html");
    }

    public static String htcTransferSuccessExcel() {
        return String.format("/report/htc/transfer-success/excel.html");
    }

    public static String htcTransferSuccessEmail() {
        return String.format("/report/htc/transfer-success/email.html");
    }

    //----------- Sổ tư vấn xét nghiệm
    public static String htcVisitBook() {
        return String.format("/report/htc-book/index.html");
    }

    public static String laytestBook() {
        return String.format("/report/laytest-book/index.html");
    }

    public static String laytestBook(String start, String end) {
        return String.format("/report/laytest-book/index.html?start=%s&end=%s", start, end);
    }

    public static String htcVisitBook(String start, String end, String services, String code) {
        return String.format("/report/htc-book/index.html?start=%s&end=%s&services=%s&code=%s", start, end, services, code);
    }

    public static String htcVisitBookExcel() {
        return String.format("/report/htc-book/excel.html");
    }

    public static String htcVisitBookEmail() {
        return String.format("/report/htc-book/email.html");
    }

    public static String htcVisitBookPdf() {
        return String.format("/report/htc-book/pdf.html");
    }

    public static String htcLaytestExcel() {
        return String.format("/report/laytest-book/excel.html");
    }

    public static String htcLaytestEmail() {
        return String.format("/report/laytest-book/email.html");
    }

    public static String htcLaytestPdf() {
        return String.format("/report/laytest-book/pdf.html");
    }

    /* Báo cáo MER */
    public static String htcMER() {
        return String.format("/report/htc-mer/index.html");
    }

    public static String htcMERExcel() {
        return String.format("/report/htc-mer/excel.html");
    }

    public static String htcMEREmail() {
        return String.format("/report/htc-mer/email.html");
    }

    public static String htcMERPdf() {
        return String.format("/report/htc-mer/pdf.html");
    }

    public static String htcMER(String services, String fromTime, String toTime, String objects) {
        return String.format("/report/htc-mer/index.html?service=%s&from_time=%s&to_time=%s&objects=%s", services, fromTime, toTime, objects);
    }

    /*----Rà soát ca bệnh ---*/
    public static String pacPatientNew() {
        return String.format("%s/pac-new/new.html", backend());
    }
    public static String pqmExport() {
        return String.format("/report/pqm/export.html" );
    }

    public static String pacPatientUpdate(Long ID) {
        return String.format("%s/pac-patient/update.html?id=%s", backend(), ID);
    }

    public static String pqmVctUpdate(Long ID) {
        return String.format("%s/pqm-vct/update.html?id=%s", backend(), ID);
    }

    public static String pqmVctDelete(Long ID) {
        return String.format("%s/pqm-vct/delete.html?id=%s", backend(), ID);
    }

    public static String pacAcceptUpdate(Long ID) {
        return String.format("%s/pac-accept/update.html?id=%s", backend(), ID);
    }

    public static String pacPatientView(String tab, Long ID) {
        return String.format("%s/pac-new/view.html?tab=%s&id=%s", backend(), tab, ID);
    }

    public static String pacPatientVaacView(Long ID) {
        return String.format("%s/pac-new/view-vaac.html?id=%s", backend(), ID);
    }

    public static String pacReviewView(Long ID) {
        return String.format("%s/pac-review/view.html?id=%s", backend(), ID);
    }

    public static String patientView(String tab, Long ID) {
        return String.format("%s/pac-patient/view.html?tab=%s&id=%s", backend(), tab, ID);
    }

    public static String pacAcceptView(Long ID) {
        return String.format("%s/pac-accept/view.html?id=%s", backend(), ID);
    }

    public static String pacPatientView(Long ID) {
        return String.format("%s/pac-patient/view.html?id=%s", backend(), ID);
    }

    public static String pacOutProvinceView(Long ID) {
        return String.format("%s/pac-out-province/view.html?id=%s", backend(), ID);
    }

    public static String pacReviewRemove(Long ID) {
        return String.format("%s/pac-review/remove.html?id=%s", backend(), ID);
    }

    public static String pacAcceptRemove(Long ID) {
        return String.format("%s/pac-accept/remove.html?id=%s", backend(), ID);
    }

    public static String pacPatientRemove(Long ID) {
        return String.format("%s/pac-patient/remove.html?id=%s", backend(), ID);
    }

    public static String pacReviewRestore(Long ID) {
        return String.format("%s/pac-review/restore.html?id=%s", backend(), ID);
    }

    public static String pacReviewDelete(Long ID) {
        return String.format("%s/pac-review/delete.html?id=%s", backend(), ID);
    }

    public static String pacAcceptRestore(Long ID) {
        return String.format("%s/pac-accept/restore.html?id=%s", backend(), ID);
    }

    public static String pacAcceptDelete(Long ID) {
        return String.format("%s/pac-accept/delete.html?id=%s", backend(), ID);
    }

    public static String htcImport() {
        return "/import-htc-visit/index.html";
    }

    public static String opcVisitImportTG() {
        return "/import-opc-visit-tg/index.html";
    }

    public static String opcViralImport() {
        return "/import-opc-viral-tg/index.html";
    }

    public static String opcViralBookImport() {
        return "/import-opc-viral-book/index.html";
    }

    public static String htcElog() {
        return "/import-elog-grid/index.html";
    }

    public static String hivInfoImport() {
        return "/import-hiv-info/index.html";
    }

    //DSNAnh
    public static String htcConfirmNew() {
        return String.format("%s/htc-confirm/new.html", backend());
    }

    //PDTHANG
    public static String htcConfirmIndex() {
        return String.format("%s/htc-confirm/index.html", backend());
    }

    //DSNAnh Đối chiếu khách hàng 14/09/2019 
    public static String htcConfirmIndexFeedback(String code) {
        return String.format("%s/htc-confirm/index.html?tab=update&sourceID=%s", backend(), code);
    }

    // TrangBN
    public static String htcConfirmUpdate(Long confirmID) {
        return String.format("%s/htc-confirm/update.html?oid=%s", backend(), confirmID);
    }

    public static String htcConfirmUpdateEarly(Long confirmID) {
        return String.format("%s/htc-confirm/update-early.html?oid=%s", backend(), confirmID);
    }

    public static String htcConfirmUpdate(String tab, Long confirmID) {
        return String.format("%s/htc-confirm/update.html?tab=%s&oid=%s", backend(), tab, confirmID);
    }

    public static String htcConfirmUpdateEarly(String tab, Long confirmID) {
        return String.format("%s/htc-confirm/update-early.html?tab=%s&oid=%s", backend(), tab, confirmID);
    }

    public static String htcConfirmIndex(String tab) {
        if (tab == null || "null".equals(tab)) {
            tab = "";
        }
        return String.format("%s/htc-confirm/index.html?tab=%s", backend(), tab);
    }

    public static String removeHtcConfirm(String ID) {
        return String.format("%s/htc-confirm/remove.html?oid=%s", backend(), ID);

    }

    public static String removeWaitConfirm(String ID) {
        return String.format("%s/htc-confirm/wait-remove.html?oid=%s", backend(), ID);

    }

    //khôi phục khách hàng khẳng định đã xóa
    public static String restoreHtcConfirm(String ID) {
        return String.format("%s/htc-confirm/restore.html?oid=%s", backend(), ID);

    }

    //xóa vĩnh viễn khách hàng khẳng định
    public static String deleteHtcConfirm(String ID) {
        return String.format("%s/htc-confirm/delete.html?oid=%s", backend(), ID);

    }

    //khôi phục khách hàng tiếp nhận khẳng định đã xóa
    public static String restoreWaitConfirm(String ID) {
        return String.format("%s/htc-confirm/wait-restore.html?oid=%s", backend(), ID);

    }

    //xóa vĩnh viễn khách hàng tiếp nhận khẳng định
    public static String deleteWaitConfirm(String ID) {
        return String.format("%s/htc-confirm/wait-delete.html?oid=%s", backend(), ID);

    }

    //khôi phục khách hàng tvxn đã xóa
    public static String restoreHtc(String ID) {
        return String.format("%s/htc/restore.html?oid=%s", backend(), ID);

    }

    //xóa vĩnh viễn khách hàng tvxn
    public static String deleteHtc(String ID) {
        return String.format("%s/htc/delete.html?oid=%s", backend(), ID);

    }

    public static String htcConfirmView(String tab, Long ID) {
        if ("null".equals(tab)) {
            tab = "";
        }
        return String.format("%s/htc-confirm/view.html?tab=%s&oid=%s", backend(), tab, ID);
    }

    // DSNAnh
    // Danh sách khách hàng xét nghiệm khẳng định dương tính
    public static String htcConfirmPositiveView() {
        return "/report/htc-confirm/positive.html";
    }

    public static String htcConfirmPositiveView(String start, String end) {
        return String.format("/report/htc-confirm/positive.html?start=%s&end=%s", start, end);
    }

    public static String htcConfirmPositivePDF() {
        return String.format("/report/htc-confirm/positive/pdf.html");
    }

    public static String htcConfirmPositiveExcel() {
        return String.format("/report/htc-confirm/positive/excel.html");
    }

    public static String htcConfirmPositiveEmail() {
        return String.format("/report/htc-confirm/positive/email.html");
    }

    //20/11/2019 
    public static String htcConfirmPositiveDistinct() {
        return "/report/htc-confirm-positive-distinct/index.html";
    }

    public static String htcConfirmPositiveDistinct(String start, String end) {
        return String.format("/report/htc-confirm-positive-distinct/index.html?start=%s&end=%s", start, end);
    }

    public static String htcConfirmPositiveDistinctPDF() {
        return String.format("/report/htc-confirm-positive-distinct/pdf.html");
    }

    public static String htcConfirmPositiveDistinctExcel() {
        return String.format("/report/htc-confirm-positive-distinct/excel.html");
    }

    public static String htcConfirmPositiveDistinctEmail() {
        return String.format("/report/htc-confirm-positive-distinct/email.html");
    }

    //import htc confirm
    public static String htcConfirmImport() {
        return "/import-htc-confirm/index.html";
    }

    public static String pacPatientImport() {
        return String.format("/import-pac-patient/index.html");
    }

    //import opc arv
    public static String opcArvImport() {
        return String.format("/import-opc-arv/index.html");
    }

    public static String htcConfirmWait() {
        return String.format("%s/htc-confirm/wait.html", backend());
    }

    public static String htcConfirmWait(String tab) {
        if ("null".equals(tab)) {
            tab = "";
        }
        return String.format("%s/htc-confirm/wait.html?tab=%s", backend(), tab);
    }

    public static String htcVisitLaytest() {
        return String.format("%s/htc-laytest/index.html", backend());
    }

    public static String laytest() {
        return String.format("%s/laytest/index.html", backend());
    }

    public static String laytestAgency() {
        return String.format("%s/laytest/agency.html", backend());
    }

    public static String laytestIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/laytest/index.html?tab=%s", backend(), tab);
    }

    public static String laytestDashboard() {
        return String.format("%s/laytest-dashboard/index.html", backend());
    }

    public static String laytestNew() {
        return String.format("%s/laytest/new.html", backend());
    }

    public static String htcLaytestIndex() {
        return String.format("%s/htc-laytest/index.html", backend());
    }

    public static String htcLaytestIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/htc-laytest/index.html?tab=%s", backend(), tab);
    }

    public static String laytestUpdate(Long ID) {
        return String.format("%s/laytest/update.html?oid=%s", backend(), ID);
    }

    public static String laytestUpdate(Long ID, String tab) {
        return String.format("%s/laytest/update.html?oid=%s&tab=%s", backend(), ID, tab);
    }

    public static String laytestNew(Long ID) {
        return String.format("%s/htc/new.html?laytest_id=%s", backend(), ID);
    }

    public static String laytestRemove(Long ID) {
        return String.format("%s/laytest/remove.html?oid=%s", backend(), ID);
    }

    public static String laytestView(Long ID) {
        return String.format("%s/laytest/view.html?oid=%s", backend(), ID);
    }

    public static String laytestView(Long ID, String tab) {
        return String.format("%s/laytest/view.html?oid=%s&tab=%s", backend(), ID, tab);
    }

    public static String laytestHtcView(Long ID) {
        return String.format("%s/htc-laytest/view.html?oid=%s", backend(), ID);
    }

    //khôi phục khách hàng laytest
    public static String restoreLaytest(String ID) {
        return String.format("%s/laytest/restore.html?oid=%s", backend(), ID);

    }

    //xóa vĩnh viễn khách hàng laytest
    public static String deleteLaytest(String ID) {
        return String.format("%s/laytest/delete.html?oid=%s", backend(), ID);

    }

    //trang chủ pacnew
    public static String pacNew() {
        return String.format("%s/pac-new/index.html", backend());

    }

    public static String pacNewVaac() {
        return String.format("%s/pac-new/vaac.html", backend());

    }

    //trang chủ pacnew
    public static String pacNew(String tab) {
        if ("null".equals(tab) || "".equals(tab) || tab == null) {
            tab = PacTabEnum.NEW_IN_PROVINCE.getKey();
        }
        return String.format("%s/pac-new/index.html?tab=%s", backend(), tab);

    }

    public static String pacNewRemove(String tab, Long ID) {
        return String.format("%s/pac-new/remove.html?tab=%s&oid=%s", backend(), tab, ID);
    }

    public static String pacNewDelete() {
        return backend() + "/pac-new/remove.html?tab=&oid=";
    }

    //khôi phục người nhiễm
    public static String restorePacNew(String ID) {
        return String.format("%s/pac-new/restore.html?oid=%s", backend(), ID);

    }

    public static String restorePacPatient(String ID) {
        return String.format("%s/pac-patient/restore.html?id=%s", backend(), ID);

    }

    //xóa vĩnh viễn người nhiễm
    public static String deletePacNew(String ID) {
        return String.format("%s/pac-new/delete.html?id=%s", backend(), ID);

    }

    //xóa vĩnh viễn người nhiễm
    public static String deletePacPatient(String ID) {
        return String.format("%s/pac-patient/delete.html?oid=%s", backend(), ID);

    }

    public static String pacNewUpdate(String tab, Long ID) {
        return String.format("%s/pac-new/update.html?tab=%s&id=%s", backend(), tab, ID);
    }

    // Cập nhật người thông tin người nhiễm và bệnh nhân sau khi kết nối
    public static String pacUpdateConnected() {
        return "/service/pac-new/update-connected.json";
    }

    // Cập nhật người thông tin người nhiễm và bệnh nhân đang điều trị nguồn ARV
    public static String pacUpdateConnectedArv() {
        return "/service/pac-new/update-connected-arv.json";
    }

    public static String staffConfig() {
        return "/profile/config.html";
    }

    /**
     *
     * @author pdThang
     * @return
     */
    public static String actionReceivedInfo() {
        return "/service/htc-confirm/received-info.json";

    }

    public static String actionReceivedInfoUpdate() {
        return "/service/htc-confirm/received-info-update.json";

    }

    //DSNAnh
    public static String pacOpcIndex() {
        return String.format("%s/pac-opc/index.html", backend());

    }

    public static String pacOpcIndex(String tab) {
        if ("".equals(tab) || tab == null) {
            tab = "not_connected";
        }
        return String.format("%s/pac-opc/index.html?tab=%s", backend(), tab);
    }

    public static String pacOpcView(String tab, Long ID) {
        return String.format("%s/pac-opc/view.html?tab=%s&id=%s", backend(), tab, ID);
    }

    public static String pacOpcRemove(String tab, Long ID) {
        return String.format("%s/pac-opc/remove.html?tab=%s&oid=%s", backend(), tab, ID);
    }

    //khôi phục người nhiễm
    public static String pacOpcRestore(String ID) {
        return String.format("%s/pac-opc/restore.html?oid=%s", backend(), ID);

    }

    //xóa vĩnh viễn người nhiễm
    public static String pacOpcDelete(String ID) {
        return String.format("%s/pac-opc/delete.html?oid=%s", backend(), ID);

    }

    public static String pacReviewIndex() {
        return String.format("%s/pac-review/index.html", backend());
    }

    public static String pacReviewIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/pac-review/index.html?tab=%s", backend(), tab);
    }

    public static String pacAcceptIndex() {
        return String.format("%s/pac-accept/index.html", backend());
    }

    public static String pacAcceptIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/pac-accept/index.html?tab=%s", backend(), tab);
    }

    public static String pacPatientIndex() {
        return String.format("%s/pac-patient/index.html", backend());
    }

    public static String pacExportPatientIndex() {
        return String.format("/report/pac-hivinfo-export/index.html");
    }

    public static String pacExportPatientIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("/report/pac-hivinfo-export/index.html?tab=%s", tab);
    }

    public static String pacPatientIndex(String tab) {
        if (tab == null) {
            tab = "";
        }
        return String.format("%s/pac-patient/index.html?tab=%s", backend(), tab);
    }

    public static String pacNewVaacUpdate() {
        return "/service/pac-new/update-vaac.json";
    }

    public static String pacUpdateReview() {
        return "/service/pac-review/update-review.json";
    }

    public static String pacUpdateReportPost() {
        return "/service/pac-patient/report-update-post.json";
    }

    public static String pacReviewGet() {
        return "/service/pac-review/get.json";
    }

    public static String pacNewVaacGet() {
        return "/service/pac-new/vaac-get.json";
    }

    public static String inPacNew() {
        return String.format("/report/pac-new/pdf.html");
    }

    public static String inPacNewVaacPdf() {
        return String.format("/report/pac-new/vaac-pdf.html");
    }

    public static String inPacNewVaacMail() {
        return String.format("/report/pac-new/vaac-mail.html");
    }

    public static String inPacNewVaacExcel() {
        return String.format("/report/pac-new/vaac-excel.html");
    }

    public static String pacNewFillter() {
        return "/service/pac-new/fillter.json";
    }

    public static String pacNewConnect() {
        return "/service/pac-new/connect.json";
    }

    public static String pacReviewFillter() {
        return "/service/pac-review/fillter.json";
    }

    public static String pacPatientFillter() {
        return "/service/pac-patient/fillter.json";
    }

    public static String pacAcceptFillter() {
        return "/service/pac-accept/fillter.json";
    }

    public static String emailPacNew() {
        return String.format("/report/pac-new/email.html");
    }

    public static String excelPacNew() {
        return String.format("/report/pac-new/excel.html");
    }

    public static String excelOpcVisit() {
        return String.format("/report/opc-visit/excel.html");
    }

    //Yêu cầu rà soát
    public static String request() {
        return "/service/pac-new/request.json";
    }

    //Chuyển gửi tỉnh khác
    public static String pacNewTransfer() {
        return "/service/pac-new/transfer-province.json";
    }

    //BC DS người nhiễm phát hiện 
    public static String pacNewExport() {
        return "/report/pac-new-export/index.html";
    }

    public static String inPacNewExport() {
        return String.format("/report/pac-new-export/pdf.html");
    }

    public static String emailPacNewExport() {
        return String.format("/report/pac-new-export/email.html");
    }

    public static String excelPacNewExport() {
        return String.format("/report/pac-new-export/excel.html");
    }

    //Rà soát lại
    public static String pacReviewCheck() {
        return "/service/pac-accept/review-check.json";
    }

    // Báo cáo huyện/xã
    public static String pacLocalIndex() {
        return String.format("/report/pac-local/index.html");
    }

    public static String pacLocalPdf() {
        return String.format("/report/pac-local/pdf.html");
    }

    public static String pacLocalExcel() {
        return String.format("/report/pac-local/excel.html");
    }

    public static String pacLocalEmail() {
        return String.format("/report/pac-local/email.html");
    }

    // Báo cáo xã
    public static String pacWardIndex() {
        return String.format("/report/pac-ward/index.html");
    }

    public static String pacWardPdf() {
        return String.format("/report/pac-ward/pdf.html");
    }

    public static String pacWardExcel() {
        return String.format("/report/pac-ward/excel.html");
    }

    public static String pacWardEmail() {
        return String.format("/report/pac-ward/email.html");
    }

    //BC DS người nhiễm tử vong
    public static String pacDead() {
        return "/report/pac-dead/index.html";
    }

    public static String pacDeadExport() {
        return "/report/pac-dead-export/index.html";
    }

    public static String inPacDead() {
        return String.format("/report/pac-dead/pdf.html");
    }

    public static String emailPacDead() {
        return String.format("/report/pac-dead/email.html");
    }

    public static String excelPacDead() {
        return String.format("/report/pac-dead/excel.html");
    }


    /* Báo cáo MER KC*/
    public static String laytestMER() {
        return String.format("/report/laytest-mer/index.html");
    }

    public static String laytestMERExcel() {
        return String.format("/report/laytest-mer/excel.html");
    }

    public static String laytestMEREmail() {
        return String.format("/report/laytest-mer/email.html");
    }

    public static String laytestMERPdf() {
        return String.format("/report/laytest-mer/pdf.html");
    }

    public static String laytestMER(String fromTime, String toTime) {
        return String.format("/report/laytest-mer/index.html?from_time=%s&to_time=%s", fromTime, toTime);
    }

    /*-----------Report-----------*/
    public static String deadReport() {
        return String.format("/report/pac-dead-local/index.html");
    }

    public static String aidsReport(String action) {
        return String.format("/report/pac-aids/" + action + ".html");
    }

    public static String deadReport(String start, String end) {
        return String.format("/report/pac-dead-local/index.html?from_time=%s&to_time=%s", start, end);
    }

    public static String deadReportPDF() {
        return String.format("/report/pac-dead-local/pdf.html");
    }

    public static String deadReportExcel() {
        return String.format("/report/pac-dead-local/excel.html");
    }

    public static String deadReportEmail() {
        return String.format("/report/pac-dead-local/email.html");
    }

    public static String earlyHivReport() {
        return String.format("/report/pac-early-hiv/index.html");
    }

    public static String earlyHivReport(String start, String end) {
        return String.format("/report/pac-early-hiv/index.html?from_time=%s&to_time=%s", start, end);
    }

    public static String earlyHivReportPDF() {
        return String.format("/report/pac-early-hiv/pdf.html");
    }

    public static String earlyHivReportExcel() {
        return String.format("/report/pac-early-hiv/excel.html");
    }

    public static String earlyHivReportEmail() {
        return String.format("/report/pac-early-hiv/email.html");
    }

    public static String htcConfirmBook() {
        return String.format("/report/confirm-book/index.html");
    }

    public static String htcConfirmBook(String start, String end, String fullname, String code, String sampleSaveCode, Long sourceSiteID, String resultID) {
        return String.format("/report/confirm-book/index.html?start=%s&end=%s&fullname=%s&code=%s&sampleSaveCode=%s&sourceSiteID=%s&resultID=%s", start, end, fullname, code, sampleSaveCode, sourceSiteID == null ? "" : String.valueOf(sourceSiteID), resultID);
    }

    public static String htcConfirmBookExcel() {
        return String.format("/report/confirm-book/excel.html");
    }

    public static String pacDetectHivResidentReport() {
        return String.format("/report/pac-detecthiv-resident/index.html");
    }

    public static String pacDetectHivResidentReportPdf() {
        return String.format("/report/pac-detecthiv-resident/pdf.html");
    }

    public static String pacDetectHivResidentReportExcel() {
        return String.format("/report/pac-detecthiv-resident/excel.html");
    }

    public static String pacDetectHivResidentReportEmail() {
        return String.format("/report/pac-detecthiv-resident/email.html");
    }

    public static String pacDetectHivTreatmentReport() {
        return String.format("/report/pac-detecthiv-treatment/index.html");
    }

    public static String pacDetectHivTreatmentReportPdf() {
        return String.format("/report/pac-detecthiv-treatment/pdf.html");
    }

    public static String pacDetectHivTreatmentReportExcel() {
        return String.format("/report/pac-detecthiv-treatment/excel.html");
    }

    public static String pacDetectHivTreatmentReportEmail() {
        return String.format("/report/pac-detecthiv-treatment/email.html");
    }

    public static String htcConfirmBookEmail() {
        return String.format("/report/confirm-book/email.html");
    }

    public static String htcConfirmBookPdf() {
        return String.format("/report/confirm-book/pdf.html");
    }

    //Sổ xét nghiệm sàng lọc
    public static String htcTestBook() {
        return "/report/htc-test-book/index.html";
    }

    public static String htcTestBook(String start, String end, String services, String objects, String code) {
        return String.format("/report/htc-test-book/index.html?start=%s&end=%s&services=%s&objects=%s&code=%s", start, end, services, objects, code);
    }

    public static String htcTestBookExcel() {
        return String.format("/report/htc-test-book/excel.html");
    }

    public static String htcTestBookEmail() {
        return String.format("/report/htc-test-book/email.html");
    }

    public static String htcTestBookPdf() {
        return String.format("/report/htc-test-book/pdf.html");
    }

    public static String arvPreBook() {
        return "/report/opc-book-pre-arv/index.html";
    }

    public static String arvPreBook(String start, String end) {
        return String.format("/report/opc-book-pre-arv/index.html?start=%s&end=%s", start, end);
    }

    public static String arvPreBookExcel() {
        return String.format("/report/opc-book-pre-arv/excel.html");
    }

    public static String arvPreBookEmail() {
        return String.format("/report/opc-book-pre-arv/email.html");
    }

    public static String arvPreBookPdf() {
        return String.format("/report/opc-book-pre-arv/pdf.html");
    }

    //Kiểm tra thông tin
    public static String pacPatientGet() {
        return "/service/pac-patient/get.json";
    }

    public static String pacPatientUpdate() {
        return "/service/pac-patient/update.json";
    }

    public static String pacPatientUpdateCancel() {
        return "/service/pac-patient/update-cancel.json";
    }

    public static String pacPatientCancel() {
        return "/service/pac-patient/cancel.json";
    }

    public static String pacDetectHivGenderReport() {
        return "/report/pac-detecthiv-gender/index.html";
    }

    public static String pacDashboard() {
        return "/backend/pac-dashboard/index.html";
    }

    public static String pacDetectHivObjectReport() {
        return "/report/pac-detecthiv-object/index.html";
    }

    public static String pacDetectHivTransmissionReport() {
        return "/report/pac-detecthiv-transmission/index.html";
    }

    public static String pacDetectHivTransmissionReportPdf() {
        return "/report/pac-detecthiv-transmission/pdf.html";
    }

    public static String pacDetectHivTransmissionReportEmail() {
        return "/report/pac-detecthiv-transmission/email.html";
    }

    public static String pacDetectHivTransmissionReportExcel() {
        return "/report/pac-detecthiv-transmission/excel.html";
    }

    public static String pacDetectHivObjectReportExcel() {
        return "/report/pac-detecthiv-object/excel.html";
    }

    public static String pacDetectHivObjectReportPdf() {
        return "/report/pac-detecthiv-object/pdf.html";
    }

    public static String pacDetectHivObjectReportEmail() {
        return "/report/pac-detecthiv-object/email.html";
    }

    public static String pacDetectHivGenderReportPdf() {
        return "/report/pac-detecthiv-gender/pdf.html";
    }

    public static String pacDetectHivGenderReportExcel() {
        return "/report/pac-detecthiv-gender/excel.html";
    }

    public static String pacDetectHivGenderReportEmail() {
        return "/report/pac-detecthiv-gender/email.html";
    }

    public static String pacDetectHivAgeReport() {
        return "/report/pac-detecthiv-age/index.html";
    }

    public static String pacDetectHivAgeReportPdf() {
        return "/report/pac-detecthiv-age/pdf.html";
    }

    public static String pacDetectHivAgeReportExcel() {
        return "/report/pac-detecthiv-age/excel.html";
    }

    public static String pacDetectHivAgeReportEmail() {
        return "/report/pac-detecthiv-age/email.html";
    }

    public static String appendix02Sent() {
        return String.format("/report/pac-review/appendix02.html");
    }

    public static String appendix02SentAccept() {
        return String.format("/report/pac-accept/appendix02.html");
    }

    public static String appendix02Patient() {
        return String.format("/report/pac-patient/appendix02.html");
    }

    public static String confirmAppendix02Sent() {
        return String.format("/report/htc-confirm/appendix02.html");
    }

    public static String confirmAppendix02SentGet() {
        return String.format("/service/htc-confirm/getAppendix02.json");
    }

    public static String htcAppendix02Sent() {
        return String.format("/report/htc/appendix02.html");
    }

    public static String htcAppendix02SentGet() {
        return String.format("/service/htc/getAppendix02.json");
    }

    //Sổ A10/YTCS
    public static String pacPatientA10() {
        return "/report/pac-patient-a10/index.html";
    }

    public static String inPacPatientA10() {
        return String.format("/report/pac-patient-a10/pdf.html");
    }

    public static String emailPacPatientA10() {
        return String.format("/report/pac-patient-a10/email.html");
    }

    public static String excelPacPatientA10() {
        return String.format("/report/pac-patient-a10/excel.html");
    }

    // Thêm mới bệnh án
    public static String opcArvIndex() {
        return String.format("%s/opc-arv/index.html", backend());
    }

    public static String opcArvIndex(Long ID, String pageRedirect) {
        return String.format("%s/opc-arv/index.html?id=%s&page_redirect=%s", backend(), ID, pageRedirect);
    }

    public static String opcArvIndex(String tab) {
        if ("null".equals(tab)) {
            tab = "all";
        }
        return String.format("%s/opc-arv/index.html?tab=%s", backend(), tab);
    }

    public static String opcReceiveIndex() {
        return String.format("%s/opc-from-arv/index.html", backend());
    }

    public static String opcReceiveIndex(Long ID, String pageRedirect) {
        return String.format("%s/opc-from-arv/index.html?id=%s&page_redirect=%s", backend(), ID, pageRedirect);
    }

    public static String opcReceiveIndex(String tab) {
        return String.format("%s/opc-from-arv/index.html?tab=%s", backend(), tab);
    }

    public static String opcViralIndex() {
        return String.format("%s/opc-viralload/index.html", backend());
    }

    public static String opcViralIndex(String tab) {
        return String.format("%s/opc-viralload/index.html?tab=%s", backend(), tab);
    }

    public static String opcVisitIndex() {
        return String.format("%s/opc-visit/index.html", backend());
    }

    public static String opcVisitIndex(String tab) {
        return String.format("%s/opc-visit/index.html?tab=%s", backend(), tab);
    }

    public static String opcReceiveView(String ID, String tab) {
        return String.format("%s/opc-from-arv/view.html?id=%s&tab=%s", backend(), ID, tab);
    }

    public static String opcReceiveRemove(String ID) {
        return String.format("%s/opc-from-arv/remove.html?id=%s", backend(), ID);
    }

    public static String opcReceiveRestore(String ID) {
        return String.format("%s/opc-from-arv/restore.html?id=%s", backend(), ID);
    }

    public static String opcArvNew() {
        return String.format("%s/opc-arv/new.html", backend());
    }

    public static String opcReceiveNew() {
        return String.format("%s/opc-from-arv/new.html", backend());
    }

    public static String opcReceiveNew(Long oldArvID, Long newArvID) {
        return String.format("%s/opc-from-arv/new.html?opc_id=%s&opc_new_id=%s", backend(), oldArvID, newArvID);
    }

    public static String opcReceiveNew(String ID) {
        return String.format("%s/opc-from-arv/new.html?opc_id=%s", backend(), ID);
    }

    public static String opcArvNew(Long ID, String printable) {
        return String.format("%s/opc-arv/new.html?id=%s&printable=%s", backend(), ID, printable);
    }

    // Hiển thị confirm box
    public static String opcArvNew(String arvID, String feedback) {
        return String.format("%s/opc-arv/new.html?id=%s&feedback=%s", backend(), arvID, feedback);
    }

    // Cập nhật bệnh án
    public static String opcArvUpdate(Long ID) {
        return String.format("%s/opc-arv/update.html?id=%s", backend(), ID);
    }

    //xem bệnh án
    public static String opcArvView(Long ID) {
        return String.format("%s/opc-arv/view.html?id=%s", backend(), ID);
    }

    public static String opcViralView(Long ID) {
        return String.format("%s/opc-viralload/view.html?id=%s", backend(), ID);
    }

    public static String opcVisitView(Long ID) {
        return String.format("%s/opc-visit/view.html?id=%s", backend(), ID);
    }

    //xóa bệnh án
    public static String opcArvRemove(Long ID) {
        return String.format("%s/opc-arv/remove.html?oid=%s", backend(), ID);
    }

    //khôi phục bệnh án đã xóa
    public static String restoreOpcArv(String ID) {
        return String.format("%s/opc-arv/restore.html?oid=%s", backend(), ID);
    }

    //Xóa vĩnh viễn bệnh án
    public static String deleteOpcArv(String ID) {
        return String.format("%s/opc-arv/delete.html?oid=%s", backend(), ID);
    }

    public static String arvLog() {
        return "/service/opc-arv/log.json";
    }

    public static String arvViralloadLog() {
        return "/service/opc-viralload/log.json";
    }

    public static String arvVisitLog() {
        return "/service/opc-visit/log.json";
    }

    // Cập nhật thông tin điều trị
    public static String opcStage(String arvID) {
        return backend() + "/opc-stage/update.html?arvid=" + arvID;
    }

    // Cập nhật thông tin điều trị
    public static String opcStageNew(String arvID) {
        return backend() + "/opc-stage/new.html?arvid=" + arvID;
    }

    public static String opcStageNew() {
        return backend() + "/opc-stage/new.html";
    }

    // Cập nhật tải lượng virus
    public static String opcViralLoad(String arvID) {
        return backend() + "/opc-viralload/update.html?arvid=" + arvID;
    }

    // Cập nhật thông tin khác
    public static String opcTest(Long ID) {
        return String.format("%s/opc-test/update.html?id=%s", backend(), ID);
    }

    public static String opcTreatment(Long ID) {
        return String.format("%s/opc-treatment/update.html?id=%s", backend(), ID);
    }

    public static String opcTestNew(Long arvID) {
        return String.format("%s/opc-test/action-new.html?arvid=%s", backend(), arvID);
    }

    public static String opcTestUpdate(Long arvID, Long id) {
        return String.format("%s/opc-test/action-update.html?arvid=%s&id=%s", backend(), arvID, id);
    }

    public static String opcTestView(Long arvID, Long id) {
        return String.format("%s/opc-test/action-view.html?arvid=%s&id=%s", backend(), arvID, id);
    }

    // Cập nhật thông tin khác
    public static String opcTest(Long ID, String tab) {
        return String.format("%s/opc-test/update.html?id=%s&tab=%s", backend(), ID, tab);
    }

    public static String opcTreatment(Long ID, String tab) {
        return String.format("%s/opc-treatment/update.html?id=%s&tab=%s", backend(), ID, tab);
    }

    public static String opcTestRemove(Long arvid, Long id) {
        return String.format("%s/opc-test/remove.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    public static String opcTestDelete(Long arvid, Long id) {
        return String.format("%s/opc-test/delete.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    public static String opcTestRestore(Long arvid, Long id) {
        return String.format("%s/opc-test/restore.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    // Cập nhật thông tin điều trị
    public static String stageUpdate(String stageID, String arvID) {
        return String.format("%s/opc-stage/stage-update.html?arvid=%s&stageid=%s", backend(), arvID, stageID);
    }

    public static String stageView(String stageID, String arvID) {
        return String.format("%s/opc-stage/stage-view.html?arvid=%s&stageid=%s&action=view", backend(), arvID, stageID);
    }

    // Cập nhật thông tin điều trị khi click nút xem/sửa/xóa
    public static String opcStage(String stageID, String arvID, String action) {
        return String.format("%s/opc-stage/update.html?arvid=%s&stageid=%s&action=%s", backend(), arvID, stageID, action);
    }

    // Cập nhật thông tin điều trị
    public static String opcTabStage(String arvid, String tab) {
        return String.format("%s/opc-stage/update.html?arvid=%s&tab=%s", backend(), arvid, tab);
    }

    // Xóa vĩnh viễn thông tin điều trị
    public static String opcDeleteStage(String stageID, String arvID) {
        return String.format("%s/opc-stage/delete.html?arvid=%s&stageid=%s", backend(), arvID, stageID);
    }

    // Xóa logic thông tin điều trị
    public static String opcRemoveStage(String stageID, String arvID) {
        return String.format("%s/opc-stage/remove.html?arvid=%s&stageid=%s", backend(), arvID, stageID);
    }

    // Xóa logic thông tin điều trị
    public static String opcRestoreStage(String stageID, String arvID) {
        return String.format("%s/opc-stage/restore.html?arvid=%s&stageid=%s", backend(), arvID, stageID);
    }

    public static String opcVisit(Long arvID, String tab, Long id, String pageRedirect) {
        return String.format("%s/opc-visit/update.html?arvid=%s&tab=%s&id=%s&page_redirect=%s", backend(), arvID, tab, id, pageRedirect);
    }

    public static String opcVisit(Long arvID, String tab) {
        return String.format("%s/opc-visit/update.html?arvid=%s&tab=%s", backend(), arvID, tab);
    }

    // Cập nhật tải lượng virus
    public static String opcViral(Long arvID, String tab) {
        return String.format("%s/opc-viralload/update.html?arvid=%s&tab=%s", backend(), arvID, tab);
    }

    public static String opcViralRemove(Long arvID, Long id) {
        return String.format("%s/opc-viralload/remove.html?arvid=%s&id=%s", backend(), arvID, id);
    }

    public static String opcViralRestore(Long arvID, Long id) {
        return String.format("%s/opc-viralload/restore.html?arvid=%s&id=%s", backend(), arvID, id);
    }

    public static String opcViralDelete(Long arvID, Long id) {
        return String.format("%s/opc-viralload/delete.html?arvid=%s&id=%s", backend(), arvID, id);
    }

    // Cập nhật thông tin tái khám
    public static String opcVisitRemove(Long arvid, Long id) {
        return String.format("%s/opc-visit/remove.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    public static String opcVisitDelete(Long arvid, Long id) {
        return String.format("%s/opc-visit/delete.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    public static String opcVisitRestore(Long arvid, Long id) {
        return String.format("%s/opc-visit/restore.html?arvid=%s&id=%s", backend(), arvid, id);
    }

    // Sửa thông tin quản lý tỉnh huyện xã
    public static String pacEditManage(Long ID) {
        return String.format("%s/pac-patient/manage-update.html?id=%s", backend(), ID);
    }

    //Tỉnh yêu cầu rà soát lại
    public static String pacCheckProvince(Long ID) {
        return String.format("%s/pac-patient/review.html?id=%s", backend(), ID);
    }

    //Huyện duyệt rà soát lại
    public static String pacReviewDistrict(Long ID) {
        return String.format("%s/pac-accept/review.html?id=%s", backend(), ID);
    }

    public static String pacPatientManageUpdate(Long ID) {
        return String.format("%s/pac-patient/manage-update.html?id=%s", backend(), ID);
    }

    public static String opcArvReceiveHTC() {
        return "/backend/opc-from-htc/index.html";
    }

    public static String opcArvReceiveHTC(String tab) {
        if ("null".equals(tab)) {
            tab = "";
        }
        return String.format("%s/opc-from-htc/index.html?tab=%s", backend(), tab);
    }

    public static String removeReceiveHTC(String ID) {
        return String.format("%s/opc-from-htc/remove.html?oid=%s", backend(), ID);
    }

    public static String restoreReceiveHTC(String ID) {
        return String.format("%s/opc-from-htc/restore.html?oid=%s", backend(), ID);
    }

    public static String viewReceiveHTC(String ID) {
        return String.format("%s/opc-from-htc/view.html?oid=%s", backend(), ID);

    }

    public static String opcArvReceiveHTC(Long ID, Long htcID, String pageRedirect) {
        return String.format("%s/opc-from-htc/index.html?id=%s&htc_id=%s&page_redirect=%s", backend(), ID, htcID, pageRedirect);
    }

    public static String opcTT03(String method) {
        return String.format("/report/opc-tt03/%s.html", method);
    }

    public static String opcReportInsurance(String method) {
        return String.format("/report/opc-insurance/%s.html", method);
    }

    //DS biến động điều trị ARV
    public static String opcTreatmentFluctuations() {
        return "/report/opc-arv-treatment/index.html";
    }

    public static String inOpcTreatmentFluctuations() {
        return String.format("/report/opc-arv-treatment/pdf.html");
    }

    public static String emailOpcTreatmentFluctuations() {
        return String.format("/report/opc-arv-treatment/email.html");
    }

    public static String excelOpcTreatmentFluctuations() {
        return String.format("/report/opc-arv-treatment/excel.html");
    }

    //DSBN quản lý
    public static String opcPatient() {
        return "/report/opc-arv-grid/index.html";
    }

    public static String inOpcPatient() {
        return String.format("/report/opc-arv-grid/pdf.html");
    }

    public static String emailOpcPatient() {
        return String.format("/report/opc-arv-grid/email.html");
    }

    public static String excelOpcPatient() {
        return String.format("/report/opc-arv-grid/excel.html");
    }

    //Sổ ARV
    public static String opcArvBook() {
        return "/report/opc-book-khang-hiv/index.html";
    }

    public static String inOpcArvBook() {
        return String.format("/report/opc-book-khang-hiv/pdf.html");
    }

    public static String emailOpcArvBook() {
        return String.format("/report/opc-book-khang-hiv/email.html");
    }

    public static String excelOpcArvBook() {
        return String.format("/report/opc-book-khang-hiv/excel.html");
    }

    public static String tt03Quarter(String action) {
        return String.format("/report/tt03-quarter/%s.html", action);
    }

    public static String tt03Year(String action) {
        return String.format("/report/tt03-year/%s.html", action);
    }

    public static String tt09(String action) {
        return String.format("/report/tt09/%s.html", action);
    }

    //DS bệnh nhân AIDS
    public static String pacAidsReport() {
        return "/report/pac-aids-report/index.html";
    }

    public static String inPacAidsReport() {
        return String.format("/report/pac-aids-report/pdf.html");
    }

    public static String emailPacAidsReport() {
        return String.format("/report/pac-aids-report/email.html");
    }

    public static String excelPacAidsReport() {
        return String.format("/report/pac-aids-report/excel.html");
    }

    public static String opcArvVisitNew(Long arvID) {
        return String.format("/backend/opc-visit/new.html?arvid=%s", arvID);
    }

    public static String opcArvVisitUpdate(Long arvID, Long ID) {
        return String.format("/backend/opc-visit/update-visit.html?arvid=%s&visitid=%s", arvID, ID);
    }

    public static String opcArvVisitView(Long arvID, Long ID) {
        return String.format("/backend/opc-visit/view-visit.html?arvid=%s&visitid=%s", arvID, ID);
    }

    //BC phác đồ
    public static String opcRegimenReport(String method) {
        return String.format("/report/opc-regimen/%s.html", method);
    }

    //BC thuần tập
    public static String opcThuanTap(String method) {
        return String.format("/report/opc-thuantap/%s.html", method);
    }

    //BC OPC MER
    public static String opcMerReport() {
        return "/report/opc-mer/index.html";
    }

    public static String opcMerReportPdf() {
        return "/report/opc-mer/pdf.html";
    }

    public static String opcMerReportExcel() {
        return "/report/opc-mer/excel.html";
    }

    public static String opcMerReportEmail() {
        return "/report/opc-mer/email.html";
    }

    public static String opcMerQuaterlyReport() {
        return "/report/opc-mer-quaterly/index.html";
    }

    public static String pacOutProvinceGender(String action) {
        return String.format("/report/pac-out-province-gender/%s.html", action);
    }

    public static String pacOutProvinceAge(String action) {
        return String.format("/report/pac-out-province-age/%s.html", action);
    }

    public static String pacOutProvinceObject(String action) {
        return String.format("/report/pac-out-province-object/%s.html", action);
    }

    public static String pacOutProvinceTransmission(String action) {
        return String.format("/report/pac-out-province-transmission/%s.html", action);
    }

    public static String pacOutProvinceResident(String action) {
        return String.format("/report/pac-out-province-resident/%s.html", action);
    }

    public static String pacOutProvinceTreatment(String action) {
        return String.format("/report/pac-out-province-treatment/%s.html", action);
    }

    public static String filterAddHistory(Long oid, Long cid) {
        return String.format("%s/pac-out-province/filter-history.html?oid=%s&cid=%s", backend(), oid, cid);
    }

    public static String opcQuickTreatment(String method) {
        return String.format("/report/opc-quick-treatment/%s.html", method);
    }

    // Sổ theo dõi TLVR HIV OPC
    public static String opcBookViralLoad(String method) {
        return String.format("/report/opc-book-viralload/%s.html", method);
    }

    // Sổ theo ARV
    public static String opcBookArv(String method) {
        return String.format("/report/opc-book/%s.html", method);
    }

    public static String opcBook() {
        return String.format("/report/opc-book/index.html");
    }

    public static String opcBookExcel() {
        return String.format("/report/opc-book/excel.html");
    }

    public static String opcBookEmail() {
        return String.format("/report/opc-book/email.html");
    }

    public static String pacDetectHivInsuranceReport() {
        return String.format("/report/pac-detecthiv-insurance/index.html");
    }

    public static String pacDetectHivInsuranceReportExcel() {
        return "/report/pac-detecthiv-insurance/excel.html";
    }

    public static String pacDetectHivInsuranceReportEmail() {
        return "/report/pac-detecthiv-insurance/email.html";
    }

    public static String pacDetectHivInsuranceReportPdf() {
        return "/report/pac-detecthiv-insurance/pdf.html";
    }

    public static String pqmVct() {
        return "/backend/pqm-vct/index.html";
    }

    public static String pqmVctRecency() {
        return "/backend/pqm-vct-recency/index.html";
    }

    public static String pqmData() {
        return "/report/pqm-data/index.html";
    }

    public static String pqmDataExcel() {
        return "/report/pqm-data/excel.html";
    }

    public static String pqmImportVct() {
        return "/pqm-vct/import.html";
    }

    public static String pqmPrep() {
        return "/backend/pqm-prep/index.html";
    }

    public static String pqmPrepUpdateInfo() {
        return "/backend/pqm-prep/update-info.html";
    }

    public static String pqmImportPrep() {
        return "/pqm-prep/import.html";
    }

    public static String pqmArv() {
        return "/backend/pqm-arv/index.html";
    }

    public static String pqmDrugPlan() {
        return "/backend/pqm-drug-plan/index.html";
    }

    public static String pqmDrugEstimate() {
        return "/backend/pqm-drug-estimate/index.html";
    }

    public static String pqmDrugElmise() {
        return "/backend/pqm-drug-elmise/index.html";
    }

    public static String pqmReportDrugElmise() {
        return "/report/pqm-drug-elmise/index.html";
    }

    public static String pqmReportDrugElmiseView() {
        return "/report/pqm-drug-elmise/index-view.html";
    }

    public static String pqmDrugElmiseExcel() {
        return "/pqm-drug-elmise/import.html";
    }

    public static String pqmImportArv1() {
        return "/pqm-arv/import1.html";
    }

    public static String pqmImportPqmShiArt() {
        return "/pqm-shi-art/import.html";
    }

    public static String pqmImportPqmShiMmd() {
        return "/pqm-shi-mmd/import.html";
    }

    public static String pqmImportArvOneSheet() {
        return "/pqm-arv/one-sheet/import.html";
    }

    public static String pqmDrugPlanImports() {
        return "/pqm-drug-plan/import.html";
    }

    public static String pqmDrugEstimateImports() {
        return "/pqm-drug-estimate/import.html";
    }

    public static String pqmImportArvFiveSheet() {
        return "/pqm-arv/five-sheet/import.html";
    }

    public static String pqmArvView(Long ID, String tab) {
        return String.format("%s/pqm-arv/view.html?oid=%s&tab=%s", backend(), ID, tab);
    }

    public static String pqmShiArt() {
        return "/backend/pqm-shi-art/index.html";
    }

    public static String pqmShiMmd() {
        return "/backend/pqm-shi-mmd/index.html";
    }
}
