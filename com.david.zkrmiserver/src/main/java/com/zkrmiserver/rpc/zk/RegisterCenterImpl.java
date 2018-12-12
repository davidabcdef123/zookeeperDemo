package com.zkrmiserver.rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * Created by sc on 2018/12/11.
 */
public class RegisterCenterImpl implements IRegisterCenter {

    private CuratorFramework curatorFramework;

    {
        curatorFramework= CuratorFrameworkFactory.builder().connectString(ZkConfig.CONNNECTION_STR).sessionTimeoutMs(4000).
                retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();
    }
    @Override
    public void register(String serviceName, String serviceAddress) {
        //注册相应的服务
        String servicePath=ZkConfig.ZK_REGISTER_PATH+"/"+serviceName;
        try {
            if(curatorFramework.checkExists().forPath(servicePath)==null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }

            String addressPath=servicePath+"/"+serviceAddress;
            String rsNode= curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(addressPath,"0".getBytes());
            System.out.println("服务注册成功:"+rsNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
