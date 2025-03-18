package com.gms.entity.json.hivinfo.patient;

import java.io.Serializable;

/**
 * Lưu thông tin mẫu giám sát phát hiện tử vong
 *
 */
public class GsphTuVong implements Serializable {

    private String code;
    private String deathDate; // Ngày tử vong
    private Integer maNoiDangKy; // Mã nơi đăng ký

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public Integer getMaNoiDangKy() {
        return maNoiDangKy;
    }

    public void setMaNoiDangKy(Integer maNoiDangKy) {
        this.maNoiDangKy = maNoiDangKy;
    }

}
