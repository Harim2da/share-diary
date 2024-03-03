package share_diary.diray.apiTest.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.member.controller.request.*;

public class MemberSteps {

    private final static String URL = "/api/member";

    public static ExtractableResponse<Response> 회원가입요청(MemberSignUpRequestDTO json) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when()
                .post(URL + "/signUp")
                .then().log().all().extract();
    }

    public static MemberSignUpRequestDTO 회원가입요청_생성(String loginId,String email,String password,String nickName) {
        return MemberSignUpRequestDTO.of(loginId, email, password, nickName);
    }

    public static ExtractableResponse<Response> 아이디찾기요청(String email) {
        final var response = RestAssured.given().log().all()
                .when()
                .param("email", email)
                .get(URL + "/me/id")
                .then()
                .log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 비밀번호확인요청(String token, MemberPasswordRequestDTO request) {
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL + "/me/pwd")
                .then()
                .log().all().extract();
        return response;
    }

    public static MemberPasswordRequestDTO 비밀번호확인요청_생성() {
        MemberPasswordRequestDTO request = MemberPasswordRequestDTO.from("1234");
        return request;
    }

    public static ExtractableResponse<Response> 비밀번호변경요청(String token, MemberPasswordUpdateDTO request) {
        var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL + "/pwd")
                .then()
                .log().all().extract();
        return response;
    }

    public static MemberPasswordUpdateDTO 비밀번호변경요청_생성() {
        MemberPasswordUpdateDTO request = new MemberPasswordUpdateDTO("1234","4321");
        return request;
    }

    public static ExtractableResponse<Response> 회원수정요청(String token, MemberUpdateRequestDTO request) {
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .patch(URL + "/me")
                .then()
                .log().all().extract();
        return response;
    }

    public static MemberUpdateRequestDTO 회원수정요청_생성() {
        MemberUpdateRequestDTO request = MemberUpdateRequestDTO.of(
                "jipdol2@gmail.com",
                "1234",
                "4321",
                "집순2"
        );
        return request;
    }


    public static ExtractableResponse<Response> 아이디중복체크요청(MemberLoginIdRequestDTO request) {
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL + "/loginId/validation")
                .then()
                .log().all().extract();
        return response;
    }

    public static MemberLoginIdRequestDTO 아이디중복체크요청_생성() {
        MemberLoginIdRequestDTO request = new MemberLoginIdRequestDTO("jipdol2");
        return request;
    }

    public static ExtractableResponse<Response> 이메일중복체크요청(MemberEmailRequestDTO request) {
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL+"/email/validation")
                .then()
                .log().all().extract();
        return response;
    }

    public static MemberEmailRequestDTO 이메일중복체크요청_생성() {
        MemberEmailRequestDTO request = new MemberEmailRequestDTO("jipdol2@gmail.com");
        return request;
    }
}
