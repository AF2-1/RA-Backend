package kr.co.tmax.rabackend.domain.strategy;

import java.util.List;
import java.util.Optional;

public interface StrategyReader {

    Optional<Strategy> findBySimulationIdAndStrategyName(String simulationId, String strategyName);

    List<Strategy> findAllBySimulationId(String simulationId);

}
