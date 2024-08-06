package org.qihua.trigger.api.dto;

import lombok.Data;

/**
 * @Author: Lime
 * @Description: 商品购物车请求对象
 * @Date: 2024/8/5 23:50
 */
@Data
public class SkuProductShopCartRequestDTO {

    /** 用户ID */
    private String userId;
    /** sku商品 */
    private Long sku;

}
