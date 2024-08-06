package org.qihua.domain.activity.service;

import org.qihua.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @Author: Lime
 * @Description: sku商品服务
 * @Date: 2024/8/5 23:23
 */
public interface IRaffleActivitySkuProductService {

    /**
     * 查询当前活动ID下，创建的 sku 商品。「sku可以兑换活动抽奖次数」
     * @param activityId 活动ID
     * @return 返回sku商品集合
     */
    List<SkuProductEntity> querySkuProductByActivityId(Long activityId);

}
