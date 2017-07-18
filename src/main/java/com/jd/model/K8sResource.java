package com.jd.model;

/**
 * Created by hansiming on 2017/7/14.
 */
public class K8sResource {

    private int id;
    private String userName;
    private String namespaceName;
    private String resourceName;
    private int thriftServerNodePort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getThriftServerNodePort() {
        return thriftServerNodePort;
    }

    public void setThriftServerNodePort(int thriftServerNodePort) {
        this.thriftServerNodePort = thriftServerNodePort;
    }
}
