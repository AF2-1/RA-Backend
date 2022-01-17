package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationReaderImpl {

    private final SimulationRepository simulationRepository;

    public Optional<Simulation> findById(String id) {
        return simulationRepository.findById(id);
    }

//    public List<Simulation> findByStrategy(String name) {
//        return simulationRepository.findByStrategy(name);
//    }
}
