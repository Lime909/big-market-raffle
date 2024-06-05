package org.qihua.domain.strategy.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.model.entity.RuleActionEntity;
import org.qihua.domain.strategy.model.entity.RuleMatterEntity;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.annotation.LogicStrategy;
import org.qihua.domain.strategy.service.rule.ILogicFilter;
import org.qihua.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.qihua.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lime
 * @description 抽奖前，白名单规则过滤
 * @date 2024-06-05 15:21:07
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WHITELIST)
public class RuleWhiteListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-白名单 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(),
                ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        String userId = ruleMatterEntity.getUserId();

        /** 查询规则配置 */
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        /** 过滤其他规则 */
        String[] userWhiteIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userWhiteId : userWhiteIds) {
            if (userId.equals(userWhiteId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_WHITELIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}


