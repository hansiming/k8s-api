package com.jd.service.impl;

import com.jd.service.K8sServiceService;
import com.jd.util.K8sClientUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.jd.model.K8sConstant.*;
import static com.jd.util.K8sClientUtil.*;

/**
 * Created by hansiming on 2017/7/17.
 */
@Service
public class K8sServiceServiceImpl implements K8sServiceService {

    /**
    kind: Service
    apiVersion: v1
    metadata:
        name: spark-master
    spec:
        ports:
            - port: 7077
            targetPort: 7077
            name: spark
            - port: 8080
            targetPort: 8080
            name: http
        selector:
            component: spark-master
     */
    @Override
    public void createMasterService(String namespaceName, String resourceName, int id) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        //get master service name
        String masterServiceName = DEFAULT_SPARK_NAME + MASTER_INFO + SERVICE_INFO;

        //get container name
        String masterContainerName = DEFAULT_SPARK_NAME + MASTER_INFO;

        //get ports
        int sparkPort = CONTAINER_DEFAULT_SPARK_PORT;
        int httpPort = CONTAINER_DEFAULT_HTTP_PORT;

        //暂时不暴露spark和http的接口
        // Tip 暴露spark端口，http端口也会默认暴露
//        int sparkNodePort = CONTAINER_DEFAULT_SPARK_NODE_PORT + id;
//        String type = DEFAULT_SERVICE_TYPE;

        //get default name
        String sparkName = DEFAULT_SPARK_NAME;
        String httpName = DEFAULT_HTTP_NAME;

        //make selector
        Map<String, String> masterSelector = getMasterSelector(masterContainerName);

        client.services().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(masterServiceName).endMetadata()
                .editOrNewSpec().addNewPort().withName(sparkName).withPort(sparkPort).withNewTargetPort(sparkPort).endPort()
                .addNewPort().withName(httpName).withPort(httpPort).withNewTargetPort(httpPort).endPort()
                .addToSelector(masterSelector).endSpec().done();
    }

    /**
     kind: Service
     apiVersion: v1
     metadata:
        name: spark-thriftserver
     spec:
        type: NodePort
        ports:
            - port: 10000
            targetPort: 10000
            nodePort: 30022
            name: spark
        selector:
            component: spark-thriftserver
     */
    @Override
    public void createThriftServerService(String namespaceName, String resourceName, int id) throws Exception {

        KubernetesClient client = K8sClientUtil.getKubernetesClient();

        String thriftSeverServiceName = DEFAULT_SPARK_NAME + THRIFT_SERVER_INFO + SERVICE_INFO;
        String workContainerName = DEFAULT_SPARK_NAME + WORK_INFO;

        String type = DEFAULT_SERVICE_TYPE;
        int port = CONTAINER_DEFAULT_THRIFT_PORT;
        int targetPort = CONTAINER_DEFAULT_THRIFT_PORT;
        int nodePort = CONTAINER_DEFAULT_THRIFT_NODE_PORT + id;
        String name = DEFAULT_SPARK_NAME;
        Map<String, String> selector = getThriftSelector(workContainerName);

        client.services().inNamespace(namespaceName).createNew().editOrNewMetadata().withName(thriftSeverServiceName).endMetadata()
                .editOrNewSpec().withType(type).addNewPort().withPort(port).withNewTargetPort(targetPort).withNodePort(nodePort).withName(name).endPort()
                .addToSelector(selector).endSpec().done();
    }
}
