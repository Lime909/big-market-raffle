package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.RaffleActivity;
import org.qihua.infrastructure.persistent.po.RaffleActivityCount;

/**
 * @Author：Lime
 * @Description: 抽奖活动次数配置表Dao
 * @Date：2024/6/30 14:33
 */
@Mapper
public interface IRaffleActivityCountDao {

    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
