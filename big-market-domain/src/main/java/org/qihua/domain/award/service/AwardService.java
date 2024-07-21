package org.qihua.domain.award.service;

import org.qihua.domain.award.event.SendAwardMessageEvent;
import org.qihua.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.qihua.domain.award.model.entity.TaskEntity;
import org.qihua.domain.award.model.entity.UserAwardRecordEntity;
import org.qihua.domain.award.model.valobj.TaskStateVO;
import org.qihua.domain.award.repository.IAwardRepository;
import org.qihua.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description: 奖品服务
 * @Date：2024/7/15 11:49
 */
@Service
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        /** 构建消息对象 */
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

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
    }

}
