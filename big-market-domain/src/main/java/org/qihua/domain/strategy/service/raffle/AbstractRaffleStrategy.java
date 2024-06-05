package org.qihua.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qihua.domain.strategy.model.entity.RaffleAwardEntity;
import org.qihua.domain.strategy.model.entity.RaffleFactorEntity;
import org.qihua.domain.strategy.model.entity.RuleActionEntity;
import org.qihua.domain.strategy.model.entity.StrategyEntity;
import org.qihua.domain.strategy.model.volobj.RuleLogicCheckTypeVO;
import org.qihua.domain.strategy.repository.IStrategyRepository;
import org.qihua.domain.strategy.service.IRaffleStrategy;
import org.qihua.domain.strategy.service.armory.IStrategyDispatch;
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

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch dispatch) {
        this.repository = repository;
        this.dispatch = dispatch;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        /** 1.参数校验 */
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(strategyId == null || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        /** 2.策略查询 */
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        /** 3.抽奖前 - 规则过滤 */
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity =
                this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder()
                        .userId(userId)
                        .strategyId(strategyId)
                        .build(), strategy.ruleModels());
        if(ruleActionEntity.getCode().equals(RuleLogicCheckTypeVO.TAKE_OVER.getCode())){
            if(ruleActionEntity.getRuleModel().equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())) {
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
//            } else if (ruleActionEntity.getCode().equals(DefaultLogicFactory.LogicModel.RULE_WHITELIST.getCode())) {
//                return RaffleAwardEntity.builder()
//                        .awardId(ruleActionEntity.getData().getAwardId())
//                        .build();
            } else if(ruleActionEntity.getRuleModel().equals(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode())){
                RuleActionEntity.RaffleBeforeEntity ruleBefore = ruleActionEntity.getData();
                String ruleWeightValueKey = ruleBefore.getRuleWeightValueKey();
                Integer awardId = dispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }
        /** 4.默认抽奖流程 */
        Integer awardId = dispatch.getRandomAwardId(strategyId);

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}