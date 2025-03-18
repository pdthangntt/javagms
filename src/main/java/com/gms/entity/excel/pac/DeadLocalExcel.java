package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacDeadLocalForm;
import com.gms.entity.form.pac.TablePacDeadForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author pdThang
 */
public class DeadLocalExcel extends BaseView implements IExportExcel {

    private PacDeadLocalForm form;
    private String extension;

    public DeadLocalExcel(PacDeadLocalForm form, String extension) {
        this.useStyle = false;
        this.form = form;
        this.extension = extension;
        this.sheetName = "Báo cáo tử vong";
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
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));

       //Gộp cột dòng tiêu đề
        setMerge(1, 1, 0, 9, false);
        setMerge(2, 2, 0, 9, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        createTitleDateRow(String.format("Từ %s đến ngày %s", form.getStartDate(), form.getEndDate()));
        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Địa chỉ", ("".equals(form.getTitleLocation()) && form.isPAC()) ? form.getProvinceName() : ("".equals(form.getTitleLocation()) && form.isTTYT()) ? String.format("%s, %s", form.getDistrictName(), form.getProvinceName()) : ("".equals(form.getTitleLocation()) && form.isTYT()) ? String.format("%s, %s, %s", form.getWardName(), form.getDistrictName(), form.getProvinceName()) : ("".equals(form.getTitleLocation()) && form.isVAAC()) ? form.getProvinceName() : form.getTitleLocation(), cellIndexFilter);
        if (!"0".equals(form.getAgeFrom()) && !"".equals(form.getAgeTo()) && 
                !"".equals(form.getAgeTo()) && !"-1".equals(form.getAgeFrom())) {
            createFilterRow("Tuổi ", String.format("Từ %s đến %s", form.getAgeFrom(), form.getAgeTo()), cellIndexFilter);
        }
        if (!"".equals(form.getAgeFrom()) && "".equals(form.getAgeTo()) && !"0".equals(form.getAgeFrom())) {
            createFilterRow("Tuổi ", String.format("Từ %s", form.getAgeFrom()), cellIndexFilter);
        }
        if ("0".equals(form.getAgeFrom()) && !"".equals(form.getAgeTo())) {
            createFilterRow("Tuổi ", String.format("Đến %s", form.getAgeTo()), cellIndexFilter);
        }
//        if (StringUtils.isNotEmpty(form.getDeathTimeFrom()) || StringUtils.isNotEmpty(form.getDeathTimeTo())) {
//            createFilterRow("Ngày tử vong ", String.format("%s  %s", (StringUtils.isNotEmpty(form.getDeathTimeFrom()) ? ("từ " + form.getDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getDeathTimeTo()) ? ("đến " + form.getDeathTimeTo()) : ""), cellIndexFilter);
//        }
        if (StringUtils.isNotEmpty(form.getGender())) {
            createFilterRow("Giới tính", form.getOptions().get(ParameterEntity.GENDER).getOrDefault(form.getGender(), "#" + form.getGender()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getJob())) {
            createFilterRow("Nghề nghiệp", form.getOptions().get(ParameterEntity.JOB).getOrDefault(form.getJob(), "#" + form.getJob()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getRace())) {
            createFilterRow("Dân tộc", form.getOptions().get(ParameterEntity.RACE).get(form.getRace()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getTransmision())) {
            createFilterRow("Đường lây nhiễm", form.getOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(form.getTransmision()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getObject())) {
            createFilterRow("Nhóm đối tượng", form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(form.getObject()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getTreatment())) {
            createFilterRow("Trạng thái điều trị", form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(form.getTreatment()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getResident())) {
            createFilterRow("Hiện trạng cư trú", form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(form.getResident()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getPatientStatus())) {
            createFilterRow("Trạng thái người nhiễm", (StringUtils.isEmpty(form.getPatientStatus()) || form.getPatientStatus().contains(",") ? " còn sống, tử vong" : "alive".equals(form.getPatientStatus()) ? "Còn sống" : "Tử vong"), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getDeathTimeFrom()) || StringUtils.isNotEmpty(form.getDeathTimeTo())) {
            createFilterRow("Ngày tử vong", String.format("%s  %s", (StringUtils.isNotEmpty(form.getDeathTimeFrom()) ? ("từ " + form.getDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getDeathTimeTo()) ? ("đến " + form.getDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getRequestDeathTimeFrom()) || StringUtils.isNotEmpty(form.getRequestDeathTimeTo())) {
            createFilterRow("Ngày báo tử vong", String.format("%s  %s", (StringUtils.isNotEmpty(form.getRequestDeathTimeFrom()) ? ("từ " + form.getRequestDeathTimeFrom()) : ""), StringUtils.isNotEmpty(form.getRequestDeathTimeTo()) ? ("đến " + form.getRequestDeathTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getUpdateTimeFrom()) || StringUtils.isNotEmpty(form.getUpdateTimeTo())) {
            createFilterRow("Ngày cập nhật", String.format("%s  %s", (StringUtils.isNotEmpty(form.getUpdateTimeFrom()) ? ("từ " + form.getUpdateTimeFrom()) : ""), StringUtils.isNotEmpty(form.getUpdateTimeTo()) ? ("đến " + form.getUpdateTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getAidsFrom()) || StringUtils.isNotEmpty(form.getAidsTo())) {
            createFilterRow("Ngày chẩn đoán AIDS", String.format("%s  %s", (StringUtils.isNotEmpty(form.getAidsFrom()) ? ("từ " + form.getAidsFrom()) : ""), StringUtils.isNotEmpty(form.getAidsTo()) ? ("đến " + form.getAidsTo()) : ""), cellIndexFilter);
        }
        //filter info default
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {
        
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();//dùng để merge khi không có thông tin
        Cell cell;

        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        Font fontNormal = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(false);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        CellStyle rowNormal = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(fontNormal);

        CellStyle rowLeft = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.LEFT);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        //Tiêu đề
        row = createTableHeaderRow("TT", "Đơn vị báo cáo", "Nam", null, "Nữ", null, "Không rõ", null, "Tổng", null);
        rownum = row.getRowNum();
        setMerge(rownum, rownum, 2, 3, true);
        setMerge(rownum, rownum, 4, 5, true);
        setMerge(rownum, rownum, 6, 7, true);
        setMerge(rownum, rownum, 8, 9, true);
        createTableHeaderRow(null, null, "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%");
        setMerge(rownum, rownum + 1, 0, 0, true);
        setMerge(rownum, rownum + 1, 1, 1, true);

        // Biến tổng      
        int maleTotal = 0;
        int femaleTotal = 0;
        int otherTotal = 0;

        if (form.getTable() != null  && form.getSum() > 0) {

            if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
                row = createTableRow("", "Toàn quốc");
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 1, 9, true);
            }
            int stt = 1;

            if (form.getTable() != null) {
                for (TablePacDeadForm genderForm : form.getTable()) {

                    // Cộng tổng cộng
                    maleTotal += genderForm.getMale();
                    femaleTotal += genderForm.getFemale();
                    otherTotal += genderForm.getOther();

                    // Hiển thị tỉnh
                    // KHông phải VAAC hoặc là VAAC không chọn ht tỉnh
                    if ((StringUtils.isNotEmpty(form.getLevelDisplay()) && !"province".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC())) {
                        row = createTableRow(stt, genderForm.getDisplayName());
                        rownum = row.getRowNum();
                        setMerge(rownum, rownum, 1, 9, true);
                    }

                    // TH chọn ht tỉnh và login VAAC
                    if ((StringUtils.isEmpty(form.getLevelDisplay()) || "province".equals(form.getLevelDisplay())) && form.isVAAC()) {
                        createTableRow(stt, genderForm.getDisplayName(), genderForm.getMale(), genderForm.getMalePercent(),
                                genderForm.getFemale(), genderForm.getFemalePercent(), genderForm.getOther(), genderForm.getOtherPercent(),
                                genderForm.getSum(), genderForm.getSumPercent(form.getSum()));
                    }

                    // Hiển thị huyện
                    if (genderForm.getChilds() != null && !genderForm.getChilds().isEmpty()) {
                        for (TablePacDeadForm child : genderForm.getChilds()) {
                            if (("ward".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC() && "ward".equals(form.getLevelDisplay()) && !"other".equals(genderForm.getWardID()))) {
                                row = createTableRow("", "    " + child.getDisplayName());
                                rownum = row.getRowNum();
                                setMerge(rownum, rownum, 1, 9, true);
                            }

                            if ("district".equals(form.getLevelDisplay()) || "other".equals(genderForm.getWardID())) {
                                createTableRow("", "Tỉnh khác".equals(genderForm.getDisplayName()) ? child.getDisplayName() : "     " + child.getDisplayName(), child.getMale(), child.getMalePercent(),
                                        child.getFemale(), child.getFemalePercent(), child.getOther(), child.getOtherPercent(),
                                        child.getSum(), child.getSumPercent(form.getSum()));
                            }

                            // Lặp hiển thị xã
                            if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                for (TablePacDeadForm childWard : child.getChilds()) {
                                    createTableRow("", "        " + childWard.getDisplayName(), childWard.getMale(), childWard.getMalePercent(),
                                            childWard.getFemale(), childWard.getFemalePercent(), childWard.getOther(), childWard.getOtherPercent(),
                                            childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                }
                            }
                            // Tổng huyện
                            if (!"Tỉnh khác".equals(genderForm.getDisplayName()) && "ward".equals(form.getLevelDisplay())
                                    && (StringUtils.isEmpty(form.getDistrict()) || form.getDistrict().contains(",")) && genderForm.getChilds() != null && genderForm.getChilds().size() > 1) {
                                createTableRow("", "     Tổng huyện", child.getMale(), child.getMalePercent(),
                                        child.getFemale(), child.getFemalePercent(), child.getOther(), child.getOtherPercent(),
                                        child.getSum(), child.getSumPercent(form.getSum()));
                            }
                        }
                    }

                    // Tổng tỉnh
                    if (form.getTable().size() > 1 && (!"province".equals(form.getLevelDisplay()) && (StringUtils.isEmpty(form.getProvince()) || form.getProvince().contains(","))) && !"Tỉnh khác".equals(genderForm.getDisplayName())) {
                        createTableRow("", "Tổng tỉnh", genderForm.getMale(), genderForm.getMalePercent(),
                                genderForm.getFemale(), genderForm.getFemalePercent(), genderForm.getOther(), genderForm.getOtherPercent(),
                                genderForm.getSum(), genderForm.getSumPercent(form.getSum()));
                    }
                    stt++;
                }

                // Dòng tổng cộng
                row = createTableRow("Tổng cộng", null, maleTotal, TextUtils.toPercent(maleTotal, form.getSum()), femaleTotal,
                        TextUtils.toPercent(femaleTotal, form.getSum()), otherTotal, TextUtils.toPercent(otherTotal, form.getSum()),
                        form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 0, 1, true);
            }
        }
    }
}
