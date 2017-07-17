package com.jd.service.impl;

import com.jd.dao.K8sNamespaceDao;
import com.jd.model.K8sNamespace;
import com.jd.service.K8sControllerService;
import com.jd.service.K8sResourceService;
import com.jd.service.K8sServiceService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by hansiming on 2017/7/17.
 */
@Service
public class K8sResourceServiceImpl implements K8sResourceService {

    @Value("${api_version}")
    private String apiVersion;

    @Autowired
    private K8sNamespaceDao k8sNamespaceDao;

    @Autowired
    private K8sControllerService k8sControllerService;

    @Autowired
    private K8sServiceService k8sServiceService;

    @Override
    public ReturnMessage createResource(String userName, String resourceName, int containerCount) {

        //1.创建一个uuid作为namespaceName
        String namespaceName = UUID.randomUUID().toString();
        K8sNamespace k8sNamespace = new K8sNamespace();
        k8sNamespace.setUserName(userName);
        k8sNamespace.setNamespaceName(namespaceName);
        int id = k8sNamespaceDao.insertNamespace(k8sNamespace);

        try{

            //2. 创建一个namespace
            createNamespace(namespaceName);

            //3. 创建一个MasterController
            k8sControllerService.createMasterController(namespaceName, resourceName);

            //4. 创建一个MasterService
            k8sServiceService.createMasterService(namespaceName, resourceName, id);

            //5. 创建一个WorkController
            k8sControllerService.createWorkController(namespaceName, resourceName, containerCount);

            //6. 创建一个ThriftServerController
            k8sControllerService.createThriftServerController(namespaceName, resourceName);

            //7. 创建一个ThriftServerService
            k8sServiceService.createThriftServerService(namespaceName, resourceName, id);
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
