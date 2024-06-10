package org.qihua.domain.strategy.model.volobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Lime
 * @description 规则树对象
 * @date 2024-06-10 11:16:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {
    /** 规则树ID */
    private String treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则根节点 */
    private String treeRootRuleNode;
    /** 规则节点 */
    private Map<String, RuleTreeNodeVO> treeNodeMap;

}
