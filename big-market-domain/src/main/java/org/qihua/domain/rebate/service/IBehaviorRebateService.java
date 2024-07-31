package org.qihua.domain.rebate.service;

import org.qihua.domain.rebate.model.entity.BehaviorEntity;
import org.qihua.domain.rebate.model.entity.BehaviorRebateOrderEntity;

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

    /**
     * 根据外部单号查询订单
     *
     * @param userId        用户ID
     * @param outBusinessNo 业务ID；签到则是日期字符串，支付则是外部的业务ID
     * @return 返利订单实体
     */
    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo);

}
