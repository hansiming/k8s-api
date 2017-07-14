package com.jd.test;

import com.jd.K8sApiApplication;
import com.jd.dao.K8sNamespaceDao;
import com.jd.model.K8sNamespace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

/**
 * Created by hansiming on 2017/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = K8sApiApplication.class)
@WebAppConfiguration
public class NamespaceTest {

    @Autowired
    private K8sNamespaceDao dao;

    @Test
    public void insert() {

        K8sNamespace namespace = new K8sNamespace();
        namespace.setUserName("哈哈");
        namespace.setNamespaceName(UUID.randomUUID().toString());
        dao.insertNamespace(namespace);
    }
}
