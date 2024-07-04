package org.qihua.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.qihua.domain.activity.model.entity.ActivityOrderEntity;

/**
 * @Author：Lime
 * @Description: 下单聚合对象
 * @Date：2024/7/3 13:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 总次数 */
    private Integer totalCount;
    /** 日次数 */
    private Integer dayCount;
    /** 月次数 */
    private Integer monthCount;
    /** 活动订单实体 */
    private ActivityOrderEntity activityOrderEntity;

}
