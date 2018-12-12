package com.zkrmiserver.rpc.zk;

import com.IHelloService;
import com.zkrmiserver.rpc.anno.RpcAnnotation;

/**
 * Created by sc on 2018/12/11.
 */
@RpcAnnotation(value = IHelloService.class)
public class HelloServiceImpl2 implements IHelloService {
    @Override
    public String sayHello(String msg) {
        return "I'm 8081 node ："+msg;
    }
}
