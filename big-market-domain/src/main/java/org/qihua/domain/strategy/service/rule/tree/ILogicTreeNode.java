package org.qihua.domain.strategy.service.rule.tree;

import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author Lime
 * @description 规则树接口
 * @date 2024-06-10 11:23:04
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue);
}
