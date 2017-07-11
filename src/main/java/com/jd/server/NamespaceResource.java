package com.jd.server;

import com.google.gson.Gson;
import com.jd.service.NamespaceService;
import com.jd.util.ReturnResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by hansiming on 2017/7/10.
 */
@Component
@Path("/namespace")
public class NamespaceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResource.class);
    private static Gson gson = new Gson();

    @Autowired
    private NamespaceService service;

    @GET
    @Path("/getNamespaces")
    public String getNamespaces() {
        return gson.toJson(service.getNamespaces());
    }

    @GET
    @Path("/getNamespaceByName")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNamespaceByName(@QueryParam("namespaceName") String namespaceName) {

        if(StringUtils.isBlank(namespaceName)) {
            return gson.toJson(new ReturnResult(false, "namespaceName is empty", null));
        }

        return gson.toJson(service.getNamespaceByName(namespaceName));
    }

    @POST
    @Path("/createNamespace")
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@FormParam("namespaceName") String namespaceName, @FormParam("labelKey") String labelKey, @FormParam("labelValue") String labelValue) {

        if(StringUtils.isBlank(namespaceName)) {
            return gson.toJson(new ReturnResult(false, "namespaceName is empty", null));
        }

        if(StringUtils.isBlank(labelKey)) {
            return gson.toJson(new ReturnResult(false, "label key is empty", null));
        }

        if(StringUtils.isBlank(labelValue)) {
            return gson.toJson(new ReturnResult(false, "label value is empty", null));
        }

        return gson.toJson(service.create(namespaceName, labelKey, labelValue));
    }

    @POST
    @Path("/delNamespace")
    @Produces(MediaType.APPLICATION_JSON)
    public String del(@FormParam("namespaceName") String namespaceName) {

        if(StringUtils.isBlank(namespaceName)) {
            return gson.toJson(new ReturnResult(false, "namespaceName is empty", null));
        }

        return gson.toJson(service.del(namespaceName));
    }
}
