package com.example.runningeventsserver.dtos.contracts.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDto implements Serializable {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email")
    @Size(max = 50, message = "Email cannot be longer than 50 characters")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long")
    private String password;
}
