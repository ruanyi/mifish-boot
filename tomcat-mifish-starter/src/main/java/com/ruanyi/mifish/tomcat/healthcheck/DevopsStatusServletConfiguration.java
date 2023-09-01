package com.ruanyi.mifish.tomcat.healthcheck;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-07-04 17:24
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "mifish.tomcat.healtch.check", prefix = "enabled", matchIfMissing = true)
public class DevopsStatusServletConfiguration {

    @Bean
    public ServletRegistrationBean devopsStatusServletRegistrationBean(DevopsStatusServlet devopsStatusServlet) {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(devopsStatusServlet);
        registrationBean.addUrlMappings(devopsStatusServlet.getUrlMappings());
        return registrationBean;
    }

    @Bean
    public DevopsStatusServlet newDevopsStatusServlet() {
        return new DevopsStatusServlet();
    }
}
