//package com.jd.service.impl;
//
//import com.jd.dao.K8sNamespaceDao;
//import com.jd.model.K8sResource;
//import com.jd.service.KuberneteService;
//import com.jd.util.K8sClientUtil;
//import com.jd.util.ReturnMessage;
//import io.fabric8.kubernetes.api.model.ContainerPort;
//import io.fabric8.kubernetes.api.model.Quantity;
//import io.fabric8.kubernetes.client.KubernetesClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.UUID;
//
//import static com.jd.util.K8sClientUtil.*;
//
///**
// * Created by hansiming on 2017/7/14.
// */
//@Service
//public class KubernetesServiceImpl implements KuberneteService {
//
//    private KubernetesClient client = K8sClientUtil.getKubernetesClient();
//
//    @Value("${api_version}")
//    private String apiVersion;
//
//    @Autowired
//    private K8sNamespaceDao k8sNamespaceDao;
//
//    @Override
//    public ReturnMessage createResource(String userName, String resourceName, int containerCount) {
//
//        //1.创建一个uuid作为namespaceName
//        String namespaceName = UUID.randomUUID().toString();
//        K8sResource k8sNamespace = new K8sResource();
//        k8sNamespace.setUserName(userName);
//        k8sNamespace.setNamespaceName(namespaceName);
//        k8sNamespaceDao.insertNamespace(k8sNamespace);
//
//        try{
//
//            //2.创建一个namespace
//            createNamespace(namespaceName);
//
//            //3.创建一个MasterController
//            createMasterController(namespaceName, resourceName);
//
//            //4.创建一个MasterService
//            createMasterService(namespaceName, resourceName);
//
//            //5. 创建一个WorkController
//            createWorkController(namespaceName, resourceName);
//
//            //6. 创建一个ThriftServerController
//            createThriftServerController(namespaceName, resourceName);
//
//            //7. 创建一个ThriftServerService
//        } catch (Exception e) {
//
//            return new ReturnMessage(false, e.getMessage());
//        }
//
//        return null;
//    }
//
//    private void createNamespace(String namespaceName) throws Exception {
//
//        client.namespaces().createNew().withNewMetadata().withName(namespaceName).endMetadata().done();
//    }
//
//    /**
//     kind: ReplicationController
//     apiVersion: v1
//     metadata:
//        name: spark-master-controller
//     spec:
//        replicas: 1
//        selector:
//            component: spark-master
//        template:
//            metadata:
//                labels:
//                    component: spark-master
//            spec:
//                containers:
//                    - name: spark-master
//                    image: jcloud.com/xdata/spark:2.1.1
//                    command: ["/home/hadoop/start-master"]
//                ports:
//                    - containerPort: 7077
//                        - containerPort: 8080
//                resources:
//                    requests:
//                        cpu: 100m
//     */
//    private void createMasterController(String namespaceName, String resourceName) throws Exception {
//
//        //get controller name
//        String masterControllerName = getMasterReplicationControllerName(resourceName);
//
//        //make selector
//        Map<String, String> masterSelector = getMasterSelector(resourceName);
//
//        //make container Port
//        ContainerPort sparkPort = getDefaultSparkContainerPort();
//        ContainerPort httpPort = getDefalutHttpContainerPort();
//
//        //make resources request and limit
//        Map<String, Quantity> requests = getDefaultResource();
//        Map<String, Quantity> limit = getDefaultResource();
//
//        //TODO image and command
//        client.replicationControllers().inNamespace(namespaceName).createNew()
//                .editOrNewMetadata().withName(masterControllerName).endMetadata()
//                .editOrNewSpec().withReplicas(1).withSelector(masterSelector)
//                .editOrNewTemplate().withNewMetadata().addToLabels(masterSelector).endMetadata()
//                .editOrNewSpec().addNewContainer().withName(masterControllerName).withImage("").withCommand("").withPorts(sparkPort, httpPort)
//                .editOrNewResources().addToRequests(requests).addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
//    }
//
//    /**
//     kind: Service
//     apiVersion: v1
//     metadata:
//        name: spark-master
//     spec:
//        ports:
//            - port: 7077
//            targetPort: 7077
//            name: spark
//            - port: 8080
//            targetPort: 8080
//            name: http
//        selector:
//            component: spark-master
//     */
//    private void createMasterService(String namespaceName, String resourceName) {
//
//        //get master service name
//        String masterServiceName = getMasterServiceName(resourceName);
//
//        //get ports
//        int sparkPort = getDefaultSparkPort();
//        int httpPort = getDefaultHttpPort();
//        int sparkNodePort = getDefaultSparkNodePort();
//
//        //get default name
//        String sparkName = getDefaultSparkName();
//        String httpName = getDefaultHttpName();
//
//        //make selector
//        Map<String, String> masterSelector = getMasterSelector(resourceName);
//
//        client.services().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(masterServiceName).endMetadata()
//                .editOrNewSpec().addNewPort().withName(sparkName).withPort(sparkPort).withNewTargetPort(sparkPort).withNodePort(sparkNodePort).endPort()
//                .addNewPort().withName(httpName).withPort(httpPort).withNewTargetPort(httpPort).endPort()
//                .addToSelector(masterSelector).endSpec().done();
//    }
//
//    /**
//     apiVersion: v1
//     metadata:
//        name: spark-worker-controller
//     spec:
//        replicas: 4
//        selector:
//            component: spark-worker
//        template:
//            metadata:
//                labels:
//                    component: spark-worker
//            spec:
//                containers:
//                    - name: spark-worker
//                    image: jcloud.com/xdata/spark:2.1.1
//                    command: ["/home/hadoop/start-worker"]
//                    ports:
//                        - containerPort: 8081
//                    resources:
//                        requests:
//                            cpu: 100m
//     */
//    private void createWorkController(String namespaceName, String resourceName) {
//
//        String workControllerName = getWorkControllerName(resourceName);
//        int workReplicaCount = 4;
//        Map<String, String> selector = getWorkerSelector(resourceName);
//        String image = "";
//        String command = "";
//        ContainerPort port = null;
//        Map<String, Quantity> requests = getDefaultResource();
//        Map<String, Quantity> limit = getDefaultResource();
//
//        client.replicationControllers().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(workControllerName).endMetadata()
//                .editOrNewSpec().withReplicas(workReplicaCount).withSelector(selector).editOrNewTemplate().editOrNewMetadata().addToLabels(selector).endMetadata()
//                .editOrNewSpec().addNewContainer().withName(workControllerName).withImage(image).withCommand(command).withPorts(port)
//                .editOrNewResources().addToRequests(requests).addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
//    }
//
//    /**
//     kind: ReplicationController
//     apiVersion: v1
//     metadata:
//        name: spark-thriftserver-controller
//     spec:
//        replicas: 1
//        selector:
//            component: spark-thriftserver
//        template:
//            metadata:
//                labels:
//                    component: spark-thriftserver
//            spec:
//                containers:
//                    - name: spark-worker
//                    image: jcloud.com/xdata/spark:2.1.1
//                    command: ["/home/hadoop/start-thriftserver"]
//                ports:
//                    - containerPort: 10000
//                resources:
//                    requests:
//                        cpu: 100m
//     */
//    private void createThriftServerController(String namespaceName, String resourceName) {
//
//        String thriftServerConrollerName = "";
//        int thriftServerConrollerReplicaCount = 1;
//        Map<String, String> selector = null;
//        String image = "";
//        String command = "";
//        ContainerPort port = null;
//        Map<String, Quantity> requests = getDefaultResource();
//        Map<String, Quantity> limit = getDefaultResource();
//
//        client.replicationControllers().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(thriftServerConrollerName).endMetadata()
//                .editOrNewSpec().withReplicas(thriftServerConrollerReplicaCount).withSelector(selector).editOrNewTemplate().editOrNewMetadata().addToLabels(selector).endMetadata()
//                .editOrNewSpec().addNewContainer().withName(thriftServerConrollerName).withImage(image).withCommand(command).withPorts(port)
//                .editOrNewResources().addToRequests(requests).addToLimits(limit).endResources().endContainer().endSpec().endTemplate().endSpec().done();
//    }
//
//    /**
//     kind: Service
//     apiVersion: v1
//        metadata:
//            name: spark-thriftserver
//     spec:
//        type: NodePort
//        ports:
//            - port: 10000
//            targetPort: 10000
//            nodePort: 30022
//            name: spark
//        selector:
//            component: spark-thriftserver
//     */
//    private void createThriftServerService(String namespaceName, String resourceName) {
//
//        String thriftSeverServiceName = "";
//        String type = "";
//        int port = 1;
//        int targetPort = 1;
//        int nodePort = 1;
//        String name = "";
//        Map<String, String> selector = null;
//
//        client.services().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(thriftSeverServiceName).endMetadata()
//                .editOrNewSpec().withType(type).addNewPort().withPort(port).withNewTargetPort(targetPort).withNodePort(nodePort).withName(name).endPort()
//                .addToSelector(selector).endSpec().done();
//    }
//}
