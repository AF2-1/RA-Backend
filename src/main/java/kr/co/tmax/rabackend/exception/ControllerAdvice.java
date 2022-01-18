package kr.co.tmax.rabackend.exception;

import kr.co.tmax.rabackend.common.CommonResponse;
import kr.co.tmax.rabackend.interfaces.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public CommonResponse onException(Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail("시스템 에러", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public CommonResponse onBindException(BindException e, Locale locale) {
        log.error(e.getMessage());
        return CommonResponse.fail("잘못된 요청", ValidationResult.create(e, messageSource, locale));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public CommonResponse onBadRequestException(BadRequestException e) {
        log.error(e.getMessage());
        return CommonResponse.fail("잘못된 요청: " + e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public CommonResponse onResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        return CommonResponse.fail("잘못된 요청: " + e.getMessage(), null);
    }
}
