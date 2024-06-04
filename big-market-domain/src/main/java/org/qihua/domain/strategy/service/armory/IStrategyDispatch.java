package org.qihua.domain.strategy.service.armory;

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

    Integer getRandomAwardId(Long strategyId, String ruleModels);
}
