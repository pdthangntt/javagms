package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.ViralloadForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class ViralSiteExcel extends BaseView implements IExportExcel {

    private ViralloadForm form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public ViralSiteExcel(ViralloadForm form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "VIRALLOADSITE";
    }

    public ViralSiteExcel(ViralloadForm data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        createTable();

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
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(600));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(120));

        setMerge(1, 1, 0, 4, false);

        createRow();
        createTitleRow("Báo cáo tải lượng virus HIV".toUpperCase());
        createRow();

        int cellIndexFilter = 0;
//        createFilterRow("Loại báo cáo", form.getTitle(), cellIndexFilter);
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất BC", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        getCurrentSheet();
        createRow();

        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Nội dung");
        headers.add("Số lượng");

        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //row table
        createTableRowBold("I", "Kết quả xét nghiệm tải lượng virus của tất cả BN đang ĐT ARV (bậc 1, bậc 2, trẻ em, người lớn)", "");
        createTableRowBold("1", "Tổng số BN cần làm XN trong tháng", form.getTable().getXnTrongThang());
        createTableRowBold("2", "Tổng số BN được làm xét nghiệm lần 1 trong tháng", form.getTable().getXnLan1());
        createTableRow("2.1", "Số BN có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml", form.getTable().getXnLan1Duoi200());
        createTableRow("2.2", "Số BN có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml", form.getTable().getXnLan1Tu200Den1000());
        createTableRow("2.3", "Số BN có KQXN >= 1000 bản sao/ml", form.getTable().getXnLan1Tren1000());
        createTableRowBold("", "Tổng số BN được làm lại xét nghiệm lần 2", form.getTable().getXnLan2());
        createTableRow("", "Số BN có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml", form.getTable().getXnLan2Duoi200());
        createTableRow("", "Số BN có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml", form.getTable().getXnLan2Tu200Den1000());
        createTableRow("", "Số BN có KQXN >= 1000 bản sao/ml", form.getTable().getXnLan2Tren1000());
        createTableRowBold("II", "Kết quả xét nghiệm tải lượng HIV trong BN điều trị ARV bậc 2", "");
        createTableRowBold("3", "Số BN điều trị ARV bậc 2 được làm XN trong tháng", form.getTable().getArvBac2());
        createTableRow("3.1", "Số BN có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml", form.getTable().getArvBac2Duoi200());
        createTableRow("3.2", "Số BN có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml", form.getTable().getArvBac2Tu200Den1000());
        createTableRow("3.3", "Số BN có KQXN >= 1000 bản sao/ml", form.getTable().getArvBac2Tren1000());
        createTableRowBold("III", "Số bệnh nhân được xét nghiệm định kỳ tại thời điểm 12 tháng kể từ khi bắt đầu điều trị", "");
        createTableRow("4.1", "Số BN có KQXN dưới ngưỡng phát hiện đến dưới 200 bản sao/ml", form.getTable().getXn12ThangDuoi200());
        createTableRow("4.2", "Số BN có KQXN từ 200 bản sao đến dưới 1000 bản sao/ml", form.getTable().getXn12ThangTu200Den1000());
        createTableRow("4.3", "Số BN có KQXN >= 1000 bản sao/ml", form.getTable().getXn12ThangTren1000());

    }

}
