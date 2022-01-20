package kr.co.tmax.rabackend.domain.simulation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand.*;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.exception.BadRequestException;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.RegisterStrategyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimulationService {

    private final SimulationStore simulationStore;
    private final SimulationReader simulationReader;
    private final WebClient webClient;
    private final AppProperties appProperties;

    public Simulation registerSimulation(RegisterSimulationRequest request) {
        Simulation initSimulation = request.toEntity();
//        requestAA(initSimulation);
        return simulationStore.store(initSimulation);
    }

    /**
     * request to Inference Server to get the weights from AA(Asset Allocation) Algo(Strategy)
     * @param simulation
     */
    private void requestAA(Simulation simulation) {
        // todo: AI 서버에서 실패 응답이 온 경우 예외를 던져 Simulation이 DB에 저장되는 것을 막아야한다.
        simulation.getStrategies().forEach(strategy -> {
            RegisterStrategyRequest requestBody = createRequest(simulation, strategy);
            SimulationDto.RegisterStrategyResponse response = executeRequest(requestBody);
            log.debug("simulationId: {} strategyName: {} AI response:{}",
                    simulation.getSimulationId(), strategy.getName(), response.toString());
        });
    }

    private SimulationDto.RegisterStrategyResponse executeRequest(RegisterStrategyRequest requestBody) {
        return webClient.post()
                .uri(appProperties.getAi().getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(SimulationDto.RegisterStrategyResponse.class)
                .block();
    }

    private RegisterStrategyRequest createRequest(Simulation simulation, Strategy strategy) {
        return RegisterStrategyRequest.builder()
                .strategy(strategy.getName())
                .rebalancingLen(simulation.getRebalancingPeriod())
                .assetList(simulation.getAssets())
                .startDate(simulation.getStartDate())
                .endDate(simulation.getEndDate())
                .callbackUrl(appProperties.getAi().getCallBackUrl())
                .build();
    }

    public List<Simulation> getSimulations(GetSimulationsRequest command) {
        return simulationReader.findByUserId(command.getUserId());
    }

    public Simulation getSimulation(GetSimulationRequest command) {
        return simulationReader.findByUserIdAndSimulationId(command.getUserId(), command.getSimulationId())
                .orElseThrow(() -> new ResourceNotFoundException("simulation", "simulationId", command.getSimulationId()));
    }

    public void deleteSimulation(DeleteSimulationRequest command) {
        Simulation simulation = simulationReader.findById(command.getSimulationId())
                .orElseThrow(() -> new ResourceNotFoundException("simulation", "simulationId", command.getSimulationId()));

        if(!simulation.getUserId().equals(command.getUserId()))
            throw new BadRequestException("simulation의 소유자만 삭제가 가능합니다");

        simulationStore.delete(simulation);
    }

    public void updateSimulation(UpdateSimulationRequest command) {
        Simulation simulation = simulationReader.findById(command.getSimulationId()).orElseThrow(() ->
                new ResourceNotFoundException("Simulation", "simulationId", command.getSimulationId()));

        simulation.update(command.getStrategyName());
        simulationStore.store(simulation);
    }
}
