package org.qihua.domain.activity.service;

import org.qihua.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @Author：Lime
 * @Description: 抽奖活动服务
 * @Date：2024/7/3 15:52
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity{

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
