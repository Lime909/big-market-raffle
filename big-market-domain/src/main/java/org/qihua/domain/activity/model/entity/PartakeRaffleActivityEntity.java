package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：Lime
 * @Description: 参与抽奖活动实体对象
 * @Date：2024/7/13 10:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartakeRaffleActivityEntity {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;

}
