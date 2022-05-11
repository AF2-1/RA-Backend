package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyRank;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StrategyRepository extends MongoRepository<Strategy, String> {

    Optional<Strategy> findBySimulationIdAndName(String simulationId, String name);

    List<Strategy> findAllBySimulationId(String simulationId);

    @Aggregation(pipeline = {
            "    { $lookup: {\n" +
                    "           from: \"simulations\",\n" +
                    "           localField: \"simulationId\",\n" +
                    "           foreignField: \"_id\",\n" +
                    "           as: \"simulation\"\n" +
                    "         }\n" +
                    "    }\n",
            "    { $match: { $and : [{ userId: { $ne: null }}, { \"evaluationResults.cagr\": { $ne: null }}]}}\n",
            "    { $sort : { \"evaluationResults.cagr\" : -1 } }\n",
            "    { $limit : 5 }\n",
            "    { $project : {\n" +
                    "        _id : 0,\n" +
                    "        strategyId : '$_id',\n" +
                    "        userId : 1,\n" +
                    "        simulation : 1,\n" +
                    "        recommendedPf : 1,\n" +
                    "        evaluationResults : 1,\n" +
                    "        dailyValues : 1,\n" +
                    "        dailyWeights : 1,\n" +
                    "        strategyName : '$name',\n" +
                    "        rebalancingWeighs : 1\n" +
                    "        }\n" +
                    "    }"
    })
    AggregationResults<StrategyRank> findFiveRanksAboutCagr();
}
