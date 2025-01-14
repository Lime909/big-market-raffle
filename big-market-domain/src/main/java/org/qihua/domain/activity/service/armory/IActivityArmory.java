package org.qihua.domain.activity.service.armory;

/**
 * @Author：Lime
 * @Description: 活动装配预热
 * @Date：2024/7/10 10:52
 */
public interface IActivityArmory {

    boolean assembleActivitySkuByActivityId(Long activityId);

    boolean assembleActivitySku(Long sku);

}
