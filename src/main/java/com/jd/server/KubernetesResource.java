package com.jd.server;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jd.service.K8sResourceService;
import com.jd.util.ReturnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by hansiming on 2017/7/26.
 */
@Component
@Path("/ras/kubernetes")
public class KubernetesResource {

    @Autowired
    private K8sResourceService k8sResourceService;

    private Gson gson = new Gson();

    /**
     * 得到k8s资源
     * @param userName 用户名
     * @param resourceName 资源名称
     * @return
     */
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getResource(@QueryParam("userName") String userName,
                              @QueryParam("resourceName") String resourceName) {


        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        if(Strings.isNullOrEmpty(resourceName))
            return gson.toJson(new ReturnMessage(false, "resource name is empty"));

        return gson.toJson(k8sResourceService.getResource(userName, resourceName));
    }

    /**
     * 通过k8s创建一个新的集群
     * @param userName
     * @param resourceTypeId
     * @param useTime
     * @param useType
     * @param containerCount
     * @param description
     * @return
     */
    @POST
    @Path("/create/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createResources(@PathParam("userName") String userName,
                                  @FormParam("resourceTypeId") Long resourceTypeId,
                                  @FormParam("useTime") String useTime,
                                  @FormParam("useType") String useType,
                                  @FormParam("containerCount") Integer containerCount,
                                  @FormParam("description") String description) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        if(Strings.isNullOrEmpty(description))
            return gson.toJson(new ReturnMessage(false, "description is empty"));

        try {
            Integer.parseInt(useTime);
        }
        catch (NumberFormatException e) {
            return gson.toJson(ReturnMessage.paramError("userTime"));
        }
        if (containerCount < 5) {
            return gson.toJson(ReturnMessage.error("购买的资源数据量至少为5"));
        }
        if (containerCount > 20) {
            return gson.toJson(ReturnMessage.error("购买的资源数据量不能超过20"));
        }

        try {
            return gson.toJson(k8sResourceService.createResource(userName, description, containerCount, resourceTypeId, useTime, useType));
        } catch (Exception e) {
            return gson.toJson(ReturnMessage.error(e.getMessage()));
        }
    }

    /**
     * 删除资源
     * @param userName
     * @param resourceId
     * @return
     */
    @GET
    @Path("/delete/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteResources(@PathParam("userName") String userName,
                                  @QueryParam("id") int resourceId) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        return gson.toJson(k8sResourceService.deleteResource(userName, resourceId));
    }

    /**
     * 通过k8s来扩容
     * @param userName
     * @param resourceId
     * @param containerCount 修改后的集群数量
     * @return
     */
    @POST
    @Path("/update/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String executeResources(@PathParam("userName") String userName,
                                   @FormParam("id") int resourceId,
                                   @FormParam("resourceTypeId") Long resourceTypeId,
                                   @FormParam("containerCount") int containerCount) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        if(containerCount < 0)
            return gson.toJson(new ReturnMessage(false, "container count is less than 0"));

        return gson.toJson(k8sResourceService.editResource(userName, resourceId, containerCount, resourceTypeId));
    }
}

