package org.qihua.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.qihua.domain.strategy.model.entity.StrategyEntity;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Lime
 * @description 责任链工厂
 * @date 2024-06-09 15:45:47
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainMap;
    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository repository) {
        this.logicChainMap = logicChainMap;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();

        if(ruleModels == null || ruleModels.length == 0) {
            return logicChainMap.get(LogicModel.RULE_DEFAULT.getCode());
        }

        ILogicChain logicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = logicChainMap.get(ruleModels[i]);
            current = current.appendNext(nextChain);
        }

        current.appendNext(logicChainMap.get(LogicModel.RULE_DEFAULT.getCode()));

        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        private Integer awardId;
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重抽奖"),
        ;
        private final String code;
        private final String info;
    }

}
