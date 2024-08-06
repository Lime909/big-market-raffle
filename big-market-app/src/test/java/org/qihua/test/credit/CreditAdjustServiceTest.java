package org.qihua.test.credit;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qihua.domain.credit.model.entity.TradeEntity;
import org.qihua.domain.credit.model.valobj.TradeNameVO;
import org.qihua.domain.credit.model.valobj.TradeTypeVO;
import org.qihua.domain.credit.service.ICreditAdjustService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Lime
 * @Description: 积分调额服务测试
 * @Date: 2024/8/5 16:04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditAdjustServiceTest {

    @Resource
    public ICreditAdjustService creditAdjustService;

    @Test
    public void test_createOrder_forward() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.FORWARD);
        tradeEntity.setAmount(new BigDecimal("10.19"));
        tradeEntity.setOutBusinessNo("100009900001");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_reverse() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-20.19"));
        tradeEntity.setOutBusinessNo("100009900002");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_pay() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("70009240609002");
        creditAdjustService.createOrder(tradeEntity);

        new CountDownLatch(1).await();
    }


}
