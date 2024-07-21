package org.qihua.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：Lime
 * @Description: 任务状态值对象
 * @Date：2024/7/15 11:24
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {
    create("create", "创建"),
    completed("completed","任务完成"),
    fail("fail","任务失败"),
    ;

    private final String code;
    private final String info;
}
