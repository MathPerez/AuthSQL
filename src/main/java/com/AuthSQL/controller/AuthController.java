package com.AuthSQL.controller;

import com.AuthSQL.model.JwtResponse;
import com.AuthSQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AuthSQL.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        if (userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            JwtTokenProvider tokenJwt = new JwtTokenProvider();
            String token = tokenJwt.createToken(loginRequest.getUsername());
            String username = tokenJwt.getUsernameFromToken(token); 
            return ResponseEntity.ok(new JwtResponse(token, username));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
