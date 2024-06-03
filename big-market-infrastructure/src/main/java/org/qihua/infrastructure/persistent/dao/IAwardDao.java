package org.qihua.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.qihua.infrastructure.persistent.po.Award;

import java.util.List;


/**
 * @author Lime
 * @description
 * @date 2024-06-03 09:40:28
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
