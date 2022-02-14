package kr.co.tmax.rabackend.interfaces.strategy;

import io.swagger.annotations.ApiOperation;
import kr.co.tmax.rabackend.domain.simulation.SimulationCommand;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StrategyController {
    private final CompleteStrategyRequestValidator completeStrategyRequestValidator;
    private final SimulationService simulationService;

    @ApiOperation(value = "시뮬레이션 전략 완료", notes = "시뮬레이션의 전략을 완료 상태로 변경합니다")
    @PostMapping("/simulation/callback")
    @Transactional
    public void completeStrategy(@RequestParam String simulationId,
                                 @RequestParam String strategyName,
                                 @Validated @RequestBody SimulationDto.CompleteStrategyRequest request,
                                 BindingResult bindingResult) throws BindException {
        completeStrategyRequestValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);

        log.debug("callback called | simulationId: {} strategyName: {}", simulationId, strategyName);
        SimulationCommand.CompleteStrategyRequest command = request.toCommand(simulationId, strategyName);
        simulationService.completeStrategy(command);
    }
}
