package com.jd.service;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sServiceService {

    void createMasterService(String namespaceName, String resourceName, int id) throws Exception ;

    void createThriftServerService(String namespaceName, String resourceName, int id) throws Exception ;
}
