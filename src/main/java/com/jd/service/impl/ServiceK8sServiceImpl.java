package com.jd.service.impl;

import com.jd.service.ServiceK8sService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import com.jd.util.ReturnResult;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by hansiming on 2017/7/11.
 */
@Service
public class ServiceK8sServiceImpl implements ServiceK8sService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceK8sServiceImpl.class);

    @Override
    public ReturnMessage getServices() {

        ServiceList list;
        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            list = client.services().list();
        } catch (Exception e) {
            LOGGER.error("get services has error, e = {}", e);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("get services successful, list = {}", list);
        return new ReturnResult(true, "success", list);
    }

    @Override
    public ReturnMessage getServicesByNamespaceName(String namespaceName) {

        if(!K8sClientUtil.namespaceIsExist(namespaceName)) {
            LOGGER.error("could not find namespace by name = {} ", namespaceName);
            return new ReturnResult(false, "could not find namespace by name : " + namespaceName, null);
        }

        ServiceList list;
        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            list = client.services().inNamespace(namespaceName).list();
        } catch (Exception e) {
            LOGGER.error("get services has error, e = {}, namespaceName = {}", e, namespaceName);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("get services successful, list = {}", list);
        return new ReturnResult(true, "success", list);
    }

    @Override
    public ReturnMessage create(String namespaceName, String serviceName, String labelKey, String labelValue) {

        if(!K8sClientUtil.namespaceIsExist(namespaceName)) {
            LOGGER.error("could not find namespace by name = {} ", namespaceName);
            return new ReturnResult(false, "could not find namespace by name : " + namespaceName, null);
        }

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.services().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(serviceName)
                    .addToLabels(labelKey, labelValue).endMetadata().done();
        } catch (Exception e) {
            LOGGER.error("create services has error, e = {}, serviceName = {}, namespaceName = {}, labelKey = {}, labelValue = {}",
                    e, serviceName, namespaceName, labelKey, labelValue);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("create services successful, serviceName = {}, namespaceName = {}, labelKey = {}, labelValue = {}",
                serviceName, namespaceName, labelKey, labelValue);
        return new ReturnResult(true, "suceess", null);
    }

    @Override
    public ReturnMessage edit(String namespaceName, String serviceName, String labelKey, String labelValue) {

        if(!K8sClientUtil.namespaceIsExist(namespaceName)) {
            LOGGER.error("could not find namespace by name = {} ", namespaceName);
            return new ReturnResult(false, "could not find namespace by name : " + namespaceName, null);
        }

        if(!K8sClientUtil.serviceIsExist(serviceName)) {
            LOGGER.error("could not find serviceName by name = {} ", serviceName);
            return new ReturnResult(false, "could not find service by name : " + serviceName, null);
        }

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.services().inNamespace(namespaceName).withName(serviceName).edit().editMetadata()
                    .addToLabels(labelKey, labelValue).endMetadata().done();
        } catch (Exception e) {
            LOGGER.error("edit services has error, e = {}, serviceName = {}, namespaceName = {}, labelKey = {}, labelValue = {}",
                    e, serviceName, namespaceName, labelKey, labelValue);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("edit services successful, serviceName = {}, namespaceName = {}, labelKey = {}, labelValue = {}",
                serviceName, namespaceName, labelKey, labelValue);
        return new ReturnResult(true, "suceess", null);
    }

    @Override
    public ReturnMessage del(String namespaceName, String serviceName) {

        if(!K8sClientUtil.namespaceIsExist(namespaceName)) {
            LOGGER.error("could not find namespace by name = {} ", namespaceName);
            return new ReturnResult(false, "could not find namespace by name : " + namespaceName, null);
        }

        if(!K8sClientUtil.serviceIsExist(serviceName)) {
            LOGGER.error("could not find serviceName by name = {} ", serviceName);
            return new ReturnResult(false, "could not find service by name : " + serviceName, null);
        }

        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient();
            client.services().inNamespace(namespaceName).withName(serviceName).delete();
        } catch (Exception e) {
            LOGGER.error("del services has error, e = {}, serviceName = {}, namespaceName = {}",
                    e, serviceName, namespaceName);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.info("del services successful, serviceName = {}, namespaceName = {}}",
                serviceName, namespaceName);
        return new ReturnResult(true, "suceess", null);
    }
}
