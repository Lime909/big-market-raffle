package org.qihua.test.trigger;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.strategy.service.IRaffleStrategy;
import org.qihua.trigger.api.IRaffleStrategyService;
import org.qihua.trigger.api.dto.RaffleAwardListRequestDTO;
import org.qihua.trigger.api.dto.RaffleAwardListResponseDTO;
import org.qihua.types.model.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Lime
 * @Description: 营销抽奖服务测试
 * @Date: 2024/7/26 23:38
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyControllerTest {

    @Resource
    private IRaffleStrategyService raffleStrategyService;

    @Test
    public void test_RaffleAwardList() {
        RaffleAwardListRequestDTO requestDTO = new RaffleAwardListRequestDTO();
        requestDTO.setUserId("xiaofuge");
        requestDTO.setActivityId(100301L);
        Response<List<RaffleAwardListResponseDTO>> responseDTO = raffleStrategyService.queryRaffleAwardList(requestDTO);

        log.info("请求参数：{}", JSON.toJSONString(requestDTO));
        log.info("测试结果：{}", JSON.toJSONString(responseDTO));
    }

}
