package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.DetectHivTransmissionForm;
import com.gms.entity.form.pac.TableDetectHivTransmissionForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class DetectHivTransmisionExcel extends BaseView implements IExportExcel {

    private DetectHivTransmissionForm form;
    private String extension;
    private String provinceParam;

    public DetectHivTransmisionExcel(DetectHivTransmissionForm form, String extension, String provinceParam) {
        this.useStyle = false;
        this.form = form;
        this.extension = extension;
        this.sheetName = "Transmision";
        this.provinceParam = provinceParam;
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
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(100));

        setMerge(1, 1, 0, 13, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Địa chỉ", ("".equals(form.getTitleLocation()) && form.isPAC()) ? form.getProvinceName() : ("".equals(form.getTitleLocation()) && form.isTTYT()) ? String.format("%s, %s", form.getDistrictName(), form.getProvinceName()) : ("".equals(form.getTitleLocation()) && form.isTYT()) ? String.format("%s, %s, %s", form.getWardName(), form.getDistrictName(), form.getProvinceName()) : ("".equals(form.getTitleLocation()) && form.isVAAC()) ? form.getProvinceName() : form.getTitleLocation(), cellIndexFilter);
        if (StringUtils.isNotEmpty(form.getConfirmTimeFrom()) || StringUtils.isNotEmpty(form.getConfirmTimeTo())) {
            createFilterRow("Ngày XN khẳng định", String.format("%s  %s", (StringUtils.isNotEmpty(form.getConfirmTimeFrom()) ? ("từ " + form.getConfirmTimeFrom()) : ""), StringUtils.isNotEmpty(form.getConfirmTimeTo()) ? ("đến " + form.getConfirmTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getManageTimeFrom()) || StringUtils.isNotEmpty(form.getManageTimeTo())) {
            createFilterRow("Ngày quản lý", String.format("%s  %s", (StringUtils.isNotEmpty(form.getManageTimeFrom()) ? ("từ " + form.getManageTimeFrom()) : ""), StringUtils.isNotEmpty(form.getManageTimeTo()) ? ("đến " + form.getManageTimeTo()) : ""), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getCreateAtFrom()) || StringUtils.isNotEmpty(form.getCreateAtTo())) {
            createFilterRow("Ngày nhập liệu", String.format("%s  %s", (StringUtils.isNotEmpty(form.getCreateAtFrom()) ? ("từ " + form.getCreateAtFrom()) : ""), StringUtils.isNotEmpty(form.getCreateAtTo()) ? ("đến " + form.getCreateAtTo()) : ""), cellIndexFilter);
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
            createFilterRow("Trạng thái người nhiễm", (StringUtils.isEmpty(form.getPatientStatus()) || form.getPatientStatus().contains(",") ? " còn sống, tử vong" : "alive".equals(form.getPatientStatus()) ? "Còn sống" : "Tử vong"), cellIndexFilter);
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
        getCurrentSheet();
        TableDetectHivTransmissionForm lastItem = form.getTable().isEmpty() ? null : form.getTable().get(form.getTable().size() - 1);
        //Dòng đầu tiên để trắng
        Row row = createRow();
        int rowIndex = getLastRowNumber();
        //Tiêu đề
        createTableHeaderRow("TT", "Đơn vị báo cáo", "Lây qua đường máu", "",
                "Lây qua đường tình dục", "", "Mẹ truyền sang con", "",
                "Không rõ", "", "Không có thông tin", "", "Tổng", "");
        createTableHeaderRow("", "", "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%", "Số ca", "%");

        setMerge(rowIndex, rowIndex + 1, 0, 0, false);
        setMerge(rowIndex, rowIndex + 1, 1, 1, false);
        setMerge(rowIndex, rowIndex, 2, 3, false);
        setMerge(rowIndex, rowIndex, 4, 5, false);
        setMerge(rowIndex, rowIndex, 6, 7, false);
        setMerge(rowIndex, rowIndex, 8, 9, false);
        setMerge(rowIndex, rowIndex, 10, 11, false);
        setMerge(rowIndex, rowIndex, 12, 13, false);
        if (lastItem == null || lastItem.getTong() == 0) {
            createTableEmptyRow("Không tìm thấy thông tin", 14);
            return;
        }

        if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
            row = createTableRow("", "Toàn quốc");
            setMerge(row.getRowNum(), row.getRowNum(), 1, 13, true);
        }

        if (form.getTable() != null && form.getTable().size() > 0) {
            for (TableDetectHivTransmissionForm item : form.getTable()) {
                if ("all".equals(item.getProvinceID())) {
                    continue;
                }
                if ("province".equals(form.getLevelDisplay())) { //tổng tỉnh
                    //Add row giá trị
                    createTableRow(item.getStt(), item.getDisplayName(), item.getBlood(), item.getBloodPercent(),
                            item.getSexuality(), item.getSexualityPercent(), item.getMomtochild(), item.getMomtochild(),
                            item.getUnclear(), item.getUnclearPercent(), item.getNoinformation(), item.getNoinformationPercent(),
                            item.getTong(), item.getTongPercent(lastItem.getTong()));
                }

                if (!"province".equals(form.getLevelDisplay())) {
                    row = createTableRow(item.getStt(), item.getDisplayName());
                    setMerge(row.getRowNum(), row.getRowNum(), 1, 13, true);
                }

                if (item.getChilds() != null && item.getChilds().size() > 0) {
                    for (TableDetectHivTransmissionForm pChild : item.getChilds()) {
                        if (!"ward".equals(form.getLevelDisplay()) || "Tỉnh khác".equals(item.getDisplayName())) {
                            createTableRow("", "Tỉnh khác".equals(item.getDisplayName()) ? pChild.getDisplayName() : "     " + pChild.getDisplayName(), pChild.getBlood(), pChild.getBloodPercent(),
                                    pChild.getSexuality(), pChild.getSexualityPercent(), pChild.getMomtochild(), pChild.getMomtochild(),
                                    pChild.getUnclear(), pChild.getUnclearPercent(), pChild.getNoinformation(), pChild.getNoinformationPercent(),
                                    pChild.getTong(), pChild.getTongPercent(lastItem.getTong()));
                        }

                        if ("ward".equals(form.getLevelDisplay()) && !"Tỉnh khác".equals(item.getDisplayName())) {
                            row = createTableRow("", "     " + pChild.getDisplayName());
                            setMerge(row.getRowNum(), row.getRowNum(), 1, 13, true);
                        }

                        if (pChild.getChilds() != null && pChild.getChilds().size() > 0) {
                            for (TableDetectHivTransmissionForm dChild : pChild.getChilds()) {
                                if (true) {
                                    createTableRow("", "         " + dChild.getDisplayName(), dChild.getBlood(), dChild.getBloodPercent(),
                                            dChild.getSexuality(), dChild.getSexualityPercent(), dChild.getMomtochild(), dChild.getMomtochild(),
                                            dChild.getUnclear(), dChild.getUnclearPercent(), dChild.getNoinformation(), dChild.getNoinformationPercent(),
                                            dChild.getTong(), dChild.getTongPercent(lastItem.getTong()));
                                }
                            }
                        }

                        if ("ward".equals(form.getLevelDisplay()) && item.getChilds() != null && item.getChilds().size() > 1 && !"Tỉnh khác".equals(item.getDisplayName())) {//tổng huyện
                            if (true) {
                                createTableRow("", "     " + "Tổng huyện", pChild.getBlood(), pChild.getBloodPercent(),
                                        pChild.getSexuality(), pChild.getSexualityPercent(), pChild.getMomtochild(), pChild.getMomtochild(),
                                        pChild.getUnclear(), pChild.getUnclearPercent(), pChild.getNoinformation(), pChild.getNoinformationPercent(),
                                        pChild.getTong(), pChild.getTongPercent(lastItem.getTong()));

                            }
                        }
                    } //end for huyện
                }//end for Tỉnh

                if (!"province".equals(form.getLevelDisplay()) && form.getTable().size() > 1 && !"Tỉnh khác".equals(item.getDisplayName())) {
                    if (true) {
                        createTableRow("", "Tổng tỉnh", item.getBlood(), item.getBloodPercent(),
                                item.getSexuality(), item.getSexualityPercent(), item.getMomtochild(), item.getMomtochild(),
                                item.getUnclear(), item.getUnclearPercent(), item.getNoinformation(), item.getNoinformationPercent(),
                                item.getTong(), item.getTongPercent(lastItem.getTong()));
                    }
                }
            }
            
            createTableRow("TỔNG CỘNG", null, lastItem.getBlood(), lastItem.getBloodPercent(),
                    lastItem.getSexuality(), lastItem.getSexualityPercent(), lastItem.getMomtochild(), lastItem.getMomtochild(),
                    lastItem.getUnclear(), lastItem.getUnclearPercent(), lastItem.getNoinformation(), lastItem.getNoinformationPercent(),
                    lastItem.getTong(), lastItem.getTongPercent(lastItem.getTong()));
            setMerge(getLastRowNumber() -1, getLastRowNumber()-1, 0, 1, true);
        }
    }
}
