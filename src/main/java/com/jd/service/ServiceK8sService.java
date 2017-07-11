package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/11.
 */
public interface ServiceK8sService {

    ReturnMessage getServices();

    ReturnMessage getServicesByNamespaceName(String namespaceName);

    ReturnMessage create(String namespaceName, String serviceName, String labelKey, String labelValue);

    ReturnMessage edit(String namespaceName, String serviceName, String labelKey, String labelValue);

    ReturnMessage del(String namespaceName, String serviceName);
}
