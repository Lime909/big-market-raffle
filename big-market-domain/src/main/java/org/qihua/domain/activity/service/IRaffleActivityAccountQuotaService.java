package org.qihua.domain.activity.service;

import org.qihua.domain.activity.model.entity.ActivityAccountEntity;
import org.qihua.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @Author：Lime
 * @Description: 抽奖活动额度接口
 * @Date：2024/7/3 13:30
 */
public interface IRaffleActivityAccountQuotaService {


    /**
     * 创建sku账户充值订单，给用户增加抽奖次数
     *
     * 1.在【打卡、签到、分享、对话、积分兑换】的行为下，创建出活动订单，给用户的活动账户【日、月】增加可用次数
     * 2.对用户的抽奖次数，比如新进来就依次，前端页面上，用户点击可以获得一次抽奖机会
     *
     * @param skuRechargeEntity
     * @return 活动ID
     */
    String createOrder(SkuRechargeEntity skuRechargeEntity);

    /**
     * 查询活动期间，用户抽奖的日次数
     *
     * @param activityId
     * @param userId
     * @return 参与次数
     */
    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    /**
     * 查询活动账户额度「总、月、日」
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 账户实体
     */
    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    /**
     * 查询活动账户 - 总，参与次数
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 参与次数
     */
    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);
}
