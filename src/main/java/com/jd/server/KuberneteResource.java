package com.jd.server;

import com.google.gson.Gson;
import com.jd.dao.K8sNamespaceDao;
import com.jd.service.K8sResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.UUID;

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

        return gson.toJson(service.createResource(userName, resourceName, containerCount));
    }
}
