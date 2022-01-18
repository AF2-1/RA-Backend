package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimulationService {

    private final SimulationStore simulationStore;
    private final WebClient webClient;

    public Simulation registerSimulation(RegisterSimulationRequest request) {
        Simulation initSimulation = request.toEntity();
        Simulation simulation = simulationStore.store(initSimulation);
        requestToAIServer(simulation);
        return simulation;
    }

    private void requestToAIServer(Simulation simulation) {
        return;
    }


}
