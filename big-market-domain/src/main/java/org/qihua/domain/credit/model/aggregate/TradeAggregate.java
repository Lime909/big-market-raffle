package org.qihua.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.domain.credit.model.entity.CreditAccountEntity;
import org.qihua.domain.credit.model.entity.CreditOrderEntity;
import org.qihua.domain.credit.model.valobj.TradeNameVO;
import org.qihua.domain.credit.model.valobj.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 交易聚合对象
 * @Date: 2024/8/5 10:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeAggregate {

    /** 用户ID */
    private String userId;
    /** 积分账户实体 */
    private CreditAccountEntity creditAccountEntity;
    /** 积分流水实体 */
    private CreditOrderEntity creditOrderEntity;

    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount) {
        return CreditAccountEntity.builder().userId(userId).adjustAmount(adjustAmount).build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId, TradeNameVO tradeName, TradeTypeVO tradeType,
                                                            BigDecimal tradeAmount, String outBusinessNo) {
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeName)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();

    }
}