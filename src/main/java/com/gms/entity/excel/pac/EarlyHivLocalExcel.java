package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacEarlyHivLocalForm;
import com.gms.entity.form.pac.TablePacDeadForm;
import com.gms.entity.form.pac.TablePacEarlyHivForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * 
 * @author pdThang
 */
public class EarlyHivLocalExcel extends BaseView implements IExportExcel {

    private PacEarlyHivLocalForm form;
    private String extension;

    public EarlyHivLocalExcel(PacEarlyHivLocalForm form, String extension) {
        this.form = form;
        this.extension = extension;
        this.sheetName = "Báo cáo nhiễm mới";
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
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(90));

        setMerge(1, 1, 0, 5, false);

        //Dòng đầu tiên để trắng
        Row row = createRow();

        row = createRow();
        Cell cell = row.createCell(0);
        cell.setCellValue(form.getTitle());
        setBold(cell);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        if (!"".equals(form.getStartDate()) && !"".equals(form.getEndDate())) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Báo cáo từ %s đến %s", form.getStartDate(), form.getEndDate()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        
        if (form.getTitleLocation() != null && !form.getTitleLocation().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Địa chỉ: %s", form.getTitleLocation()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        
        if (form.getSearchDistrict()!= null && !form.getSearchDistrict().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Quận/huyện: %s", form.getSearchDistrict()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchWard()!= null && !form.getSearchWard().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Phường/xã: %s", form.getSearchWard()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchGender()!= null && !form.getSearchGender().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Giới tính: %s", form.getSearchGender()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchAge()!= null && !form.getSearchAge().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Độ tuổi: %s", form.getSearchAge()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchJob()!= null && !form.getSearchJob().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Nghề nghiệp: %s", form.getSearchJob()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchObject()!= null && !form.getSearchObject().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Nhóm đối tượng: %s", form.getSearchObject()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
//        if (form.getSearchRisk()!= null && !form.getSearchRisk().isEmpty()) {
//            row = createRow();
//            cell = row.createCell(0);
//            cell.setCellValue(String.format("Nguy cơ lấy nhiễm: %s", form.getSearchRisk()));
//            setFont(cell, fontSize, false, false, Font.U_NONE);
//            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
//        }
        if (form.getSearchBloodBase()!= null && !form.getSearchBloodBase().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Nơi lấy máu: %s", form.getSearchBloodBase()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchTreatment()!= null && !form.getSearchTreatment().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Trạng thái điều trị: %s", form.getSearchTreatment()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }
        if (form.getSearchResident()!= null && !form.getSearchResident().isEmpty()) {
            row = createRow();
            cell = row.createCell(0);
            cell.setCellValue(String.format("Hiện trạng cư trú: %s", form.getSearchResident()));
            setFont(cell, fontSize, false, false, Font.U_NONE);
            cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
        }

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Tên đơn vị: %s", form.getSiteName()));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);

        row = createRow();
        cell = row.createCell(0);
        cell.setCellValue(String.format("Ngày xuất báo cáo: %s", TextUtils.formatDate(new Date(), "dd/MM/yyyy")));
        setFont(cell, fontSize, false, false, Font.U_NONE);
        cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
    }

    private void createTable() {
        Sheet currentSheet = getCurrentSheet();

        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();//dùng để merge khi không có thông tin
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
        cell.setCellValue("TT");
        cell.setCellStyle(th);
        setBorders(cell);

        cell = createCell(row, 1);
        cell.setCellValue("Đơn vị báo cáo");
        cell.setCellStyle(th);

        cell = createCell(row, 2);
        cell.setCellValue("Nam");
        cell.setCellStyle(th);

        cell = createCell(row, 3);
        cell.setCellValue("Nữ");
        cell.setCellStyle(th);

        cell = createCell(row, 4);
        cell.setCellValue("Không rõ");
        cell.setCellStyle(th);

        cell = createCell(row, 5);
        cell.setCellValue("Tổng");
        cell.setCellStyle(th);

        // Load data records
        int totalMale = 0;
        int totalFemale = 0;
        int totalOther = 0;
        int totalTotal = 0;
        int index = 0;
        int x = 0;
        if (form.getTable() != null) {
            for (TablePacEarlyHivForm model : form.getTable()) {
                int i = 0;
                x = x + 1;

                row = createRow();

                cell = createCell(row, i);
                if ("".equals(model.getWardName()) || model.getWardName() == null) {
                    setBold(cell);
                }
                setBorders(cell);
                cell.setCellValue(model.getStt() == 0 ? "" : model.getStt() + "");
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                if ("".equals(model.getWardName()) || model.getWardName() == null) {
                    setBold(cell);
                }
                setBorders(cell);
                cell.setCellValue("".equals(model.getWardName()) || model.getWardName() == null ? model.getDistrictName() : "   " + model.getWardName());
                cell.getCellStyle().setAlignment(HorizontalAlignment.LEFT);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                if ("".equals(model.getWardName()) || model.getWardName() == null) {
                    setBold(cell);
                }
                setBorders(cell);
                cell.setCellValue((("".equals(model.getWardName()) || model.getWardName() == null) && (form.isIsTYT() || form.isSerchWard())) ? " " : String.valueOf(model.getMale()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                if ("".equals(model.getWardName()) || model.getWardName() == null) {
                    setBold(cell);
                }
                setBorders(cell);
                cell.setCellValue((("".equals(model.getWardName()) || model.getWardName() == null) && (form.isIsTYT() || form.isSerchWard())) ? " " : String.valueOf(model.getFemale()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                if ("".equals(model.getWardName()) || model.getWardName() == null) {
                    setBold(cell);
                }
                setBorders(cell);
                cell.setCellValue((("".equals(model.getWardName()) || model.getWardName() == null) && (form.isIsTYT() || form.isSerchWard())) ? " " : String.valueOf(model.getOther()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                cell = createCell(row, i += 1);
                setBold(cell);
                setBorders(cell);
                cell.setCellValue((("".equals(model.getWardName()) || model.getWardName() == null) && (form.isIsTYT() || form.isSerchWard())) ? " " : String.valueOf(model.getTotal()));
                cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

                //Tính tổng
                if (!form.isIsTYT() && form.isSerchWard() == false) {
                    if ("".equals(model.getWardName()) || model.getWardName() == null) {
                        index += 1;
                        x = 0;
                        totalMale = totalMale + model.getMale();
                        totalFemale = totalFemale + model.getFemale();
                        totalOther = totalOther + model.getOther();
                        totalTotal = totalTotal + model.getTotal();
                    }
                }
                if (form.isIsTYT() || form.isSerchWard()) {
                    if (!"".equals(model.getWardName()) && model.getWardName() != null) {
                        index += 1;
                        x = 0;
                        totalMale = totalMale + model.getMale();
                        totalFemale = totalFemale + model.getFemale();
                        totalOther = totalOther + model.getOther();
                        totalTotal = totalTotal + model.getTotal();
                    }
                }
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
            cell.setCellValue(totalMale);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            cell = createCell(row, 3);
            setBold(cell);
            setBorders(cell);
            cell.setCellValue(totalFemale);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            cell = createCell(row, 4);
            setBold(cell);
            setBorders(cell);
            cell.setCellValue(totalOther);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

            cell = createCell(row, 5);
            setBold(cell);
            setBorders(cell);
            cell.setCellValue(totalTotal);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        }

    }
}
