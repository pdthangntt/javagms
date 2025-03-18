package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.ViralloadManagerForm;
import com.gms.entity.form.opc_arv.ViralloadManagerTable;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class ViralManagerExcel extends BaseView implements IExportExcel {

    private ViralloadManagerForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public ViralManagerExcel(ViralloadManagerForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "VIRALLOAD";
    }

    public ViralManagerExcel(ViralloadManagerForm data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();
        createTable02();

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
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 16, false);

        createRow();
        createTitleRow("Báo cáo tải lượng virus HIV".toUpperCase());
        createRow();

        int cellIndexFilter = 1;
//        createFilterRow("Loại báo cáo", form.getTitle(), cellIndexFilter);
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất BC", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        createRow();
        int index = getLastRowNumber();

        List<String> headers = new ArrayList<>();
        List<String> secondHeaders = new ArrayList<>();
        headers.add("STT");
        headers.add("Tên PKNT");
        headers.add("Số mẫu XN TLHIV bậc 1");
        headers.add("Số mẫu XN TLHIV bậc 2");
        headers.add("Thời điểm - lý do XN cho BN");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Kết quả xét nghiệm (cp/ml) BN làm xét nghiệm lần 1");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Tư vấn tăng cường tuân thủ điều trị");
        headers.add("Đối với BN làm XN lần 2 do kết quả lần 1 > 1000 cp/ml");
        headers.add("");
        headers.add("Thất bại điều trị chuyển phác đồ");

        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        secondHeaders.add("");
        secondHeaders.add("");
        secondHeaders.add("");
        secondHeaders.add("");
        secondHeaders.add("ĐT ARV tại thời điểm 6 tháng	");
        secondHeaders.add("XN định kỳ sau 12 tháng");
        secondHeaders.add("XN định kỳ khác");
        secondHeaders.add("Có biểu hiện nghi ngờ TBĐT");
        secondHeaders.add("PN mang thai/PN cho con bú");
        secondHeaders.add("Dưới ngưỡng phát hiện");
        secondHeaders.add("Từ ngưỡng phát hiện đến < 200");
        secondHeaders.add("200 - 1000");
        secondHeaders.add("> 1000");
        secondHeaders.add("");
        secondHeaders.add("< 1000");
        secondHeaders.add(">= 1000");
        secondHeaders.add("");

        createTableHeaderRow(secondHeaders.toArray(new String[secondHeaders.size()]));

        setMerge(index, index + 1, 0, 0, true);
        setMerge(index, index + 1, 1, 1, true);
        setMerge(index, index + 1, 2, 2, true);
        setMerge(index, index + 1, 3, 3, true);
        setMerge(index, index, 4, 8, true);
        setMerge(index, index, 9, 12, true);
        setMerge(index, index + 1, 13, 13, true);
        setMerge(index, index, 14, 15, true);
        setMerge(index, index + 1, 16, 16, true);

        //row table
        List<Object> row;
        int i = 1;
        for (Map.Entry<Long, ViralloadManagerTable> entry : form.getTable().entrySet()) {
            ViralloadManagerTable item = entry.getValue();
            row = new ArrayList<>();
            row.add(i++);
            row.add(form.getOptions().get("siteOpcFrom").getOrDefault(entry.getKey() + "", "#" + entry.getKey()));
            row.add(item.getXnbac1());
            row.add(item.getXnbac2());
            row.add(item.getXn6thang());
            row.add(item.getXn12thang());
            row.add(item.getXnkhac());
            row.add(item.getTbdt());
            row.add(item.getPnmt());
            row.add(item.getXnLan1phatHien());
            row.add(item.getXnLan1Duoi200());
            row.add(item.getXnLan1Tu200Den1000());
            row.add(item.getXnLan1Tren1000());
            row.add(item.getTuvan());
            row.add(item.getXnLan2duoi1000());
            row.add(item.getXnLan2tren1000());
            row.add(item.getThatbaiphacdo());
            createTableRow(row.toArray(new Object[row.size()]));
        }

        int indexTotal = getLastRowNumber();
        row = new ArrayList<>();
        row.add("Tổng cộng");
        row.add("");

        row.add(form.getSumTable().getXnbac1());
        row.add(form.getSumTable().getXnbac2());
        row.add(form.getSumTable().getXn6thang());
        row.add(form.getSumTable().getXn12thang());
        row.add(form.getSumTable().getXnkhac());
        row.add(form.getSumTable().getTbdt());
        row.add(form.getSumTable().getPnmt());
        row.add(form.getSumTable().getXnLan1phatHien());
        row.add(form.getSumTable().getXnLan1Duoi200());
        row.add(form.getSumTable().getXnLan1Tu200Den1000());
        row.add(form.getSumTable().getXnLan1Tren1000());
        row.add(form.getSumTable().getTuvan());
        row.add(form.getSumTable().getXnLan2duoi1000());
        row.add(form.getSumTable().getXnLan2tren1000());
        row.add(form.getSumTable().getThatbaiphacdo());
        createTableRow(row.toArray(new Object[row.size()]));

        setMerge(indexTotal, indexTotal, 0, 1, true);

    }

    private void createTable02() {
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow("TỔNG HỢP SỐ LIỆU TRONG THÁNG", HorizontalAlignment.LEFT, 0);

        int indexTotal = getLastRowNumber();
        //row table
        List<Object> row = new ArrayList<>();

        row = new ArrayList<>();
        row.add("Tổng số BN cần làm XN trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getXnTrongThang());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Tổng số BN được làm xét nghiệm lần đầu trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan1phatHien()
                + form.getSumTable().getXnLan1Duoi200()
                + form.getSumTable().getXnLan1Tu200Den1000()
                + form.getSumTable().getXnLan1Tren1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 được làm XN trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnbac2());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 có kết quả XN < 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getArvBac2Duoi1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN điều trị ARV bậc 2 có kết quả XN >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getTable02().getArvBac2Tren1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan1phatHien());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan1Duoi200());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan1Tu200Den1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN làm XN lần 1 có KQXN >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan1Tren1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Tổng số BN XN lần 2 có kết quả >= 1000 bản sao/ml");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getXnLan2tren1000());
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("Số BN thất bại điều trị, chuyển phác đồ trong tháng");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add(form.getSumTable().getThatbaiphacdo());
        createTableRow(row.toArray(new Object[row.size()]));

        int indexRow = 0;
        int indexRow2 = 0;

        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);
        setMerge(indexTotal + indexRow++, indexTotal + indexRow2++, 0, 5, true);

    }

}
