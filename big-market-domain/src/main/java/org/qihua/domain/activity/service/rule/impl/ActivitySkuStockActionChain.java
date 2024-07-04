package org.qihua.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.ActivityCountEntity;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.service.rule.AbstractActionChain;
import org.springframework.stereotype.Component;

/**
 * @Author：Lime
 * @Description: 商品库存规则节点
 * @Date：2024/7/3 18:14
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {


    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");

//        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
        return true;
    }
}
