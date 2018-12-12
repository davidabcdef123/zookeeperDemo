package com.zkrmiclient;

import com.loadbalance.LoadBanalce;
import com.loadbalance.RandomLoadBanalce;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sc on 2018/12/12.
 */
public class ServiceDiscoverImpl implements IServiceDiscovery{

    //地址
    List<String> repos = new ArrayList<>();

    //zookeeper地址
    private String addrdss;

    private CuratorFramework curatorFramework;

    public ServiceDiscoverImpl(String addrdss) {
        this.addrdss = addrdss;
        curatorFramework=CuratorFrameworkFactory.builder().connectString(addrdss).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();
    }

    @Override
    public String discover(String servidename) {
        String path=ZkConfig.ZK_REGISTER_PATH+"/"+servidename;
        try {
            repos=curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException("获取子节点异常："+e);
        }
        //动态发现服务节点的变化
        registerWatcher(path);

        //负载均衡
        LoadBanalce loadBanalce = new RandomLoadBanalce();


        return loadBanalce.selectHost(repos);
    }

    private void registerWatcher(String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener pathChildrenCacheListener=new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PatchChild Watcher 异常"+e);
        }

    }
}
