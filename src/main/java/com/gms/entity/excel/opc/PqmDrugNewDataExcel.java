package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.PqmDrugNewForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class PqmDrugNewDataExcel extends BaseView implements IExportExcel {

    private PqmDrugNewForm form;
    private String extension;

    public PqmDrugNewDataExcel(PqmDrugNewForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "Dulieu";
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(350));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 7, false);

        createRow();
        createTitleRow("Kết quả chỉ số thuốc ARV (HMED)"); 
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Thời gian báo cáo", String.format("Quý %s năm %s", form.getMonth(), form.getYear()), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getSiteSearch())) {
            createFilterRow("Cơ sở", form.getSiteSearch(), cellIndexFilter);
        }

        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();

        createTableHeaderRow(
                "STT", "Tên cơ sở", "Tên thuốc", "Nguồn", 
                "Tỉ lệ cảnh báo số lượng thuốc đủ cấp phát trong quý tiếp",
                "Tỉ lệ đánh giá hiệu quả dự trù và quản lý kho",
                "Tồn đầu kỳ",
                "Nhập định kỳ",
                "Nhập khác",
                "Xuất cho bệnh nhân trong kỳ",
                "Xuất điều chuyển trong kỳ",
                "Hư hao",
                "Tồn cuối kỳ"
        );
        int i = 1;
        if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {
            for (PqmDrugNewDataEntity item : form.getItemAPI()) {
                createTableRow(i++,
                        form.getSiteMaps().getOrDefault(item.getSiteID(), "Không rõ"),
                        item.getDrugName(),
                        item.getSource(),
                        item.getTlcb(),
                        item.getTldg(),
                        item.getTdk(),
                        item.getNdk(),
                        item.getNk(),
                        item.getXcbntk(),
                        item.getXdctk(),
                        item.getHh(),
                        item.getTck()
                );
            }
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 12, true);

        }

    }
}
