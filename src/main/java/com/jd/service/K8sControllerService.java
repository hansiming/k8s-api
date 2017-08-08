package com.jd.service;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sControllerService {

    void createMasterController(String namespaceName) throws Exception;

    void createWorkController(String namespaceName, int containerCount, long resourceTypeId) throws Exception ;

    void editWorkController(String namespaceName, int containerCount, long resourceTypeId, long oldResourceTypeId) throws Exception ;

    void createThriftServerController(String namespaceName) throws Exception ;
}
