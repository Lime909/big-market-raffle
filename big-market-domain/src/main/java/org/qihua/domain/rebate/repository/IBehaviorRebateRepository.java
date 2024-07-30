package org.qihua.domain.rebate.repository;

import org.qihua.domain.rebate.model.aggerate.BehaviorRebateAggregate;
import org.qihua.domain.rebate.model.valobj.BehaviorTypeVO;
import org.qihua.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @Author: Lime
 * @Description: 行为返利服务仓储接口
 * @Date: 2024/7/30 11:11
 */
public interface IBehaviorRebateRepository {

    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
