package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacLocalForm;
import com.gms.entity.form.pac.PacLocalTableForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class LocalExcel extends BaseView implements IExportExcel {

    private PacLocalForm form;
    private String extension;

    public LocalExcel(PacLocalForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Báo cáo theo huyện xã";
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

    /**
     * Tạo header cho excel
     *
     * @throws Exception
     */
    private void createHeader() throws Exception {
        Sheet sheet = getCurrentSheet();
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(300));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(200));

        if (form.isPAC()) {
            sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(200));
            sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(200));
            sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(200));
            sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(200));
        }

        if (!form.isVAAC() && !form.isPAC()) {
            sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(200));
            sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(200));
            sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(200));
        }

        int titleColIndex = 0;
        if (form.isVAAC()) {
            titleColIndex = 4;
        }

        if (!form.isVAAC() && form.isPAC()) {
            titleColIndex = 7;
        }

        if (!form.isVAAC() && !form.isPAC()) {
            titleColIndex = 6;
        }

        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();
        int cellIndexFilter = 1;

        row = createTitleRow(form.getTitle());
        rownum = row.getRowNum();
        setMerge(rownum, rownum, 0, titleColIndex, false);

        row = createTitleDateRow(String.format("%s %s", (StringUtils.isNotEmpty(form.getFromTime()) ? ("từ " + form.getFromTime()) : ""), StringUtils.isNotEmpty(form.getFromTime()) ? ("đến " + form.getToTime()) : ""));
        rownum = row.getRowNum();
        setMerge(rownum, rownum, 0, titleColIndex, false);

        createRow();

        //Thông tin tìm kiếm
        createFilterRow("Địa chỉ", "".equals(form.getTitleLocationDisplay()) ? form.getProvinceName() : form.getTitleLocationDisplay(), cellIndexFilter);

        //filter info default
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();//dùng để merge khi không có thông tin

        row = createTableRowNoBorder(String.format("%s %s", form.isVAAC() && form.getLevelDisplay().equals("province") ? "Tổng số tỉnh/thành phố có người nhiễm:" : form.isVAAC() && form.getLevelDisplay().equals("district") ? "Tổng số quận/huyện có người nhiễm:" : "Tổng số phường/xã có người nhiễm:",
                form.isVAAC() && form.getLevelDisplay().equals("province") ? form.numberFormat(form.getProvinceTotal()) : form.isVAAC() && form.getLevelDisplay().equals("district") ? form.numberFormat(form.getDistrictTotal()) : form.numberFormat(form.getWardTotal())), null);
        rownum = row.getRowNum();
        setMerge(rownum, rownum, 0, 1, false);

        if (form.isPAC()) {
            row = createTableRowNoBorder(String.format("%s %s", "Người nhiễm chưa rà soát: ", String.valueOf(form.numberFormat(form.getNotReviewTotal()))), null,
                    String.format("%s %s", "Người nhiễm cần rà soát:", String.valueOf(form.numberFormat(form.getNeedReviewTotal()))), null,
                    String.format("%s %s", "Người nhiễm đã rà soát:", String.valueOf(form.numberFormat(form.getReviewedTotal()))), null,
                    String.format("%s %s", "Người nhiễm quản lý:", String.valueOf(form.numberFormat(form.getSum()))), null,
                    String.format("%s %s", "Người nhiễm ngoại tỉnh:", String.valueOf(form.numberFormat(form.getOutProvinceTotal()))), null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 0, 1, false);
            setMerge(rownum, rownum, 2, 3, false);
            setMerge(rownum, rownum, 4, 5, false);
            setMerge(rownum, rownum, 6, 7, false);
            setMerge(rownum, rownum, 8, 12, false);
        }

        if (!form.isPAC() && !form.isVAAC()) {
            row = createTableRowNoBorder(String.format("%s %s", "Người nhiễm cần rà soát:", String.valueOf(form.numberFormat(form.getNeedReviewTotal()))), null,
                    String.format("%s %s", "Người nhiễm đã rà soát:", String.valueOf(form.numberFormat(form.getReviewedTotal()))), null,
                    String.format("%s %s", "Người nhiễm quản lý:", String.valueOf(form.numberFormat(form.getSum()))), null,
                    String.format("%s %s", "Người nhiễm ngoại tỉnh:", String.valueOf(form.numberFormat(form.getOutProvinceTotal()))), null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 0, 1, false);
            setMerge(rownum, rownum, 2, 3, false);
            setMerge(rownum, rownum, 4, 5, false);
            setMerge(rownum, rownum, 6, 9, false);
        }
        createRow();

        // TH VAAC
        if (form.isVAAC()) {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", "Số người nhiễm quản lý", null, null, null);
            createTableHeaderRow(null, null, "Còn sống", "Tử vong", "Lũy tích");
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 4, true);
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);

            if (form.getTable() != null && form.getSum() > 0) {
                if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
                    row = createTableRow("", "Toàn quốc");
                    rownum = row.getRowNum();
                    setMerge(rownum, rownum, 1, 4, true);
                }

                int stt = 1;

                for (PacLocalTableForm local : form.getTable()) {
                    // Hiển thị tỉnh
                    // KHông phải VAAC hoặc là VAAC không chọn ht tỉnh
                    if ((StringUtils.isNotEmpty(form.getLevelDisplay()) && !"province".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC())) {
                        row = createTableRow(stt, local.getDisplayName());
                        rownum = row.getRowNum();
                        setMerge(rownum, rownum, 1, 4, true);
                    }

                    // TH chọn ht tỉnh và login VAAC
                    if ((StringUtils.isEmpty(form.getLevelDisplay()) || "province".equals(form.getLevelDisplay())) && form.isVAAC()) {
                        createTableRow(stt, local.getDisplayName(), local.getNumAlive(), local.getNumDead(), local.getCumulative());
                    }

                    // Hiển thị huyện
                    if (local.getChilds() != null && !local.getChilds().isEmpty()) {
                        for (PacLocalTableForm child : local.getChilds()) {
                            if (("ward".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC() && "ward".equals(form.getLevelDisplay()))) {
                                row = createTableRow("", "    " + child.getDisplayName());
                                rownum = row.getRowNum();
                                setMerge(rownum, rownum, 1, 4, true);
                            }

                            if ("district".equals(form.getLevelDisplay())) {
                                createTableRow("", String.format("%s%s", "    ", child.getDisplayName()), child.getNumAlive(), child.getNumDead(), child.getCumulative());
                            }

                            // Lặp hiển thị xã
                            if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                for (PacLocalTableForm childWard : child.getChilds()) {
                                    createTableRow("", "        " + childWard.getDisplayName(), childWard.getNumAlive(), childWard.getNumDead(), childWard.getCumulative());
                                }
                            }
                            // Tổng huyện
                            if ("ward".equals(form.getLevelDisplay())
                                    && (StringUtils.isEmpty(form.getDistrict()) || form.getDistrict().contains(","))
                                    && local.getChilds() != null && local.getChilds().size() > 1) {
                                createTableRow("", "     Tổng huyện", child.getNumAlive(), child.getNumDead(), child.getCumulative());
                            }
                        }
                    }
                    // Tổng tỉnh
                    if (form.getTable().size() > 1 && (!"province".equals(form.getLevelDisplay()) && (StringUtils.isEmpty(form.getProvince()) || form.getProvince().contains(",")))) {
                        createTableRow("", "Tổng tỉnh", local.getNumAlive(), local.getNumDead(),
                                local.getCumulative());
                    }
                    stt++;
                }
                // Dòng tổng cộng
                row = createTableRow("Tổng cộng", null, form.getAliveTotal(), form.getDeadTotal(), form.getSum());
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 0, 1, true);
            }
        }

        // TK <> VAAC
        if (!form.isVAAC()) {
            if (form.isPAC()) {
                row = createTableHeaderRow("TT", "Đơn vị báo cáo", "Số người nhiễm \n chưa rà soát", "Số người nhiễm \n cần rà soát", "Số người nhiễm \n đã rà soát", "Số người nhiễm quản lý", null, null, "Số người nhiễm ngoại tỉnh");
                createTableHeaderRow(null, null, null, null, null, "Còn sống", "Tử vong", "Lũy tích");
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 5, 7, true);
                setMerge(rownum, rownum + 1, 0, 0, true);
                setMerge(rownum, rownum + 1, 1, 1, true);
                setMerge(rownum, rownum + 1, 2, 2, true);
                setMerge(rownum, rownum + 1, 3, 3, true);
                setMerge(rownum, rownum + 1, 4, 4, true);
                setMerge(rownum, rownum + 1, 8, 8, true);

                if (form.getTable() != null && (form.getWardTotal() > 0 || form.getOutProvinceTotal() > 0)) {
                    for (PacLocalTableForm local : form.getTable()) {
                        // Hiển thị huyện
                        if (local.getChilds() != null && !local.getChilds().isEmpty()) {
                            for (PacLocalTableForm child : local.getChilds()) {
                                createTableRow(child.getStt(), String.format("%s%s", "    ", child.getDisplayName()), child.getNotReviewYet(), child.getNeedReview(), child.getReviewed(), child.getNumAlive(), child.getNumDead(), child.getCumulative(), child.getOutprovince());

                                // Lặp hiển thị xã
                                if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                    for (PacLocalTableForm childWard : child.getChilds()) {
                                        createTableRow("", "        " + childWard.getDisplayName(), childWard.getNotReviewYet(), childWard.getNeedReview(), childWard.getReviewed(), childWard.getNumAlive(), childWard.getNumDead(), childWard.getCumulative(), childWard.getOutprovince());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!form.isPAC()) {
                row = createTableHeaderRow("TT", "Đơn vị báo cáo", "Số người nhiễm \n cần rà soát", "Số người nhiễm \n đã rà soát", "Số người nhiễm quản lý", null, null,  "Số người nhiễm ngoại tỉnh");
                createTableHeaderRow(null, null, null, null, "Còn sống", "Tử vong", "Lũy tích");
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 4, 6, true);
                setMerge(rownum, rownum + 1, 0, 0, true);
                setMerge(rownum, rownum + 1, 1, 1, true);
                setMerge(rownum, rownum + 1, 2, 2, true);
                setMerge(rownum, rownum + 1, 3, 3, true);
                setMerge(rownum, rownum + 1, 7, 7, true);

                if (form.getTable() != null && (form.getWardTotal() > 0 || form.getOutProvinceTotal() > 0)) {
                    for (PacLocalTableForm local : form.getTable()) {
                        // Hiển thị huyện
                        if (local.getChilds() != null && !local.getChilds().isEmpty()) {
                            for (PacLocalTableForm child : local.getChilds()) {
                                if (!form.isTYT()) {
                                    createTableRow(child.getStt(), String.format("%s%s", "    ", child.getDisplayName()), child.getNeedReview(), child.getReviewed(), child.getNumAlive(), child.getNumDead(), child.getCumulative(), child.getOutprovince());
                                }

                                if (form.isTYT()) {
                                    createTableRow(child.getStt(), String.format("%s%s", "    ", child.getDisplayName()), "", "", "", "", "", "");
                                }

                                // Lặp hiển thị xã
                                if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                    for (PacLocalTableForm childWard : child.getChilds()) {
                                        createTableRow("", "        " + childWard.getDisplayName(), childWard.getNeedReview(), childWard.getReviewed(), childWard.getNumAlive(), childWard.getNumDead(), childWard.getCumulative(), childWard.getOutprovince());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
