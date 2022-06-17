package com.example.parse.controller;

import com.example.parse.exception.WrongParamsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({WrongParamsException.class})
    public ResponseEntity<Object> handle(WrongParamsException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(new JsonResponse(exception.getMessage(),
                httpStatus.value()));
    }

    private class JsonResponse {
        private final String message;
        private final int httpStatus ;

        public JsonResponse(String message, int httpStatus ) {
            super();
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public String getMessage() {
            return message;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

    }

}
