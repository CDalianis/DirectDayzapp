package com.digitaldetox.api;

import com.digitaldetox.authentication.AuthenticationService;
import com.digitaldetox.dto.auth.AuthenticationRequestDTO;
import com.digitaldetox.dto.auth.AuthenticationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@Valid @RequestBody AuthenticationRequestDTO dto) {
        return ResponseEntity.ok(authenticationService.authenticate(dto));
    }
}
