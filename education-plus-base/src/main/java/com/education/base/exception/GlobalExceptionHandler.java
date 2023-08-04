package com.education.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @description: 全局异常统一处理
 * @author：wufengning
 * @date: 2023/8/2
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EducationPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(EducationPlusException e){
        log.error("系统异常啊：{}", e.getMessage());
        e.printStackTrace();
        return new RestErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(EducationPlusException e){
        log.error("系统异常：{}", e.getMessage());
        e.printStackTrace();
        if ("不允许访问".equals(e.getMessage()))
            return new RestErrorResponse("您没有权限操作此功能");
        return new RestErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer stringBuffer=new StringBuffer();
        fieldErrors.forEach(fieldError -> {
            stringBuffer.append(fieldError).append(",");
        });
        log.info(stringBuffer.toString());
        return new RestErrorResponse(stringBuffer.toString());
    }
}
