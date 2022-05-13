package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.simulation.SimulationReader;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.Ranker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Component
public class StrategyReaderImpl implements StrategyReader {
    private final StrategyRepository strategyRepository;
    private final MongoTemplate mongoTemplate;
    private final SimulationReader simulationReader;

    @Override
    public List<Strategy> findAll(Sort sort) {
        return strategyRepository.findAll();
    }

    @Override
    public Optional<Strategy> findBySimulationIdAndName(String simulationId, String strategyName) {
        return strategyRepository.findBySimulationIdAndName(simulationId, strategyName);
    }

    @Override
    public List<Strategy> findAllBySimulationId(String simulationId) {
        return strategyRepository.findAllBySimulationId(simulationId);
    }

    public List<Ranker> findRankersByCagr() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("done").is(true)
                        .and("userId").exists(true)),
                sort(Sort.Direction.DESC, "evaluationResults.cagr"),
                project()
                        .and("simulationId").as("simulationId")
                        .and("userId").as("userId")
                        .and("assets").as("assets")
                        .and("name").as("strategyName")
                        .and("evaluationResults.cagr").as("cagr"),
                limit(5L)
        );
        AggregationResults<Ranker> strategies = mongoTemplate.aggregate(aggregation, "strategies", Ranker.class);
        return strategies.getMappedResults();
    }
}
