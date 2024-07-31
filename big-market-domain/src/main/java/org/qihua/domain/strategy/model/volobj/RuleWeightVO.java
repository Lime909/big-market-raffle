package org.qihua.domain.strategy.model.volobj;

import lombok.*;

import java.util.List;

/**
 * @Author: Lime
 * @Description: 权重规则值对象
 * @Date: 2024/7/31 22:19
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleWeightVO {

    /** 原始规则值配置 */
    private String ruleValue;
    /** 权重值 */
    private Integer weight;
    /** 奖品配置 */
    private List<Integer> awardIds;
    /** 奖品列表 */
    private List<Award> awardList;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Award{
        private Integer awardId;
        private String awardTitle;
    }

}
