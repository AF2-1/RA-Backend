package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StrategyStoreImpl implements StrategyStore {
    private final StrategyRepository strategyRepository;

    @Override
    public Strategy store(Strategy strategy) {
        return strategyRepository.save(strategy);
    }
}
