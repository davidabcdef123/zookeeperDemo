package com.client.demo.defCon;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sc on 2018/12/11.
 */
public class ConnecttionDemo {

    public static void main(String[] args) throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.80.135:2181,192.168.80.136:2181,192.168.80.137:2181", 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("进入监听");
                if(Event.KeeperState.SyncConnected==event.getState()){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
        String defintion="/zk-test201812111";
        zooKeeper.create(defintion, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        Stat stat=new Stat();
        //获取节点数据
        byte[] bytes = zooKeeper.getData(defintion, null, stat);
        System.out.println(new String(bytes));

        //修改节点数据
        System.out.println("修改之前stat="+stat.getVersion());
        Stat stat1=zooKeeper.setData(defintion, "1".getBytes(), stat.getVersion());

        System.out.println("修改之后stat="+stat1.getVersion());
        System.out.println("修改之后zxid="+stat1.getMzxid());

        zooKeeper.delete(defintion,stat1.getVersion());
        zooKeeper.close();

    }
}
