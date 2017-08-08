package com.jd.service.impl;

import com.google.common.collect.Maps;
import com.jd.model.AdsContainerResourceType;
import com.jd.service.K8sControllerService;
import com.jd.util.K8sClientUtil;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.jd.model.K8sConstant.*;
import static com.jd.util.K8sClientUtil.*;

/**
 * Created by hansiming on 2017/7/17.
 */
@Service
public class K8sControllerServiceImpl implements K8sControllerService {

//    @Autowired
//    ContainerResourceTypeService containerResourceTypeService;

    @Value("${default_resource__memory_unit_size}")
    private String defaultResourceMemoryUnitSize;

    @Value("${default_resource_cpu_unit_size}")
    private String defaultResourceWorkerCpuUnitSize;

    @Value("${default_container_thrift_port}")
    private int defaultContainerThriftPort;

    @Value("${default_container_worker_port}")
    private int defaultContainerWorkerPort;

    @Value("${default_container_spark_port}")
    private int defaultContainerSparkrPort;

    @Value("${default_container_http_port}")
    private int defaultContainerHttpPort;

    @Value("${default_container_thrift_node_port}")
    private int defaultContainerThriftNodePort;

    /**
     kind: ReplicationController
     apiVersion: v1
     metadata:
     name: spark-master-controller
     spec:
     replicas: 1
     selector:
     component: spark-master
     template:
     metadata:
     labels:
     component: spark-master
     spec:
     containers:
     - name: spark-master
     image: jcloud.com/xdata/spark:2.1.1
     command: ["/home/hadoop/start-master"]
     ports:
     - containerPort: 7077
     - containerPort: 8080
     resources:
     requests:
     cpu: 100m
     */
    @Override
    public void createMasterController(String namespaceName) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        //get controller name
        String masterControllerName = DEFAULT_SPARK_NAME + MASTER_INFO + CONTROLLER_INFO;

        //get container name
        String masterContainerName = DEFAULT_SPARK_NAME + MASTER_INFO;

        //make selector
        Map<String, String> masterSelector = getMasterSelector(masterContainerName);

        //replica
        int replicaCount = DEFAULT_MASTER_REPLICA;

        //make container Port
        ContainerPort sparkPort = getDefaultSparkContainerPort();
        ContainerPort httpPort = getDefalutHttpContainerPort();

        //get image and command
        String image = DEFAULT_IMAGE;
        String command = DEFAULT_MASTER_COMMAND;

        //make resources request and limit
        Map<String, Quantity> requests = getDefaultResource();
        Map<String, Quantity> limit = getDefaultResource();

        client.replicationControllers().inNamespace(namespaceName).createNew()
                .editOrNewMetadata().withName(masterControllerName).endMetadata()
                .editOrNewSpec().withReplicas(replicaCount).withSelector(masterSelector)
                .editOrNewTemplate().withNewMetadata().addToLabels(masterSelector).endMetadata()
                .editOrNewSpec().addNewContainer().withName(masterContainerName).withImage(image).withCommand(command).withPorts(sparkPort, httpPort)
                .editOrNewResources().addToRequests(requests).addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
    }

    /**
     apiVersion: v1
     metadata:
     name: spark-worker-controller
     spec:
     replicas: 4
     selector:
     component: spark-worker
     template:
     metadata:
     labels:
     component: spark-worker
     spec:
     containers:
     - name: spark-worker
     image: jcloud.com/xdata/spark:2.1.1
     command: ["/home/hadoop/start-worker"]
     ports:
     - containerPort: 8081
     resources:
     requests:
     cpu: 100m
     */
    @Override
    public void createWorkController(String namespaceName, int containerCount, long resourceTypeId) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        String workControllerName = DEFAULT_SPARK_NAME + WORK_INFO + CONTROLLER_INFO;
        String workContainerName = DEFAULT_SPARK_NAME + WORK_INFO;

        int workReplicaCount = containerCount;
        Map<String, String> selector = getWorkerSelector(workContainerName);
        String image = DEFAULT_IMAGE;
        String command = DEFAULT_WORKER_COMMAND;
        ContainerPort port = getDefaultWorkContainerPort();

        // set request and limit
        AdsContainerResourceType type = new AdsContainerResourceType();
        type.setContainerCores(1);
        type.setContainerMemory(4);
//                containerResourceTypeService.getResourceTypeById(resourceTypeId);
        if(type == null) {
            throw new Exception("can`t find resource type by resourceTypeId = " + resourceTypeId);
        }

//        Map<String, Quantity> requests = getWorkerResource(type);
        Map<String, Quantity> limit = getWorkerResource(type);

        client.replicationControllers().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(workControllerName).endMetadata()
                .editOrNewSpec().withReplicas(workReplicaCount).withSelector(selector).editOrNewTemplate().editOrNewMetadata().addToLabels(selector).endMetadata()
                .editOrNewSpec().addNewContainer().withName(workContainerName).withImage(image).withCommand(command).withPorts(port)
                .editOrNewResources()/*.addToRequests(requests)*/.addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
    }

    @Override
    public void editWorkController(String namespaceName, int containerCount, long resourceTypeId, long oldResourceTypeId) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();
        String workControllerName = DEFAULT_SPARK_NAME + WORK_INFO + CONTROLLER_INFO;

        /** can`t change resource type */
        // update resource type
//        if(resourceTypeId != oldResourceTypeId) {
//
//            AdsContainerResourceType type = containerResourceTypeService.getResourceTypeById(resourceTypeId);
//
//            Map<String, Quantity> requests = getWorkerResource(type);
//            Map<String, Quantity> limit = getWorkerResource(type);
//
//            client.replicationControllers().inNamespace(namespaceName).withName(workControllerName).edit().editSpec()
//                    .editTemplate().editSpec().addNewContainer().editResources()
//                    .removeFromRequests(RESOURCE_CPU_KEY).removeFromRequests(RESOURCE_MEM_KEY).addToRequests(requests)
//                    .removeFromLimits(RESOURCE_CPU_KEY).removeFromLimits(RESOURCE_MEM_KEY).addToLimits(limit)
//                    .endResources().endContainer().endSpec().endTemplate().endSpec().done();
//        }

        /** change the work count*/
        client.replicationControllers().inNamespace(namespaceName).withName(workControllerName)
                .scale(containerCount);
    }

    /**
     kind: ReplicationController
     apiVersion: v1
     metadata:
     name: spark-thriftserver-controller
     spec:
     replicas: 1
     selector:
     component: spark-thriftserver
     template:
     metadata:
     labels:
     component: spark-thriftserver
     spec:
     containers:
     - name: spark-worker
     image: jcloud.com/xdata/spark:2.1.1
     command: ["/home/hadoop/start-thriftserver"]
     ports:
     - containerPort: 10000
     resources:
     requests:
     cpu: 100m
     */
    @Override
    public void createThriftServerController(String namespaceName) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        String thriftServerControllerName = DEFAULT_SPARK_NAME + THRIFT_SERVER_INFO + CONTROLLER_INFO;
        String thriftServerContainerName = DEFAULT_SPARK_NAME + THRIFT_SERVER_INFO;

        int thriftServerControllerReplicaCount = DEFAULT_THRIFT_SERVER_REPLICA;
        Map<String, String> selector = getThriftSelector(thriftServerContainerName);
        String image = DEFAULT_IMAGE;
        String command = DEFAULT_THRIFT_SERVER_COMMAND;
        ContainerPort port = getDefaultThriftServerContainerPort();
        Map<String, Quantity> requests = getDefaultResource();
        Map<String, Quantity> limit = getDefaultResource();

        client.replicationControllers().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(thriftServerControllerName).endMetadata()
                .editOrNewSpec().withReplicas(thriftServerControllerReplicaCount).withSelector(selector).editOrNewTemplate().editOrNewMetadata().addToLabels(selector).endMetadata()
                .editOrNewSpec().addNewContainer().withName(thriftServerContainerName).withImage(image).withCommand(command).withPorts(port)
                .editOrNewResources().addToRequests(requests).addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
    }

    private Map<String, Quantity> getWorkerResource(AdsContainerResourceType type) {

        int cpu = type.getContainerCores();
        int mem = type.getContainerMemory();

        Map<String, Quantity> resource = Maps.newHashMap();
        resource.put(RESOURCE_CPU_KEY, new Quantity(cpu + RESOURCE_UNIT));
        resource.put(RESOURCE_MEM_KEY, new Quantity(mem + RESOURCE_UNIT));
        return resource;
    }

    private Map<String, Quantity> getDefaultResource() {

        Map<String, Quantity> resource = Maps.newHashMap();
        resource.put(RESOURCE_CPU_KEY, new Quantity(defaultResourceWorkerCpuUnitSize));
        resource.put(RESOURCE_MEM_KEY, new Quantity(defaultResourceMemoryUnitSize));
        return resource;
    }

    private ContainerPort getDefaultSparkContainerPort() {

        ContainerPort sparkPort = new ContainerPort();
        sparkPort.setContainerPort(defaultContainerSparkrPort);
        return sparkPort;
    }

    private ContainerPort getDefalutHttpContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(defaultContainerHttpPort);
        return httpPort;
    }

    private ContainerPort getDefaultThriftServerContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(defaultContainerThriftPort);
        return httpPort;
    }

    private ContainerPort getDefaultWorkContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(defaultContainerWorkerPort);
        return httpPort;
    }
}
