package com.gms.entity.excel.htc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.ReferralSourceEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc.HtcElogForm;
import java.io.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author pdThang
 */
public class HtcElogExcel extends BaseView implements IExportExcel {

    
    private HtcElogForm form;
    private String extension;

    public HtcElogExcel(HtcElogForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sheet 1";
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();

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
        Sheet currentSheet = getCurrentSheet();
        int i = 0;
        currentSheet.setColumnWidth(i, PixelUtils.pixel2WidthUnits(150));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(190));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(300));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));
        currentSheet.setColumnWidth(i += 1, PixelUtils.pixel2WidthUnits(500));

        Row row = createRow();
        setMerge(1, 1, 0, 3, false);
        Cell cell = row.createCell(0);
        cell.setCellValue(form.getTitle().toUpperCase());
        setBold(cell);
        setFont(cell, (short) 18, true, false, new Byte("0"));
        row.setHeight(PixelUtils.pixel2WidthUnits(30));
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        setMerge(2, 2, 0, 3, false);
        cell = row.createCell(0);
        cell.setCellValue(String.format("Tên cơ sở: %s", form.getSiteName().toUpperCase()));
        setFont(cell, fontSize, false, true, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        setMerge(3, 3, 0, 3, false);
        cell = row.createCell(0);
        cell.setCellValue((StringUtils.isEmpty(form.getSearch().getAdvisoryeTimeFrom()) || StringUtils.isEmpty(form.getSearch().getAdvisoryeTimeTo())) ? form.getAdvisoryeTime() : form.getSearch().getAdvisoryeTimeFrom().equals(form.getSearch().getAdvisoryeTimeTo()) ? String.format("Ngày tư vấn: %s", form.getSearch().getAdvisoryeTimeFrom()) : String.format("Ngày tư vấn từ %s đến %s", form.getSearch().getAdvisoryeTimeFrom(), form.getSearch().getAdvisoryeTimeTo()));
        setFont(cell, (short) 12, false, true, new Byte("0"));
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        setMerge(4, 4, 0, 3, false);
        cell = row.createCell(0);
        cell.setCellValue(String.format("Mã cơ sở: %s", form.getSiteCode().toUpperCase()));
        setFont(cell, fontSize, false, true, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        row = createRow();
        setMerge(5, 5, 0, 3, false);
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        cell = row.createCell(0);
        cell.setCellValue(!"".equals(form.getSearch().getService()) && form.getSearch().getService() != null ? String.format("Dịch vụ: %s", "CD".equals(form.getSearch().getService()) ? "CĐ" : form.getSearch().getService()) : "");
        setFont(cell, fontSize, false, true, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        int i = 0;

        //Dòng đầu tiên để trắng
        Row row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        //Dòng đầu tiên để trắng
        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        Cell cell;

        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        th.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        th.setAlignment(HorizontalAlignment.LEFT);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        th.setFont(font);

        //Tiêu đề
        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(13));
        cell = createCell(row, i);
        cell.setCellValue("TT");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, i += 1);
        cell.setCellValue("A3. Mã cơ sở ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A4. Số thứ tự khách hàng");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A2. Loại dịch vụ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A2.1 Mục đích khách hàng đến cơ sở cố định");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A3.1 Nhân viên dịch vụ không chuyên");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A5. Tên khách hàng");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A6. Số điện thoại");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A7. Số chứng minh");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A8. Dân tộc");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A9. Giới tính");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A10. Năm sinh");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A11.4  Địa chỉ thường trú");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A11.3 Phường /xã");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A11.2 Quận /Huyện");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A11.1 Tỉnh/Thành phố");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A12.4. Địa chỉ tạm trú");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A12.3 Phường / Xã");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A12.2 Quận / Huyện");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A12.1 Tỉnh / Thành phố");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A13. Nghề nghiệp");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.1 Nghiện chích ma túy (NCMT)");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue(" A14.2 Người bán dâm (PNBD)");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.3 Phụ nữ mang thai");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.4 Người hiến máu");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.5 Bệnh nhân lao");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.6 Người mắc nhiễm trùng lây truyền qua đường TD");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.7 Thanh niên khám tuyển nghĩa vụ quân sự	");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.8 Nam quan hệ tình dục với nam (MSM)");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.9 Người chuyển giới");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.10 Vợ/chồng/ban tình của người nhiễm HIV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.11 Vợ/chồng/ban tình của người NCC");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.12Bệnh nhân nghi ngờ AIDS");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A14.13 Các đối tượng khác");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A15.1 Tiêm chích ma túy");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A15.2 Quan hệ tình dục với người mua/bán dâm");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A15.3 Quan hệ tình dục đồng giới nam");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A15.4 Quan hệ với nhiều người (ko vì tiềm & ma túy)");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A15.5 Nguy cơ khác của bản thân");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.1 Tiếp cận cộng đồng");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.2 Kênh xét nghiệm theo dấu bạn tình/bạn chích");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.3 Cán Bộ y tế");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.4 Mạng xã hội");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.5 Khác");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Nguyên nhân khách hàng biết đến dịch vụ(Cũ, ko dùng nữa)");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.1. Mã số của tiếp cận cộng đồng:");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A17.2 Mã số theo dấu bạn tình/bạn chích");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("A1. Ngày lấy máu xét nghiệm sàng lọc	");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("B. Xét nghiệm sàng lọc");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("B1.Kết quả xét nghiệm sàng lọc");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("B1.1 Chọn khi kết quả XN là có phản ứng ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("B2. Khách hàng đồng ý tiếp tục XN tại cơ sở y tế ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("B2.1. Tên cơ sở  XN sàng lọc	");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C1. Tên cơ sở xét nghiệm khẳng định HIV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C2. Mã xét nghiệm khẳng định do cơ sở TVXN cố định cấp");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C2. 1. Loại hình xét nghiệm khẳng định");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C3. Kết quả xét nghiệm khẳng định");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C3a. Kết quả xét nghiệm nhanh nhiễm mới");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C4a. Đường lây truyền HIV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C4b. Loại xét nghiệm PCR HIV bổ sung	");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C4b. Kết quả xét nghiệm PCR HIV :");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C5.  Ngày xét nghiệm khẳng định ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C6. Ngày cơ sở nhận kết quả xét nghiệm khẳng định");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C8. Kết quả xét nghiệm tải lượng virus");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("C8b. Kết quả xét nghiệm tải lượng virus");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Chăm sóc điều trị HIV/AIDS");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Điều trị các bệnh LNQĐTD");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Điều trị Lao");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Dự phòng lây truyền mẹ con");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("KHHGĐ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Chăm sóc y tế khác");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Chăm sóc y tế khác");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Tiếp cận cộng đồng");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Nhóm hỗ trợ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Cai nghiện cộng đồng	Tư vấn giảm nguy cơ bổ sung");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Lấy máu xét nghiệm khẳng định HIV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Khác");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Không chuyển gửi");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Ghi chú");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D1. Ngày thực hiện tư vấn chuyển gửi điều trị ARV:");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D1a. Kết quả tư vấn chuyển gửi điều trị ARV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D1.1 Ngày chuyển gửi điều trị ARV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D1.2. Kết quả tư vấn cung cấp thông tin thực hiện xét nghiệm theo dấu bạn tình/bạn chích ");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D2. Tên cơ sở điều trị mà khách hàng được chuyển gửi tới");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D2. Mã cơ sở điều trị mà khách hàng được chuyển gửi tới");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D2.1 Tỉnh/Thành phố chuyển đến");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D2.2 Huyện chuyển đến");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D3. Ngày đăng ký điều trị ARV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D4. Tên cơ sở mà khách hành đã đăng ký điều trị");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D4. Mã cơ sở mà khách hàng đã đăng ký điều trị");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D4.1 Tỉnh/Thành phố điều trị");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D4.2 Huyện điều trị");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("D5. Mã số điều trị HIV");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Tư vấn viên 01");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Tư vấn viên 02");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("Booking ID");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("GUID");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("UPDATE Status");
        cell.setCellStyle(th);

        cell = createCell(row, i += 1);
        cell.setCellValue("updateTime");
        cell.setCellStyle(th);

        // loop to create row content
        int index = 0;

        if (form.getItems().size() > 0) {

            for (HtcVisitEntity item : form.getItems()) {
                int indexRow = 0;
                // Số thứ tự
                row = createRow();
                cell = createCell(row, indexRow);

                cell.setCellValue(index += 1);
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(form.getSiteCode().toUpperCase());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getCode()) ? "" : item.getCode().toUpperCase());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getServiceID()) ? "" : "CD".equals(item.getServiceID()) ? "CĐ" : item.getServiceID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPatientName()) ? "" : item.getPatientName().toUpperCase());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPatientPhone()) ? "" : item.getPatientPhone());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPatientID()) ? "" : item.getPatientID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getRaceID()) ? "" : form.getOptions().get("race").get(item.getRaceID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getGenderID()) ? "3" : item.getGenderID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getYearOfBirth()) ? "" : item.getYearOfBirth());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPermanentAddress()) ? "" : item.getPermanentAddress());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPermanentWardID()) ? "" : form.getWards().get(item.getPermanentWardID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPermanentDistrictID()) ? "" : form.getLocations().get("districts").getOrDefault(item.getPermanentDistrictID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPermanentProvinceID()) ? "" : form.getLocations().get("provinces").getOrDefault(item.getPermanentProvinceID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getCurrentAddress()) ? "" : item.getCurrentAddress());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getCurrentWardID()) ? "" : form.getWards().get(item.getCurrentWardID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getCurrentDistrictID()) ? "" : form.getLocations().get("districts").getOrDefault(item.getCurrentDistrictID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getCurrentProvinceID()) ? "" : form.getLocations().get("provinces").getOrDefault(item.getCurrentProvinceID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getJobID()) ? "" : form.getOptions().get("job").get(item.getJobID()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.PREGNANT_WOMEN.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.LAO.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.MSM.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue("");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : TestObjectGroupEnum.KHAC.getKey().equals(item.getObjectGroupID()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                if (item.getRiskBehaviorID().isEmpty()) {
                    cell.setCellValue("");
                } else {
                    for (String string : item.getRiskBehaviorID()) {
                        if ("1".equals(string)) {
                            cell.setCellValue("1");
                            break;
                        }
                        cell.setCellValue("");
                    }
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                if (item.getRiskBehaviorID().isEmpty()) {
                    cell.setCellValue("");
                } else {
                    for (String string : item.getRiskBehaviorID()) {
                        if ("2".equals(string)) {
                            cell.setCellValue("1");
                            break;
                        }
                        cell.setCellValue("");
                    }
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                if (item.getRiskBehaviorID().isEmpty()) {
                    cell.setCellValue("");
                } else {
                    for (String string : item.getRiskBehaviorID()) {
                        if ("3".equals(string)) {
                            cell.setCellValue("1");
                            break;
                        }
                        cell.setCellValue("");
                    }
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                if (item.getRiskBehaviorID().isEmpty()) {
                    cell.setCellValue("");
                } else {
                    for (String string : item.getRiskBehaviorID()) {
                        if ("4".equals(string)) {
                            cell.setCellValue("1");
                            break;
                        }
                        cell.setCellValue("");
                    }
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                if (item.getRiskBehaviorID().isEmpty()) {
                    cell.setCellValue("");
                } else {
                    for (String string : item.getRiskBehaviorID()) {
                        if ("5".equals(string)) {
                            cell.setCellValue("1");
                            break;
                        }
                        cell.setCellValue("");
                    }
                }
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getReferralSource() == null || item.getReferralSource().isEmpty() ? "" :  item.getReferralSource().contains(ReferralSourceEnum.TC_CONG_DONG.getKey()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getReferralSource() == null || item.getReferralSource().isEmpty() ? "" : item.getReferralSource().contains(ReferralSourceEnum.KENH_BTBC.getKey()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getReferralSource() == null || item.getReferralSource().isEmpty() ? "" : item.getReferralSource().contains(ReferralSourceEnum.CAN_BO_YTE.getKey()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getReferralSource() == null || item.getReferralSource().isEmpty() ? "" : item.getReferralSource().contains(ReferralSourceEnum.INTERNET.getKey()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getReferralSource() == null || item.getReferralSource().isEmpty() ? "" : item.getReferralSource().contains(ReferralSourceEnum.KHAC.getKey()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getApproacherNo()) ? "" : item.getApproacherNo().toUpperCase());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getAdvisoryeTime() == null ? "" : TextUtils.formatDate(item.getAdvisoryeTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getIsAgreePreTest()) ? "" : item.getIsAgreePreTest());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getTestResultsID()) ? "" : item.getTestResultsID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getResultsTime() == null ? "" : TextUtils.formatDate(item.getResultsTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getIsAgreeTest() == null ? "" : item.getIsAgreeTest() == true ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(form.getSiteName());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getSiteConfirmTest()) ? "" : form.getConfirmSite().get(item.getSiteConfirmTest()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getConfirmTestNo()) ? "" : item.getConfirmTestNo());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getConfirmResultsID()) ? "" : item.getConfirmResultsID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getEarlyHiv()) ? "" : item.getEarlyHiv());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getModeOfTransmission()) ? "" : ("1".equals(item.getModeOfTransmission()) || "2".equals(item.getModeOfTransmission()) || "3".equals(item.getModeOfTransmission()) || "4".equals(item.getModeOfTransmission())) ? item.getModeOfTransmission() : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getResultsSiteTime() == null ? "" : TextUtils.formatDate(item.getResultsSiteTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getVirusLoad()) ? "" : item.getVirusLoad());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getExchangeConsultTime() == null ? "" : TextUtils.formatDate(item.getExchangeConsultTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getArvExchangeResult()) ? "" : "1".equals(item.getArvExchangeResult()) ? "1" : "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getExchangeTime() == null ? "" : TextUtils.formatDate(item.getExchangeTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getPartnerProvideResult()) ? "" : item.getPartnerProvideResult());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getArrivalSite()) ? "" : item.getArrivalSite());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getExchangeProvinceID()) ? "" : form.getLocations().get("provinces").getOrDefault(item.getExchangeProvinceID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getExchangeDistrictID()) ? "" : form.getLocations().get("districts").getOrDefault(item.getExchangeDistrictID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getRegisterTherapyTime() == null ? "" : TextUtils.formatDate(item.getRegisterTherapyTime(), "dd/MM/yyyy"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getRegisteredTherapySite()) ? "" : item.getRegisteredTherapySite());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getTherapyRegistProvinceID()) ? "" : form.getLocations().get("provinces").getOrDefault(item.getTherapyRegistProvinceID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getTherapyRegistDistrictID()) ? "" : form.getLocations().get("districts").getOrDefault(item.getTherapyRegistDistrictID(), " "));
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getTherapyNo()) ? "" : item.getTherapyNo());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getStaffBeforeTestID()) ? "" : item.getStaffBeforeTestID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(StringUtils.isEmpty(item.getStaffAfterID()) ? "" : item.getStaffAfterID());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell = createCell(row, indexRow += 1);

                cell.setCellValue(item.getUpdateAt() == null ? "" : TextUtils.formatDate(item.getUpdateAt(), "dd/MM/yyyy hh:mm:ss"));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            }
        }

    }

}
