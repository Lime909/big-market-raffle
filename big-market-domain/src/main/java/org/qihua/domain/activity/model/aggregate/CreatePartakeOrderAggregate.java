package org.qihua.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.activity.model.entity.ActivityAccountDayEntity;
import org.qihua.domain.activity.model.entity.ActivityAccountEntity;
import org.qihua.domain.activity.model.entity.ActivityAccountMonthEntity;
import org.qihua.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @Author：Lime
 * @Description: 参与活动订单聚合对象
 * @Date：2024/7/13 11:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 账户总额度 */
    private ActivityAccountEntity activityAccountEntity;

    /** 是否存在月额度 */
    private boolean isExistAccountMonth = true;
    /** 账户月额度 */
    private ActivityAccountMonthEntity activityAccountMonthEntity;

    /** 是否存在日额度 */
    private boolean isExistAccountDay = true;
    /** 账户日额度 */
    private ActivityAccountDayEntity activityAccountDayEntity;

    /** 订单的实体对象 */
    private UserRaffleOrderEntity userRaffleOrderEntity;
}
