package com.drillmap.service;

import com.drillmap.App;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmohamed on 8/11/14.
 */
@RestController("peopleService")
public class PeopleRestService {

    private final Log logger = LogFactory.getLog(PeopleRestService.class);

    public static final String SERVICE_PATH = "/people";

    @Autowired
    Environment environment;

    ApplicationContext context;


    @RequestMapping(value = PeopleRestService.SERVICE_PATH, method = RequestMethod.GET)
     public List<Person> loadAllUsers() {
        return Arrays.asList(
                new Person("Haytham", "Mohamed"),
                new Person("John", "Smith")
        );
    }

    public void register() throws Exception {
        System.out.println("post constructing people rest service");
        System.out.println("I'm running at a host name: "
                + environment.getProperty(App.SERVER_HOST)
                + " and port: "
                + environment.getProperty(App.SERVER_PORT));
    }

}
