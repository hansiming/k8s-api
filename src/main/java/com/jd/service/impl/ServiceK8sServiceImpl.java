package com.jd.service.impl;

import com.jd.service.ServiceK8sService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import com.jd.util.ReturnResult;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by hansiming on 2017/7/11.
 */
@Service
public class ServiceK8sServiceImpl implements ServiceK8sService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceK8sServiceImpl.class);

    @Value("${k8s_url}")
    private String k8sUrl;

    @Override
    public ReturnMessage getServices() {

        ServiceList list;
        try {
            KubernetesClient client = K8sClientUtil.getKubernetesClient(k8sUrl);
            list = client.services().list();
        } catch (Exception e) {
            LOGGER.error("get services has error, e = {}", e);
            return new ReturnResult(false, e.getMessage(), null);
        }

        LOGGER.error("get services successful, list = {}", list);
        return new ReturnResult(true, "success", list);
    }

    @Override
    public ReturnMessage getServicesByNamespaceName(String namespaceName) {
        return null;
    }

    @Override
    public ReturnMessage create(String namespaceName, String serviceName, String labelKey, String labelValue) {
        return null;
    }

    @Override
    public ReturnMessage edit(String namespaceName, String serviceName, String labelKey, String labelValue) {
        return null;
    }

    @Override
    public ReturnMessage del(String namespaceName, String serviceName) {
        return null;
    }
}
