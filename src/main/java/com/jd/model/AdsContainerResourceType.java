package com.jd.model;

import java.math.BigDecimal;

public class AdsContainerResourceType {
    private Long id;

    private String resourceName;

    private String resourceDesc;

    private Integer containerCores;

    private Integer containerMemory;

    private String createTime;

    private String lastUpdateTime;

    private BigDecimal price;

    private String priceView;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public Integer getContainerCores() {
        return containerCores;
    }

    public void setContainerCores(Integer containerCores) {
        this.containerCores = containerCores;
    }

    public Integer getContainerMemory() {
        return containerMemory;
    }

    public void setContainerMemory(Integer containerMemory) {
        this.containerMemory = containerMemory;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceView() {
        return priceView;
    }

    public void setPriceView(String priceView) {
        this.priceView = priceView;
    }
}