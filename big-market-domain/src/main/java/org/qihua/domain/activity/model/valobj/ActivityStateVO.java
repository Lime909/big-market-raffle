package org.qihua.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：Lime
 * @Description: 活动状态值对象
 * @Date：2024/7/3 13:31
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {

    create("create", "创建"),
    open("open", "开启"),
    close("close", "关闭"),
    ;

    private final String code;
    private final String info;
}
