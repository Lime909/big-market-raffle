package org.qihua.domain.strategy.service.rule.chain;

/**
 * @author Lime
 * @description 责任链装配
 * @date 2024-06-09 15:49:27
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain chain);
}
