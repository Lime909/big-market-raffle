package org.qihua.trigger.api;

import org.qihua.trigger.api.dto.ActivityDrawRequestDTO;
import org.qihua.trigger.api.dto.ActivityDrawResponseDTO;
import org.qihua.types.model.Response;

/**
 * @Author: Lime
 * @Description:
 * @Date: 2024/7/23 22:36
 */
public interface IRaffleActivityService {


    /**
     * 活动装配，数据预热缓存
     * @param activityId
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request
     * @return
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

}
