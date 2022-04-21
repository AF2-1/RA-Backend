package kr.co.tmax.rabackend.infrastructure.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("StrategyReader는")
class StrategyReaderImplTest {

    @InjectMocks
    protected StrategyReaderImpl strategyReaderl;

    @Mock
    protected StrategyRepository strategyRepository;

    @Test
    @DisplayName("simulation id와 전략 이름으로 찾을 수 있다.")
    void findBySimulationIdAndName() {
        // given
        var id = UUID.randomUUID().toString();

        given(strategyRepository.findBySimulationIdAndName(id, id)).willReturn(Optional.empty());

        // when
        strategyReaderl.findBySimulationIdAndName(id, id);

        // then
        then(strategyRepository).should().findBySimulationIdAndName(id, id);
    }

    @Test
    @DisplayName("simulation id로 모든 전략을 찾을 수 있다.")
    void findAllBySimulationId() {
        // given
        var id = UUID.randomUUID().toString();

        given(strategyRepository.findAllBySimulationId(id)).willReturn(new ArrayList<>());

        // when
        strategyReaderl.findAllBySimulationId(id);

        // then
        then(strategyRepository).should().findAllBySimulationId(id);
    }
}