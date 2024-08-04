package org.qihua.domain.award.service.distribute;

import org.qihua.domain.award.model.entity.DistributeAwardEntity;

/**
 * @Author: Lime
 * @Description: 分发奖品接口
 * @Date: 2024/8/4 00:50
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);

}
