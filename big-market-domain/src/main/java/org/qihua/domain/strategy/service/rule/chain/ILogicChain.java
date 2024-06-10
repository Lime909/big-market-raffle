package org.qihua.domain.strategy.service.rule.chain;

import org.qihua.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author Lime
 * @description 抽奖策略规则责任链接口
 * @date 2024-06-09 15:49:13
 */
public interface ILogicChain extends ILogicChainArmory{
    /**
     *
     * @param userId
     * @param strategyId
     * @return 奖品对象
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
