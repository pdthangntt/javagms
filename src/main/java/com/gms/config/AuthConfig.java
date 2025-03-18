package com.gms.config;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import java.util.Collection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class AuthConfig implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private static AuthConfig instance;
    private boolean isSecurity;

    public static AuthConfig newInstance() {
        try {
            instance = new AuthConfig();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
        return instance;
    }

    public void setIsSecurity(boolean isSecurity) {
        this.isSecurity = isSecurity;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl().split("\\?")[0];

        if (url.contains("/error")
                || url.contains("/static/")
                || url.contains("/excel/")
                || url.contains("/webjars/")
                || url.contains("/auth/")
                || url.contains(UrlUtils.signin()) || url.equalsIgnoreCase(UrlUtils.logout())
                || url.equalsIgnoreCase("/j_spring_security_check")
                || url.equalsIgnoreCase("/OneSignalSDKWorker.js")
                || url.equalsIgnoreCase("/OneSignalSDKUpdaterWorker.js")
                || url.equalsIgnoreCase("/ping.json")) {
            return null;
        }

        LoggedUser user = fi.getRequest().getUserPrincipal() == null ? null : (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null || user.getRoles() == null) {
            return SecurityConfig.createList("permitAll");
        }

        if (!this.isSecurity || url.equalsIgnoreCase("/")
                || url.equalsIgnoreCase(UrlUtils.backendHome())
                || url.equalsIgnoreCase(UrlUtils.home())) {
            return null;
        }

        if (!user.getRoles().contains(url)) {
            return SecurityConfig.createList("permitAll");
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
