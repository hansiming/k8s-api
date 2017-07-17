package com.jd.model;

/**
 * Created by hansiming on 2017/7/14.
 */
public class K8sConstant {

    /** Info*/
    public static final String MASTER_INFO = "-master";
    public static final String WORK_INFO = "-worker";
    public static final String THRIFT_SERVER_INFO = "-thriftserver";
    public static final String CONTROLLER_INFO = "-controller";
    public static final String SERVICE_INFO = "-service";
    public static final String COMPONENT_INFO = "component";

    /** Resources*/
    public static final String RESOURCE_MEM_KEY = "memory";
    public static final String RESOURCE_MEM_VALUE = "1Gi";
    public static final String RESOURCE_CPU_KEY = "cpu";
    public static final String RESOURCE_CPU_VALUE = "100m";

    /** Ports */
    public static final int CONTAINER_DEFAULT_SPARK_PORT = 7077;
    public static final int CONTAINER_DEFAULT_HTTP_PORT = 8080;
    public static final int CONTAINER_DEFAULT_WORKER_PORT = 8081;
    public static final int CONTAINER_DEFAULT_THRIFT_PORT = 10000;
    public static final int CONTAINER_DEFAULT_THRIFT_NODE_PORT = 30022;
    public static final int CONTAINER_DEFAULT_SPARK_NODE_PORT = 32000;

    /** Names*/
    public static final String DEFAULT_SPARK_NAME = "spark";
    public static final String DEFAULT_HTTP_NAME = "http";

    /** Type*/
    public static final String DEFAULT_SERVICE_TYPE = "NodePort";

    /** Image And Command*/
    public static final String DEFAULT_IMAGE = "jcloud.com/xdata/spark:2.1.1";
    public static final String DEFAULT_MASTER_COMMAND = "/home/hadoop/start-master";
    public static final String DEFAULT_WORKER_COMMAND = "/home/hadoop/start-worker";
    public static final String DEFAULT_THRIFT_SERVER_COMMAND = "/home/hadoop/start-thriftserver";

    /** Replica*/
    public static final int DEFAULT_MASTER_REPLICA = 1;
    public static final int DEFAULT_WORKER_REPLICA = 4;
    public static final int DEFAULT_THRIFT_SERVER_REPLICA = 1;
}
