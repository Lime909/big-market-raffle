package org.qihua.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lime
 * @description 抽象策略责任链，判断走什么抽奖策略
 * @date 2024-06-09 16:01:40
 */
@Slf4j
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();
}
