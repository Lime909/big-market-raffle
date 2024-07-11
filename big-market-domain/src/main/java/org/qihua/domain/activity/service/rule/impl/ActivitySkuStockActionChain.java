package org.qihua.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.ActivityCountEntity;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.armory.IActivityDispatch;
import org.qihua.domain.activity.service.rule.AbstractActionChain;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description: 商品库存规则节点
 * @Date：2024/7/3 18:14
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始，sku：{} activityId：{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        /** 扣减库存 */
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        /** true：库存扣减成功 */
        if (status) {
            log.info("活动责任链-商品库存处理【校验&扣减】成功，sku：{} activityId：{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

            /** 写入延迟队列，延迟消费更新库存记录 */
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());

            return true;
        }

        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
    }
}
