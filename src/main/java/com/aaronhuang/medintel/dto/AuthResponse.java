package com.aaronhuang.medintel.dto;

public record AuthResponse(
    String token,
    String tokenType
) {}
