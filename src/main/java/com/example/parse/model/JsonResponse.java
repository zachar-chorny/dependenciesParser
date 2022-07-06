package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class JsonResponse {
    private final String message;
    private final int httpStatus;
}
