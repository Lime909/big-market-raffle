package org.qihua.domain.rebate.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: Lime
 * @Description: 发送返利消息事件
 * @Date: 2024/7/30 13:53
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<RebateMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebateMessage {
        /** 用户ID */
        private String userId;
        /** 返利描述 */
        private String rebateDesc;
        /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
        private String rebateType;
        /** 返利配置【sku值，积分值】 */
        private String rebateConfig;
        /** 业务ID - 拼接的唯一值 */
        private String bizId;
    }
}
