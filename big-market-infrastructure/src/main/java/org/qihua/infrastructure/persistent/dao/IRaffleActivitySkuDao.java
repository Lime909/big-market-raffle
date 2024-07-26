package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.RaffleActivitySku;

import java.util.List;

/**
 * @Author：Lime
 * @Description: 商品skuDao
 * @Date：2024/7/3 11:27
 */
@Mapper
public interface IRaffleActivitySkuDao {

    RaffleActivitySku queryActivitySku(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    List<RaffleActivitySku> queryActivitySkuListByActivityId(Long activityId);
}
