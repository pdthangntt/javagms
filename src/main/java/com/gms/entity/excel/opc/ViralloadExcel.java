package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.OpcBookViralLoadForm;
import com.gms.entity.form.opc_arv.OpcBookViralLoadTableForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author TrangBN
 */
public class ViralloadExcel extends BaseView implements IExportExcel {
    
    private final OpcBookViralLoadForm form;
    private final String extension;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    
    public ViralloadExcel(OpcBookViralLoadForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "So TLVR";
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
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(150));
        
        createRow();
        
        createTitleRow(form.getTitle());
        createTitleDateRow("Thời gian từ " + form.getStartDate() + " đến " + form.getEndDate());
        
        setMerge(1, 1, 0, 24, false);
        setMerge(2, 2, 0, 24, false);
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất sổ", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        int index = getLastRowNumber();
        List<String> headers = new ArrayList<>();
        List<String> secondHeader = new ArrayList<>();
        headers.add("TT");
        headers.add("Họ tên bệnh nhân");
        headers.add("Mã số");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("");
        headers.add("Ngày bắt đầu điều trị ARV");
        headers.add("Lý do xét nghiệm");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Phác đồ ARV \n(bậc 1, bậc 2, bậc 3)");
        headers.add("Ngày lấy mẫu");
        headers.add("Ngày nhận kết quả");
        headers.add("Kết quả XN (bản sao/ml)\n ĐỐI VỚI BN LÀM XN LẦN 1");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Tư vấn\ntuân thủ điều trị");
        headers.add("ĐỐI VỚI BN LÀM XN LẦN 2\nDO KQ LẦN 1 ≥200 BẢN SAO/ML");
        headers.add("");
        headers.add("");
        headers.add("NGÀY CHUYỂN PHÁC ĐỒ SAU KHI\nKHẲNG ĐỊNH THẤT BẠI ĐIỀU TRỊ");
        headers.add("Ghi chú");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));
        
        setMerge(index, index, 4, 5, false);
        setMerge(index, index, 7, 10, false);
        setMerge(index, index, 14, 17, false);
        setMerge(index, index, 19, 21, false);

        secondHeader.add("");
        secondHeader.add("");
        secondHeader.add("");
        secondHeader.add("");
        secondHeader.add("Nam");
        secondHeader.add("Nữ");
        secondHeader.add("");
        secondHeader.add("Sau 12 tháng điều trị ARV");
        secondHeader.add("Định kỳ khác");
        secondHeader.add("Nghi ngờ thất bại điều trị");
        secondHeader.add("PN mang thai PN cho con bú");
        secondHeader.add("");
        secondHeader.add("");
        secondHeader.add("");
        secondHeader.add("Dưới ngưỡng phát hiện");
        secondHeader.add("Từ ngưỡng phát hiện\nđến < 200");
        secondHeader.add("200 - <1000");
        secondHeader.add("≥1000");
        secondHeader.add("");
        secondHeader.add("Ngày làm XN lần 1");
        secondHeader.add("<1000");
        secondHeader.add("≥1000");
        secondHeader.add("");
        secondHeader.add("");
        createTableHeaderRow(secondHeader.toArray(new String[secondHeader.size()]));
        
        setMerge(index, index + 1, 0, 0, false);
        setMerge(index, index + 1, 1, 1, false);
        setMerge(index, index + 1, 2, 2, false);
        setMerge(index, index + 1, 3, 3, false);
        setMerge(index, index + 1, 6, 6, false);
        setMerge(index, index + 1, 11, 11, false);
        setMerge(index, index + 1, 12, 12, false);
        setMerge(index, index + 1, 13, 13, false);
        setMerge(index, index + 1, 18, 18, false);
        setMerge(index, index + 1, 22, 22, false);
        setMerge(index, index + 1, 23, 23, false);
        
        //row table
        List<Object> row;
        int i = 1;
        if (form.getTable() != null && form.getTable().getData() != null) {
            for (OpcBookViralLoadTableForm p : form.getTable().getData()) {
                row = new ArrayList<>();
                row.add(i++);
                row.add(p.getFullName());
                row.add(p.getCode());
                row.add(p.getDob());
                row.add(p.getGenderID().equals(GenderEnum.MALE.getKey()) ? "x" : "");
                row.add(p.getGenderID().equals(GenderEnum.FEMALE.getKey()) ? "x" : "");
                row.add(TextUtils.formatDate(p.getTreatmentTime(), DATE_FORMAT));
                row.add(p.isReasonAfter12Month() ? "x" : "");
                row.add(p.isReasonOtherPeriod() ? "x" : "");
                row.add(p.isReasonFailtreatment() ? "x" : "");
                row.add(p.isReasonPregnant() ? "x" : "");
                row.add(StringUtils.isEmpty(p.getTreatmentRegimentStage()) ? "" : form.getOptions().get("treatment-regimen-stage").get(p.getTreatmentRegimentStage()));
                row.add(p.getSampleTime() == null ? "" : TextUtils.formatDate(p.getSampleTime(), DATE_FORMAT));
                row.add(p.getResultTime() == null ? "" : TextUtils.formatDate(p.getResultTime(), DATE_FORMAT));
                row.add(p.getIdKq1() != null && StringUtils.isNotEmpty(p.getResult()) && (p.getResult().equals("1") || p.getResult().equals("6")) ? "x" : "");
                row.add(p.getIdKq1() != null && StringUtils.isNotEmpty(p.getResult()) && p.getResult().equals("2") ? "x" : "");
                row.add(p.getIdKq1() != null && StringUtils.isNotEmpty(p.getResult()) && p.getResult().equals("3") ? "x" : "");
                row.add(p.getIdKq1() != null && StringUtils.isNotEmpty(p.getResult()) && p.getResult().equals("4") ? "x" : "");
                row.add(StringUtils.isNotEmpty(p.getVisitID()) ? "x" : "");
                row.add(p.getFirstTestTime() == null ? "" : TextUtils.formatDate(p.getFirstTestTime(), DATE_FORMAT));
                row.add(p.getIdKq2() != null &&  StringUtils.isNotEmpty(p.getResult()) &&  !p.getResult().equals("4") ? "x" : "");
                row.add(p.getIdKq2() != null &&  StringUtils.isNotEmpty(p.getResult()) &&  p.getResult().equals("4") ? "x" : "");
                row.add(p.getIdKq2() != null &&  StringUtils.isNotEmpty(p.getResult()) &&  p.getResult().equals("4") ? StringUtils.isNotEmpty(TextUtils.formatDate(p.getRegimenDate(), DATE_FORMAT)) ? TextUtils.formatDate(p.getRegimenDate(), DATE_FORMAT) : "" : "");
                row.add("");
                createTableRow(row.toArray(new Object[row.size()]));
            }
        } else {
            createTableEmptyRow("", 24);
        }
        
        createRow();
        createNormalRow("TỔNG HỢP SỐ LIỆU TRONG THÁNG", true, 0);
        createRow();
        row = new ArrayList<>();
        row.add("Tổng số BN cần làm XN trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getXnTrongThang());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Tổng số BN được làm xét nghiệm lần đầu trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan1());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 được làm XN trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getDtBac2());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 có kết quả XN < 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getArvBac2Duoi1000());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 có kết quả XN >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getArvBac2Tren1000());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan1DuoiNguongPhatHien());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan1Duoi200());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan1F200T1000());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan1From1000());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Tổng số BN XN lần 2 có kết quả >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getXnLan2From1000());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
        
        row = new ArrayList<>();
        row.add("Số BN thất bại điều trị, chuyển phác đồ trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTableMonth().getTbdt());
        row.add("");
        row.add("");
        createTableRow(row.toArray(new Object[row.size()]));
        index = getLastRowNumber()-1;
        setMerge(index, index, 0, 3, false);
        setMerge(index, index, 4, 6, false);
    }        
}
