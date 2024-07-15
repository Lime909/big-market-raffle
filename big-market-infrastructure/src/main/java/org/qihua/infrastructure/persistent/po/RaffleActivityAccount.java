package org.qihua.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 抽奖活动账户表
 * @Date：2024/6/29 14:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccount {
    /** 自增ID */
    private Long id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 总次数 */
    private Integer totalCount;
    /** 总剩余次数 */
    private Integer totalCountSurplus;
    /** 日次数 */
    private Integer dayCount;
    /** 日剩余次数 */
    private Integer dayCountSurplus;
    /** 月次数 */
    private Integer monthCount;
    /** 月剩余次数 */
    private Integer monthCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
