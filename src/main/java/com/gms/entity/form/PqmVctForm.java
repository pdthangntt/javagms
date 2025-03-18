package com.gms.entity.form;

import com.gms.components.TextUtils;
import com.gms.entity.db.PqmVctEntity;
import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * @author pdThang
 */
public class PqmVctForm implements Serializable {

    private String ID; // ID
    private String earlyHivDate; // Ngày xn moi nhiem
    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới
    private String registerTherapyTime; // Ngày đăng ký điều trị
    private String exchangeTime;//Ngày chguyeenr gui
    private String registeredTherapySite; // Tên cơ sở mà khách hành đã đăng ký điều trị
    private String therapyNo; // Mã số điều trị
    private String blocked;
    private String confirmTestNo;
    private String identityCard;
    private String name;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public String getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(String registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    /**
     * Chuyển dữ liệu từ form sang entity
     *
     * @author pdThang
     * @param vct
     * @return
     * @throws ParseException
     */
    public PqmVctEntity getEntity(PqmVctEntity vct) throws Exception {
        vct.setID(Long.valueOf(getID()));
        vct.setEarlyHivDate(earlyHivDate == null || earlyHivDate.equals("") ? null : TextUtils.convertDate(earlyHivDate, "dd/MM/yyyy"));
        vct.setEarlyDiagnose(getEarlyDiagnose());
        vct.setRegisterTherapyTime(registerTherapyTime == null || registerTherapyTime.equals("") ? null : TextUtils.convertDate(registerTherapyTime, "dd/MM/yyyy"));
        vct.setExchangeTime(exchangeTime == null || exchangeTime.equals("") ? null : TextUtils.convertDate(exchangeTime, "dd/MM/yyyy"));
        vct.setRegisteredTherapySite(getRegisteredTherapySite());
        vct.setTherapyNo(getTherapyNo());
        vct.setBlocked(Integer.valueOf(getBlocked()));
        vct.setPatientName(name);
        vct.setConfirmTestNo(confirmTestNo);
        vct.setNote(note);
        return vct;
    }

}
