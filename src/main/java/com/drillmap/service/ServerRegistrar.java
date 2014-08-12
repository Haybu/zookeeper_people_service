package com.drillmap.service;

import com.netflix.curator.x.discovery.ServiceDiscovery;
import com.netflix.curator.x.discovery.ServiceInstance;
import com.netflix.curator.x.discovery.UriSpec;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/** not in use
 *
 * Created by hmohamed on 8/11/14.
 */
@Component("registrar")
public class ServerRegistrar {

    @Autowired
    public ServiceDiscovery< RestServiceDetails > discovery;

    @Autowired
    EmbeddedWebApplicationContext server;


    private final Log logger = LogFactory.getLog(ServerRegistrar.class);

    public void registerService (String serviceBeanName, String serviceRequestMapping
                    , String serviceLogicalName, String version)
    {
        int servicePort = server.getEmbeddedServletContainer().getPort();
        String serviceHost = ((TomcatEmbeddedServletContainer)server.getEmbeddedServletContainer()).getTomcat().getHost().getName();

        logger.info("start registering service " + serviceBeanName + " on server: " + serviceHost + " on port: " + servicePort);

        UriSpec uriSpec = getUriSpec(serviceHost, serviceRequestMapping);

        final ServiceInstance< RestServiceDetails > instance;
        try {
            instance = ServiceInstance.<RestServiceDetails>builder()
                .name(serviceLogicalName)
                .payload(new RestServiceDetails(version))
                .port(servicePort)
                .uriSpec(uriSpec)
                .build();

            logger.info("zookeeper will register a service at uri: " + instance.buildUriSpec());

            discovery.registerService( instance );
        } catch (Exception e) {
            logger.info("error registering service: " + serviceBeanName);
            e.printStackTrace();
        }

        logger.info("service " + serviceBeanName + " is registered successfully");

    }

    /**
     *
     * @param hostname: example localhost
     * @param servicePath: example /users
     * @return
     */
    public UriSpec getUriSpec(final String hostname, final String servicePath) {
        UriSpec uriSpec = new UriSpec(
                String.format( "{scheme}://%s:{port}%s%s",
                        hostname,
                        AppEmbeddedServletContainer.contextPath,
                        servicePath
                ) );

        return uriSpec;

    }


}
