package org.qihua.domain.activity.service.quota;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.qihua.domain.activity.model.entity.*;
import org.qihua.domain.activity.model.valobj.OrderTradeTypeVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.qihua.domain.activity.service.quota.policy.ITradePolicy;
import org.qihua.domain.activity.service.quota.rule.IActionChain;
import org.qihua.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author：Lime
 * @Description: 抽奖活动抽象类，定义标准的流程
 * @Date：2024/7/3 14:02
 */
@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

    private final Map<String, ITradePolicy> tradePolicyMap;

    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory,
                                              Map<String, ITradePolicy> tradePolicyMap) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyMap = tradePolicyMap;
    }

    @Override
    public UnpaidActivityOrderEntity createOrder(SkuRechargeEntity skuRechargeEntity) {
        /** 1.参数校验 */
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (StringUtils.isBlank(userId) || sku == null || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        /** 2.查询未支付订单「一个月以内的未支付订单」 */
        UnpaidActivityOrderEntity unpaidActivityOrder = activityRepository.queryUnpaidActivityOrder(skuRechargeEntity);
        if(unpaidActivityOrder != null) {
            return unpaidActivityOrder;
        }

        /** 3.1 通过sku查询活动信息 */
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        /** 3.2.查询活动信息 */
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        /** 3.3.查询次数信息「用户在活动可参与的次数」 */
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        /** 4. 账户额度 【交易属性的兑换，需要校验额度账户】*/
        if (skuRechargeEntity.getOrderTradeType().equals(OrderTradeTypeVO.credit_pay_trade)) {
            BigDecimal availableAmount = activityRepository.queryUserCreditAccountAmount(userId);
            if (availableAmount.compareTo(activitySkuEntity.getProductAmount()) < 0) {
                throw new AppException(ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getCode(), ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getInfo());
            }
        }

        /** 6.活动动作规则校验 「过滤失败则直接抛异常」- 责任链扣减sku库存*/
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        /** 7.构建订单聚合对象 */
        CreateQuotaOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        /** 8.交易策略 - 【积分兑换，支付类订单】【返利无支付交易订单，直接充值到账】【订单状态变更交易类型策略】 */
        ITradePolicy tradePolicy = tradePolicyMap.get(skuRechargeEntity.getOrderTradeType().getCode());
        tradePolicy.trade(createOrderAggregate);

        /** 9.返回订单信息 */
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        return UnpaidActivityOrderEntity.builder()
                .userId(userId)
                .orderId(activityOrderEntity.getOrderId())
                .outBusinessNo(activityOrderEntity.getOutBusinessNo())
                .payAmount(activityOrderEntity.getPayAmount())
                .build();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
