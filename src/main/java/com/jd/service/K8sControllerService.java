package com.jd.service;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sControllerService {

    void createMasterController(String namespaceName) throws Exception;

    void createWorkController(String namespaceName, int containerCount) throws Exception ;

    void editWorkController(String namespaceName, int containerCount) throws Exception ;

    void createThriftServerController(String namespaceName) throws Exception ;
}
