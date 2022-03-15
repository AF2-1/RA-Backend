package kr.co.tmax.rabackend.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StrategyService {
    private final StrategyReader strategyReader;

    public List<Strategy> findAllBySimulation(String simulationId) {
        return strategyReader.findAllBySimulationId(simulationId);
    }
}
