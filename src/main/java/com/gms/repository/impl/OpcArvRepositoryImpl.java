package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.form.opc_arv.ArvBookChildForm;
import com.gms.entity.form.opc_arv.ArvBookTableForm;
import com.gms.entity.form.opc_arv.InsuranceTable;
import com.gms.entity.form.opc_arv.OpcMerSixMonthTable;
import com.gms.entity.form.opc_arv.OpcPreArvChildForm;
import com.gms.entity.form.opc_arv.OpcPreArvTableForm;
import com.gms.entity.form.opc_arv.QuickTreatmentTable;
import com.gms.entity.form.opc_arv.RegimenTable;
import com.gms.entity.form.opc_arv.SoKhangTheArvForm;
import com.gms.entity.form.opc_arv.SoKhangTheArvTableForm;
import com.gms.entity.form.opc_arv.TT03Table02;
import com.gms.entity.form.opc_arv.TT03Table03;
import com.gms.entity.form.opc_arv.TT03Table04;
import com.gms.entity.form.opc_arv.TT03Table05;
import com.gms.entity.form.opc_arv.TT03Table06;
import com.gms.entity.form.opc_arv.ViralloadManagerTable;
import com.gms.entity.form.opc_arv.ViralloadManagerTable02;
import com.gms.entity.form.opc_arv.ViralloadTable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class OpcArvRepositoryImpl extends BaseRepositoryImpl {

    private static final String ddMMyyyy = "dd/MM/yyyy";
    
    /**
     * TT03 - Bảng 02
     *
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, TT03Table02> getTT03Table02(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, TT03Table02> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/tt03_table02.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            TT03Table02 item = new TT03Table02();
            item.setLao(Integer.valueOf(object[1].toString()));
            item.setArv(Integer.valueOf(object[2].toString()));
            data.put(site, item);
        }
        return data;
    }

    /**
     * TT03 - Bảng 03
     *
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, TT03Table03> getTT03Table03(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, TT03Table03> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/tt03_table03.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            TT03Table03 item = new TT03Table03();
            item.setTlvr(Integer.valueOf(object[1].toString()));
            item.setTlvr1000(Integer.valueOf(object[2].toString()));
            item.setMonth(Integer.valueOf(object[3].toString()));
            item.setMonth1000(Integer.valueOf(object[4].toString()));
            data.put(site, item);
        }
        return data;
    }

    /**
     * TT03 - Bảng 06
     *
     * @auth pdThang
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, TT03Table06> getTT03Table06(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, TT03Table06> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/tt03_table06.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            TT03Table06 item = new TT03Table06();
            item.setR11(Integer.valueOf(object[1].toString()));
            item.setR12(Integer.valueOf(object[2].toString()));
            item.setR13(Integer.valueOf(object[3].toString()));
            item.setR21(Integer.valueOf(object[4].toString()));
            item.setR22(Integer.valueOf(object[5].toString()));
            item.setR23(Integer.valueOf(object[6].toString()));
            data.put(site, item);
        }
        return data;
    }

    /**
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, TT03Table04> getTT03Table04(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, TT03Table04> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/tt03_table04.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            TT03Table04 item = new TT03Table04();
            item.setRegister(Integer.valueOf(object[1].toString()));
            item.setTotal(Integer.valueOf(object[2].toString()));
            item.setInh(Integer.valueOf(object[3].toString()));
            data.put(site, item);
        }
        return data;
    }

    /**
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, Map<String, TT03Table05>> getTT03Table05(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, Map<String, TT03Table05>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/tt03_table05.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            String type = object[1].toString(); //bậc phác đồ

            TT03Table05 item = new TT03Table05();
            item.setArv(Integer.valueOf(object[9].toString()));
            item.setBackTreatment(Integer.valueOf(object[4].toString()));
            item.setDead(Integer.valueOf(object[8].toString()));
            item.setFirstRegister(Integer.valueOf(object[3].toString()));
            item.setMoveAway(Integer.valueOf(object[6].toString()));
            item.setPreArv(Integer.valueOf(object[2].toString()));
            item.setQuitTreatment(Integer.valueOf(object[7].toString()));
            item.setTransferTo(Integer.valueOf(object[5].toString()));
            item.setWard(Integer.valueOf(object[10].toString()));

            if (data.get(site) == null) {
                data.put(site, new HashMap<String, TT03Table05>());
                data.get(site).put("1", new TT03Table05());
                data.get(site).put("2", new TT03Table05());
                data.get(site).put("total", new TT03Table05());
            }
            data.get(site).put(type, item);
        }

        for (Map.Entry<Long, Map<String, TT03Table05>> entry : data.entrySet()) {
            // Tính số điều trị còn lại trong thời gian báo cáo
//            for (Map.Entry<String, TT03Table05> entry1 : entry.getValue().entrySet()) {
//                entry1.getValue().setArv((entry1.getValue().getFirstRegister() + entry1.getValue().getBackTreatment() + entry1.getValue().getTransferTo())
//                        - (entry1.getValue().getMoveAway() + entry1.getValue().getQuitTreatment() + entry1.getValue().getDead()));
//            }

            // Tính tổng
            TT03Table05 table = data.get(entry.getKey()).get("total");
            Map<String, TT03Table05> value = entry.getValue();
            for (Map.Entry<String, TT03Table05> chitieu : value.entrySet()) {
                if (StringUtils.isNotEmpty(chitieu.getKey()) && chitieu.getKey().equals("total")) {
                    continue;
                }
                table.setArv(table.getArv() + chitieu.getValue().getArv());
                table.setBackTreatment(table.getBackTreatment() + chitieu.getValue().getBackTreatment());
                table.setDead(table.getDead() + chitieu.getValue().getDead());
                table.setFirstRegister(table.getFirstRegister() + chitieu.getValue().getFirstRegister());
                table.setMoveAway(table.getMoveAway() + chitieu.getValue().getMoveAway());
                table.setPreArv(table.getPreArv() + chitieu.getValue().getPreArv());
                table.setQuitTreatment(table.getQuitTreatment() + chitieu.getValue().getQuitTreatment());
                table.setTransferTo(table.getTransferTo() + chitieu.getValue().getTransferTo());
                table.setWard(table.getWard() + chitieu.getValue().getWard());
            }
            data.get(entry.getKey()).put("total", table);
        }

        return data;
    }

    public HashMap<String, HashMap<String, OpcMerSixMonthTable>> getMerSixMonthTable(Set<Long> siteID, Date beforeSixMonth, Date fromDate, Date toDate) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("before_six_month", TextUtils.formatDate(beforeSixMonth, FORMATDATE));
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_table01_six_month.sql", params).getResultList();

        String key = "";
        String gender = "";
        HashMap<String, HashMap<String, OpcMerSixMonthTable>> table = new HashMap<>();

        // Khởi tạo giá trị cho từng nhóm tuổi theo giới tính
        HashSet<String> keys = new HashSet<>();
        keys.add("mauSoMoi");
        keys.add("mauSoCu");
        keys.add("tuSoMoi");
        keys.add("tuSoCu");
        keys.add("phacDo3Thang");
        keys.add("itNhat1LanCu");
        keys.add("itNhat1LanMoi");
        keys.add("sangLocNghiLaoCu");
        keys.add("sangLocNghiLaoMoi");
        keys.add("chuanDoanLaoCu");
        keys.add("chuanDoanLaoMoi");
        keys.add("dieuTriLaoCu");
        keys.add("dieuTriLaoMoi");

        for (String obj : keys) {
            table.put(obj, new HashMap<String, OpcMerSixMonthTable>());
            table.get(obj).put(GenderEnum.MALE.getKey(), new OpcMerSixMonthTable());
            table.get(obj).put(GenderEnum.FEMALE.getKey(), new OpcMerSixMonthTable());
            table.get(obj).put("total", new OpcMerSixMonthTable());
        }

        if (result == null || result.isEmpty()) {
            return table;
        }

        OpcMerSixMonthTable formOne;
        for (Object[] objects : result) {
            if (objects == null || StringUtils.isEmpty(objects[0].toString())
                    || (!objects[1].toString().equals(GenderEnum.MALE.getKey()) && !objects[1].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }

            gender = objects[1].toString();
            key = objects[0].toString();
            keys.add(key);

            formOne = new OpcMerSixMonthTable();
            formOne.setUnderOneAge(Integer.parseInt(objects[2].toString()));
            formOne.setOneToFourteen(Integer.parseInt(objects[3].toString()));
            formOne.setOverFifteen(Integer.parseInt(objects[4].toString()));

            if (table.containsKey(key)) {
                formOne.setSum(formOne.getSum() + table.get(key).get("total").getSum());
                table.get(key).put(gender, formOne);
                table.get(key).put("total", formOne);
            }
        }

        return table;
    }

    public ViralloadTable getViralloadTable(Long siteID, String fromDate, String toDate) {
        ViralloadTable table = new ViralloadTable();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        List<Object[]> result = query("opc/viralload_site.sql", params).getResultList();
        int i = 0;
        Object[] object = result.get(0);

        table.setXnTrongThang(Integer.valueOf(object[i++].toString()));

        table.setXnLan1Duoi200(Integer.valueOf(object[i++].toString()));
        table.setXnLan1Tu200Den1000(Integer.valueOf(object[i++].toString()));
        table.setXnLan1Tren1000(Integer.valueOf(object[i++].toString()));

        table.setXnLan2Duoi200(Integer.valueOf(object[i++].toString()));
        table.setXnLan2Tu200Den1000(Integer.valueOf(object[i++].toString()));
        table.setXnLan2Tren1000(Integer.valueOf(object[i++].toString()));

        table.setArvBac2Duoi200(Integer.valueOf(object[i++].toString()));
        table.setArvBac2Tu200Den1000(Integer.valueOf(object[i++].toString()));
        table.setArvBac2Tren1000(Integer.valueOf(object[i++].toString()));

        table.setXn12ThangDuoi200(Integer.valueOf(object[i++].toString()));
        table.setXn12ThangTu200Den1000(Integer.valueOf(object[i++].toString()));
        table.setXn12ThangTren1000(Integer.valueOf(object[i++].toString()));

        return table;
    }

    public ViralloadManagerTable02 getViralloadManagerTable02(Set<Long> siteID, String fromDate, String toDate) {
        ViralloadManagerTable02 table = new ViralloadManagerTable02();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        List<Object[]> result = query("opc/viralload_manager_table02.sql", params).getResultList();
        int i = 0;
        Object[] object = result.get(0);

        table.setXnTrongThang(Integer.valueOf(object[i++].toString()));
        table.setArvBac2Duoi1000(Integer.valueOf(object[i++].toString()));
        table.setArvBac2Tren1000(Integer.valueOf(object[i++].toString()));

        return table;
    }

    public Map<Long, ViralloadManagerTable> getViralloadManagerTable(Set<Long> siteID, String fromDate, String toDate) {
        Map<Long, ViralloadManagerTable> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        List<Object[]> result = query("opc/viralload_manager.sql", params).getResultList();

        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            int i = 1;
            Long site = Long.valueOf(object[0].toString());
            ViralloadManagerTable item = new ViralloadManagerTable();
            item.setXnbac1(Integer.valueOf(object[i++].toString()));
            item.setXnbac2(Integer.valueOf(object[i++].toString()));
            item.setXn6thang(Integer.valueOf(object[i++].toString()));
            item.setXn12thang(Integer.valueOf(object[i++].toString()));
            item.setXnkhac(Integer.valueOf(object[i++].toString()));
            item.setTbdt(Integer.valueOf(object[i++].toString()));
            item.setPnmt(Integer.valueOf(object[i++].toString()));
            item.setXnLan1phatHien(Integer.valueOf(object[i++].toString()));
            item.setXnLan1Duoi200(Integer.valueOf(object[i++].toString()));
            item.setXnLan1Tu200Den1000(Integer.valueOf(object[i++].toString()));
            item.setXnLan1Tren1000(Integer.valueOf(object[i++].toString()));
            item.setTuvan(Integer.valueOf(object[i++].toString()));
            item.setXnLan2duoi1000(Integer.valueOf(object[i++].toString()));
            item.setXnLan2tren1000(Integer.valueOf(object[i++].toString()));
            item.setThatbaiphacdo(Integer.valueOf(object[i++].toString()));

            data.put(site, item);
        }

        return data;
    }

    /**
     * BC phác đồ
     *
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, RegimenTable> getRegimenTable(List<Long> siteID, Date fromDate, Date toDate, String insurance) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        params.put("insurance", insurance);

        List<Object[]> result = query("opc/regimen.sql", params).getResultList();
        HashMap<String, RegimenTable> data = new HashMap<>();

        for (Object[] object : result) {
            String regimentID = String.valueOf(object[0].toString());
            String type = String.valueOf(object[1].toString());
            RegimenTable item = new RegimenTable();
            item.setRegimentID(regimentID);
            item.setType(type);
            item.setBatdaunhan(Integer.valueOf(object[2].toString()));
            item.setDangnhan(Integer.valueOf(object[3].toString()));
            item.setBonhan_chuyendi(Integer.valueOf(object[4].toString()));
            item.setBonhan_tuvong(Integer.valueOf(object[5].toString()));
            item.setBonhan_botri(Integer.valueOf(object[6].toString()));
            item.setBonhan_matdau(Integer.valueOf(object[7].toString()));
            item.setNhanThuoc(Integer.valueOf(object[9].toString()));
            data.put(String.format("%s_%s", regimentID, type), item);
        }
        return data;
    }

    /**
     * Báo cáo bảo hiểm y tế
     *
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Map<Long, InsuranceTable> getInsuranceTable(Set<Long> siteID, Date fromDate, Date toDate) {
        Map<Long, InsuranceTable> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/insurance.sql", params).getResultList();
        for (Object[] object : result) {
            if (object[0] == null || object[0].toString().equals("")) {
                continue;
            }
            Long site = Long.valueOf(object[0].toString());
            InsuranceTable item = new InsuranceTable();
            item.setInsurance100(Integer.valueOf(object[1].toString()));
            item.setInsurance95(Integer.valueOf(object[2].toString()));
            item.setInsurance80(Integer.valueOf(object[3].toString()));
            item.setInsuranceNone(Integer.valueOf(object[5].toString()));
            item.setExpire1(Integer.valueOf(object[6].toString()));
            item.setExpire2(Integer.valueOf(object[7].toString()));
            item.setExpire3(Integer.valueOf(object[8].toString()));
            item.setDungtuyen(Integer.valueOf(object[9].toString()));
            item.setTraituyen(Integer.valueOf(object[10].toString()));
            item.setInsurance(Integer.valueOf(object[11].toString()));
            //Khác
            item.setInsuranceOther(item.getInsurance()
                    - item.getInsurance100()
                    - item.getInsurance95()
                    - item.getInsurance80());
            data.put(site, item);
        }
        return data;
    }

    public List<QuickTreatmentTable> getQuickTreatmentTable(Set<Long> siteID, Date fromDate, Date toDate) {

        List<QuickTreatmentTable> treatmentTables = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/quick_treatment.sql", params).getResultList();

        if (result == null || result.isEmpty()) {
            for (Long id : siteID) {
                treatmentTables.add(new QuickTreatmentTable(id));
            }
            return treatmentTables;
        }

        QuickTreatmentTable quick = new QuickTreatmentTable();
        for (Object[] obj : result) {
            quick = new QuickTreatmentTable();
            quick.setSiteID(Long.valueOf(obj[0].toString()));
            quick.setArvTreatment(Integer.valueOf(obj[1].toString()));
            quick.setArvQuickTreatment(Integer.valueOf(obj[2].toString()));
            quick.setArvTreatmentDuringDay(Integer.valueOf(obj[3].toString()));
            quick.setReceiveMedicineMax90(Integer.valueOf(obj[4].toString()));
            quick.setReceiveMedicineThreeMonth(Integer.valueOf(obj[5].toString()));
            quick.setNote("");
            treatmentTables.add(quick);
        }

        List<QuickTreatmentTable> cloneTable = new ArrayList(treatmentTables);
        List<Long> resultID = new ArrayList<>();
        resultID.addAll(CollectionUtils.collect(cloneTable, TransformerUtils.invokerTransformer("getSiteID")));// get site ID

        for (Long id : siteID) {
            if (resultID.contains(id)) {
                continue;
            }
            quick = new QuickTreatmentTable(id);
            treatmentTables.add(quick);
        }
        return treatmentTables;
    }

    @Cacheable(value = "getPreArv")
    public List<OpcPreArvTableForm> getPreArv(Set<Long> siteID, int year, String word, Date fromDate, Date toDate) {
        String FormatDate = "dd/MM/yyyy";
        List<OpcPreArvTableForm> data = new ArrayList<>();
        HashMap<String, OpcPreArvTableForm> defaultArv = new HashMap<>();
        HashMap<String, Object> param2 = new HashMap<>();
        param2.put("site_id", siteID);
        param2.put("word", word);
        param2.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        param2.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        
        List<Object[]> result2 = query("opc/pre_arv_2.sql", param2).getResultList();
        for (Object[] object : result2) {
            if(Integer.parseInt(object[26].toString()) == year){
            OpcPreArvTableForm item = new OpcPreArvTableForm();
            item.setArvID(object[0] == null ? "" : object[0].toString());
            item.setRegistrationTime(object[2] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[2].toString().substring(0, 10)));
            item.setFullName(object[3] == null ? "" : object[3].toString());
            item.setCode(object[4] == null ? "" : object[4].toString());
            item.setGenderID(object[5] == null ? "" : object[5].toString());
            item.setDob(object[6] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[6].toString().substring(0, 10)));
            item.setPermanentAddress(object[7] == null ? "" : object[7].toString());
            item.setPermanentAddressGroup(object[8] == null ? "" : object[8].toString());
            item.setPermanentAddressStreet(object[9] == null ? "" : object[9].toString());
            item.setPermanentWardID(object[10] == null ? "" : object[10].toString());
            item.setPermanentDistrictID(object[11] == null ? "" : object[11].toString());
            item.setPermanentProvinceID(object[12] == null ? "" : object[12].toString());
            item.setRegistrationStatus(object[13] == null ? "" : object[13].toString());
            item.setClinicalStage(object[14] == null ? "" : object[14].toString());
            item.setCd4(object[15] == null ? "" : object[15].toString());
            item.setPatientWeight(object[16] == null ? 0 : object[16].toString().equals("0.0") ? 0 : Float.parseFloat(object[16].toString()));
            item.setPatientHeight(object[17] == null ? 0 : object[17].toString().equals("0.0") ? 0 : Float.parseFloat(object[17].toString()));
            item.setStatusOfTreatmentID(object[18] == null ? "" : object[18].toString());
            item.setEndCase(object[19] == null ? "" : object[19].toString());
            item.setEndTime(object[20] == null ? "" : object[20].toString());
            item.setTranferFromTime(object[24] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[24].toString().substring(0, 10)));
            item.setTreatmentTime(object[25] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[25].toString().substring(0, 10)));
            defaultArv.put(item.getArvID(), item);
            }
        }
        
        HashMap<String, Object> param3 = new HashMap<>();
        param3.put("site_id", siteID);
        List<Object[]> result3 = query("opc/pre_arv_3.sql", param3).getResultList();
        for (Object[] object : result3) {
            if (defaultArv.get(object[0].toString()) != null) {
                defaultArv.get(object[0].toString()).setTreatmentTime(object[2] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[2].toString().substring(0, 10)));
            }
        }
        
        
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(fromDate);
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(toDate);
        
        Map<String, OpcPreArvChildForm> childsMap = new HashMap<>();
        
        while (beginCalendar.before(finishCalendar)) {
            Date start = TextUtils.getFirstDayOfMonth(beginCalendar.getTime());
            Date end = TextUtils.getLastDayOfMonth(beginCalendar.getTime());
            HashMap<String, Object> params = new HashMap<>();
            params.put("site_id", siteID);
            params.put("beginYear", TextUtils.formatDate(fromDate, FORMATDATE));
            params.put("endYear", TextUtils.formatDate(TextUtils.getLastDayOfYear(fromDate), FORMATDATE));
            params.put("start", TextUtils.formatDate(fromDate, FORMATDATE));
            params.put("end", TextUtils.formatDate(toDate, FORMATDATE));
            params.put("from_date", TextUtils.formatDate(start, FORMATDATE));
            params.put("to_date", TextUtils.formatDate(end, FORMATDATE));
            List<Object[]> result = query("opc/pre_arv.sql", params).getResultList();
            if (result == null || result.isEmpty()) {
                beginCalendar.add(Calendar.MONTH, 1);
                continue;
            }
            
            for (Object[] object : result) {
                if(defaultArv.get(object[0].toString()) != null){
                    OpcPreArvChildForm child = new OpcPreArvChildForm();
                    child.setArvID(object[0] == null ? "" : object[0].toString());
                    child.setYear(object[6] == null ? "" : object[6].toString());
                    child.setQuarter(object[7] == null ? "" : object[7].toString());
                    child.setMonth(object[8] == null ? "" : object[8].toString());
                    child.setResult(object[9] == null ? "" : object[9].toString());
                    child.setTime(object[10] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[10].toString().substring(0, 10)));
                    child.setType(object[11] == null ? "" : object[11].toString());
                    childsMap.put(String.format("%s-%s-%s-%s", child.getArvID(), child.getResult(), child.getTime(), child.getType()), child);
                }
            }
            beginCalendar.add(Calendar.MONTH, 1);
        }
        
        
        
        for (Map.Entry<String, OpcPreArvTableForm> entry : defaultArv.entrySet()) {
            for (Map.Entry<String, OpcPreArvChildForm> e : childsMap.entrySet()) {
                if(entry.getKey().equals(e.getKey().split("-")[0])){
                    List<OpcPreArvChildForm> childList = entry.getValue().getChilds() == null ? new ArrayList() : entry.getValue().getChilds();
                    childList.add(e.getValue());
                    entry.getValue().setChilds(childList);
                }
            }
        }
        
        for (Map.Entry<String, OpcPreArvTableForm> entry : defaultArv.entrySet()) {
            OpcPreArvTableForm item = entry.getValue();
            data.add(item);
        }
        return data;
    }

    public List<ArvBookTableForm> getArvBook(Set<Long> siteID, String word, Date fromDate, Date toDate) {
        String FormatDate = "dd/MM/yyyy";
        List<ArvBookTableForm> data = new ArrayList<>();
        HashMap<String, ArvBookTableForm> defaultArv = new HashMap<>();
        
        //Thông tin cơ bản, Khi bắt đầu điều trị
        HashMap<String, Object> param2 = new HashMap<>();
        param2.put("site_id", siteID);
        param2.put("word", word);
        param2.put("flag", "1");
        param2.put("start", TextUtils.formatDate(fromDate, FORMATDATE));
        param2.put("end", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result2 = query("opc/arv_book_2.sql", param2).getResultList();
        for (Object[] object : result2) {
            ArvBookTableForm item = new ArvBookTableForm();
            item.setArvID(object[0] == null ? "" : object[0].toString());
            item.setStageID(object[1] == null ? "" : object[1].toString());
            item.setCode(object[2] == null ? "" : object[2].toString());
            item.setFullName(object[3] == null ? "" : object[3].toString());
            item.setGenderID(object[4] == null ? "" : object[4].toString());
            item.setDob(object[5] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[5].toString().substring(0, 10)));
            item.setTreatmentTime(object[6] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[6].toString().substring(0, 10)));
            item.setFirstTreatmentRegimentID(object[7] == null ? "" : object[7].toString());
            item.setTreatmentRegimentIDChange(object[7] == null ? "" : object[7].toString());
            item.setClinicalStage(object[8] == null ? "" : object[8].toString());
            item.setCd4(object[9] == null ? "" : object[9].toString());
            item.setPatientWeight(object[10] == null ? 0 : object[10].toString().equals("0.0") ? 0 : Float.parseFloat(object[10].toString()));
            item.setPatientHeight(object[11] == null ? 0 : object[11].toString().equals("0.0") ? 0 : Float.parseFloat(object[11].toString()));
            List<ArvBookChildForm> childs = new ArrayList();
            
            ArvBookChildForm item2 = new ArvBookChildForm();
            item2.setArvID(item.getArvID());
            item2.setTreatmentRegimentID(item.getFirstTreatmentRegimentID());
            item2.setMonth(0 + "");
            item2.setResult("");
            item2.setTime(item.getTreatmentTime());
            item2.setType("ĐT");
            childs.add(item2);
            item.setChilds(childs);
            
            defaultArv.put(item.getArvID(), item);
        }
        
        param2.put("flag", null);
        param2.remove("start");
        param2.remove("end");
        List<Object[]> result4 = query("opc/arv_book_2.sql", param2).getResultList();
        for (Object[] object : result4) {
            if (defaultArv.get(object[0].toString()) != null && StringUtils.isEmpty(defaultArv.get(object[0].toString()).getTreatmentTime())) {
                defaultArv.get(object[0].toString()).setTreatmentTime(object[6] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[6].toString().substring(0, 10)));
            }
            if(defaultArv.get(object[0].toString()) != null && StringUtils.isEmpty(defaultArv.get(object[0].toString()).getFirstTreatmentRegimentID())){
                defaultArv.get(object[0].toString()).setFirstTreatmentRegimentID(object[7] == null ? "" : object[7].toString());
                defaultArv.get(object[0].toString()).getChilds().get(0).setTreatmentRegimentID(object[7] == null ? "" : object[7].toString());
            }
        }

        //Thay đổi phác đồ điều trị
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(fromDate);
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(toDate);
        
        Map<String, ArvBookChildForm> childMap = new HashMap<>();
        while (beginCalendar.before(finishCalendar)) {
            HashMap<String, Object> param3 = new HashMap<>();
            param3.put("site_id", siteID);
            param3.put("start", TextUtils.formatDate(fromDate, FORMATDATE));
            param3.put("end", TextUtils.formatDate(toDate, FORMATDATE));
            param3.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfMonth(beginCalendar.getTime()), FORMATDATE));
            List<Object[]> result3 = query("opc/arv_book_3.sql", param3).getResultList();
            
            if (result3 == null || result3.isEmpty()) {
                beginCalendar.add(Calendar.MONTH, 1);
                continue;
            }
            for (Object[] object : result3) {
                if (defaultArv.get(object[0].toString()) != null) {
                    defaultArv.get(object[0].toString()).setRegimentDate(object[2] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[2].toString().substring(0, 10)));
                    defaultArv.get(object[0].toString()).setTreatmentRegimentID(object[3] == null ? "" : object[3].toString());
                    defaultArv.get(object[0].toString()).setCausesChange(object[4] == null ? "" : object[4].toString());
                    if(object[2].toString() != null){
                        ArvBookChildForm child = new ArvBookChildForm();
                        Calendar testTime = Calendar.getInstance();
                        testTime.setTime(TextUtils.convertDate(object[2].toString().substring(0, 10), FORMATDATE));
                        Calendar firstTreatmentTime = Calendar.getInstance();
                        firstTreatmentTime.setTime(TextUtils.convertDate(defaultArv.get(object[0].toString()).getTreatmentTime(), FormatDate));

                        child.setMonth((testTime.get(Calendar.YEAR) - firstTreatmentTime.get(Calendar.YEAR))*12 + (testTime.get(Calendar.MONTH) - firstTreatmentTime.get(Calendar.MONTH)) + "");
                        child.setResult("ĐT");
                        child.setTime(TextUtils.formatDate(FORMATDATE, FormatDate, object[2].toString().substring(0, 10)));
                        child.setType("ĐT");
                        child.setArvID(defaultArv.get(object[0].toString()).getArvID());
                        child.setTreatmentRegimentID(object[3] == null ? "" : object[3].toString());
                        
                        childMap.put(String.format("%s-%s-%s-%s", child.getArvID(), child.getResult(), child.getTime(), child.getType()), child);
                    }
                }
            }
            beginCalendar.add(Calendar.MONTH, 1);
        }

        //Theo dõi người bệnh trong quá trình điều trị bằng thuốc kháng HIV
        beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(fromDate);
        finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(toDate);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(fromDate);
        endTime.add(Calendar.YEAR, 5);
        
        while (beginCalendar.before(endTime)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("site_id", siteID);
            params.put("beginTime", TextUtils.formatDate(fromDate, FORMATDATE));
            params.put("endTime", TextUtils.formatDate(TextUtils.getLastDayOfYear(endTime.getTime()), FORMATDATE));
            params.put("start", TextUtils.formatDate(fromDate, FORMATDATE));
            params.put("end", TextUtils.formatDate(toDate, FORMATDATE));
            params.put("from_date", TextUtils.formatDate(TextUtils.getFirstDayOfMonth(beginCalendar.getTime()), FORMATDATE));
            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfMonth(beginCalendar.getTime()), FORMATDATE));
            List<Object[]> result = query("opc/arv_book.sql", params).getResultList();
            if (result == null || result.isEmpty()) {
                beginCalendar.add(Calendar.MONTH, 1);
                continue;
            }
            
            ArvBookChildForm child;
            for (Object[] object : result) {
                if (defaultArv.get(object[0].toString()) != null) {
                    ArvBookTableForm item = defaultArv.get(object[0].toString());
                    Calendar testTime = Calendar.getInstance();
                    if(object[10].toString().equals("B")){
                        testTime.setTime(TextUtils.convertDate(object[4].toString().substring(0, 10), FORMATDATE));
                    } else {
                        testTime.setTime(TextUtils.convertDate(object[9].toString().substring(0, 10), FORMATDATE));
                    }
                    Calendar firstTreatmentTime = Calendar.getInstance();
                    firstTreatmentTime.setTime(TextUtils.convertDate(item.getTreatmentTime(), FormatDate));
                    
                    child = new ArvBookChildForm();
                    child.setMonth((testTime.get(Calendar.YEAR) - firstTreatmentTime.get(Calendar.YEAR))*12 + (testTime.get(Calendar.MONTH) - firstTreatmentTime.get(Calendar.MONTH)) + "");
                    child.setArvID(object[0] == null ? "" : object[0].toString());
                    child.setStageID(object[1] == null ? "" : object[1].toString());
                    child.setStatusOftreatment(object[2] == null ? "" : object[2].toString());
                    child.setTreatmentRegimentID(object[3] == null ? "" : object[3].toString());
                    child.setEndTime(object[4] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[4].toString().substring(0, 10)));
                    child.setResult(object[8] == null ? "" : object[8].toString());
                    child.setTime(object[9] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[9].toString().substring(0, 10)));
                    child.setType(object[10] == null ? "" : object[10].toString());
                    childMap.put(String.format("%s-%s-%s-%s", child.getArvID(), child.getResult(), child.getTime(), child.getType()), child);
                    
                    item.setEndTime(object[4] == null ? "" : TextUtils.formatDate(FORMATDATE, FormatDate, object[4].toString().substring(0, 10)));
                }
            }
            beginCalendar.add(Calendar.MONTH, 1);
        }
        
        for (Map.Entry<String, ArvBookTableForm> entry : defaultArv.entrySet()) {
            for (Map.Entry<String, ArvBookChildForm> e : childMap.entrySet()) {
                if(entry.getKey().equals(e.getKey().split("-")[0])){
                    List<ArvBookChildForm> childList = entry.getValue().getChilds() == null ? new ArrayList() : entry.getValue().getChilds();
                    childList.add(e.getValue());
                    entry.getValue().setChilds(childList);
                }
            }
        }
        
        for (Map.Entry<String, ArvBookTableForm> entry : defaultArv.entrySet()) {
            ArvBookTableForm item = entry.getValue();
            Calendar testTime = Calendar.getInstance();
            if(StringUtils.isNotEmpty(item.getEndTime())){
                testTime.setTime(TextUtils.convertDate(item.getEndTime(), FormatDate));
            }
            Calendar firstTime = Calendar.getInstance();
            firstTime.setTime(TextUtils.convertDate(item.getTreatmentTime(), FormatDate));

            item.setMonth((testTime.get(Calendar.YEAR) - firstTime.get(Calendar.YEAR))*12 + (testTime.get(Calendar.MONTH) - firstTime.get(Calendar.MONTH)));
            data.add(item);
        }
        return data;
    }
    
    /**
     * Lấy dữ liệu sổ kháng thể arv
     * 
     * @param siteID
     * @param keyword
     * @param fromDate
     * @param toDate
     * @param page
     * @param size
     * @return
     * @throws ParseException 
     */
    public DataPage<SoKhangTheArvForm> getSoKhangTheArv(Long siteID, String keyword, Date fromDate, Date toDate, int page, int size) throws ParseException {
        
        //Thông tin cơ bản, Khi bắt đầu điều trị
        String toDateFormat = "yyyy-MM-dd hh:mm:ss";
        DataPage<SoKhangTheArvForm> dataPage = new DataPage<>();
        dataPage.setCurrentPage(page);
        dataPage.setMaxResult(size);
        
        HashMap<String, Object> param = new HashMap<>();
        param.put("siteID", siteID);
        param.put("fromDate", TextUtils.formatDate(fromDate, FORMATDATE));
        param.put("toDate", TextUtils.formatDate(toDate, FORMATDATE));
        param.put("keywords", StringUtils.isNotEmpty(keyword) ? keyword.trim() : null);
        
        HashMap<String, Object> paramLine2 = new HashMap<>();
        paramLine2.put("siteID", siteID);
        paramLine2.put("fromDate", TextUtils.formatDate(fromDate, FORMATDATE));
        paramLine2.put("toDate", TextUtils.formatDate(toDate, FORMATDATE));
        paramLine2.put("keywords", StringUtils.isNotEmpty(keyword) ? keyword.trim() : null);
        
        List<Object[]> result = query("opc/arv_book_tragbn1.sql", param).getResultList();
        List<Object[]> resultSecondLine = query("opc/arv_book_tragbn2.sql", paramLine2).getResultList();
        
        List<SoKhangTheArvForm> resultForm = new ArrayList<>();
        
        Map<String, List<SoKhangTheArvTableForm>> mapResult = new HashMap<>();
        Map<String, List<SoKhangTheArvTableForm>> mapResultSecondLine = new HashMap<>();
        Map<String, List<SoKhangTheArvTableForm>> mapChild = new HashMap<>();
        Map<String, String> removeMap = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return dataPage;
        }
        
        SoKhangTheArvForm arvForm = new SoKhangTheArvForm();
        SoKhangTheArvTableForm form = new SoKhangTheArvTableForm();
        SoKhangTheArvTableForm child = new SoKhangTheArvTableForm();
        SoKhangTheArvTableForm lastKnownArv = new SoKhangTheArvTableForm();
        SoKhangTheArvTableForm sixMonth = new SoKhangTheArvTableForm();
        boolean temp = false;
        int labelCurrentDate = 0;
        int labelFinalEnd = 0;
        String currentDate = getFirstdateMonth(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
        
        for (Object[] obj : result) {
            String regimentId = "";
            String key = String.valueOf(obj[0]);
            form = new SoKhangTheArvTableForm();
            
            // Xử lý trong khoảng tìm kiếm
            form.setTreatmentTime(obj[8] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[8])));
            if (removeMap.containsKey(key) && removeMap.get(key).equals("invalid")) {
                continue;
            } else if (StringUtils.isNotEmpty(form.getTreatmentTime()) && ((fromDate != null && compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(fromDate, ddMMyyyy)) < 0) || ( toDate != null && compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(toDate, ddMMyyyy)) > 0))) {
                removeMap.put(key, "invalid");
                continue;
            }
            removeMap.put(key, "");
            
            if (!mapResult.containsKey(key)) {
                mapResult.put(key, new ArrayList<SoKhangTheArvTableForm>());
            }
            
            form.setPatientID(key);
            form.setFullname(obj[1] == null ? null : String.valueOf(obj[1]));
            form.setGenderID(obj[2] == null ? null : String.valueOf(obj[2]));
            form.setDob(obj[3] == null ? null : TextUtils.formatDate(toDateFormat, "yyyy", String.valueOf(obj[3])));
            form.setArvID(obj[4] == null ? null : String.valueOf(obj[4]));
            form.setStageID(obj[5] == null ? null : String.valueOf(obj[5]));
            form.setStatusOfTreatment(obj[6] == null ? null : String.valueOf(obj[6]));
            form.setTreatment(obj[7] == null ? null : String.valueOf(obj[7]));
            
            form.setEndCase(obj[9] == null ? null : String.valueOf(obj[9]));
            form.setEndTime(obj[10] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[10])));
            form.setRegistrationType(obj[11] == null ? null : String.valueOf(obj[11]));
            form.setTranferFromTime(obj[12] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[12])));
            form.setTreatmentRegimentID(obj[13] == null ? null : String.valueOf(obj[13]));
            form.setTreatmentRegimenStage(obj[14] == null ? null : String.valueOf(obj[14]));
            
            form.setRegimenDate(obj[15] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[15])));
            form.setExaminationTime(obj[17] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[17])));
            form.setDateOfArrival(obj[18] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[18])));
            form.setCode(obj[19] == null ? null : String.valueOf(obj[19]));
            form.setClinicalStage(obj[20] == null ? null : String.valueOf(obj[20]));
            form.setCd4(obj[21] == null ? null : String.valueOf(obj[21]));
            form.setCausesChange(obj[22] == null ? null : String.valueOf(obj[22]));
            form.setWeight(obj[23] == null ? null : String.valueOf(obj[23]));
            form.setHeight(obj[24] == null ? null : String.valueOf(obj[24]));
            form.setLine("1");
            
            mapResult.get(key).add(form);
            
            if (mapResult.get(key) != null && !mapResult.get(key).isEmpty()) {
                for (int i = mapResult.get(key).size() - 1; i >= 0; i--) {
                    if (StringUtils.isEmpty(mapResult.get(key).get(i).getTreatmentRegimentID()) && StringUtils.isEmpty(mapResult.get(key).get(i).getRegimenDate())) {
                        mapResult.get(key).get(i).setTreatmentRegimentID(regimentId);
                        continue;
                    }
                    regimentId = mapResult.get(key).get(i).getTreatmentRegimentID();
                }
            }
        }
        
        // Lấy dữ liệu dòng dưới của bảng
        for (Object[] obj : resultSecondLine) {
            String key = String.valueOf(obj[0]);
            form = new SoKhangTheArvTableForm();
            
            // Xử lý trong khoảng tìm kiếm
            removeMap = new HashMap<>();
            form.setTreatmentTime(obj[13] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[13])));
            if (removeMap.containsKey(key) && removeMap.get(key).equals("invalid")) {
                continue;
            } else if (compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(fromDate, ddMMyyyy)) < 0 || compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(toDate, ddMMyyyy)) > 0) {
                removeMap.put(key, "invalid");
                continue;
            }
            removeMap.put(key, "");
            
            if (compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(fromDate, ddMMyyyy)) < 0 || compareDateTo(form.getTreatmentTime(), TextUtils.formatDate(toDate, ddMMyyyy)) > 0) {
                continue;
            }
            
            if (!mapResultSecondLine.containsKey(key)) {
                mapResultSecondLine.put(key, new ArrayList<SoKhangTheArvTableForm>());
            }
            
            form.setPatientID(key);
            form.setLaoStartTime(obj[2] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[2])));
            form.setInhStartTime(obj[3] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[3])));
            form.setCotrimoxazoleStartTime(obj[4] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[4])));
            form.setCd4Result(obj[5] == null ? null : String.valueOf(obj[5]));
            form.setCd4TestTime(obj[6] == null ? null : TextUtils.formatDate(toDateFormat, ddMMyyyy, String.valueOf(obj[6])));
            form.setStageID(obj[8] == null ? null : String.valueOf(obj[8]));
            
            mapResultSecondLine.get(key).add(form);
        }
        
        // Hiển thị theo từng dòng gồm các tháng, mỗi tháng gồm các lần đăng ký hoặc điều trị lại
        String treatmentDate = "";
        String treatmentOrigin = "";
        String treatmentDate2 = "";
        String lastEndTime = "";
        boolean hasHideValue = false;
        
        for (Map.Entry<String, List<SoKhangTheArvTableForm>> entry : mapResult.entrySet()) {
            labelCurrentDate = 0;
            labelFinalEnd = 0;
            List<SoKhangTheArvTableForm> secondLine = mapResultSecondLine.get(entry.getKey());
            
            treatmentDate = "";
            arvForm = new SoKhangTheArvForm();
            mapChild = new HashMap<>();
            
            arvForm.setVeryFirstRegiment(StringUtils.isEmpty(arvForm.getVeryFirstRegiment()) && !entry.getValue().isEmpty() ? entry.getValue().get(0).getTreatmentRegimentID() : "");
            
            // Lấy ngày kết thúc cuối cùng nếu có
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                SoKhangTheArvTableForm last = entry.getValue().get(entry.getValue().size() - 1);
                lastEndTime = StringUtils.isNotEmpty(last.getTranferFromTime()) ? last.getTranferFromTime() : StringUtils.isNotEmpty(last.getEndTime()) ? last.getEndTime() : "";
            }
            
            for (SoKhangTheArvTableForm f : entry.getValue()) {
                if (f.getStatusOfTreatment().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()) && StringUtils.isNotEmpty(f.getTreatmentRegimentID())) {
                    arvForm.setGeneral(f);
                    treatmentDate = getFirstdateMonth(StringUtils.isNotEmpty(f.getTreatmentTime()) ? f.getTreatmentTime() : "");
                    break;
                }
                if (StringUtils.isNotEmpty(f.getDateOfArrival())) {
                    arvForm.setGeneral(f);
                    treatmentDate = getFirstdateMonth(StringUtils.isNotEmpty(f.getDateOfArrival()) ? f.getDateOfArrival() : "");
                    break;
                }
            }
            
            if (arvForm.getGeneral() == null) {
                for (SoKhangTheArvTableForm f : entry.getValue()) {
                    if (f.getStatusOfTreatment().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) {
                        arvForm.setGeneral(f);
                        treatmentDate = getFirstdateMonth(StringUtils.isNotEmpty(f.getTreatmentTime()) ? f.getTreatmentTime() : "");
                        break;
                    }
                }
            }
            
            if (StringUtils.isEmpty(treatmentDate)) {
                continue;
            }
            
            for (SoKhangTheArvTableForm f : entry.getValue()) {
                if (StringUtils.isEmpty(treatmentDate)) {
                    continue;
                }
                labelFinalEnd = Integer.parseInt(getLabelKey(treatmentDate, f.getTreatmentTime()));
            }
            
            labelCurrentDate = Integer.parseInt(getLabelKey(treatmentDate, currentDate));
            treatmentDate2 = treatmentDate;
            treatmentOrigin = treatmentDate;
            
            // Add value month
            for (int i = 0; i < 61; i++) {
                mapChild.put(String.valueOf(i), new ArrayList<SoKhangTheArvTableForm>());
                
                if (i != 0) {
                    treatmentDate = getFirstdateMonth(addDate(treatmentDate, 1));
                }
                
                for (SoKhangTheArvTableForm fo : entry.getValue()) {
                    
                    // Set ngày thay, lý do, thời gian thay đổi phác đồ
                    child = new SoKhangTheArvTableForm();
                    
                    if (StringUtils.isNotEmpty(fo.getRegimenDate())) {
                        arvForm.setChangeRegimenIdDate(fo.getRegimenDate());
                    }
                    if (StringUtils.isNotEmpty(fo.getCausesChange())) {
                        arvForm.getGeneral().setCausesChange(fo.getCausesChange());
                    }
                    if (StringUtils.isNotEmpty(fo.getRegimenDate())) {
                        arvForm.getGeneral().setTreatmentRegimentIDChange(fo.getTreatmentRegimentID());
                    }
                    
                    // Chưa có ngày kết thúc, thay đổi phác đồ, ngày chuyển đi, ngày chuyển tới
                    if (StringUtils.isEmpty(fo.getEndTime()) && StringUtils.isEmpty(fo.getRegimenDate())
                            && StringUtils.isEmpty(fo.getTranferFromTime()) && StringUtils.isEmpty(fo.getTranferFromTime())
                            && (StringUtils.isEmpty(fo.getRegistrationType()) || !fo.getRegistrationType().equals("3"))
                            && StringUtils.isNotEmpty(fo.getTreatmentTime())
                            && getFirstdateMonth(fo.getTreatmentTime()).equals(treatmentDate) && 
                            i == Integer.parseInt(getLabelKey(treatmentOrigin, fo.getTreatmentTime()))) {
                        if (getLastSameDay(mapChild.get(String.valueOf(i)), fo, "treatment")) {
                            continue;
                        }
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, fo);
                        mapChild.get(String.valueOf(i)).add(child);
                        continue;
                    }
                    // Chưa có ngày kết thúc, thay đổi phác đồ, ngày chuyển đi, có ngày chuyển tới, ngày điều trị
                    if (StringUtils.isEmpty(fo.getEndTime()) && StringUtils.isEmpty(fo.getTranferFromTime()) && StringUtils.isNotEmpty(fo.getDateOfArrival()) && i == Integer.parseInt(getLabelKey(treatmentOrigin, fo.getTreatmentTime())) && StringUtils.isEmpty(fo.getRegimenDate())) {
                        if (getLastSameDay(mapChild.get(String.valueOf(i)), fo, "arrival")) {
                            continue;
                        }
                        
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, fo);
                        if (i != (Integer.parseInt(getLabelKey(treatmentOrigin, fo.getDateOfArrival())))) {
                            child.setLine("3");
                        }
                        mapChild.get(String.valueOf(i)).add(child);
                        continue;
                    }
                    // có ngày chuyển đi
                    if (StringUtils.isNotEmpty(fo.getTranferFromTime()) && i == Integer.parseInt(getLabelKey(treatmentOrigin, fo.getTranferFromTime()))) {
                        if (getLastSameDay(mapChild.get(String.valueOf(i)), fo, "tranferFromTime")) {
                            continue;
                        }
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, fo);
                        mapChild.get(String.valueOf(i)).add(child);
                        continue;
                    }
                    
                    // Có ngày kết thúc 
                    if (StringUtils.isNotEmpty(fo.getEndTime()) && getFirstdateMonth(fo.getEndTime()).equals(treatmentDate) && i == Integer.parseInt(getLabelKey(treatmentOrigin, fo.getEndTime()))) {
                        if (getLastSameDay(mapChild.get(String.valueOf(i)), fo, "endTime")) {
                            continue;
                        }
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, fo);
                        mapChild.get(String.valueOf(i)).add(child);
                        continue;
                    }
                    // Có ngày thay đổi phác đồ, chưa có ngày kết thúc
                    if ((StringUtils.isEmpty(fo.getEndTime()) && StringUtils.isNotEmpty(fo.getRegimenDate()) && getFirstdateMonth(fo.getRegimenDate()).equals(treatmentDate) 
                            && i == Integer.parseInt(getLabelKey(treatmentOrigin, fo.getRegimenDate())))) {
                        if (getLastSameDay(mapChild.get(String.valueOf(i)), fo, "regimentDate")) {
                            continue;
                        }
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, fo);
                        child.setLine("3");
                        child.setRegimentKey("yes");
                        
                        mapChild.get(String.valueOf(i)).add(child);
                    }
                }
            }
            
            // Add missing month
            treatmentDate2 = arvForm.getTreatmentTime();
            for (int i = 0; i < 61; i++) {
                if (i != 0) {
                    treatmentDate2 = getFirstdateMonth(addDate(treatmentDate2, 1));
                }
                
                if (!mapChild.get(String.valueOf(i)).isEmpty()) {
                    sixMonth = new SoKhangTheArvTableForm();
                    lastKnownArv = new SoKhangTheArvTableForm();
                    setTbl(lastKnownArv, mapChild.get(String.valueOf(i)).get(mapChild.get(String.valueOf(i)).size() - 1));
                    
                    if (24 < i && i < 60) {
                        if (i == 30 || i == 36 || i == 42 || i == 48 || i == 54) {
                            continue;
                        }
                        
                        if (StringUtils.isNotEmpty(lastKnownArv.getTranferFromTime()) || StringUtils.isNotEmpty(lastKnownArv.getEndTime()) || StringUtils.isNotEmpty(lastKnownArv.getTreatmentTime())) {
                            sixMonth = new SoKhangTheArvTableForm();
                            setTbl(sixMonth, lastKnownArv);
                        }
                    }
                    continue;
                }
                
                if ((i <= labelCurrentDate && labelCurrentDate < 24) || (i <= 6 * (Math.ceil((labelCurrentDate + 6) / 6)) && labelCurrentDate > 24)) {
                    lastKnownArv.setTreatment(treatmentDate2);
                    child = new SoKhangTheArvTableForm();
                    setTbl(child, lastKnownArv);
                    if (StringUtils.isEmpty(lastKnownArv.getEndTime()) && StringUtils.isEmpty(lastKnownArv.getTranferFromTime())) {
                        if (i <= 24) {
                            child.setLine("3");
                        }

                        if ((24 < labelFinalEnd && labelFinalEnd <= 30 ) || (30 < labelFinalEnd && labelFinalEnd <= 36) || (36 < labelFinalEnd && labelFinalEnd <= 42) 
                                 || (42 < labelFinalEnd && labelFinalEnd <= 48) || (48 < labelFinalEnd && labelFinalEnd <= 54)  || (54 < labelFinalEnd && labelFinalEnd <= 60)) {
                            
                            if ((i == 30 || i == 36 || i == 42 || i == 48 || i == 54 || i == 60) && !hasHideValue && StringUtils.isEmpty(lastEndTime)) {
                                if (!(i != 30 && ((StringUtils.isNotEmpty(lastKnownArv.getTreatmentTime()) && Integer.parseInt(getLabelKey(arvForm.getGeneral().getTreatmentTime(), lastKnownArv.getTreatmentTime())) > (i - 6))
                                        || (StringUtils.isNotEmpty(lastKnownArv.getDateOfArrival()) && Integer.parseInt(getLabelKey(arvForm.getGeneral().getTreatmentTime(), lastKnownArv.getDateOfArrival())) > (i - 6))))) {
                                    child.setLine("3");
                                }
                                mapChild.get(String.valueOf(i)).add(child);
                                hasHideValue = true;
                                continue;
                            }
                        }
                        
                        if (!(i != 30 &&((StringUtils.isNotEmpty(lastKnownArv.getTreatmentTime()) && Integer.parseInt(getLabelKey(arvForm.getGeneral().getTreatmentTime(), lastKnownArv.getTreatmentTime())) > (i - 6))
                                || (StringUtils.isNotEmpty(lastKnownArv.getDateOfArrival()) && Integer.parseInt(getLabelKey(arvForm.getGeneral().getTreatmentTime(), lastKnownArv.getDateOfArrival())) > (i - 6))))) {
                            child.setLine("3");
                        }
                        
                        mapChild.get(String.valueOf(i)).add(child);
                        continue;
                    }
                    
                    if ((i == 24 || i == 30 || i == 36 || i == 42 || i == 48 || i == 54 || i == 60)
                            && (StringUtils.isNotEmpty(lastKnownArv.getEndTime()) || StringUtils.isNotEmpty(lastKnownArv.getTranferFromTime())) ) {
                        if ( i > 24 && ((labelCurrentDate < 30)
                                || (labelCurrentDate < 36)
                                || (labelCurrentDate < 42) 
                                || (labelCurrentDate < 48)
                                || (labelCurrentDate < 54)
                                || (labelCurrentDate < 60)) && StringUtils.isNotEmpty(lastEndTime) &&
                                ((StringUtils.isNotEmpty(lastKnownArv.getEndTime()) && !lastEndTime.equals(lastKnownArv.getEndTime())) || 
                                ((StringUtils.isNotEmpty(lastKnownArv.getTranferFromTime()) && !lastEndTime.equals(lastKnownArv.getTranferFromTime()))))) {
                            break;
                        }
                        
                        if (mapChild.size() > 0 && ( (mapChild.get(String.valueOf(i - 6)) == null || mapChild.get(String.valueOf(i - 6)).isEmpty()) || ((mapChild.get(String.valueOf(i - 6)) != null && mapChild.get(String.valueOf(i - 6)).size() > 0) && 
                                (StringUtils.isNotEmpty(mapChild.get(String.valueOf(i - 6)).get(mapChild.get(String.valueOf(i - 6)).size() - 1).getEndTime())
                                || StringUtils.isNotEmpty(mapChild.get(String.valueOf(i - 6)).get(mapChild.get(String.valueOf(i - 6)).size() - 1).getTranferFromTime()))))) {
                            continue;
                        }
                        
                        child = new SoKhangTheArvTableForm();
                        setTbl(child, sixMonth);
                        
                        mapChild.get(String.valueOf(i)).add(child);
                        if((StringUtils.isNotEmpty(lastKnownArv.getEndTime()) && lastEndTime.equals(lastKnownArv.getEndTime())) || 
                                ((StringUtils.isNotEmpty(lastKnownArv.getTranferFromTime()) && lastEndTime.equals(lastKnownArv.getTranferFromTime())))) {
                            break;
                        }
                    }
                }
            }
            
            if (secondLine != null && !secondLine.isEmpty()) {
                for (Map.Entry<String, List<SoKhangTheArvTableForm>> ent : mapChild.entrySet()) {
                    for (SoKhangTheArvTableForm so : secondLine) {
                        temp = false;
                        if (StringUtils.isNotEmpty(so.getLaoStartTime()) && ent.getKey().equals(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getLaoStartTime()))) {
                            so.setLine("2");
                            so.setKeyLao(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getLaoStartTime()));
                            temp = true;
                        }
                        if (StringUtils.isNotEmpty(so.getInhStartTime()) && ent.getKey().equals(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getInhStartTime()))) {
                            so.setLine("2");
                            so.setKeyInh(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getInhStartTime()));
                            temp = true;
                        }
                        if ((StringUtils.isNotEmpty(so.getCotrimoxazoleStartTime()) && ent.getKey().equals(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getCotrimoxazoleStartTime())))) {
                            so.setLine("2");
                            so.setKeyCotri(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getCotrimoxazoleStartTime()));
                            temp = true;
                        }
                        if (StringUtils.isNotEmpty(so.getCd4TestTime()) && ent.getKey().equals(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getCd4TestTime()))) {
                            so.setLine("2");
                            so.setKeyCd4(getLabelKey(arvForm.getGeneral().getTreatmentTime(), so.getCd4TestTime()));
                            temp = true;
                        }
                        if (temp) {
                            ent.getValue().add(so);
                        }
                    }
                }
            }
            
            arvForm.setData(mapChild);
            resultForm.add(arvForm);
        }
        Collections.sort(resultForm);
        
        dataPage.setTotalRecords(resultForm.size());
        dataPage.setData(resultForm);
        return dataPage;
    }
    
    /**
     * Return day after add number of date
     * 
     * @param date
     * @param numDate
     * @return 
     */
    private String addDate(String date, int numDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(ddMMyyyy);
        Date parsedDate = sdf.parse(date);
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(parsedDate);

        try {
            //Setting the date to the given date
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
        }

        cal.add(Calendar.MONTH, numDate);
        String newDate = sdf.format(cal.getTime());
        
        return newDate;
    }
    
    /** 
     * Get first day of month
     * 
     * @param date
     * @return 
     */
    private String getFirstdateMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ddMMyyyy);
        Date parsedDate;
        try {
            parsedDate = sdf.parse(date);
        } catch (ParseException ex) {
            return null;
        }
        Date firstDay = TextUtils.getFirstDayOfMonth(parsedDate);
        return TextUtils.formatDate(firstDay, ddMMyyyy);
    }
    
    /**
     * So sánh ngày
     * 
     * @param dateSource
     * @param dateCompare
     * @return 
     */
    private Integer compareDateTo(String dateSource, String dateCompare) {
        
        if (StringUtils.isEmpty(dateSource) || StringUtils.isEmpty(dateCompare)) {
            return null;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(ddMMyyyy);
        try {
            Date source = sdf.parse(dateSource);
            Date target = sdf.parse(dateCompare);
            
            return source.compareTo(target);
        } catch (ParseException ex) {
            return null;
        }
    }
    
    /**
     * Lấy label tính từ ngày đầu tiên
     * 
     * @param fDate first date of arv
     * @param date
     * @return
     * @throws ParseException 
     */
    private String getLabelKey(String fDate, String date) throws ParseException {
        
        String maxDate = addDate(fDate, 69);
        if (compareDateTo(maxDate, date) < 0) {
            date = maxDate;
        }
        
        date = getFirstdateMonth(date);
        fDate = getFirstdateMonth(fDate);
        
        if (date.equals(fDate)) {
            return "0";
        }
        
        int i = 0;
        while (!fDate.equals(date) && i < 70) {
            i++;
            fDate = addDate(fDate, 1);
            if (fDate.equals(date)) {
                return String.valueOf(i);
            }
        }
        return String.valueOf(-1);
    }
    
    public void setTbl(SoKhangTheArvTableForm target, SoKhangTheArvTableForm obj) {
        target.setStt(obj.getStt());
        target.setCode(obj.getCode());
        target.setPatientID(obj.getPatientID());
        target.setFullname(obj.getFullname());
        target.setGenderID(obj.getGenderID());
        target.setDob(obj.getDob());
        target.setArvID(obj.getArvID());
        target.setStageID(obj.getStageID());
        target.setStatusOfTreatment(obj.getStatusOfTreatment());
        target.setTreatment(obj.getTreatment());
        target.setTreatmentTime(obj.getTreatmentTime());
        target.setEndCase(obj.getEndCase());
        target.setEndTime(obj.getEndTime());
        target.setRegistrationType(obj.getRegistrationType());
        target.setTranferFromTime(obj.getTranferFromTime());
        target.setTreatmentRegimentID(obj.getTreatmentRegimentID());
        target.setTreatmentRegimentIDChange(obj.getTreatmentRegimentIDChange());
        target.setExaminationTime(obj.getExaminationTime());
        target.setDateOfArrival(obj.getDateOfArrival());
        target.setClinicalStage(obj.getClinicalStage());
        target.setCd4(obj.getCd4());
        target.setWeight(obj.getWeight());
        target.setHeight(obj.getHeight());
        target.setRegimenDate(obj.getRegimenDate());
        target.setCausesChange(obj.getCausesChange());
        target.setTreatmentRegimenStage(obj.getTreatmentRegimenStage());
        target.setLine(obj.getLine());
    }
    
    /**
     * Lấy dữ liệu gàn nhất nếu cùng ngày
     * 
     * @param mapChildI
     * @param fo
     * @param type 
     */
    private boolean getLastSameDay(List<SoKhangTheArvTableForm> mapChildI, SoKhangTheArvTableForm fo, String type) {
        
        for (SoKhangTheArvTableForm form : mapChildI) {
            if (type.equals("treatment") && StringUtils.isNotEmpty(form.getTreatmentTime()) && compareDateTo(form.getTreatmentTime(), fo.getTreatmentTime()) == 0) {
                setTbl(form, fo);
                return true;
            }
            if (type.equals("tranferFromTime") && StringUtils.isNotEmpty(form.getTranferFromTime()) && compareDateTo(form.getTranferFromTime(), fo.getTranferFromTime()) == 0) {
                setTbl(form, fo);
                return true;
            }
            if (type.equals("endTime") && StringUtils.isNotEmpty(form.getEndTime()) && compareDateTo(form.getEndTime(), fo.getEndTime()) == 0) {
                setTbl(form, fo);
                return true;
            }
            if (type.equals("regimentDate") && StringUtils.isNotEmpty(form.getRegimenDate()) && compareDateTo(form.getRegimenDate(), fo.getRegimenDate()) == 0) {
                setTbl(form, fo);
                return true;
            }
            if (type.equals("arrival") && StringUtils.isNotEmpty(form.getDateOfArrival()) && compareDateTo(form.getDateOfArrival(), fo.getDateOfArrival()) == 0) {
                setTbl(form, fo);
                return true;
            }
        }
        return false;
    }
}
