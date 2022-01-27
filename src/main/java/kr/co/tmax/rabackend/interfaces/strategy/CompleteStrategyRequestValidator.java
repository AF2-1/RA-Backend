package kr.co.tmax.rabackend.interfaces.strategy;

import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompleteStrategyRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SimulationDto.CompleteStrategyRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        return;
    }
}
