package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacOutProvinceTransmissionForm;
import com.gms.entity.form.pac.PacOutProvinceTransmissionTable;
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
public class OutProvinceTransmissionExcel extends BaseView implements IExportExcel {

    private PacOutProvinceTransmissionForm form;
    private String extension;

    public OutProvinceTransmissionExcel(PacOutProvinceTransmissionForm form, String extension) {
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
        headers.add("Lây qua đường máu");
        headers.add("Lây qua đường tình dục");
        headers.add("Mẹ truyền sang con");
        headers.add("Không rõ");
        headers.add("Không có thông tin");
        headers.add("Tổng số");
        createTableHeaderRow(headers.toArray(new String[headers.size()]));

        int i = 1;
        // loop to create row content
        for (Map.Entry<String, HashMap<String, PacOutProvinceTransmissionTable>> entry : form.getItems().entrySet()) {
            String key = entry.getKey();
            HashMap<String, PacOutProvinceTransmissionTable> value = entry.getValue();

            int index = getLastRowNumber();
            createTableRow(i++, form.getOptions().get("provinces").get(key),
                    "Ca ngoại tỉnh theo hộ khẩu",
                    value.get("permanent").getBlood(),
                    value.get("permanent").getSexuality(),
                    value.get("permanent").getMomtochild(),
                    value.get("permanent").getUnclear(),
                    value.get("permanent").getNoinformation(),
                    value.get("permanent").getSum()
            );

            createTableRow("", "", "Ca mới, chưa chuyển quản lý",
                    value.get("notmanage").getBlood(),
                    value.get("notmanage").getSexuality(),
                    value.get("notmanage").getMomtochild(),
                    value.get("notmanage").getUnclear(),
                    value.get("notmanage").getNoinformation(),
                    value.get("notmanage").getSum()
            );
            createTableRow("", "", "Ca đã chuyển quản lý",
                    value.get("hasmanage").getBlood(),
                    value.get("hasmanage").getSexuality(),
                    value.get("hasmanage").getMomtochild(),
                    value.get("hasmanage").getUnclear(),
                    value.get("hasmanage").getNoinformation(),
                    value.get("hasmanage").getSum()
            );

            setMerge(index, index + 2, 0, 0, true);
            setMerge(index, index + 2, 1, 1, true);

        }
        int indexTotal = getLastRowNumber();
        createTableRowBold("Tổng cộng", "", "Ca ngoại tỉnh theo hộ khẩu",
                form.getTotal(form.getItems(), "permanent").getBlood(),
                form.getTotal(form.getItems(), "permanent").getSexuality(),
                form.getTotal(form.getItems(), "permanent").getMomtochild(),
                form.getTotal(form.getItems(), "permanent").getUnclear(),
                form.getTotal(form.getItems(), "permanent").getNoinformation(),
                form.getTotal(form.getItems(), "permanent").getSum()
        );
        createTableRowBold("", "", "Ca mới, chưa chuyển quản lý",
                form.getTotal(form.getItems(), "notmanage").getBlood(),
                form.getTotal(form.getItems(), "notmanage").getSexuality(),
                form.getTotal(form.getItems(), "notmanage").getMomtochild(),
                form.getTotal(form.getItems(), "notmanage").getUnclear(),
                form.getTotal(form.getItems(), "notmanage").getNoinformation(),
                form.getTotal(form.getItems(), "notmanage").getSum()
        );
        createTableRowBold("", "", "Ca đã chuyển quản lý",
                form.getTotal(form.getItems(), "hasmanage").getBlood(),
                form.getTotal(form.getItems(), "hasmanage").getSexuality(),
                form.getTotal(form.getItems(), "hasmanage").getMomtochild(),
                form.getTotal(form.getItems(), "hasmanage").getUnclear(),
                form.getTotal(form.getItems(), "hasmanage").getNoinformation(),
                form.getTotal(form.getItems(), "hasmanage").getSum()
        );
        setMerge(indexTotal, indexTotal + 2, 0, 1, true);
    }

}
