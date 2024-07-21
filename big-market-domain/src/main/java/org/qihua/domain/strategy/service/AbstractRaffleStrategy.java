package org.qihua.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.strategy.model.entity.RaffleAwardEntity;
import org.qihua.domain.strategy.model.entity.RaffleFactorEntity;
import org.qihua.domain.strategy.model.entity.StrategyAwardEntity;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.model.volobj.StrategyAwardRuleModelVO;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
import org.qihua.domain.strategy.service.rule.chain.ILogicChain;
import org.qihua.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.qihua.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;

/**
 * @author Lime
 * @description 抽奖策略抽象类，定义标准流程
 * @date 2024-06-05 15:18:45
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {
    /**
     * 策略仓储服务
     */
    protected IStrategyRepository repository;
    /**
     * 策略调度服务
     */
    protected IStrategyDispatch strategyDispatch;
    /**
     * 抽奖的责任链
     */
    protected final DefaultChainFactory defaultChainFactory;
    /**
     * 抽奖的决策树
     */
    protected final DefaultTreeFactory defaultTreeFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch dispatch
            , DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.repository = repository;
        this.strategyDispatch = dispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        /** 1.参数校验 */
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        /** 2.责任链抽奖计算 */
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        if (!chainStrategyAwardVO.getLogicModel().equals(DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode())) {
            return buildRaffleAwardEntity(strategyId, chainStrategyAwardVO.getAwardId(), null);
        }

        /** 3.规则树抽奖过滤 */
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        /** 4.返回抽奖结果 */
        return buildRaffleAwardEntity(strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

    }

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig) {
        StrategyAwardEntity strategyAward = repository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardConfig(awardConfig)
                .sort(strategyAward.getSort())
                .build();
    }

    /**
     * 抽奖计算，责任链抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    /**
     * 抽奖结果过滤，决策树抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);

}
