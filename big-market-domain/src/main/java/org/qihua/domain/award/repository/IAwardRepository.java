package org.qihua.domain.award.repository;

import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author：Lime
 * @Description: 奖品仓储服务
 * @Date：2024/7/15 11:43
 */
public interface IAwardRepository {

    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

}
