package com.example.runningeventsserver.services;

import com.example.runningeventsserver.dtos.contracts.requests.SignupRequestDto;
import com.example.runningeventsserver.models.User;
import com.example.runningeventsserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public void signup(SignupRequestDto signupRequestDto) {
        User newUser = new User();
        newUser.setEmail(signupRequestDto.getEmail());
        newUser.setPassword(bcryptEncoder.encode(signupRequestDto.getPassword()));
        userRepository.save(newUser);
    }

    public boolean isPasswordValid(String username, String password) {
        User user = getUser(username);
        return bcryptEncoder.matches(password, user.getPassword());
    }

    public void updatePassword(String email, String password) {
        String encrypted = bcryptEncoder.encode(password);
        User user = userRepository.findByEmail(email);
        user.setPassword(encrypted);
        userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public Boolean isUserAdmin(String username) {
        return userRepository.findByEmail(username).isAdmin();
    }

    public Boolean userExists(String username) {
        return userRepository.existsByEmail(username);
    }
}
