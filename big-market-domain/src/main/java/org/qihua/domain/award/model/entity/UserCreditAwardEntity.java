package org.qihua.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 用户积分奖品实体对象
 * @Date: 2024/8/4 08:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAwardEntity {

    /** 用户ID */
    private String userId;
    /** 积分值账户 */
    private BigDecimal creditAmount;

}