package org.qihua.domain.task.service;

import org.qihua.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 消息任务服务接口
 * @Date：2024/7/17 21:44
 */
public interface ITaskService {

    /**
     * 查询发送MQ失败和超时1分钟未发送的MQ
     *
     * @return 未发送的任务消息列表10条
     */
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);

}
