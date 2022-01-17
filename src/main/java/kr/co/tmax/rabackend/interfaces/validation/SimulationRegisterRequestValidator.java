package kr.co.tmax.rabackend.interfaces.validation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.RegisterSimulationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationRegisterRequestValidator implements Validator {

    private final AppProperties appProperties;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterSimulationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("{}.{}() is called", this.getClass().getSimpleName(), "validate");
        log.debug("strategies: {}", appProperties.getSimulation().getStrategies());
        RegisterSimulationRequest request = (RegisterSimulationRequest) target;

        checkValidStrategy(request.getStrategies(), errors);
    }

    private void checkValidStrategy(List<String> strategies, Errors errors) {
        Set<String> strategiesSet = appProperties.getSimulation().getStrategies();
        if (!strategiesSet.containsAll(strategies))
            errors.rejectValue("strategies", "bad.request", null, null);
    }

    private void checkDuplicatedSimulation() {
        // todo: 해당 유저가 중복되는 시뮬레이션 이미 요청했는지 체크 중복되는게 있다면 error 담기
    }

    private void checkConcurrentSimulation() {
        // todo: 현재 유저가 이미 진행중인 시뮬레이션이 있다면 error 담기
    }

    private void checkValidAsset() {
        // todo: 유효한 자산인지 체크 유효하지 않다면 error 담기
    }

    private void checkValidDate() {
        // todo: 시작일과 종료일이 유효한지 않다면 error 담기
    }
}
