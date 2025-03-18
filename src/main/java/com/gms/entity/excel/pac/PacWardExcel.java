package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacWardForm;
import com.gms.entity.form.pac.PacWardTableForm;
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
public class PacWardExcel extends BaseView implements IExportExcel {

    private final PacWardForm form;
    private final String extension;

    public PacWardExcel(PacWardForm form, String extension) {
        super.setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "BC thang duoc quan ly";
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
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 14, false);
        setMerge(2, 2, 0, 14, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());
        createTitleDateRow(String.format("Tháng %s - %s", form.getMonth(), form.getYear()));
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getGenderID())) {
            createFilterRow("Giới tính", form.getOptions().get(ParameterEntity.GENDER).get(form.getGenderID()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getObjectID())) {
            createFilterRow("Nhóm đối tượng", form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(form.getObjectID()), cellIndexFilter);
        }
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
        createRow();
    }

    private void createTable() throws Exception {

        createRow();
        int index = getLastRowNumber();

        List<String> headers = new ArrayList<>();
        List<String> secondHeaders = new ArrayList<>();
        headers.add("TT");
        headers.add("Địa bàn quản lý");
        if (form.isPAC()) {
            headers.add("Xã/phường trọng điểm về HIV/AIDS");
        }
        headers.add("Số liệu theo danh sách quản lý");
        headers.add("");
        headers.add("");
        headers.add("Không rõ địa chỉ");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Số người hiện đang được quản lý");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Nhóm tuổi");
        headers.add("");
        headers.add("");

        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        secondHeaders.add("");
        secondHeaders.add("");
        if (form.isPAC()) {
            secondHeaders.add("");
        }
        secondHeaders.add("HIV");
        secondHeaders.add("AIDS");
        secondHeaders.add("Tử vong");
        secondHeaders.add("Mất dấu");
        secondHeaders.add("Không có thực tế");
        secondHeaders.add("Chuyển ngoại tỉnh");
        secondHeaders.add("Chưa xác định được");
        secondHeaders.add("Không có thông tin");
        secondHeaders.add("Tổng cộng");
        secondHeaders.add("Chuyển trong tỉnh");
        secondHeaders.add("Đi làm ăn xa");
        secondHeaders.add("Trong trại");
        secondHeaders.add("Đang ở địa phương");
        secondHeaders.add("Tổng cộng");
        secondHeaders.add("<15");
        secondHeaders.add(">=15");
        secondHeaders.add("Tổng cộng");

        createTableHeaderRow(secondHeaders.toArray(new String[secondHeaders.size()]));
        int x = form.isPAC() ? 0 : -1;

        setMerge(index, index + 1, 0, 0, true);
        setMerge(index, index + 1, 1, 1, true);
        if (form.isPAC()) {
            setMerge(index, index + 1, 2, 2, true);
        }
        setMerge(index, index, 3 + x, 5 + x, true);
        setMerge(index, index, 6 + x, 11 + x, true);
        setMerge(index, index, 12 + x, 16 + x, true);
        setMerge(index, index, 17 + x, 19 + x, true);

        if (form.getTable().size() > 0) {
            List<Object> row;
            for (PacWardTableForm item : form.getTable()) {
                if (item.isActive()) {
                    row = new ArrayList<>();
                    row.add(form.isPAC() && item.getWardName() != null ? "" : item.getStt());
                    row.add(String.format("%s", form.isPAC() && item.getDistrictName() == null ? "    " : "") + (item.getWardName() == null ? item.getDistrictName() : item.getWardName()));
                    if (form.isPAC()) {
                        row.add(item.getDistrictName() == null || item.getDistrictName().equals("Không rõ") ? "" : item.getWardName() == null ? item.getCountWard() + "/" + item.getCountWard() + "" : "");
                    }
                    row.add(item.getHiv());
                    row.add(item.getAids());
                    row.add(item.getTv());
                    row.add(item.getMatDau());
                    row.add(item.getKhongCoThucTe());
                    row.add(item.getChuyenDiNoiKhac());
                    row.add(item.getChuaXacDinhDuoc());
                    row.add(item.getKhongCoThongTin());
                    row.add(item.getTotalUnclear());
                    row.add(item.getChuyenTrongTinh());
                    row.add(item.getDiLamAnXa());
                    row.add(item.getDiTrai());
                    row.add(item.getDangODiaPhuong());
                    row.add(item.getTotalManage());
                    row.add(item.getUnder15());
                    row.add(item.getOver15());
                    row.add(item.getTotalAge());

                    createTableRow(row.toArray(new Object[row.size()]));
                }
            }
//            tổng

            int indexTotal = getLastRowNumber();
            row = new ArrayList<>();
            row.add("Tổng cộng");
            row.add("");
            if (form.isPAC()) {
                row.add(form.getTableTotal().getCountWard() + "/" + form.getTableTotal().getCountWard() + "");
            }
            row.add(form.getTableTotal().getHiv());
            row.add(form.getTableTotal().getAids());
            row.add(form.getTableTotal().getTv());
            row.add(form.getTableTotal().getMatDau());
            row.add(form.getTableTotal().getKhongCoThucTe());
            row.add(form.getTableTotal().getChuyenDiNoiKhac());
            row.add(form.getTableTotal().getChuaXacDinhDuoc());
            row.add(form.getTableTotal().getKhongCoThongTin());
            row.add(form.getTableTotal().getTotalUnclear());
            row.add(form.getTableTotal().getChuyenTrongTinh());
            row.add(form.getTableTotal().getDiLamAnXa());
            row.add(form.getTableTotal().getDiTrai());
            row.add(form.getTableTotal().getDangODiaPhuong());
            row.add(form.getTableTotal().getTotalManage());
            row.add(form.getTableTotal().getUnder15());
            row.add(form.getTableTotal().getOver15());
            row.add(form.getTableTotal().getTotalAge());

            createTableRow(row.toArray(new Object[row.size()]));

            setMerge(indexTotal, indexTotal, 0, 1, true);
        }
    }
}
