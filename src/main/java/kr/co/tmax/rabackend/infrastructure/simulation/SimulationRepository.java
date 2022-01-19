package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SimulationRepository extends MongoRepository<Simulation, String> {

    Optional<Simulation> findBySimulationId(String id);

    List<Simulation> findByUserId(String userId);

    Optional<Simulation> findByUserIdAndSimulationId(String userId, String simulationId);
}
