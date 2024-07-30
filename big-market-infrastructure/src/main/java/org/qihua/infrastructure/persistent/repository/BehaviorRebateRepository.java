package org.qihua.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.rebate.model.aggerate.BehaviorRebateAggregate;
import org.qihua.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.qihua.domain.rebate.model.entity.TaskEntity;
import org.qihua.domain.rebate.model.valobj.BehaviorTypeVO;
import org.qihua.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import org.qihua.domain.rebate.repository.IBehaviorRebateRepository;
import org.qihua.infrastructure.event.EventPublisher;
import org.qihua.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import org.qihua.infrastructure.persistent.dao.ITaskDao;
import org.qihua.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import org.qihua.infrastructure.persistent.po.DailyBehaviorRebate;
import org.qihua.infrastructure.persistent.po.Task;
import org.qihua.infrastructure.persistent.po.UserBehaviorRebateOrder;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lime
 * @Description:
 * @Date: 2024/7/30 11:12
 */
@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>();
        for(DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
            dailyBehaviorRebateVOS.add(DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .build());
        }
        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
               try{
                   for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                       BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();

                       /** 用户行为返利对象 */
                       UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                       userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                       userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                       userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                       userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                       userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                       userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                       userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                       userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                       /** 任务对象 */
                       TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                       Task task = new Task();
                       task.setUserId(taskEntity.getUserId());
                       task.setTopic(taskEntity.getTopic());
                       task.setMessageId(taskEntity.getMessageId());
                       task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                       task.setState(taskEntity.getState().getCode());
                       taskDao.insert(task);
                   }
                   return 1;
               }catch (DuplicateKeyException e){
                   status.setRollbackOnly();
                   log.error("写入返利记录，唯一索引冲突 userId:{}", userId, e);
                   throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
               }
            });
        } finally {
            dbRouter.clear();
        }

        /** 同步发送MQ消息 */
        for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates){
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                /** 发送消息【在事务外执行，如果失败还有任务补偿】*/
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                /** 更新数据库记录，task 任务表 */
                taskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }

        }
    }
}
