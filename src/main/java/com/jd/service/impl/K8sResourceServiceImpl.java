package com.jd.service.impl;

import com.jd.dao.K8sResourceDao;
import com.jd.model.K8sConstant;
import com.jd.model.K8sResource;
import com.jd.service.K8sControllerService;
import com.jd.service.K8sResourceService;
import com.jd.service.K8sServiceService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by hansiming on 2017/7/17.
 */
@Service
public class K8sResourceServiceImpl implements K8sResourceService {

    @Autowired
    private K8sResourceDao k8sResourceDao;

    @Autowired
    private K8sControllerService k8sControllerService;

    @Autowired
    private K8sServiceService k8sServiceService;

    @Override
    public ReturnMessage createResource(String userName, String resourceName, int containerCount) {

        //1.创建一个uuid作为namespaceName
        String namespaceName = UUID.randomUUID().toString();
        K8sResource k8sResource = new K8sResource();
        k8sResource.setUserName(userName);
        k8sResource.setNamespaceName(namespaceName);
        k8sResource.setResourceName(resourceName);
        k8sResourceDao.insertResource(k8sResource);
        /** thrift server node port = 30022 + id*/
        k8sResource.setThriftServerNodePort(k8sResource.getId() + K8sConstant.CONTAINER_DEFAULT_THRIFT_NODE_PORT);
        k8sResourceDao.updateResource(k8sResource);

        try{

            //2. 创建一个namespace
            createNamespace(namespaceName);

            //3. 创建一个MasterController
            k8sControllerService.createMasterController(namespaceName, resourceName);

            //4. 创建一个MasterService
            /** Master未向外界暴露，所以暂时没有Master Service 的 node port*/
            k8sServiceService.createMasterService(namespaceName, resourceName, 1);

            //5. 创建一个WorkController
            k8sControllerService.createWorkController(namespaceName, resourceName, containerCount);

            //6. 创建一个ThriftServerController
            k8sControllerService.createThriftServerController(namespaceName, resourceName);

            //7. 创建一个ThriftServerService
            k8sServiceService.createThriftServerService(namespaceName, resourceName, k8sResource.getThriftServerNodePort());
        } catch (Exception e) {

            return new ReturnMessage(false, e.getMessage());
        }

        return new ReturnMessage(true, "success");
    }

    private void createNamespace(String namespaceName) throws Exception {
        KubernetesClient client = K8sClientUtil.getKubernetesClient();
        client.namespaces().createNew().withNewMetadata().withName(namespaceName).endMetadata().done();
    }
}
