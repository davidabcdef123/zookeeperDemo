package com.zkrmiserver.rpc.boostrap2;

import com.IHelloService;
import com.zkrmiserver.rpc.zk.RpcServer;
import com.zkrmiserver.rpc.zk.*;

import java.io.IOException;

/**
 * Created by sc on 2018/12/12.
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {
        IHelloService helloService = new HelloServiceImpl();
        IHelloService helloService2 = new HelloServiceImpl2();
        IRegisterCenter registerCenter=new RegisterCenterImpl();

        RpcServer rpcServer = new RpcServer(registerCenter, "127.0.0.1:8080");
        rpcServer.bind(helloService,helloService2);
        rpcServer.publisher();
        System.in.read();
    }
}
