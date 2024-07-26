package org.qihua.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lime
 * @description 次数锁
 * @date 2024-06-10 12:09:43
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

        Long raffleCount = 0L;
        try {
            raffleCount = Long.parseLong(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则过滤-次数锁异常 ruleValue: " + ruleValue + " 配置不正确");
        }

        Integer userRaffleCount = repository.queryTodayUserRaffleCount(userId, strategyId);

        /** 用户抽奖次数大于规则限定值，规则放行 */
        if (userRaffleCount > raffleCount) {
            log.info("规则过滤-次数锁 【放行】 userId:{} strategyId:{} raffleCount:{} userRaffleCount:{}", userId, strategyId, raffleCount, userRaffleCount);
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }

        /** 用户抽奖次数小于规则限定值，规则放行 */
        log.info("规则过滤-次数锁 【拦截】 userId:{} strategyId:{} raffleCount:{} userRaffleCount:{}", userId, strategyId, raffleCount, userRaffleCount);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
