package org.qihua.domain.strategy.repository;

import org.qihua.domain.strategy.model.entity.StrategyAwardEntity;
import org.qihua.domain.strategy.model.entity.StrategyEntity;
import org.qihua.domain.strategy.model.entity.StrategyRuleEntity;
import org.qihua.domain.strategy.model.volobj.RuleTreeVO;
import org.qihua.domain.strategy.model.volobj.StrategyAwardRuleModelVO;
import org.qihua.domain.strategy.model.volobj.StrategyAwardStockKeyVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 13:46:18
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    /**
     * 根据规则树ID，查询树结构信息
     *
     * @param treeId 规则树ID
     * @return 树结构信息
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**
     * 缓存奖品库存
     *
     * @param cacheKey
     * @param awardCount
     */
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    /**
     * 缓存key，decr扣减库存
     *
     * @param cacheKey
     * @return
     */
    Boolean subtractionAwardStock(String cacheKey);

    /**
     * 缓存key，decr扣减库存
     *
     * @param cacheKey
     * @param endTime
     * @return
     */
    Boolean subtractionAwardStock(String cacheKey, Date endTime);

    /**
     * 写入奖品库存消费队列
     *
     * @param strategyAwardStockKeyVO
     */
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    /**
     * 获取奖品库存消费队列
     *
     * @return
     * @throws InterruptedException
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品消耗库存
     *
     * @param strategyId
     * @param awardId
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    /**
     * 根据策略ID+奖品ID的唯一值组合，查询奖品信息
     *
     * @param strategyId
     * @param awardId
     * @return StrategyAwardEntity
     */
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    /**
     * 查询策略ID
     *
     * @param activityId
     * @return
     */
    Long queryStrategyIdByActivityId(Long activityId);

    /**
     * 查询今日抽奖次数
     *
     * @param userId
     * @param strategyId
     * @return
     */
    Integer queryTodayUserRaffleCount(String userId, Long strategyId);

    /**
     * 根据规则树ID集合查询奖品中加锁数量的配置「部分奖品需要抽奖N次解锁」
     *
     * @param treeIds 规则树ID值
     * @return key 规则树，value rule_lock 加锁值
     */
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
