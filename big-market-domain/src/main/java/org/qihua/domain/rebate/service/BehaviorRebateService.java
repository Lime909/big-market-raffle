package org.qihua.domain.rebate.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.domain.rebate.event.SendRebateMessageEvent;
import org.qihua.domain.rebate.model.aggerate.BehaviorRebateAggregate;
import org.qihua.domain.rebate.model.entity.BehaviorEntity;
import org.qihua.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.qihua.domain.rebate.model.entity.TaskEntity;
import org.qihua.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import org.qihua.domain.rebate.model.valobj.TaskStateVO;
import org.qihua.domain.rebate.repository.IBehaviorRebateRepository;
import org.qihua.types.common.Constants;
import org.qihua.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Lime
 * @Description:
 * @Date: 2024/7/30 08:51
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {

        /** 1.查询返利配置 */
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if (dailyBehaviorRebateVOS == null || dailyBehaviorRebateVOS.isEmpty()) {
            return new ArrayList<>();
        }

        /** 2.构建聚合对象 */
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for(DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            /** 业务ID = 用户ID + 返利类型 + 外部透传业务ID */
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();

            /** 行为返利下单实体 */
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            /** MQ消息实体 */
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .bizId(bizId)
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .build();

            /** 构建事件信息 */
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            /** 组装任务对象 */
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setState(TaskStateVO.complete);

            /** 组装聚合对象 */
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .build();
            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        /** 3.存储聚合对象数据 */
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);

        /** 4.返回订单ID集合 */
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }
}
