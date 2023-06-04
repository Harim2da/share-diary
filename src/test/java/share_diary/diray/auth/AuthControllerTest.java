package share_diary.diray.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import share_diary.diray.member.domain.Member;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@MockBean({JpaMetamodelMappingContext.class, WebMvcConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    private static final String URL = "/api/auth";

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception{
        //given
//        LoginRequestDTO loginDto = LoginRequestDTO.from("jipdol2", "1234");
//        String json = objectMapper.writeValueAsString(loginDto);

//        String accessToken = authService.makeAccessToken(loginRequestDTO);
//        Long id = authService.extractIdByToken(accessToken);

//        String accessToken = "5hk2jh5l32i525h2h52h52h";
//
//        when(authService.makeAccessToken(any())).thenReturn(accessToken);
//        when(authService.extractIdByToken(accessToken)).thenReturn()
//        //expected
//        mockMvc.perform(post(URL+"/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//        ).andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value())
//                .andDo(print());
    }

    private Member createMember(
            String memberId,
            String email,
            String password,
            String nickName
    ) {
        return Member.builder()
                .memberId(memberId)
                .email(email)
                .password(password)
                .nickName(nickName)
                .build();
    }



}