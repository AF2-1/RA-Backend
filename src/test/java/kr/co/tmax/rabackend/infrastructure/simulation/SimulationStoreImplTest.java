package kr.co.tmax.rabackend.infrastructure.simulation;

import kr.co.tmax.rabackend.domain.simulation.Simulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("SimulationStore는")
class SimulationStoreImplTest {

    @InjectMocks
    protected SimulationStoreImpl simulationStore;

    @Mock
    protected SimulationRepository simulationRepository;

    @Test
    @DisplayName("Simulation을 저장할 수 있다.")
    void storeSuccessTest() {
        // given
        var simulation = new Simulation();
        given(simulationRepository.save(simulation)).willReturn(simulation);

        // when
        simulationStore.store(simulation);

        // then
        then(simulationRepository).should().save(simulation);
    }

    @Test
    @DisplayName("Simulation을 삭제할 수 있다.")
    void deleteSuccessTest() {
        // given
        var simulation = new Simulation();
        BDDMockito.doNothing().when(simulationRepository).delete(simulation);

        // when
        simulationStore.delete(simulation);

        // then
        then(simulationRepository).should(BDDMockito.times(1)).delete(simulation);
    }

}