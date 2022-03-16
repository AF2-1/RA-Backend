package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StrategyRepository extends MongoRepository<Strategy, String> {

    Optional<Strategy> findBySimulationIdAndName(String simulationId, String name);

    List<Strategy> findAllBySimulationId(String simulationId);

}
