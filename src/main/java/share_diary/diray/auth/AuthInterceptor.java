package share_diary.diray.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.exception.http.UnAuthorizedException;
import share_diary.diray.exception.jwt.AccessTokenRenewException;
import share_diary.diray.exception.jwt.TokenExpiredException;
import share_diary.diray.exception.jwt.TokenIsNotValidException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //만약 oauth 로그인 후 redirect 된 uri 라면 그냥 true
        //interceptor 에 대해 학습 더 필요

        if(CorsUtils.isPreFlightRequest(request)){
            log.info("-----Preflight Request Occur-----");
            return true;
        }

        if(handler instanceof HandlerMethod) {
            log.info("-----No Auth Request-----");
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
            if(noAuth != null){
                return true;
            }
        }

        String accessToken = request.getHeader("Authorization");

        if(isValidToken(accessToken)){
            return true;
        }

        String refreshToken = extractRefreshToken(request);
        if(refreshToken==null){
            throw new TokenIsNotValidException();
        }

        authService.validateToken(refreshToken);
        throw new AccessTokenRenewException();
    }

    private boolean isValidToken(String token){
        try{
            authService.validateToken(token);
            return true;
        }catch (TokenExpiredException e){
            throw new TokenExpiredException();
        }catch (TokenIsNotValidException e){
            throw new TokenIsNotValidException();
        }
    }

    private String extractRefreshToken(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "REFRESH_TOKEN");
        if(cookie==null){
            return null;
        }
        return cookie.getValue();
    }
}
