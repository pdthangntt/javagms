package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
public class TT03Form extends BaseForm {

    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private String startDate;
    private String endDate;
    private String tab;
    private boolean opc;
    private boolean opcManager;
    private Map<Long, TT03Table02> table02;
    private Map<Long, TT03Table03> table03;
    private Map<Long, TT03Table06> table06;
    private Map<Long, TT03Table04> table04;
    private Map<Long, Map<String, TT03Table05>> table05;

    public TT03Table02 getSumTable02() {
        TT03Table02 table = new TT03Table02();
        table.setArv(0);
        table.setLao(0);

        for (Map.Entry<Long, TT03Table02> entry : table02.entrySet()) {
            TT03Table02 item = entry.getValue();
            table.setLao(table.getLao() + item.getLao());
            table.setArv(table.getArv() + item.getArv());
        }
        return table;
    }

    public TT03Table03 getSumTable03() {
        TT03Table03 table = new TT03Table03();
        table.setTlvr(0);
        table.setTlvr1000(0);
        table.setMonth(0);
        table.setMonth1000(0);

        for (Map.Entry<Long, TT03Table03> entry : table03.entrySet()) {
            TT03Table03 item = entry.getValue();
            table.setTlvr(table.getTlvr() + item.getTlvr());
            table.setTlvr1000(table.getTlvr1000() + item.getTlvr1000());
            table.setMonth(table.getMonth() + item.getMonth());
            table.setMonth1000(table.getMonth1000() + item.getMonth1000());
        }
        return table;
    }

    public TT03Table06 getSumTable06() {
        TT03Table06 table = new TT03Table06();
        table.setR11(0);
        table.setR12(0);
        table.setR13(0);
        table.setR21(0);
        table.setR22(0);
        table.setR23(0);

        for (Map.Entry<Long, TT03Table06> entry : table06.entrySet()) {
            TT03Table06 item = entry.getValue();
            table.setR11(table.getR11() + item.getR11());
            table.setR12(table.getR12() + item.getR12());
            table.setR13(table.getR13() + item.getR13());
            table.setR21(table.getR21() + item.getR21());
            table.setR22(table.getR22() + item.getR22());
            table.setR23(table.getR23() + item.getR23());
        }
        return table;
    }

    public TT03Table04 getSumTable04() {
        TT03Table04 table = new TT03Table04();
        table.setInh(0);
        table.setRegister(0);
        table.setTotal(0);

        for (Map.Entry<Long, TT03Table04> entry : table04.entrySet()) {
            TT03Table04 item = entry.getValue();
            table.setInh(table.getInh() + item.getInh());
            table.setRegister(table.getRegister() + item.getRegister());
            table.setTotal(table.getTotal() + item.getTotal());
        }
        return table;
    }

    public Map<String, TT03Table05> getSumTable05() {
        Map<String, TT03Table05> table = new HashMap<>();
        table.put("1", new TT03Table05());
        table.put("2", new TT03Table05());
        table.put("total", new TT03Table05());

        for (Map.Entry<Long, Map<String, TT03Table05>> entry : table05.entrySet()) {
            Map<String, TT03Table05> map = entry.getValue();

            String key = "total";
            TT03Table05 item = map.getOrDefault(key, new TT03Table05());
            table.get(key).setArv(table.get(key).getArv() + item.getArv());
            table.get(key).setBackTreatment(table.get(key).getBackTreatment() + item.getBackTreatment());
            table.get(key).setDead(table.get(key).getDead() + item.getDead());
            table.get(key).setFirstRegister(table.get(key).getFirstRegister() + item.getFirstRegister());
            table.get(key).setMoveAway(table.get(key).getMoveAway() + item.getMoveAway());
            table.get(key).setPreArv(table.get(key).getPreArv() + item.getPreArv());
            table.get(key).setQuitTreatment(table.get(key).getQuitTreatment() + item.getQuitTreatment());
            table.get(key).setTransferTo(table.get(key).getTransferTo() + item.getTransferTo());
            table.get(key).setWard(table.get(key).getWard() + item.getWard());

            key = "1";
            item = map.getOrDefault(key, new TT03Table05());
            table.get(key).setArv(table.get(key).getArv() + item.getArv());
            table.get(key).setBackTreatment(table.get(key).getBackTreatment() + item.getBackTreatment());
            table.get(key).setDead(table.get(key).getDead() + item.getDead());
            table.get(key).setFirstRegister(table.get(key).getFirstRegister() + item.getFirstRegister());
            table.get(key).setMoveAway(table.get(key).getMoveAway() + item.getMoveAway());
            table.get(key).setPreArv(table.get(key).getPreArv() + item.getPreArv());
            table.get(key).setQuitTreatment(table.get(key).getQuitTreatment() + item.getQuitTreatment());
            table.get(key).setTransferTo(table.get(key).getTransferTo() + item.getTransferTo());
            table.get(key).setWard(table.get(key).getWard() + item.getWard());

            key = "2";
            item = map.getOrDefault(key, new TT03Table05());
            table.get(key).setArv(table.get(key).getArv() + item.getArv());
            table.get(key).setBackTreatment(table.get(key).getBackTreatment() + item.getBackTreatment());
            table.get(key).setDead(table.get(key).getDead() + item.getDead());
            table.get(key).setFirstRegister(table.get(key).getFirstRegister() + item.getFirstRegister());
            table.get(key).setMoveAway(table.get(key).getMoveAway() + item.getMoveAway());
            table.get(key).setPreArv(table.get(key).getPreArv() + item.getPreArv());
            table.get(key).setQuitTreatment(table.get(key).getQuitTreatment() + item.getQuitTreatment());
            table.get(key).setTransferTo(table.get(key).getTransferTo() + item.getTransferTo());
            table.get(key).setWard(table.get(key).getWard() + item.getWard());
        }
        return table;
    }

    public boolean isOpc() {
        return opc;
    }

    public void setOpc(boolean opc) {
        this.opc = opc;
    }

    public boolean isOpcManager() {
        return opcManager;
    }

    public void setOpcManager(boolean opcManager) {
        this.opcManager = opcManager;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<Long, TT03Table02> getTable02() {
        return table02;
    }

    public void setTable02(Map<Long, TT03Table02> table02) {
        this.table02 = table02;
    }

    public Map<Long, TT03Table03> getTable03() {
        return table03;
    }

    public void setTable03(Map<Long, TT03Table03> table03) {
        this.table03 = table03;
    }

    public Map<Long, TT03Table06> getTable06() {
        return table06;
    }

    public void setTable06(Map<Long, TT03Table06> table06) {
        this.table06 = table06;
    }

    public Map<Long, TT03Table04> getTable04() {
        return table04;
    }

    public void setTable04(Map<Long, TT03Table04> table04) {
        this.table04 = table04;
    }

    public Map<Long, Map<String, TT03Table05>> getTable05() {
        return table05;
    }

    public void setTable05(Map<Long, Map<String, TT03Table05>> table05) {
        this.table05 = table05;
    }

}
