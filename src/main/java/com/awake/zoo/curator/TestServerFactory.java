package com.awake.zoo.curator;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestServerFactory {
    private static final Logger logger = LoggerFactory.getLogger(TestServerFactory.class);

    public static TestingServer instance(){
        InstanceSpec instanceSpec = new InstanceSpec(new File("/work/temp"),
                2181, 2888, 3888, false, 1, 1000,
                1000, null, "172.19.60.174");
        try {
            TestingServer server = new TestingServer(instanceSpec, false);
            return server;
        } catch (Exception e) {
            logger.error("初始化zk失败", e);
        }
        return null;
    }
}
