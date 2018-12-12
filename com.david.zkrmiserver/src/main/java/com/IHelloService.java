package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by sc on 2018/12/11.
 */
public interface IHelloService extends Remote {

    String sayHello(String msg)throws RemoteException;
}
