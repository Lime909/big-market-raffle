package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.activity.model.valobj.OrderStateVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动参与对象实体
 * @Date：2024/7/3 13:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOrderEntity {

    /** 用户ID */
    private String userId;
    /** sku */
    private Long sku;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 订单ID */
    private String orderId;
    /** 下单时间 */
    private Date orderTime;
    /** 总次数 */
    private Integer totalCount;
    /** 日次数 */
    private Integer dayCount;
    /** 月次数 */
    private Integer monthCount;
    /** 支付金额 */
    private BigDecimal payAmount;
    /** 订单状态 */
    private OrderStateVO state;
    /** 防重复的ID，确保幂等 */
    private String outBusinessNo;

}
