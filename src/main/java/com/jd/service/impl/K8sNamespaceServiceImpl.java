package com.jd.service.impl;

import com.jd.service.K8sNamespaceService;
import com.jd.util.K8sClientUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by hansiming on 2017/7/18.
 */
@Service
public class K8sNamespaceServiceImpl implements K8sNamespaceService {

    private final Logger LOGGER = LoggerFactory.getLogger(K8sNamespaceServiceImpl.class);

    @Override
    public void createNamespace(String namespaceName) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();
        LOGGER.info("create namespace, namespace = {}, client = {}", namespaceName, client);
        client.namespaces().createNew().withNewMetadata().withName(namespaceName).endMetadata().done();
    }

    @Override
    public void deleteNamespace(String namespaceName) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();
        client.namespaces().withName(namespaceName).delete();
    }
}
