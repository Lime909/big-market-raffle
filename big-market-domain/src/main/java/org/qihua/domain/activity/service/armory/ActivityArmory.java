package org.qihua.domain.activity.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动装配，sku预热
 * @Date：2024/7/10 10:56
 */
@Slf4j
@Service
public class ActivityArmory implements IActivityArmory, IActivityDispatch {

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        /** 预热活动sku */
        cacheActivitySkuStockCount(sku, activitySkuEntity.getStockCountSurplus());
        /** 预热活动【查询时预热到缓存】*/
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        /** 预热活动次数【查询时预热到缓存】*/
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySkuStockCount(cacheKey, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractionActivitySkuStock(sku, cacheKey, endDateTime);
    }
}
