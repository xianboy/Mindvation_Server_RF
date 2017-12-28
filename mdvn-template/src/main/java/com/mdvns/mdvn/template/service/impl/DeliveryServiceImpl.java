package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.CustomDeliveryRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.template.domain.entity.Delivery;
import com.mdvns.mdvn.template.repository.DeliveryRepository;
import com.mdvns.mdvn.template.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger LOG = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    @Resource
    private DeliveryRepository deliveryRepository;

    /**
     * 创建交付件
     *
     * @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @param deliveries deliveries
     * @return List<Delivery>
     */
    @Override
    public List<Delivery> create(Long creatorId, String hostSerialNo, List<Delivery> deliveries) {
        LOG.info("新建模板交付件开始...");
        List<Delivery> list = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            delivery.setCreatorId(creatorId);
            delivery.setHostSerialNo(hostSerialNo);
            delivery.setSerialNo(buildSerialNo(hostSerialNo));
            list.add(delivery);
        }
        list = this.deliveryRepository.save(list);
        LOG.info("新建模板交付件成功...");
        return list;
    }

    /**自定义交付件
     * @param customRequest request
     * @return Delivery
     */
    @Override
    public Long create(CustomDeliveryRequest customRequest) {
        LOG.info("自定义交付件开始...");
        List<Delivery> deliveries = this.deliveryRepository.findByHostSerialNoAndIsDeleted(customRequest.getHostSerialNo(), MdvnConstant.ZERO);
        if (!deliveries.isEmpty()) {
            this.deliveryRepository.delete(deliveries);
        }
        Delivery delivery = new Delivery();
        delivery.setCreatorId(customRequest.getCreatorId());
        delivery.setHostSerialNo(customRequest.getHostSerialNo());
        delivery.setSerialNo(buildSerialNo(customRequest.getHostSerialNo()));
        delivery.setName(customRequest.getName());
        delivery.setTypeId(customRequest.getTypeId());
        delivery = this.deliveryRepository.saveAndFlush(delivery);
        LOG.info("自定义交付件成功, id为【{}】", delivery.getId());
        return delivery.getId();
    }

    /**
     * 构建template编号
     *
     * @param hostSerialNo 上一层关联对象编号
     * @return 编号
     */
    private String buildSerialNo(String hostSerialNo) {
        //查询表中的最大id  maxId
        Long maxId = this.deliveryRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += MdvnConstant.ONE;
        return hostSerialNo + MdvnConstant.DASH + MdvnConstant.D + maxId;
    }


}
