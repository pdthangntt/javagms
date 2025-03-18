package com.gms.entity.excel.tt09;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.tt09.Phuluc01TT09Form;
import com.gms.entity.form.tt09.TablePhuluc01Form;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author vvthanh
 */
public class PhuLuc01Excel extends BaseView implements IExportExcel {

    private Phuluc01TT09Form form;
    private String extension;

    public PhuLuc01Excel(Phuluc01TT09Form form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Phụ lục 01-TT092012TTBYT";
    }

    @Override
    public String getFileName() {
        return String.format("%s.%s", form.getFileName(), extension);
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

    /**
     * Create header
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(35));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(65));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(65));

        setMerge(1, 1, 0, 6, false);

        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue(form.getTitle());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Báo cáo tháng %s năm %s (%s - %s)", (form.getMonth() >= 10 ? form.getMonth() : "0" + form.getMonth()), form.getYear(), form.getStartDate(), form.getEndDate()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        StringBuilder service = new StringBuilder();
        service.append("Dịch vụ: ");
        for (Map.Entry<String, String> entry : form.getServices().entrySet()) {
            service.append(entry.getValue());
            service.append(",");
        }
        service.deleteCharAt(service.length() - 1);
        cell = row.createCell(0);
        cell.setCellValue(service.toString());
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        
        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Cơ sở: %s", form.getSiteName()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Ngày báo cáo: %s", TextUtils.formatDate(new Date(), "dd/MM/yyyy")));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
    }

    /**
     * Table
     */
    private void createTable() {
        //Dòng đầu tiên để trắng
        Row row = createRow();
        Cell cell;

        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        //Tiêu đề
        row = createRow();
        cell = createCell(row, 0);
        cell.setCellValue("STT");
        cell.setCellStyle(th);

        cell = createCell(row, 1);
        cell.setCellValue("Đối tượng xét nghiệm");
        cell.setCellStyle(th);

        cell = createCell(row, 2);
        cell.setCellValue("Nhóm tuổi");
        cell.setCellStyle(th);

        cell = createCell(row, 12);
        cell.setCellValue("Giới");
        cell.setCellStyle(th);

        cell = createCell(row, 16);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);

        //Dòng tiêu đề thứ 2
        row = createRow();
        cell = createCell(row, 2);
        cell.setCellValue("<15");
        cell.setCellStyle(th);

        cell = createCell(row, 4);
        cell.setCellValue("15- <25");
        cell.setCellStyle(th);

        cell = createCell(row, 6);
        cell.setCellValue("25-49");
        cell.setCellStyle(th);

        cell = createCell(row, 8);
        cell.setCellValue(">49");
        cell.setCellStyle(th);

        cell = createCell(row, 10);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th);

        cell = createCell(row, 12);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 14);
        cell.setCellValue("Nữ");
        cell.setCellStyle(th);

        //Dòng tiêu đề thứ 3
        row = createRow();
        for (int i = 2; i <= 17; i = i + 2) {
            cell = createCell(row, i);
            cell.setCellValue("XN");
            cell.setCellStyle(th);
            setBorders(cell);

            cell = createCell(row, i + 1);
            cell.setCellValue("(+)");
            cell.setCellStyle(th);
            setBorders(cell);
        }

        int index = 7;
        setMerge(index, index + 2, 0, 0, true); //STT
        setMerge(index, index + 2, 1, 1, true); //Đối tượng XN
        setMerge(index, index, 2, 11, true); //Dòng tuổi
        setMerge(index, index, 12, 15, true); //Dòng giới
        setMerge(index, index + 1, 16, 17, true); //Dòng tổng

        //gọp dòng tiêu đề thứ 02
        index++;
        for (int i = 2; i <= 15; i = i + 2) {
            setMerge(index, index, i, i + 1, true); //Dòng tổng row
        }

        int indexSum = getLastRowNumber();
        int indexTotal = 0;
        
        for (TablePhuluc01Form item : form.getTable()) {
            if (item.getPosition() == -1) {
                continue;
            }
            indexTotal++;
            //stt
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue(item.getStt());
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            //Đối tượng xét nghiệm
            cell = createCell(row, 1);
            setBorders(cell);
            cell.setCellValue(item.getDoituongxetnghiem());
            cell.getCellStyle().setWrapText(true);

            cell = createCell(row, 2);
            setBorders(cell);
            cell.setCellValue(item.getAge0_15Tong());
            cell = createCell(row, 3);
            setBorders(cell);
            cell.setCellValue(item.getAge0_15DuongTinh());

            cell = createCell(row, 4);
            setBorders(cell);
            cell.setCellValue(item.getAge15_25Tong());
            cell = createCell(row, 5);
            setBorders(cell);
            cell.setCellValue(item.getAge15_25DuongTinh());

            cell = createCell(row, 6);
            setBorders(cell);
            cell.setCellValue(item.getAge25_49Tong());
            cell = createCell(row, 7);
            setBorders(cell);
            cell.setCellValue(item.getAge25_49DuongTinh());

            cell = createCell(row, 8);
            setBorders(cell);
            cell.setCellValue(item.getAge49_maxTong());
            cell = createCell(row, 9);
            setBorders(cell);
            cell.setCellValue(item.getAge49_maxDuongTinh());

            cell = createCell(row, 10);
            setBorders(cell);
            cell.setCellValue(item.getAge_noneTong());
            cell = createCell(row, 11);
            setBorders(cell);
            cell.setCellValue(item.getAge_noneDuongtinh());

            //Giới tính
            cell = createCell(row, 12);
            setBorders(cell);
            cell.setCellValue(item.getGenderMaleTong());
            cell = createCell(row, 13);
            setBorders(cell);
            cell.setCellValue(item.getGenderMaleDuongTinh());

            cell = createCell(row, 14);
            setBorders(cell);
            cell.setCellValue(item.getGenderFemaleTong());
            cell = createCell(row, 15);
            setBorders(cell);
            cell.setCellValue(item.getGenderFemaleDuongTinh());

            cell = createCell(row, 16);
            setBorders(cell);
            cell.setCellValue(item.getTong());
            cell = createCell(row, 17);
            setBorders(cell);
            cell.setCellValue(item.getTongDuongTinh());
        }
        
        for (TablePhuluc01Form item : form.getTable()) {
            
            if (item.getPosition() != -1 || item.getTong() == 0 || !item.getoParentID().equals(Long.valueOf("0"))) {
                continue;
            }
            //stt
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue("");
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            //Đối tượng xét nghiệm
            cell = createCell(row, 1);
            setBorders(cell);
            cell.setCellValue(item.getDoituongxetnghiem());
            cell.getCellStyle().setWrapText(true);

            cell = createCell(row, 2);
            setBorders(cell);
            cell.setCellValue(item.getAge0_15Tong());
            cell = createCell(row, 3);
            setBorders(cell);
            cell.setCellValue(item.getAge0_15DuongTinh());

            cell = createCell(row, 4);
            setBorders(cell);
            cell.setCellValue(item.getAge15_25Tong());
            cell = createCell(row, 5);
            setBorders(cell);
            cell.setCellValue(item.getAge15_25DuongTinh());

            cell = createCell(row, 6);
            setBorders(cell);
            cell.setCellValue(item.getAge25_49Tong());
            cell = createCell(row, 7);
            setBorders(cell);
            cell.setCellValue(item.getAge25_49DuongTinh());

            cell = createCell(row, 8);
            setBorders(cell);
            cell.setCellValue(item.getAge49_maxTong());
            cell = createCell(row, 9);
            setBorders(cell);
            cell.setCellValue(item.getAge49_maxDuongTinh());

            cell = createCell(row, 10);
            setBorders(cell);
            cell.setCellValue(item.getAge_noneTong());
            cell = createCell(row, 11);
            setBorders(cell);
            cell.setCellValue(item.getAge_noneDuongtinh());

            //Giới tính
            cell = createCell(row, 12);
            setBorders(cell);
            cell.setCellValue(item.getGenderMaleTong());
            cell = createCell(row, 13);
            setBorders(cell);
            cell.setCellValue(item.getGenderMaleDuongTinh());

            cell = createCell(row, 14);
            setBorders(cell);
            cell.setCellValue(item.getGenderFemaleTong());
            cell = createCell(row, 15);
            setBorders(cell);
            cell.setCellValue(item.getGenderFemaleDuongTinh());

            cell = createCell(row, 16);
            setBorders(cell);
            cell.setCellValue(item.getTong());
            cell = createCell(row, 17);
            setBorders(cell);
            cell.setCellValue(item.getTongDuongTinh());
        }

        //Dòng tổng
        row = createRow();
        cell = createCell(row, 0);
        setBorders(cell);

        cell = createCell(row, 1);
        setBold(cell);
        setBorders(cell);
        cell.setCellValue("Tổng cộng:");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 2);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        String fml = "SUM(C" + (indexSum + 1) + ":C" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 3);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(D" + (indexSum + 1) + ":D" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 4);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(E" + (indexSum + 1) + ":E" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 5);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(F" + (indexSum + 1) + ":F" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 6);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(G" + (indexSum + 1) + ":G" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 7);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(H" + (indexSum + 1) + ":H" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 8);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(I" + (indexSum + 1) + ":I" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 9);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(J" + (indexSum + 1) + ":J" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 10);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(K" + (indexSum + 1) + ":K" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 11);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(L" + (indexSum + 1) + ":L" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 12);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(M" + (indexSum + 1) + ":M" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 13);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(N" + (indexSum + 1) + ":N" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 14);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(O" + (indexSum + 1) + ":O" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 15);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(P" + (indexSum + 1) + ":P" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 16);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(Q" + (indexSum + 1) + ":Q" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
        
        cell = createCell(row, 17);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(R" + (indexSum + 1) + ":R" + (indexSum + indexTotal) + ")";
        cell.setCellFormula(fml);
    }
}
