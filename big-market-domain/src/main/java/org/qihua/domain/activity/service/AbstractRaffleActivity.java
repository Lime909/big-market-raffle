package org.qihua.domain.activity.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.*;
import org.qihua.domain.activity.repository.IActivityRepository;

/**
 * @Author：Lime
 * @Description: 抽奖活动抽象类，定义标准的流程
 * @Date：2024/7/3 14:02
 */
@Slf4j
public abstract class AbstractRaffleActivity implements IRaffleOrder{

    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity){
        /** 1.通过sku查询活动信息 */
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        /** 2.查询活动信息 */
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        /** 3.查询次数信息，用于可参与多少次 */
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity),JSON.toJSONString(activityCountEntity),JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }
}
