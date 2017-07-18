package com.jd.dao;

import com.jd.model.K8sResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by hansiming on 2017/7/14.
 */
@Repository
public interface K8sResourceDao {

    int insertResource(K8sResource k8sResource);

    int updateResource(K8sResource k8sResource);

    K8sResource selectResourceByResourceNameAndUserName(@Param("userName") String userName,@Param("resourceId") String resourceId);

    int deleteResource(K8sResource k8sResource);

    K8sResource selectResourceById(int resourceId);
}
