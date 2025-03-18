package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.OpcParameterAttributeEnum;
import com.gms.entity.constant.OpcParameterTypeEnum;
import com.gms.entity.db.OpcParameterEntity;
import com.gms.entity.form.opc_arv.OpcBookViralLoadTable2Form;
import com.gms.entity.form.opc_arv.OpcBookViralLoadTableForm;
import com.gms.entity.form.opc_arv.OpcViralBookImportForm;
import com.gms.repository.OpcParameterRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class OpcBookViralRepositoryImpl extends BaseRepositoryImpl {

    @Autowired
    private OpcParameterRepository opcParameterRepository;
    
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    /**
     * Lấy dữ liệu bảng 02
     * 
     * @param siteID
     * @param keywords
     * @param fromDate
     * @param toDate
     * @return 
     */
    public OpcBookViralLoadTable2Form getBookViralLoadTable02(Long siteID, String keywords, String fromDate, String toDate) throws ParseException {
        
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfrmt1 = new SimpleDateFormat("dd/MM/yyyy");
        
        Date fromDateConvert = sdfrmt1.parse(fromDate);
        Date toDateConvert = sdfrmt1.parse(toDate);
        
        OpcBookViralLoadTable2Form table = new OpcBookViralLoadTable2Form();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("keywords", StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        params.put("from_date", sdfrmt.format(fromDateConvert));
        params.put("to_date",  sdfrmt.format(toDateConvert));

        List<Object[]> result = query("opc/book_viralload_tbl2.sql", params).getResultList();
        Object[] object = result.get(0);

        table.setXnTrongThang(Integer.valueOf(object[0].toString()));
        table.setArvBac2Duoi1000(Integer.valueOf(object[1].toString()));
        table.setArvBac2Tren1000(Integer.valueOf(object[2].toString()));

        return table;
    }

    /**
     * Lấy thông tin sổ TLVR
     * 
     * @throws java.text.ParseException
     * @auth TrangBN
     * 
     * @param siteID
     * @param keywords
     * @param fromDate
     * @param toDate
     * @param page
     * @param size
     * @return 
     */
    public DataPage<OpcBookViralLoadTableForm> getBookViralLoadTable(Long siteID, String keywords, String fromDate, String toDate, int page, int size) throws ParseException {
        
        SimpleDateFormat sdfrmt1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDateConvert = sdfrmt1.parse(fromDate);
        Date toDateConvert = sdfrmt1.parse(toDate);

        DataPage<OpcBookViralLoadTableForm> dataPage = new DataPage<>();
        dataPage.setCurrentPage(page);
        dataPage.setMaxResult(size);
        List<OpcBookViralLoadTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> paramsTotal = new HashMap<>();
        params.put("site_id", siteID);
        params.put("keywords", StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        params.put("from_date", sdfrmt.format(fromDateConvert));
        params.put("to_date", sdfrmt.format(toDateConvert));
        params.put("size", size);
        params.put("offsetNumber", (page - 1) * size);
        paramsTotal.put("site_id", siteID);
        paramsTotal.put("keywords", StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        paramsTotal.put("from_date", sdfrmt.format(fromDateConvert));
        paramsTotal.put("to_date",  sdfrmt.format(toDateConvert));
        
        int totalRecords = 0;
        Object single = query("opc/book_viralload_total.sql", paramsTotal).getSingleResult();
        if (single != null) {
            totalRecords = Integer.valueOf(single.toString());
        }
        
        List<Object[]> result = query("opc/book_viralload.sql", params).getResultList();
        if (result == null || result.isEmpty()) {
            return dataPage;
        }

        try {
            for (Object[] object : result) {
                if (object[0] == null || object[0].toString().equals("")) {
                    continue;
                }
                OpcBookViralLoadTableForm item = new OpcBookViralLoadTableForm();
                item.setCode(object[0] == null ? null : object[0].toString());
                item.setInsuranceNo(object[1] == null ? null : object[1].toString());
                item.setPatientPhone(object[2] == null ? null : object[2].toString());
                item.setFullName(object[3] == null ? null : object[3].toString());
                item.setDob(object[4] == null ? null : object[4].toString());
                item.setGenderID(object[5] == null ? null : object[5].toString());
                item.setIdentityCard(object[6] == null ? null : object[6].toString());
                item.setTestTime(object[7] == null ? null : sdfrmt.parse(object[7].toString().substring(0,10)));
                item.setViralloadID(object[8] == null ? null : Long.valueOf(object[8].toString()));
                item.setSampleTime(object[9] == null ? null : sdfrmt.parse(object[9].toString().substring(0,10)));
                item.setResultTime(object[10] == null ? null : sdfrmt.parse(object[10].toString().substring(0,10)));
                item.setTreatmentTime(object[11] == null ? null : sdfrmt.parse(object[11].toString().substring(0,10)));
                item.setStatusOfTreatmentID(object[12] == null ? null : object[12].toString());
                item.setTreatmentRegimentStage(object[13] == null ? null : object[13].toString());
                item.setResultKq1(object[14] == null ? null : object[14].toString());
                item.setResultKq2(object[15] == null ? null : object[15].toString());
                item.setFirstTestTime(object[16] == null ? null : sdfrmt.parse(object[16].toString().substring(0,10)));
                item.setVisitID(object[17] == null ? null : object[17].toString());
                item.setRegimenDate(object[18] == null ? null : sdfrmt.parse(object[18].toString().substring(0,10)));
                item.setResult(object[19] == null ? null : object[19].toString());
                item.setIdKq1(object[20] == null ? null : Long.valueOf(object[20].toString()));
                item.setIdKq2(object[21] == null ? null : Long.valueOf(object[21].toString()));
                data.add(item);
            }
        } catch (ParseException e) {
        }
        
        // Set thông tin lý do xét nghiệm
        getParameters(data);
        
        dataPage.setTotalPages((int) Math.ceil(Double.valueOf(totalRecords) / size));
        dataPage.setTotalRecords(totalRecords);
        dataPage.setData(data);
        return dataPage;
    }
    
    /**
     * Get for table 02 for some fields
     * 
     * @param siteID
     * @param keywords
     * @param fromDate
     * @param toDate
     * @return
     * @throws ParseException 
     */
    public DataPage<OpcBookViralLoadTableForm> getBookViralLoadTable2(Long siteID, String keywords, String fromDate, String toDate) throws ParseException {
        
        SimpleDateFormat sdfrmt1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDateConvert = sdfrmt1.parse(fromDate);
        Date toDateConvert = sdfrmt1.parse(toDate);

        DataPage<OpcBookViralLoadTableForm> dataPage = new DataPage<>();
        List<OpcBookViralLoadTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("keywords", StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        params.put("from_date", sdfrmt.format(fromDateConvert));
        params.put("to_date", sdfrmt.format(toDateConvert));
        
        List<Object[]> result = query("opc/book_viralload_2_total.sql", params).getResultList();
        if (result == null || result.isEmpty()) {
            return dataPage;
        }

        try {
            for (Object[] object : result) {
                if (object[0] == null || object[0].toString().equals("")) {
                    continue;
                }
                OpcBookViralLoadTableForm item = new OpcBookViralLoadTableForm();
                item.setCode(object[0] == null ? null : object[0].toString());
                item.setInsuranceNo(object[1] == null ? null : object[1].toString());
                item.setPatientPhone(object[2] == null ? null : object[2].toString());
                item.setFullName(object[3] == null ? null : object[3].toString());
                item.setDob(object[4] == null ? null : object[4].toString());
                item.setGenderID(object[5] == null ? null : object[5].toString());
                item.setIdentityCard(object[6] == null ? null : object[6].toString());
                item.setTestTime(object[7] == null ? null : sdfrmt.parse(object[7].toString().substring(0,10)));
                item.setViralloadID(object[8] == null ? null : Long.valueOf(object[8].toString()));
                item.setSampleTime(object[9] == null ? null : sdfrmt.parse(object[9].toString().substring(0,10)));
                item.setResultTime(object[10] == null ? null : sdfrmt.parse(object[10].toString().substring(0,10)));
                item.setTreatmentTime(object[11] == null ? null : sdfrmt.parse(object[11].toString().substring(0,10)));
                item.setStatusOfTreatmentID(object[12] == null ? null : object[12].toString());
                item.setTreatmentRegimentStage(object[13] == null ? null : object[13].toString());
                item.setResultKq1(object[14] == null ? null : object[14].toString());
                item.setResultKq2(object[15] == null ? null : object[15].toString());
                item.setFirstTestTime(object[16] == null ? null : sdfrmt.parse(object[16].toString().substring(0,10)));
                item.setVisitID(object[17] == null ? null : object[17].toString());
                item.setRegimenDate(object[18] == null ? null : sdfrmt.parse(object[18].toString().substring(0,10)));
                item.setResult(object[19] == null ? null : object[19].toString());
                item.setIdKq1(object[20] == null ? null : Long.valueOf(object[20].toString()));
                item.setIdKq2(object[21] == null ? null : Long.valueOf(object[21].toString()));
                data.add(item);
            }
        } catch (ParseException e) {
        }
        
        // Set thông tin lý do xét nghiệm
        getParameters(data);
        dataPage.setData(data);
        return dataPage;
    }
    
     /**
     * Tham số
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<OpcBookViralLoadTableForm> getParameters(List<OpcBookViralLoadTableForm> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<Long> ids = new HashSet<>(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getViralloadID")));

        List<OpcParameterEntity> parameters = opcParameterRepository.findByTypeAndObjectID(OpcParameterTypeEnum.VIRAL_LOAD.getKey(), ids);
        Map<Long, Map<String, List<String>>> mParameters = new HashMap<>();
        for (OpcParameterEntity parameter : parameters) {
            if (mParameters.get(parameter.getObjectID()) == null) {
                mParameters.put(parameter.getObjectID(), new HashMap<String, List<String>>());
            }
            if (mParameters.get(parameter.getObjectID()).get(parameter.getAttrName()) == null) {
                mParameters.get(parameter.getObjectID()).put(parameter.getAttrName(), new ArrayList<String>());
            }
            mParameters.get(parameter.getObjectID()).get(parameter.getAttrName()).add(parameter.getParameterID());
        }
        for (OpcBookViralLoadTableForm entity : entities) {
            Map<String, List<String>> mAttrs = mParameters.getOrDefault(entity.getViralloadID(), null);
            if (mAttrs == null) {
                continue;
            }
            if (mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null) != null) {
                entity.setCauses(mAttrs.getOrDefault(OpcParameterAttributeEnum.VIRAL_LOAD_CAUSE.getKey(), null));
            }
        }
        return entities;
    }
    
    /**
     * Lấy thông tin TLVR dùng thêm mới update từ sổ TLVR
     * 
     * @auth TrangBN
     * @param code
     * @param siteID
     * @return 
     */
    public List<OpcViralBookImportForm> getViralBook(String code, Long siteID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("code", code);
        List<OpcViralBookImportForm> viral = new ArrayList<>();
        OpcViralBookImportForm form = new OpcViralBookImportForm();
        List<Object[]> result = query("opc/viralload_book_import.sql", params).getResultList();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        
        for (Object[] ob : result) {
            form = new OpcViralBookImportForm();
            form.setArvID(ob[0] != null ? Long.valueOf(ob[0].toString()) : null);
            form.setViralID(ob[1] != null ? Long.valueOf(ob[1].toString()) : null);
            form.setTestTime(TextUtils.formatDate(FORMATDATETIME, DATE_FORMAT, String.valueOf(ob[2])));
            form.setStageID(ob[3] != null ? Long.valueOf(ob[3].toString()) : null);
            form.setCode(ob[4] != null ?  ob[4].toString() : null);
            viral.add(form);
        }
        return viral;
    }
}
