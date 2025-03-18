package com.gms.config;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.StaffActivityEntity;
import com.gms.service.StaffActivityService;
import com.google.gson.Gson;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author vvthanh
 */
@Component
public class ActivityConfig extends HandlerInterceptorAdapter {

    @Autowired
    private StaffActivityService staffService;
    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
                && request.getRequestURI().contains(".html")) {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                userAgent = request.getHeader("user-agent");
            }
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
            if (m.find()) {
                userAgent = m.group(1);
            }

            LoggedUser user = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            StaffActivityEntity entity = new StaffActivityEntity();
            entity.setExpires(response.getHeader("Expires"));
            entity.setIp(TextUtils.getClientIpAddress(request));
            entity.setRequestMethod(request.getMethod());
            entity.setRequestUrl(request.getRequestURI());
            entity.setUserID(user.getUser().getID());
            entity.setUserAgent(userAgent);
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                entity.setRequestParameter(gson.toJson(parameterMap));
            }
//            staffService.log(entity);
        }
        return super.preHandle(request, response, handler);
    }
}
