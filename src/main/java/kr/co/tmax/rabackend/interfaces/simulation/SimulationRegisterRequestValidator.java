package kr.co.tmax.rabackend.interfaces.simulation;

import kr.co.tmax.rabackend.config.AppProperties;
import kr.co.tmax.rabackend.domain.asset.Asset;
import kr.co.tmax.rabackend.domain.asset.AssetCommand;
import kr.co.tmax.rabackend.domain.asset.AssetReader;
import kr.co.tmax.rabackend.domain.simulation.Simulation;
import kr.co.tmax.rabackend.domain.simulation.SimulationStore;
import kr.co.tmax.rabackend.domain.strategy.StrategyReader;
import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto.RegisterSimulationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimulationRegisterRequestValidator implements Validator {

    private final AppProperties appProperties;
    private final AssetReader assetReader;
    private final StrategyReader strategyReader;
    private final SimulationStore simulationStore;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterSimulationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("{}.{}() is called", this.getClass().getSimpleName(), "validate");
        log.debug("strategies: {}", appProperties.getSimulation().getStrategies());

        RegisterSimulationRequest request = (RegisterSimulationRequest) target;
        checkValidDate(request.getStartDate(), request.getEndDate(), request.getAssets(), errors);
        checkValidStrategy(request.getStrategies(), errors);
        checkValidAsset(request.getAssets(), errors);
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

    public void checkConcurrentSimulation(List<Simulation> simulations, final Errors errors) {
        simulations.stream()
                .filter(simulation -> !simulation.isDone())
                .forEach(filteredSimulation -> {
                    if (strategyReader.findAllBySimulationId(filteredSimulation.getSimulationId()).stream().anyMatch(strategy -> !strategy.isDone())) {
                        errors.rejectValue("strategies", "simulation.running", null, null);
                        return;
                    }
                    filteredSimulation.complete();
                    simulationStore.store(filteredSimulation);
                });
    }

    private void checkValidAsset(List<AssetCommand> assets, Errors errors) {
        if (assets.stream().anyMatch(Predicate.not(asset -> assetReader.existsByTickerAndIndex(asset.getTicker(), asset.getIndex())))) {
            errors.rejectValue("assets", "bad.request", null, null);
        }
    }

    private void checkValidDate(LocalDate startDate, LocalDate endDate, List<AssetCommand> assets, Errors errors) {
        if (assets.stream().noneMatch(Predicate.not(asset -> assetReader.existsByTickerAndIndex(asset.getTicker(), asset.getIndex())))) {
            List<Asset> assetList = assets.stream().map(asset -> assetReader.searchByTickerAndIndex(asset.getTicker(), asset.getIndex()).orElse(new Asset())).collect(Collectors.toList());
            LocalDate validStartDate = LocalDate.from(assetList.stream().map(Asset::getStartDate)
                    .max(LocalDateTime::compareTo).orElseThrow(null));
            LocalDate validEndDate = LocalDate.from(assetList.stream().map(Asset::getEndDate)
                    .min(LocalDateTime::compareTo).orElseThrow(null));

            if (startDate.isAfter(endDate) || endDate.isAfter(validEndDate) || startDate.isBefore(validStartDate))
                errors.rejectValue("endDate", "bad.request", new Object[]{validStartDate, validEndDate}, null);
        }
    }
}