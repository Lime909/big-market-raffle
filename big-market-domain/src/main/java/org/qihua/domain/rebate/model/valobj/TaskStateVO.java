package org.qihua.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lime
 * @Description: 任务状态值对象
 * @Date: 2024/7/30 14:31
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete", "任务完成"),
    fail("fail","任务失败"),
    ;

    private final String code;
    private final String info;

}
