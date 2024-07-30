package org.qihua.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.entity.SkuRechargeEntity;
import org.qihua.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.qihua.domain.rebate.event.SendRebateMessageEvent;
import org.qihua.domain.rebate.model.valobj.RebateTypeVO;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.event.BaseEvent;
import org.qihua.types.exception.AppException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Lime
 * @Description: 监听：行为返利消息
 * @Date: 2024/7/30 21:09
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户行为返利消息 topic:{} message:{}", topic, message);
            /** 1.转换对象 */
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if (!rebateMessage.getRebateType().equals(RebateTypeVO.SKU.getCode())) {
                log.info("监听用户行为返利消息 - 非sku奖励暂时不处理 topic:{} message:{}", topic, message);
                return;
            }

            /** 2.入账奖励 */
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        } catch (AppException e) {
            if (e.getCode().equals(ResponseCode.INDEX_DUP.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic:{} message:{}", topic, message, e);
            throw e;
        }
    }
}
