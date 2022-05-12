package kr.co.tmax.rabackend.infrastructure.strategy;

import com.mongodb.BasicDBObject;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.infrastructure.simulation.SimulationRepository;
import kr.co.tmax.rabackend.infrastructure.user.UserRepository;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@SpringBootTest
public class test {

    @Autowired
    StrategyRepository strategyRepository;

    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void test1() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("done").is(true)
                        .and("userId").exists(true)
                ),
                sort(Sort.Direction.DESC, "evaluationResults.cagr"
                ),
                project()
                        .and("name").as("strategyName")
                        .and("evaluationResults.cagr").as("cagr")
                        .and("userId").as("userId")
        );
        AggregationResults<SimulationDto.Ranker> strategies = mongoTemplate.aggregate(aggregation, "strategies", SimulationDto.Ranker.class);
        List<SimulationDto.Ranker> mappedResults = strategies.getMappedResults();
        System.out.println("mappedResults = " + mappedResults);
    }
}
