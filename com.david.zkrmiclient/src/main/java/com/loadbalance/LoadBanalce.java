package com.loadbalance;

import java.util.List;

/**
 * Created by sc on 2018/12/12.
 */
public interface LoadBanalce {

    String selectHost(List<String> repos);
}
