package org.qihua.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.rebate.model.valobj.BehaviorTypeVO;

/**
 * @Author: Lime
 * @Description: 返利行为实体
 * @Date: 2024/7/30 13:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    /** 用户ID */
    private String userId;
    /** 行为类型 sign-签到 openai-pay-支付 */
    private BehaviorTypeVO behaviorTypeVO;
    /** 业务ID 签到-日期字符串 支付-订单号 */
    private String outBusinessNo;

}
