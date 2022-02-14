package kr.co.tmax.rabackend.domain.simulation;

import org.springframework.transaction.annotation.Transactional;

public interface SimulationStore {

    @Transactional
    Simulation store(Simulation simulation);

    void delete(Simulation simulation);
}
