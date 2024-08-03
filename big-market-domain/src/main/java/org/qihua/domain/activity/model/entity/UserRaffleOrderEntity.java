package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.activity.model.valobj.UserRaffleOrderStateVO;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 用户抽奖订单实体对象
 * @Date：2024/7/13 10:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrderEntity {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 策略ID */
    private Long strategyId;
    /** 订单ID */
    private String orderId;
    /** 下单时间 */
    private Date orderTime;
    /** 订单状态 create-创建 used-已使用 cancel-已作废 */
    private UserRaffleOrderStateVO orderState;
    /** 活动结束时间 */
    private Date endDateTime;

}
