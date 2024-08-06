package org.qihua.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.TaskEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.model.entity.UserCreditAwardEntity;
import org.qihua.domain.award.model.valobj.AccountStatusVO;
import org.qihua.domain.award.model.valobj.AwardStateVO;
import org.qihua.domain.award.repository.IAwardRepository;
import org.qihua.infrastructure.event.EventPublisher;
import org.qihua.infrastructure.persistent.dao.*;
import org.qihua.infrastructure.persistent.po.Task;
import org.qihua.infrastructure.persistent.po.UserAwardRecord;
import org.qihua.infrastructure.persistent.po.UserCreditAccount;
import org.qihua.infrastructure.persistent.po.UserRaffleOrder;
import org.qihua.infrastructure.persistent.redis.IRedisService;
import org.qihua.types.common.Constants;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author：Lime
 * @Description: 奖品仓储服务实现
 * @Date：2024/7/15 11:44
 */
@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IAwardDao awardDao;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
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

        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(userAwardRecordEntity.getUserId());
        userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());

        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    /** 写入记录 */
                    userAwardRecordDao.insert(userAwardRecord);
                    /** 写入任务 */
                    taskDao.insert(task);
                    /** 更新抽奖单 */
                    int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrderReq);
                    if (count != 1) {
                        status.setRollbackOnly();
                        log.error("写入中奖记录，用户抽奖单已使用过，不可重复抽奖 userId:{} activityId:{} awardId:{}", userId, activityId, awardId);
                        throw new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(), ResponseCode.ACTIVITY_ORDER_ERROR.getInfo());
                    }
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

        try {
            /** 发送信息【在事务外执行，失败了也能有任务补偿】*/
            eventPublisher.publish(task.getTopic(), task.getMessage());
            /** 更新数据库记录，task任务表 */
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("写入中奖记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, userAwardRecordEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

    @Override
    public String queryAwardConfig(Integer awardId) {
        /** 优先从缓存中获取 */
        String cacheKey = Constants.RedisKey.AWARD_CONFIG_KEY + awardId;
        String awardConfig = redisService.getValue(cacheKey);
        if (awardConfig != null) return awardConfig;
        /** 从数据库中获取 */
        awardConfig = awardDao.queryAwardConfigByAwardId(awardId);
        redisService.setValue(cacheKey, awardConfig);
        return awardConfig;
    }

    @Override
    public void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();

        /** 更新发奖记录 */
        UserAwardRecord userAwardRecordReq = new UserAwardRecord();
        userAwardRecordReq.setUserId(userId);
        userAwardRecordReq.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecordReq.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userCreditAwardEntity.getUserId());
        userCreditAccountReq.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());

        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(giveOutPrizesAggregate.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    /** 更新发奖记录「存在就更新」「首次就插入」*/
                    UserCreditAccount userCreditAccountRes = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (userCreditAccountRes == null) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    } else {
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }

                    /** 更新奖品记录「发奖完成」*/
                    int updateAwardCount = userAwardRecordDao.updateAwardRecordCompletedState(userAwardRecordReq);
                    if (updateAwardCount == 0) {
                        log.error("更新中奖记录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        status.setRollbackOnly();
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("更新中奖记录，唯一索引冲突 userId:{}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }
    }

    @Override
    public String queryAwardKey(Integer awardId) {
        /** 优先从缓存中获取 */
        String cacheKey = Constants.RedisKey.AWARD_KEY_KEY + awardId;
        String awardKey = redisService.getValue(cacheKey);
        if (awardKey != null) return awardKey;
        /** 从数据库中读取 */
        awardKey = awardDao.queryAwardKeyByAwardId(awardId);
        redisService.setValue(cacheKey, awardKey);
        return awardKey;
    }

    @Override
    public List<UserAwardRecordEntity> queryUserAwardRecord(String userId) {
        try {
            dbRouter.doRouter(userId);
            List<UserAwardRecord> userAwardRecords = userAwardRecordDao.queryUserAwardRecord(userId);
            List<UserAwardRecordEntity> userAwardRecordEntities = new ArrayList<>();
            for (UserAwardRecord userAwardRecord : userAwardRecords) {
                UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
                userAwardRecordEntity.setUserId(userAwardRecord.getUserId());
                userAwardRecordEntity.setOrderId(userAwardRecord.getOrderId());
                userAwardRecordEntity.setAwardId(userAwardRecord.getAwardId());
                userAwardRecordEntity.setAwardTitle(userAwardRecord.getAwardTitle());
                userAwardRecordEntity.setAwardTime(userAwardRecord.getAwardTime());
                userAwardRecordEntities.add(userAwardRecordEntity);
            }
            return userAwardRecordEntities;
        } finally {
            dbRouter.clear();
        }

    }

}
