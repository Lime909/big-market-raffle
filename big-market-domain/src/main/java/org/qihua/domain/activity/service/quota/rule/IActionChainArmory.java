package org.qihua.domain.activity.service.quota.rule;

/**
 * @Author：Lime
 * @Description: 抽奖动作责任链装配
 * @Date：2024/7/3 18:04
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);

}
