package org.qihua.domain.activity.service.quota.rule;

/**
 * @Author：Lime
 * @Description: 抽奖动作动作责任链抽象类
 * @Date：2024/7/3 18:06
 */
public abstract class AbstractActionChain implements IActionChain {

    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
