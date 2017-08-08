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
    private String useTime;
    private String useType;
    private long resourceTypeId;
    private int containerCount;
    private String startTime;
    private String endTime;
    private String createTime;
    private String updateTime;

    public K8sResource() { }

    public K8sResource(String userName, String resourceName) {
        this.userName = userName;
        this.resourceName = resourceName;
    }

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

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount = containerCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "K8sResource{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", namespaceName='" + namespaceName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", thriftServerNodePort=" + thriftServerNodePort +
                ", useTime='" + useTime + '\'' +
                ", useType='" + useType + '\'' +
                ", resourceTypeId=" + resourceTypeId +
                ", containerCount=" + containerCount +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
