package kr.co.tmax.rabackend.infrastructure.strategy;

import kr.co.tmax.rabackend.domain.strategy.Strategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("StrategyStore는")
class StrategyStoreImplTest {

    @InjectMocks
    protected StrategyStoreImpl strategyStore;

    @Mock
    protected StrategyRepository strategyRepository;

    @Test
    @DisplayName("Strategy를 저장하는 로직이 정상적으로 수행된다.")
    void storeTest() {
        // given
        Strategy strategy = new Strategy();
        given(strategyRepository.save(strategy)).willReturn(strategy);

        // when
        strategyStore.store(strategy);

        // then
        then(strategyRepository).should().save(strategy);
    }

    @Test
    @DisplayName("Strategy가 null일 경우 예외가 발생한다.")
    void storeFailTest() {
        // given
        given(strategyRepository.save(null)).willThrow(IllegalArgumentException.class);

        // when, then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> strategyStore.store(null)
        );

        // then
        then(strategyRepository).should().save(null);
    }
}