package org.qihua.domain.strategy.model.volobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lime
 * @description 策略奖品库存Key标识值
 * @date 2024-06-11 16:39:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {

    /** 策略ID */
    private Long strategyId;
    /** 奖品ID */
    private Integer awardId;

}
