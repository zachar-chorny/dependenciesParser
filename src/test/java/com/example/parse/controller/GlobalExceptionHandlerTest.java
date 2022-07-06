package com.example.parse.controller;

import com.example.parse.exception.WrongParamsException;
import com.example.parse.model.JsonResponse;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @DisplayName("Test case return ResponseEntity from MethodArgumentNotValidException.")
    @Test
    void handleMethodArgumentNotValid_shouldReturnCorrectResponse(){
        MethodArgumentNotValidException exception = createTestMethodArgumentNotValidException();
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest webRequest = null;
        List<JsonResponse> jsonResponses = List.of(new JsonResponse("Path can't be null", 400));
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(status).headers(headers).body(jsonResponses);
        ResponseEntity<Object> actualResponse = globalExceptionHandler.
                handleMethodArgumentNotValid(exception, headers, status, webRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @DisplayName("Test case return ResponseEntity from WrongParamsException.")
    @Test
    void handleWrongParamsException_shouldReturnCorrectResponse(){
        WrongParamsException exception = new WrongParamsException("File can't be null");
        JsonResponse jsonResponse = new JsonResponse("File can't be null", HttpStatus.BAD_REQUEST.value());
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        ResponseEntity<Object> actual = globalExceptionHandler.handle(exception);
        Assertions.assertEquals(expected, actual);
    }

    private MethodArgumentNotValidException createTestMethodArgumentNotValidException() {
        BindingResult bindingResult = new BeanPropertyBindingResult(null, "setting");
        ObjectError objectError = new ObjectError("setting", "Path can't be null");
        bindingResult.addError(objectError);
        return new MethodArgumentNotValidException(null, bindingResult);
    }

}