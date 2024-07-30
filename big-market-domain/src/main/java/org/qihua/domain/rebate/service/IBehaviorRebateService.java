package org.qihua.domain.rebate.service;

import org.qihua.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @Author: Lime
 * @Description: 行为返利服务
 * @Date: 2024/7/30 08:51
 */
public interface IBehaviorRebateService {

    /**
     * 创建行为动作的入账订单
     *
     * @param behaviorEntity
     * @return 订单ID
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);

}
