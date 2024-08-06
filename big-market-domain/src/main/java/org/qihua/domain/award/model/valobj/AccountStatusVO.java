package org.qihua.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lime
 * @Description: 账户状态枚举
 * @Date: 2024/8/4 08:56
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {

    open("open", "开启"),
    close("close", "冻结"),
    ;
    private final String code;
    private final String info;
}
