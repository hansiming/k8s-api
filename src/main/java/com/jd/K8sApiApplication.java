package com.jd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by hansiming on 2017/7/10.
 */
@SpringBootApplication
public class K8sApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(K8sApiApplication.class);
    }

    public static void main(String[] args){

        new K8sApiApplication().configure(new SpringApplicationBuilder(K8sApiApplication.class)).run(args);
    }
}
