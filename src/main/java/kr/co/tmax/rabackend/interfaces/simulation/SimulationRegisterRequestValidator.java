package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetService;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationService;
import kr.co.tmax.rabackend.interfaces.asset.AssetDto;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.RegisterSimulationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationRegisterRequestValidator implements Validator {

    private final AppProperties appProperties;
    private final SimulationService simulationService;
    private final AssetService assetService;

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
        checkValidDate(request.getStartDate(), request.getEndDate(), request.getAssets(), errors);
    }

    private void checkValidStrategy(List<String> strategies, Errors errors) {
        Set<String> strategiesSet = appProperties.getSimulation().getStrategies();
        if (!strategiesSet.containsAll(strategies))
            errors.rejectValue("strategies", "bad.request", null, null);
    }

    private void checkDuplicatedSimulation(RegisterSimulationRequest request, List<Simulation> simulations, Errors errors) {
        // todo: 해당 유저가 중복되는 시뮬레이션 이미 요청했는지 체크 중복되는게 있다면 error 담기
//        request.getAssets()
    }

    private void checkConcurrentSimulation(List<Simulation> simulations, Errors errors) {
        // todo: 현재 유저가 이미 진행중인 시뮬레이션이 있다면 error 담기
        if (simulations.stream().anyMatch(s -> !s.isDone()))
            errors.rejectValue("simulation", "bad.request", null, "진행중인 시뮬레이션이 있습니다");
    }

    private void checkValidAsset() {
        // todo: 유효한 자산인지 체크 유효하지 않다면 error 담기
    }

    private void checkValidDate(LocalDate startDate, LocalDate endDate, List<String> assets, Errors errors) {
        List<Asset> assetList = assets.stream().map(assetService::searchByCertainTicker).collect(Collectors.toList());
        LocalDate validStartDate = LocalDate.from(assetList.stream().map(Asset::getStartDate).max(LocalDateTime::compareTo).orElseThrow(null));
        LocalDate validEndDate = LocalDate.from(assetList.stream().map(Asset::getEndDate).min(LocalDateTime::compareTo).orElseThrow(null));

        if(startDate.isBefore(validStartDate))
            errors.rejectValue("startDate", "range.startDate", new Object[]{validStartDate}, null);

        if(endDate.isAfter(validEndDate))
            errors.rejectValue("endDate", "range.endDate", new Object[]{validEndDate}, null);

        if(startDate.isAfter(endDate))
            errors.rejectValue("endDate", "range.period", new Object[]{validStartDate, validEndDate}, null);
    }
}