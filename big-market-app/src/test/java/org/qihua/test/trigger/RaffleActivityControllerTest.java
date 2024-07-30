package org.qihua.test.trigger;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.trigger.api.IRaffleActivityService;
import org.qihua.trigger.api.dto.ActivityDrawRequestDTO;
import org.qihua.trigger.api.dto.ActivityDrawResponseDTO;
import org.qihua.trigger.http.RaffleActivityController;
import org.qihua.types.model.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: Lime
 * @Description: 营销活动服务测试
 * @Date: 2024/7/27 00:38
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_armory() {
        Response<Boolean> response = raffleActivityService.armory(100301L);
        log.info("装配结果：{}", JSON.toJSONString(response));
    }

    @Test
    public void test_draw() {
        for (int i = 0; i < 20; i++) {
            ActivityDrawRequestDTO requestDTO = new ActivityDrawRequestDTO();
            requestDTO.setActivityId(100301L);
            requestDTO.setUserId("xiaofuge");
            Response<ActivityDrawResponseDTO> responseDTO = raffleActivityService.draw(requestDTO);

            log.info("请求参数：{}", JSON.toJSONString(requestDTO));
            log.info("抽奖结果：{}", JSON.toJSONString(responseDTO));
        }
    }

    @Test
    public void test_calendarSignRebate() {
        Response<Boolean> response = raffleActivityService.calendarSignRebate("xiaofuge");
        log.info("测试结果：{}", JSON.toJSONString(response));
    }
}
