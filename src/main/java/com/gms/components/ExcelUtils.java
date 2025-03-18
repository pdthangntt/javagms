package com.gms.components;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author vvthanh
 */
public class ExcelUtils {

    public static Workbook getWorkbook(String excelFilePath) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);//bỏ giới hạn khi import file excel quá lớn
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    //Được sử dụng để có thể đọc được cả định dạng .xls và xlsx
    public static Workbook getWorkbook(InputStream inputStream, String excelFilePath)
            throws IOException {
        ZipSecureFile.setMinInflateRatio(0);//bỏ giới hạn khi import file excel quá lớn
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    //Được sử dụng để có thể đọc được cả định dạng .xls và xlsx
    public static int getNumberSheetOfFile(InputStream inputStream, String excelFilePath)
            throws IOException {
        int num = 0;
        if (excelFilePath.endsWith("xlsx")) {
            num = new XSSFWorkbook(inputStream).getNumberOfSheets();
        } else if (excelFilePath.endsWith("xls")) {
            num = new HSSFWorkbook(inputStream).getNumberOfSheets();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return num;
    }

    /**
     * Get value of cell, convert to String
     *
     * @param workbook
     * @param cell
     * @return
     */
    public static String getCellValue(Workbook workbook, Cell cell) {
        CellType cellType = cell.getCellType();
        String content = null;
        switch (cellType) {
            case BOOLEAN:
                content = String.valueOf(cell.getBooleanCellValue());
                break;
            case _NONE:
            case BLANK:
                break;
            case FORMULA:
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                try {
                    content = String.valueOf(evaluator.evaluate(cell).getNumberValue());
                } catch (Exception e) {
                    //Bỏ qua lỗi file excel do sai công thức trên excel
                    break;
                }
                break;
            case NUMERIC:
//                        content = String.valueOf(String.format("%.0f", cell.getNumericCellValue()));
                content = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toPlainString();
                if (StringUtils.isNotEmpty(content)) {
                    String key = content.substring(content.length() - 2);
                    if (key.equals(".0")) {
                        content = content.replace(".0", "");
                    }
                }
                break;
            case STRING:
                content = cell.getStringCellValue();
                break;
            case ERROR:
                break;
        }
        return content;
    }
}
