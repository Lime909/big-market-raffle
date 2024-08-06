package org.qihua.domain.activity.service.partake;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.qihua.domain.activity.model.entity.UserRaffleOrderEntity;
import org.qihua.domain.activity.model.valobj.ActivityStateVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.IRaffleActivityPartakeService;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 抽奖活动参与抽奖类
 * @Date：2024/7/12 10:48
 */
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

    protected final IActivityRepository activityRepository;

    public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(String userId, Long activityId) {
        return createOrder(PartakeRaffleActivityEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .build());
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        /** 1.基础信息 */
        String userId = partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        Date currentDate = new Date();
        log.info("创建活动抽奖单开始 userId:{} activityId:{}", userId, activityId);
        // 1. 活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        /** 校验活动状态 */
        if (!activityEntity.getState().equals(ActivityStateVO.open)) {
            log.error("创建活动抽奖单失败，活动状态未开启 activityId:{} state:{}", activityId, activityEntity.getState());
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        /** 校验活动日期 */
        if (activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            log.error("创建活动抽奖单失败，活动时间未开始 activityId:{} state:{}", activityId, activityEntity.getState());
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }

        /** 2.查询未被使用的订单参与记录 */
        UserRaffleOrderEntity userRaffleOrderEntity = activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
        if (userRaffleOrderEntity != null) {
            log.info("创建参与活动订单存在 userId:{} activityId:{} userRaffleOrderEntity:{}", userId, activityId, JSON.toJSONString(userRaffleOrderEntity));
            return userRaffleOrderEntity;
        }

        /** 3.账户额度的过滤 & 返回账户的构建对象 */
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentDate);

        /** 4.构建订单 */
        UserRaffleOrderEntity userRaffleOrder = this.buildUserRaffleOrder(userId, activityId, currentDate);

        /** 5.填充抽奖的实体对象 */
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrder);

        /** 6.保存聚合对象 */
        activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);
        log.info("创建活动抽奖单完成 userId:{} activityId:{} orderId:{}", userId, activityId, userRaffleOrder.getOrderId());
        /** 7.返回订单信息 */
        return userRaffleOrder;
    }


    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);

}
