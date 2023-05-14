package share_diary.diray.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.exception.http.UnAuthorizedException;
import share_diary.diray.exception.jwt.AccessTokenRenewException;
import share_diary.diray.exception.jwt.TokenIsNotValidException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {
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
        }catch (UnAuthorizedException e){
            throw new UnAuthorizedException();
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
