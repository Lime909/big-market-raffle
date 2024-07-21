package org.qihua.domain.activity.service.partake;

import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.qihua.domain.activity.model.entity.*;
import org.qihua.domain.activity.model.valobj.UserRaffleOrderStateVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：Lime
 * @Description: 参与活动抽奖服务
 * @Date：2024/7/13 11:42
 */
@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
        /** 查询总账户额度 */
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);
        /** 额度判断*/
        if (activityAccountEntity == null || activityAccountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_QUOTA_ERROR.getCode(), ResponseCode.ACTIVITY_QUOTA_ERROR.getInfo());
        }

        String month = dateFormatMonth.format(currentDate);
        /** 查询月账户额度 */
        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        if (activityAccountMonthEntity != null && activityAccountMonthEntity.getMonthCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACTIVITY_MONTH_QUOTA_ERROR.getInfo());
        }

        /** 创建月账户额度 true-存在 false-不存在 */
        boolean isExistAccountMonth = activityAccountMonthEntity != null;
        if (activityAccountMonthEntity == null) {
            activityAccountMonthEntity = new ActivityAccountMonthEntity();
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setMonth(month);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
        }

        String day = dateFormatDay.format(currentDate);
        /** 查询日账户额度 */
        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if (activityAccountDayEntity != null && activityAccountDayEntity.getDayCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACTIVITY_DAY_QUOTA_ERROR.getInfo());
        }

        /** 创建日账户额度 true-存在 false-不存在 */
        boolean isExistAccountDay = activityAccountDayEntity != null;
        if (activityAccountDayEntity == null) {
            activityAccountDayEntity = new ActivityAccountDayEntity();
            activityAccountDayEntity.setUserId(userId);
            activityAccountDayEntity.setActivityId(activityId);
            activityAccountDayEntity.setDay(day);
            activityAccountDayEntity.setDayCount(activityAccountEntity.getDayCount());
            activityAccountDayEntity.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
        }

        /** 构建值对象 */
        CreatePartakeOrderAggregate createPartakeOrderAggregate = new CreatePartakeOrderAggregate();
        createPartakeOrderAggregate.setUserId(userId);
        createPartakeOrderAggregate.setActivityId(activityId);
        createPartakeOrderAggregate.setActivityAccountEntity(activityAccountEntity);
        createPartakeOrderAggregate.setExistAccountMonth(isExistAccountMonth);
        createPartakeOrderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);
        createPartakeOrderAggregate.setExistAccountDay(isExistAccountDay);
        createPartakeOrderAggregate.setActivityAccountDayEntity(activityAccountDayEntity);

        return createPartakeOrderAggregate;
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        /** 构建订单 */
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userId);
        userRaffleOrderEntity.setActivityId(activityId);
        userRaffleOrderEntity.setActivityName(activityEntity.getActivityName());
        userRaffleOrderEntity.setStrategyId(activityEntity.getStrategyId());
        userRaffleOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.create);
        userRaffleOrderEntity.setOrderTime(currentDate);

        return userRaffleOrderEntity;
    }
}
