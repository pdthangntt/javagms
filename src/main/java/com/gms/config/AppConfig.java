package com.gms.config;

import com.gms.components.UrlUtils;
import com.gms.components.hivinfo.CallUtils;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.gms.*")
public class AppConfig implements WebMvcConfigurer {

    private final ActivityConfig activityInterceptor;
    public static final String SECRET = "@1980đội";

    @Autowired
    private Environment env;

    /**
     *
     * @param activityInterceptor
     */
    @Autowired
    public AppConfig(ActivityConfig activityInterceptor) {
        this.activityInterceptor = activityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(activityInterceptor);
    }

    @Bean(name = "callUtils")
    public CallUtils getHIVInfoAPIUtils() {
        return new CallUtils(env.getProperty("app.hivinfo.api.url"), SECRET, env.getProperty("app.hivinfo.username"), env.getProperty("app.hivinfo.pwd"));
    }

    @Bean(name = "urlUtils")
    public UrlUtils getUrlUtils() {
        return new UrlUtils(env.getProperty("app.baseUrl"));
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("vi", "VN"));
        resolver.setCookieDomain("app_locale_cookie");
        resolver.setCookieMaxAge(60 * 60);
        return resolver;
    }

    @Bean
    @Description("Thymeleaf view resolver")
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.setEnableSpringELCompiler(env.getProperty("spring.thymeleaf.enable-spring-el-compiler", Boolean.class));
        return templateEngine;
    }

    private ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(env.getProperty("spring.thymeleaf.prefix"));
        templateResolver.setSuffix(env.getProperty("spring.thymeleaf.suffix"));
        templateResolver.setTemplateMode(env.getProperty("spring.thymeleaf.mode"));
        templateResolver.setCharacterEncoding(env.getProperty("spring.thymeleaf.encoding"));
        templateResolver.setCacheable(env.getProperty("spring.thymeleaf.cache", Boolean.class));
        templateResolver.setOrder(1);
        return templateResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        boolean staticCache = env.getProperty("app.static.cache", Boolean.class);

        addResource(registry, "/OneSignalSDKUpdaterWorker.js", "classpath:/static/backend/plugin/onesignal/OneSignalSDKUpdaterWorker.js", true);
        addResource(registry, "/OneSignalSDKWorker.js", "classpath:/static/backend/plugin/onesignal/OneSignalSDKWorker.js", true);
        addResource(registry, "/excel/**", "classpath:/import_templates/", true);
        addResource(registry, "/webjars/**", "/webjars/", true);
        addResource(registry, "/static/backend/images/**", "classpath:/static/backend/images/", true);
        addResource(registry, "/static/backend/plugin/**", "classpath:/static/backend/plugin/", staticCache);
        addResource(registry, "/static/backend/css/**", "classpath:/static/backend/css/", staticCache);
        addResource(registry, "/static/backend/js/**", "classpath:/static/backend/js/", staticCache);
        addResource(registry, "/static/report/js/**", "classpath:/static/report/js/", staticCache);
        addResource(registry, "/static/report/css/**", "classpath:/static/report/css/", true);

        addResource(registry, "/static/auth/images/**", "classpath:/static/auth/images/", true);
        addResource(registry, "/static/auth/css/**", "classpath:/static/auth/css/", staticCache);
        addResource(registry, "/static/auth/js/**", "classpath:/static/auth/js/", staticCache);
        addResource(registry, "/static/auth/fonts/font-awesome-4.7.0/css/**", "classpath:/static/auth/fonts/font-awesome-4.7.0/css/", true);
        addResource(registry, "/static/auth/fonts/font-awesome-4.7.0/fonts/**", "classpath:/static/auth/fonts/font-awesome-4.7.0/fonts/", true);
        addResource(registry, "/static/importation/js/**", "classpath:/static/importation/js/", staticCache);
    }

    private void addResource(ResourceHandlerRegistry registry, String handler, String location, boolean isCache) {
        if (isCache) {
            registry.addResourceHandler(handler).addResourceLocations(location)
                    .setCachePeriod(env.getProperty("app.static.cache.ram.time", Integer.class) * 60 * 60 * 24)
                    .setCacheControl(CacheControl.maxAge(env.getProperty("app.static.cache.disk.time", Integer.class), TimeUnit.DAYS))
                    .resourceChain(true)
                    .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
        } else {
            registry.addResourceHandler(handler).addResourceLocations(location);
        }
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    @Bean
    public Boolean disableSSLValidation() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return true;
    }

}
