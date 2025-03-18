package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacOutProvinceObjectForm;
import com.gms.entity.form.pac.PacOutProvinceObjectTable;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pdThang
 */
public class OutProvinceObjectExcel extends BaseView implements IExportExcel {

    private PacOutProvinceObjectForm form;
    private String extension;

    public OutProvinceObjectExcel(PacOutProvinceObjectForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "Bao cao ngoai tinh";
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
        HashMap<String, String> labels = (new PacPatientInfoEntity()).getAttributeLabels();

        int i = 0;
        Sheet sheet = getCurrentSheet();
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(40));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(80));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));

        //Dòng đầu tiên để trắng
        setMerge(1, 1, 0, 10, false);
        createRow();
        createTitleRow(form.getTitle().toUpperCase());

        createRow();

        int cellIndexFilter = 1;

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
            createFilterRow("Trạng thái người nhiễm", (StringUtils.isEmpty(form.getPatientStatus()) || form.getPatientStatus().contains(",") ? " còn sống, tử vong" : "alive".equals(form.getPatientStatus()) ? "Còn sống" : "Tử vong"), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getPlaceTest())) {
            createFilterRow("Nơi XN khẳng định", form.getOptions().get(ParameterEntity.PLACE_TEST).get(form.getPlaceTest()), cellIndexFilter);
        }
        if (StringUtils.isNotEmpty(form.getFacility())) {
            createFilterRow("Nơi điều trị", form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(form.getFacility()), cellIndexFilter);
        }

        createFilterRow("Tên đơn vị ", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất báo cáo", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        createRow();
        List<String> headers;

        headers = new ArrayList<>();
        headers.add("STT");
        headers.add("Đơn vị báo cáo");
        headers.add("Tiêu chí");
        headers.add("Nghiện chích ma túy");
        headers.add("Người hành nghề mại dâm");
        headers.add("Nam quan hệ tình dục với nam");
        headers.add("Vợ/chồng/bạn tình của người nhiễm HIV");
        headers.add("Phụ nữ mang thai");
        headers.add("Bệnh nhân Lao");
        headers.add("Đối tượng khác");
        headers.add("Không có thông tin");
        headers.add("Tổng số");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        int i = 1;
        // loop to create row content
        for (Map.Entry<String, HashMap<String, PacOutProvinceObjectTable>> entry : form.getItems().entrySet()) {
            String key = entry.getKey();
            HashMap<String, PacOutProvinceObjectTable> value = entry.getValue();

            int index = getLastRowNumber();
            createTableRow(i++, form.getOptions().get("provinces").get(key),
                    "Ca ngoại tỉnh theo hộ khẩu",
                    value.get("permanent").getNtmt(),
                    value.get("permanent").getMd(),
                    value.get("permanent").getMsm(),
                    value.get("permanent").getVcbtbc(),
                    value.get("permanent").getPnmt(),
                    value.get("permanent").getLao(),
                    value.get("permanent").getKhongthongtin(),
                    value.get("permanent").getKhac(),
                    value.get("permanent").getSum()
            );

            createTableRow("", "", "Ca mới, chưa chuyển quản lý",
                    value.get("notmanage").getNtmt(),
                    value.get("notmanage").getMd(),
                    value.get("notmanage").getMsm(),
                    value.get("notmanage").getVcbtbc(),
                    value.get("notmanage").getPnmt(),
                    value.get("notmanage").getLao(),
                    value.get("notmanage").getKhongthongtin(),
                    value.get("notmanage").getKhac(),
                    value.get("notmanage").getSum()
            );
            createTableRow("", "", "Ca đã chuyển quản lý",
                    value.get("hasmanage").getNtmt(),
                    value.get("hasmanage").getMd(),
                    value.get("hasmanage").getMsm(),
                    value.get("hasmanage").getVcbtbc(),
                    value.get("hasmanage").getPnmt(),
                    value.get("hasmanage").getLao(),
                    value.get("hasmanage").getKhongthongtin(),
                    value.get("hasmanage").getKhac(),
                    value.get("hasmanage").getSum()
            );

            setMerge(index, index + 2, 0, 0, true);
            setMerge(index, index + 2, 1, 1, true);

        }
        int indexTotal = getLastRowNumber();
        createTableRowBold("Tổng cộng", "","Ca ngoại tỉnh theo hộ khẩu",
                form.getTotal(form.getItems(), "permanent").getNtmt(),
                form.getTotal(form.getItems(), "permanent").getMd(),
                form.getTotal(form.getItems(), "permanent").getMsm(),
                form.getTotal(form.getItems(), "permanent").getVcbtbc(),
                form.getTotal(form.getItems(), "permanent").getPnmt(),
                form.getTotal(form.getItems(), "permanent").getLao(),
                form.getTotal(form.getItems(), "permanent").getKhongthongtin(),
                form.getTotal(form.getItems(), "permanent").getKhac(),
                form.getTotal(form.getItems(), "permanent").getSum()
        );
        createTableRowBold("", "","Ca mới, chưa chuyển quản lý",
                form.getTotal(form.getItems(), "notmanage").getNtmt(),
                form.getTotal(form.getItems(), "notmanage").getMd(),
                form.getTotal(form.getItems(), "notmanage").getMsm(),
                form.getTotal(form.getItems(), "notmanage").getVcbtbc(),
                form.getTotal(form.getItems(), "notmanage").getPnmt(),
                form.getTotal(form.getItems(), "notmanage").getLao(),
                form.getTotal(form.getItems(), "notmanage").getKhongthongtin(),
                form.getTotal(form.getItems(), "notmanage").getKhac(),
                form.getTotal(form.getItems(), "notmanage").getSum()
        );
        createTableRowBold("", "","Ca đã chuyển quản lý",
                form.getTotal(form.getItems(), "hasmanage").getNtmt(),
                form.getTotal(form.getItems(), "hasmanage").getMd(),
                form.getTotal(form.getItems(), "hasmanage").getMsm(),
                form.getTotal(form.getItems(), "hasmanage").getVcbtbc(),
                form.getTotal(form.getItems(), "hasmanage").getPnmt(),
                form.getTotal(form.getItems(), "hasmanage").getLao(),
                form.getTotal(form.getItems(), "hasmanage").getKhongthongtin(),
                form.getTotal(form.getItems(), "hasmanage").getKhac(),
                form.getTotal(form.getItems(), "hasmanage").getSum()
        );
        setMerge(indexTotal, indexTotal + 2, 0, 1, true);
    }

}
