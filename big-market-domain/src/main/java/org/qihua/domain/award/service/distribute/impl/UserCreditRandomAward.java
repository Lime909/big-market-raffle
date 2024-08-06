package org.qihua.domain.award.service.distribute.impl;

import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.qihua.domain.award.model.entity.DistributeAwardEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.model.entity.UserCreditAwardEntity;
import org.qihua.domain.award.model.valobj.AwardStateVO;
import org.qihua.domain.award.repository.IAwardRepository;
import org.qihua.domain.award.service.distribute.IDistributeAward;
import org.qihua.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @Author: Lime
 * @Description: 用户随机积分奖励发放
 * @Date: 2024/8/4 00:54
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        Integer awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();
        /** 配置如果为空就需要去数据库查询 */
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = repository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值 如1，100");
        }

        /** 生成随机积分值 */
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        /** 构建聚合对象 */
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete);

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(
                distributeAwardEntity.getUserId(),
                creditAmount);

        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAwardEntity);

        /** 存储发奖奖品 */
        repository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);

    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
