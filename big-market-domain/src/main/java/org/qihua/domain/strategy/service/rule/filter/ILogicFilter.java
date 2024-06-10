package org.qihua.domain.strategy.service.rule.filter;

import org.qihua.domain.strategy.model.entity.RuleActionEntity;
import org.qihua.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author Lime
 * @description 过滤器逻辑
 * @date 2024-06-05 15:21:59
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
