package me.inhohwang.service;

import lombok.RequiredArgsConstructor;
import me.inhohwang.config.jwt.TokenProvider;
import me.inhohwang.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //유효성 검사 시 실패하면 예외
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        Long userId=refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user=userService.findById(userId);
        return tokenProvider.generateToken(user, Duration.ofHours(2));

    }
}
