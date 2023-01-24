package com.example.runningeventsserver.controllers;

import com.example.runningeventsserver.config.JwtTokenUtil;
import com.example.runningeventsserver.dtos.EventDto;
import com.example.runningeventsserver.dtos.contracts.requests.ChangePasswordRequestDto;
import com.example.runningeventsserver.dtos.contracts.requests.LoginRequestDto;
import com.example.runningeventsserver.dtos.contracts.requests.VerifyTokenRequestDto;
import com.example.runningeventsserver.dtos.contracts.responses.LoginResponseDto;
import com.example.runningeventsserver.dtos.contracts.requests.SignupRequestDto;
import com.example.runningeventsserver.dtos.contracts.responses.VerifyTokenResponseDto;
import com.example.runningeventsserver.dtos.errors.wrappers.AuthErrorWrapper;
import com.example.runningeventsserver.dtos.errors.Error;
import com.example.runningeventsserver.models.User;
import com.example.runningeventsserver.services.EventService;
import com.example.runningeventsserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult result) {
        if (userService.userExists(signupRequestDto.getEmail())) {
            result.addError(new FieldError("signupRequestDto", "email", "Given email is taken"));
        }
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())) {
            result.addError(new FieldError("signupRequestDto", "password", "Passwords do not match"));
        }

        if (result.hasErrors()) {
            AuthErrorWrapper error = new AuthErrorWrapper(result.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        userService.signup(signupRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            AuthErrorWrapper error = new AuthErrorWrapper(result.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new Error("Invalid login credentials"), HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userService.loadUserByUsername(loginRequestDto.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        User user = userService.getUser(userDetails.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(user.getId(), userDetails.getUsername(), token, user.isAdmin()));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto, BindingResult result) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userService.isPasswordValid(username, changePasswordRequestDto.getCurrentPassword())) {
            return new ResponseEntity<>(new Error("Provided current password is invalid"), HttpStatus.UNAUTHORIZED);
        }
        if (!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmNewPassword())) {
            result.addError(new FieldError("changePasswordRequestDto", "password", "Passwords do not match"));
        }
        if (result.hasErrors()) {
            AuthErrorWrapper error = new AuthErrorWrapper(result.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        userService.updatePassword(username, changePasswordRequestDto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@Valid @RequestBody VerifyTokenRequestDto verifyTokenRequestDto) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(verifyTokenRequestDto.getToken());
            UserDetails userDetails = userService.loadUserByUsername(username);
            jwtTokenUtil.validateToken(verifyTokenRequestDto.getToken(), userDetails);
        } catch (Exception ex) {
            return ResponseEntity.ok(new VerifyTokenResponseDto(false));
        }
        return ResponseEntity.ok(new VerifyTokenResponseDto(true));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getUserEvents() {
        User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        List<EventDto> events = eventService.getUserEvents(user.getId());
        return ResponseEntity.ok(events);
    }
}
