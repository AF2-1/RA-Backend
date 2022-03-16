package kr.co.tmax.rabackend.domain.simulation;

public interface SimulationStore {
    Simulation store(Simulation simulation);

    void delete(Simulation simulation);
}
