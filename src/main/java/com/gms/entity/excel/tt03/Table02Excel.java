package com.gms.entity.excel.tt03;

import com.gms.components.PixelUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.form.tt03.Table02Form;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author vvthanh
 */
public class Table02Excel extends BaseView {

    private List<Table02Form> form;

    public Table02Excel(Workbook workbook, String sheetName, List<Table02Form> form) {
        this.workbook = workbook;
        this.sheetName = sheetName;
        this.form = form;
    }

    public void build() throws Exception {
        createHeader();
        createBody();
    }

    private void createHeader() {
        int index = getLastRowNumber();

        Row row = createRow();
        row = createRow();
        Cell cell = createCell(row, 0);
        cell.setCellValue("BẢNG 02: TƯ VẤN, XÉT NGHIỆM HIV");
        setFont(cell, fontSize, true, false, Font.U_NONE, IndexedColors.RED.getIndex());

        //Dòng tiêu đề
        row = createRow();
        row.setHeight(PixelUtils.pixel2WidthUnits(62));
        cell = createCell(row, 0);
        cell.setCellValue("TT");
        setBold(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, row.getPhysicalNumberOfCells());
        cell.setCellValue("Danh mục báo cáo");
        setBold(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, row.getPhysicalNumberOfCells());
        cell.setCellValue("Số người được tư vấn trước xét nghiệm");
        setBold(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, row.getPhysicalNumberOfCells());
        cell.setCellValue("Số người được xét nghiệm HIV");
        setBold(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, row.getPhysicalNumberOfCells() + 1);
        cell.setCellValue("Số người nhận được kết quả xét nghiệm");
        setBold(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        //Dòng gộp cột - tiêu đề thứ 2
        row = createRow();
        cell = createCell(row, 3);
        cell.setCellValue("Tổng");
        setBold(cell);
        setBorders(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 4);
        cell.setCellValue("HIV (+)");
        setBold(cell);
        setBorders(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 5);
        cell.setCellValue("Tổng");
        setBold(cell);
        setBorders(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        cell = createCell(row, 6);
        cell.setCellValue("HIV (+)");
        setBold(cell);
        setBorders(cell);
        cell.getCellStyle().setWrapText(true);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        //Gộp cột title tabel
        index++;
        setMerge(index, index, 0, 6, false);
        //Gộp cột header table
        index++;
        setMerge(index, index + 1, 0, 0, true);
        setMerge(index, index + 1, 1, 1, true);
        setMerge(index, index + 1, 2, 2, true);
        setMerge(index, index, 3, 4, true);
        setMerge(index, index, 5, 6, true);
    }

    private void createBody() {
        int index = getLastRowNumber();
        int indexTotal = 0;
        Row row;
        Cell cell;
        //Loại trừ các số tổng con
        List<Integer> remove = new ArrayList<>();
        for (Table02Form item : form) {
            if (item.getPosition() <= -1) {
                continue;
            }
            if (!item.getoParentID().equals(Long.parseLong("0"))) {
                remove.add(getLastRowNumber() + 1);
            }
            indexTotal++;
            row = createRow();
            cell = createCell(row, 0);
            setBorders(cell);
            cell.setCellValue(item.getStt());
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            if (item.getDanhMucBaoCao().contains("Thời kỳ mang thai") || item.getDanhMucBaoCao().contains("Giai đoạn chuyển dạ, đẻ")) {
                cell.setCellValue("");
            }

            cell = createCell(row, 1);
            setBorders(cell);
            cell.setCellValue(item.getDanhMucBaoCao());
            cell.getCellStyle().setWrapText(true);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            cell = createCell(row, 2);
            setBorders(cell);
            cell.setCellValue(item.getTuVanTruocXetNghiem());
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            cell = createCell(row, 3);
            setBorders(cell);
            cell.setCellValue(item.getDuocXetNghiemTong());
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            cell = createCell(row, 4);
            setBorders(cell);
            cell.setCellValue(item.getDuocXetNghiemDuongTinh());
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            cell = createCell(row, 5);
            setBorders(cell);
            cell.setCellValue(item.getNhanKetQuaTong());
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
            cell = createCell(row, 6);
            setBorders(cell);
            cell.setCellValue(item.getNhanKetQuaDuongtinh());
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
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
        String fml = "SUM(C" + (index + 1) + ":C" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- C" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 3);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(D" + (index + 1) + ":D" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- D" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 4);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(E" + (index + 1) + ":E" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- E" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 5);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(F" + (index + 1) + ":F" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- F" + integer;
        }
        cell.setCellFormula(fml);

        cell = createCell(row, 6);
        setBold(cell);
        setBorders(cell);
        cell.setCellType(CellType.FORMULA);
        fml = "SUM(G" + (index + 1) + ":G" + (index + indexTotal) + ")";
        for (Integer integer : remove) {
            fml += "- G" + integer;
        }
        cell.setCellFormula(fml);

    }

}
