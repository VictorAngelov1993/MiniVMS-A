package com.minivms.paymentservice.controller;

import com.minivms.paymentservice.model.dto.security.TokenRequestDto;
import com.minivms.paymentservice.model.dto.security.TokenResponseDto;
import com.minivms.paymentservice.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final String expectedClientId;
    private final String expectedClientSecret;

    public AuthController(JwtService jwtService,
                          @Value("${app.jwt.client-id}") String expectedClientId,
                          @Value("${app.jwt.client-secret}") String expectedClientSecret) {
        this.jwtService = jwtService;
        this.expectedClientId = expectedClientId;
        this.expectedClientSecret = expectedClientSecret;
    }
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> issueToken(@RequestBody TokenRequestDto request) {
        if(!expectedClientId.equals(request.getClientId()) || !expectedClientSecret.equals(request.getClientSecret())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> roles = (request.getRoles() == null || request.getRoles().isEmpty() ? List.of("BUYER") : request.getRoles());

        String token = jwtService.generateToken(request.getClientId(), roles);
        return ResponseEntity.ok(new TokenResponseDto(token, "Bearer"));
    }

}
