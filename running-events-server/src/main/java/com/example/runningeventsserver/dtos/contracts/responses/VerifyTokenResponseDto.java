package com.example.runningeventsserver.dtos.contracts.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyTokenResponseDto {
    private Boolean isValid;
}
