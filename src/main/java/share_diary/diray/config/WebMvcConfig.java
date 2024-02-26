package share_diary.diray.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.AuthInterceptor;
import share_diary.diray.auth.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final String []allowedOrigins = {
            "http://localhost:3000",
            "http://3.37.251.88:3000",
            "https://its-diary.vercel.app"
    };

    public WebMvcConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(authService))
                .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                )
                .allowedOrigins(allowedOrigins);
    }
}
