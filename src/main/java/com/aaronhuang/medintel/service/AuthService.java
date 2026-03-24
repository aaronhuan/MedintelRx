package com.aaronhuang.medintel.service;

import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.dto.AuthResponse;
import com.aaronhuang.medintel.dto.LoginRequest;
import com.aaronhuang.medintel.dto.RegisterRequest;
import com.aaronhuang.medintel.repository.UserProfileRepository;
import com.aaronhuang.medintel.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserProfileRepository userProfileRepository,
                       UserProfileService userProfileService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userProfileRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        UserProfile created = userProfileService.create(new UserProfile(
            request.email(),
            request.password(),
            request.timeZoneId(),
            request.phoneNumber(),
            request.dateOfBirth()
        ));

        return new AuthResponse(jwtService.generateToken(created.getId().toString()), "Bearer");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserProfile user = userProfileRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new AuthResponse(jwtService.generateToken(user.getId().toString()), "Bearer");
    }
}
