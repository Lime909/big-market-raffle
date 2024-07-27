package org.qihua.domain.strategy.service;

import org.qihua.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author Lime
 * @description 策略奖品接口
 * @date 2024-06-13 00:39:40
 */
public interface IRaffleAward {

    /**
     * 根据策略ID查询抽奖奖品列表配置
     *
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

    /**
     * 根据活动ID查询抽奖奖品列表配置
     *
     * @param activityId 活动ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);

}
