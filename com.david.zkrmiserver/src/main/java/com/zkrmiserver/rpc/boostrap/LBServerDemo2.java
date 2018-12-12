package com.zkrmiserver.rpc.boostrap;

import com.IHelloService;
import com.zkrmiserver.rpc.zk.HelloServiceImpl2;
import com.zkrmiserver.rpc.zk.IRegisterCenter;
import com.zkrmiserver.rpc.zk.RegisterCenterImpl;
import com.zkrmiserver.rpc.zk.RpcServer;

/**
 * Created by sc on 2018/12/12.
 * 分别启动两个服务，每个服务中只有一个sevice
 */
public class LBServerDemo2 {

    public static void main(String[] args) throws Exception {
        IHelloService helloService=new HelloServiceImpl2();
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        RpcServer rpcServer=new RpcServer(registerCenter,"127.0.0.1:8081");
        rpcServer.bind(helloService);
        rpcServer.publisher();
        System.in.read();
    }
}
