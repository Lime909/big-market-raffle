package org.qihua.domain.activity.service.quota.policy.impl;

import org.qihua.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.qihua.domain.activity.model.valobj.OrderStateVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 返利无支付交易订单，直接充值到账
 * @Date: 2024/8/5 17:33
 */
@Service("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateNoPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        /** 不需要支付，修改订单金额为0，状态修改为完成，直接给账户充值 */
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveNoPayOrder(createQuotaOrderAggregate);
    }
}
