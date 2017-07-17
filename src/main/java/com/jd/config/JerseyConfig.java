package com.jd.config;

import com.jd.server.KuberneteResource;
import com.jd.server.NamespaceResource;
import com.jd.server.ServiceK8sResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by hansiming on 2017/7/10.
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(NamespaceResource.class);
        register(ServiceK8sResource.class);
        register(KuberneteResource.class);
    }
}
