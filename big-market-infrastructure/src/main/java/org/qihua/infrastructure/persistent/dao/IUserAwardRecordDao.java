package org.qihua.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.UserAwardRecord;

/**
 * @Author：Lime
 * @Description: 用户中奖记录表
 * @Date：2024/7/11 23:13
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDao {

    void insert(UserAwardRecord userAwardRecord);

    int updateAwardRecordCompletedState(UserAwardRecord userAwardRecordReq);

}
