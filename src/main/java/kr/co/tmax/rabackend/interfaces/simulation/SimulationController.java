package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SimulationController {

    private final SimulationService simulationService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/simulations")
    public ResponseEntity<CommonResponse> registerSimulation(@PathVariable String userId,
                                                             @RequestBody SimulationDto.RegisterSimulationRequest request,
                                                             UriComponentsBuilder uriComponentsBuilder){

        SimulationCommand.RegisterSimulationRequest command = modelMapper.map(request, SimulationCommand.RegisterSimulationRequest.class);
        simulationService.registerSimulation(userId, command);

        CommonResponse<Object> commonResponse = CommonResponse.success("시뮬레이션 생성 요청 확인", null);
        URI location = uriComponentsBuilder
                .path(String.format("/users/%s/simulations", userId))
                .build().toUri();
        return ResponseEntity.created(location).body(commonResponse);
    }
}
