package org.qihua.domain.activity.service.armory;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动调度【扣减库存】
 * @Date：2024/7/10 11:30
 */
public interface IActivityDispatch {

    /**
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     *
     * @param sku
     * @param endDateTime
     * @return
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);

}
