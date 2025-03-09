package com.fiuni.adoptamena.auth;

import com.fiuni.adoptamena.api.dto.user.UserProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String token;
    UserProfileDTO user;
}
