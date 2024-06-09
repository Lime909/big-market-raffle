package org.qihua.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.strategy.model.entity.RaffleAwardEntity;
import org.qihua.domain.strategy.model.entity.RaffleFactorEntity;
import org.qihua.domain.strategy.model.entity.RuleActionEntity;
import org.qihua.domain.strategy.model.entity.StrategyEntity;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.model.volobj.StrategyAwardRuleModelVO;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
import org.qihua.domain.strategy.service.rule.chain.ILogicChain;
import org.qihua.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.qihua.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.qihua.types.enums.ResponseCode;
import org.qihua.types.exception.AppException;

/**
 * @author Lime
 * @description 抽奖策略抽象类，定义标准流程
 * @date 2024-06-05 15:18:45
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {
    /** 策略仓储服务 */
    protected IStrategyRepository repository;
    /** 策略调度服务 */
    protected IStrategyDispatch dispatch;
    /** 抽奖的责任链 */
    private final DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch dispatch
    , DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.dispatch = dispatch;
        this.defaultChainFactory = defaultChainFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        /** 1.参数校验 */
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(strategyId == null || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        /** 2.获取抽奖责任链 - 前置规则的责任链处理 */
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        /** 3.抽奖前 - 规则过滤 */
        Integer awardId = logicChain.logic(userId, strategyId);
        /** 4.查询奖品规则「抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）」*/
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        /** 5.抽奖中 - 规则过滤 */
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity =
                this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                        .userId(userId)
                        .strategyId(strategyId)
                        .awardId(awardId)
                        .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (ruleActionCenterEntity.getCode().equals(RuleLogicCheckTypeVO.TAKE_OVER.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }


        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}