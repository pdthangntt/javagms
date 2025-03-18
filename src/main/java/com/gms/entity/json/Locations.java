package com.gms.entity.json;

import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class Locations implements Serializable {

    private List<ProvinceEntity> province;
    private List<DistrictEntity> district;
    private List<WardEntity> ward;

    public List<ProvinceEntity> getProvince() {
        return province;
    }

    public void setProvince(List<ProvinceEntity> province) {
        this.province = province;
    }

    public List<DistrictEntity> getDistrict() {
        return district;
    }

    public void setDistrict(List<DistrictEntity> district) {
        this.district = district;
    }

    public List<WardEntity> getWard() {
        return ward;
    }

    public void setWard(List<WardEntity> ward) {
        this.ward = ward;
    }

}
