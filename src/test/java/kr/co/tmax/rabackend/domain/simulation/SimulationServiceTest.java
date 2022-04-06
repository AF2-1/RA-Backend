package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.domain.strategy.StrategyStore;
import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.external.KserveApiClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @InjectMocks
    private SimulationService simulationService;

    @Mock
    private SimulationStore simulationStore;
    @Mock
    private SimulationReader simulationReader;
    @Mock
    private StrategyStore strategyStore;
    @Mock
    private StrategyReader strategyReader;
    @Mock
    private AssetReader assetReader;
    @Mock
    private KserveApiClient kserveApiClient;

    private Simulation simulation;
    private Strategy strategy;
    private SimulationCommand.RegisterSimulationRequest registerSimulationRequest;
    private Asset asset;

    @BeforeEach
    void setUp() {
        simulation = Simulation.builder()
                .rebalancingPeriod(0)
                .userId("2")
                .endDate(LocalDate.MIN)
                .startDate(LocalDate.MIN)
                .assets(new ArrayList<>())
                .build();

        strategy = new Strategy();

        var assetCommands = new ArrayList<AssetCommand>();
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));
        assetCommands.add(new AssetCommand("KOSPI", "033780.KS"));

        var strategyRequest = new ArrayList<String>();
        strategyRequest.add("ew");
        strategyRequest.add("iv");
        strategyRequest.add("rp");

        registerSimulationRequest = SimulationCommand.RegisterSimulationRequest.builder()
                .userId("2")
                .cnt(1)
                .endDate(LocalDate.MIN)
                .startDate(LocalDate.MIN)
                .rebalancingPeriod(40)
                .assets(assetCommands)
                .strategies(strategyRequest)
                .build();

        asset = Asset.builder()
                .id("1")
                .ticker("033780.KS")
                .index("KOSPI")
                .name("KT & G KT&G")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName(value = "새로운 Simulation을 등록할 수 있다.")
    void registerSimulationTest() {
        // given
        given(assetReader.searchByTickerAndIndex(anyString(), anyString())).willReturn(Optional.of(asset));
        given(simulationStore.store(any(Simulation.class))).willReturn(simulation);
        given(strategyStore.store(any(Strategy.class))).willReturn(strategy);
        BDDMockito.doNothing().when(kserveApiClient).requestAA(any(), any());

        // when
        simulationService.registerSimulation(registerSimulationRequest);

        // then
        then(assetReader).should(BDDMockito.atLeast(3)).searchByTickerAndIndex(anyString(), anyString());
        then(simulationStore).should().store(any());
        then(strategyStore).should(BDDMockito.atLeast(3)).store(any(Strategy.class));
        then(kserveApiClient).should().requestAA(any(), any());
    }

    @Test
    @DisplayName(value = "UserId로 simulation을 전체 조회할 수 있다.")
    void getSimulationsTest() {
        // given
        given(simulationReader.findByUserId(anyString())).willReturn(new ArrayList<Simulation>());

        // when
        simulationService.getSimulations(new SimulationCommand.GetSimulationsRequest(anyString()));

        // then
        then(simulationReader).should().findByUserId(anyString());
    }

    @Test
    @DisplayName(value = "simulation 단건조회를 할 수 있다.")
    void getSimulationTest() {
        // given
        given(simulationReader.findByUserIdAndSimulationId(anyString(), anyString())).willReturn(Optional.of(new Simulation()));

        // when
        simulationService.getSimulation(new SimulationCommand.GetSimulationRequest(anyString(), anyString()));

        // then
        then(simulationReader).should().findByUserIdAndSimulationId(anyString(), anyString());
    }

    @Nested
    @DisplayName(value = "deleteSimulation 메소드는")
    class SimulationDeletionTest {
        @Test
        @DisplayName(value = "올바른 simulationId가 주어지면 simulation을 삭제할 수 있다.")
        void deleteSimulationTest() {
            // given
            given(simulationReader.findById(anyString())).willReturn(Optional.of(simulation));
            BDDMockito.doNothing().when(simulationStore).delete(any());

            // when
            simulationService.deleteSimulation(new SimulationCommand.DeleteSimulationRequest(simulation.getUserId(), anyString()));

            // then
            then(simulationReader).should().findById(anyString());
            then(simulationStore).should().delete(any());
        }

        @Test
        @DisplayName(value = "userId가 조회된 simulation의 userId와 다르면 BadRequestException이 발생한다.")
        void deleteSimulationFailTest() {
            // given
            given(simulationReader.findById(anyString())).willThrow(new BadRequestException("simulation의 소유자만 삭제가 가능합니다"));

            // when, then
            Assertions.assertThrows(
                    BadRequestException.class,
                    () -> simulationService.deleteSimulation(new SimulationCommand.DeleteSimulationRequest(UUID.randomUUID().toString(), anyString()))
            );
            then(simulationReader).should().findById(anyString());
            then(simulationStore).should(BDDMockito.times(0)).delete(any());
        }
    }

    @Test
    @DisplayName(value = "전략 상태를 완료로 바꿀 수 있다.")
    void completeStrategyTest() {
        // given
        final var strategyMock = mock(Strategy.class);

        given(strategyReader.findBySimulationIdAndName(anyString(), anyString())).willReturn(Optional.of(strategyMock));
        BDDMockito.doNothing().when(strategyMock).complete(any(), any(), any(), any(), any(), any());
        given(strategyStore.store(strategyMock)).willReturn(strategyMock);

        // when
        simulationService.completeStrategy(SimulationCommand.CompleteStrategyRequest.builder().simulationId("1").strategyName("ew").build());

        // then
        then(strategyReader).should(BDDMockito.atLeast(1)).findBySimulationIdAndName(anyString(), anyString());
        then(strategyStore).should(BDDMockito.atLeast(1)).store(any());
    }
}
