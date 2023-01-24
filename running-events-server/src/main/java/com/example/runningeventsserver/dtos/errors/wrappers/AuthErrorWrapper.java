package com.example.runningeventsserver.dtos.errors.wrappers;

import com.example.runningeventsserver.dtos.errors.AuthError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorWrapper {
    private AuthError authError;

    public AuthErrorWrapper(List<FieldError> fieldErrors) {
        this.authError = new AuthError();
        for (FieldError error : fieldErrors) {
            switch (error.getField()) {
                case "email":
                    authError.setLoginInvalid(error.getDefaultMessage());
                    break;
                case "password":
                    authError.setPasswordInvalid(error.getDefaultMessage());
                    break;
            }
        }
    }
}
