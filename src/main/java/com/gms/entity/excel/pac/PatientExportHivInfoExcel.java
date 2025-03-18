package com.gms.entity.excel.pac;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.pac.PacPatientForm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 *
 * @author pdThang
 */
public class PatientExportHivInfoExcel extends BaseView implements IExportExcel {

    private PacPatientForm form;
    private String extension;

    public PatientExportHivInfoExcel(PacPatientForm form, String extension) {
        setUseStyle(false);
        this.form = form;
        this.extension = extension;
        this.sheetName = "DuLieu";
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
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
        sheet.setColumnWidth(i++, PixelUtils.pixel2WidthUnits(230));
    }

    /**
     * Tạo bảng trong excel
     */
    private void createTable() {

        //Dòng đầu tiên để trắng
        Cell cell;

        Font font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBold(true);

        CellStyle th = workbook.createCellStyle();
        th.setWrapText(true);
        th.setAlignment(HorizontalAlignment.CENTER);
        th.setVerticalAlignment(VerticalAlignment.CENTER);
        th.setFont(font);

        int indexT = 0;
        //Tiêu đề

        Row row = createRow();
        cell = createCell(row, indexT++);
        cell.setCellValue("ThemMoi");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("STT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TinhTrangSinhTu");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDTinhTrangCuTru");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TinhTrangCuTru");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDTrangThaiDieuTri");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TrangThaiDieuTri");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("MaHIVInfo");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("xxnguon");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("xxĐơn vị");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("Them");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("XoaBo");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("MaThe");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("HoTen");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NamSinh");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDGioiTinh");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenGioiTinh");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDDanToc");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenDanToc");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDHuyenHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenHuyenHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDXaHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenXaHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("SonhaHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("DuongPhoHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("ToHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDHuyenTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenHuyenTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDXaTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenXaTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("SonhaTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("DuongPhoTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("ToTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("SoCMND");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayXetNghiemHIV");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayCDAIDS");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayTuVong");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NoiLayMau");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNoiLayMau");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NoiXetNghiemHIV");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNoiXNHIV");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NoiChanDoanAIDS");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNoiChanDoanAIDS");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("DoiTuong");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDDoiTuong");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("DuongLay");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDDuongLay");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgheNghiep");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNgheNghiep");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NguyCo");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNguyCo");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDTinHTrang");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayNhapHIV");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayNhapTuVong");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgaySua");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayXoa");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("GhiChu");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDTinhHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenTinhHK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDTinhTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenTinhTT");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("SoTheBH");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayBatDauBH");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayHetHan");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("TenCoSoDieuTri");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDCoSoDieuTri");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayDK");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("LoaiDangKy");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayARVDauTien");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayARVOPC");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("PhacDoDauTien");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("PhacDoHienTai");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NgayKetThuc");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("LyDoKetThuc");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("NoiLayMau");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("IDNoiLayMau");
        cell.setCellStyle(th);

        cell = createCell(row, indexT++);
        cell.setCellValue("MaBA");
        cell.setCellStyle(th);

        // loop to create row content
        int stt = 1;
        if (form.getItems().size() > 0) {

            for (PacPatientInfoEntity item : form.getItems()) {
                int index = 0;
                // Số thứ tự
                row = createRow();

                cell = createCell(row, index++);
                cell.setCellValue(form.getHivCodeOptions().get(String.valueOf(item.getID())) == null ? "1" : "0");

                cell = createCell(row, index++);
                cell.setCellValue(stt++);

                cell = createCell(row, index++);
                cell.setCellValue(item.getDeathTime() == null ? "0" : "1");

                cell = createCell(row, index++);
                cell.setCellValue(item.getStatusOfResidentID() == null || "".equals(item.getStatusOfResidentID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID())) ? "" : form.getsOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getStatusOfResidentID() == null || "".equals(item.getStatusOfResidentID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID())) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(item.getStatusOfResidentID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getStatusOfTreatmentID() == null || "".equals(item.getStatusOfTreatmentID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID())) ? "" : form.getsOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getStatusOfTreatmentID() == null || "".equals(item.getStatusOfTreatmentID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID())) ? "" : form.getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(item.getStatusOfTreatmentID()));

                cell = createCell(row, index++);
                cell.setCellValue(form.getHivCodeOptions().get(item.getID() + ""));

                cell = createCell(row, index++);
                cell.setCellValue(item.getSourceServiceID() == null || "".equals(item.getSourceServiceID()) ? "" : form.getOptions().get(ParameterEntity.SERVICE).get(item.getSourceServiceID()));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("1");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(item.getID());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getFullname()) ? "" : item.getFullname());

                cell = createCell(row, index++);
                cell.setCellValue((item.getYearOfBirth() == null || item.getYearOfBirth() == 0) ? "" : String.valueOf(item.getYearOfBirth()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getsOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getGenderID()) ? "" : form.getOptions().get(ParameterEntity.GENDER).get(item.getGenderID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getRaceID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.RACE).get(item.getRaceID())) ? "" : form.getsOptions().get(ParameterEntity.RACE).get(item.getRaceID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getRaceID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.RACE).get(item.getRaceID())) ? "" : form.getOptions().get(ParameterEntity.RACE).get(item.getRaceID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentDistrictID()) ? "" : form.getsOptions().get("district").get(item.getPermanentDistrictID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentDistrictID()) ? "" : form.getDistricts().get(item.getPermanentDistrictID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentWardID()) ? "" : form.getWardHivInfoCodes().get(item.getPermanentWardID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentWardID()) ? "" : form.getWards().get(item.getPermanentWardID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentAddressNo()) ? "" : item.getPermanentAddressNo());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentAddressStreet()) ? "" : item.getPermanentAddressStreet());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentAddressGroup()) ? "" : item.getPermanentAddressGroup());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentDistrictID()) ? "" : form.getsOptions().get("district").get(item.getCurrentDistrictID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentDistrictID()) ? "" : form.getDistricts().get(item.getCurrentDistrictID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentWardID()) ? "" : form.getWardHivInfoCodes().get(item.getCurrentWardID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentWardID()) ? "" : form.getWards().get(item.getCurrentWardID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentAddressNo()) ? "" : item.getCurrentAddressNo());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentAddressStreet()) ? "" : item.getCurrentAddressStreet());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentAddressGroup()) ? "" : item.getCurrentAddressGroup());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getIdentityCard()) ? "" : item.getIdentityCard());

                cell = createCell(row, index++);
                cell.setCellValue(item.getConfirmTime() == null ? "" : TextUtils.formatDate(item.getConfirmTime(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue(item.getAidsStatusDate()== null ? "" : TextUtils.formatDate(item.getAidsStatusDate(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue(item.getDeathTime() == null ? "" : TextUtils.formatDate(item.getDeathTime(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getBloodBaseID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID())) ? "" : form.getOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getBloodBaseID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID())) ? "" : form.getsOptions().get(ParameterEntity.BLOOD_BASE).get(item.getBloodBaseID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteConfirmID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID())) ? "" : form.getOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteConfirmID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID())) ? "" : form.getsOptions().get(ParameterEntity.PLACE_TEST).get(item.getSiteConfirmID()));

                cell = createCell(row, index++);
                cell.setCellValue(!"2".equals(item.getAidsStatus()) ? "" : (StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())));

                cell = createCell(row, index++);
                cell.setCellValue(!"2".equals(item.getAidsStatus()) ? "" : (StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())) ? "" : form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())));

                
                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) ? "" : form.getsOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getObjectGroupID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID())) ? "" : form.getsOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(item.getObjectGroupID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getModeOfTransmissionID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(item.getModeOfTransmissionID())) ? "" : form.getOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(item.getModeOfTransmissionID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getModeOfTransmissionID())  || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(item.getModeOfTransmissionID())) ? "" : form.getsOptions().get(ParameterEntity.MODE_OF_TRANSMISSION).get(item.getModeOfTransmissionID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getJobID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.JOB).get(item.getJobID())) ? "" : form.getOptions().get(ParameterEntity.JOB).get(item.getJobID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getJobID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.JOB).get(item.getJobID())) ? "" : form.getsOptions().get(ParameterEntity.JOB).get(item.getJobID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getRiskBehaviorID() == null || item.getRiskBehaviorID().isEmpty() || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.RISK_BEHAVIOR).get(item.getRiskBehaviorID().get(0))) ? "" : form.getOptions().get(ParameterEntity.RISK_BEHAVIOR).get(item.getRiskBehaviorID().get(0)));

                cell = createCell(row, index++);
                cell.setCellValue(item.getRiskBehaviorID() == null || item.getRiskBehaviorID().isEmpty() || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.RISK_BEHAVIOR).get(item.getRiskBehaviorID().get(0))) ? "" : form.getsOptions().get(ParameterEntity.RISK_BEHAVIOR).get(item.getRiskBehaviorID().get(0)));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(item.getCreateAT()== null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(item.getUpdateAt() == null ? "" : TextUtils.formatDate(item.getUpdateAt(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getNote())  ? "" : item.getNote());

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentProvinceID()) ? "" : form.getsOptions().get("province").get(item.getPermanentProvinceID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getPermanentProvinceID()) ? "" : form.getProvinces().get(item.getPermanentProvinceID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentProvinceID()) ? "" : form.getsOptions().get("province").get(item.getCurrentProvinceID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getCurrentProvinceID()) ? "" : form.getProvinces().get(item.getCurrentProvinceID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getHealthInsuranceNo() == null || item.getHealthInsuranceNo().isEmpty() ? "" : item.getHealthInsuranceNo());

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getSiteTreatmentFacilityID()) || StringUtils.isEmpty(form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID())) ? "" : form.getsOptions().get(ParameterEntity.TREATMENT_FACILITY).get(item.getSiteTreatmentFacilityID()));

                cell = createCell(row, index++);
                cell.setCellValue(item.getCreateAT()== null ? "" : TextUtils.formatDate(item.getCreateAT(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(item.getStartTreatmentTime()== null ? "" : TextUtils.formatDate(item.getStartTreatmentTime(), "dd/MM/yyyy"));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue(StringUtils.isEmpty(item.getTreatmentRegimenID()) ? "" : form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(item.getTreatmentRegimenID()));

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");

                cell = createCell(row, index++);
                cell.setCellValue("");
            }
        }
    }

}
