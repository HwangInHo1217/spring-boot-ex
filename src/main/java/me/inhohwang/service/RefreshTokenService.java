package me.inhohwang.service;

import lombok.RequiredArgsConstructor;
import me.inhohwang.repository.RefreshTokenRepository;
import me.inhohwang.springbootdeveloper.domain.RefreshToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return  refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new IllegalArgumentException("Unexpected refresh token"));
    }
}
