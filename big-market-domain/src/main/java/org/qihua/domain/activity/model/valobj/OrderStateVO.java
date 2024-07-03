package org.qihua.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：Lime
 * @Description: 活动下单状态值对象
 * @Date：2024/7/3 13:31
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {

    completed("completed", "完成");


    private final String code;
    private final String info;
}
