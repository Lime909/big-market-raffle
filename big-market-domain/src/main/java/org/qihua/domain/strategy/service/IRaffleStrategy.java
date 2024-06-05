package org.qihua.domain.strategy.service;

import org.qihua.domain.strategy.model.entity.RaffleAwardEntity;
import org.qihua.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author Lime
 * @description 抽奖策略接口
 * @date 2024-06-05 15:22:25
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
