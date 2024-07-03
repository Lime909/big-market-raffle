package org.qihua.test.domain.activity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.activity.model.entity.ActivityOrderEntity;
import org.qihua.domain.activity.model.entity.ActivityShopCartEntity;
import org.qihua.domain.activity.service.IRaffleOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description: 抽奖活动订单单元测试
 * @Date：2024/7/3 16:17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {

    @Resource
    private IRaffleOrder raffleOrder;

    @Test
    public void test_createRaffleActivityOrder() {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("lime");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity activityOrderEntity = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果： {}" , JSON.toJSONString(activityOrderEntity));
    }
}
