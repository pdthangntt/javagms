package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.Mer04QuarterlyForm;
import com.gms.entity.form.opc_arv.OpcMerForm;
import com.gms.entity.form.opc_arv.OpcMerFormAges;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class MerQuarterlyExcel extends BaseView implements IExportExcel  {
    private OpcMerForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public MerQuarterlyExcel(OpcMerForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "MerQuarterly";
    }

    public MerQuarterlyExcel(OpcMerForm data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();

        getCurrentSheet().getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        workbook.write(out);
        return out.toByteArray();
    }

    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
    }

    /**
     * Tạo header cho excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(600));

        setMerge(1, 1, 0, 8, false);

        createRow();
        createTitleRow("Báo cáo PEPFAR quý".toUpperCase());
        createRow();

        int cellIndexFilter = 1;
        if(StringUtils.isNotEmpty(form.getTreatmentSiteName())){
            createFilterRow("Cơ sở điều trị", form.getTreatmentSiteName(), cellIndexFilter);
        }
        createFilterRow("Kỳ báo cáo", String.format("Quý %s năm %s", Integer.parseInt(StringUtils.isEmpty(form.getQuarter()) ? "0" : form.getQuarter()) + 1, form.getYear()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        getCurrentSheet();
        createRow();

        int rowNumber = getLastRowNumber();
        setMerge(rowNumber, rowNumber, 1, 8, false);
        setMerge(rowNumber + 1, rowNumber + 2, 0, 0, false);
        setMerge(rowNumber + 1, rowNumber + 2, 1, 1, false);
        setMerge(rowNumber + 1, rowNumber + 1, 2, 4, false);
        setMerge(rowNumber + 1, rowNumber + 1, 5, 7, false);
        setMerge(rowNumber + 1, rowNumber + 2, 8, 8, false);
//        setMerge(rowNumber + 7, rowNumber + 7, 2, 7, false);
        setMerge(rowNumber + 8, rowNumber + 8, 2, 7, false);
        setMerge(rowNumber + 9, rowNumber + 9, 2, 7, false);
        
        setMerge(rowNumber + 22, rowNumber + 23, 0, 0, false);
        setMerge(rowNumber + 22, rowNumber + 23, 1, 1, false);
        setMerge(rowNumber + 22, rowNumber + 22, 2, 4, false);
        setMerge(rowNumber + 22, rowNumber + 22, 5, 7, false);
        setMerge(rowNumber + 22, rowNumber + 23, 8, 8, false);
        
        setMerge(rowNumber + 10, rowNumber + 10, 2, 7, false);
        setMerge(rowNumber + 11, rowNumber + 11, 2, 7, false);
        setMerge(rowNumber + 12, rowNumber + 12, 2, 7, false);
        setMerge(rowNumber + 13, rowNumber + 13, 2, 7, false);
        setMerge(rowNumber + 14, rowNumber + 14, 2, 7, false);
        setMerge(rowNumber + 15, rowNumber + 15, 2, 7, false);
        
        setMerge(rowNumber + 16, rowNumber + 16, 2, 7, false);
        setMerge(rowNumber + 17, rowNumber + 17, 2, 7, false);
        setMerge(rowNumber + 18, rowNumber + 18, 2, 7, false);
        setMerge(rowNumber + 19, rowNumber + 19, 2, 7, false);
        setMerge(rowNumber + 20, rowNumber + 20, 2, 7, false);
        setMerge(rowNumber + 21, rowNumber + 21, 2, 7, false);
        
        setMerge(rowNumber + 32, rowNumber + 32, 2, 7, false);
        setMerge(rowNumber + 33, rowNumber + 33, 2, 7, false);
        setMerge(rowNumber + 34, rowNumber + 34, 2, 7, false);
        setMerge(rowNumber + 35, rowNumber + 35, 2, 7, false);
        setMerge(rowNumber + 36, rowNumber + 36, 2, 7, false);
        setMerge(rowNumber + 37, rowNumber + 37, 2, 7, false);
        setMerge(rowNumber + 38, rowNumber + 38, 2, 7, false);
        
        createTableRowBold("B", "CHỈ SỐ BÁO CÁO HÀNG QUÝ", "", "", "", "", "", "", "");
        createTableHeaderRow("", "", "Nam", "", "", "Nữ", "", "", "Tổng");
        createTableHeaderRow("", "", "< 1", "1 - 14", "≥15", "< 1", "1 - 14", "≥15", "Tổng");
        
        createTableRowBold("1", "Số người bệnh đang được điều trị ARV tính đến cuối quý báo cáo, không tính số người bệnh không khám trên 28 ngày, trong đó:", form.getTable01Quarterly().get("28days").get("1").getUnderOneAge(), form.getTable01Quarterly().get("28days").get("1").getOneToFourteen(), form.getTable01Quarterly().get("28days").get("1").getOverFifteen(), form.getTable01Quarterly().get("28days").get("2").getUnderOneAge(), form.getTable01Quarterly().get("28days").get("2").getOneToFourteen(), form.getTable01Quarterly().get("28days").get("2").getOverFifteen(), form.getTable01Quarterly().get("28days").get("total").getTotal());
        createTableRow("", "1.1. Phân nhóm theo số tháng thuốc ARV phát cho người bệnh", form.getTable01Quarterly().get("monthArv").get("1").getUnderOneAge(), form.getTable01Quarterly().get("monthArv").get("1").getOneToFourteen(), form.getTable01Quarterly().get("monthArv").get("1").getOverFifteen(), form.getTable01Quarterly().get("monthArv").get("2").getUnderOneAge(), form.getTable01Quarterly().get("monthArv").get("2").getOneToFourteen(), form.getTable01Quarterly().get("monthArv").get("2").getOverFifteen(), form.getTable01Quarterly().get("monthArv").get("total").getTotal());
        createTableRow("", "1.1.1. Số NB được cấp thuốc <3 tháng", form.getTable01Quarterly().get("underThreeMonth").get("1").getUnderOneAge(), form.getTable01Quarterly().get("underThreeMonth").get("1").getOneToFourteen(), form.getTable01Quarterly().get("underThreeMonth").get("1").getOverFifteen(), form.getTable01Quarterly().get("underThreeMonth").get("2").getUnderOneAge(), form.getTable01Quarterly().get("underThreeMonth").get("2").getOneToFourteen(), form.getTable01Quarterly().get("underThreeMonth").get("2").getOverFifteen(), form.getTable01Quarterly().get("underThreeMonth").get("total").getTotal());
        createTableRow("", "1.1.2. Số NB được cấp thuốc từ 3 đến <6 tháng", form.getTable01Quarterly().get("threeToSixMonth").get("1").getUnderOneAge(), form.getTable01Quarterly().get("threeToSixMonth").get("1").getOneToFourteen(), form.getTable01Quarterly().get("threeToSixMonth").get("1").getOverFifteen(), form.getTable01Quarterly().get("threeToSixMonth").get("2").getUnderOneAge(), form.getTable01Quarterly().get("threeToSixMonth").get("2").getOneToFourteen(), form.getTable01Quarterly().get("threeToSixMonth").get("2").getOverFifteen(), form.getTable01Quarterly().get("threeToSixMonth").get("total").getTotal());
        createTableRow("", "1.1.3. Số NB được cấp thuốc từ 6 tháng trở lên", form.getTable01Quarterly().get("fromSixMonth").get("1").getUnderOneAge(), form.getTable01Quarterly().get("fromSixMonth").get("1").getOneToFourteen(), form.getTable01Quarterly().get("fromSixMonth").get("1").getOverFifteen(), form.getTable01Quarterly().get("fromSixMonth").get("2").getUnderOneAge(), form.getTable01Quarterly().get("fromSixMonth").get("2").getOneToFourteen(), form.getTable01Quarterly().get("fromSixMonth").get("2").getOverFifteen(), form.getTable01Quarterly().get("fromSixMonth").get("total").getTotal());
        createTableRow("", "1.2. Phân nhóm theo đối tượng nguy cơ cao", form.getTable0102Quarterly().getTong(), "", "", "", "", "", form.getTable0102Quarterly().getTong());
        createTableRow("", "1.2.1. NCMT", form.getTable0102Quarterly().getNcmt(), "", "", "", "", "", form.getTable0102Quarterly().getNcmt());
        createTableRow("", "1.2.2. MSM", form.getTable0102Quarterly().getMsm(), "", "", "", "", "", form.getTable0102Quarterly().getMsm());
        createTableRow("", "1.2.3. Chuyển giới", form.getTable0102Quarterly().getChuyengioi(), "", "", "", "", "", form.getTable0102Quarterly().getChuyengioi());
        createTableRow("", "1.2.4. PNMD", form.getTable0102Quarterly().getPnmd(), "", "", "", "", "", form.getTable0102Quarterly().getPnmd());
        createTableRow("", "1.2.5. Học viên cai nghiện", form.getTable0102Quarterly().getPhamnhan(), "", "", "", "", "", form.getTable0102Quarterly().getPhamnhan());
        
        createTableRowBold("2", "Số người bệnh bắt đầu được điều trị ARV tại cơ sở trong quý báo cáo", form.getTable02Quarterly().getTong(), "", "", "", "", "", form.getTable02Quarterly().getTong());
        createTableRow("", "2.1. Phân nhóm theo đối tượng nguy cơ cao", form.getTable02Quarterly().getTong(), "", "", "", "", "", form.getTable02Quarterly().getTong());
        createTableRow("", "2.1.1. NCMT", form.getTable02Quarterly().getNcmt(), "", "", "", "", "", form.getTable02Quarterly().getNcmt());
        createTableRow("", "2.1.2. MSM", form.getTable02Quarterly().getMsm(), "", "", "", "", "", form.getTable02Quarterly().getMsm());
        createTableRow("", "2.1.3. Chuyển giới", form.getTable02Quarterly().getChuyengioi(), "", "", "", "", "", form.getTable02Quarterly().getChuyengioi());
        createTableRow("", "2.1.4. PNMD", form.getTable02Quarterly().getPnmd(), "", "", "", "", "",form.getTable02Quarterly().getPnmd());
        createTableRow("", "2.1.5. Học viên cai nghiện", form.getTable02Quarterly().getPhamnhan(), "", "", "", "", "", form.getTable02Quarterly().getPhamnhan());
        createTableRow("", "2.2. Đang nuôi con bằng sữa mẹ tại thời điểm bắt đầu điều trị ARV", form.getTable02Quarterly().getHasChild(), "", "", "", "", "", form.getTable02Quarterly().getHasChild());
        
        Map<String, HashMap<String, OpcMerFormAges>> table03Quarterly = form.getTable03Quarterly();
        createTableHeaderRow("", "", "Nam", "", "", "Nữ", "", "", "Tổng");
        createTableHeaderRow("", "", "< 1", "1 - 14", "≥15", "< 1", "1 - 14", "≥15", "Tổng");
        createTableRowBold("3", "Số người bệnh không khám trên 28 ngày trong kỳ báo cáo, trong đó", table03Quarterly.get("tong").get("1").getUnderOneAge(),table03Quarterly.get("tong").get("1").getOneToFourteen(), table03Quarterly.get("tong").get("1").getOverFifteen(), table03Quarterly.get("tong").get("2").getUnderOneAge(), table03Quarterly.get("tong").get("2").getOneToFourteen(), table03Quarterly.get("tong").get("2").getOverFifteen(), table03Quarterly.get("tong").get("total").getTotal());
        createTableRow("", "3.1. Số người bệnh tử vong",  table03Quarterly.get("tuvong").get("1").getUnderOneAge(),table03Quarterly.get("tuvong").get("1").getOneToFourteen(), table03Quarterly.get("tuvong").get("1").getOverFifteen(), table03Quarterly.get("tuvong").get("2").getUnderOneAge(), table03Quarterly.get("tuvong").get("2").getOneToFourteen(), table03Quarterly.get("tuvong").get("2").getOverFifteen(), table03Quarterly.get("tuvong").get("total").getTotal());
        createTableRow("", "3.2. Số người bệnh mất dấu (cơ sở không theo dấu được), trong đó:", table03Quarterly.get("matdau").get("1").getUnderOneAge(),table03Quarterly.get("matdau").get("1").getOneToFourteen(), table03Quarterly.get("matdau").get("1").getOverFifteen(), table03Quarterly.get("matdau").get("2").getUnderOneAge(), table03Quarterly.get("matdau").get("2").getOneToFourteen(), table03Quarterly.get("matdau").get("2").getOverFifteen(), table03Quarterly.get("matdau").get("total").getTotal());
        createTableRow("", "3.2.1. Số NB điều trị < 3 tháng", table03Quarterly.get("duoi3thang").get("1").getUnderOneAge(),table03Quarterly.get("duoi3thang").get("1").getOneToFourteen(), table03Quarterly.get("duoi3thang").get("1").getOverFifteen(), table03Quarterly.get("duoi3thang").get("2").getUnderOneAge(), table03Quarterly.get("duoi3thang").get("2").getOneToFourteen(), table03Quarterly.get("duoi3thang").get("2").getOverFifteen(), table03Quarterly.get("duoi3thang").get("total").getTotal());
        createTableRow("", "3.2.2. Số NB điều trị từ 3 tháng trở lên", table03Quarterly.get("tren3thang").get("1").getUnderOneAge(),table03Quarterly.get("tren3thang").get("1").getOneToFourteen(), table03Quarterly.get("tren3thang").get("1").getOverFifteen(), table03Quarterly.get("tren3thang").get("2").getUnderOneAge(), table03Quarterly.get("tren3thang").get("2").getOneToFourteen(), table03Quarterly.get("tren3thang").get("2").getOverFifteen(), table03Quarterly.get("tren3thang").get("total").getTotal());
        createTableRow("", "3.3. Số NB chuyển đi cơ sở khác", table03Quarterly.get("chuyendi").get("1").getUnderOneAge(),table03Quarterly.get("chuyendi").get("1").getOneToFourteen(), table03Quarterly.get("chuyendi").get("1").getOverFifteen(), table03Quarterly.get("chuyendi").get("2").getUnderOneAge(), table03Quarterly.get("chuyendi").get("2").getOneToFourteen(), table03Quarterly.get("chuyendi").get("2").getOverFifteen(), table03Quarterly.get("chuyendi").get("total").getTotal());
        createTableRow("", "3.4. Số NB dừng điều trị", table03Quarterly.get("botri").get("1").getUnderOneAge(),table03Quarterly.get("botri").get("1").getOneToFourteen(), table03Quarterly.get("botri").get("1").getOverFifteen(), table03Quarterly.get("botri").get("2").getUnderOneAge(), table03Quarterly.get("botri").get("2").getOneToFourteen(), table03Quarterly.get("botri").get("2").getOverFifteen(), table03Quarterly.get("botri").get("total").getTotal());
        
        Mer04QuarterlyForm table04Quarterly = form.getTable04Quarterly();
        createTableRowBold("4", "Số người bệnh không khám trên 28 ngày, quay lại điều trị trong quý báo cáo", table03Quarterly.get("quaylai").get("1").getUnderOneAge(),table03Quarterly.get("quaylai").get("1").getOneToFourteen(), table03Quarterly.get("quaylai").get("1").getOverFifteen(), table03Quarterly.get("quaylai").get("2").getUnderOneAge(), table03Quarterly.get("quaylai").get("2").getOneToFourteen(), table03Quarterly.get("quaylai").get("2").getOverFifteen(), table03Quarterly.get("quaylai").get("total").getTotal());
        createTableRow("", "4.1. Phân theo nhóm đối tượng nguy cơ cao", table04Quarterly.getTong(), "", "", "", "", "", table04Quarterly.getTong());
        createTableRow("", "4.1.1. NCMT", table04Quarterly.getNcmt(), "", "", "", "", "", table04Quarterly.getNcmt());
        createTableRow("", "4.1.2. MSM", table04Quarterly.getMsm(), "", "", "", "", "", table04Quarterly.getMsm());
        createTableRow("", "4.1.3. Chuyển giới", table04Quarterly.getChuyengioi(), "", "", "", "", "", table04Quarterly.getChuyengioi());
        createTableRow("", "4.1.4. PNMD", table04Quarterly.getPnmd(), "", "", "", "", "", table04Quarterly.getPnmd());
        createTableRow("", "4.1.5. Học viên cai nghiện", table04Quarterly.getPhamnhan(), "", "", "", "", "", table04Quarterly.getPhamnhan());
    }
}
