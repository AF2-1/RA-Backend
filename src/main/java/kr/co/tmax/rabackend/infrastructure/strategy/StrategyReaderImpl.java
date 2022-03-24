package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StrategyReaderImpl implements StrategyReader {
    private final StrategyRepository strategyRepository;

    @Override
    public Optional<Strategy> findBySimulationIdAndName(String simulationId, String strategyName) {
        return strategyRepository.findBySimulationIdAndName(simulationId, strategyName);
    }

    @Override
    public List<Strategy> findAllBySimulationId(String simulationId) {
        return strategyRepository.findAllBySimulationId(simulationId);
    }
}
