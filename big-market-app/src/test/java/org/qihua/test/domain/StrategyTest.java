package org.qihua.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.strategy.service.armory.IStrategyArmory;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 21:43:17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IStrategyDispatch strategyDispatch;

    @Test
    public void test_strategyArmory() {
        boolean success = strategyArmory.assembleStrategy(100002L);
        log.info("测试结果: {}", success);
    }

    @Test
    public void test_getAssembleRandomVal() {
        log.info("测试结果：{} - 奖品id值", strategyDispatch.getRandomAwardId(100002L));
    }

}
