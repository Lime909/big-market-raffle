package org.qihua.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.UserCreditOrder;

/**
 * @Author: Lime
 * @Description: 用户积分流水订单Dao
 * @Date: 2024/8/5 08:55
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao {

    void insert(UserCreditOrder userCreditOrder);

}
