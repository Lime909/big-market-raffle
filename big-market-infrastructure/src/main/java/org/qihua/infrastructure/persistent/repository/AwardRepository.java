package org.qihua.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.TaskEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.repository.IAwardRepository;
import org.qihua.infrastructure.event.EventPublisher;
import org.qihua.infrastructure.persistent.dao.ITaskDao;
import org.qihua.infrastructure.persistent.dao.IUserAwardRecordDao;
import org.qihua.infrastructure.persistent.po.Task;
import org.qihua.infrastructure.persistent.po.UserAwardRecord;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description: 奖品仓储服务实现
 * @Date：2024/7/15 11:44
 */
@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    /** 写入记录 */
                    userAwardRecordDao.insert(userAwardRecord);
                    /** 写入任务 */
                    taskDao.insert(task);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId:{} activityId:{} awardId:{}", userId, activityId, awardId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

        try{
            /** 发送信息【在事务外执行，失败了也能有任务补偿】*/
            eventPublisher.publish(taskEntity.getTopic(), task.getMessage());
            /** 更新数据库记录，task任务表 */
            taskDao.updateTaskSendMessageCompleted(task);
        }catch (Exception e){
            log.error("写入中奖记录，发送MQ消息失败 userId:{} topic:{}", userId, taskEntity.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

}
