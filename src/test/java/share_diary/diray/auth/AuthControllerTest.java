package share_diary.diray.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import share_diary.diray.auth.dto.request.LoginRequestDTO;
import share_diary.diray.config.WebMvcConfig;
import share_diary.diray.jwt.JwtManager;
import share_diary.diray.member.domain.Member;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@MockBean({JpaMetamodelMappingContext.class})
class AuthControllerTest {

    @Value("${jwt.secret}")
    private String KEY;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtManager jwtManager;

    private static final String URL = "/api/auth";

    private static final int ACCESS_TIME = 60*1*1000;  //1분
    private static final int REFRESH_TIME = 60*2*1000; //2분

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception{
        //given
        LoginRequestDTO loginDto = LoginRequestDTO.from("jipdol2", "1234");
        String json = objectMapper.writeValueAsString(loginDto);

        String accessToken = makeToken(1L,ACCESS_TIME);
        Long id = getClaims(accessToken).get("id", Long.class);
        String refreshToken = makeToken(id,REFRESH_TIME);

        when(authService.makeAccessToken(any())).thenReturn(accessToken);
        when(authService.makeRefreshTokenToDB(any())).thenReturn(refreshToken);
//        when(authService.makeRefreshToken(any())).thenReturn(refreshToken);

        //expected
        mockMvc.perform(post(URL+"/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(MockMvcResultMatchers.cookie().value("REFRESH_TOKEN",refreshToken))
                .andDo(print());
    }

    public String makeToken(Long id,int tokenTime){
        return Jwts.builder()
                .claim("id",id)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenTime))
                .compact();
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
    }

    public Claims getClaims(String token){

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }

    private Member createMember(
            String loginId,
            String email,
            String password,
            String nickName
    ) {
        return Member.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }
}