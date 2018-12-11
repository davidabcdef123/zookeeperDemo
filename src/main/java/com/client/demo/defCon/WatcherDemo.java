package com.client.demo.defCon;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sc on 2018/12/11.
 */
public class WatcherDemo {
    public static void main(String[] args) throws Exception {
        String defintion = "/zk-watch-test";
        Watcher constom=new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("我是自定义的的watch"+event.getType()+"->"+event.getPath()+"->"+event.getState());
            }
        };

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.80.135:2181,192.168.80.136:2181,192.168.80.137:2181", 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("我是默认的watch"+event.getType()+"->"+event.getPath()+"->"+event.getState());
                if(Event.KeeperState.SyncConnected==event.getState()){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();

        zooKeeper.create(defintion, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //exists  getdata getchildren 有监听的三个时间
        Stat stat=zooKeeper.exists(defintion, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("我是exists的watch"+event.getType()+"->"+event.getPath()+"->"+event.getState());
                //在此绑定
                try {
                    //zooKeeper.exists(event.getPath(), constom);//这样会执行自定义的zookeeper
                    zooKeeper.exists(event.getPath(), true);//这样会执行zookeeper里的参数的watch
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        stat = zooKeeper.setData(defintion, "0".getBytes(), stat.getVersion());
        Thread.sleep(2000);//没有这个看不到效果两次setData的效果
        stat = zooKeeper.setData(defintion, "1".getBytes(), stat.getVersion());
        zooKeeper.delete(defintion,stat.getVersion());
        zooKeeper.close();


    }

}
