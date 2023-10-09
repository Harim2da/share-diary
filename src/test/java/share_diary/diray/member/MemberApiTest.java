package share_diary.diray.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.member.dto.request.MemberPasswordRequestDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.request.MemberUpdateRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MemberApiTest extends ApiTest {

    private static final String URL = "/api/member";

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        //given
        final MemberSignUpRequestDTO member = MemberSteps.회원가입요청_생성("jipdol2");
//        String json = objectMapper.writeValueAsString(member);

        //expect
        final var response = MemberSteps.회원가입요청(member);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findMemberIdTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));

        String email = "jipdol2@gmail.com";
        //expect

        final var response = RestAssured.given().log().all()
                .when()
                .param("email", email)
                .get(URL + "/me/id")
                .then()
                .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("loginId")).isEqualTo("jipdol2");
        assertThat(response.jsonPath().getString("email")).isEqualTo("jipdol2@gmail.com");
        assertThat(response.jsonPath().getString("nickName")).isEqualTo("jipdol2");
    }

    @Test
    @DisplayName("회원 수정 전 비밀번호 확인 - 성공")
    void passwordCheckTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        final String token = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"))
                .body().jsonPath().getString("accessToken");
        MemberPasswordRequestDTO request = MemberPasswordRequestDTO.from("1234");

        //expected
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION,token)
                .body(request)
                .when()
                .post(URL + "/me/pwd")
                .then()
                .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    //TODO : password change

    @Test
    @DisplayName("회원 수정 테스트")
    void updateMemberTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        final String token = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"))
                .body().jsonPath().getString("accessToken");

        MemberUpdateRequestDTO request = MemberUpdateRequestDTO.of(
                "jipdol2@gmail.com",
                "4321",
                "4321",
                "집순2"
        );

        //expected
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION,token)
                .body(request)
                .when()
                .patch(URL + "/me")
                .then()
                .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("nickName")).isEqualTo("집순2");
    }


    //TODO : loginId validation check

    //TODO : email validation check
}