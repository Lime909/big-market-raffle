package org.qihua.domain.activity.service.quota.product;

import org.qihua.domain.activity.model.entity.SkuProductEntity;
import org.qihua.domain.activity.repository.IActivityRepository;
import org.qihua.domain.activity.service.IRaffleActivitySkuProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Lime
 * @Description: sku商品服务
 * @Date: 2024/8/5 23:57
 */
@Service
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {

    @Resource
    private IActivityRepository repository;

    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }
}
