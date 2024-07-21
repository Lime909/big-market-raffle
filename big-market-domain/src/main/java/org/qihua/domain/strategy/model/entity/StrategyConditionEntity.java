package org.qihua.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 策略条件实体
 * @date 2024-06-03 14:03:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyConditionEntity {

    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Integer strategyId;

}
