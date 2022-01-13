package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimulationService {

    private final SimulationStore simulationStore;

    public Simulation registerSimulation(String userId, RegisterSimulationRequest request) {
        Simulation simulation = request.toEntity(userId);
        // todo: 시뮬레이션의 전략 별로 AI 서버로 요청보내기
        return simulationStore.store(simulation);
    }
}
