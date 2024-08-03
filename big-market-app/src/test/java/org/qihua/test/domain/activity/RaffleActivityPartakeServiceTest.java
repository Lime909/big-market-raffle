package org.qihua.test.domain.activity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.qihua.domain.activity.model.entity.UserRaffleOrderEntity;
import org.qihua.domain.activity.service.IRaffleActivityPartakeService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description:
 * @Date：2024/7/14 0:31
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityPartakeServiceTest {

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Test
    public void test_create_order() {
        /** 请求参数 */
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        /** 调用接口 */
        UserRaffleOrderEntity userRaffleOrderEntity = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("调用参数:{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果:{}", JSON.toJSONString(userRaffleOrderEntity));

    }

}
