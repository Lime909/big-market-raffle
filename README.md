Make by Lime

设计模式上采用DDD架构进行领域分离
从流程上划分领域：
1、签到获取抽奖积分以及抽奖次数的“返利领域”
2、负责可能的积分扣减动作以及积分增加的“积分领域”
3、管理不同策略的“活动领域”
4、实际抽奖行为的“抽奖领域”
5、发放奖品行为的“奖品领域”
6、用于消息队列任务发放的“任务领域”
