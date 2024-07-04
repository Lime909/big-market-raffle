package org.qihua.domain.activity.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.activity.model.aggregate.CreateOrderAggregate;
import org.qihua.domain.activity.model.entity.*;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.rule.IActionChain;
import org.qihua.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;

/**
 * @Author：Lime
 * @Description: 抽奖活动抽象类，定义标准的流程
 * @Date：2024/7/3 14:02
 */
@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{


    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        /** 1.参数校验 */
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if(StringUtils.isBlank(userId) || sku == null || StringUtils.isBlank(outBusinessNo)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        /** 2.查询基础信息 */
        /** 2.1.通过sku查询活动信息 */
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        /** 2.2.查询活动信息 */
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        /** 2.3.查询次数信息 */
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        /** 3.活动动作规则校验 */
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        /** 4.构建订单聚合对象 */
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        /** 5.保存订单 */
        doSaveOrder(createOrderAggregate);

        /** 6.返回单号 */
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) ;
}
