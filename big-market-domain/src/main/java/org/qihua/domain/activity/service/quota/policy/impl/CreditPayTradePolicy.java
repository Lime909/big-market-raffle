package org.qihua.domain.activity.service.quota.policy.impl;

import org.qihua.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.qihua.domain.activity.model.valobj.OrderStateVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

/**
 * @Author: Lime
 * @Description: 积分兑换，支付类订单
 * @Date: 2024/8/5 17:33
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
