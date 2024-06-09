package org.qihua.domain.strategy.service.rule.chain;

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
     * @return awardId
     */
    Integer logic(String userId, Long strategyId);
}
