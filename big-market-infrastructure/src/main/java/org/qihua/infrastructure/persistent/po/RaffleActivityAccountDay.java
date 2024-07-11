package org.qihua.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 抽奖活动账户表-日次数
 * @Date：2024/7/11 22:58
 */
@Data
public class RaffleActivityAccountDay {

    /** 自增ID */
    private Long id;
    /** 用户ID */
    private String userId;
    /** 日期（yyyy-mm-dd） */
    private String day;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
