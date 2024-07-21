package org.qihua.domain.task.model.entity;

import lombok.Data;
import org.qihua.domain.award.event.SendAwardMessageEvent;
import org.qihua.domain.award.model.valobj.TaskStateVO;
import org.qihua.types.event.BaseEvent;

/**
 * @Author：Lime
 * @Description: 消息任务实体对象
 * @Date：2024/7/17 21:46
 */
@Data
public class TaskEntity {

    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private String message;

}
