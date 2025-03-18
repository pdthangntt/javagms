package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author othoa
 */
public class PqmDrugPlanDataExcel extends BaseView implements IExportExcel {

    private List<PqmDrugPlanDataEntity> items;
    private String extension;
    private Integer year;
    private Integer month;
    private String site;
    private String tab;
    private String siteNameSearch;

    public PqmDrugPlanDataExcel(List<PqmDrugPlanDataEntity> items, Integer year, Integer month, String site, String extension, String tab, String siteNameSearch) {
        this.useStyle = true;
        this.items = items;
        this.year = year;
        this.month = month;
        this.site = site;
        this.tab = tab;
        this.siteNameSearch = siteNameSearch;
        this.extension = extension;
        this.sheetName = "Data";
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
        return String.format("%s.%s", "Bang_tong_hop_chi_so_thuoc", extension);
    }

    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        int i = 0;
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(70));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 5, false);

        createRow();
        createTitleRow("Chỉ số thuốc dự trù an toàn");
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", month < 10 ? "0" + month : month, year), cellIndexFilter);
        if (StringUtils.isNotBlank(tab) && tab.equals("detail") && StringUtils.isNotEmpty(siteNameSearch)) {
            createFilterRow("Cơ sở", siteNameSearch, cellIndexFilter);
        }
        createFilterRow("Tên đơn vị", site, cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createRow();
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        createTableHeaderRow("STT",
                "Tên thuốc",
                "Đơn vị tính",
                "Số lượng thuốc tồn cuối kỳ",
                "Số lượng thuốc để đạt dự trù an toàn 2 tháng");
        int i = 1;
        if (items != null && !items.isEmpty()) {
            for (PqmDrugPlanDataEntity item : items) {
                createTableRow(
                        i++,
                        item.getDrugName(),
                        item.getUnit(),
                        item.getTotalDrugBacklog(),
                        item.getTotalDrugPlan2Month()
                );
            }
        } else {
            int index = getLastRowNumber();
            createTableHeaderRow(
                    "Không có thông tin");
            setMerge(index, index, 0, 4, true);

        }
    }
}
