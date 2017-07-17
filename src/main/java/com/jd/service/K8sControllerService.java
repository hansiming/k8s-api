package com.jd.service;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sControllerService {

    void createMasterController(String namespaceName, String resourceName) throws Exception;

    void createWorkController(String namespaceName, String resourceName, int containerCount) throws Exception ;

    void createThriftServerController(String namespaceName, String resourceName) throws Exception ;
}
