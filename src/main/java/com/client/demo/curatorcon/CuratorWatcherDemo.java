package com.client.demo.curatorcon;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by sc on 2018/12/11.
 */
public class CuratorWatcherDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.80.135:2181,192.168.80.136:2181,192.168.80.137:2181")
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("curatorWatch").build();
        curatorFramework.start();
        String definitionPath = "/test1";
        //当前节点的创建和删除(必须在程序启动前就已经存在节点才能监听到删除)事件监听 --添加这个节点和修改会有监听，添加子节点貌似没有
        //addListenerWithNodeCache(curatorFramework,definitionPath);
        //子节点的增加、修改、删除的事件监听
        //addListenerWithPathChildCache(curatorFramework,definitionPath);
        //综合节点监听事件
        addListenerWithTreeCache(curatorFramework,definitionPath);

        System.in.read();

    }

    private static void addListenerWithTreeCache(CuratorFramework curatorFramework, String definitionPath) throws Exception {
        TreeCache treeCache = new TreeCache(curatorFramework, definitionPath);
        TreeCacheListener treeCacheListener=new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println(event.getType()+"->"+event.getData().getPath());
            }
        };
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();
    }


    /**
     * PathChildCache 监听一个节点下子节点的创建、删除、更新
     * NodeCache  监听一个节点的更新和创建事件
     * TreeCache  综合PatchChildCache和NodeCache的特性
     */
    public static void addListenerWithPathChildCache(CuratorFramework curatorFramework,String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener=new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("child redeive event:"+event.getType());
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }

    /**
    * Author: sc
    * Since: 2018/12/11
    * Describe:增加监听
    * Update: [变更日期YYYY-MM-DD][更改人姓名][变更描述]
    */
    public static void addListenerWithNodeCache(CuratorFramework curatorFramework,String path) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,path,false);
        NodeCacheListener nodeCacheListener=new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("receive event:"+nodeCache.getCurrentData().getPath());
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }
}
