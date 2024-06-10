package org.qihua.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
import org.qihua.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.qihua.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lime
 * @description 默认责任链，在最后一个
 * @date 2024-06-09 15:47:59
 */
@Slf4j
@Component("rule_default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    protected IStrategyDispatch strategyDispatch;

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
        return DefaultChainFactory.StrategyAwardVO.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();

    }
}
