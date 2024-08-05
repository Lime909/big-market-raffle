package org.qihua.domain.credit.service;

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

}
