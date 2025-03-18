package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcBookPreArvForm;
import com.gms.entity.form.opc_arv.OpcPreArvChildForm;
import com.gms.entity.form.opc_arv.OpcPreArvTableForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class PreArvExcel extends BaseView implements IExportExcel {
    private final OpcBookPreArvForm form;
    private final String extension;

    public PreArvExcel(OpcBookPreArvForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "So preArv";
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
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(500));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(20, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(21, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(23, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(24, PixelUtils.pixel2WidthUnits(200));

        setMerge(1, 1, 0, 24, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Năm", form.getYear(), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất sổ", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        int index = getLastRowNumber();
        createTableHeaderRow("Số TT", "Thông tin chung","","","","","","Thời điểm đăng ký","","","","Thời điểm điều trị bằng thuốc kháng HIV", "",
                "Theo dõi người bệnh trước điều trị bằng thuốc kháng HIV", "","","","","","","","","","","");
        createTableHeaderRow("", "Ngày đăng ký","Họ và tên","Mã số bệnh án","Năm sinh","","Địa chỉ","Tình trạng khi đăng ký",
                "Giai đoạn LS","CD4 hoặc CD4%","Cân nặng /Chiều cao","Ngày đủ tiêu chuẩn điều trị",
                "Ngày bắt đầu điều trị", String.format("Năm %s", Integer.parseInt(form.getYear())),"","","", String.format("Năm %s", Integer.parseInt(form.getYear()) + 1),"","","", String.format("Năm %s", Integer.parseInt(form.getYear()) + 2),"","","");
        createTableHeaderRow("", "","","","Nam","Nữ","","","","","","", "","Quý 1","Quý 2","Quý 3", "Quý 4","Quý 1","Quý 2","Quý 3", "Quý 4","Quý 1","Quý 2","Quý 3", "Quý 4");
        createTableHeaderRow("(1)", "(2)", "(3)", "(4)", "(5)", "(6)", "(7)", "(8)", "(9)", "(10)", "(11)", "(12)", "(13)", "(14)", "(15)", "(16)", "(17)", "(18)", "(19)", "(20)", "(21)", "(22)", "(23)", "(24)", "(25)");

        setMerge(index, index + 2, 0, 0, false);
        setMerge(index, index, 1, 6, false);
        setMerge(index, index, 7, 10, false);
        setMerge(index, index, 11, 12, false);
        setMerge(index, index, 13, 24, false);
        
        setMerge(index + 1, index + 2, 1, 1, false);
        setMerge(index + 1, index + 2, 2, 2, false);
        setMerge(index + 1, index + 2, 3, 3, false);
        setMerge(index + 1, index + 2, 6, 6, false);
        setMerge(index + 1, index + 2, 7, 7, false);
        setMerge(index + 1, index + 2, 8, 8, false);
        setMerge(index + 1, index + 2, 9, 9, false);
        setMerge(index + 1, index + 2, 10, 10, false);
        setMerge(index + 1, index + 2, 11, 11, false);
        setMerge(index + 1, index + 2, 12, 12, false);
        
        setMerge(index + 1 , index + 1, 4, 5, false);
        setMerge(index + 1 , index + 1, 13, 16, false);
        setMerge(index + 1 , index + 1, 17, 20, false);
        setMerge(index + 1 , index + 1, 21, 24, false);
        int stt = 1;
        List<String> typeTop = new ArrayList();
        typeTop.add("CĐ"); 
        typeTop.add("cd4"); 
        typeTop.add("TV"); 
        typeTop.add("B"); 
        
        List<String> typeBot = new ArrayList();
        typeBot.add("ctx"); 
        typeBot.add("inh"); 
        if (form.getDataPage() != null && form.getDataPage().getData() != null && form.getDataPage().getData().size() > 0) {
            for (OpcPreArvTableForm item : form.getDataPage().getData()) {
                int rowNum = getLastRowNumber();
                if(StringUtils.isNotEmpty(item.getArvID())){
                    createTableRow(stt ++, item.getRegistrationTime(), item.getFullName(), item.getCode(),
                        StringUtils.isNotEmpty(item.getGenderID()) && "1".equals(item.getGenderID()) ? item.getDob() : "", 
                        StringUtils.isNotEmpty(item.getGenderID()) && "2".equals(item.getGenderID()) ? item.getDob() : "", 
                        item.getPermanentAddressFull(),
                        StringUtils.isNotEmpty(item.getRegistrationStatus()) ? form.getOptions().get(ParameterEntity.ARV_REGISTRATION_STATUS).get(item.getRegistrationStatus()) : "",
                        StringUtils.isNotEmpty(item.getClinicalStage()) ? form.getOptions().get(ParameterEntity.CLINICAL_STAGE).get(item.getClinicalStage()) : "", 
                        item.getCd4(),
                        item.getPatientWeight() == 0.0 ? "" : (item.getPatientWeight() + " kg"), 
                        "", 
                        item.getTreatmentTime(), 
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + "", "1", typeTop), 
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + "", "2", typeTop), 
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + "", "3", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + "", "4", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "1", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "2", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "3", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "4", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "1", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "2", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "3", typeTop),
                        displayChildTop(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "4", typeTop));
                    createTableRow ("", "", "", "", "", "", "", "", "", "", 
                            item.getPatientHeight() == 0.0 ? "" : (item.getPatientHeight() + " cm"), "", "",
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + "", "1", typeBot), 
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + "", "2", typeBot), 
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + "", "3", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + "", "4", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "1", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "2", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "3", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 1 + "", "4", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "1", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "2", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "3", typeBot),
                        displayChildBot(item.getChilds(), Integer.parseInt(form.getYear()) + 2 + "", "4", typeBot));
                    setMerge(rowNum, rowNum + 1, 0, 0, false);
                    setMerge(rowNum, rowNum + 1, 1, 1, false);
                    setMerge(rowNum, rowNum + 1, 2, 2, false);
                    setMerge(rowNum, rowNum + 1, 3, 3, false);
                    setMerge(rowNum, rowNum + 1, 4, 4, false);
                    setMerge(rowNum, rowNum + 1, 5, 5, false);
                    setMerge(rowNum, rowNum + 1, 6, 6, false);
                    setMerge(rowNum, rowNum + 1, 7, 7, false);
                    setMerge(rowNum, rowNum + 1, 8, 8, false);
                    setMerge(rowNum, rowNum + 1, 9, 9, false);
//                    setMerge(rowNum, rowNum + 1, 10, 10, false);
                    setMerge(rowNum, rowNum + 1, 11, 11, false);
                    setMerge(rowNum, rowNum + 1, 12, 12, false);
                }
                
            }
        }
    }
    
    public String displayChildTop(List<OpcPreArvChildForm> childs, String year, String quarter, List<String> types){
        StringBuilder result = new StringBuilder("");
        if(childs != null && childs.size() > 0){
            for(OpcPreArvChildForm child : childs){
                if(child.getYear() == null || child.getQuarter() == null || child.getType() == null){
                    continue;
                }
                if(child.getYear().equals(year) && child.getQuarter().equals(quarter) && types.contains(child.getType())){
                    if(child.getType().equals("cd4") && StringUtils.isNotEmpty(child.getResult())){
                        result.append(String.format("%s \n (%s \n %s) \n", child.getType().toUpperCase(), child.getTime(), child.getResult()));
                    } else {
                        result.append(String.format("%s \n (%s) \n", child.getType().toUpperCase(), child.getTime()));
                    }
                }
            }
        }
        return result.toString();
    }
    
    public String displayChildBot(List<OpcPreArvChildForm> childs, String year, String quarter, List<String> types){
        StringBuilder result = new StringBuilder("");
        if(childs != null && childs.size() > 0){
            for(OpcPreArvChildForm child : childs){
                if(child.getYear() == null || child.getQuarter() == null || child.getType() == null){
                    continue;
                }
                if(child.getYear().equals(year) && child.getQuarter().equals(quarter) && types.contains(child.getType())){
                    result.append(String.format("%s \n (%s) \n", child.getType().toUpperCase(), child.getTime()));
                }
            }
        }
        return result.toString();
    }
}
