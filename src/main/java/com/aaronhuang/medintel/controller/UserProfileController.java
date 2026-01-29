package com.aaronhuang.medintel.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaronhuang.medintel.domain.model.UserProfile;
import com.aaronhuang.medintel.service.UserProfileService;


@RestController
@RequestMapping("/api/user")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{userId}")
    public UserProfile getUserProfile(@PathVariable UUID userId) {
        return userProfileService.getById(userId);
    }

    @PostMapping
    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.create(userProfile);
    }

    @GetMapping("/all")
    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileService.listAll();
    }
    
}
