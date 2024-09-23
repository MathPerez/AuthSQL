package com.AuthSQL.controller;

import com.AuthSQL.model.JwtResponse;
import com.AuthSQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AuthSQL.security.JwtTokenProvider;
import com.AuthSQL.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenJwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        if (userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            String token = tokenJwt.createToken(loginRequest.getUsername());
            String username = tokenJwt.getUsernameFromToken(token); 
            return ResponseEntity.ok(new JwtResponse(token, username));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User userDTO) {
        User user = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
