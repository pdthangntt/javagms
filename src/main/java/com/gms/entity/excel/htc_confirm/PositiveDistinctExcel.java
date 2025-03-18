package com.gms.entity.excel.htc_confirm;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveDistinctForm;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveDistinctTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class PositiveDistinctExcel extends BaseView implements IExportExcel {

    private HtcConfirmPositiveDistinctForm form;
    private String extension;

    public PositiveDistinctExcel(HtcConfirmPositiveDistinctForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "DSKH XNKĐ (+)";
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
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(400));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(250));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(155));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(155));

        setMerge(1, 1, 0, 10, false);
        setMerge(2, 2, 0, 10, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        createTitleDateRow(String.format("Từ ngày %s đến ngày %s", form.getStart(), form.getEnd()));
        //Tạo thêm dòng trắng
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getSiteName())) {
            createFilterRow("Cơ sở khẳng định", form.getSiteName(), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getSiteName())) {
            createFilterRow("Ngày báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        }

    }

    private void createTable() {
        getCurrentSheet();
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int index = getLastRowNumber();
        //Tiêu đề
        createTableHeaderRow("Mã XN khẳng định", "Họ và tên", "Năm sinh", "", "CMND", "Địa chỉ", "Ngày lấy mẫu", "Cơ sở gửi mẫu", "Ngày nhận mẫu", "Ngày XN khẳng định",
                "Mã khách hàng sàng lọc",
                "Tên sinh phẩm XN nhiễm mới",
                "Ngày XN nhiễm mới HIV",
                "KQXN nhiễm mới ban đầu",
                "Ngày XN tải lượng virus",
                "Nồng độ virus",
                "KQXN tải lượng virus",
                "Kết luận chẩn đoán nhiễm mới",
                "Ghi chú");
        createTableHeaderRow("", "", "Nam", "Nữ", "", "", "", "", "", "", "");

        setMerge(index, index + 1, 0, 0, true);
        setMerge(index, index + 1, 1, 1, true);
        setMerge(index, index, 2, 3, true);
        setMerge(index, index + 1, 4, 4, true);
        setMerge(index, index + 1, 5, 5, true);
        setMerge(index, index + 1, 6, 6, true);
        setMerge(index, index + 1, 7, 7, true);
        setMerge(index, index + 1, 8, 8, true);
        setMerge(index, index + 1, 9, 9, true);
        setMerge(index, index + 1, 10, 10, true);
        setMerge(index, index + 1, 11, 11, true);
        setMerge(index, index + 1, 12, 12, true);
        setMerge(index, index + 1, 13, 13, true);
        setMerge(index, index + 1, 14, 14, true);
        setMerge(index, index + 1, 15, 15, true);
        setMerge(index, index + 1, 16, 16, true);
        setMerge(index, index + 1, 17, 17, true);
        setMerge(index, index + 1, 18, 18, true);

        if (form.getTable() != null && form.getTable().size() > 0) {
            for (HtcConfirmPositiveDistinctTableForm item : form.getTable()) {
                createTableRow(
                        item.getCode() == null ? "" : item.getCode(),
                        item.getFullName() == null ? "" : item.getFullName(),
                        item.getGenderID() == null ? "" : "1".equals(item.getGenderID()) ? item.getYear() : "",
                        item.getGenderID() == null ? "" : "2".equals(item.getGenderID()) ? item.getYear() : "",
                        item.getIdentityCard() == null ? "" : item.getIdentityCard(),
                        item.getAddress() == null ? "" : item.getAddress(),
                        item.getSourceReceiveSampleTime() == null ? "" : item.getSourceReceiveSampleTime(),
                        item.getSourceSiteID() == null ? "" : item.getSourceSiteID(),
                        item.getSampleReceiveTime() == null ? "" : item.getSampleReceiveTime(),
                        item.getConfirmTime() == null ? "" : item.getConfirmTime(),
                        item.getSourceID() == null ? "" : item.getSourceID(),
                        item.getEarlyBioName() == null ? "" : item.getEarlyBioName(),
                        item.getEarlyHivDate() == null ? "" : item.getEarlyHivDate(),
                        item.getEarlyHiv() == null ? "" : item.getEarlyHiv(),
                        item.getVirusLoadDate() == null ? "" : item.getVirusLoadDate(),
                        item.getVirusLoadNumber() == null ? "" : item.getVirusLoadNumber(),
                        item.getVirusLoad() == null ? "" : item.getVirusLoad(),
                        item.getEarlyDiagnose() == null ? "" : item.getEarlyDiagnose(),
                        item.getNote() == null ? "" : item.getNote()
                );
            }
        }
    }
}
