package com.aaronhuang.medintel.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record RegisterRequest(
    @NotBlank @Email String email,
    @NotBlank String password,
    @JsonAlias("timezone") String timeZoneId,
    String phoneNumber,
    LocalDate dateOfBirth
) {}
