package org.qihua.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.qihua.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.qihua.types.common.Constants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lime
 * @description 黑名单责任链
 * @date 2024-06-09 15:46:10
 */
@Slf4j
@Component("rule_blacklist")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        /** 查询规则值的配置 */
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        /** 黑名单抽奖判断 */
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .awardRuleValue("0.01,1")
                        .build();
            }
        }

        /** 过滤其他责任链 */
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);

    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }

}
