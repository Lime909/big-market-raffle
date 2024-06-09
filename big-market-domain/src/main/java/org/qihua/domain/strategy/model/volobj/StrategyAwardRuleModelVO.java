package org.qihua.domain.strategy.model.volobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.qihua.types.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lime
 * @description 抽奖策略规则规则值对象
 * @date 2024-06-09 13:59:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;

    public String[] raffleCenterRuleModelList(){
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues =ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues){
            if(DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

    public String[] raffleAfterRuleModelList(){
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues =ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues){
            if(DefaultLogicFactory.LogicModel.isAfter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
