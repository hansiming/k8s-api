package com.jd.service;

/**
 * Created by hansiming on 2017/7/18.
 */
public interface K8sNamespaceService {

    void createNamespace(String namespaceName) throws Exception;

    void deleteNamespace(String namespaceName) throws Exception;
}
