package org.qihua.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 抽奖奖品列表，应答对象
 * @date 2024-06-12 23:55:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {
    /** 奖品ID */
    private Integer awardId;
    /** 奖品标题 */
    private String awardTitle;
    /** 奖品副标题 */
    private String awardSubtitle;
    /** 奖品编号 */
    private Integer sort;
}
