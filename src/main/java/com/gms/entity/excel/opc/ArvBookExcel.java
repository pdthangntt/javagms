package com.gms.entity.excel.opc;

import com.gms.components.ExcelUtils;
import com.gms.components.PixelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.BaseView;
import com.gms.entity.excel.IExportExcel;
import com.gms.entity.form.opc_arv.ArvBookForm1;
import com.gms.entity.form.opc_arv.SoKhangTheArvForm;
import com.gms.entity.form.opc_arv.SoKhangTheArvTableForm;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Admin
 */
public class ArvBookExcel extends BaseView implements IExportExcel {

    private final ArvBookForm1 form;
    private final String extension;

    public ArvBookExcel(ArvBookForm1 form, String extension) {
        this.useStyle = true;
        this.form = form;
        this.extension = extension;
        this.sheetName = "So Arv";
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
        //Điều chỉnh chiều rộng cho các cột
        sheet.setColumnWidth(0, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(1, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(2, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(3, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(4, PixelUtils.pixel2WidthUnits(50));
        sheet.setColumnWidth(5, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(6, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(7, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(8, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(9, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(10, PixelUtils.pixel2WidthUnits(100));
        sheet.setColumnWidth(11, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(12, PixelUtils.pixel2WidthUnits(150));
        sheet.setColumnWidth(13, PixelUtils.pixel2WidthUnits(200));

        sheet.setColumnWidth(14, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(15, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(16, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(17, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(18, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(19, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(20, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(21, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(22, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(23, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(24, PixelUtils.pixel2WidthUnits(200));

        sheet.setColumnWidth(25, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(26, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(27, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(28, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(29, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(30, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(31, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(32, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(33, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(34, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(35, PixelUtils.pixel2WidthUnits(200));

        sheet.setColumnWidth(36, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(37, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(38, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(39, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(40, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(41, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(42, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(43, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(44, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(45, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(46, PixelUtils.pixel2WidthUnits(200));

        sheet.setColumnWidth(47, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(48, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(49, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(50, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(51, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(52, PixelUtils.pixel2WidthUnits(200));
        sheet.setColumnWidth(53, PixelUtils.pixel2WidthUnits(200));

        setMerge(1, 1, 0, 24, false);

        createRow();
        createTitleRow(form.getTitle());
        createRow();

        int cellIndexFilter = 1;
        if (StringUtils.isNotEmpty(form.getFrom()) || StringUtils.isNotEmpty(form.getTo())) {
            createFilterRow("Ngày điều trị ARV", String.format("%s  %s", (StringUtils.isNotEmpty(form.getFrom()) ? ("từ " + form.getFrom()) : ""), StringUtils.isNotEmpty(form.getTo()) ? ("đến " + form.getTo()) : ""), cellIndexFilter);
        }
        createFilterRow("Tên đơn vị", form.getSiteName(), cellIndexFilter);
        createFilterRow("Ngày xuất sổ", TextUtils.formatDate(new Date(), "dd/MM/yyyy"), cellIndexFilter);
    }

    private void createTable() throws Exception {
        getCurrentSheet();
        createRow();
        int index = getLastRowNumber();
        StringBuffer strB = new StringBuffer();
        createTableHeaderRow("Thông tin cơ bản", "", "", "", "", "Khi bắt đầu điều trị", "", "", "", "", "Thay đổi phác đồ điều trị", "", "", "Theo dõi người bệnh trong quá trình điều trị bằng thuốc kháng HIV", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        createTableHeaderRow("Số TT", "Mã số bệnh án", "Họ và tên", "Năm sinh", "", "Ngày bắt đầu", "Phác đồ ban đầu", "Giai đoạn LS", "CD4", "Cân nặng /Chiều cao", "Ngày thay đổi phác đồ", "Lý do thay đổi", "Phác đồ thay thế", "Tháng ĐT đầu tiên (0)", "1", "2", "3", "4", "5", "Tháng thứ 6", "", "7", "8", "9", "10", "11", "Tháng thứ 12", "", "13", "14", "15", "16", "17", "Tháng thứ 18", "", "19", "20", "21", "22", "23", "Tháng thứ 24", "", "Tháng thứ 30", "", "Tháng thứ 36", "", "Tháng thứ 42", "", "Tháng thứ 48", "", "Tháng thứ 54", "", "Tháng thứ 60", "");
        createTableHeaderRow("", "", "", "Nam", "Nữ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "CD4", "", "", "", "", "", "", "CD4", "", "", "", "", "", "", "CD4", "", "", "", "", "", "", "CD4", "", "CD4", "", "CD4", "", "CD4", "", "CD4", "", "CD4", "", "CD4");
        createTableHeaderRow("(1)", "(2)", "(3)", "(4)", "", "(5)", "(6)", "(7)", "(8)", "(9)", "(10)", "(11)", "(12)", "(13)", "(14)", "(15)", "(16)", "(17)", "(18)", "(19)", "(20)", "(21)", "(22)", "(23)", "(24)", "(25)", "(26)", "(27)", "(28)", "(29)", "(30)", "(31)", "(32)", "(33)", "(34)", "(35)", "(36)", "(37)", "(38)", "(39)", "(40)", "(41)", "(42)", "(43)", "(44)", "(45)", "(46)", "(47)", "(48)", "(49)", "(50)", "(51)", "(52)", "(53)");

        setMerge(index, index, 0, 4, false);
        setMerge(index, index, 5, 9, false);
        setMerge(index, index, 10, 12, false);
        setMerge(index, index, 13, 53, false);

        setMerge(index + 1, index + 2, 0, 0, false);
        setMerge(index + 1, index + 2, 1, 1, false);
        setMerge(index + 1, index + 2, 2, 2, false);
        setMerge(index + 1, index + 1, 3, 4, false);
        setMerge(index + 1, index + 2, 5, 5, false);
        setMerge(index + 1, index + 2, 6, 6, false);
        setMerge(index + 1, index + 2, 7, 7, false);
        setMerge(index + 1, index + 2, 8, 8, false);
        setMerge(index + 1, index + 2, 9, 9, false);
        setMerge(index + 1, index + 2, 10, 10, false);
        setMerge(index + 1, index + 2, 11, 11, false);
        setMerge(index + 1, index + 2, 12, 12, false);
        setMerge(index + 1, index + 2, 13, 13, false);
        setMerge(index + 1, index + 2, 14, 14, false);
        setMerge(index + 1, index + 2, 15, 15, false);
        setMerge(index + 1, index + 2, 16, 16, false);
        setMerge(index + 1, index + 2, 17, 17, false);
        setMerge(index + 1, index + 2, 18, 18, false);
        setMerge(index + 1, index + 1, 19, 20, false);
        setMerge(index + 1, index + 2, 21, 21, false);
        setMerge(index + 1, index + 2, 22, 22, false);
        setMerge(index + 1, index + 2, 23, 23, false);
        setMerge(index + 1, index + 2, 24, 24, false);
        setMerge(index + 1, index + 2, 25, 25, false);
        setMerge(index + 1, index + 1, 26, 27, false);
        setMerge(index + 1, index + 2, 28, 28, false);
        setMerge(index + 1, index + 2, 29, 29, false);
        setMerge(index + 1, index + 2, 30, 30, false);
        setMerge(index + 1, index + 2, 31, 31, false);
        setMerge(index + 1, index + 2, 32, 32, false);
        setMerge(index + 1, index + 1, 33, 34, false);
        setMerge(index + 1, index + 2, 35, 35, false);
        setMerge(index + 1, index + 2, 36, 36, false);
        setMerge(index + 1, index + 2, 37, 37, false);
        setMerge(index + 1, index + 2, 38, 38, false);
        setMerge(index + 1, index + 2, 39, 39, false);
        setMerge(index + 1, index + 1, 40, 41, false);
        setMerge(index + 1, index + 1, 42, 43, false);
        setMerge(index + 1, index + 1, 44, 45, false);
        setMerge(index + 1, index + 1, 46, 47, false);
        setMerge(index + 1, index + 1, 48, 49, false);
        setMerge(index + 1, index + 1, 50, 51, false);
        setMerge(index + 1, index + 1, 52, 53, false);
        setMerge(index + 3, index + 3, 3, 4, false);
        int stt = 1;

        List<String> lineTop = new ArrayList<>();
        List<String> lineBottom = new ArrayList<>();

        if (form.getDataPage() != null && form.getDataPage().getData() != null && !form.getDataPage().getData().isEmpty()) {
            for (SoKhangTheArvForm so : form.getDataPage().getData()) {
                // Dòng đầu
                for (int i = 1; i < 54; i++) {
                    switch (i) {
                        case 1:
                            lineTop.add(String.valueOf(stt++));
                            lineBottom.add("");
                            break;
                        case 2:
                            lineTop.add(so.getGeneral().getCode());
                            lineBottom.add("");
                            break;
                        case 3:
                            lineTop.add(so.getGeneral().getFullname());
                            lineBottom.add("");
                            break;
                        case 4:
                            lineTop.add(so.getGeneral().getGenderID().equals(GenderEnum.MALE.getKey()) ? so.getGeneral().getDob() : "");
                            lineBottom.add("");
                            break;
                        case 5:
                            lineTop.add(so.getGeneral().getGenderID().equals(GenderEnum.FEMALE.getKey()) ? so.getGeneral().getDob() : "");
                            lineBottom.add("");
                            break;
                        case 6:
                            lineTop.add(so.getGeneral().getTreatmentTime());
                            lineBottom.add("");
                            break;
                        case 7:
                            lineTop.add(StringUtils.isNotEmpty(so.getGeneral().getTreatmentRegimentID()) ? form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(so.getGeneral().getTreatmentRegimentID()) : "");
                            lineBottom.add("");
                            break;
                        case 8:
                            lineTop.add(StringUtils.isNotEmpty(so.getGeneral().getClinicalStage()) ? form.getOptions().get(ParameterEntity.CLINICAL_STAGE).get(so.getGeneral().getClinicalStage()) : "");
                            lineBottom.add("");
                            break;
                        case 9:
                            lineTop.add(StringUtils.isNotEmpty(so.getGeneral().getCd4()) ? so.getGeneral().getCd4() : "");
                            lineBottom.add("");
                            break;
                        case 10:
                            lineTop.add(so.getGeneral() != null && StringUtils.isNotEmpty(so.getGeneral().getWeight()) && !"0.0".equals(so.getGeneral().getWeight()) ? so.getGeneral().getWeight() : "");
                            lineBottom.add(so.getGeneral() != null && StringUtils.isNotEmpty(so.getGeneral().getHeight()) && !"0.0".equals(so.getGeneral().getHeight()) ? so.getGeneral().getHeight() : "");
                            break;
                        case 11:
                            lineTop.add(StringUtils.isNotEmpty(so.getChangeRegimenIdDate()) ? so.getChangeRegimenIdDate() : "");
                            lineBottom.add("");
                            break;
                        case 12:
                            lineTop.add(StringUtils.isNotEmpty(so.getGeneral().getCausesChange()) ? so.getGeneral().getCausesChange() : "");
                            lineBottom.add("");
                            break;
                        case 13:
                            lineTop.add(StringUtils.isNotEmpty(so.getGeneral().getTreatmentRegimentIDChange()) ? form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(so.getGeneral().getTreatmentRegimentIDChange()) : "");
                            lineBottom.add("");break;
                        case 14:
                            lineTop.add(getMonthData(strB, so.getData().get("0"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("0"), "0"));
                            break;
                        case 15:
                            lineTop.add(getMonthData(strB, so.getData().get("1"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("1"), "1"));
                            break;
                        case 16:
                            lineTop.add(getMonthData(strB, so.getData().get("2"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("2"), "2"));
                            break;
                        case 17:
                            lineTop.add(getMonthData(strB, so.getData().get("3"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("3"), "3"));
                            break;
                        case 18:
                            lineTop.add(getMonthData(strB, so.getData().get("4"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("4"), "4"));
                            break;
                        case 19:
                            lineTop.add(getMonthData(strB, so.getData().get("5"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("5"), "5"));
                            break;
                        case 20:
                            lineTop.add(getMonthData(strB, so.getData().get("6"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("6"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("6"), "6"));
                            lineBottom.add("");
                            break;
                        case 22:
                            lineTop.add(getMonthData(strB, so.getData().get("7"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("7"), "7"));
                            break;
                        case 23:
                            lineTop.add(getMonthData(strB, so.getData().get("8"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("8"), "8"));
                            break;
                        case 24:
                            lineTop.add(getMonthData(strB, so.getData().get("9"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("9"), "9"));
                            break;
                        case 25:
                            lineTop.add(getMonthData(strB, so.getData().get("10"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("10"), "10"));
                            break;
                        case 26:
                            lineTop.add(getMonthData(strB, so.getData().get("11"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("11"), "11"));
                            break;
                        case 27:
                            lineTop.add(getMonthData(strB, so.getData().get("12"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("12"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("12"), "12"));
                            lineBottom.add("");
                            break;
                        case 29:
                            lineTop.add(getMonthData(strB, so.getData().get("13"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("13"), "13"));
                            break;
                        case 30:
                            lineTop.add(getMonthData(strB, so.getData().get("14"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("14"), "14"));
                            break;
                        case 31:
                            lineTop.add(getMonthData(strB, so.getData().get("15"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("15"), "15"));
                            break;
                        case 32:
                            lineTop.add(getMonthData(strB, so.getData().get("16"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("16"), "16"));
                            break;
                        case 33:
                            lineTop.add(getMonthData(strB, so.getData().get("17"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("17"), "17"));
                            break;
                        case 34:
                            lineTop.add(getMonthData(strB, so.getData().get("18"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("18"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("18"), "18"));
                            lineBottom.add("");
                            break;
                        case 36:
                            lineTop.add(getMonthData(strB, so.getData().get("19"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("19"), "19"));
                            break;
                        case 37:
                            lineTop.add(getMonthData(strB, so.getData().get("20"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("20"), "20"));
                            break;
                        case 38:
                            lineTop.add(getMonthData(strB, so.getData().get("21"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("21"), "21"));
                            break;
                        case 39:
                            lineTop.add(getMonthData(strB, so.getData().get("22"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("22"), "22"));
                            break;
                        case 40:
                            lineTop.add(getMonthData(strB, so.getData().get("23"), false));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("23"), "23"));
                            break;
                        case 41:
                            lineTop.add(getMonthData(strB, so.getData().get("24"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("24"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("24"), "24"));
                            lineBottom.add("");
                            break;
                        case 43:
                            lineTop.add(getMonthData(strB, so.getData().get("30"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("30"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("30"), "30"));
                            lineBottom.add("");
                            break;
                        case 45:
                            lineTop.add(getMonthData(strB, so.getData().get("36"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("36"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("36"), "36"));
                            lineBottom.add("");
                            break;
                        case 47:
                            lineTop.add(getMonthData(strB, so.getData().get("42"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("42"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("42"), "42"));
                            lineBottom.add("");
                            break;
                        case 49:
                            lineTop.add(getMonthData(strB, so.getData().get("48"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("48"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("48"), "48"));
                            lineBottom.add("");
                            break;
                        case 51:
                            lineTop.add(getMonthData(strB, so.getData().get("54"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("54"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("54"), "54"));
                            lineBottom.add("");
                            break;
                        case 53:
                            lineTop.add(getMonthData(strB, so.getData().get("60"), false));
                            lineTop.add(getMonthData(strB, so.getData().get("60"), true));
                            lineBottom.add(getMonthDataLine2(strB, so.getData().get("60"), "60"));
                            lineBottom.add("");
                            break;
                        default:
                            break;
                    }
                }
                // Tạo dòng dữ liệu
                createTableRow(lineTop.get(0),
                        lineTop.get(1),
                        lineTop.get(2),
                        lineTop.get(3),
                        lineTop.get(4),
                        lineTop.get(5),
                        lineTop.get(6),
                        lineTop.get(7),
                        lineTop.get(8),
                        lineTop.get(9),
                        lineTop.get(10),
                        lineTop.get(11),
                        lineTop.get(12),
                        lineTop.get(13),
                        lineTop.get(14),
                        lineTop.get(15),
                        lineTop.get(16),
                        lineTop.get(17),
                        lineTop.get(18),
                        lineTop.get(19),
                        lineTop.get(20),
                        lineTop.get(21),
                        lineTop.get(22),
                        lineTop.get(23),
                        lineTop.get(24),
                        lineTop.get(25),
                        lineTop.get(26),
                        lineTop.get(27),
                        lineTop.get(28),
                        lineTop.get(29),
                        lineTop.get(30),
                        lineTop.get(31),
                        lineTop.get(32),
                        lineTop.get(33),
                        lineTop.get(34),
                        lineTop.get(35),
                        lineTop.get(36),
                        lineTop.get(37),
                        lineTop.get(38),
                        lineTop.get(39),
                        lineTop.get(40),
                        lineTop.get(41),
                        lineTop.get(42),
                        lineTop.get(43),
                        lineTop.get(44),
                        lineTop.get(45),
                        lineTop.get(46),
                        lineTop.get(47),
                        lineTop.get(48),
                        lineTop.get(49),
                        lineTop.get(50),
                        lineTop.get(51),
                        lineTop.get(52),
                        lineTop.get(53)
                );
                createTableRow(lineBottom.get(0),
                        lineBottom.get(1),
                        lineBottom.get(2),
                        lineBottom.get(3),
                        lineBottom.get(4),
                        lineBottom.get(5),
                        lineBottom.get(6),
                        lineBottom.get(7),
                        lineBottom.get(8),
                        lineBottom.get(9),
                        lineBottom.get(10),
                        lineBottom.get(11),
                        lineBottom.get(12),
                        lineBottom.get(13),
                        lineBottom.get(14),
                        lineBottom.get(15),
                        lineBottom.get(16),
                        lineBottom.get(17),
                        lineBottom.get(18),
                        lineBottom.get(19),
                        lineBottom.get(20),
                        lineBottom.get(21),
                        lineBottom.get(22),
                        lineBottom.get(23),
                        lineBottom.get(24),
                        lineBottom.get(25),
                        lineBottom.get(26),
                        lineBottom.get(27),
                        lineBottom.get(28),
                        lineBottom.get(29),
                        lineBottom.get(30),
                        lineBottom.get(31),
                        lineBottom.get(32),
                        lineBottom.get(33),
                        lineBottom.get(34),
                        lineBottom.get(35),
                        lineBottom.get(36),
                        lineBottom.get(37),
                        lineBottom.get(38),
                        lineBottom.get(39),
                        lineBottom.get(40),
                        lineBottom.get(41),
                        lineBottom.get(42),
                        lineBottom.get(43),
                        lineBottom.get(44),
                        lineBottom.get(45),
                        lineBottom.get(46),
                        lineBottom.get(47),
                        lineBottom.get(48),
                        lineBottom.get(49),
                        lineBottom.get(50),
                        lineBottom.get(51),
                        lineBottom.get(52),
                        lineBottom.get(53)
                );
                
                lineTop = new ArrayList<>();
                lineBottom = new ArrayList<>();
                
                // set merge row
                int lastRowNumber = getLastRowNumber();
                
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 0, 0, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 1, 1, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 2, 2, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 3, 3, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 4, 4, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 5, 5, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 6, 6, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 7, 7, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 8, 8, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 10, 10, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 11, 11, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 12, 12, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 20, 20, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 27, 27, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 34, 34, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 41, 41, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 43, 43, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 45, 45, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 47, 47, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 49, 49, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 51, 51, true);
                setMerge(lastRowNumber - 2, lastRowNumber - 1, 53, 53, true);
            }
        }
    }

    /**
     * Lấy dữ liệu từng tháng
     *
     * @param rs
     * @param list
     * @param index
     * @return
     */
    private String getMonthData(StringBuffer rs, List<SoKhangTheArvTableForm> list, boolean isCd4) {
        rs = new StringBuffer();
        
        for (SoKhangTheArvTableForm it : list) {
            if (!isCd4) {
                if (StringUtils.isNotEmpty(it.getLine()) &&( it.getLine().equals("1") || it.getLine().equals("0") || it.getLine().equals("3"))) {
                    if (StringUtils.isNotEmpty(it.getTreatmentRegimentID()) && StringUtils.isNotEmpty(it.getTreatmentRegimentID()) && StringUtils.isEmpty(it.getEndCase())) {
                        rs.append(form.getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(it.getTreatmentRegimentID()) + "\n");
                    }

                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && (it.getEndCase().equals("4") || it.getEndCase().equals("1")) && StringUtils.isNotEmpty(it.getExaminationTime())) {
                        rs.append("B\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && it.getEndCase().equals("2")) {
                        rs.append("TV\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && it.getEndCase().equals("3")) {
                        rs.append("CĐ\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && it.getEndCase().equals("5")) {
                        rs.append("KT\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getDateOfArrival()) && it.getRegistrationType().equals("3") && StringUtils.isEmpty(it.getEndCase())) {
                        rs.append("CT\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getRegistrationType()) && it.getRegistrationType().equals("2") && StringUtils.isEmpty(it.getEndCase())) {
                        rs.append("ĐTL\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && (it.getEndCase().equals("4") || it.getEndCase().equals("1")) && StringUtils.isNotEmpty(it.getExaminationTime())) {
                        rs.append(it.getExaminationTime() + "\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && (it.getEndCase().equals("2") || it.getEndCase().equals("5"))) {
                        rs.append(it.getEndTime() + "\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getEndCase()) && it.getEndCase().equals("3")) {
                        rs.append(it.getTranferFromTime() + "\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getRegistrationType()) && it.getRegistrationType().equals("3") && StringUtils.isEmpty(it.getEndCase())) {
                        rs.append(it.getDateOfArrival() + "\n");
                    }
                    if (!it.getLine().equals("3") && StringUtils.isNotEmpty(it.getRegistrationType()) && it.getRegistrationType().equals("2") && StringUtils.isEmpty(it.getEndCase())) {
                        rs.append(it.getTreatmentTime() + "\n");
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(it.getLine()) && (it.getLine().equals("2") || it.getLine().equals("0"))) {
                    if (StringUtils.isNotEmpty(it.getCd4TestTime())) {
                        rs.append(it.getCd4TestTime() + "\n");
                        rs.append(StringUtils.isNotEmpty(it.getCd4Result()) ? it.getCd4Result() : "");
                    }
                }
            }
        }
        return String.valueOf(rs);
    }

    /**
     * Lấy dữ liệu dòng thông tin thứ 2
     *
     *
     * @param rs
     * @param list
     * @param isCd4
     * @return
     */
    private String getMonthDataLine2(StringBuffer rs, List<SoKhangTheArvTableForm> list, String index) {

        rs = new StringBuffer();
        for (SoKhangTheArvTableForm it : list) {
            if ( StringUtils.isNotEmpty(it.getLine()) && ( it.getLine().equals("2") || it.getLine().equals("0"))) {
                if (StringUtils.isNotEmpty(it.getLaoStartTime()) && StringUtils.isNotEmpty(it.getKeyLao()) && it.getKeyLao().equals(index)) {
                    rs.append("ĐTL\n");
                }
                if (StringUtils.isNotEmpty(it.getLaoStartTime()) && StringUtils.isNotEmpty(it.getKeyLao()) && it.getKeyLao().equals(index)) {
                    rs.append(it.getLaoStartTime() + "\n");
                }
                if (StringUtils.isNotEmpty(it.getInhStartTime()) && StringUtils.isNotEmpty(it.getInhStartTime()) && it.getKeyInh().equals(index)) {
                    rs.append("INH\n");
                }
                if (StringUtils.isNotEmpty(it.getInhStartTime()) && StringUtils.isNotEmpty(it.getInhStartTime()) && it.getKeyInh().equals(index)) {
                    rs.append(it.getInhStartTime() + "\n");
                }
                if (StringUtils.isNotEmpty(it.getCotrimoxazoleStartTime()) && StringUtils.isNotEmpty(it.getCotrimoxazoleStartTime()) && it.getKeyCotri().equals(index)) {
                    rs.append("CTX\n");
                }
                if (StringUtils.isNotEmpty(it.getCotrimoxazoleStartTime()) && StringUtils.isNotEmpty(it.getCotrimoxazoleStartTime()) && it.getKeyCotri().equals(index)) {
                    rs.append(it.getCotrimoxazoleStartTime() + "\n");
                }
            }
        }
        return String.valueOf(rs);
    }

}
