package com.jd.service.impl;

import com.google.common.collect.Maps;
import com.jd.service.K8sControllerService;
import com.jd.util.K8sClientUtil;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.jd.model.K8sConstant.*;
import static com.jd.util.K8sClientUtil.*;

/**
 * Created by hansiming on 2017/7/17.
 */
@Service
public class K8sControllerServiceImpl implements K8sControllerService {

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
    public void createMasterController(String namespaceName, String resourceName) throws Exception {

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
                .editOrNewResources().addToRequests(requests)/*.addToLimits(limit)*/.endResources().endContainer().endSpec().endTemplate().endSpec().done();
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
    public void createWorkController(String namespaceName, String resourceName, int containerCount) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        String workControllerName = DEFAULT_SPARK_NAME + WORK_INFO + CONTROLLER_INFO;
        String workContainerName = DEFAULT_SPARK_NAME + WORK_INFO;

        int workReplicaCount = containerCount;
        Map<String, String> selector = getWorkerSelector(workContainerName);
        String image = DEFAULT_IMAGE;
        String command = DEFAULT_WORKER_COMMAND;
        ContainerPort port = getDefaultWorkContainerPort();
        Map<String, Quantity> requests = getDefaultResource();
        Map<String, Quantity> limit = getDefaultResource();

        client.replicationControllers().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(workControllerName).endMetadata()
                .editOrNewSpec().withReplicas(workReplicaCount).withSelector(selector).editOrNewTemplate().editOrNewMetadata().addToLabels(selector).endMetadata()
                .editOrNewSpec().addNewContainer().withName(workContainerName).withImage(image).withCommand(command).withPorts(port)
                .editOrNewResources().addToRequests(requests)/*.addToLimits(limit)*/.endResources().endContainer().endSpec().endTemplate().endSpec().done();
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
    public void createThriftServerController(String namespaceName, String resourceName) throws Exception {

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
                .editOrNewResources().addToRequests(requests)/*.addToLimits(limit)*/.endResources().endContainer().endSpec().endTemplate().endSpec().done();
    }

    private Map<String, Quantity> getDefaultResource() {

        Map<String, Quantity> resource = Maps.newHashMap();
        resource.put(RESOURCE_CPU_KEY, new Quantity(RESOURCE_CPU_VALUE));
        //resource.put(RESOURCE_MEM_KEY, new Quantity(RESOURCE_MEM_VALUE));
        return resource;
    }

    private ContainerPort getDefaultSparkContainerPort() {

        ContainerPort sparkPort = new ContainerPort();
        sparkPort.setContainerPort(CONTAINER_DEFAULT_SPARK_PORT);
        return sparkPort;
    }

    private ContainerPort getDefalutHttpContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(CONTAINER_DEFAULT_HTTP_PORT);
        return httpPort;
    }

    private ContainerPort getDefaultThriftServerContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(CONTAINER_DEFAULT_THRIFT_PORT);
        return httpPort;
    }

    private ContainerPort getDefaultWorkContainerPort() {

        ContainerPort httpPort = new ContainerPort();
        httpPort.setContainerPort(CONTAINER_DEFAULT_WORKER_PORT);
        return httpPort;
    }
}
