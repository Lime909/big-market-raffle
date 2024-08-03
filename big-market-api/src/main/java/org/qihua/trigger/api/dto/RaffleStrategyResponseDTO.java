package org.qihua.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 抽奖应答结果
 * @date 2024-06-13 00:00:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyResponseDTO {

    /** 奖品ID */
    private Integer awardId;
    /** 奖品排序编号 */
    private Integer awardIndex;

}
