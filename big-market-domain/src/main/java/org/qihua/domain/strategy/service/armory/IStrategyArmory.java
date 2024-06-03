package org.qihua.domain.strategy.service.armory;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 13:34:09
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleStrategy(Long strategyId);
}
