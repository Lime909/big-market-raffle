package org.qihua.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 积分账户实体
 * @Date: 2024/8/5 09:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountEntity {

    /** 用户ID */
    private String userId;
    /** 调额金额,每次扣减的值 */
    private BigDecimal adjustAmount;

}
