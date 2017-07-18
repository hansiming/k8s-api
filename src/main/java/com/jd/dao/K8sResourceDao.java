package com.jd.dao;

import com.jd.model.K8sResource;
import org.springframework.stereotype.Repository;

/**
 * Created by hansiming on 2017/7/14.
 */
@Repository
public interface K8sResourceDao {

    int insertResource(K8sResource k8sResource);

    int updateResource(K8sResource k8sResource);
}
