package org.qihua.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 策略结果实体
 * @date 2024-06-03 13:55:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {
    /** 用户ID */
    private String userId;
    /** 奖品ID */
    private Integer awardId;
}
