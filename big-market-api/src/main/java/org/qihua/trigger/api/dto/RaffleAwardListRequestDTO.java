package org.qihua.trigger.api.dto;

import lombok.Data;

/**
 * @author Lime
 * @description 抽奖奖品列表，请求对象
 * @date 2024-06-12 23:54:51
 */
@Data
public class RaffleAwardListRequestDTO {

    /** 活动ID */
    private Long activityId;
    /** 用户ID */
    private String userId;

}
