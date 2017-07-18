package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/17.
 */
public interface K8sResourceService {

    ReturnMessage createResource(String userName, String resourceName, int containerCount);

    ReturnMessage deleteResource(String userName, String resourceName);
}
