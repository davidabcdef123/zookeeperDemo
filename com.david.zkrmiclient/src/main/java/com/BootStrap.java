package com;

import com.zkrmiclient.*;

/**
 * Created by sc on 2018/12/12.
 */
public class BootStrap {

    public static void main(String[] args) throws Exception {
        //连接zookeeper
        IServiceDiscovery serviceDiscovery = new ServiceDiscoverImpl(ZkConfig.CONNNECTION_STR);

        //代理类
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);

        for(int i=0;i<10;i++){
            IHelloService helloService = rpcClientProxy.clientProxy(IHelloService.class, null);
            System.out.println(helloService.sayHello("world"));
            Thread.sleep(1000);
        }
    }
}
