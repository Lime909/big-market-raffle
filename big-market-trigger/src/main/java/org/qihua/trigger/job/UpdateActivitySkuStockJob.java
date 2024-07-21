package org.qihua.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.qihua.domain.activity.service.IRaffleActivitySkuStockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author：Lime
 * @Description: 更新活动sku库存任务
 * @Date：2024/7/10 16:43
 */
@Slf4j
@Component()
public class UpdateActivitySkuStockJob {

    @Resource
    private IRaffleActivitySkuStockService skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try{
//            log.info("定时任务，更新活动sku库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if(activitySkuStockKeyVO == null){
                return;
            }
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
        } catch (Exception e) {
            log.error("定时任务，更新活动sku失败", e);
        }
    }
}