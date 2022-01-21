package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.interfaces.validation.SimulationRegisterRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Predicate;

@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SimulationController {
    private final SimulationService simulationService;
    private final ModelMapper modelMapper;
    private final SimulationRegisterRequestValidator simulationRegisterRequestValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(simulationRegisterRequestValidator);
    }

    @PostMapping("/users/{userId}/simulations")
    public ResponseEntity<CommonResponse> registerSimulation(@PathVariable String userId,
                                                             @Validated @RequestBody SimulationDto.RegisterSimulationRequest request,
                                                             BindingResult bindingResult,
                                                             UriComponentsBuilder uriComponentsBuilder) throws BindException {
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

    @GetMapping("users/{userId}/simulations")
    public ResponseEntity<CommonResponse> getSimulations(@PathVariable String userId) {
        SimulationCommand.GetSimulationsRequest command = new SimulationCommand.GetSimulationsRequest(userId);
        List<Simulation> simulations = simulationService.getSimulations(command);

        SimulationDto.SimulationsResponse simulationsResponse = SimulationDto.SimulationsResponse.create(userId, simulations);

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

    @GetMapping("users/{userId}/simulations/{simulationId}")
    public ResponseEntity<CommonResponse> getSimulation(@PathVariable String userId,
                                                        @PathVariable String simulationId) {
        SimulationCommand.GetSimulationRequest command = new SimulationCommand.GetSimulationRequest(userId, simulationId);
        Simulation simulation = simulationService.getSimulation(command);
        SimulationDto.SimulationResponse simulationResponse = SimulationDto.SimulationResponse.create(simulation);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessageAndData("시뮬레이션 단건 조회 완료", simulationResponse));
    }

    @DeleteMapping("users/{userId}/simulations/{simulationId}")
    public ResponseEntity<CommonResponse> deleteSimulation(@PathVariable String userId,
                                                           @PathVariable String simulationId) {
        SimulationCommand.DeleteSimulationRequest command = new SimulationCommand.DeleteSimulationRequest(userId, simulationId);
        simulationService.deleteSimulation(command);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.withMessage("시뮬레이션 삭제 완료"));
    }

    @PostMapping("/simulation/callback")
    public void updateSimulation(@RequestParam String simulationId,
                                 @RequestParam String strategyName) {
        log.info("[ callback arrived ] simulationId: {}, strategyName: {}", simulationId, strategyName);

        SimulationCommand.UpdateSimulationRequest command = new SimulationCommand.UpdateSimulationRequest(simulationId, strategyName);
        simulationService.updateSimulation(command);
    }
}
