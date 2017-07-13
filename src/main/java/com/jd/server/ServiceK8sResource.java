package com.jd.server;

import com.google.gson.Gson;
import com.jd.service.ServiceK8sService;
import com.jd.util.ReturnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by hansiming on 2017/7/11.
 */
@Component
@Path("/service")
public class ServiceK8sResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceK8sResource.class);
    private static Gson gson = new Gson();

    @Autowired
    public ServiceK8sService service;

    @GET
    @Path("/getServices")
    public String getServices() {

        return gson.toJson(service.getServices());
    }

    @GET
    @Path("/getServicesByNamespaceName")
    public String getServicesByNamespaceName(@QueryParam("namespaceName") String namespaceName) {

        if(isBlank(namespaceName)) {
            return gson.toJson(new ReturnMessage(false, "namespaceName is empty"));
        }

        return gson.toJson(service.getServicesByNamespaceName(namespaceName));
    }

    @POST
    @Path("/create")
    public String create(@FormParam("namespaceName") String namespaceName, @FormParam("serviceName") String serviceName,
                         @FormParam("labelKey") String labelKey, @FormParam("labelValue") String labelValue) {

        if(isBlank(namespaceName)) {
            return gson.toJson(new ReturnMessage(false, "namespaceName is empty"));
        }

        if(isBlank(serviceName)) {
            return gson.toJson(new ReturnMessage(false, "serviceName is empty"));
        }

        if(isBlank(labelKey)) {
            return gson.toJson(new ReturnMessage(false, "labelKey is empty"));
        }

        if(isBlank(labelValue)) {
            return gson.toJson(new ReturnMessage(false, "labelKey is empty"));
        }

        return gson.toJson(service.create(namespaceName, serviceName, labelKey, labelValue));
    }
}
