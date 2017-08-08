//package com.jd.test;
//
//import com.jd.util.K8sClientUtil;
//import io.fabric8.kubernetes.client.KubernetesClient;
//import org.junit.Test;
//
///**
// * Created by hansiming on 2017/7/13.
// */
//public class ServiceTest {
//
//    @Test
//    public void testSelector() throws Exception {
//
//        KubernetesClient client = K8sClientUtil.getKubernetesClient();
//        client.services().inNamespace("").withName("").get().getSpec().setSelector(null);
//    }
//}
