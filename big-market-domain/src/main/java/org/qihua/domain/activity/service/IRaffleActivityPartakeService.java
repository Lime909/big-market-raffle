package org.qihua.domain.activity.service;

import org.qihua.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.qihua.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @Author：Lime
 * @Description: 抽奖活动参与服务
 * @Date：2024/7/12 10:45
 */
public interface IRaffleActivityPartakeService {

    /**
     * 创建抽奖单：用户参与抽奖活动，扣减活动账户库存，产生抽奖单，如果存在未使用的抽奖单就直接返回已存在的抽奖单。
     *
     * @param partakeRaffleActivityEntity
     * @return 抽奖订单对象
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
