package org.qihua.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.types.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lime
 * @description 策略规则实体
 * @date 2024-06-03 22:26:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID【规则类型为策略，则不需要奖品ID】 */
    private Integer awardId;
    /** 抽象规则类型；1-策略规则、2-奖品规则 */
    private Integer ruleType;
    /** 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】 */
    private String ruleModel;
    /** 抽奖规则比值 */
    private String ruleValue;
    /** 抽奖规则描述 */
    private String ruleDesc;

    /**
     * 数据案例： 4000：101,102 5000：101,102,103
     *
     * @return
     */
    public Map<String, List<Integer>> getRuleValues() {
        if(!ruleModel.equals("rule_weight")) return null;
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap = new HashMap<>();
        for(String ruleValueGroup : ruleValueGroups) {
            // 检查输入是否为空
            if(ruleValueGroup == null || ruleValueGroup.isEmpty()){
                return resultMap;
            }
            // 分割字符串获取键和值
            String[] keyAndValue = ruleValueGroup.split(Constants.COLON);
            if(keyAndValue.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);
            }
            // 解析
            String[] valueString = keyAndValue[1].split(Constants.SPLIT);
            List<Integer> values = new ArrayList<>();
            for(String value : valueString) {
                values.add(Integer.parseInt(value));
            }
            resultMap.put(ruleValueGroup, values);
        }
        return resultMap;
    }

}
