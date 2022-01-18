package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.simulation.*;
import kr.co.tmax.rabackend.exception.ResourceNotFoundException;
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

@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SimulationController {

    private final SimulationRead simulationRead;
    private final SimulationStore simulationStore;
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

    @PostMapping("/simulation/callback")
    public void update(@RequestParam String simulationId,
                       @RequestParam String strategyName) {

        System.out.println("\"hi\" = " + "hi");

        Simulation simulation = simulationRead.findById(simulationId).orElseThrow(() ->
                new ResourceNotFoundException("Simulation", "simulationId", simulationId));

        simulation.update(simulationId, strategyName);
        simulationStore.store(simulation);

        log.info("simulation: {} ", simulation);
    }
}
