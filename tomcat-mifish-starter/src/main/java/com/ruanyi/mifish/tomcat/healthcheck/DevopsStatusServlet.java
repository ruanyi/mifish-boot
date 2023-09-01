package com.ruanyi.mifish.tomcat.healthcheck;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.Lifecycle;

/**
 * Description:
 *
 * 在当前应用新增health check的接口：devops/status
 *
 * @author: ruanyi
 * @Date: 2019-07-04 17:13
 */
public class DevopsStatusServlet extends HttpServlet implements ApplicationContextAware {

    public static final String HEALTH_CHECK_URL = "/devops/status";

    /** applicationContext */
    private ApplicationContext applicationContext;

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isSpringContextRunning()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    /**
     * isSpringContextRunning
     *
     * @return
     */
    public boolean isSpringContextRunning() {
        if (this.applicationContext instanceof Lifecycle) {
            Lifecycle lifecycle = (Lifecycle)this.applicationContext;
            return lifecycle.isRunning();
        }
        return false;
    }

    /**
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * getUrlMappings
     *
     * @return
     */
    protected String[] getUrlMappings() {
        return new String[] {HEALTH_CHECK_URL};
    }
}
