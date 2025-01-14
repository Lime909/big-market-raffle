package org.qihua.domain.credit.repository;

import org.qihua.domain.credit.model.aggregate.TradeAggregate;
import org.qihua.domain.credit.model.entity.CreditAccountEntity;

/**
 * @Author: Lime
 * @Description: 积分仓储服务
 * @Date: 2024/8/5 09:04
 */
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

    CreditAccountEntity queryUserCreditAccount(String userId);

}
