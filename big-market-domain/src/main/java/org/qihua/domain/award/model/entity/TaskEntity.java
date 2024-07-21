package org.qihua.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.award.event.SendAwardMessageEvent;
import org.qihua.domain.award.model.valobj.TaskStateVO;
import org.qihua.types.event.BaseEvent;

/**
 * @Author：Lime
 * @Description: 任务实体对象
 * @Date：2024/7/15 11:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /** 任务状态 create-创建 completed-完成 fail-失败 */
    private TaskStateVO state;

}
