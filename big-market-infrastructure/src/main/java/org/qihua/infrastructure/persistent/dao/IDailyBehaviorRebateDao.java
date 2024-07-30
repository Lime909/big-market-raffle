package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @Author: Lime
 * @Description: 日常行为返利DAO
 * @Date: 2024/7/30 11:44
 */
@Mapper
public interface IDailyBehaviorRebateDao {

    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String behaviorType);

}
