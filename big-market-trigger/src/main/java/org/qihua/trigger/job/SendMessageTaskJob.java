package org.qihua.trigger.job;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.task.model.entity.TaskEntity;
import org.qihua.domain.task.service.ITaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author：Lime
 * @Description: 发送MQ信息队列任务
 * @Date：2024/7/17 21:35
 */
@Slf4j
@Component()
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            /** 获得分库数量 */
            int dbCount = dbRouter.dbCount();
            for (int dbIdx = 1; dbIdx <= dbCount; dbIdx++) {
                int finalDbIdx = dbIdx;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dbRouter.setDBKey(finalDbIdx);
                            dbRouter.setTBKey(0);
                            List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
                            if (taskEntities.isEmpty()) return;

                            /** 发送MQ消息 */
                            for (TaskEntity taskEntity : taskEntities) {
                                /** 开启线程发送，提高发送效率，配置的线程池是CallerRunsPolicy */
                                executor.execute(() -> {
                                    try {
                                        taskService.sendMessage(taskEntity);
                                        taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                    } catch (Exception e) {
                                        log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                                        taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                                    }
                                });
                            }
                        } finally {
                            dbRouter.clear();
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败", e);
        } finally {
            dbRouter.clear();
        }

    }
}
