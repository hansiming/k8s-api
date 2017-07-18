package com.jd.service.impl;

import com.jd.dao.K8sResourceDao;
import com.jd.model.K8sConstant;
import com.jd.model.K8sResource;
import com.jd.service.K8sControllerService;
import com.jd.service.K8sNamespaceService;
import com.jd.service.K8sResourceService;
import com.jd.service.K8sServiceService;
import com.jd.util.K8sClientUtil;
import com.jd.util.ReturnMessage;
import com.jd.util.ReturnResult;
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
    private K8sNamespaceService k8sNamespaceService;

    @Autowired
    private K8sControllerService k8sControllerService;

    @Autowired
    private K8sServiceService k8sServiceService;

    @Override
    public ReturnMessage getResource(String userName, String resourceName) {

        K8sResource k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, resourceName);

        if(k8sResource == null) {
            return new ReturnMessage(false, "do not have a resource, user name is " + userName + ", resourceName is " + resourceName);
        }

        k8sResource.setNamespaceName("xxx");
        k8sResource.setThriftServerNodePort(0);

        return new ReturnResult(true, "success", k8sResource);
    }

    @Override
    public ReturnMessage createResource(String userName, String resourceName, int containerCount) {

        // check username and resource name is exists
        K8sResource k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, resourceName);

        if(k8sResource != null)
            return new ReturnMessage(false, "resouce name is empty");

        //1.创建一个uuid作为namespaceName
        k8sResource = new K8sResource();
        String namespaceName = UUID.randomUUID().toString();
        k8sResource.setNamespaceName(namespaceName);
        k8sResource.setUserName(userName);
        k8sResource.setResourceName(resourceName);
        k8sResourceDao.insertResource(k8sResource);
        /** thrift server node port = 30022 + id*/
        k8sResource.setThriftServerNodePort(k8sResource.getId() + K8sConstant.CONTAINER_DEFAULT_THRIFT_NODE_PORT);
        k8sResourceDao.updateResource(k8sResource);

        try{

            //2. 创建一个namespace
            k8sNamespaceService.createNamespace(namespaceName);

            //3. 创建一个MasterController
            k8sControllerService.createMasterController(namespaceName);

            //4. 创建一个MasterService
            /** Master未向外界暴露，所以暂时没有Master Service 的 node port*/
            k8sServiceService.createMasterService(namespaceName, 1);

            //5. 创建一个WorkController
            k8sControllerService.createWorkController(namespaceName, containerCount);

            //6. 创建一个ThriftServerController
            k8sControllerService.createThriftServerController(namespaceName);

            //7. 创建一个ThriftServerService
            k8sServiceService.createThriftServerService(namespaceName, k8sResource.getThriftServerNodePort());
        } catch (Exception e) {
            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }

    @Override
    public ReturnMessage deleteResource(String userName, int resourceId) {

        K8sResource k8sResource = k8sResourceDao.selectResourceById(resourceId);

        if(k8sResource == null)
            return new ReturnMessage(false, "do not have a resource, id is " + resourceId);

        k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, k8sResource.getResourceName());

        //check resource is belong to user or not
        if(k8sResource == null)
            return new ReturnMessage(false, "do not have a resource, name is " + k8sResource.getResourceName());

        try {
            // delete namespace
            k8sNamespaceService.deleteNamespace(k8sResource.getNamespaceName());
            k8sResourceDao.deleteResource(k8sResource);
        } catch (Exception e) {

            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }

    @Override
    public ReturnMessage editResource(String userName, int resourceId, int containerCount) {

        K8sResource k8sResource = k8sResourceDao.selectResourceById(resourceId);

        if(k8sResource == null)
            return new ReturnMessage(false, "do not have a resource, resourceId is " + resourceId);

        k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, k8sResource.getResourceName());

        //check resource is belong to user or not
        if(k8sResource == null)
            return new ReturnMessage(false, "do not have a resource, user name is " + userName + ", resourceId is " + resourceId);


        try {
            // delete namespace
            k8sControllerService.editWorkController(k8sResource.getNamespaceName(), containerCount);
        } catch (Exception e) {

            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }
}
