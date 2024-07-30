package org.qihua.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lime
 * @Description: 返利类型（sku活动库存充值商品、integral用户活动积分）
 * @Date: 2024/7/30 21:17
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {

    SKU("sku","活动库存充值商品"),
    INTEGRAL("integral","用户活动积分"),
    ;

    private final String code;
    private final String info;
}
