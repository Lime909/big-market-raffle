package org.qihua.domain.credit.service;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.credit.model.aggregate.TradeAggregate;
import org.qihua.domain.credit.model.entity.CreditAccountEntity;
import org.qihua.domain.credit.model.entity.CreditOrderEntity;
import org.qihua.domain.credit.model.entity.TradeEntity;
import org.qihua.domain.credit.repository.ICreditRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Lime
 * @Description: 积分调额服务【正向、逆向，增减积分】
 * @Date: 2024/8/5 10:27
 */
@Slf4j
@Service
public class CreditAdjustService implements ICreditAdjustService {

    @Resource
    private ICreditRepository creditRepository;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        log.info("增加账户积分额度开始 userId:{} tradeName:{} amount:{}", tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getTradeAmount());
        /** 1.创建用户积分实体 */
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(tradeEntity.getUserId(), tradeEntity.getTradeAmount());

        /** 2.创建账户订单实体 */
        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(tradeEntity.getUserId(), tradeEntity.getTradeName(),
                tradeEntity.getTradeType(), tradeEntity.getTradeAmount(), tradeEntity.getOutBusinessNo());

        /** 3.构建交易聚合对象 */
        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditAccountEntity(creditAccountEntity)
                .creditOrderEntity(creditOrderEntity)
                .build();

        creditRepository.saveUserCreditTradeOrder(tradeAggregate);

        return creditOrderEntity.getOrderId();
    }
}
