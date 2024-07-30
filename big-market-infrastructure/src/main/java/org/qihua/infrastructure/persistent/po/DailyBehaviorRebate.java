package org.qihua.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @Author: Lime
 * @Description: 日常行为返利活动配置
 * @Date: 2024/7/30 11:26
 */
@Data
public class DailyBehaviorRebate {

    /** 自增ID */
    private int id;
    /** 行为类型（sign-签到，openai_pay支付）*/
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分）*/
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
    /** 返利状态（open、close） */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更改时间 */
    private Date updateTime;
}
