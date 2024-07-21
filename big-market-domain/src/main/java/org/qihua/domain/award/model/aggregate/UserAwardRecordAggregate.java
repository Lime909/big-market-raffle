package org.qihua.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.award.model.entity.TaskEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author：Lime
 * @Description: 用户中奖记录聚合对象【一个事务】
 * @Date：2024/7/15 11:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

    /** 用户中奖记录实体 */
    private UserAwardRecordEntity userAwardRecordEntity;
    /** 任务实体 */
    private TaskEntity taskEntity;

}
