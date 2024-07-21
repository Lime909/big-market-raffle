package org.qihua.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：Lime
 * @Description: 发奖状态值对象
 * @Date：2024/7/15 11:19
 */
@Getter
@AllArgsConstructor
public enum AwardStateVO {

    create("create", "创建"),
    completed("completed", "发奖完成"),
    fail("fail","发奖失败"),
    ;

    private final String code;
    private final String info;
}
