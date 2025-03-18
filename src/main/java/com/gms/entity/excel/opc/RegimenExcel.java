package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.RegimenForm;
import com.gms.entity.form.opc_arv.RegimenTable;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author DSNAnh
 */
public class RegimenExcel extends BaseView implements IExportExcel {

    private RegimenForm form;
    private String extension;

    public RegimenExcel(RegimenForm form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "BC PhacDo";
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
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 7, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Kỳ báo cáo", String.format("Tháng %s năm %s", form.getMonth() < 10 ? "0" + form.getMonth() : form.getMonth(), form.getYear()), cellIndexFilter);
        if (form.isOpcManager()) {
            createFilterRow("Cơ sở điều trị", form.getSiteLabel(), cellIndexFilter);
        }
        createRow();
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();

        createTableHeaderRow("Mã phác đồ", "Phác đồ điều trị",
                "Số BN bắt đầu nhận thuốc trong kỳ báo cáo", "Số BN đang nhận thuốc của kỳ báo cáo trước",
                "Số BN bỏ nhận thuốc trong kỳ (*)", "Số BN nhận thuốc tính đến thời điểm báo cáo",
                "Ước tính BN tích lũy đến tháng thứ 3 (kể từ thời điểm báo cáo)", "Ghi chú");
        createTableRow("(a)", "(b)", "(c)", "(d)", "(e)", "(f)", "(g)", "(h)");

        RegimenTable adults = form.getSumAdults().get("1");
        RegimenTable adults1 = form.getSumAdults().get("2");
        int line = getLastRowNumber();
        createTableRowBold("I", "Bệnh nhân người lớn", adults.getBatdaunhan(), adults.getDangnhan(), adults.getBonhan(), adults.getNhanThuoc(), adults.getUocTinh(), "");
        createTableRowBold("", "", adults1.getBatdaunhan(), adults1.getDangnhan(), adults1.getBonhan(), adults1.getNhanThuoc(), adults1.getUocTinh(), "");

        setMerge(line, line + 1, 0, 0, false);
        setMerge(line, line + 1, 1, 1, false);

        for (Map.Entry<String, HashMap<String, RegimenTable>> en : form.getAdults().entrySet()) {
            int line1 = getLastRowNumber();
            String key = en.getKey();
            HashMap<String, RegimenTable> value = en.getValue();

            createTableRow("", key, value.get("1").getBatdaunhan(), value.get("1").getDangnhan(), value.get("1").getBonhan(), value.get("1").getNhanThuoc(), value.get("1").getUocTinh(), "");
            createTableRow("", "", value.get("2").getBatdaunhan(), value.get("2").getDangnhan(), value.get("2").getBonhan(), value.get("2").getNhanThuoc(), value.get("2").getUocTinh(), "");

            setMerge(line1, line1 + 1, 0, 0, false);
            setMerge(line1, line1 + 1, 1, 1, false);
        }

     

        RegimenTable children = form.getSumChildren().get("1");
        RegimenTable children1 = form.getSumChildren().get("2");
        int line2 = getLastRowNumber();
        createTableRowBold("II", "Bệnh nhân trẻ em", children.getBatdaunhan(), children.getDangnhan(), children.getBonhan(), children.getNhanThuoc(), children.getUocTinh(), "");
        createTableRowBold("", "", children1.getBatdaunhan(), children1.getDangnhan(), children1.getBonhan(), children1.getNhanThuoc(), children1.getUocTinh(), "");

        setMerge(line2, line2 + 1, 0, 0, false);
        setMerge(line2, line2 + 1, 1, 1, false);
        
           for (Map.Entry<String, HashMap<String, RegimenTable>> en : form.getChildren().entrySet()) {
            int line1 = getLastRowNumber();
            String key = en.getKey();
            HashMap<String, RegimenTable> value = en.getValue();

            createTableRow("", key, value.get("1").getBatdaunhan(), value.get("1").getDangnhan(), value.get("1").getBonhan(), value.get("1").getNhanThuoc(), value.get("1").getUocTinh(), "");
            createTableRow("", "", value.get("2").getBatdaunhan(), value.get("2").getDangnhan(), value.get("2").getBonhan(), value.get("2").getNhanThuoc(), value.get("2").getUocTinh(), "");

            setMerge(line1, line1 + 1, 0, 0, false);
            setMerge(line1, line1 + 1, 1, 1, false);
        }


        createTableRowBold("III", "Báo cáo lây truyền Mẹ - con", "", "", "", "", "", "");
        createTableRowBold("IV", "Bệnh nhân phơi nhiễm", "", "", "", "", "", "");

        createRow();

        int index = getLastRowNumber();
        int cellIndexFilter = 3;
        setMerge(index, index, 1, 7, false);
        createFilterRow("(*) Nêu rõ lý do bỏ thuốc vào các mục tương ứng dưới đây", "", 1);
        createFilterRow("Chuyển đi", adults.getBonhan_chuyendi() + children.getBonhan_chuyendi() + adults1.getBonhan_chuyendi() + children1.getBonhan_chuyendi(), cellIndexFilter);
        createFilterRow("Tử vong", adults.getBonhan_tuvong() + children.getBonhan_tuvong() + adults1.getBonhan_tuvong() + children1.getBonhan_tuvong(), cellIndexFilter);
        createFilterRow("Bỏ trị", adults.getBonhan_botri() + children.getBonhan_botri() + adults1.getBonhan_botri() + children1.getBonhan_botri(), cellIndexFilter);
        createFilterRow("Mất dấu", adults.getBonhan_matdau() + children.getBonhan_matdau() + adults1.getBonhan_matdau() + children1.getBonhan_matdau(), cellIndexFilter);
        createFilterRow("Chuyển phác đồ", adults.getBonhan_chuyenphacdo() + children.getBonhan_chuyenphacdo() + adults1.getBonhan_chuyenphacdo() + children1.getBonhan_chuyenphacdo(), cellIndexFilter);
    }
}
