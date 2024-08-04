package org.qihua.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.model.entity.UserCreditAwardEntity;
import org.qihua.domain.award.model.valobj.AwardStateVO;

import java.math.BigDecimal;

/**
 * @Author: Lime
 * @Description: 发放奖品聚合对象
 * @Date: 2024/8/4 08:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {

    /** 用户ID */
    private String userId;
    /** 用户发奖记录 */
    private UserAwardRecordEntity userAwardRecord;
    /** 用户积分奖品 */
    private UserCreditAwardEntity userCreditAward;

    public static UserAwardRecordEntity buildDistributeUserAwardRecord(String userId, String orderId, Integer awardId, AwardStateVO awardState) {
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardState(awardState);
        return userAwardRecord;
    }

    public static UserCreditAwardEntity buildCreditUserAwardEntity(String userId, BigDecimal creditAmount) {
        UserCreditAwardEntity userCreditAward = new UserCreditAwardEntity();
        userCreditAward.setUserId(userId);
        userCreditAward.setCreditAmount(creditAmount);
        return userCreditAward;
    }
}
