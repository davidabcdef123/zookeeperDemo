package com.client.demo.curatorcon;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by sc on 2018/12/11.
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.80.135:2181,192.168.80.136:2181,192.168.80.137:2181")
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("curatortest").build();
        curatorFramework.start();

        String beanDefi="/test1/child2";
        //原生api中，必须是逐层创建，也就是父节点必须存在，子节点才能创建
        //高版本的会有问题
        //curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(beanDefi,"9".getBytes());
        /*curatorFramework.create().creatingParentsIfNeeded().
                withMode(CreateMode.PERSISTENT).
                forPath(beanDefi,"1".getBytes());*/
        //curatorFramework.delete().deletingChildrenIfNeeded().forPath(beanDefi);

        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath(beanDefi);

        curatorFramework.setData().withVersion(stat.getVersion()).forPath(beanDefi, "1".getBytes());

        byte[] bytes=curatorFramework.getData().storingStatIn(stat).forPath(beanDefi);
        System.out.println(new String(bytes));
        curatorFramework.close();
    }
}
