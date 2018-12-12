package com.loadbalance;

import java.util.List;

/**
 * Created by sc on 2018/12/12.
 */
public abstract class AbstractLoadBalance implements LoadBanalce{
    @Override
    public String selectHost(List<String> repos) {
        if(repos==null||repos.size()==0){
            return null;
        }
        if(repos.size()==1){
            return repos.get(0);
        }
        return doSelect(repos);
    }

    public abstract String doSelect(List<String> repos);
}
