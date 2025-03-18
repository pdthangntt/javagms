package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.DetectHivObjectForm;
import com.gms.entity.form.pac.TableDetectHivObjectForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class DetectHivObjectExcel extends BaseView implements IExportExcel {

    private final DetectHivObjectForm form;
    private final String extension;

    public DetectHivObjectExcel(DetectHivObjectForm form, String extension) {
        this.useStyle = false;
        this.form = form;
        this.extension = extension;
        this.sheetName = "OBJECT";
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
        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(120));

        //Gộp cột dòng tiêu đề
        setMerge(1, 1, 0, 9, false);

        //Dòng đầu tiên để trắng
        createRow();
        //Tự động set title style
        createTitleRow(form.getTitle());
        //Tạo thêm dòng trắng
        createRow();

        //Thông tin tìm kiếm
        int cellIndexFilter = 1;
        createFilterRow("Địa chỉ", "".equals(form.getTitleLocation()) ? form.getProvinceName() : form.getTitleLocation(), cellIndexFilter);
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
//        if (StringUtils.isNotEmpty(form.getObject())) {
//            createFilterRow("Nhóm đối tượng", form.getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(form.getObject()), cellIndexFilter);
//        }
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
        //Dòng đầu tiên để trắng
        Row row = createRow();

        //Tiêu đề
        createTableHeaderRow("TT", "Đơn vị báo cáo", "Nghiện chích ma túy",
                "Người hành nghề mại dâm", "Nam quan hệ tình dục với nam",
                "Vợ/chồng/bạn tình của người nhiễm HIV",
                "Phụ nữ mang thai", "Bệnh nhân Lao",
                "Bệnh nhân Hoa liễu",
                "Phạm nhân",
                "Lây truyền mẹ con",
                "Thanh niên khám tuyển nghĩa vụ quân sự",
                "Đối tượng khác",
                "Không có thông tin", "Tổng");

        if (form.getTotal() == 0) {
            createTableEmptyRow("Không tìm thấy thông tin", 15);
            return;
        }

        if (form.isVAAC() && StringUtils.isEmpty(form.getProvince())) {
            row = createTableRow("", "Toàn quốc");
            setMerge(row.getRowNum(), row.getRowNum(), 1, 14, true);
        }

        if (form.getTable() != null && form.getTable().size() > 0) {
            for (TableDetectHivObjectForm item : form.getTable()) {
                if ("province".equals(form.getLevelDisplay())) { //tổng tỉnh
                    //Add row giá trị
                    createTableRow(item.getStt(), item.getDisplayName(), item.getNtmt(), item.getMd(), item.getMsm(), item.getVcbtbc(), item.getPnmt(), item.getLao(), item.getHoalieu(), item.getPhamnhan(), item.getMecon(), item.getNghiavu(), item.getKhac(), item.getKhongthongtin(), item.getSum());
                    //Add row %
                    row = createTableRow(null, null, item.getPercentntmt(), item.getPercentmd(), item.getPercentmsm(), item.getPercentvcbtbc(), item.getPercentpnmt(), item.getPercentlao(), item.getPercenthoalieu(), item.getPercentphamnhan(), item.getPercentmecon(), item.getPercentnghiavu(), item.getPercentkhac(), item.getPercentkhongthongtin(), item.getSumPercent(form.getTotal()));
                    setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 0, true);
                    setMerge(row.getRowNum() - 1, row.getRowNum(), 1, 1, true);
                }

                if (!"province".equals(form.getLevelDisplay())) {
                    row = createTableRow(item.getStt(), item.getDisplayName());
                    setMerge(row.getRowNum(), row.getRowNum(), 1, 14, true);
                }

                if (item.getChilds() != null && item.getChilds().size() > 0) {
                    for (TableDetectHivObjectForm pChild : item.getChilds()) {
                        if (!"ward".equals(form.getLevelDisplay()) || "Tỉnh khác".equals(item.getDisplayName())) {
                            //Add row giá trị
                            createTableRow("", "Tỉnh khác".equals(item.getDisplayName()) ? pChild.getDisplayName() : "     " + pChild.getDisplayName(), pChild.getNtmt(), pChild.getMd(), pChild.getMsm(), pChild.getVcbtbc(), pChild.getPnmt(), pChild.getLao(), pChild.getHoalieu(), pChild.getPhamnhan(), pChild.getMecon(), pChild.getNghiavu(), pChild.getKhac(), pChild.getKhongthongtin(), pChild.getSum());
                            //Add row %
                            row = createTableRow(null, null, pChild.getPercentntmt(), pChild.getPercentmd(), pChild.getPercentmsm(), pChild.getPercentvcbtbc(), pChild.getPercentpnmt(), pChild.getPercentlao(), pChild.getPercenthoalieu(), pChild.getPercentphamnhan(), pChild.getPercentmecon(), pChild.getPercentnghiavu(), pChild.getPercentkhac(), pChild.getPercentkhongthongtin(), pChild.getSumPercent(form.getTotal()));
                            //merge
                            setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 0, true);
                            setMerge(row.getRowNum() - 1, row.getRowNum(), 1, 1, true);
                        }

                        if ("ward".equals(form.getLevelDisplay()) && !"Tỉnh khác".equals(item.getDisplayName())) {
                            row = createTableRow("", "     " + pChild.getDisplayName());
                            setMerge(row.getRowNum(), row.getRowNum(), 1, 14, true);
                        }

                        if (pChild.getChilds() != null && pChild.getChilds().size() > 0) {
                            for (TableDetectHivObjectForm dChild : pChild.getChilds()) {
                                if (!form.isTTYT() || !(form.isTTYT() && !form.isWard(dChild.getWardID()) && dChild.getSum() == 0)) {
                                    //Add row giá trị
                                    createTableRow("", "          " + dChild.getDisplayName(), dChild.getNtmt(), dChild.getMd(), dChild.getMsm(), dChild.getVcbtbc(), dChild.getPnmt(), dChild.getLao(), dChild.getHoalieu(), dChild.getPhamnhan(), dChild.getMecon(), dChild.getNghiavu(), dChild.getKhac(), dChild.getKhongthongtin(), dChild.getSum());
                                    //Add row %
                                    row = createTableRow(null, null, dChild.getPercentntmt(), dChild.getPercentmd(), dChild.getPercentmsm(), dChild.getPercentvcbtbc(), dChild.getPercentpnmt(), dChild.getPercentlao(), dChild.getPercenthoalieu(), dChild.getPercentphamnhan(), dChild.getPercentmecon(), dChild.getPercentnghiavu(), dChild.getPercentkhac(), dChild.getPercentkhongthongtin(), dChild.getSumPercent(form.getTotal()));
                                    //merge
                                    setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 0, true);
                                    setMerge(row.getRowNum() - 1, row.getRowNum(), 1, 1, true);
                                }
                            }
                        }

                        if ("ward".equals(form.getLevelDisplay()) && item.getChilds() != null && item.getChilds().size() > 1 && !"Tỉnh khác".equals(item.getDisplayName())) {//tổng huyện
                            if (true) {
                                //Add row giá trị
                                createTableRow("", "     " + "Tổng huyện", pChild.getNtmt(), pChild.getMd(), pChild.getMsm(), pChild.getVcbtbc(), pChild.getPnmt(), pChild.getLao(), pChild.getHoalieu(), pChild.getPhamnhan(), pChild.getMecon(), pChild.getNghiavu(), pChild.getKhac(), pChild.getKhongthongtin(), pChild.getSum());
                                //Add row %
                                row = createTableRow(null, null, pChild.getPercentntmt(), pChild.getPercentmd(), pChild.getPercentmsm(), pChild.getPercentvcbtbc(), pChild.getPercentpnmt(), pChild.getPercentlao(), pChild.getPercenthoalieu(), pChild.getPercentphamnhan(), pChild.getPercentmecon(), pChild.getPercentnghiavu(), pChild.getPercentkhac(), pChild.getPercentkhongthongtin(), pChild.getSumPercent(form.getTotal()));
                                //merge
                                setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 0, true);
                                setMerge(row.getRowNum() - 1, row.getRowNum(), 1, 1, true);

                            }
                        }
                    } //end for huyện
                }//end for Tỉnh

                if (!"province".equals(form.getLevelDisplay()) && form.getTable().size() > 1 && !"Tỉnh khác".equals(item.getDisplayName())) {
                    if (true) {
                        //Add row giá trị
                        createTableRow("", "Tổng tỉnh", item.getNtmt(), item.getMd(), item.getMsm(), item.getVcbtbc(), item.getPnmt(), item.getLao(), item.getHoalieu(), item.getPhamnhan(), item.getMecon(), item.getNghiavu(), item.getKhac(), item.getKhongthongtin(), item.getSum());
                        //Add row %
                        row = createTableRow(null, null, item.getPercentntmt(), item.getPercentmd(), item.getPercentmsm(), item.getPercentvcbtbc(), item.getPercentpnmt(), item.getPercentlao(), item.getPercenthoalieu(), item.getPercentphamnhan(), item.getPercentmecon(), item.getPercentnghiavu(), item.getPercentkhac(), item.getPercentkhongthongtin(), item.getSumPercent(form.getTotal()));
                        setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 0, true);
                        setMerge(row.getRowNum() - 1, row.getRowNum(), 1, 1, true);
                    }
                }
            }

            //Add row giá trị
            createTableRow("Tổng cộng", null, form.getTotalntmt(), form.getTotalmd(), form.getTotalmsm(), form.getTotalvcbtbc(), form.getTotalpnmt(), form.getTotallao(), form.getTotalhoalieu(), form.getTotalphamnhan(), form.getTotalmecon(), form.getTotalnghiavu(), form.getTotalkhac(), form.getTotalkhongthongtin(), form.getTotal());
            //Add row %
            row = createTableRow(null, null, form.getSumTotal(form.getTotalntmt(), form.getTotal()),
                    form.getSumTotal(form.getTotalmd(), form.getTotal()),
                    form.getSumTotal(form.getTotalmsm(), form.getTotal()),
                    form.getSumTotal(form.getTotalvcbtbc(), form.getTotal()),
                    form.getSumTotal(form.getTotalpnmt(), form.getTotal()),
                    form.getSumTotal(form.getTotallao(), form.getTotal()),
                    form.getSumTotal(form.getTotalhoalieu(), form.getTotal()),
                    form.getSumTotal(form.getTotalphamnhan(), form.getTotal()),
                    form.getSumTotal(form.getTotalmecon(), form.getTotal()),
                    form.getSumTotal(form.getTotalnghiavu(), form.getTotal()),
                    form.getSumTotal(form.getTotalkhac(), form.getTotal()),
                    form.getSumTotal(form.getTotalkhongthongtin(), form.getTotal()),
                    form.getSumTotal(form.getTotal(), form.getTotal()));
            setMerge(row.getRowNum() - 1, row.getRowNum(), 0, 1, true);
        }

    }
}
