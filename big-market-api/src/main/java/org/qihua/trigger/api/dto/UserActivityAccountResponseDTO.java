package org.qihua.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Lime
 * @Description: 用户活动账户应答对象
 * @Date: 2024/7/31 21:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityAccountResponseDTO {
    /** 总次数 */
    private Integer totalCount;
    /** 总剩余次数 */
    private Integer totalCountSurplus;
    /** 日次数 */
    private Integer dayCount;
    /** 日剩余次数 */
    private Integer dayCountSurplus;
    /** 月次数 */
    private Integer monthCount;
    /** 月剩余次数 */
    private Integer monthCountSurplus;
}
