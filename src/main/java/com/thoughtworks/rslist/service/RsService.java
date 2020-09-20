package com.thoughtworks.rslist.service;

import java.util.Arrays;
import java.util.List;

public class RsService {
    private final List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");

    public List<String> getList() {
        return rsList;
    }
}
