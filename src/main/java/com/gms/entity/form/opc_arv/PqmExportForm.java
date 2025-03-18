package com.gms.entity.form.opc_arv;

import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Form BC pqm
 *
 * @author pdThang
 *
 */
public class PqmExportForm extends BaseForm {

    private String fileName;
    private String sheetName;
    private LinkedList<String> head;
    private LinkedList< LinkedList<String>> datas;

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
