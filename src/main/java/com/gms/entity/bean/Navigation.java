package com.gms.entity.bean;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class Navigation {

    private String name;
    private String url;
    private String icon;
    private boolean line;
    private List<Navigation> childs;

    public Navigation(String name, String url, String icon) {
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.childs = new LinkedList<>();
    }

    public Navigation(String name, String url, String icon, boolean line) {
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.line = line;
        this.childs = new LinkedList<>();
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Navigation> getChilds() {
        return childs;
    }

    public void setChilds(List<Navigation> childs) {
        this.childs = childs;
    }

}
