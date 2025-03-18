package com.gms.entity.bean;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.service.ParameterService;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author vvthanh
 */
@Component
public class View {

    @Value("${app.security.enabled}")
    private boolean security;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ParameterService parameterService;

    private static List<Navigation> navigation;

    public boolean isSecurity() {
        return security;
    }

    private LoggedUser getCurrentUser() {
        try {
            return (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.error(e.getMessage());
        }
        return null;
    }

    private String hubPqm() {
        LoggedUser currentUser = getCurrentUser();
        String hub = currentUser.getSite().getHub();
        return StringUtils.isEmpty(hub) ? "" : hub;
    }

    /**
     * Danh sách hỗ trợ
     *
     * @return
     */
    public Map<String, String> getSupports() {
        List<ParameterEntity> items = parameterService.getUserManual();
        Map<String, String> result = new LinkedHashMap<>();
        if (items != null && !items.isEmpty()) {
            for (ParameterEntity item : items) {
                result.put(item.getValue(), item.getNote());
            }
        }
        return result;
    }

    public List<Navigation> getNavigation() {
        navigation = new LinkedList<>();
        try {
            LoggedUser currentUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean isOpc = currentUser.getSite().getProvinceID() != null
                    && !currentUser.getSite().getProvinceID().equals("")
                    && currentUser.getSite().getServiceIDs() != null && currentUser.getSite().getServiceIDs().contains(ServiceEnum.OPC.getKey());
            boolean isPAC = currentUser.getSite().getProvinceID() != null
                    && !currentUser.getSite().getProvinceID().equals("")
                    && currentUser.getSite().getServiceIDs() != null && currentUser.getSite().getServiceIDs().contains(ServiceEnum.PAC.getKey());
            boolean isConfirm = currentUser.getSite().getProvinceID() != null
                    && !currentUser.getSite().getProvinceID().equals("")
                    && currentUser.getSite().getServiceIDs() != null && currentUser.getSite().getServiceIDs().contains(ServiceEnum.HTC_CONFIRM.getKey());

            Navigation system;
            String host = httpServletRequest.getHeader("X-Forwarded-Host");
            if (host == null || (host != null && !host.contains("hub"))) {
                system = new Navigation("Tổng quan", UrlUtils.home(), "fa fa-dashboard");
                system.getChilds().add(new Navigation("Cảnh báo tình hình dịch", UrlUtils.dashboardQL(), "fa fa-circle-o"));
                system.getChilds().add(new Navigation("Tư vấn & xét nghiệm", UrlUtils.dashboardHtc(), "fa fa-circle-o"));
                system.getChilds().add(new Navigation("Quản lý người nhiễm", UrlUtils.dashboardPac(), "fa fa-circle-o"));
                system.getChilds().add(new Navigation("Điều trị ngoại trú", UrlUtils.dashboardOPC(), "fa fa-circle-o"));
                navigation.add(system);
            }

            system = new Navigation("XN tại cộng đồng", "#", "fa fa-flash");
            system.getChilds().add(new Navigation("Tổng quan", UrlUtils.laytestDashboard(), "fa fa-dashboard", true));
            system.getChilds().add(new Navigation("Khách hàng mới", UrlUtils.laytestNew(), "fa fa-plus"));
            system.getChilds().add(new Navigation("Quản lý khách hàng", UrlUtils.laytest(), "fa fa-user", true));
            system.getChilds().add(new Navigation("Sổ tư vấn & xét nghiệm", UrlUtils.laytestBook(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("BC hoạt động", UrlUtils.laytestPhuluc02(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC dự án PEPFAR", UrlUtils.laytestMER(), "fa fa-pie-chart"));
            navigation.add(system);

            system = new Navigation("XN sàng lọc tại cơ sở", "#", "fa fa-flask");
            system.getChilds().add(new Navigation("Tổng quan", UrlUtils.htcDashboard(), "fa fa-dashboard", true));
            system.getChilds().add(new Navigation("Tiếp nhận XN tại cộng đồng", UrlUtils.htcVisitLaytest(), "fa fa-flash"));
            system.getChilds().add(new Navigation("Khách hàng mới", UrlUtils.htcNew(), "fa fa-plus"));
            system.getChilds().add(new Navigation("Quản lý khách hàng", UrlUtils.htcIndex(), "fa fa-user", true));
            system.getChilds().add(new Navigation("Sổ tư vấn & xét nghiệm", UrlUtils.htcVisitBook(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Sổ xét nghiệm sàng lọc HIV", UrlUtils.htcTestBook(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Xuất danh sách quản lý", UrlUtils.htcPositiveView(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("BC TT03/2015/TT-BYT", UrlUtils.htcPhuluc02(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC TT09/2012/TT-BYT", UrlUtils.htcTT09(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC dự án PEPFAR", UrlUtils.htcMER(), "fa fa-pie-chart", true));
            system.getChilds().add(new Navigation("Quản lý chỉ tiêu", UrlUtils.htcTarget(), "fa fa-bookmark-o"));
            system.getChilds().add(new Navigation("Nhập dữ liệu Excel", UrlUtils.htcImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập dữ liệu Elog Excel", UrlUtils.htcElog(), "fa fa-file-excel-o"));
            navigation.add(system);

            system = new Navigation("Xét nghiệm khẳng định", "#", "fa fa-balance-scale");
            system.getChilds().add(new Navigation("Tiếp nhận khẳng định", UrlUtils.htcConfirmWait(), "fa fa-clock-o"));
            system.getChilds().add(new Navigation("Khách hàng mới", UrlUtils.htcConfirmNew(), "fa fa-plus"));
            system.getChilds().add(new Navigation("Quản lý khách hàng", UrlUtils.htcConfirmIndex(), "fa fa-user", true));
            system.getChilds().add(new Navigation("Xuất danh sách dương tính", UrlUtils.htcConfirmPositiveView(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Xuất danh sách nhiễm mới", UrlUtils.earlyConfirmIndex(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Báo cáo nhiễm mới", UrlUtils.pqmHtcConfirmIndex(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("Sổ xét nghiệm HIV", UrlUtils.htcConfirmBook(), "fa fa-file-excel-o", true));
            system.getChilds().add(new Navigation("Nhập dữ liệu Excel", UrlUtils.htcConfirmImport(), "fa fa-file-excel-o"));
            navigation.add(system);

            system = new Navigation("Điều trị ngoại trú", "#", "fa fa-stethoscope");
            system.getChilds().add(new Navigation("Tổng quan", "#", "fa fa-dashboard", true));
            system.getChilds().add(new Navigation("Quét mã QR", UrlUtils.opcQr(), "fa fa-qrcode", true));
            system.getChilds().add(new Navigation("Tiếp nhận từ TVXN", UrlUtils.opcArvReceiveHTC(), "fa fa-flask"));
            system.getChilds().add(new Navigation("Tiếp nhận từ OPC", UrlUtils.opcReceiveIndex(), "fa fa-ambulance"));
            system.getChilds().add(new Navigation("Thêm mới bệnh án", UrlUtils.opcArvNew(), "fa fa-plus"));
            system.getChilds().add(new Navigation("Quản lý bệnh án", UrlUtils.opcArvIndex(), "fa fa-book"));
            system.getChilds().add(new Navigation("Quản lý tải lượng virus", UrlUtils.opcViralIndex(), "fa fa-user-md"));
            system.getChilds().add(new Navigation("Quản lý tái khám", UrlUtils.opcVisitIndex(), "fa fa-hospital-o", true));
//            system.getChilds().add(new Navigation("Sổ ĐKĐT trước ARV", UrlUtils.arvPreBook(), "fa fa-file-excel-o"));
//            system.getChilds().add(new Navigation("Sổ ĐT thuốc kháng ARV", UrlUtils.opcBookArv("index"), "fa fa-file-excel-o"));
//            system.getChilds().add(new Navigation("Sổ tải lượng virus", UrlUtils.opcBookViralLoad("index"), "fa fa-file-excel-o", true));
            system.getChilds().add(new Navigation("DS bệnh nhân quản lý", UrlUtils.opcPatient(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("DS biến động điều trị", UrlUtils.opcTreatmentFluctuations(), "fa fa-file-excel-o", true));
            system.getChilds().add(new Navigation("BC tải lượng virus", isOpc ? UrlUtils.viralloadIndex() : UrlUtils.viralloadManagerIndex(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC phác đồ điều trị", UrlUtils.opcRegimenReport("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC TT03/2015/TT-BYT", UrlUtils.opcTT03("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC bảo hiểm y tế", UrlUtils.opcReportInsurance("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC thuần tập", UrlUtils.opcThuanTap("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC điều trị nhanh", UrlUtils.opcQuickTreatment("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC dự án PEPFAR", UrlUtils.opcMerReport(), "fa fa-pie-chart", true));
            system.getChilds().add(new Navigation("Nhập dữ liệu Excel", UrlUtils.opcArvImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel TLVR", UrlUtils.opcViralImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel sổ TLVR", UrlUtils.opcViralBookImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel lượt khám", UrlUtils.opcVisitImportTG(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel Phác đồ", UrlUtils.opcRegimentImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel sửa trùng", UrlUtils.arvFixDublicate(), "fa fa-file-excel-o"));

            navigation.add(system);

            system = new Navigation("Quản lý người nhiễm", "#", "fa fa-users");
            system.getChilds().add(new Navigation("Tổng quan", UrlUtils.pacDashboard(), "fa fa-dashboard", true));
            system.getChilds().add(new Navigation("Thêm mới người nhiễm", UrlUtils.pacPatientNew(), "fa fa-plus"));
            system.getChilds().add(new Navigation("Người nhiễm chưa rà soát", UrlUtils.pacNew(), "fa fa-user", true));
            system.getChilds().add(new Navigation("Người nhiễm cần rà soát", UrlUtils.pacReviewIndex(), "fa fa-spinner"));
            system.getChilds().add(new Navigation("Người nhiễm đã rà soát", UrlUtils.pacAcceptIndex(), "fa fa-check-square-o", true));
            system.getChilds().add(new Navigation("Biến động điều trị", UrlUtils.pacOpcIndex(), "fa fa-stethoscope"));
            system.getChilds().add(new Navigation("Người nhiễm ngoại tỉnh", UrlUtils.pacOutProvinceIndex(), "fa fa-user-md", true));
            system.getChilds().add(new Navigation("Người nhiễm quản lý", UrlUtils.pacPatientIndex(), "fa fa-users", true));
            system.getChilds().add(new Navigation("DS người nhiễm phát hiện", UrlUtils.pacNewExport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("DS người nhiễm tử vong", UrlUtils.pacDead(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("DS bệnh nhân AIDS", UrlUtils.pacAidsReport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("BC người nhiễm phát hiện", UrlUtils.pacDetectHivGenderReport(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC tử vong", UrlUtils.deadReport(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC AIDS", UrlUtils.aidsReport("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC theo huyện xã", UrlUtils.pacLocalIndex(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC tháng", UrlUtils.pacWardIndex(), "fa fa-pie-chart", true));
            system.getChilds().add(new Navigation("BC ngoại tỉnh", UrlUtils.pacOutProvinceGender("index"), "fa fa-pie-chart", true));
            system.getChilds().add(new Navigation("Sổ A10/YTCS", UrlUtils.pacPatientA10(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập dữ liệu Excel", UrlUtils.pacPatientImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Xuất excel HIVInfo", UrlUtils.pacExportPatientIndex(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập Excel HIV Info", UrlUtils.hivInfoImport(), "fa fa-file-excel-o"));
            navigation.add(system);

            system = new Navigation("Báo cáo quốc gia", "#", "fa fa-pie-chart");
            system.getChilds().add(new Navigation("BC TT03/2015/TT-BYT", UrlUtils.tt03Quarter("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC TT09/2012/TT-BYT", UrlUtils.tt09("index"), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("BC theo chỉ số TVXN", UrlUtils.pqmIndicatorsIndex(), "fa fa-pie-chart"));
            navigation.add(system);

            system = new Navigation("Dữ liệu PQM", "#", "fa fa-database");
            try {
                if (getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("location", null) != null && getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("location", null).equals("top")) {
                    system.getChilds().add(new Navigation(getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("label", "Chưa cấu hình tên"), UrlUtils.backendPQMDashboard(), getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("icon", "")));
                }
            } catch (Exception e) {
            }

            if (isPAC || (currentUser.getSite().getHub() != null && currentUser.getSite().getHub().equals("1"))) {
                system.getChilds().add(new Navigation("Nhập dữ liệu Excel Elog", UrlUtils.pqmImportVct(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Nhập dữ liệu nhiễm mới", UrlUtils.pqmVctImportIndex(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Nhập dữ liệu Excel PrEP", UrlUtils.pqmImportPrep(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Nhập dữ liệu Excel ARV", UrlUtils.pqmImportArvFiveSheet(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Nhập dữ liệu XN TLVR", UrlUtils.importPqmViral(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Nhập dữ liệu Sổ TLVR", UrlUtils.importPqmViralBook(), "fa fa-file-excel-o", true));

                system.getChilds().add(new Navigation("Kết quả chỉ số XN-ĐT-PrEP", UrlUtils.pqmReportIndex(), "fa fa-bar-chart", true));
                system.getChilds().add(new Navigation("Khách hàng xét nghiệm", UrlUtils.pqmVct(), "fa fa-flask"));
                system.getChilds().add(new Navigation("Khách hàng nhiễm mới", UrlUtils.pqmVctRecency(), "fa fa-flask"));
                system.getChilds().add(new Navigation("Khách hàng PrEP", UrlUtils.pqmPrep(), "fa fa-thermometer-full"));
                system.getChilds().add(new Navigation("Khách hàng điều trị", UrlUtils.pqmArv(), "fa fa-stethoscope", true));
            } else if (isConfirm) {
                system.getChilds().add(new Navigation("Nhập dữ liệu nhiễm mới", UrlUtils.pqmVctImportIndex(), "fa fa-file-excel-o"));
                system.getChilds().add(new Navigation("Kết quả chỉ số XN-ĐT-PrEP", UrlUtils.pqmReportIndex(), "fa fa-bar-chart"));
                system.getChilds().add(new Navigation("Khách hàng nhiễm mới", UrlUtils.pqmVctRecency(), "fa fa-flask"));
            }

            system.getChilds().add(new Navigation("Nhập DL kế hoạch thuốc ARV", UrlUtils.pqmDrugEstimateImports(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập DL tình hình thuốc ARV", UrlUtils.pqmDrugPlanImports(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("KQ chỉ số thuốc ARV (HMED)", UrlUtils.pqmReportDrugPlanIndex(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("KQ chỉ số thuốc ARV (HMED)", UrlUtils.pqmReportDrugPlanIndexView(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("KH cung ứng thuốc ARV", UrlUtils.pqmDrugEstimate(), "fa fa-stethoscope"));
            system.getChilds().add(new Navigation("Tình hình sử dụng thuốc ARV", UrlUtils.pqmDrugPlan(), "fa fa-stethoscope", true));

            system.getChilds().add(new Navigation("Nhập DL cung ứng, SD thuốc", UrlUtils.pqmDrugElmiseExcel(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("KQ chỉ số thuốc ARV (eLMIS)", UrlUtils.pqmReportDrugElmise(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("KQ chỉ số thuốc ARV (eLMIS)", UrlUtils.pqmReportDrugElmiseView(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("Cung ứng, SD thuốc ARV", UrlUtils.pqmDrugElmise(), "fa fa-stethoscope", true));

            system.getChilds().add(new Navigation("Nhập DL BN nhận thuốc ARV", UrlUtils.pqmImportPqmShiArt(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Nhập DL kê đơn thuốc ARV", UrlUtils.pqmImportPqmShiMmd(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("Kết quả chỉ số BHYT", UrlUtils.pqmShiIndex(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("Kết quả chỉ số BHYT", UrlUtils.pqmShiIndexView(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("Bệnh nhân nhận thuốc ARV", UrlUtils.pqmShiArt(), "fa fa-stethoscope"));
            system.getChilds().add(new Navigation("Tình hình kê đơn thuốc ARV", UrlUtils.pqmShiMmd(), "fa fa-stethoscope", true));

            system.getChilds().add(new Navigation("Nhập DL SD, tồn kho thuốc", UrlUtils.pqmDrugNewImport(), "fa fa-file-excel-o"));
            system.getChilds().add(new Navigation("KQ chỉ số thuốc ARV (HMED)", UrlUtils.pqmDrugNewData(), "fa fa-bar-chart"));
            system.getChilds().add(new Navigation("Sử dụng, tồn kho thuốc ARV", UrlUtils.pqmDrugNew(), "fa fa-stethoscope", true));
            
            system.getChilds().add(new Navigation("Xuất số liệu báo cáo", UrlUtils.pqmDrugViewre(), "fa fa-file-excel-o", true));

            system.getChilds().add(new Navigation("Cơ sở dịch vụ", UrlUtils.pqmSiteIndex(), "fa fa-sitemap"));
            system.getChilds().add(new Navigation("Cấu hình tỷ trọng", UrlUtils.pqmProtortionIndex(), "fa fa-sitemap"));
            system.getChilds().add(new Navigation("Log API", UrlUtils.pqmApiLog(), "fa fa-history"));
            system.getChilds().add(new Navigation("Lịch sử nhập liệu", UrlUtils.pqmLog(), "fa fa-history"));
            try {
                if (getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("location", null) != null && getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("location", null).equals("bottom")) {
                    system.getChilds().add(new Navigation(getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("label", "Chưa cấu hình tên"), UrlUtils.backendPQMDashboard(), getOptions().get(ParameterEntity.LINK_PQM).getOrDefault("icon", "")));
                }
            } catch (Exception e) {
            }

            navigation.add(system);

            system = new Navigation("Đơn vị", "#", "fa fa-child");
            system.getChilds().add(new Navigation("Thông tin cơ sở", UrlUtils.profileSite(), "fa fa-globe"));
            system.getChilds().add(new Navigation("Cấu hình chung", UrlUtils.siteConfig(), "fa fa-cog"));
            system.getChilds().add(new Navigation("Quản lý nhân viên", UrlUtils.profileStaff(), "fa fa-users"));
            system.getChilds().add(new Navigation("Cơ sở trực thuộc", UrlUtils.siteIndex(), "fa fa-sitemap"));
            navigation.add(system);

            system = new Navigation("Thông tin cá nhân", "#", "fa fa-user");
            system.getChilds().add(new Navigation("Cấu hình cá nhân", UrlUtils.staffConfig(), "fa fa-cog"));
            system.getChilds().add(new Navigation("Đổi thông tin cá nhân", UrlUtils.profile(), "fa fa-edit"));
            system.getChilds().add(new Navigation("Đổi mật khẩu", UrlUtils.profileChangePwd(), "fa fa-key"));
            navigation.add(system);

            system = new Navigation("Cấu hình", "#", "fa fa-cog");
            system.getChilds().add(new Navigation("Tham số", UrlUtils.parameterIndex(), "fa fa-wrench"));
            system.getChilds().add(new Navigation("Quyền hạn", UrlUtils.roleIndex(), "fa fa-users"));
            system.getChilds().add(new Navigation("Cán bộ", UrlUtils.adminStaffIndex(""), "fa fa-user"));
            system.getChilds().add(new Navigation("Lịch sử", UrlUtils.logActivity(), "fa fa-comment"));
            system.getChilds().add(new Navigation("Góp ý", UrlUtils.feedbackIndex(), "fa fa-commenting-o"));
            system.getChilds().add(new Navigation("Địa danh hành chính", UrlUtils.locationIndex(), "fa fa-globe"));
            system.getChilds().add(new Navigation("Email đã gửi", UrlUtils.mailIndex(), "fa fa-at"));
            system.getChilds().add(new Navigation("Theo dõi sử dụng", UrlUtils.pqmIndex1(), "fa fa-pie-chart"));
            system.getChilds().add(new Navigation("Cấu hình VNPT", UrlUtils.hisHIVIndex(), "fa fa-cog"));
            system.getChilds().add(new Navigation("Quản lý Token API", UrlUtils.pqmApiToken(), "fa fa-cog"));
            navigation.add(system);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navigation;
    }

    public String getURLBase() {
        return baseUrl;
    }

    public ServletUriComponentsBuilder getCurrentUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest();
    }

    public String replaceQueryParam(String param, String data) {
        try {
            URL requestURL = new URL(httpServletRequest.getRequestURL().toString());
            String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
            String base = requestURL.getProtocol() + "://" + requestURL.getHost() + port;
            String url = getCurrentUrl().replaceQueryParam(param, data).toUriString()
                    .replaceAll(base, "")
                    .replace("http://localhost", "")
                    .replaceAll("%2F", "/");
            return URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException | MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Format số
     *
     * @param number
     * @return
     */
    public String numberFormat(Object number) {
        return TextUtils.numberFormat(number);
    }

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.LINK_PQM);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(ParameterEntity.LINK_PQM, "Liên kết biểu đồ PQM");

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, parameters);
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;

    }

}
