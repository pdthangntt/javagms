package com.gms.components.dataloader;

import com.gms.components.PasswordUtils;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.SitePathEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.repository.SitePathRepository;
import com.gms.repository.SiteRepository;
import com.gms.repository.StaffRepository;
import com.gms.service.ParameterService;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
//@Component
public class InitData {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SitePathRepository sitePathRepository;
    @Autowired
    private ParameterService parameterService;

//    @PostConstruct
    public void loadData() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Saigon"));
//        initSite();
        initStaff();
        parameterConfig();
    }

    @PreDestroy
    public void removeData() {
    }

    /**
     * Khởi tạo user mặc định
     *
     * @return
     */
    private boolean initStaff() {
        StaffEntity staff = staffRepository.getByUsername("vvthanh");
        if (staff != null) {
            return false;
        }
        staff = new StaffEntity();
        staff.setEmail("vvthanh@gimasys.com");
        staff.setIsActive(true);
        staff.setName("Vũ Văn Thành");
        staff.setPhone("0367883573");
        staff.setPwd(PasswordUtils.encrytePassword("123123"));
        staff.setSiteID(new Long("1"));
        staff.setUsername("vvthanh");
        staff.setCreatedBY(Long.parseLong("0"));
        staff.setUpdatedBY((Long.parseLong("0")));
        staffRepository.save(staff);
        System.out.println("-------- Save Admin user");
        return true;
    }

    /**
     * Khởi tạo cơ sở mặc định
     *
     * @return
     */
    private boolean initSite() {
        SiteEntity site = siteRepository.findByCode("vaac");
        if (site != null) {
            return false;
        }

        site = new SiteEntity();
        site.setName("Trung tâm phòng chống HIV/AIDS");
        site.setShortName("TTPC HIV/AIDS");
        site.setCode("vaac");
        site.setType(SiteEntity.TYPE_VAAC);
        site.setCreatedBY(Long.parseLong("0"));
        site.setUpdatedBY(Long.parseLong("0"));
        site.setParentID(Long.parseLong("0"));
        site.setDistrictID("001");
        site.setProvinceID("001");
        site.setWardID("001");
        site.setIsActive(true);
        site = siteRepository.save(site);

        List<Long> sitePaths = sitePathRepository.findBySiteID(site.getID());
        if (sitePaths == null || 0 == sitePaths.size()) {
            SitePathEntity sitePath = new SitePathEntity();
            sitePath.setAncestorId(site.getID());
            sitePath.setCreateAT(new Date());
            sitePath.setSiteID(site.getID());
            sitePathRepository.save(sitePath);
        }

        return true;
    }

    /**
     * Cấu hình mặc định cho tham số
     */
    private void parameterConfig() {

    }
}
