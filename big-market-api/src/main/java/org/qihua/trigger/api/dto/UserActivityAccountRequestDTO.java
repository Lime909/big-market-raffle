package org.qihua.trigger.api.dto;

import lombok.Data;

/**
 * @Author: Lime
 * @Description: 用户活动账户请求对象
 * @Date: 2024/7/31 21:09
 */
@Data
public class UserActivityAccountRequestDTO {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;

}
