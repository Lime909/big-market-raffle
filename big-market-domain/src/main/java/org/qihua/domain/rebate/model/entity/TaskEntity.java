package org.qihua.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.rebate.event.SendRebateMessageEvent;
import org.qihua.domain.rebate.model.valobj.TaskStateVO;
import org.qihua.types.event.BaseEvent;

/**
 * @Author: Lime
 * @Description: 任务实体对象
 * @Date: 2024/7/30 13:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    /** 用户ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息内容 */
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /** 消息状态 */
    private TaskStateVO state;

}
