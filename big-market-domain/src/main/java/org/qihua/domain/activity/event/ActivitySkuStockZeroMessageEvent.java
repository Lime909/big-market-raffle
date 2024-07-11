package org.qihua.domain.activity.event;

import org.apache.commons.lang3.RandomStringUtils;
import org.qihua.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author：Lime
 * @Description: 活动sku库存清空消息
 * @Date：2024/7/10 15:07
 */
@Component
public class ActivitySkuStockZeroMessageEvent extends BaseEvent<Long> {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(sku)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
