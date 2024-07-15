package org.qihua.domain.activity.model.entity;

import lombok.Data;

/**
 * @Author：Lime
 * @Description: 参与抽奖活动实体对象
 * @Date：2024/7/13 10:15
 */
@Data
public class PartakeRaffleActivityEntity {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;

}
