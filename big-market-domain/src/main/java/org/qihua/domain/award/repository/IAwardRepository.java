package org.qihua.domain.award.repository;

import org.qihua.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 奖品仓储服务
 * @Date：2024/7/15 11:43
 */
public interface IAwardRepository {

    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    String queryAwardConfig(Integer awardId);

    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    String queryAwardKey(Integer awardId);

    List<UserAwardRecordEntity> queryUserAwardRecord(String userId);
}
