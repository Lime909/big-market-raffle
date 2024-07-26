package org.qihua.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Lime
 * @Description: 活动抽奖返回对象
 * @Date: 2024/7/23 22:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawResponseDTO {

    /** 奖品ID */
    private Integer awardId;
    /** 奖品标题 */
    private String awardTitle;
    /** 排序编号【策略奖品配置的奖品顺序编号】 */
    private Integer awardIndex;

}
