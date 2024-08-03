package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.Strategy;

import java.util.List;

/**
 * @author Lime
 * @description 抽奖策略 DAO
 * @date 2024-06-03 09:40:45
 */
@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);

}
