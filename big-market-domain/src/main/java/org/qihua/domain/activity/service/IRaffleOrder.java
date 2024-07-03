package org.qihua.domain.activity.service;

import org.qihua.domain.activity.model.entity.ActivityOrderEntity;
import org.qihua.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @Author：Lime
 * @Description: 抽奖活动订单接口
 * @Date：2024/7/3 13:30
 */
public interface IRaffleOrder {

    /**
     * 以sku创建抽奖活动订单，获取参与资格
     *
     * @param activityShopCartEntity 活动通过sku领取活动
     * @return 参与记录实体
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
