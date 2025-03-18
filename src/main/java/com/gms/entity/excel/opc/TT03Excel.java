package com.gms.entity.excel.opc;

import com.gms.entity.excel.pac.*;
import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.excel.tt03.Table02Excel;
import com.gms.entity.form.opc_arv.TT03Form;
import com.gms.entity.form.opc_arv.TT03Table02;
import com.gms.entity.form.opc_arv.TT03Table03;
import com.gms.entity.form.opc_arv.TT03Table04;
import com.gms.entity.form.opc_arv.TT03Table05;
import com.gms.entity.form.opc_arv.TT03Table06;
import com.gms.entity.form.pac.PacLocalForm;
import com.gms.entity.form.pac.PacLocalTableForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class TT03Excel extends BaseView implements IExportExcel {

    private TT03Form form;
    private HashMap<String, HashMap<String, String>> options;
    private String extension;

    public TT03Excel(TT03Form form, HashMap<String, HashMap<String, String>> options, String extension) {
        this.form = form;
        this.options = options;
        this.extension = extension;
        this.sheetName = "TT032015TTBYT";
    }

    public TT03Excel(TT03Form data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] run() throws Exception {
        workbook = ExcelUtils.getWorkbook(getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        createSheet(sheetName);
        createHeader();
        if (form.getTab().equals("year")) {
            createTable02();
            createTable03();
        } else {
            createTable04();
            createTable05();
            createTable06();
        }

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
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(35));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(400));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(150));

        setMerge(1, 1, 0, 4, false);

        createRow();
        createTitleRow("BÁO CÁO CÔNG TÁC PHÒNG, CHỐNG HIV/AIDS - " + (form.isOpcManager() ? "TUYẾN TỈNH" : "TUYẾN HUYỆN"));
        createRow();

        int cellIndexFilter = 1;
        createFilterRow("Loại báo cáo", form.getTitle(), cellIndexFilter);
        createFilterRow("Kỳ báo cáo", String.format("Từ %s đến %s", form.getStartDate(), form.getEndDate()), cellIndexFilter);
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable02() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow((form.isOpcManager() ? "Bảng 3" : "Bảng 2") + ". Điều trị ARV đối với bệnh nhân HIV/LAO", HorizontalAlignment.LEFT, 0);
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Danh mục báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Số lượng");
        } else {
            for (Map.Entry<Long, TT03Table02> entry : form.getTable02().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
            }
            headers.add("Tổng cộng");
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //row table
        List<Object> row = new ArrayList<>();
        row.add("1");
        row.add("Số bệnh nhân mới đăng ký trong kỳ báo cáo");
        for (Map.Entry<Long, TT03Table02> entry : form.getTable02().entrySet()) {
            TT03Table02 item = entry.getValue();
            row.add(item.getLao());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable02().getLao());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        row = new ArrayList<>();
        row.add("2");
        row.add("Số bệnh nhân hiện quản lý đến cuối kỳ báo cáo");
        for (Map.Entry<Long, TT03Table02> entry : form.getTable02().entrySet()) {
            TT03Table02 item = entry.getValue();
            row.add(item.getArv());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable02().getArv());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }

    private void createTable03() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow((form.isOpcManager() ? "Bảng 5" : "Bảng 3") + ". Theo dõi xét nghiệm tải lượng HIV", HorizontalAlignment.LEFT, 0);
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Danh mục báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Số lượng");
        } else {
            for (Map.Entry<Long, TT03Table03> entry : form.getTable03().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
            }
            headers.add("Tổng cộng");
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //Row STT 1
        List<Object> row = new ArrayList<>();
        row.add("1");
        row.add("Số bệnh nhân điều trị ARV được làm xét nghiệm tải lượng HIV để khẳng định thất bại điều trị");
        for (Map.Entry<Long, TT03Table03> entry : form.getTable03().entrySet()) {
            TT03Table03 item = entry.getValue();
            row.add(item.getTlvr());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable03().getTlvr());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 1.1
        row = new ArrayList<>();
        row.add("");
        row.add("Trong đó, số bệnh nhân có kết quả tải lượng HIV dưới 1000 cp/ml");
        for (Map.Entry<Long, TT03Table03> entry : form.getTable03().entrySet()) {
            TT03Table03 item = entry.getValue();
            row.add(item.getTlvr1000());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable03().getTlvr1000());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2
        row = new ArrayList<>();
        row.add("2");
        row.add("Số bệnh nhân điều trị ARV được làm xét nghiệm tải lượng định kỳ tại thời điểm 12 tháng kể từ khi bắt đầu điều trị");
        for (Map.Entry<Long, TT03Table03> entry : form.getTable03().entrySet()) {
            TT03Table03 item = entry.getValue();
            row.add(item.getMonth());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable03().getMonth());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.1
        row = new ArrayList<>();
        row.add("");
        row.add("Trong đó, số bệnh nhân có kết quả tải lượng HIV dưới 1000 cp/ml");
        for (Map.Entry<Long, TT03Table03> entry : form.getTable03().entrySet()) {
            TT03Table03 item = entry.getValue();
            row.add(item.getMonth1000());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable03().getMonth1000());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }

    private void createTable06() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow("Bảng 6" + ". Dự phòng lây truyền HIV từ mẹ sang con", HorizontalAlignment.LEFT, 0);
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Danh mục báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Số lượng");
        } else {
            for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
            }
            headers.add("Tổng cộng");
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //Row STT 1
        List<Object> row = new ArrayList<>();
        row.add("1");
        row.add("Số phụ nữ  mang thai nhiễm HIV được điều trị ARV trong kỳ báo cáo, Trong đó :");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            row.add("");
        }
        if (form.isOpcManager()) {
            row.add("");
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 1.1
        row = new ArrayList<>();
        row.add("");
        row.add("1.1. Điều trị ARV trước khi có thai");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR11());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR11());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 12
        row = new ArrayList<>();
        row.add("");
        row.add("1.2. Bắt đầu điều trị ARV trong thời kỳ mang thai");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR12());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR12());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 1.3
        row = new ArrayList<>();
        row.add("");
        row.add("1.3. Bắt đầu điều trị ARV trong khi chuyển dạ, đẻ");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR13());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR13());
        }
        createTableRow(row.toArray(new Object[row.size()]));
        //Row STT 2
        row = new ArrayList<>();
        row.add("2");
        row.add("Số trẻ đẻ sống từ mẹ nhiễm HIV, trong đó :");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add("");
        }
        if (form.isOpcManager()) {
            row.add("");
        }
        createTableRow(row.toArray(new Object[row.size()]));
        //Row STT 1.1
        row = new ArrayList<>();
        row.add("");
        row.add("2.1. Được dự phòng ARV");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR21());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR21());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 12
        row = new ArrayList<>();
        row.add("");
        row.add("2.2. Được dự phòng bằng co-trimoxazole (CTX) trong vòng 2 tháng sau sinh");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR22());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR22());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 1.3
        row = new ArrayList<>();
        row.add("");
        row.add("2.3. Được điều trị ARV khi xét nghiệm CR lần 1 dương tính");
        for (Map.Entry<Long, TT03Table06> entry : form.getTable06().entrySet()) {
            TT03Table06 item = entry.getValue();
            row.add(item.getR23());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable06().getR23());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }

    private void createTable04() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow("Bảng 4. Quản lý trước điều trị ARV và điều trị dự phòng LAO", HorizontalAlignment.LEFT, 0);
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Danh mục báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Số lượng");
        } else {
            for (Map.Entry<Long, TT03Table04> entry : form.getTable04().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
            }
            headers.add("Tổng cộng");
        }
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        //Row STT 1
        List<Object> row = new ArrayList<>();
        row.add("1");
        row.add("Số bệnh nhân mới đăng ký trong kỳ báo cáo");
        for (Map.Entry<Long, TT03Table04> entry : form.getTable04().entrySet()) {
            TT03Table04 item = entry.getValue();
            row.add(item.getRegister());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable04().getRegister());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2
        row = new ArrayList<>();
        row.add("2");
        row.add("Số bệnh nhân hiện quản lý đến cuối kỳ báo cáo");
        for (Map.Entry<Long, TT03Table04> entry : form.getTable04().entrySet()) {
            TT03Table04 item = entry.getValue();
            row.add(item.getTotal());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable04().getTotal());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 3
        row = new ArrayList<>();
        row.add("3");
        row.add("Số bệnh nhân bắt đầu dự phòng bằng INH trong kỳ báo cáo");
        for (Map.Entry<Long, TT03Table04> entry : form.getTable04().entrySet()) {
            TT03Table04 item = entry.getValue();
            row.add(item.getInh());
        }
        if (form.isOpcManager()) {
            row.add(form.getSumTable04().getInh());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }

    private void createTable05() {
        getCurrentSheet();
        createRow();

        int line = getLastRowNumber();
        setMerge(line, line, 0, 2, false);
        createTitleDateRow("Bảng 5. Quản lý điều trị ARV", HorizontalAlignment.LEFT, 0);
        //Header table
        List<String> headers = new ArrayList<>();
        headers.add("TT");
        headers.add("Danh mục báo cáo");
        if (!form.isOpcManager()) {
            headers.add("Phác đồ bậc 1");
            headers.add("Phác đồ bậc 2");
            headers.add("Tổng cộng");
            //Create row Trường hợp cơ sở điều trị
            createTableHeaderRow(headers.toArray(new String[headers.size()]));
        } else {
            List<String> labels = new ArrayList<>();
            labels.add(null);
            labels.add(null);
            for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
                headers.add(this.options.get("siteOpcFrom").getOrDefault(String.valueOf(entry.getKey()), "#" + entry.getKey()));
                headers.add(null);
                headers.add(null);
                //sub row
                labels.add("Phác đồ bậc 1");
                labels.add("Phác đồ bậc 2");
                labels.add("Tổng cộng");
            }
            headers.add("Tổng cộng");
            headers.add(null);
            headers.add(null);
            labels.add("Phác đồ bậc 1");
            labels.add("Phác đồ bậc 2");
            labels.add("Tổng cộng");
            //Khoa điều trị - create cơ cở trước
            createTableHeaderRow(headers.toArray(new String[headers.size()]));
            int rowIndex = getLastRowNumber();
            int colIndex = 2;
            for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
                setMerge(rowIndex - 1, rowIndex - 1, colIndex, colIndex + 2, true);
                colIndex += 3;
            }
            //Dòng tổng
            setMerge(rowIndex - 1, rowIndex - 1, colIndex, colIndex + 2, true);
            createTableHeaderRow(labels.toArray(new String[labels.size()]));
            //merge 2 cột đầu tiên
            rowIndex = getLastRowNumber();
            setMerge(rowIndex - 2, rowIndex - 1, 0, 0, true);
            setMerge(rowIndex - 2, rowIndex - 1, 1, 1, true);
        }

        //Row STT 1
        List<Object> row = new ArrayList<>();
        List<Object> row1 = new ArrayList<>();
        row.add("1");
        row.add("Số bệnh nhân điều trị ARV cuối kỳ báo cáo trước");
        row1.add("2");
        row1.add("Số bệnh nhân điều trị ARV trong kỳ báo cáo");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "0" : item.get("1").getPreArv());
            row.add(item.get("2") == null ? "0" : item.get("2").getPreArv());
            row.add(item.get("total") == null ? "0" : item.get("total").getPreArv());
            row1.add("");
            row1.add("");
            row1.add("");
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getPreArv());
            row.add(item.get("2").getPreArv());
            row.add(item.get("total").getPreArv());
            row1.add("");
            row1.add("");
            row1.add("");
        }
        createTableRow(row.toArray(new Object[row.size()]));
        createTableRow(row1.toArray(new Object[row1.size()]));

        //Row STT 2.1
        row = new ArrayList<>();

        row.add("");
        row.add("2.1. Số bệnh nhân bắt đầu điều trị lần đầu tiên");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1") == null ? "" : item.get("1").getFirstRegister());
            row.add(item.get("2") == null ? "" : item.get("2").getFirstRegister());
            row.add(item.get("total") == null ? "" : item.get("total").getFirstRegister());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getFirstRegister());
            row.add(item.get("2").getFirstRegister());
            row.add(item.get("total").getFirstRegister());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.2
        row = new ArrayList<>();
        row.add("");
        row.add("2.2. Số bệnh nhân điều trị lại");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getBackTreatment());
            row.add(item.get("2") == null ? "" : item.get("2").getBackTreatment());
            row.add(item.get("total") == null ? "" : item.get("total").getBackTreatment());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getBackTreatment());
            row.add(item.get("2").getBackTreatment());
            row.add(item.get("total").getBackTreatment());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.3
        row = new ArrayList<>();
        row.add("");
        row.add("2.3. Số bệnh nhân chuyển đến");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getTransferTo());
            row.add(item.get("2") == null ? "" : item.get("2").getTransferTo());
            row.add(item.get("total") == null ? "" : item.get("total").getTransferTo());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getTransferTo());
            row.add(item.get("2").getTransferTo());
            row.add(item.get("total").getTransferTo());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.4
        row = new ArrayList<>();
        row.add("");
        row.add("2.4. Số bệnh nhân chuyển đi");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getMoveAway());
            row.add(item.get("2") == null ? "" : item.get("2").getMoveAway());
            row.add(item.get("total") == null ? "" : item.get("total").getMoveAway());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getMoveAway());
            row.add(item.get("2").getMoveAway());
            row.add(item.get("total").getMoveAway());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.5
        row = new ArrayList<>();
        row.add("");
        row.add("2.5. Số bệnh nhân bỏ điều trị");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getQuitTreatment());
            row.add(item.get("2") == null ? "" : item.get("2").getQuitTreatment());
            row.add(item.get("total") == null ? "" : item.get("total").getQuitTreatment());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getQuitTreatment());
            row.add(item.get("2").getQuitTreatment());
            row.add(item.get("total").getQuitTreatment());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.6
        row = new ArrayList<>();
        row.add("");
        row.add("2.6. Số bệnh nhân tử vong");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getDead());
            row.add(item.get("2") == null ? "" : item.get("2").getDead());
            row.add(item.get("total") == null ? "" : item.get("total").getDead());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getDead());
            row.add(item.get("2").getDead());
            row.add(item.get("total").getDead());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 2.7
        row = new ArrayList<>();
        row.add("");
        row.add("2.7. Số bệnh nhân hiện đang điều trị ARV cuối kỳ báo cáo này");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getArv());
            row.add(item.get("2") == null ? "" : item.get("2").getArv());
            row.add(item.get("total") == null ? "" : item.get("total").getArv());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getArv());
            row.add(item.get("2").getArv());
            row.add(item.get("total").getArv());
        }
        createTableRow(row.toArray(new Object[row.size()]));

        //Row STT 3
        row = new ArrayList<>();
        row.add("3");
        row.add("Số bệnh nhân được nhận thuốc ARV tại tuyến xã");
        for (Map.Entry<Long, Map<String, TT03Table05>> entry : form.getTable05().entrySet()) {
            Map<String, TT03Table05> item = entry.getValue();
            row.add(item.get("1") == null ? "" : item.get("1").getWard());
            row.add(item.get("2") == null ? "" : item.get("2").getWard());
            row.add(item.get("total") == null ? "" : item.get("total").getWard());
        }
        if (form.isOpcManager()) {
            Map<String, TT03Table05> item = form.getSumTable05();
            row.add(item.get("1").getWard());
            row.add(item.get("2").getWard());
            row.add(item.get("total").getWard());
        }
        createTableRow(row.toArray(new Object[row.size()]));
    }
}
