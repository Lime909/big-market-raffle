package org.qihua.domain.award.service;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.award.event.SendAwardMessageEvent;
import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.DistributeAwardEntity;
import org.qihua.domain.award.model.entity.TaskEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.model.valobj.TaskStateVO;
import org.qihua.domain.award.repository.IAwardRepository;
import org.qihua.domain.award.service.distribute.IDistributeAward;
import org.qihua.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author：Lime
 * @Description: 奖品服务
 * @Date：2024/7/15 11:49
 */
@Slf4j
@Service
public class AwardService implements IAwardService {

    private final IAwardRepository awardRepository;
    private final SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        /** 构建消息对象 */
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        /** 构建任务对象 */
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);

        /** 构建聚合对象 */
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        /** 存储聚合对象 -一个事务下的用户中奖记录 */
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);

        log.info("中奖记录保存完成 userId:{} orderId:{}", userAwardRecordEntity.getUserId(), userAwardRecordEntity.getOrderId());
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        /** 奖品Key */
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if(awardKey == null){
            log.error("分发奖品，奖品ID不存在 awardKey:{}", awardKey);
            return;
        }

        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if(distributeAward == null){
            log.error("分发奖品，相应的服务不存在 awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }

        /** 发送奖品 */
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }

    @Override
    public List<UserAwardRecordEntity> queryUserAwardRecord(String userId) {
        return awardRepository.queryUserAwardRecord(userId);
    }

}
