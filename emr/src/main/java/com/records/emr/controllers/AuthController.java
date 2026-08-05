package com.records.emr.controllers;


import com.records.emr.dtos.auth.LoginRequest;
import com.records.emr.dtos.auth.LoginResponse;
import com.records.emr.security.JwtService;
import com.records.emr.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var authResult = authenticationManager.authenticate(authRequest);
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String token = jwtService.generateToken(principal);
        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .username(principal.getUsername())
                .role(principal.getRole())
                .build());
    }
}