package com.zkrmiclient;

/**
 * Created by sc on 2018/12/12.
 * 服务发现
 */
public interface IServiceDiscovery {

    /**
    * Author: sc
    * Since: 2018/12/12
    * Describe:根据请求的服务地址，获得对应的调用地址
    * Update: [变更日期YYYY-MM-DD][更改人姓名][变更描述]
    */
    String discover(String serviceName);
}
