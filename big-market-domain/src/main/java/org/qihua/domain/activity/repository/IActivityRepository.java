package org.qihua.domain.activity.repository;

import org.qihua.domain.activity.model.aggregate.CreateOrderAggregate;
import org.qihua.domain.activity.model.entity.ActivityCountEntity;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动仓储接口
 * @Date：2024/7/3 14:04
 */
public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO);

    ActivitySkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
