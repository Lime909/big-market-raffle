package org.qihua.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

/**
 * @author Lime
 * @description 次数锁
 * @date 2024-06-10 12:09:43
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
