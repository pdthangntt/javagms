package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.DetectHivAgeForm;
import com.gms.entity.form.pac.TableDetectHivAgeForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author TrangBN
 */
public class DetectHivAgeExcel extends BaseView implements IExportExcel {

    private DetectHivAgeForm form;
    private String extension;

    public DetectHivAgeExcel(DetectHivAgeForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "AGE";
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

        boolean age1Search = StringUtils.isNotEmpty(form.getAge1());
        boolean age2Search = StringUtils.isNotEmpty(form.getAge2());
        boolean age3Search = StringUtils.isNotEmpty(form.getAge3());
        boolean age4Search = StringUtils.isNotEmpty(form.getAge4());
        boolean age5Search = StringUtils.isNotEmpty(form.getAge5());

        int colNumMerge = 3;

        // Cộng tổng cộng
        if (age1Search) {
            colNumMerge += 2;
        }
        if (age2Search) {
            colNumMerge += 2;
        }
        if (age3Search) {
            colNumMerge += 2;
        }
        if (age4Search) {
            colNumMerge += 2;
        }
        if (age5Search) {
            colNumMerge += 2;
        }
        
        setMerge(1, 1, 0, colNumMerge, false);

        //Dòng đầu tiên để trắng
        createRow();
        createTitleRow(form.getTitle());
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Địa chỉ", "".equals(form.getTitleLocationDisplay()) ? form.getProvinceName() : form.getTitleLocationDisplay(), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getDateFilter())) {
            createFilterRow("Tiêu chí tính tuổi", ("ngayxn".equals(form.getDateFilter()) ? "Theo ngày xét nghiệm" : "Theo ngày hiện tại"), cellIndexFilter);
        }
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
        if (StringUtils.isNotEmpty(form.getGender())) {
            createFilterRow("Giới tính", form.getOptions().get(ParameterEntity.GENDER).get(form.getGender()), cellIndexFilter);
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
        int rownum;//dùng để merge khi không có thông tin

        if (StringUtils.isNotEmpty(form.getAge5())) {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", form.getAge1(), null, form.getAge2() , null, form.getAge3(), null, form.getAge4(), null,form.getAge5(), null, "Tổng", null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 3, true);
            setMerge(rownum, rownum, 4, 5, true);
            setMerge(rownum, rownum, 6, 7, true);
            setMerge(rownum, rownum, 8, 9, true);
            setMerge(rownum, rownum, 10, 11, true);
            setMerge(rownum, rownum, 12, 13, true);
            createTableHeaderRow(null, null, "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%","Số ca", "%","Số ca", "%");
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);
        } else if (StringUtils.isNotEmpty(form.getAge4())) {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", form.getAge1(), null, form.getAge2() , null, form.getAge3(), null, form.getAge4(), null, "Tổng", null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 3, true);
            setMerge(rownum, rownum, 4, 5, true);
            setMerge(rownum, rownum, 6, 7, true);
            setMerge(rownum, rownum, 8, 9, true);
            setMerge(rownum, rownum, 10, 11, true);
            createTableHeaderRow(null, null, "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%","Số ca", "%");
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);
        } else if (StringUtils.isNotEmpty(form.getAge3())) {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", form.getAge1(), null, form.getAge2() , null, form.getAge3(), null, "Tổng", null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 3, true);
            setMerge(rownum, rownum, 4, 5, true);
            setMerge(rownum, rownum, 6, 7, true);
            setMerge(rownum, rownum, 8, 9, true);
            createTableHeaderRow(null, null, "Số ca", "%", "Số ca", "%", "Số ca", "%","Số ca", "%");
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);
        } else if (StringUtils.isNotEmpty(form.getAge2())) {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", form.getAge1(), null, form.getAge2() , null, "Tổng", null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 3, true);
            setMerge(rownum, rownum, 4, 5, true);
            setMerge(rownum, rownum, 6, 7, true);
            createTableHeaderRow(null, null, "Số ca", "%", "Số ca", "%","Số ca", "%");
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);
        } else {
            row = createTableHeaderRow("TT", "Đơn vị báo cáo", form.getAge1(), null,"Tổng", null);
            rownum = row.getRowNum();
            setMerge(rownum, rownum, 2, 3, true);
            setMerge(rownum, rownum, 4, 5, true);
            createTableHeaderRow(null, null, "Số ca", "%","Số ca", "%");
            setMerge(rownum, rownum + 1, 0, 0, true);
            setMerge(rownum, rownum + 1, 1, 1, true);
        }
        
        // Biến tổng      
        int age1Total = 0;
        int age2Total = 0;
        int age3Total = 0;
        int age4Total = 0;
        int age5Total = 0;

        boolean age1Search = StringUtils.isNotEmpty(form.getAge1());
        boolean age2Search = StringUtils.isNotEmpty(form.getAge2());
        boolean age3Search = StringUtils.isNotEmpty(form.getAge3());
        boolean age4Search = StringUtils.isNotEmpty(form.getAge4());
        boolean age5Search = StringUtils.isNotEmpty(form.getAge5());

        int stt = 1;
        
        int colNumMerge = 3;

        // Cộng tổng cộng
        if (age1Search) {
            colNumMerge += 2;
        }
        if (age2Search) {
            colNumMerge += 2;
        }
        if (age3Search) {
            colNumMerge += 2;
        }
        if (age4Search) {
            colNumMerge += 2;
        }
        if (age5Search) {
            colNumMerge += 2;
        }

        if (form.getTable() != null && form.getSum() > 0) {

            if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
                row = createTableRow("", "Toàn quốc");
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 1, colNumMerge, true);
            }

            String displayName = "";
            if (form.getTable() != null) {
                for (TableDetectHivAgeForm ageItemForm : form.getTable()) {

                    // Cộng tổng cộng
                    age1Total += ageItemForm.getAge1();
                    age2Total += ageItemForm.getAge2();
                    age3Total += ageItemForm.getAge3();
                    age4Total += ageItemForm.getAge4();
                    age5Total += ageItemForm.getAge5();

                    // Hiển thị tỉnh
                    // KHông phải VAAC hoặc là VAAC không chọn ht tỉnh
                    if ((StringUtils.isNotEmpty(form.getLevelDisplay()) && !"province".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC())) {
                        row = createTableRow(stt, ageItemForm.getDisplayName());
                        rownum = row.getRowNum();
                        setMerge(rownum, rownum, 1, colNumMerge, true);
                    }

                    // TH chọn ht tỉnh và login VAAC
                    if ((StringUtils.isEmpty(form.getLevelDisplay()) || "province".equals(form.getLevelDisplay())) && form.isVAAC()) {
                        
                        if (StringUtils.isNotEmpty(form.getAge5())) {
                            row = createTableRow(stt, ageItemForm.getDisplayName(), ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getAge4(), ageItemForm.getAge4Percent(),
                                                                              ageItemForm.getAge5(), ageItemForm.getAge5Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge4())) {
                            row = createTableRow(stt, ageItemForm.getDisplayName(), ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getAge4(), ageItemForm.getAge4Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge3())) {
                            row = createTableRow(stt, ageItemForm.getDisplayName(), ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge2())) {
                            row = createTableRow(stt, ageItemForm.getDisplayName(), ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else {
                            row = createTableRow(stt, ageItemForm.getDisplayName(), ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        }
                    }

                    // Hiển thị huyện
                    if (ageItemForm.getChilds() != null && !ageItemForm.getChilds().isEmpty()) {
                        for (TableDetectHivAgeForm child : ageItemForm.getChilds()) {
                            if (("ward".equals(form.getLevelDisplay()) && form.isVAAC()) || (!form.isVAAC() && "ward".equals(form.getLevelDisplay()) && !"other".equals(ageItemForm.getWardID()))) {
                                row = createTableRow("", "    " + child.getDisplayName());
                                rownum = row.getRowNum();
                                setMerge(rownum, rownum, 1, colNumMerge, true);
                            }

                            if ("district".equals(form.getLevelDisplay()) || "other".equals(ageItemForm.getWardID())) {
                                if (ageItemForm.getWardID() != null && ageItemForm.getWardID().equals("other")) {
                                    displayName = child.getDisplayName();
                                } else {
                                    displayName = String.format("%s%s", "    ", child.getDisplayName());
                                }
                                
                                if (StringUtils.isNotEmpty(form.getAge5())) {
                                    row = createTableRow("", displayName, child.getAge1(), child.getAge1Percent(),
                                                                                      child.getAge2(), child.getAge2Percent(), 
                                                                                      child.getAge3(), child.getAge3Percent(),
                                                                                      child.getAge4(), child.getAge4Percent(),
                                                                                      child.getAge5(), child.getAge5Percent(),
                                                                                      child.getSum(), child.getSumPercent(form.getSum()));
                                } else if (StringUtils.isNotEmpty(form.getAge4())) {
                                    row = createTableRow("", displayName, child.getAge1(), child.getAge1Percent(),
                                                                                      child.getAge2(), child.getAge2Percent(), 
                                                                                      child.getAge3(), child.getAge3Percent(),
                                                                                      child.getAge4(), child.getAge4Percent(),
                                                                                      child.getSum(), child.getSumPercent(form.getSum()));
                                } else if (StringUtils.isNotEmpty(form.getAge3())) {
                                    row = createTableRow("", displayName, child.getAge1(), child.getAge1Percent(),
                                                                                      child.getAge2(), child.getAge2Percent(), 
                                                                                      child.getAge3(), child.getAge3Percent(),
                                                                                      child.getSum(), child.getSumPercent(form.getSum()));
                                } else if (StringUtils.isNotEmpty(form.getAge2())) {
                                    row = createTableRow("", displayName, child.getAge1(), child.getAge1Percent(),
                                                                                      child.getAge2(), child.getAge2Percent(), 
                                                                                      child.getSum(), child.getSumPercent(form.getSum()));
                                } else {
                                    row = createTableRow("", displayName, child.getAge1(), child.getAge1Percent(),
                                                                                      child.getSum(), child.getSumPercent(form.getSum()));
                                }
                            }

                            // Lặp hiển thị xã
                            if (child.getChilds() != null && !child.getChilds().isEmpty()) {
                                for (TableDetectHivAgeForm childWard : child.getChilds()) {
                                    
                                    if (StringUtils.isNotEmpty(form.getAge5())) {
                                        row = createTableRow("", "        " + childWard.getDisplayName(), childWard.getAge1(), childWard.getAge1Percent(),
                                                                                          childWard.getAge2(), childWard.getAge2Percent(), 
                                                                                          childWard.getAge3(), childWard.getAge3Percent(),
                                                                                          childWard.getAge4(), childWard.getAge4Percent(),
                                                                                          childWard.getAge5(), childWard.getAge5Percent(),
                                                                                          childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge4())) {
                                        row = createTableRow("", "        " + childWard.getDisplayName(), childWard.getAge1(), childWard.getAge1Percent(),
                                                                                          childWard.getAge2(), childWard.getAge2Percent(), 
                                                                                          childWard.getAge3(), childWard.getAge3Percent(),
                                                                                          childWard.getAge4(), childWard.getAge4Percent(),
                                                                                          childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge3())) {
                                        row = createTableRow("", "        " + childWard.getDisplayName(), childWard.getAge1(), childWard.getAge1Percent(),
                                                                                          childWard.getAge2(), childWard.getAge2Percent(), 
                                                                                          childWard.getAge3(), childWard.getAge3Percent(),
                                                                                          childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge2())) {
                                        row = createTableRow("", "        " + childWard.getDisplayName(), childWard.getAge1(), childWard.getAge1Percent(),
                                                                                          childWard.getAge2(), childWard.getAge2Percent(), 
                                                                                          childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                    } else {
                                        row = createTableRow("", "        " + childWard.getDisplayName(), childWard.getAge1(), childWard.getAge1Percent(),
                                                                                          childWard.getSum(), childWard.getSumPercent(form.getSum()));
                                    }
                                }
                            }
                            // Tổng huyện
                            if (!"Tỉnh khác".equals(ageItemForm.getDisplayName()) && "ward".equals(form.getLevelDisplay())
                                    && (StringUtils.isEmpty(form.getDistrict()) || form.getDistrict().contains(",")) && ageItemForm.getChilds() != null && ageItemForm.getChilds().size() > 1) {
                                
                                if (StringUtils.isNotEmpty(form.getAge5())) {
                                        row = createTableRow("", "    Tổng huyện", child.getAge1(), child.getAge1Percent(),
                                                                                          child.getAge2(), child.getAge2Percent(), 
                                                                                          child.getAge3(), child.getAge3Percent(),
                                                                                          child.getAge4(), child.getAge4Percent(),
                                                                                          child.getAge5(), child.getAge5Percent(),
                                                                                          child.getSum(), child.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge4())) {
                                        row = createTableRow("", "    Tổng huyện", child.getAge1(), child.getAge1Percent(),
                                                                                          child.getAge2(), child.getAge2Percent(), 
                                                                                          child.getAge3(), child.getAge3Percent(),
                                                                                          child.getAge4(), child.getAge4Percent(),
                                                                                          child.getSum(), child.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge3())) {
                                        row = createTableRow("", "    Tổng huyện", child.getAge1(), child.getAge1Percent(),
                                                                                          child.getAge2(), child.getAge2Percent(), 
                                                                                          child.getAge3(), child.getAge3Percent(),
                                                                                          child.getSum(), child.getSumPercent(form.getSum()));
                                    } else if (StringUtils.isNotEmpty(form.getAge2())) {
                                        row = createTableRow("", "    Tổng huyện", child.getAge1(), child.getAge1Percent(),
                                                                                          child.getAge2(), child.getAge2Percent(), 
                                                                                          child.getSum(), child.getSumPercent(form.getSum()));
                                    } else {
                                        row = createTableRow("", "    Tổng huyện", child.getAge1(), child.getAge1Percent(),
                                                                                          child.getSum(), child.getSumPercent(form.getSum()));
                                    }
                            }
                        }
                    }

                    // Tổng tỉnh
                    if (form.getTable().size() > 1 && (!"province".equals(form.getLevelDisplay()) && (StringUtils.isEmpty(form.getProvince()) || form.getProvince().contains(","))) && !"Tỉnh khác".equals(ageItemForm.getDisplayName())) {
                       
                        if (StringUtils.isNotEmpty(form.getAge5())) {
                            row = createTableRow("", "Tổng tỉnh", ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getAge4(), ageItemForm.getAge4Percent(),
                                                                              ageItemForm.getAge5(), ageItemForm.getAge5Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge4())) {
                            row = createTableRow("", "Tổng tỉnh", ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getAge4(), ageItemForm.getAge4Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge3())) {
                            row = createTableRow("", "Tổng tỉnh", ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getAge3(), ageItemForm.getAge3Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge2())) {
                            row = createTableRow("", "Tổng tỉnh", ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getAge2(), ageItemForm.getAge2Percent(), 
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        } else {
                            row = createTableRow("", "Tổng tỉnh", ageItemForm.getAge1(), ageItemForm.getAge1Percent(),
                                                                              ageItemForm.getSum(), ageItemForm.getSumPercent(form.getSum()));
                        }
                    }
                    stt++;
                }

                // Dòng tổng cộng
                if (StringUtils.isNotEmpty(form.getAge5())) {
                            row = createTableRow("Tổng cộng",  null, age1Total, TextUtils.toPercent(age1Total, form.getSum()),
                                                                              age2Total, TextUtils.toPercent(age2Total, form.getSum()), 
                                                                              age3Total, TextUtils.toPercent(age3Total, form.getSum()), 
                                                                              age4Total, TextUtils.toPercent(age4Total, form.getSum()), 
                                                                              age5Total, TextUtils.toPercent(age5Total, form.getSum()), 
                                                                              form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge4())) {
                            row = createTableRow("Tổng cộng",  null, age1Total, TextUtils.toPercent(age1Total, form.getSum()),
                                                                              age2Total, TextUtils.toPercent(age2Total, form.getSum()), 
                                                                              age3Total, TextUtils.toPercent(age3Total, form.getSum()), 
                                                                              age4Total, TextUtils.toPercent(age4Total, form.getSum()), 
                                                                              form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge3())) {
                            row = createTableRow("Tổng cộng",  null, age1Total, TextUtils.toPercent(age1Total, form.getSum()),
                                                                              age2Total, TextUtils.toPercent(age2Total, form.getSum()), 
                                                                              age3Total, TextUtils.toPercent(age3Total, form.getSum()), 
                                                                              form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                        } else if (StringUtils.isNotEmpty(form.getAge2())) {
                            row = createTableRow("Tổng cộng",  null,age1Total, TextUtils.toPercent(age1Total, form.getSum()),
                                                                              age2Total, TextUtils.toPercent(age2Total, form.getSum()), 
                                                                              form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                        } else {
                            row = createTableRow("Tổng cộng",  null, age1Total, TextUtils.toPercent(age1Total, form.getSum()),
                                                                              form.getSum(), TextUtils.toPercent(form.getSum(), form.getSum()));
                        }
                rownum = row.getRowNum();
                setMerge(rownum, rownum, 0, 1, true);
            }
        }
    }
}
