package org.qihua.domain.rebate.model.aggerate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.qihua.domain.rebate.model.entity.TaskEntity;

/**
 * @Author: Lime
 * @Description: 行为返利聚合对象
 * @Date: 2024/7/30 14:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    /** 用户ID */
    private String userId;
    /** 行为返利订单实体对象 */
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    /** 任务实体对象 */
    private TaskEntity taskEntity;

}
