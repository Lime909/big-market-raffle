package org.qihua.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lime
 * @description 规则过滤校验类型值对象
 * @date 2024-06-05 15:47:09
 */
@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    ALLOW("0000","放行；执行后续的留存流程，不受规则影响"),
    TAKE_OVER("0001","接管；后续的流程，受到规则的影响"),
    ;

    private final String code;
    private final String info;
}
