package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sResourceService {

    ReturnMessage getResource(String userName, String resourceName);

    ReturnMessage createResource(String userName, String resourceName, int containerCount);

    ReturnMessage deleteResource(String userName, int resourceId);

    ReturnMessage editResource(String userName, int resourceId, int containerCount);
}
