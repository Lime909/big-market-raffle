package org.qihua.domain.award.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author：Lime
 * @Description:
 * @Date：2024/7/15 11:28
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
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
    public static class SendAwardMessage {
        /**
         * 用户ID
         */
        private String userId;
        /**
         * 订单ID
         */
        private String orderId;
        /**
         * 奖品ID
         */
        private Integer awardId;
        /**
         * 奖品名称
         */
        private String awardTitle;
        /**
         * 奖品配置信息
         */
        private String awardConfig;
    }

}
