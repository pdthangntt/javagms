package com.gms.config;

import com.gms.components.UrlUtils;
import com.gms.service.CommonService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Order(1)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.security.enabled}")
    private boolean isSecurity;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CommonService commonService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(commonService).passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().disable();

        //Các trang không yêu cầu login
        http.authorizeRequests().antMatchers(UrlUtils.signin(), UrlUtils.logout()).permitAll();
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage(UrlUtils.error403());

        // Cấu hình remember me, thời gian là 1 ngày
        http.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);

        // Cấu hình cho Login Form.
        http.authorizeRequests()
                .and().formLogin().loginPage(UrlUtils.signin()).loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl(UrlUtils.home()).failureUrl(UrlUtils.signin() + "?error=true").usernameParameter("username").passwordParameter("password")
                .and().logout().logoutUrl(UrlUtils.logout()).logoutSuccessUrl(UrlUtils.signin());

        http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                AuthConfig security = AuthConfig.newInstance();
                security.setIsSecurity(isSecurity);
                o.setSecurityMetadataSource(security);
                return o;
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/auth/**")
                .antMatchers("/excel/**")
                .antMatchers("/api/**")
                .antMatchers("/static/**")
                .antMatchers("/excel/**")
                .antMatchers("/webjars/**");
    }

}
