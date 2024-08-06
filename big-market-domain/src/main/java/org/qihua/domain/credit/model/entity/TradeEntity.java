package org.qihua.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.credit.model.valobj.TradeNameVO;
import org.qihua.domain.credit.model.valobj.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 增加额度实体
 * @Date: 2024/8/5 09:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeEntity {

    /** 用户ID */
    private String userId;
    /** 交易名称 */
    private TradeNameVO tradeName;
    /** 交易类型 */
    private TradeTypeVO tradeType;
    /** 交易金额 */
    private BigDecimal amount;
    /** 业务防重ID */
    private String outBusinessNo;

}
