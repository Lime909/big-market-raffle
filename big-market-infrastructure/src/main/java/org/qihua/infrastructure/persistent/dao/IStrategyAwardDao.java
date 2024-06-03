package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.StrategyAward;

import java.util.List;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 09:41:07
 */
@Mapper
public interface IStrategyAwardDao {
     List<StrategyAward> queryStrategyAward();
}
