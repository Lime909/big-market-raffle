package org.qihua.trigger.api.dto;

import lombok.Data;

/**
 * @Author: Lime
 * @Description: 抽奖策略规则，权重配置，查询N次抽奖可解锁奖品范围，请求对象
 * @Date: 2024/7/31 21:54
 */
@Data
public class RaffleStrategyRuleWeightRequestDTO {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;

}
