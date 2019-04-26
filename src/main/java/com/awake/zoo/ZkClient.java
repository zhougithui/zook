package com.awake.zoo;

import java.util.List;
import java.io.IOException;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkClient.class);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        // 创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
        ZooKeeper zk = new ZooKeeper("172.19.60.174:2181,172.19.60.174:2182,172.19.60.174:2183", 500000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                // dosomething
                System.out.println("watcher event fire...");
            }
        });

        monitor(zk);

        // 创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
        zk.create("/root", "mydata".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
        zk.create("/root/childone", "childone".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 取得/root节点下的子节点名称,返回List<String>
        List<String> rootItems = zk.getChildren("/root", true);
        System.out.println("print /root...");
        for (String item : rootItems) {
            System.out.println(item);
        }
        // 取得/root/childone节点下的数据,返回byte[]
        byte[] bytes = zk.getData("/root/childone", true, null);
        System.out.println(new String(bytes));

        // 修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
        zk.setData("/root/childone", "childonemodify".getBytes(), -1);

        // 删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
        zk.delete("/root/childone", -1);
        zk.delete("/root", -1);

        // 关闭session
        zk.close();
    }

    public static void monitor(ZooKeeper zk){
        zk.exists("/root", (event) -> {
                logger.info(event.toString());
            }, (rc1, path1, ctx1, stat1) -> {
                logger.info(ctx1.getClass().getName() + "&&&" + rc1 + "###" + path1 + "$$$" + (stat1 != null ? stat1.getVersion() : null));
                monitor(zk);
            }, new ZkContext(1)
        );
    }
}