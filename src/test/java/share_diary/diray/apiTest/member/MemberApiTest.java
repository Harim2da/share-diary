package share_diary.diray.apiTest.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import share_diary.diray.apiTest.ApiTest;
import share_diary.diray.apiTest.auth.AuthSteps;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;

class MemberApiTest extends ApiTest {

    private static final String URL = "/api/member";

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        //회원가입
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        //로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2","1234"));
    }

    @Test
    @DisplayName("회원가입 API")
    void signUpTest() throws Exception {
        //given
        final MemberSignUpRequestDTO member = MemberSteps.회원가입요청_생성("jipsun2","jipsun2@gmail.com","1234","집순2");

        //expect
        final var response = MemberSteps.회원가입요청(member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("아이디 찾기 API")
    void findMemberIdTest() throws Exception {
        //given
        String email = "jipdol2@gmail.com";

        //expect
        final var response = MemberSteps.아이디찾기요청(email);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("data.loginId")).isEqualTo("jipdol2");
        assertThat(response.jsonPath().getString("data.email")).isEqualTo("jipdol2@gmail.com");
        assertThat(response.jsonPath().getString("data.nickName")).isEqualTo("집돌2");
    }

    @Test
    @DisplayName("회원 수정 전 비밀번호 확인 API")
    void passwordCheckTest() throws Exception {
        //given
        final String token = loginResponse
                .body().jsonPath().getString("accessToken");

        //expected
        final var response = MemberSteps.비밀번호확인요청(token, MemberSteps.비밀번호확인요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("비밀번호 변경 -> 재로그인 API")
    void updateMemberPasswordTest(){
        //given
        final String token = loginResponse
                .body().jsonPath().getString("accessToken");
        final String refreshToken = loginResponse
                .cookie("REFRESH_TOKEN");

        //expected
        var response = MemberSteps.비밀번호변경요청(token, MemberSteps.비밀번호변경요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        AuthSteps.로그아웃요청(token,refreshToken);
            //변경 후 로그인
        response = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2", "4321"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("회원 수정 API")
    void updateMemberTest() throws Exception {
        //given
        final String token = loginResponse
                .body().jsonPath().getString("accessToken");

        //expected
        final var response = MemberSteps.회원수정요청(token, MemberSteps.회원수정요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("nickName")).isEqualTo("집순2");
    }

    @Test
    @DisplayName("아이디 중복 체크 API")
    void loginIdValidationCheckTest(){
        //expected
        final var response = MemberSteps.아이디중복체크요청(MemberSteps.아이디중복체크요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Boolean.class)).isEqualTo(true);
    }

    @Test
    @DisplayName("이메일 중복 체크 API")
    void emailValidationCheckTest(){
        //expected
        final var response = MemberSteps.이메일중복체크요청(MemberSteps.이메일중복체크요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Boolean.class)).isEqualTo(true);
    }

    @Test
    @DisplayName("마이페이지 조회 API")
    void myPageTest(){
        String token = loginResponse.body().jsonPath().getString("accessToken");

        final var response = RestAssured.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL + "/myPage")
                .then()
                .log().all().extract();

        assertThat(response.jsonPath().getString("loginId")).isEqualTo("jipdol2");
        assertThat(response.jsonPath().getString("email")).isEqualTo("jipdol2@gmail.com");
        assertThat(response.jsonPath().getString("nickName")).isEqualTo("집돌2");
        assertThat(response.jsonPath().getString("joinStatus")).isEqualTo(JoinStatus.USER.name());
    }

}