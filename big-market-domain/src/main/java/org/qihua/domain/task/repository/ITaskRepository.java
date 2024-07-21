package org.qihua.domain.task.repository;

import org.qihua.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 任务仓储服务接口
 * @Date：2024/7/17 21:55
 */
public interface ITaskRepository {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);

}
