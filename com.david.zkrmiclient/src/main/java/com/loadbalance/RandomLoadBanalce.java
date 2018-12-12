package com.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * Created by sc on 2018/12/12.
 */
public class RandomLoadBanalce extends AbstractLoadBalance {
    @Override
    public String doSelect(List<String> repos) {
        int len=repos.size();
        Random random = new Random();
        return repos.get(random.nextInt(len));
    }
}
