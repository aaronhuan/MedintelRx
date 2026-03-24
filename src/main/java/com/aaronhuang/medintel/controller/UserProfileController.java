package com.aaronhuang.medintel.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;


import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.service.UserProfileService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public UserProfile getUserProfile(Authentication auth) {
        String userId = auth.getName();
        return userProfileService.getById(UUID.fromString(userId));
    }
    
}
