package org.qihua.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.award.model.valobj.AwardStateVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Lime
 * @Description: 分发奖品实体对象
 * @Date: 2024/8/4 00:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributeAwardEntity {

    /** 用户ID */
    private String userId;
    /** 抽奖订单ID【作为幂等使用】 */
    private String orderId;
    /** 奖品ID */
    private Integer awardId;
    /** 奖品配置 发奖的时候根据这个发 */
    private String awardConfig;

}
