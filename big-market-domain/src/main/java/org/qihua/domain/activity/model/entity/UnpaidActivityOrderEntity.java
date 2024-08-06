package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 未完成支付的活动单
 * @Date: 2024/8/5 23:24
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidActivityOrderEntity {

    /** 用户ID */
    private String userId;
    /** 订单ID */
    private String orderId;
    /** 防重复的ID，确保幂等 */
    private String outBusinessNo;
    /** 支付金额 */
    private BigDecimal payAmount;

}
