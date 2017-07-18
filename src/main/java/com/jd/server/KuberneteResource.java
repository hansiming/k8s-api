package com.jd.server;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jd.service.K8sResourceService;
import com.jd.util.ReturnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

/**
 * Created by hansiming on 2017/7/14.
 */
@Component
@Path("/ras/resources")
public class KuberneteResource {

    @Autowired
    private K8sResourceService service;

    private Gson gson = new Gson();

    @POST
    @Path("/create")
    public String createResources(@FormParam("userName") String userName,
                                  @FormParam("resourceName") String resourceName,
                                  @FormParam("containerCount") int containerCount) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        if(Strings.isNullOrEmpty(resourceName))
            return gson.toJson(new ReturnMessage(false, "resource name is empty"));

        if(containerCount < 0)
            return gson.toJson(new ReturnMessage(false, "container count is less than 0"));

        return gson.toJson(service.createResource(userName, resourceName, containerCount));
    }

    @GET
    @Path("/delete")
    public String deleteResources(@QueryParam("userName") String userName,
                                  @QueryParam("id") int resourceId) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        return gson.toJson(service.deleteResource(userName, resourceId));
    }

    @POST
    @Path("/execute")
    public String executeResources(@FormParam("userName") String userName,
                                  @FormParam("id") int resourceId,
                                  @FormParam("containerCount") int containerCount) {

        if(Strings.isNullOrEmpty(userName))
            return gson.toJson(new ReturnMessage(false, "user name is empty"));

        if(containerCount < 0)
            return gson.toJson(new ReturnMessage(false, "container count is less than 0"));

        return gson.toJson(service.editResource(userName, resourceId, containerCount));
    }
}
