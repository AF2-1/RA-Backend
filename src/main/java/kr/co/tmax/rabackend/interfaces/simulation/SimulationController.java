package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.interfaces.validation.SimulationRegisterRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

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
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(simulationRegisterRequestValidator);
    }

    @PostMapping("/users/{userId}/simulations")
    public ResponseEntity<CommonResponse> registerSimulation(@PathVariable String userId,
                                                             @Validated @RequestBody SimulationDto.RegisterSimulationRequest request,
                                                             BindingResult bindingResult,
                                                             UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        SimulationCommand.RegisterSimulationRequest command = modelMapper.map(request, SimulationCommand.RegisterSimulationRequest.class);
        simulationService.registerSimulation(command);

        CommonResponse<Object> commonResponse = CommonResponse.success("시뮬레이션 생성 요청 확인", null);
        URI location = uriComponentsBuilder
                .path(String.format("/users/%s/simulations", userId))
                .build().toUri();
        return ResponseEntity.created(location).body(commonResponse);
    }

    @GetMapping("users/{userId}/simulations")
    public ResponseEntity<CommonResponse> getSimulations(@PathVariable String userId) {
        // command로 변환
        SimulationCommand.GetSimulationsRequest command = new SimulationCommand.GetSimulationsRequest(userId);

        // command를 가지고 service 요청 Simulation 목록 가져옴
        List<Simulation> simulations = simulationService.getSimulations(command);

        // Simulation을 그대로 반환하지 않고 Dto로 변환해서 응답을 만드는 과정
        List<SimulationDto.GetSimulationResponse> getSimulationResponses = simulations.stream()
                .map(SimulationDto.GetSimulationResponse::create)
                .collect(Collectors.toList());

        SimulationDto.GetSimulationsResponse getSimulationsResponse = SimulationDto.GetSimulationsResponse.builder()
                .userId(userId)
                .simulations(getSimulationResponses)
                .build();

        // 완료되지 않은 시뮬레이션 포함 여부 확인
        boolean isDone = simulations.stream()
                .map(Simulation::isDone)
                .allMatch(Predicate.isEqual(true));

        // 시뮬레이션이 모두 완료되었을 때 응답
        if (isDone)
            return ResponseEntity.ok(CommonResponse.success("시뮬레이션 목록 확인(모든 시뮬레이션이 완료되었습니다)", getSimulationsResponse));

        // 시뮬레이션목록에 완료되지 않은 시뮬레이션이 포함된 경우 응답
        return ResponseEntity.accepted()
                .body(CommonResponse.success("시뮬레이션 목록 확인(진행중인 시뮬레이션이 있습니다)", getSimulationsResponse));
    }
}
