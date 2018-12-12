package com.zkrmiserver.rpc.zk;

import com.zkrmiserver.rpc.anno.RpcAnnotation;
import com.zkrmiserver.rpc.zk.IRegisterCenter;
import com.zkrmiserver.rpc.zk.ProcessorHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sc on 2018/12/12.
 * 用于发布一个远程服务
 */
public class RpcServer {

    //创建一个线程池
    private static final ExecutorService executorService= Executors.newCachedThreadPool();

    private IRegisterCenter registerCenter; //注册中心
    private String serviceAddress; //服务发布地址

    // 存放服务名称和服务对象之间的关系key=serviceClassName,value="127.0.0.1:8080"服务地址
    Map<String,Object> handlerMap=new HashMap<>();

    public RpcServer(IRegisterCenter registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
    }

    /**
    * Author: sc
    * Since: 2018/12/12
    * Describe:绑定服务名称和服务对象
    * Update: [变更日期YYYY-MM-DD][更改人姓名][变更描述]
    */
    public void bind(Object...services){
        for (int i = 0; i < services.length; i++) {
            Object service = services[i];
            RpcAnnotation rpcAnnotation=service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName=rpcAnnotation.value().getName();
            String version=rpcAnnotation.version();
            if(version!=null && !version.equals("")){
                serviceName=serviceName+"-"+version;
            }
            handlerMap.put(serviceName,service);//绑定服务接口名称对应的服务
        }
    }

    public void publisher(){
        ServerSocket serverSocket=null;
        String[] addrs=serviceAddress.split(":");
        try {
            serverSocket = new ServerSocket(Integer.parseInt(addrs[1]));
            for(String interfaceName:handlerMap.keySet()){
                registerCenter.register(interfaceName,serviceAddress);
                System.out.println("注册服务成功："+interfaceName+"->"+serviceAddress);
            }
            while (true){//循环监听
                Socket socket=serverSocket.accept();
                //通过线程池去处理请求
                executorService.execute(new ProcessorHandler(socket,handlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
