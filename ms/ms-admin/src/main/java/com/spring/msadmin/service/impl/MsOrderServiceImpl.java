package com.spring.msadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spring.msadmin.entity.msOrder;
import com.spring.msadmin.mapper.MsOrderMapper;
import com.spring.msadmin.service.MsOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MsOrderServiceImpl implements MsOrderService {

    @Resource
    private MsOrderMapper msOrderMapper;

    @Override
    public List<msOrder> getAll() {
        return msOrderMapper.selectList(null);
    }

    @Override
    public String getPidByName(String name) {
        return msOrderMapper.selectList(new QueryWrapper<msOrder>().eq("name",name)).get(0).getPid();
    }

    @Override
    public Integer getSpendByName(String name) {
        return msOrderMapper.selectList(new QueryWrapper<msOrder>().eq("name",name)).get(0).getSpend();
    }

    @Override
    public int putUserOrder(String name, String pid, int spend){
        msOrder userOrder = new msOrder(name,pid,spend);
        return msOrderMapper.insert(userOrder);
    }
}
