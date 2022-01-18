package kr.co.tmax.rabackend.domain.simulation;

import java.util.Optional;

public interface SimulationRead {

    Optional<Simulation> findById(String id);
}
