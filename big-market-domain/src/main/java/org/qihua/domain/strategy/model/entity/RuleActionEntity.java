package org.qihua.domain.strategy.model.entity;

import lombok.*;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;

/**
 * @author Lime
 * @description 规则动作实体
 * @date 2024-06-05 15:16:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    public static class RaffleEntity {}

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RaffleBeforeEntity extends RaffleEntity {
        /** 策略ID */
        private Long strategyId;
        /** 权重key */
        private String ruleWeightValueKey;
        /** 奖品ID */
        private Integer awardId;
    }

    public static class RaffleCenterEntity extends RaffleEntity {

    }

    public static class RaffleAfterEntity extends RaffleEntity {

    }
}
