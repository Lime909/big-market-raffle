package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.RuleTreeNodeLine;

import java.util.List;

/**
 * @author Lime
 * @description 规则树节点连线表Dao
 * @date 2024-06-11 00:39:39
 */
@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);

}

