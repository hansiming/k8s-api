package com.jd.dao;

import com.jd.model.K8sNamespace;
import org.springframework.stereotype.Repository;

/**
 * Created by hansiming on 2017/7/14.
 */
@Repository
public interface K8sNamespaceDao {

    K8sNamespace insertNamespace(K8sNamespace k8sNamespace);
}
