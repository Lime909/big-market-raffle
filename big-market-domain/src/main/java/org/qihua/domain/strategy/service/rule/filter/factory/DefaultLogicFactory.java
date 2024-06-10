package org.qihua.domain.strategy.service.rule.filter.factory;

import com.alibaba.fastjson2.util.AnnotationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.qihua.domain.strategy.model.entity.RuleActionEntity;
import org.qihua.domain.strategy.service.annotation.LogicStrategy;
import org.qihua.domain.strategy.service.rule.filter.ILogicFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lime
 * @description 规则工厂
 * @date 2024-06-05 15:20:04
 */
@Service
public class DefaultLogicFactory {
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters){
        logicFilters.forEach(logicFilter -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logicFilter.getClass(), LogicStrategy.class);
            if(strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logicFilter);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter(){
        return (Map<String, ILogicFilter<T>>) (Map<?,?>)logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel{
        RULE_WEIGHT("rule_weight","【抽奖前】根据抽奖权重返回可抽奖范围key","before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前】黑名单规则过滤，命中黑名单就直接返回","before"),
        RULE_WHITELIST("rule_whitelist","【抽奖前】白名单规则过滤，命中白名单就直接返回","before"),
        RULE_LOCK("rule_lock", "【抽奖中规则】抽奖n次后，对应奖品可解锁抽奖", "center"),
        RULE_LUCK_AWARD("rule_luck_award", "【抽奖后规则】抽奖n次后，对应奖品可解锁抽奖", "after")
        ;
        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code){
            return LogicModel.valueOf(code.toUpperCase()).getType().equals("center");
        }

        public static boolean isAfter(String code){
            return LogicModel.valueOf(code.toUpperCase()).getType().equals("after");
        }
    }
}
