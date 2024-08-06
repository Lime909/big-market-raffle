package org.qihua.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lime
 * @Description: 交易类型枚举值
 * @Date: 2024/8/5 09:56
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "逆向交易，- 积分"),
    ;
    private final String code;
    private final String info;

}
