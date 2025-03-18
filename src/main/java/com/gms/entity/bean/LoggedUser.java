package com.gms.entity.bean;

import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.db.WardEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author vvthanh
 */
public class LoggedUser implements UserDetails {

    private StaffEntity user;
    private SiteEntity site;
    private List<String> roles;
    private HashMap<String, String> config;
    private HashMap<String, String> provinceConfig;
    private ProvinceEntity siteProvince;
    private DistrictEntity siteDistrict;
    private WardEntity siteWard;

    public HashMap<String, String> getProvinceConfig() {
        return provinceConfig;
    }

    public void setProvinceConfig(HashMap<String, String> provinceConfig) {
        this.provinceConfig = provinceConfig;
    }

    public ProvinceEntity getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(ProvinceEntity siteProvince) {
        this.siteProvince = siteProvince;
    }

    public DistrictEntity getSiteDistrict() {
        return siteDistrict;
    }

    public void setSiteDistrict(DistrictEntity siteDistrict) {
        this.siteDistrict = siteDistrict;
    }

    public WardEntity getSiteWard() {
        return siteWard;
    }

    public void setSiteWard(WardEntity siteWard) {
        this.siteWard = siteWard;
    }

    public HashMap<String, String> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }

    public void setSite(SiteEntity site) {
        this.site = site;
    }

    public LoggedUser(StaffEntity user, List<String> roles) {
        this.user = user;
        this.roles = roles;
    }

    public LoggedUser(StaffEntity user, SiteEntity site, List<String> roles) {
        this.user = user;
        this.site = site;
        this.roles = roles;
    }

    public LoggedUser(StaffEntity user, SiteEntity site, List<String> roles, HashMap<String, String> config) {
        this.user = user;
        this.site = site;
        this.roles = roles;
        this.config = config;
    }

    public List<String> getRoles() {
        return roles;
    }

    public SiteEntity getSite() {
        return site;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public StaffEntity getUser() {
        return user;
    }

    public void setUser(StaffEntity user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
