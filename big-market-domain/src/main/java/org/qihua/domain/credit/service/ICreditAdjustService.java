package org.qihua.domain.credit.service;

import org.qihua.domain.credit.model.entity.CreditAccountEntity;
import org.qihua.domain.credit.model.entity.TradeEntity;

/**
 * @Author: Lime
 * @Description: 积分调额服务
 * @Date: 2024/8/5 09:53
 */
public interface ICreditAdjustService {

    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 订单号
     */
    String createOrder(TradeEntity tradeEntity);

    /**
     * 查询用户积分账户
     * @param userId 用户ID
     * @return 积分账户实体
     */
    CreditAccountEntity queryUserCreditAccount(String userId);
}
