package kr.co.tmax.rabackend.domain.simulation;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SimulationReader {
    @Transactional
    Optional<Simulation> findById(String simulationId);

    List<Simulation>  findByUserId(String userId);
    Optional<Simulation> findByUserIdAndSimulationId(String userId, String simulationId);
}
