package com.jd.service;

import com.jd.util.ReturnMessage;

/**
 * Created by hansiming on 2017/7/10.
 */
public interface NamespaceService {

    ReturnMessage getNamespaces();

    ReturnMessage getNamespaceByName(String namespaceName);

    ReturnMessage create(String namespaceName, String labelKey, String labelValue);

    ReturnMessage del(String namespaceName);
}
