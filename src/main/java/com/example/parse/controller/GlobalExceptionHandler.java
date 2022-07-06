package com.example.parse.controller;

import com.example.parse.exception.WrongParamsException;
import com.example.parse.model.JsonResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<JsonResponse> responses = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(s -> new JsonResponse(new DefaultMessageSourceResolvable(s).getDefaultMessage(),
                        status.value()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responses, headers, status);
    }

    @ExceptionHandler({WrongParamsException.class})
    public ResponseEntity<Object> handle(WrongParamsException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(new JsonResponse(exception.getMessage(),
                httpStatus.value()));
    }

}
