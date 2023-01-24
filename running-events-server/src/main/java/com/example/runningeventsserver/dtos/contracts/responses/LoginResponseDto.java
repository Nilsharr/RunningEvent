package com.example.runningeventsserver.dtos.contracts.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class LoginResponseDto implements Serializable {
    private String userId;
    private String username;
    private String token;
    private boolean admin;
}
