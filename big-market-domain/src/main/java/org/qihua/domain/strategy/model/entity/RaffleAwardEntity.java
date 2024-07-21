package org.qihua.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 抽奖奖品实体
 * @date 2024-06-05 15:15:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {

    /** 奖品ID */
    private Integer awardId;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 排序 */
    private Integer sort;

}

