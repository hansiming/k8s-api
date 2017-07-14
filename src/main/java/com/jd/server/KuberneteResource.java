package com.jd.server;

import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.UUID;

/**
 * Created by hansiming on 2017/7/14.
 */
@Component
@Path("/ras/resources")
public class KuberneteResource {

    @POST
    @Path("/create")
    public String createResources() {

        //1.创建namespace的名字
        String uuid = UUID.randomUUID().toString();

        //2.启动master

        //3.启动worker

        //4.启动ThriftServer

        return "";
    }
}
