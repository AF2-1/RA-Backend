package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationStoreImpl implements SimulationStore {

    private final SimulationRepository simulationRepository;

    @Override
    @Transactional
    public Simulation store(Simulation simulation) {
        return simulationRepository.save(simulation);
    }

    @Override
    public void delete(Simulation simulation) {
        simulationRepository.delete(simulation);
    }
}
