package org.qihua.domain.activity.service;

import org.qihua.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * @Author：Lime
 * @Description: 活动库存接口
 * @Date：2024/7/10 15:34
 */
public interface ISkuStock {

    /**
     * 获取活动sku库存消耗队列
     * @return 奖品库存key信息
     * @throws InterruptedException
     */
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 延迟队列+任务趋势更新活动sku库存
     * @param sku
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 延迟库存以消耗完毕，清空数据库库存
     * @param sku
     */
    void clearActivitySkuStock(Long sku);
}
