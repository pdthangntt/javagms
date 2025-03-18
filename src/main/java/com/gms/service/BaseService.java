package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author vvthanh
 */
public abstract class BaseService {

    protected static final String FORMATDATETIME = "yyyy-MM-dd hh:mm:ss";
    protected static final String FORMATDATE = "yyyy-MM-dd";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Logger
     *
     * @return
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Lấy tài khoản đang đăng nhập trên hệ thống
     *
     * @return LoggedUser || null
     */
    protected LoggedUser getCurrentUser() {
        try {
            return (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.info("User is gest. {}", e.getMessage());
        }
        return null;
    }

    protected Long getCurrentUserID() {
        try {
            LoggedUser currentUser = getCurrentUser();
            return currentUser == null ? new Long("0") : currentUser.getUser().getID();
        } catch (Exception e) {
            logger.debug("User is gest. {}", e.getMessage());
        }
        return new Long("0");
    }

    /**
     * Convert địa chỉ full
     *
     * @param address
     * @param province
     * @param district
     * @param ward
     * @return
     */
    protected String buildAddress(String address, ProvinceEntity province, DistrictEntity district, WardEntity ward) {
        address = address == null ? "" : address;
        String key = TextUtils.removemarks(address);

        if (key.contains(TextUtils.removemarks("Việt Nam")) || key.contains(TextUtils.removemarks("ViệtNam"))) {
            return address;
        }

        if (ward != null ) {
//            String wardName = TextUtils.removemarks(ward.getName()).replaceAll("phuong-", "").replaceAll("phuong", "").trim();
//             && !key.contains(wardName)
            address += (StringUtils.isEmpty(address) ? "" : ", ") + ward.getName();
        }

        if (district != null && !key.contains(TextUtils.removemarks(district.getName()).replaceAll("huyen-", "").replaceAll("huyen", "").replaceAll("quan-", "").replaceAll("quan", "").replaceAll("thanh-pho-", "").replaceAll("thanh-pho", "").trim()) || (district != null && district.getName() != null && district.getName().equals("Quận 6"))) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + district.getName();
        }

        if (province != null && !key.contains(TextUtils.removemarks(province.getName()).replaceAll("tinh-", "").replaceAll("tinh", "").replaceAll("thanh-pho-", "").replaceAll("thanh-pho", "").trim())) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + province.getName();
        }
        return address;
    }
    
    /**
     * Convert địa chỉ full
     *
     * @auth vvThành
     * @param no
     * @param street
     * @param group
     * @param province
     * @param district
     * @param ward
     * @return
     */
    public String buildAddress(String no, String street, String group, ProvinceEntity province, DistrictEntity district, WardEntity ward) {
        String address = StringUtils.isEmpty(no) ? "" : no;
        if (street != null && !"".equals(street)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + street;
        }
        if (group != null && !"".equals(group)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + group;
        }
        return buildAddress(address, province, district, ward);
    }

}
