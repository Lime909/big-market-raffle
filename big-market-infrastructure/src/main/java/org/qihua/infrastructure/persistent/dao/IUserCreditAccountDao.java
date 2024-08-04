package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.UserCreditAccount;

/**
 * @Author: Lime
 * @Description: 用户积分额度Dao
 * @Date: 2024/8/4 00:45
 */
@Mapper
public interface IUserCreditAccountDao {

    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    void insert(UserCreditAccount userCreditAccountReq);

}
