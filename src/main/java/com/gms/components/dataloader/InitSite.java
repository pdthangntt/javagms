package com.gms.components.dataloader;

import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class InitSite {

    @Autowired
    private SiteService siteService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;

//    @PostConstruct
    public void loadData() {
//        initSitePAC();
    }

//    @PreDestroy
    public void removeData() {
    }

    public String getCodeByName(String name) {
        String code = "";
        String[] myName = name.trim().split(" ");
        for (int i = 0; i < myName.length; i++) {
            try {
                code += myName[i].charAt(0);
            } catch (Exception e) {
            }
        }
        return code;
    }

    /**
     * Khởi tạo user mặc định
     *
     * @return
     */
    private boolean initSitePAC() {
        List<SiteEntity> siteEntitys = siteService.findAll();
        Map<String, SiteEntity> mSiteName = new HashMap<>();
        Map<String, SiteEntity> mSiteDistrict = new HashMap<>();
        Map<String, SiteEntity> mSiteWard = new HashMap<>();
        for (SiteEntity model : siteEntitys) {
            if (model.getID().equals(Long.valueOf("1"))) {
                continue;
            }
//            siteService.remove(model);

            if (model.getType() == SiteEntity.TYPE_PROVINCE) {
                mSiteName.put(model.getProvinceID(), model);
            }
            if (model.getType() == SiteEntity.TYPE_DISTRICT) {
                mSiteDistrict.put(model.getDistrictID(), model);
            }
            if (model.getType() == SiteEntity.TYPE_WARD) {
                mSiteWard.put(model.getWardID(), model);
            }
        }
//        if (1 == 1) {
//            return false;
//        }

        List<ProvinceEntity> entitys = locationsService.findAllProvince();
        for (ProvinceEntity province : entitys) {
            SiteEntity site = mSiteName.getOrDefault(province.getID(), null);
            if (site == null) {
                site = new SiteEntity();
                site.setType(SiteEntity.TYPE_PROVINCE);
            }
            site.setCode(getCodeByName(province.getName().replaceAll(province.getType(), "")) + province.getID());
            site.setName("Trung tâm phòng chống HIV/AIDS " + province.getName());
            site.setShortName("PAC " + province.getName());
            site.setProvinceID(province.getID());
            site.setDistrictID("001");
            site.setWardID("001");

            site.setIsActive(province.getName().contains("Quảng Ninh")
                    || province.getName().contains("Tây Ninh")
                    || province.getName().contains("Tiền Giang")
                    || province.getName().contains("Đồng Nai"));

            site.setServices(null); //parameterService.getServices());
            site.setParentID(Long.valueOf("1"));

            site = siteService.save(site);
            if (site == null) {
                System.out.println("Not save site province " + province.getName());
                continue;
            }

//            if (province.getName().contains("Quảng Ninh")
//                    || province.getName().contains("Tây Ninh")
//                    || province.getName().contains("Tiền Giang")
//                    || province.getName().contains("Đồng Nai")) {
            List<DistrictEntity> districts = locationsService.findDistrictByProvinceID(province.getID());
            for (DistrictEntity district : districts) {

                SiteEntity siteTTYT = mSiteDistrict.getOrDefault(district.getID(), null);
                if (siteTTYT == null) {
                    siteTTYT = new SiteEntity();
                    siteTTYT.setType(SiteEntity.TYPE_DISTRICT);
                }
                siteTTYT.setCode(getCodeByName(district.getName().replaceAll(district.getType(), "")) + district.getID());
                siteTTYT.setName("Trung tâm Y tế " + district.getName());
                siteTTYT.setShortName("TTYT " + district.getName());
                siteTTYT.setProvinceID(province.getID());
                siteTTYT.setDistrictID(district.getID());
                siteTTYT.setWardID("0");
                siteTTYT.setIsActive(site.isIsActive());
                siteTTYT.setServices(null); //parameterService.getServices());
                siteTTYT.setParentID(site.getID());
                siteTTYT = siteService.save(siteTTYT);

                if (siteTTYT == null) {
                    System.out.println("Not save site district " + district.getName() + ", " + province.getName());
                    continue;
                }

                List<WardEntity> wards = locationsService.findWardByDistrictID(district.getID());
                for (WardEntity ward : wards) {

                    SiteEntity siteTYT = mSiteWard.getOrDefault(ward.getID(), null);
                    if (siteTYT == null) {
                        siteTYT = new SiteEntity();
                        siteTYT.setType(SiteEntity.TYPE_WARD);
                    }
                    siteTYT.setCode(getCodeByName(ward.getName().replaceAll(ward.getType(), "")) + ward.getID());
                    siteTYT.setName("Trạm Y tế " + ward.getName());
                    siteTYT.setShortName("TYT " + ward.getName());
                    siteTYT.setProvinceID(province.getID());
                    siteTYT.setDistrictID(district.getID());
                    siteTYT.setWardID(ward.getID());
                    siteTYT.setIsActive(site.isIsActive());
                    siteTYT.setServices(null); //parameterService.getServices());
                    siteTYT.setParentID(siteTTYT.getID());
                    siteService.save(siteTYT);
                }

            }

        }
//        }
        return true;
    }

}
