package com.jd.util;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Created by hansiming on 2017/7/10.
 */
public class K8sClientUtil {

    public static KubernetesClient getKubernetesClient(String k8sUrl) {
        Config config = new ConfigBuilder().withMasterUrl(k8sUrl).build();
        return new DefaultKubernetesClient(config);
    }
}
