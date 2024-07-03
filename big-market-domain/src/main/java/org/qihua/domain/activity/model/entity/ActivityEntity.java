package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.activity.model.valobj.ActivityStateVO;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动实体对象
 * @Date：2024/7/3 13:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 活动描述 */
    private String activityDesc;
    /** 活动开始时间 */
    private Date beginDateTime;
    /** 活动结束时间 */
    private Date endDateTime;
    /** 活动参与次数配置 */
    private Long activityCountId;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 活动状态 */
    private ActivityStateVO state;
}
