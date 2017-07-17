package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/14.
 */
public interface KuberneteService {

    ReturnMessage createResource(String userName, String resourceName, int containerCount);
}
