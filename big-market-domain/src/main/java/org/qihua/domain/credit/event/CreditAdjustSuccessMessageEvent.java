package org.qihua.domain.credit.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Lime
 * @Description: 积分
 * @Date: 2024/8/5 18:39
 */
@Component
public class CreditAdjustSuccessMessageEvent extends BaseEvent<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;

    @Override
    public EventMessage<CreditAdjustSuccessMessage> buildEventMessage(CreditAdjustSuccessMessage data) {
        return EventMessage.<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>builder()
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
    public static class CreditAdjustSuccessMessage {
        /** 用户ID */
        private String userId;
        /** 订单ID */
        private String orderId;
        /** 交易金额 */
        private BigDecimal amount;
        /** 业务防重ID */
        private String outBusinessNo;
    }
}
