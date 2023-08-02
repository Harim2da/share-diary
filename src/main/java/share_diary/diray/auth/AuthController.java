package share_diary.diray.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.token.AccessToken;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.auth.dto.request.LoginRequestDTO;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

//    RefreshToken 에 관하여 : https://medium.com/@uk960214/refresh-token-%EB%8F%84%EC%9E%85%EA%B8%B0-f12-dd79de9fb0f0

    /**
     * 로그인(일반)
     */
/*    @NoAuth
    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody LoginRequestDTO requestDTO){
//        log.info("requestDTO = {}", requestDTO.toString());
        String accessToken = authService.makeAccessToken(requestDTO);
        Long id = authService.extractIdByToken(accessToken);
        ResponseCookie cookie = makeRefreshTokenCookie(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(AccessToken.of(accessToken));
    }*/

    /**
     * 로그인(소셜) - review
     */
    @NoAuth
    @PostMapping(value = {"/login","/login/{provider}"})
    public ResponseEntity<AccessToken> loginSocial(@PathVariable String provider,@RequestBody LoginRequestDTO requestDTO){
//        log.info("requestDTO = {}", requestDTO.toString());
        String accessToken = authService.makeAccessToken(provider,requestDTO);
        Long id = authService.extractIdByToken(accessToken);
        ResponseCookie cookie = makeRefreshTokenCookie(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(AccessToken.of(accessToken));
    }

    /**
     * 로그아웃
     */
    //    @NoAuth
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@AuthenticationPrincipal LoginSession loginSession, @CookieValue("refreshToken") String refreshToken){
//        authService.removeRefreshToken(refreshToken);
        authService.removeRefreshTokenToDB(refreshToken);
        ResponseCookie cookie = expiredResponseCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(null);
    }

    private ResponseCookie makeRefreshTokenCookie(Long id){
//        String refreshToken = authService.makeRefreshToken(id);
        String refreshToken = authService.makeRefreshTokenToDB(id);
        return ResponseCookie.from("REFRESH_TOKEN",refreshToken)
                .path("/")
                .domain("localhost")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .build();
    }

    private ResponseCookie expiredResponseCookie() {
        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", "")
                .path("/")
                .domain("localhost")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(0))
                .build();
        return cookie;
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

    /**
     * TODO: 추후 삭제 필요
     * Redis 저장 확인
     */
    @NoAuth
    @GetMapping("/GetRedis")
    public ResponseEntity<?> getRefreshToken(@RequestParam String token){
        authService.getRefreshToken(token);
        return ResponseEntity.ok()
                .body(null);
    }
}
