package org.qihua.domain.activity.service.quota.policy;

import org.qihua.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @Author: Lime
 * @Description: 交易策略接口，包括；返利兑换（不用支付），积分订单（需要支付）
 * @Date: 2024/8/5 17:35
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);

}
