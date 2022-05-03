package kr.co.tmax.rabackend.infrastructure.strategy;

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
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
    void test() {
        Set<String> userset = new HashSet();
        List<Double> cagrset = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "evaluationResults.cagr");
        List<Strategy> all = strategyRepository.findAll(sort);
        for (int i = 0; i < all.size(); i++) {
            Simulation simulation = simulationRepository.findBySimulationId(all.get(i).getSimulationId()).orElseThrow(null);
            if(all.get(i).getEvaluationResults().getCagr() != null && simulation.getUserId() != null) {
//                System.out.println("userset = " + userset);
//                System.out.println("userId = " + simulation.getUserId());
//                System.out.println("cagr = " + all.get(i).getEvaluationResults().getCagr());
                userset.add(simulation.getUserId());
                if (userset.size() > 4) {
                    break;
                }
            }
        }

        Iterator<String> iter = userset.iterator();
        List<SimulationDto.Ranker> rankers = new ArrayList<>();
        SimulationDto.DashBoardResponse dashBoardResponse = new SimulationDto.DashBoardResponse();
        while (iter.hasNext()) {
            SimulationDto.Ranker ranker = new SimulationDto.Ranker();
            User user = userRepository.findById(iter.next()).orElseThrow(null);
            ranker.setEmail(user.getEmail());
            ranker.setName(user.getName());
            System.out.println("ranker = " + ranker);
            rankers.add(ranker);
            dashBoardResponse.setRankers(rankers);
        }
        System.out.println("dashBoardResponse = " + dashBoardResponse);
    }

    @Test
    void test1() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("done").is(true)
                ),
                group("evaluationResults", "name"
                ),
                sort(Sort.Direction.DESC, "evaluationResults.cagr"
                ),
                project()
                        .and("evaluationResults.cagr").as("evaluationResults.cagr")
        );
        AggregationResults<Strategy> strategies = mongoTemplate.aggregate(aggregation, "strategies", Strategy.class);
        List<Strategy> mappedResults = strategies.getMappedResults();
        System.out.println("mappedResults = " + mappedResults);

    }
}
