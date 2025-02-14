package org.qihua.domain.strategy.service.rule.tree.factory.engine;

import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author Lime
 * @description 规则树组合接口
 * @date 2024-06-10 11:53:34
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);

}
