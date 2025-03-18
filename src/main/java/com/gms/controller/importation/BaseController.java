package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.controller.WebController;
import com.gms.entity.form.ImportForm;
import com.google.common.io.Files;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vvthanh
 */
//@RequestMapping("import")
public abstract class BaseController<T> extends WebController implements IBaseController<T> {

    protected String renderUpload(Model model) {
        model.addAttribute("form", initForm());
        return "importation/upload";
    }

    protected String renderRead(Model model, String path, ImportForm form, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("Vui lòng chọn một tập tin excel để tải lên");
        }
        form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model, 3));
        form.setFileName(file.getOriginalFilename());
        form.setFilePath(fileTmp(file));
        model.addAttribute("form", form);
        model.addAttribute("tab", "read");
        return path;
    }

    protected String renderValidate(Model model, String path, ImportForm form) throws Exception {
        if (form.getMapCols() == null || form.getFilePath() == null) {
            throw new Exception("Dữ liệu không chính xác!");
        }
        File file = new File(form.getFilePath());
        FileInputStream fis = new FileInputStream(file);
        DataInputStream inputStream = new DataInputStream(fis);
        Map<String, String> cols = gson.fromJson(form.getMapCols(), Map.class);
        Map<Integer, List<String>> data = readFile(ExcelUtils.getWorkbook(inputStream, file.getName()), 0, model);

        List<T> items = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
            List<String> cells = entry.getValue();
            items.add(mapping(cols, cells));
        }
        items.remove(0);

        model.addAttribute("items", items);
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("tab", "validate");
        return path;
    }

    protected String renderRead(Model model, ImportForm form, MultipartFile file) throws IOException {
        return renderRead(model, "importation/read", form, file);
    }

    protected String renderView(Model model, String path) {
        model.addAttribute("tab", "view");
        return path;
    }

    protected String fileTemplate(String fileName) {
        return String.format("/excel/%s", fileName);
    }

    protected String fileTmp(MultipartFile file) throws IOException {
        File filePath = Files.createTempDir();
        File tmp = new File(filePath + "/" + file.getOriginalFilename());
        file.transferTo(tmp);
        return tmp.getPath();
    }

    protected Map<Integer, List<String>> readFile(Workbook workbook, int sheetIndex, Model model) {
        return readFile(workbook, sheetIndex, model, -1);
    }

    protected Map<Integer, List<String>> readFile(Workbook workbook, int sheetIndex, Model model, int count) {
        Map<Integer, List<String>> data = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        model.addAttribute("sheet_name", sheet.getSheetName());
        model.addAttribute("sheet_total_row", sheet.getLastRowNum());

        Iterator<Row> iRow = sheet.rowIterator();
        Row row = null;
        Cell cell = null;
        String type = null;
        String value = null;
        int cellIndex = 0;
        while (iRow.hasNext()) {
            row = iRow.next();
            if (count > -1 && row.getRowNum() >= count) {
                break;
            }
            data.put(row.getRowNum(), new ArrayList<String>());
            Iterator<Cell> iCell = row.cellIterator();
            cellIndex = 0;
            while (iCell.hasNext()) {
                cell = iCell.next();
                value = ExcelUtils.getCellValue(workbook, cell);
//                if (cell.getColumnIndex() == 0) {
//                    if (value != null && !"".equals(value)) {
//                        type = value;
//                    } else {
//                        value = type;
//                    }
//                }
                if (cell.getColumnIndex() > cellIndex) {
                    for (int i = cellIndex; i < cell.getColumnIndex() - 1; i++) {
                        data.get(row.getRowNum()).add("");
                    }
                }
                cellIndex = cell.getColumnIndex();
                data.get(row.getRowNum()).add(value);
            }
        }
        return data;
    }

    protected Map<Integer, List<String>> readFileFormattedCell(Workbook workbook, int sheetIndex, Model model, int count) {
        Map<Integer, List<String>> data = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        model.addAttribute("sheet_name", sheet.getSheetName());
        model.addAttribute("sheet_total_row", sheet.getLastRowNum());

        Iterator<Row> iRow = sheet.rowIterator();
        Row row = null;
        Cell cell = null;
        String type = null;
        String value = null;
        int cellIndex = 0;
        while (iRow.hasNext()) {
            row = iRow.next();
            if (count > -1 && row.getRowNum() >= count) {
                break;
            }
            data.put(row.getRowNum(), new ArrayList<String>());
            Iterator<Cell> iCell = row.cellIterator();
            cellIndex = 0;
            while (iCell.hasNext()) {
                cell = iCell.next();
                value = ExcelUtils.getCellValue(workbook, cell);

                try {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = String.valueOf(TextUtils.formatDate(cell.getDateCellValue(), FORMATDATE));
                    }
                } catch (Exception e) {
                }

                if (cell.getColumnIndex() > cellIndex) {
                    for (int i = cellIndex; i < cell.getColumnIndex() - 1; i++) {
                        data.get(row.getRowNum()).add("");
                    }
                }
                cellIndex = cell.getColumnIndex();
                data.get(row.getRowNum()).add(value == null ? null : value.equals("null") ? "" : value);
            }
        }
        return data;
    }

    protected static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected void addExcelError(String attributeName, Row row, Map<String, String> cols, CellStyle style) {
        String key = getKey(cols, attributeName);
        if (key != null && row.getCell(Integer.valueOf(key)) != null) {
            row.getCell(Integer.valueOf(key)).setCellStyle(style);
        }
    }

    protected void addExcelErrorByCol(Row row, int col, CellStyle style) {
        String key = col == 0 ? null : String.valueOf(col);
        if (key != null) {
            row.getCell(Integer.valueOf(key)).setCellStyle(style);
        }
    }

    protected String saveFile(Workbook workbook, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename() == null ? "" : TextUtils.removeDiacritical(file.getOriginalFilename().replaceAll(",", ""));

        File outputFile = File.createTempFile(Files.createTempDir() + "/" + fileName, fileName.endsWith("xlsx") ? ".xlsx" : ".xls");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }
        return java.util.Base64.getEncoder().encodeToString(outputFile.getPath().getBytes());
    }
}
