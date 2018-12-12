package com.zkrmiserver.rpc.boostrap;

import com.IHelloService;
import com.zkrmiserver.rpc.zk.HelloServiceImpl;
import com.zkrmiserver.rpc.zk.IRegisterCenter;
import com.zkrmiserver.rpc.zk.RegisterCenterImpl;
import com.zkrmiserver.rpc.zk.RpcServer;

/**
 * Created by sc on 2018/12/12.
 * //分别启动两个服务，每个服务中只有一个sevice
 */
public class LBServerDemo {

    public static void main(String[] args) throws Exception {
        //初始化服务
        IHelloService helloService=new HelloServiceImpl();
        //初始化注册中心
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        //服务对外类
        RpcServer rpcServer=new RpcServer(registerCenter,"127.0.0.1:8080");
        //绑定服务名称和地址，handlerMap的存放
        rpcServer.bind(helloService);
        //服务注册到zookeeper，并且serveraccept中
        rpcServer.publisher();
        System.in.read();
    }
}
