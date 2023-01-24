package com.example.runningeventsserver.dtos.contracts.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long")
    private String currentPassword;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long")
    private String newPassword;
    @NotBlank(message = "Please confirm password")
    private String confirmNewPassword;
}
