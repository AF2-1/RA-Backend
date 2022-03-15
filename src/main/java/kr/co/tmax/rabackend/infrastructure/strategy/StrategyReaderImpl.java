package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StrategyReaderImpl implements StrategyReader {
    private final StrategyRepository strategyRepository;

    @Override
    public Optional<Strategy> findBySimulationIdAndStrategyName(String simulationId, String strategyName) {
        return strategyRepository.findBySimulationIdAndName(simulationId, strategyName);
    }

    @Override
    public List<Strategy> findAllBySimulationId(String simulationId) {
        return strategyRepository.findAllBySimulationId(simulationId);
    }
}
