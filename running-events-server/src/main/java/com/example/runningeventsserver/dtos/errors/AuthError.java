package com.example.runningeventsserver.dtos.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthError {
    private String loginInvalid;
    private String passwordInvalid;
}
