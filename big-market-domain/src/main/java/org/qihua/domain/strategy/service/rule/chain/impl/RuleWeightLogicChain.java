package org.qihua.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
import org.qihua.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.qihua.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Lime
 * @description 权重责任链
 * @date 2024-06-09 15:48:37
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;
    @Resource
    protected IStrategyDispatch strategyDispatch;

    /**
     * 用户抽奖次数，后续完成这部分流程开发的时候，从数据库/Redis中读取
     */
    public Long userScore = 0L;


    @Override
    protected String ruleModel() {
        return "rule_weight";
    }

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());

        /** 1.根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。 */
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (analyticalValueGroup == null || analyticalValueGroup.isEmpty()) {
            return null;
        }

        /** 2.转换keys，默认排序 */
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        /** 3.找出最小符合的值 */
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        /** 4.权重抽奖 */
        if (nextValue == null) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }

        /** 5. 过滤其他责任链 */
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }

}