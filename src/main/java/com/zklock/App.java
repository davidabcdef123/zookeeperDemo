package com.zklock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 *只是个demo，并未实际应用，只是记录下代码
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        CountDownLatch countDownLatch=new CountDownLatch(10);
        for(int i=0;i<10;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                    DistributedLock distributedLock=new DistributedLock();
                    distributedLock.lock(); //获得锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
        System.in.read();
    }
}
