package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AccessToken;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.auth.dto.request.LoginRequestDTO;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

//    https://medium.com/@uk960214/refresh-token-%EB%8F%84%EC%9E%85%EA%B8%B0-f12-dd79de9fb0f0

    /**
     * 로그인
     */
    @NoAuth
    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody LoginRequestDTO loginRequestDTO){

        log.info("signInDto = {}", loginRequestDTO.toString());

        String accessToken = authService.makeAccessToken(loginRequestDTO);
        Long id = authService.extractIdByToken(accessToken);
        ResponseCookie cookie = makeRefreshTokenCookie(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(AccessToken.of(accessToken));
    }

    private ResponseCookie makeRefreshTokenCookie(Long id){
        String refreshToken = authService.makeRefreshToken(id);

        return ResponseCookie.from("REFRESH_TOKEN",refreshToken)
                .path("/")
                .domain("localhost")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .build();

    }

    /**
     * accessToken 갱신
     */
    @NoAuth
    @GetMapping("/token")
    public ResponseEntity<AccessToken> renewAccessToken(@CookieValue("refreshToken") String refreshToken){

        authService.validateToken(refreshToken);
        Long id = authService.extractIdByToken(refreshToken);
        String accessToken = authService.renewAccessToken(id);
        return ResponseEntity.ok()
                .body(AccessToken.of(accessToken));
    }

    //TODO: 로그아웃 API
}
