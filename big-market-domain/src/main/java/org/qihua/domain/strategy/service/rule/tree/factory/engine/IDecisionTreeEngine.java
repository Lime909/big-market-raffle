package org.qihua.domain.strategy.service.rule.tree.factory.engine;

import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author Lime
 * @description 规则树组合接口
 * @date 2024-06-10 11:53:34
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
