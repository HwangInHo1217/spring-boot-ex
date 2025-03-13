package me.inhohwang.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.inhohwang.config.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.inhohwang.config.TokenAuthenticationFilter;
import me.inhohwang.config.jwt.TokenProvider;
import me.inhohwang.repository.RefreshTokenRepository;
import me.inhohwang.service.UserService;
import me.inhohwang.springbootdeveloper.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/articles";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        ///리프레시 토큰 생성->저장->쿠키에저장
        String refreshToken= tokenProvider.generateToken(user,REFRESH_TOKEN_DURATION);
    }
    private void saveRefreshToken(Long userId, String newRefreshToken){}

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){

    }
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removerAuthorizationRequestCookie(request, response);
    }
    private String getTargetUrl(String token){
        return null;
    }
}
