package org.qihua.domain.award.service;

import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.DistributeAwardEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 奖品服务接口
 * @Date：2024/7/15 11:04
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    /**
     * 配送发货奖品
     */
    void distributeAward(DistributeAwardEntity distributeAwardEntity);

    List<UserAwardRecordEntity> queryUserAwardRecord(String userId);
}
