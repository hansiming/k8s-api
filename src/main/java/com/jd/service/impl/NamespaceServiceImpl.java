package com.jd.service.impl;

import com.jd.service.NamespaceService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import com.jd.util.ReturnResult;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by hansiming on 2017/7/10.
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceServiceImpl.class);

    @Override
    public ReturnMessage getNamespaces() {

        NamespaceList list;
        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            list = client.namespaces().list();
//            client.replicationControllers().inNamespace("").withName("").get();
        } catch (Exception e) {
            LOGGER.error("get namespace has exception, e = {}, url = {}", e);
            return new ReturnResult(false, e.getMessage(), null);
        }
        return new ReturnResult(true, "success", list);
    }

    @Override
    public ReturnMessage getNamespaceByName(String namespaceName) {

        if(!K8sClientUtil.namespaceIsExist(namespaceName)) {
            LOGGER.error("could not find namespace by name = {} ", namespaceName);
            return new ReturnResult(false, "could not find namespace by name : " + namespaceName, null);
        }

        Namespace namespace;
        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            namespace = client.namespaces().withName(namespaceName).get();
        } catch (Exception e) {
            LOGGER.error("get namespace by name has error, e = {}, namespace name = {}", e, namespaceName);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("find namespace  by name = {}, namespace = {} ", namespaceName, namespace);
        return new ReturnResult(true, "success", namespace);
    }

    @Override
    public ReturnMessage create(String namespaceName, String labelKey, String labelValue) {

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.namespaces().createNew().withNewMetadata().withName(namespaceName)
                    .addToLabels(labelKey, labelValue).endMetadata().done();
        } catch (Exception e) {
            LOGGER.error("create namespace has error, e = {}, namespace name = {}, label key = {}, label value = {}", e, namespaceName, labelKey, labelValue);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("create namespace successful, namespace = {}", namespaceName);
        return new ReturnResult(true, "success", null);
    }

    @Override
    public ReturnMessage del(String namespaceName) {

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.namespaces().withName(namespaceName).delete();
        } catch (Exception e) {
            LOGGER.error("del namespace has error, e = {}, namespace name = {}", e, namespaceName);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("del namespace successful, namespace = {}", namespaceName);
        return new ReturnResult(true, "success", null);
    }

    @Override
    public ReturnMessage edit(String namespaceName, String labelKey, String labelValue) {

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.namespaces().withName(namespaceName).edit().editMetadata().addToLabels(labelKey, labelValue).endMetadata().done();
        } catch (Exception e) {
            LOGGER.error("edit namespace has error, e = {}, namespace name = {}", e, namespaceName);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("edit namespace successful, namespace = {}", namespaceName);
        return new ReturnResult(true, "success", null);
    }
}
