package com.jd.server;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by hansiming on 2017/7/11.
 * Replication Controller Resource
 */
@Component
@Path("/RC")
public class RCResource {

    @GET
    @Path("/getRC")
    public String getRCs() {

        return "";
    }
}
