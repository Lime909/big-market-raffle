package org.qihua.trigger.api.dto;

import lombok.Data;

/**
 * @Author: Lime
 * @Description: 活动抽奖请求对象
 * @Date: 2024/7/23 22:41
 */
@Data
public class ActivityDrawRequestDTO {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;

}
