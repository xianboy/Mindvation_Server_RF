package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.CustomDeliveryRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.template.domain.entity.Delivery;

import java.util.List;

public interface DeliveryService{

    //给模板创建交付件
    List<Delivery> create(Long creatorId, String hostSerialNo, List<Delivery> delivery);

    //自定义交付件
    Long create(CustomDeliveryRequest customRequest);

    //根据hostSerialNo和isDeleted获取交付件
    List<Delivery> retrieveDeliveriesByHostSerialNo(String hostSerialNo, Integer isDeleted);
}
