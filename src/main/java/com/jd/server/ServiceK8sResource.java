package com.jd.server;

import com.jd.service.ServiceK8sService;
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

    @Autowired
    public ServiceK8sService service;

    @GET
    @Path("/getServices")
    public String getServices() {

        return "";
    }
}
