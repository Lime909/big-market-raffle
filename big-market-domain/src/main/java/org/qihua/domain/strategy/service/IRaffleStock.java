package org.qihua.domain.strategy.service;

import org.qihua.domain.strategy.model.volobj.StrategyAwardStockKeyVO;

/**
 * @author Lime
 * @description 抽奖库存服务
 * @date 2024-06-11 17:06:47
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     * @return 奖品库存值
     * @throws InterruptedException
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     * @param strategyId
     * @param awardId
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

}
