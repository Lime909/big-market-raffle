package org.qihua.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 任务表，发送MQ
 * @Date：2024/7/11 23:13
 */
@Mapper
public interface ITaskDao {

    void insert(Task task);

    @DBRouter
    void updateTaskSendMessageCompleted(Task task);

    @DBRouter
    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();
}
