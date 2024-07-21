package org.qihua.domain.award.service;

import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author：Lime
 * @Description: 奖品服务接口
 * @Date：2024/7/15 11:04
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
}
