package org.qihua.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.ActivityCountEntity;
import org.qihua.domain.activity.model.entity.ActivityEntity;
import org.qihua.domain.activity.model.entity.ActivitySkuEntity;
import org.qihua.domain.activity.model.valobj.ActivityStateVO;
import org.qihua.domain.activity.service.rule.AbstractActionChain;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        log.info("活动责任链-基础信息【有效期、状态、库存】校验开始。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        /** 校验活动状态 */
        if (!activityEntity.getState().equals(ActivityStateVO.open)) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        /** 校验活动日期 */
        Date currentDate = new Date();
        /** 开始时间在现在时间之后？ || 结束时间在现在时间之前？ */
        if (activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        /** 检验活动sku库存 */
        if (activitySkuEntity.getStockCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
        }
        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}