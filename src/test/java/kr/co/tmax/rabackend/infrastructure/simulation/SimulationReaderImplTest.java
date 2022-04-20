package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("SimulationReader은")
class SimulationReaderImplTest {

    @InjectMocks
    protected SimulationReaderImpl simulationReader;

    @Mock
    protected SimulationRepository simulationRepository;

    @Test
    @DisplayName("simulation ID로 찾을 수 있다.")
    void findByIdSuccessTest() {
        // given
        given(simulationRepository.findBySimulationId(anyString())).willReturn(any());

        // when
        simulationReader.findById(UUID.randomUUID().toString());

        // then
        then(simulationRepository).should().findBySimulationId(anyString());
    }

    @Test
    @DisplayName("user ID로 찾을 수 있다.")
    void findByUserIdSuccessTest() {
        // given
        given(simulationRepository.findByUserId(anyString())).willReturn(any());

        // when
        simulationReader.findByUserId(UUID.randomUUID().toString());

        // then
        then(simulationRepository).should().findByUserId(anyString());
    }

    @Test
    @DisplayName("UserId와 SimulationId로 찾을 수 있다.")
    void findByUserIdAndSimulationId() {
        // given
        var userId = UUID.randomUUID().toString();
        var simulationId = UUID.randomUUID().toString();
        var simulation = Simulation.builder()
                .userId(userId)
                .build();

        given(simulationRepository.findByUserIdAndSimulationId(userId, simulationId)).willReturn(Optional.of(simulation));

        // when
        simulationReader.findByUserIdAndSimulationId(userId, simulationId);

        // then
        then(simulationRepository).should().findByUserIdAndSimulationId(userId, simulationId);
    }
}