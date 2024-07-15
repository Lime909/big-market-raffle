package org.qihua.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：Lime
 * @Description: 用户抽奖订单状态
 * @Date：2024/7/13 10:20
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {

    create("create","创建"),
    used("used","已使用"),
    cancel("cancel","已作废"),
    ;

    private String code;
    private String info;

}
