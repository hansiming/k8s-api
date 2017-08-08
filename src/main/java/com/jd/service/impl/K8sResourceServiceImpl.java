package com.jd.service.impl;

import com.jd.dao.K8sResourceMapper;
import com.jd.model.K8sResource;
import com.jd.service.K8sControllerService;
import com.jd.service.K8sNamespaceService;
import com.jd.service.K8sResourceService;
import com.jd.service.K8sServiceService;
import com.jd.util.ReturnMessage;
import com.jd.util.ReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by hansiming on 2017/7/26.
 */
@Service
public class K8sResourceServiceImpl implements K8sResourceService {

    private static Logger log = Logger.getLogger(K8sResourceServiceImpl.class.getName());

    @Autowired
    private K8sResourceMapper k8sResourceDao;

    @Autowired
    private K8sNamespaceService k8sNamespaceService;

    @Autowired
    private K8sControllerService k8sControllerService;

    @Autowired
    private K8sServiceService k8sServiceService;

    @Value("${default_container_thrift_node_port}")
    private int defaultContainerThriftNodePort;

    @Override
    public ReturnMessage getResource(String userName, String resourceName) {

        K8sResource k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, resourceName);

        if(k8sResource == null) {
            log.info("get kubernetes resource is fail, do not have a resource, user name is " + userName + ", resourceName is " + resourceName);
            return new ReturnMessage(false, "do not have a resource, user name is " + userName + ", resourceName is " + resourceName);
        }

        /** 隐藏namespace的名字和ThriftServerNodePort*/
        k8sResource.setNamespaceName("xxx");
        k8sResource.setThriftServerNodePort(0);

        return new ReturnResult(true, "success", k8sResource);
    }

    @Override
    public ReturnMessage createResource(String userName, String resourceName, int containerCount, long resourceTypeId, String useTime, String useType) {

        // check username and resource name is exists
        K8sResource k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, resourceName);

        if(k8sResource != null) {
            log.info("create kubernetes resource is fail, resource name is already exists, resource name = " + resourceName + ",username = " + userName);
            return new ReturnMessage(false, "resource name is already exists");
        }

        //1.创建一个uuid作为namespaceName
        k8sResource = new K8sResource();
        String namespaceName = UUID.randomUUID().toString();
//        DateTime now = DateTime.now();
        k8sResource.setNamespaceName(namespaceName);
        k8sResource.setUserName(userName);
        k8sResource.setResourceName(resourceName);
        k8sResource.setUseTime(useTime);
        k8sResource.setUseType(useType);
        k8sResource.setResourceTypeId(resourceTypeId);
        k8sResource.setContainerCount(containerCount);
//        k8sResource.setCreateTime(DateUtil.formatDateTime(now));
//        k8sResource.setUpdateTime(DateUtil.formatDateTime(now));
//        k8sResource.setStartTime(DateUtil.formatDateTime(now));
//        k8sResource.setEndTime(DateUtil.formatDateTime(
//                DateUtil.getEndDayForApplyCluster(now,
//                        useType,
//                        Integer.parseInt(useTime))));
        k8sResourceDao.insertResource(k8sResource);
        /** thrift server node port = 30022 + id*/
        k8sResource.setThriftServerNodePort(k8sResource.getId() + defaultContainerThriftNodePort);
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
            k8sControllerService.createWorkController(namespaceName, containerCount, resourceTypeId);

            //6. 创建一个ThriftServerController
            k8sControllerService.createThriftServerController(namespaceName);

            //7. 创建一个ThriftServerService
            k8sServiceService.createThriftServerService(namespaceName, k8sResource.getThriftServerNodePort());
        } catch (Exception e) {

            log.info("create kubernetes resource is fail, resource name = " + resourceName + ",username = " + userName + ", e=" + e);
            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }

    @Override
    public ReturnMessage deleteResource(String userName, int resourceId) {

        K8sResource k8sResource = k8sResourceDao.selectResourceById(resourceId);

        if(checkResourceIsExistsByUserNameAndResourceId(userName, k8sResource)) {

            log.info("delete kubernetes resource is fail, do not have a resource,userName = " + userName + ", id is " + resourceId);
            return new ReturnMessage(false, "do not have a resource, userName = " + userName + ", id is " + resourceId);
        }

        try {
            // delete namespace
            k8sNamespaceService.deleteNamespace(k8sResource.getNamespaceName());
            //delete to db
            k8sResourceDao.deleteResource(k8sResource);
        } catch (Exception e) {

            log.info("delete kubernetes resource is fail, userName = " + userName + ", resourceName = " + k8sResource.getResourceName() + ", e = " + e);
            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }

    @Override
    public ReturnMessage editResource(String userName, int resourceId, int containerCount, long resourceTypeId) {

        K8sResource k8sResource = k8sResourceDao.selectResourceById(resourceId);

        if (checkResourceIsExistsByUserNameAndResourceId(userName, k8sResource)) {

            log.info("edit kubernetes resource is fail, do not have a resource,userName = " + userName + ", id is " + resourceId);
            return new ReturnMessage(false, "do not have a resource, resourceId is " + resourceId);
        }

//        DateTime now = DateTime.now();
//        k8sResource.setUpdateTime(DateUtil.formatDateTime(now));
        k8sResource.setContainerCount(containerCount);
        k8sResourceDao.updateResource(k8sResource);

        try {
            // scale
            k8sControllerService.editWorkController(k8sResource.getNamespaceName(), k8sResource.getContainerCount() + containerCount, resourceTypeId, k8sResource.getResourceTypeId());
        } catch (Exception e) {

            return new ReturnMessage(false, e.getMessage());
        }
        return new ReturnMessage(true, "success");
    }

    private boolean checkResourceIsExistsByUserNameAndResourceId(String userName, K8sResource k8sResource) {

        if(k8sResource == null)
            return true;

        k8sResource = k8sResourceDao.selectResourceByResourceNameAndUserName(userName, k8sResource.getResourceName());

        //check resource is belong to user or not
        if(k8sResource == null)
            return true;

        return false;
    }
}

