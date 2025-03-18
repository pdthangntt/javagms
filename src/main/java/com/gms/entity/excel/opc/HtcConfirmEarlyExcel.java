package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.EarlyHtcConfirmReportForm;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class HtcConfirmEarlyExcel extends BaseView implements IExportExcel {

    private EarlyHtcConfirmReportForm form;
    private String extension;

    public HtcConfirmEarlyExcel(EarlyHtcConfirmReportForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "Sổ xét nghiệm";
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
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

//        setMerge(1, 1, 0, 7, false);
//
//        createRow();
//        createTitleRow(form.getTitle());
//        createRow();
//
//        int cellIndexFilter = 1;
//        createFilterRow("Ngày XN k", String.format("từ %s đến %s", form.getStartDate(), form.getEndDate()), cellIndexFilter);
//        String donvi = "";
//        if (form.getSites() != null && !form.getSites().isEmpty()) {
//            for (String site : form.getSites()) {
//                donvi = donvi + form.getSiteOptions().getOrDefault(site, "") + ", ";
//            }
//            StringBuilder sb = new StringBuilder(donvi);
//            sb.deleteCharAt(sb.length() - 2);
//            donvi = sb.toString();
//            createFilterRow("Đơn vị", donvi, cellIndexFilter);
//        }
//
//        createRow();
//        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
//        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
//        createRow();

        createTableHeaderRow("",
                "THÔNG TIN KHÁCH HÀNG", "", "", "", "", "", "", "", "", "",
                "ĐƠN VỊ GỬI MẪU", "NGÀY LẤY MẪU", "Ngày XN",
                "Kết quả XN", "", "", "",
                "KẾT LUẬN", "MÃ SỐ BN của PXN", "KẾT QUẢ NHIỄM MỚI", "TLVR", "ĐƠN VỊ KHẲNG ĐỊNH", "GHI CHÚ");
        int line = getLastRowNumber() - 1;

        createTableHeaderRow("STT", "HỌ TÊN", "MÃ SỐ NƠI GỬI", "Giới", "Năm sinh",
                "", "Địa chỉ", "", "",
                "Số CMT/CCCD", "ĐỐI TƯỢNG", "", "", "",
                "SP1", "SP2", "SP3", "SP4", "", "", "", "", "");

        int line2 = getLastRowNumber() - 1;

        createTableHeaderRow("", "", "", "", "",
                "Địa chỉ", "Tỉnh/TP", "Quận/Huyện", "Xã/Phường", "", "", "", "", "",
                "Kết quả XN", "Kết quả XN", "Kết quả XN", "Kết quả XN");
        //Dong 1
        setMerge(line, line, 1, 10, true);
        setMerge(line, line + 2, 11, 11, true);
        setMerge(line, line + 2, 12, 12, true);
        setMerge(line, line + 2, 13, 13, true);
        setMerge(line, line, 14, 17, true);
        setMerge(line, line + 2, 18, 18, true);
        setMerge(line, line + 2, 19, 19, true);
        setMerge(line, line + 2, 20, 20, true);
        setMerge(line, line + 2, 21, 21, true);
        setMerge(line, line + 2, 22, 22, true);
        setMerge(line, line + 2, 23, 23, true);
        //dÒNG 2
        setMerge(line2, line2 + 1, 0, 0, true);
        setMerge(line2, line2 + 1, 1, 1, true);
        setMerge(line2, line2 + 1, 2, 2, true);
        setMerge(line2, line2 + 1, 3, 3, true);
        setMerge(line2, line2 + 1, 4, 4, true);
        setMerge(line2, line2, 6, 8, true);
        setMerge(line2, line2 + 1, 9, 9, true);
        setMerge(line2, line2 + 1, 10, 10, true);
        //Dòng 3

        int index = 1;
        for (HtcConfirmEntity item : form.getItems()) {
            createTableRow(index++,
                    item.getFullname(),
                    item.getSourceID(),
                    item.getGenderID() == null || item.getGenderID().equals("") ? "" : form.getOptions().get("gender").get(item.getGenderID()),
                    item.getYear(),
                    item.getAddress(),
                    form.getOptions().get("province").get(item.getProvinceID()),
                    form.getOptions().get("district").get(item.getDistrictID()),
                    form.getOptions().get("ward").get(item.getWardID()),
                    item.getPatientID(),
                    item.getObjectGroupID() == null || "".equals(item.getObjectGroupID()) ? "" : form.getObjectGroups().getOrDefault(item.getObjectGroupID(), "Khác"),
                    form.getSiteOptions().get(item.getSourceSiteID() + ""),
                    TextUtils.formatDate(item.getSourceReceiveSampleTime(), "dd/MM/yyyy"),
                    TextUtils.formatDate(item.getEarlyHivDate(), "dd/MM/yyyy"),
                    item.getBioNameResult1() == null || "".equals(item.getBioNameResult1()) ? "" : form.getOptions().get("bio-name-result").get(item.getBioNameResult1()),
                    item.getBioNameResult2() == null || "".equals(item.getBioNameResult2()) ? "" : form.getOptions().get("bio-name-result").get(item.getBioNameResult2()),
                    item.getBioNameResult3() == null || "".equals(item.getBioNameResult3()) ? "" : form.getOptions().get("bio-name-result").get(item.getBioNameResult3()), "",
                    item.getResultsID() == null || item.getResultsID().equals("") ? "" : form.getOptions().get("test-result-confirm").get(item.getResultsID()),
                    item.getCode(),
                    item.getEarlyDiagnose() == null || "".equals(item.getEarlyDiagnose()) ? "" : form.getOptions().get("early-diagnose").get(item.getEarlyDiagnose()),
                    item.getVirusLoadNumber() == null ? "" : item.getVirusLoadNumber(),
                    form.getSiteOptions().get(item.getSiteID() + ""),
                     ""
            );

        }

    }

}
