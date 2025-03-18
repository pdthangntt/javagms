package com.gms.entity.form.opc_arv;

import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Form BC pqm
 *
 * @author pdThang
 *
 */
public class PqmExportAllForm extends BaseForm {

    private String title;
    private String fileName;
    private String sheetName;
    private LinkedList<String> head;
    private LinkedHashMap<String, String> filter;
    private LinkedList< LinkedList<String>> datas;

    public LinkedHashMap<String, String> getFilter() {
        return filter;
    }

    public void setFilter(LinkedHashMap<String, String> filter) {
        this.filter = filter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LinkedList<String> getHead() {
        return head;
    }

    public void setHead(LinkedList<String> head) {
        this.head = head;
    }

    public LinkedList<LinkedList<String>> getDatas() {
        return datas;
    }

    public void setDatas(LinkedList<LinkedList<String>> datas) {
        this.datas = datas;
    }

}
