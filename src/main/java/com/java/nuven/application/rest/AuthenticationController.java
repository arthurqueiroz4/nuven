package com.java.nuven.application.rest;

import com.java.nuven.application.dto.LoginDTO;
import com.java.nuven.application.dto.LoginResponseDTO;
import com.java.nuven.domain.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

     @PostMapping
     public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginDTO loginDTO) {
         return ResponseEntity.ok(authenticationService.authenticate(loginDTO));
     }
}
