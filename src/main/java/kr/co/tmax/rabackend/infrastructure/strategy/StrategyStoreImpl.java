package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StrategyStoreImpl implements StrategyStore {
    private final StrategyRepository strategyRepository;

    @Override
    public Strategy store(Strategy strategy) {
        return strategyRepository.save(strategy);
    }

    @Override
    public void delete(Strategy strategy) {
        strategyRepository.delete(strategy);
    }
}
