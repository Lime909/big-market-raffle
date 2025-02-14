package org.qihua.domain.strategy.service;

import org.qihua.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author Lime
 * @description 抽奖库存服务
 * @date 2024-06-11 17:06:47
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

}
