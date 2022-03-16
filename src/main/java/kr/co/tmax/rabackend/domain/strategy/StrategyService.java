package kr.co.tmax.rabackend.domain.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StrategyService {
    private final StrategyReader strategyReader;

    public List<Strategy> findAllBySimulation(String simulationId) {
        return strategyReader.findAllBySimulationId(simulationId);
    }
}
