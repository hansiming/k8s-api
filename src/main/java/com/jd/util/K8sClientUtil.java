package com.jd.util;

import com.google.common.collect.Maps;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.jd.model.K8sConstant.*;

/**
 * Created by hansiming on 2017/7/10.
 */
@Component
public class K8sClientUtil {

    @Value("${k8s_url}")
    private String k8sUrl;

    private static K8sClientUtil util;

    @PostConstruct
    public void init() {
        util = this;
    }

    public static KubernetesClient getKubernetesClient() {
        Config config = new ConfigBuilder().withMasterUrl(util.k8sUrl).build();
        return new DefaultKubernetesClient(config);
    }

    public static boolean namespaceIsExist(String namespaceName) {

        KubernetesClient client = getKubernetesClient();
        if(client.namespaces().withName(namespaceName).get() == null)
            return false;
        return true;
    }

    public static boolean serviceIsExist(String serviceName) {

        KubernetesClient client = getKubernetesClient();
        if(client.services().withName(serviceName).get() == null)
            return false;
        return true;
    }

    public static Map<String, String> getMasterSelector(String masterContainerName) {

        Map<String, String> selector = Maps.newHashMap();
        selector.put(COMPONENT_INFO, masterContainerName);
        return selector;
    }

    public static Map<String, String> getWorkerSelector(String workerContainerName) {

        Map<String, String> selector = Maps.newHashMap();
        selector.put(COMPONENT_INFO, workerContainerName);
        return selector;
    }

    public static Map<String, String> getThriftSelector(String thriftServerContainerName) {

        Map<String, String> selector = Maps.newHashMap();
        selector.put(COMPONENT_INFO, thriftServerContainerName + THRIFT_SERVER_INFO);
        return selector;
    }
}
