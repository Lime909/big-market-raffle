package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.qihua.infrastructure.persistent.po.StrategyAward;

import java.util.List;

/**
 * @author Lime
 * @description 抽奖策略奖品明细配置 - 概率、规则 DAO
 * @date 2024-06-03 09:41:07
 */
@Mapper
public interface IStrategyAwardDao {
     List<StrategyAward> queryStrategyAwardList();

     List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);
}
