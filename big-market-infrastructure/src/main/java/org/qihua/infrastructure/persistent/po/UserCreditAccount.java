package org.qihua.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Lime
 * @Description: 用户积分账户
 * @Date: 2024/8/4 00:42
 */
@Data
public class UserCreditAccount {
    /** 自增ID */
    private int id;
    /** 用户ID */
    private String userId;
    /** 总积分，显示总账户值 */
    private BigDecimal totalAmount;
    /** 可用积分，每次扣减的值 */
    private BigDecimal availableAmount;
    /** 账户状态【open-可用 close-冻结】*/
    private String accountStatus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
