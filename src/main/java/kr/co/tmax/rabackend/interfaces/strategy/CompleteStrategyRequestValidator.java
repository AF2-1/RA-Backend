package kr.co.tmax.rabackend.interfaces.strategy;

import kr.co.tmax.rabackend.interfaces.simulation.SimulationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
