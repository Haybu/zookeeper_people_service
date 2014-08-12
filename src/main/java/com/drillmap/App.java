package com.drillmap;

import com.drillmap.service.RestServiceDetails;
import com.drillmap.service.ServerRegistrar;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.x.discovery.ServiceDiscovery;
import com.netflix.curator.x.discovery.ServiceDiscoveryBuilder;
import com.netflix.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "services.zookeeper")
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // zookeeper hostname
    private String keeperHostName;

    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_HOST = "server.host";

    public static void main( String[] args )
    {
        SpringApplication application = new SpringApplication(App.class);
        application.setShowBanner(false);
        ApplicationContext context = application.run(args);

        // why not have this to be on the context event
        ServerRegistrar registrar = (ServerRegistrar) context.getBean("registrar");
        registrar.registerService("peopleService", "/people", "peopleService", "1.0");
    }



    /**
     * To connect to Apache ZooKeeper, we need to create an instance of CuratorFramework class
     *
     * @return
     */
    @Bean( initMethod = "start", destroyMethod = "close" )
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient(this.getKeeperHostName(), new ExponentialBackoffRetry(1000, 3));
    }

    /**
     * create an instance of ServiceDiscovery class which will allow to publish service information for discovery
     * into Apache ZooKeeper using just created CuratorFramework instance (we also would like to submit
     * RestServiceDetails as additional metadata along with every service registration)
     *
     * @return
     */
    @Bean( initMethod = "start", destroyMethod = "close" )
    public ServiceDiscovery<RestServiceDetails> discovery() {
        JsonInstanceSerializer< RestServiceDetails > serializer =
                         new JsonInstanceSerializer< RestServiceDetails >( RestServiceDetails.class );

        return ServiceDiscoveryBuilder.builder(RestServiceDetails.class)
                .client(curator())
                .basePath("services")
                .serializer(serializer)
                .build();
    }

    public String getKeeperHostName() {
        return keeperHostName;
    }

    public void setKeeperHostName(String hostname) {
        this.keeperHostName = hostname;
    }

}
