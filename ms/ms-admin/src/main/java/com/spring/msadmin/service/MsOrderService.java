package com.spring.msadmin.service;

import com.spring.msadmin.entity.msOrder;

import java.util.List;

public interface MsOrderService {
    public List<msOrder> getAll();
    public String getPidByName(String name);
    public Integer getSpendByName(String name);
    public int putUserOrder(String name, String pid, int spend);
}
