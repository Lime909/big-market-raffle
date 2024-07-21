package org.qihua.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import org.qihua.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.qihua.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.qihua.domain.activity.model.entity.*;
import org.qihua.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.qihua.domain.activity.model.valobj.ActivityStateVO;
import org.qihua.domain.activity.model.valobj.UserRaffleOrderStateVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.infrastructure.event.EventPublisher;
import org.qihua.infrastructure.persistent.dao.*;
import org.qihua.infrastructure.persistent.po.*;
import org.qihua.infrastructure.persistent.redis.IRedisService;
import org.qihua.types.common.Constants;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author：Lime
 * @Description: 活动仓储服务实现
 * @Date：2024/7/3 15:43
 */
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;
    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        Long cacheSkuStock = redisService.getAtomicLong(cacheKey);
        if (cacheSkuStock == null || cacheSkuStock == 0) {
            cacheSkuStock = 0L;
        }
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(cacheSkuStock.intValue())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        /** 优先从缓存中获取 */
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) return activityEntity;
        /** 从库中获取数据 */
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();

        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        /** 优先从缓存获取 */
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (activityCountEntity != null) return activityCountEntity;
        /** 从库中读取数据 */
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        /** 订单对象 */
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
        raffleActivityOrder.setSku(activityOrderEntity.getSku());
        raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
        raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
        raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
        raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
        raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
        raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

        /** 账户对象 */
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createQuotaOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());

        try {
            /** 以用户ID作为切分键，通过dbRouter设定路由 */
            dbRouter.doRouter(createQuotaOrderAggregate.getUserId());
            /** 编程式事务 */
            transactionTemplate.execute(status -> {
                try {
                    /** 1.写入订单 */
                    raffleActivityOrderDao.insert(raffleActivityOrder);
                    /** 2.更新账户 */
                    int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                    /** 3.创建账户-更新 为0，则账户不存在，创建新账户 */
                    if (count == 0) {
                        raffleActivityAccountDao.insert(raffleActivityAccount);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId:{}, activityId:{}, sku:{}", activityOrderEntity.getUserId(),
                            activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

    }

    @Override
    public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus == 0) {
            /** 库存消耗完了，发送MQ消息，更新数据库库存 */
            eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(), activitySkuStockZeroMessageEvent.buildEventMessage(sku));
            return false;
        } else if (surplus < 0) {
            /** 库存为0个，恢复为0个 */
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }

        /**1.根据cacheKey decr后的值，99，98，96和key组合称为库存锁的Key使用
         * 2.加锁为了兜底，后续要恢复库存的时候不会超卖，因为都加锁了
         * 3.加锁时间是活动到期+1天
         */
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        long expireMills = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMills, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("活动sku库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        raffleActivitySkuDao.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        raffleActivitySkuDao.clearActivitySkuStock(sku);
    }

    @Override
    public UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        /** 查询数据 */
        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(partakeRaffleActivityEntity.getUserId());
        userRaffleOrderReq.setActivityId(partakeRaffleActivityEntity.getActivityId());
        UserRaffleOrder userRaffleOrderRes = userRaffleOrderDao.queryNoUsedRaffleOrder(userRaffleOrderReq);
        if (userRaffleOrderRes == null) return null;
        /** 封装结果 */
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userRaffleOrderRes.getUserId());
        userRaffleOrderEntity.setActivityId(userRaffleOrderRes.getActivityId());
        userRaffleOrderEntity.setActivityName(userRaffleOrderRes.getActivityName());
        userRaffleOrderEntity.setStrategyId(userRaffleOrderRes.getStrategyId());
        userRaffleOrderEntity.setOrderId(userRaffleOrderRes.getOrderId());
        userRaffleOrderEntity.setOrderTime(userRaffleOrderRes.getOrderTime());
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.valueOf(userRaffleOrderRes.getOrderState()));

        return userRaffleOrderEntity;
    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        /** 查询数据 */
        RaffleActivityAccount raffleActivityAccountReq = new RaffleActivityAccount();
        raffleActivityAccountReq.setUserId(userId);
        raffleActivityAccountReq.setActivityId(activityId);
        RaffleActivityAccount raffleActivityAccountRes = raffleActivityAccountDao.queryActivityAccountByUserId(raffleActivityAccountReq);
        /** 封装结果 */
        return ActivityAccountEntity.builder()
                .userId(raffleActivityAccountRes.getUserId())
                .activityId(raffleActivityAccountRes.getActivityId())
                .totalCount(raffleActivityAccountRes.getTotalCount())
                .totalCountSurplus(raffleActivityAccountRes.getTotalCountSurplus())
                .dayCount(raffleActivityAccountRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountRes.getDayCountSurplus())
                .monthCount(raffleActivityAccountRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
        /** 查询数据 */
        RaffleActivityAccountMonth raffleActivityAccountMonthReq = new RaffleActivityAccountMonth();
        raffleActivityAccountMonthReq.setUserId(userId);
        raffleActivityAccountMonthReq.setActivityId(activityId);
        raffleActivityAccountMonthReq.setMonth(month);
        RaffleActivityAccountMonth raffleActivityAccountMonthRes = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(raffleActivityAccountMonthReq);
        if (raffleActivityAccountMonthRes == null) return null;
        /** 封装结果 */
        return ActivityAccountMonthEntity.builder()
                .userId(raffleActivityAccountMonthRes.getUserId())
                .activityId(raffleActivityAccountMonthRes.getActivityId())
                .month(raffleActivityAccountMonthRes.getMonth())
                .monthCount(raffleActivityAccountMonthRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonthRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        /** 查询数据 */
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(day);
        RaffleActivityAccountDay raffleActivityAccountDayRes = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (raffleActivityAccountDayRes == null) return null;
        /** 封装结果 */
        return ActivityAccountDayEntity.builder()
                .userId(raffleActivityAccountDayReq.getUserId())
                .activityId(raffleActivityAccountDayRes.getActivityId())
                .day(raffleActivityAccountDayRes.getDay())
                .dayCount(raffleActivityAccountDayReq.getDayCount())
                .dayCountSurplus(raffleActivityAccountDayReq.getDayCountSurplus())
                .build();
    }


    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        try {
            String userId = createPartakeOrderAggregate.getUserId();
            Long activityId = createPartakeOrderAggregate.getActivityId();
            ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
            ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
            ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
            UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();

            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    /** 1.更新总账户 */
                    int totalCount = raffleActivityAccountDao.updateActivityAccountSubtractionQuota(
                            RaffleActivityAccount.builder()
                                    .userId(userId)
                                    .activityId(activityId)
                                    .build()
                    );
                    if (totalCount != 1) {
                        status.setRollbackOnly();
                        log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId:{} activityId:{}", userId, activityId);
                        throw new AppException(ResponseCode.ACTIVITY_QUOTA_ERROR.getCode(), ResponseCode.ACTIVITY_QUOTA_ERROR.getInfo());
                    }

                    /** 2.创建或更新月账户，true-存在就更新，false-不存在则插入 */
                    if (createPartakeOrderAggregate.isExistAccountMonth()) {
                        int updateMonthCount = raffleActivityAccountMonthDao.updateActivityAccountMonthSubtractionQuota(
                                RaffleActivityAccountMonth.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .month(activityAccountMonthEntity.getMonth())
                                        .build()
                        );
                        if (updateMonthCount != 1) {
                            /** 未更新成功则回滚 */
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId:{} activityId:{} month:{} ", userId, activityId, activityAccountMonthEntity.getMonth());
                            throw new AppException(ResponseCode.ACTIVITY_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACTIVITY_MONTH_QUOTA_ERROR.getInfo());
                        }
                    } else {
                        raffleActivityAccountMonthDao.insertActivityAccountMonth(RaffleActivityAccountMonth.builder()
                                .userId(activityAccountMonthEntity.getUserId())
                                .activityId(activityAccountMonthEntity.getActivityId())
                                .month(activityAccountMonthEntity.getMonth())
                                .monthCount(activityAccountMonthEntity.getMonthCount())
                                .monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus())
                                .build()
                        );
                        /** 创建新月账户，则更新总账户表的月镜像额度 */
                        raffleActivityAccountDao.updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                                .build()
                        );
                    }

                    /** 3.创建或更新日账户，true-存在及更新，false-不存在则插入 */
                    if (createPartakeOrderAggregate.isExistAccountDay()) {
                        int updateDayCount = raffleActivityAccountDayDao.updateActivityAccountDaySubtractionQuota(
                                RaffleActivityAccountDay.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .day(activityAccountDayEntity.getDay())
                                        .build()
                        );
                        if (updateDayCount != 1) {
                            /** 未更新成功则回滚 */
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新日额度不足，异常 userId:{} activityId:{} day:{}", userId, activityId, activityAccountDayEntity.getDay());
                        }
                    } else {
                        raffleActivityAccountDayDao.insertActivityAccountDay(RaffleActivityAccountDay.builder()
                                .userId(activityAccountDayEntity.getUserId())
                                .activityId(activityAccountDayEntity.getActivityId())
                                .day(activityAccountDayEntity.getDay())
                                .dayCount(activityAccountDayEntity.getDayCount())
                                .dayCountSurplus(activityAccountDayEntity.getDayCountSurplus())
                                .build()
                        );
                        /** 新创建日账户，则更新总账户表的日镜像额度 */
                        raffleActivityAccountDao.updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                                .build());
                    }

                    /** 4.写入参与活动订单 */
                    userRaffleOrderDao.insert(UserRaffleOrder.builder()
                            .userId(userRaffleOrderEntity.getUserId())
                            .activityId(userRaffleOrderEntity.getActivityId())
                            .activityName(userRaffleOrderEntity.getActivityName())
                            .strategyId(userRaffleOrderEntity.getStrategyId())
                            .orderId(userRaffleOrderEntity.getOrderId())
                            .orderTime(userRaffleOrderEntity.getOrderTime())
                            .orderState(userRaffleOrderEntity.getOrderState().getCode())
                            .build());
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入创建参与活动记录，唯一索引冲突 userId:{} activityId:{} ", userId, activityId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });

        } finally {
            dbRouter.clear();
        }
    }

}
