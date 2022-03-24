package kr.co.tmax.rabackend.domain.strategy;

import java.util.List;
import java.util.Optional;

public interface StrategyReader {

    Optional<Strategy> findBySimulationIdAndName(String simulationId, String name);

    List<Strategy> findAllBySimulationId(String simulationId);

}
