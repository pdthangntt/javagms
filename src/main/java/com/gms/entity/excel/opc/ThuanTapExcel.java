package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.ThuanTapForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class ThuanTapExcel extends BaseView implements IExportExcel  {
    private ThuanTapForm form;
    private String extension;

    public ThuanTapExcel(ThuanTapForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "BC Thuan Tap";
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

    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(300));
        
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(50));
        
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(50));
        
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(20, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(21, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(23, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(24, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(25, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(26, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(27, PixelUtils.pixel2WidthUnits(50));
        
        sheet.setColumnWidth(28, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(29, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(30, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(31, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(32, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(33, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(34, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(35, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(36, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(37, PixelUtils.pixel2WidthUnits(50));
        
        sheet.setColumnWidth(38, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(39, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(40, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(41, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(42, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(43, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(44, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(45, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(46, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(47, PixelUtils.pixel2WidthUnits(50));

        setMerge(1, 1, 0, 51, false);
        
        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
//        createFilterRow("Kỳ báo cáo", String.format("Năm %s từ %s đến %s", form.getMonth() < 10 ? "0" + form.getMonth() : form.getMonth(), form.getYear(), form.getStartDate(), form.getEndDate()), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getSiteLabel())) {
            createFilterRow("Cơ sở điều trị", form.getSiteLabel(), cellIndexFilter);
        }
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        int rowIndex = getLastRowNumber();
        createTableHeaderRow("TT", "Năm báo cáo: " + form.getYear() ,"Tổng năm","","","","","","","","","", "Quý 1","","","","","","","","","","Quý 2","","","","","","","","","", "Quý 3","","","","","","","","","","Quý 4","","","","","","","","","");
        createTableHeaderRow("","", String.format("%s %s", "Năm", String.valueOf(form.getYear() -1)),"","","","",String.format("%s %s", "Năm", String.valueOf(form.getYear())),"","","","", String.format("%s %s", "Năm", String.valueOf(form.getYear() -1)),"","","","",String.format("%s %s", "Năm", String.valueOf(form.getYear())),"","","","", String.format("%s %s", "Năm", String.valueOf(form.getYear() -1)),"","","","",String.format("%s %s", "Năm", String.valueOf(form.getYear())),"","","","", String.format("%s %s", "Năm", String.valueOf(form.getYear() -1)),"","","","",String.format("%s %s", "Năm", String.valueOf(form.getYear())),"","","","", String.format("%s %s", "Năm", String.valueOf(form.getYear()-1)),"","","","",String.format("%s %s", "Năm", String.valueOf(form.getYear())),"","","","");
        createTableHeaderRow("","Nhóm bệnh nhân theo dõi", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng",  "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng" ,"BN < 15 tuổi","","BN > 15 tuổi","","Tổng", "BN < 15 tuổi","","BN > 15 tuổi","","Tổng");
        createTableHeaderRow("","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","", "Nam","Nữ","Nam","Nữ","");
        
        setMerge(rowIndex, rowIndex + 3, 0, 0, false);
        setMerge(rowIndex, rowIndex + 1, 1, 1, false);
        setMerge(rowIndex, rowIndex, 2, 11, false);
        setMerge(rowIndex, rowIndex, 12, 21, false);
        setMerge(rowIndex, rowIndex, 22, 31, false);
        setMerge(rowIndex, rowIndex, 32, 41, false);
        setMerge(rowIndex, rowIndex, 42, 51, false);
        
        setMerge(rowIndex + 1, rowIndex + 1, 2, 6, false);
        setMerge(rowIndex + 1, rowIndex + 1, 7, 11, false);
        setMerge(rowIndex + 1, rowIndex + 1, 12, 16, false);
        setMerge(rowIndex + 1, rowIndex + 1, 17, 21, false);
        
        setMerge(rowIndex + 1, rowIndex + 1, 22, 26, false);
        setMerge(rowIndex + 1, rowIndex + 1, 27, 31, false);
        setMerge(rowIndex + 1, rowIndex + 1, 32, 36, false);
        setMerge(rowIndex + 1, rowIndex + 1, 37, 41, false);
        setMerge(rowIndex + 1, rowIndex + 1, 42, 46, false);
        setMerge(rowIndex + 1, rowIndex + 1, 47, 51, false);
        
        setMerge(rowIndex + 2, rowIndex + 3, 1, 1, false);
        setMerge(rowIndex + 2, rowIndex + 2, 2, 3, false);
        setMerge(rowIndex + 2, rowIndex + 2, 4, 5, false);
        setMerge(rowIndex + 2, rowIndex + 3, 6, 6, false);
        setMerge(rowIndex + 2, rowIndex + 2, 7, 8, false);
        setMerge(rowIndex + 2, rowIndex + 2, 9, 10, false);
        setMerge(rowIndex + 2, rowIndex + 3, 11, 11, false);
        
        setMerge(rowIndex + 2, rowIndex + 2, 12, 13, false);
        setMerge(rowIndex + 2, rowIndex + 2, 14, 15, false);
        setMerge(rowIndex + 2, rowIndex + 3, 16, 16, false);
        setMerge(rowIndex + 2, rowIndex + 2, 17, 18, false);
        setMerge(rowIndex + 2, rowIndex + 2, 19, 20, false);
        setMerge(rowIndex + 2, rowIndex + 3, 21, 21, false);
        
        setMerge(rowIndex + 2, rowIndex + 2, 22, 23, false);
        setMerge(rowIndex + 2, rowIndex + 2, 24,25, false);
        setMerge(rowIndex + 2, rowIndex + 3, 26, 26, false);
        setMerge(rowIndex + 2, rowIndex + 2, 27, 28, false);
        setMerge(rowIndex + 2, rowIndex + 2, 29, 30, false);
        setMerge(rowIndex + 2, rowIndex + 3, 31, 31, false);
        
        setMerge(rowIndex + 2, rowIndex + 2, 32, 33, false);
        setMerge(rowIndex + 2, rowIndex + 2, 34, 35, false);
        setMerge(rowIndex + 2, rowIndex + 3, 36, 36, false);
        setMerge(rowIndex + 2, rowIndex + 2, 37, 38, false);
        setMerge(rowIndex + 2, rowIndex + 2, 39, 40, false);
        
        setMerge(rowIndex + 2, rowIndex + 3, 46, 46, false);
        setMerge(rowIndex + 2, rowIndex + 3, 51, 51, false);
        
        List<Object> row = new ArrayList<>();
        // Chỉ tiêu 1
        row.add("1");
        row.add("Số bệnh nhân BẮT ĐẦU điều trị ARV tại cơ sở (Số bệnh nhân GỐC ban đầu)");
        // Tổng năm
        int sum = 0;
        int temp = 0;
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        // Quý 1
        sum = 0;
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        // Quý 2
        sum = 0;
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        // Quý 3
        sum = 0;
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        // Quý 4
        sum = 0;
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct1").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct1
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 2, 6, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 16, 16, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 26, 26, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 36, 36, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 46, 46, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 12, 15, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 22, 25, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 32, 35, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 42, 45, IndexedColors.YELLOW);
        
        // ===========Ct 2==================
        row = new ArrayList<>();
        row.add("2");
        row.add("Số bệnh nhân CHUYỂN TỚI trong giai đoạn theo dõi");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct2").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct2
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 21, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 31, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 41, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 51, 51, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 20, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 27, 30, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 37, 40, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 47, 50, IndexedColors.YELLOW);

        // ===========Ct 3==================
        row = new ArrayList<>();
        row.add("3");
        row.add("Số bệnh nhân CHUYỂN ĐI trong giai đoạn theo dõi");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct3").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct3
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 21, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 31, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 41, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 51, 51, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 20, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 27, 30, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 37, 40, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 47, 50, IndexedColors.YELLOW);
        
        // ===========Ct 4==================
        row = new ArrayList<>();
        row.add("4");
        row.add("Số bệnh nhân THỰC TẾ phải theo dõi (THỰC TẾ = BAN ĐẦU + CHUYỂN TỚI - CHUYỂN ĐI)");
        // Tổng năm
        sum = 0;
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        
        sum = 0;
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen()));
        sum += temp;
        row.add(formatMinus(sum));
        // Quý 1
        sum = 0;
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        sum = 0;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen()));
        sum += temp;
        
        row.add(formatMinus(sum));
        // Quý 2
        sum = 0;
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        sum = 0;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen()));
        sum += temp;
        row.add(formatMinus(sum));
        
        // Quý 3
        sum = 0;
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        sum = 0;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen()));
        sum += temp;
        row.add(formatMinus(sum));
        // Quý 4
        sum = 0;
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear() - 1), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        sum = 0;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen()));
        sum += temp;
        
        temp = form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen();
        row.add(formatMinus(form.getDataResults().get("ct4").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen()));
        sum += temp;
        
        row.add(formatMinus(sum));
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct4
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 2, 51, IndexedColors.GREEN);
        
        // ===========Ct5==================
        row = new ArrayList<>();
        row.add("5");
        row.add("Số bệnh nhân CÒN SỐNG VÀ ĐANG TIẾP TỤC điều trị tại thời điểm tháng thứ 12");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct5").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct5
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 27, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 37, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 47, 51, IndexedColors.GREEN);
        
        // ===========Ct6==================
        row = new ArrayList<>();
        row.add("6");
        row.add("Số bệnh nhân phải NGỪNG/DỪNG điều trị đến hết thời điểm tháng thứ 12");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct6").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct6
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 21, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 31, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 41, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 51, 51, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 20, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 27, 30, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 37, 40, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 47, 50, IndexedColors.YELLOW);
        
        // ===========Ct7==================
        row = new ArrayList<>();
        row.add("7");
        row.add("Số bệnh nhân TỬ VONG");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct7").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct7
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 21, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 31, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 41, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 51, 51, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 20, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 27, 30, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 37, 40, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 47, 50, IndexedColors.YELLOW);
        
        // ===========Ct8==================
        row = new ArrayList<>();
        row.add("8");
        row.add("Số bệnh nhân BỎ TRỊ (không theo dõi được)");
        // Tổng năm
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "total", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 1
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "0", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 2
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "1", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 3
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "2", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        // Quý 4
        sum = 0;
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(0);
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getUnderFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.MALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(form.getDataResults().get("ct8").get(String.format("%s-%s-%s", "3", String.valueOf(form.getYear()), GenderEnum.FEMALE.getKey())).getOverFifteen());
        sum += Integer.parseInt(row.get(row.size()-1).toString());
        row.add(sum);
        createTableRow(row.toArray(new Object[row.size()]));
        // Bôi màu ct8
        rowIndex = getLastRowNumber() - 1;
        setColors(rowIndex, rowIndex, 7, 11, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 21, 21, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 31, 31, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 41, 41, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 51, 51, IndexedColors.GREEN);
        setColors(rowIndex, rowIndex, 17, 20, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 27, 30, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 37, 40, IndexedColors.YELLOW);
        setColors(rowIndex, rowIndex, 47, 50, IndexedColors.YELLOW);
        
    }
    
    /**
     * Format số âm
     * 
     * @param in
     * @return 
     */
    private String formatMinus(int in){
        return in < 0 ? String.format("(%s)", in * -1) : String.valueOf(in);
    }
    
}
