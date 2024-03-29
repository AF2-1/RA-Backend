package kr.co.tmax.rabackend.interfaces.simulation;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.config.common.CommonResponse;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.domain.simulation.SimulationStore;
import kr.co.tmax.rabackend.domain.strategy.Strategy;
import kr.co.tmax.rabackend.domain.strategy.StrategyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequestMapping(value = "/api/v1/", produces = "application/json; charset=utf8")
@RequiredArgsConstructor
@RestController
public class SimulationController {
    private final SimulationService simulationService;
    private final StrategyService strategyService;
    private final SimulationStore simulationStore;
    private final ModelMapper modelMapper;
    private final SimulationRegisterRequestValidator simulationRegisterRequestValidator;

    @ApiOperation(value = "시뮬레이션 생성", notes = "시뮬레이션을 생성합니다")
    @PostMapping("/users/{userId}/simulations")
    public ResponseEntity<CommonResponse> registerSimulation(@NotBlank @PathVariable String userId,
                                                             @Validated @RequestBody SimulationDto.RegisterSimulationRequest request,
                                                             BindingResult bindingResult,
                                                             UriComponentsBuilder uriComponentsBuilder) throws BindException {

        List<Simulation> simulations = simulationService.getSimulations(new SimulationCommand.GetSimulationsRequest(userId));
        simulationRegisterRequestValidator.checkConcurrentSimulation(simulations, bindingResult);
        simulationRegisterRequestValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);

        SimulationCommand.RegisterSimulationRequest command = modelMapper.map(request, SimulationCommand.RegisterSimulationRequest.class);
        simulationService.registerSimulation(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(getLocation(userId, uriComponentsBuilder))
                .body(CommonResponse.withMessage("시뮬레이션 생성 요청 확인"));
    }

    private URI getLocation(String userId, UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder
                .path(String.format("/users/%s/simulations", userId))
                .build().toUri();
    }

    @ApiOperation(value = "시뮬레이션 목록 조회", notes = "시뮬레이션 목록을 조회합니다")
    @GetMapping("users/{userId}/simulations")
    public ResponseEntity<CommonResponse> getSimulations(@NotBlank @PathVariable String userId) {
        SimulationCommand.GetSimulationsRequest command = new SimulationCommand.GetSimulationsRequest(userId);
        List<Simulation> simulations = simulationService.getSimulations(command);

        List<SimulationDto.SimpleSimulationResponse> simpleSimulationResponses = simulations.stream()
                .map(simulation -> SimulationDto.SimpleSimulationResponse.create(simulation, strategyService.findAllBySimulation(simulation.getSimulationId())))
                .collect(Collectors.toList());

        SimulationDto.SimulationsResponse simulationsResponse = new SimulationDto.SimulationsResponse(userId, simpleSimulationResponses);

        if (isSimulationsDone(simulations))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonResponse.withMessageAndData("시뮬레이션 목록 확인(모든 시뮬레이션이 완료되었습니다)", simulationsResponse));

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(CommonResponse.withMessageAndData("시뮬레이션 목록 확인(진행중인 시뮬레이션이 있습니다)", simulationsResponse));
    }

    private boolean isSimulationsDone(List<Simulation> simulations) {
        return simulations.stream()
                .map(Simulation::isDone)
                .allMatch(Predicate.isEqual(true));
    }

    @ApiOperation(value = "시뮬레이션 단건 조회", notes = "시뮬레이션을 조회합니다")
    @GetMapping("users/{userId}/simulations/{simulationId}")
    public ResponseEntity<CommonResponse> getSimulation(@PathVariable String userId,
                                                        @PathVariable String simulationId) {
        SimulationCommand.GetSimulationRequest command = new SimulationCommand.GetSimulationRequest(userId, simulationId);
        Simulation simulation = simulationService.getSimulation(command);

        List<Strategy> strategies = strategyService.findAllBySimulation(command.getSimulationId());

        SimulationDto.SimulationResponse simulationResponse = SimulationDto.SimulationResponse.create(simulation, strategies);

//        Simulation finishedSimulation = modelMapper.map(simulationResponse, Simulation.class);
//        System.out.println("finishedSimulation = " + finishedSimulation);

//        if (simulationResponse.isDone()) {
//            simulationStore.store(finishedSimulation);
//        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("시뮬레이션 단건 조회 완료", simulationResponse));
    }

    @ApiOperation(value = "시뮬레이션 삭제", notes = "시뮬레이션을 삭제합니다")
    @DeleteMapping("users/{userId}/simulations/{simulationId}")
    public ResponseEntity<CommonResponse> deleteSimulation(@PathVariable String userId,
                                                           @PathVariable String simulationId) {
        SimulationCommand.DeleteSimulationRequest command = new SimulationCommand.DeleteSimulationRequest(userId, simulationId);
        simulationService.deleteSimulation(command);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessage("시뮬레이션 삭제 완료"));
    }

    @ApiOperation(value = "대시보드 조회", notes = "대시보드를 조회합니다")
    @GetMapping("dashboards")
    public ResponseEntity<CommonResponse> getDashBoard() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("대시보드 조회 완료", simulationService.getDashBoard()));
    }

    @ApiOperation(value = "대시보드 상세 조회", notes = "대시보드를 상세 조회합니다")
    @GetMapping("dashboards/{simulationId}/strategies/{strategyName}")
    public ResponseEntity<CommonResponse> getDashBoardDetail(@PathVariable String simulationId,
                                                             @PathVariable String strategyName) {
        System.out.println("simulationId = " + simulationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("대시보드 상세 조회 완료", simulationService.getDashBoardDetail(simulationId, strategyName)));
    }
}
