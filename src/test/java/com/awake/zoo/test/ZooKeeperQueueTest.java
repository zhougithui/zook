package com.awake.zoo.test;

import com.awake.zoo.queue.ZooKeeperQueue;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ZooKeeperQueueTest {

    @Test
    public void testQueue() throws InterruptedException, IOException, KeeperException {
        final ZooKeeperQueue queue = new ZooKeeperQueue("test");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 10; i++) {
                    try {
                        Thread.sleep(new Random(1).nextInt(100 * i));
                        queue.produce(("massive" + i).getBytes());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Produce-thread").start();


        TimeUnit.SECONDS.sleep(5);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.consume();
                        System.out.println("--------------------------------------------------------");
                        System.out.println();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        TimeUnit.SECONDS.sleep(5);
    }
}
