package com.jd.config;

import com.jd.server.NamespaceResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by hansiming on 2017/7/10.
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(NamespaceResource.class);
    }
}
