package com.jd.server;

import com.google.gson.Gson;
import com.jd.service.ServiceK8sService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
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
}
