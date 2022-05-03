package kr.co.tmax.rabackend.domain.strategy;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface StrategyReader {

    public List<Strategy> findAll(Sort sort);

    Optional<Strategy> findBySimulationIdAndName(String simulationId, String name);

    List<Strategy> findAllBySimulationId(String simulationId);

}
