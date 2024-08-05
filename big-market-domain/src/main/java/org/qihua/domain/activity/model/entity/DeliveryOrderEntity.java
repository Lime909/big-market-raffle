package org.qihua.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Lime
 * @Description: 出货单实体对象
 * @Date: 2024/8/5 18:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderEntity {

    /** 用户ID */
    private String userId;
    /** 业务防重ID */
    private String outBusinessNo;

}
