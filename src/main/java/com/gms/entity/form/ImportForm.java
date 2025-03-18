package com.gms.entity.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
public class ImportForm implements Serializable {

    private String title;
    private String smallTitle;
    private String smallUrl;
    private String uploadUrl;
    private String readUrl;
    private String validateUrl;
    private String saveUrl;
    private String template;
    private String fileName;
    private String filePath;

    //read url
    private Map<Integer, List<String>> data;
    private HashMap<String, String> cols;
    private String mapCols;

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMapCols() {
        return mapCols;
    }

    public void setMapCols(String mapCols) {
        this.mapCols = mapCols;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<Integer, List<String>> getData() {
        return data;
    }

    public void setData(Map<Integer, List<String>> data) {
        this.data = data;
    }

    public HashMap<String, String> getCols() {
        return cols;
    }

    public void setCols(HashMap<String, String> cols) {
        this.cols = cols;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallTitle() {
        return smallTitle;
    }

    public void setSmallTitle(String smallTitle) {
        this.smallTitle = smallTitle;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getReadUrl() {
        return readUrl;
    }

    public void setReadUrl(String readUrl) {
        this.readUrl = readUrl;
    }

    public String getValidateUrl() {
        return validateUrl;
    }

    public void setValidateUrl(String validateUrl) {
        this.validateUrl = validateUrl;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
