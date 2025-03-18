package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcMerSixMonthForm;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTable;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTablePercent;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTableSum;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.castor.core.util.StringUtil;

/**
 *
 * @author pdThang
 */
public class MERSixMonthExcel extends BaseView implements IExportExcel {
    
    private OpcMerSixMonthForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;
    
    public MERSixMonthExcel(OpcMerSixMonthForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "BCDUANPEPFAR";
    }
    
    public MERSixMonthExcel(OpcMerSixMonthForm data) {
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
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(30));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(420));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(90));
        
        setMerge(1, 1, 0, 8, false);
        
        createRow();
        createTitleRow("Báo cáo dự án PEPFAR".toUpperCase());
        createRow();
        
        int cellIndexFilter = 1;
//        createFilterRow("Loại báo cáo", form.getTitle(), cellIndexFilter);
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        if (!StringUtils.isEmpty(form.getSearchSiteID())) {
            createFilterRow("Cơ sở điều trị", options.get("treatment-facility").get(form.getSearchSiteID()), cellIndexFilter);
        }
        
    }
    
    private void createTable() {
        getCurrentSheet();
        createRow();
        int line = getLastRowNumber();

        //Header table
        createTableRowBold("C", "CHỈ SỐ BÁO CÁO 6 THÁNG", "", "", "", "", "", "", "");
        setMerge(line, line, 1, 8, false);
        createTableHeaderRow("I", "CHỈ SỐ LAO-HIV", "Nam", "", "", "Nữ", "", "", "Tổng");
        createTableHeaderRow("", "", "<1", "1-14", "≥15", "<1", "1-14", "≥15", "");
        
        setMerge(line + 1, line + 1, 2, 4, false);
        setMerge(line + 1, line + 1, 5, 7, false);

        //row table
        OpcMerSixMonthTablePercent row1 = form.getTable03().get("tyLeHoanThanh");
        createTableRowBold("1", "Tỷ lệ NB ARV hoàn thành điều trị dự phòng Lao (INH hoặc phác đồ 3 tháng) trong 6 tháng", row1.getUnderOneAgeNam(), row1.getOneToFourteenNam(), row1.getOverFifteenNam(), row1.getUnderOneAgeNu(), row1.getOneToFourteenNu(), row1.getOverFifteenNu(), row1.getSum());
        OpcMerSixMonthTableSum row2 = form.getTable02().get("mauSo");
        createTableRow("1.1", "Mẫu số: Số NB ARV bắt đầu điều trị dự phòng Lao 6 tháng trước kỳ báo cáo (từ " + form.getStartDateSixMonthBefore() + "đến " + form.getEndDateSixMonthBefore() + ")", row2.getUnderOneAgeNam(), row2.getOneToFourteenNam(), row2.getOverFifteenNam(), row2.getUnderOneAgeNu(), row2.getOneToFourteenNu(), row2.getOverFifteenNu(), row2.getSum());
        
        createTableRow("", "1.1.1. Số NB mới điều trị ARV",
                form.getTable().get("mauSoMoi").get("1").getUnderOneAge(),
                form.getTable().get("mauSoMoi").get("1").getOneToFourteen(),
                form.getTable().get("mauSoMoi").get("1").getOverFifteen(),
                form.getTable().get("mauSoMoi").get("2").getUnderOneAge(),
                form.getTable().get("mauSoMoi").get("2").getOneToFourteen(),
                form.getTable().get("mauSoMoi").get("2").getOverFifteen(),
                form.getTable().get("mauSoMoi").get("total").getTotal());
        createTableRow("", "1.1.2. Số NB ARV cũ",
                form.getTable().get("mauSoCu").get("1").getUnderOneAge(),
                form.getTable().get("mauSoCu").get("1").getOneToFourteen(),
                form.getTable().get("mauSoCu").get("1").getOverFifteen(),
                form.getTable().get("mauSoCu").get("2").getUnderOneAge(),
                form.getTable().get("mauSoCu").get("2").getOneToFourteen(),
                form.getTable().get("mauSoCu").get("2").getOverFifteen(),
                form.getTable().get("mauSoCu").get("total").getTotal());
        
        OpcMerSixMonthTableSum row3 = form.getTable02().get("tuSo");
        createTableRow("1.2", "Tử số: Trong nhóm NB trong mẫu số, số NB hoàn thành điều trị dự phòng Lao tính đến cuối kỳ báo cáo, trong đó", row3.getUnderOneAgeNam(), row3.getOneToFourteenNam(), row3.getOverFifteenNam(), row3.getUnderOneAgeNu(), row3.getOneToFourteenNu(), row3.getOverFifteenNu(), row3.getSum());
        createTableRow("", "Phân nhóm theo nhóm bệnh nhân ARV", row3.getUnderOneAgeNam(), row3.getOneToFourteenNam(), row3.getOverFifteenNam(), row3.getUnderOneAgeNu(), row3.getOneToFourteenNu(), row3.getOverFifteenNu(), row3.getSum());
        
        createTableRow("", "1.2.1. Số NB mới điều trị ARV",
                form.getTable().get("tuSoMoi").get("1").getUnderOneAge(),
                form.getTable().get("tuSoMoi").get("1").getOneToFourteen(),
                form.getTable().get("tuSoMoi").get("1").getOverFifteen(),
                form.getTable().get("tuSoMoi").get("2").getUnderOneAge(),
                form.getTable().get("tuSoMoi").get("2").getOneToFourteen(),
                form.getTable().get("tuSoMoi").get("2").getOverFifteen(),
                form.getTable().get("tuSoMoi").get("total").getTotal());
        createTableRow("", "1.2.2. Số NB ARV cũ",
                form.getTable().get("tuSoCu").get("1").getUnderOneAge(),
                form.getTable().get("tuSoCu").get("1").getOneToFourteen(),
                form.getTable().get("tuSoCu").get("1").getOverFifteen(),
                form.getTable().get("tuSoCu").get("2").getUnderOneAge(),
                form.getTable().get("tuSoCu").get("2").getOneToFourteen(),
                form.getTable().get("tuSoCu").get("2").getOverFifteen(),
                form.getTable().get("tuSoCu").get("total").getTotal());
        
        int line2 = getLastRowNumber();
        createTableRow("", "Phân nhóm theo phác đồ điều trị dự phòng Lao", row3.getSum(), "", "", "", "", "", row3.getSum());
        createTableRow("", "INH", row3.getSum(), "", "", "", "", "", row3.getSum());
        createTableRow("", "Phác đồ 3 tháng", "", "", "", "", "", "", "");
        setMerge(line2, line2, 2, 7, false);
        setMerge(line2 + 1, line2 + 1, 2, 7, false);
        setMerge(line2 + 2, line2 + 2, 2, 7, false);
        
        OpcMerSixMonthTablePercent row4 = form.getTable03().get("tyLeBatDau");
        createTableRowBold("2", "Tỷ lệ NB ARV được sàng lọc lao và được bắt đầu điều trị lao trong kỳ báo cáo", row4.getUnderOneAgeNam(), row4.getOneToFourteenNam(), row4.getOverFifteenNam(), row4.getUnderOneAgeNu(), row4.getOneToFourteenNu(), row4.getOverFifteenNu(), row4.getSum());
        
        OpcMerSixMonthTableSum row5 = form.getTable02().get("itNhat1Lan");
        createTableRow("2.1", "Số NB ARV được sàng lọc lao ít nhất 1 lần trong kỳ báo cáo, trong đó", row5.getUnderOneAgeNam(), row5.getOneToFourteenNam(), row5.getOverFifteenNam(), row5.getUnderOneAgeNu(), row5.getOneToFourteenNu(), row5.getOverFifteenNu(), row5.getSum());
        
        createTableRow("", "2.1.1. Số NB mới điều trị ARV",
                form.getTable().get("itNhat1LanMoi").get("1").getUnderOneAge(),
                form.getTable().get("itNhat1LanMoi").get("1").getOneToFourteen(),
                form.getTable().get("itNhat1LanMoi").get("1").getOverFifteen(),
                form.getTable().get("itNhat1LanMoi").get("2").getUnderOneAge(),
                form.getTable().get("itNhat1LanMoi").get("2").getOneToFourteen(),
                form.getTable().get("itNhat1LanMoi").get("2").getOverFifteen(),
                form.getTable().get("itNhat1LanMoi").get("total").getTotal());
        createTableRow("", "2.1.2. Số NB ARV cũ",
                form.getTable().get("itNhat1LanCu").get("1").getUnderOneAge(),
                form.getTable().get("itNhat1LanCu").get("1").getOneToFourteen(),
                form.getTable().get("itNhat1LanCu").get("1").getOverFifteen(),
                form.getTable().get("itNhat1LanCu").get("2").getUnderOneAge(),
                form.getTable().get("itNhat1LanCu").get("2").getOneToFourteen(),
                form.getTable().get("itNhat1LanCu").get("2").getOverFifteen(),
                form.getTable().get("itNhat1LanCu").get("total").getTotal());
        
        OpcMerSixMonthTableSum row6 = form.getTable02().get("sangLocNghiLao");
        createTableRow("2.2", "Số NB ARV được sàng lọc và nghi lao trong kỳ báo cáo, trong đó", row6.getUnderOneAgeNam(), row6.getOneToFourteenNam(), row6.getOverFifteenNam(), row6.getUnderOneAgeNu(), row6.getOneToFourteenNu(), row6.getOverFifteenNu(), row6.getSum());
        
        createTableRow("", "2.2.1. Số NB mới điều trị ARV",
                form.getTable().get("sangLocNghiLaoMoi").get("1").getUnderOneAge(),
                form.getTable().get("sangLocNghiLaoMoi").get("1").getOneToFourteen(),
                form.getTable().get("sangLocNghiLaoMoi").get("1").getOverFifteen(),
                form.getTable().get("sangLocNghiLaoMoi").get("2").getUnderOneAge(),
                form.getTable().get("sangLocNghiLaoMoi").get("2").getOneToFourteen(),
                form.getTable().get("sangLocNghiLaoMoi").get("2").getOverFifteen(),
                form.getTable().get("sangLocNghiLaoMoi").get("total").getTotal());
        createTableRow("", "2.2.2. Số NB ARV cũ",
                form.getTable().get("sangLocNghiLaoCu").get("1").getUnderOneAge(),
                form.getTable().get("sangLocNghiLaoCu").get("1").getOneToFourteen(),
                form.getTable().get("sangLocNghiLaoCu").get("1").getOverFifteen(),
                form.getTable().get("sangLocNghiLaoCu").get("2").getUnderOneAge(),
                form.getTable().get("sangLocNghiLaoCu").get("2").getOneToFourteen(),
                form.getTable().get("sangLocNghiLaoCu").get("2").getOverFifteen(),
                form.getTable().get("sangLocNghiLaoCu").get("total").getTotal());
        
        OpcMerSixMonthTableSum row7 = form.getTable02().get("chuanDoanLao");
        createTableRow("2.3", "Số NB ARV được chẩn đoán lao trong kỳ báo cáo, trong đó", row7.getUnderOneAgeNam(), row7.getOneToFourteenNam(), row7.getOverFifteenNam(), row7.getUnderOneAgeNu(), row7.getOneToFourteenNu(), row7.getOverFifteenNu(), row7.getSum());
        
        createTableRow("", "2.3.1. Số NB mới điều trị ARV",
                form.getTable().get("chuanDoanLaoMoi").get("1").getUnderOneAge(),
                form.getTable().get("chuanDoanLaoMoi").get("1").getOneToFourteen(),
                form.getTable().get("chuanDoanLaoMoi").get("1").getOverFifteen(),
                form.getTable().get("chuanDoanLaoMoi").get("2").getUnderOneAge(),
                form.getTable().get("chuanDoanLaoMoi").get("2").getOneToFourteen(),
                form.getTable().get("chuanDoanLaoMoi").get("2").getOverFifteen(),
                form.getTable().get("chuanDoanLaoMoi").get("total").getTotal());
        createTableRow("", "2.3.2. Số NB ARV cũ",
                form.getTable().get("chuanDoanLaoCu").get("1").getUnderOneAge(),
                form.getTable().get("chuanDoanLaoCu").get("1").getOneToFourteen(),
                form.getTable().get("chuanDoanLaoCu").get("1").getOverFifteen(),
                form.getTable().get("chuanDoanLaoCu").get("2").getUnderOneAge(),
                form.getTable().get("chuanDoanLaoCu").get("2").getOneToFourteen(),
                form.getTable().get("chuanDoanLaoCu").get("2").getOverFifteen(),
                form.getTable().get("chuanDoanLaoCu").get("total").getTotal());
        
        OpcMerSixMonthTableSum row8 = form.getTable02().get("dieuTriLao");
        createTableRow("2.4", "Số NB ARV được bắt đầu điều trị lao trong kỳ báo cáo, trong đó", row8.getUnderOneAgeNam(), row8.getOneToFourteenNam(), row8.getOverFifteenNam(), row8.getUnderOneAgeNu(), row8.getOneToFourteenNu(), row8.getOverFifteenNu(), row8.getSum());
        
        createTableRow("", "2.4.1. Số NB mới điều trị ARV",
                form.getTable().get("dieuTriLaoMoi").get("1").getUnderOneAge(),
                form.getTable().get("dieuTriLaoMoi").get("1").getOneToFourteen(),
                form.getTable().get("dieuTriLaoMoi").get("1").getOverFifteen(),
                form.getTable().get("dieuTriLaoMoi").get("2").getUnderOneAge(),
                form.getTable().get("dieuTriLaoMoi").get("2").getOneToFourteen(),
                form.getTable().get("dieuTriLaoMoi").get("2").getOverFifteen(),
                form.getTable().get("dieuTriLaoMoi").get("total").getTotal());
        createTableRow("", "2.4.2. Số NB ARV cũ",
                form.getTable().get("dieuTriLaoCu").get("1").getUnderOneAge(),
                form.getTable().get("dieuTriLaoCu").get("1").getOneToFourteen(),
                form.getTable().get("dieuTriLaoCu").get("1").getOverFifteen(),
                form.getTable().get("dieuTriLaoCu").get("2").getUnderOneAge(),
                form.getTable().get("dieuTriLaoCu").get("2").getOneToFourteen(),
                form.getTable().get("dieuTriLaoCu").get("2").getOverFifteen(),
                form.getTable().get("dieuTriLaoCu").get("total").getTotal());
        
    }
    
}
