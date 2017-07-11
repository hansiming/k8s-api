package com.jd.server;

import com.google.gson.Gson;
import com.jd.service.ServiceK8sService;
import com.jd.util.ReturnMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by hansiming on 2017/7/11.
 */
@Component
@Path("/service")
public class ServiceK8sResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResource.class);
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
    public String getServicesByNamespaceName(String namespaceName) {

        if(StringUtils.isBlank(namespaceName)) {
            return gson.toJson(new ReturnMessage(false, "namespaceName is empty"));
        }

        return gson.toJson(service.getServicesByNamespaceName(namespaceName));
    }

    @POST
    @Path("/create")
    public String create(String namespaceName, String serviceName, String labelKey, String labelValue) {

        if(StringUtils.isBlank(namespaceName)) {
            return gson.toJson(new ReturnMessage(false, "namespaceName is empty"));
        }

        if(StringUtils.isBlank(serviceName)) {
            return gson.toJson(new ReturnMessage(false, "serviceName is empty"));
        }

        if(StringUtils.isBlank(labelKey)) {
            return gson.toJson(new ReturnMessage(false, "labelKey is empty"));
        }

        if(StringUtils.isBlank(labelValue)) {
            return gson.toJson(new ReturnMessage(false, "labelKey is empty"));
        }

        return gson.toJson(service.create(namespaceName, serviceName, labelKey, labelValue));
    }
}
