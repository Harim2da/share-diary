package share_diary.diray.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http.csrf(csrf->csrf.disable())
//                .authorizeHttpRequests(request->request
//                        .requestMatchers("/static/**").permitAll()
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/api/member/signUp").permitAll()
//                        .requestMatchers("/api/member/login").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .build();
//    }

//    @Bean("encoder")
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
