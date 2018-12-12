package com.zkrmiserver.rpc.zk;

/**
 * Created by sc on 2018/12/11.
 */
public interface IRegisterCenter {

    /**
    * Author: sc
    * Since: 2018/12/11
    * Describe:注册服务名称和服务地址
    * Update: [变更日期YYYY-MM-DD][更改人姓名][变更描述]
    */
    void register(String serviceName,String serviceAddress);
}
