package org.qihua.domain.strategy.service.armory;

import java.util.Date;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 13:34:28
 */
public interface IStrategyDispatch {
    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 权重ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId, String ruleModels);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param key = strategyId + _ + ruleWeightValue；
     * @return 抽奖结果
     */
    Integer getRandomAwardId(String key);

    /**
     * 策略ID和奖品ID，扣减奖品缓存库存
     * @param strategyId
     * @param awardId
     * @param endTime
     * @return
     */
    Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endTime);

}
