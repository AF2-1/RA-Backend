package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationReaderImpl implements SimulationReader {
    private final SimulationRepository simulationRepository;

    @Override
    public Optional<Simulation> findById(String simulationId) {
        return simulationRepository.findBySimulationId(simulationId);
    }

    @Override
    public List<Simulation> findByUserId(String userId) {
        return simulationRepository.findByUserId(userId);
    }

    @Override
    public Optional<Simulation> findByUserIdAndSimulationId(String userId, String simulationId) {
        return simulationRepository.findByUserIdAndSimulationId(userId, simulationId);
    }
}
