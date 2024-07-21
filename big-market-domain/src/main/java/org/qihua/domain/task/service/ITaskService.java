package org.qihua.domain.task.service;

import org.qihua.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 消息任务服务接口
 * @Date：2024/7/17 21:44
 */
public interface ITaskService {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);

}
