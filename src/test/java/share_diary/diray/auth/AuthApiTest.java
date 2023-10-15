package share_diary.diray.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.dto.request.LoginRequestDTO;
import share_diary.diray.member.MemberSteps;

import static org.assertj.core.api.Assertions.*;

class AuthApiTest extends ApiTest {

    private static final String URL = "/api/auth";

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        LoginRequestDTO request = AuthSteps.회원로그인요청_생성("jipdol2","1234");

        //expected
        final var response = AuthSteps.회원로그인요청(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotNull();
        assertThat(response.cookie("REFRESH_TOKEN")).isNotNull();
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest(){
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        final var loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2","1234"));
        String token = loginResponse.body().jsonPath().getString("accessToken");
        String refreshToken = loginResponse.cookie("REFRESH_TOKEN");

        final var response = AuthSteps.로그아웃요청(token, refreshToken);
        //expected
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie("REFRESH_TOKEN")).isNotNull();
    }

}