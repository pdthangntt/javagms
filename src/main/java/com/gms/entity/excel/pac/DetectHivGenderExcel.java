package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.DetectHivGenderForm;
import com.gms.entity.form.pac.TableDetectHivGenderForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author TrangBN
 */
public class DetectHivGenderExcel extends BaseView implements IExportExcel {

    private DetectHivGenderForm form;
    private String extension;

    public DetectHivGenderExcel(DetectHivGenderForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "GENDER";
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
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(120));

        setMerge(1, 1, 0, 9, false);

        //Dòng đầu tiên để trắng
        createRow();
        createTitleRow(form.getTitle());
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Địa chỉ", "".equals(form.getTitleLocationDisplay()) ? form.getProvinceName() : form.getTitleLocationDisplay(), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getConfirmTimeTo())) {
            createFilterRow("Ngày XN khẳng định", String.format("%s  %s", (StringUtils.isNotEmpty(form.getConfirmTimeFrom()) ? ("từ " + form.getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getConfirmTimeTo()) ? ("đến " + form.getConfirmTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getManageTimeFrom()) || StringUtils.isNotEmpty(form.getManageTimeTo())) {
            createFilterRow("Ngày quản lý", String.format("%s  %s", (StringUtils.isNotEmpty(form.getManageTimeFrom()) ? ("từ " + form.getManageTimeFrom()) : ""), StringUtils.isNotEmpty(form.getManageTimeTo()) ? ("đến " + form.getManageTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getInputTimeFrom()) || StringUtils.isNotEmpty(form.getInputTimeTo())) {
            createFilterRow("Ngày nhập liệu", String.format("%s  %s", (StringUtils.isNotEmpty(form.getInputTimeFrom()) ? ("từ " + form.getInputTimeFrom()) : ""), StringUtils.isNotEmpty(form.getInputTimeTo()) ? ("đến " + form.getInputTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getAidsFrom()) || StringUtils.isNotEmpty(form.getAidsTo())) {
            createFilterRow("Ngày chẩn đoán AIDS", String.format("%s  %s", (StringUtils.isNotEmpty(form.getAidsFrom()) ? ("từ " + form.getAidsFrom()) : ""), StringUtils.isNotEmpty(form.getAidsTo()) ? ("đến " + form.getAidsTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getUpdateTimeFrom()) || StringUtils.isNotEmpty(form.getUpdateTimeTo())) {
            createFilterRow("Ngày cập nhật", String.format("%s  %s", (StringUtils.isNotEmpty(form.getUpdateTimeFrom()) ? ("từ " + form.getUpdateTimeFrom()) : ""), StringUtils.isNotEmpty(form.getUpdateTimeTo()) ? ("đến " + form.getUpdateTimeTo()) : ""), cellIndexFilter);
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
            createFilterRow("Trạng thái người nhiễm", (StringUtils.isEmpty(form.getPatientStatus()) || form.getPatientStatus().contains(",") ? " còn sống, tử vong" : "alive".equals(form.getPatientStatus()) ? " còn sống" : " tử vong"), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getPlaceTest())) {
            createFilterRow("Nơi XN khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getPlaceTest()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getFacility())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getFacility()), cellIndexFilter);
        }
        //filter info default
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() {

        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rownum = row.getRowNum();//dùng để merge khi không có thông tin
        
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
        String displayName = "";
        
        if (form.getTable() != null  && form.getSum() > 0) {

            if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
                row = createTableRow("", "Toàn quốc");
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 1, 9, true);
            }
            int stt = 1;

            if (form.getTable() != null) {
                for (TableDetectHivGenderForm genderForm : form.getTable()) {

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
                        for (TableDetectHivGenderForm child : genderForm.getChilds()) {
                            if (("ward".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC() && "ward".equals(form.getLevelDisplay()) && !"other".equals(genderForm.getWardID()))) {
                                row = createTableRow("", "    " + child.getDisplayName());
                                rownum = row.getRowNum();
                                setMerge(rownum, rownum, 1, 9, true);
                            }

                            if ("district".equals(form.getLevelDisplay()) || "other".equals(genderForm.getWardID())) {
                                if (genderForm.getWardID() != null && genderForm.getWardID().equals("other")) {
                                    displayName = child.getDisplayName();
                                } else {
                                    displayName = String.format("%s%s", "    ", child.getDisplayName());
                                }
                                createTableRow("", displayName, child.getMale(), child.getMalePercent(),
                                        child.getFemale(), child.getFemalePercent(), child.getOther(), child.getOtherPercent(),
                                        child.getSum(), child.getSumPercent(form.getSum()));
                            }

                            // Lặp hiển thị xã
                            if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                for (TableDetectHivGenderForm childWard : child.getChilds()) {
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
