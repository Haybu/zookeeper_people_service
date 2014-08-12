package com.drillmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by hmohamed on 6/23/14.
 */
@Component
@ConfigurationProperties(prefix="services.zookeeper")
public class AppEmbeddedServletContainer implements EmbeddedServletContainerCustomizer {

    public static String contextPath;

    //ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/401.html");
    //ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
    //ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
    //ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String _contextPath) {
        this.contextPath = _contextPath;
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setContextPath(getContextPath());
        //container.addErrorPages(error400Page, error401Page, error404Page, error500Page);
        //container.setPort(getPort());
    }
}
