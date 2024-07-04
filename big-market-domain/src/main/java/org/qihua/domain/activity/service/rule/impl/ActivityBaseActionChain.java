package org.qihua.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.ActivityCountEntity;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.service.rule.AbstractActionChain;
import org.springframework.stereotype.Component;

/**
 * @Author：Lime
 * @Description: 活动规则过滤【日期、状态】
 * @Date：2024/7/3 18:13
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
