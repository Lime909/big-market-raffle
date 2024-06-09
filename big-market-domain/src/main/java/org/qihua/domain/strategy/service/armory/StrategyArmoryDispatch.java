package org.qihua.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.qihua.domain.strategy.model.entity.StrategyAwardEntity;
import org.qihua.domain.strategy.model.entity.StrategyEntity;
import org.qihua.domain.strategy.model.entity.StrategyRuleEntity;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.types.common.Constants;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Lime
 * @description
 * @date 2024-06-03 13:33:50
 */
@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory,IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;


    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1.查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 2.权重策略配置 - rule_model权重规则
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if(ruleWeight == null) return true;

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if(ruleWeight == null){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Integer>> ruleWeightValue = strategyRuleEntity.getRuleValues();
        Set<String> keys = ruleWeightValue.keySet();
        for(String key : keys){
            List<Integer> ruleValues = ruleWeightValue.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key), strategyAwardEntitiesClone);
        }

        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1.获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        // 2.获取概率值总值
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 3.获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        // 4.生成策略奖品概率查找表
        List<Integer> strategySearchTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity award : strategyAwardEntities) {
            Integer awardId = award.getAwardId();
            BigDecimal awardRate = award.getAwardRate();
            // 按照概率值计算需要加到查找表的数量
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategySearchTables.add(awardId);
            }
        }
        // 5.对策略奖品查找表进行乱序
        Collections.shuffle(strategySearchTables);
        // 6.生成Map，通过概率来获得相应的奖品id
        Map<Integer, Integer> shuffleAwardSearchTable = new LinkedHashMap<>();
        for (int i = 0; i < strategySearchTables.size(); i++) {
            shuffleAwardSearchTable.put(i, strategySearchTables.get(i));
        }

        // 7.存储进入redis
        repository.storeStrategyAwardSearchRateTable(key, shuffleAwardSearchTable.size(), shuffleAwardSearchTable);
    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeight) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeight);
        return getRandomAwardId(key);
    }

    @Override
    public Integer getRandomAwardId(String key){
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
