package org.qihua.test.domain.debate;

/**
 * @Author: Lime
 * @Description:
 * @Date: 2024/7/30 15:44
 */

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.activity.service.armory.IActivityArmory;
import org.qihua.domain.rebate.model.entity.BehaviorEntity;
import org.qihua.domain.rebate.model.valobj.BehaviorTypeVO;
import org.qihua.domain.rebate.service.IBehaviorRebateService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 行为返利单测
 * @create 2024-04-30 17:53
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BehaviorRebateServiceTest {

    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IBehaviorRebateService behaviorRebateService;

    @Before
    public void init(){
        activityArmory.assembleActivitySkuByActivityId(100301L);
    }

    @Test
    public void test_createOrder() throws InterruptedException {
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId("xiaofuge");
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
        // 重复的 OutBusinessNo 会报错唯一索引冲突，这也是保证幂等的手段，确保不会多记账
        behaviorEntity.setOutBusinessNo("20230503");

        List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
        log.info("请求参数：{}", JSON.toJSONString(behaviorEntity));
        log.info("测试结果：{}", JSON.toJSONString(orderIds));

        new CountDownLatch(1).await();
    }
}
