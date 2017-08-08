package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/26.
 */
public interface K8sResourceService {

    ReturnMessage getResource(String userName, String resourceName);

    ReturnMessage createResource(String userName, String resourceName, int containerCount, long resourceTypeId, String useTime, String useType);

    ReturnMessage deleteResource(String userName, int resourceId);

    ReturnMessage editResource(String userName, int resourceId, int containerCount, long resourceTypeId);
}