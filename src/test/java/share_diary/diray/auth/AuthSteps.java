package share_diary.diray.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.auth.dto.request.LoginRequestDTO;

public class AuthSteps {

    private final static String URL = "/api/auth";

    public static ExtractableResponse<Response> 회원로그인요청(LoginRequestDTO request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL + "/login")
                .then()
                .log().all().extract();
    }

    public static LoginRequestDTO 회원로그인요청_생성(String name){
        final String loginId = name;
        final String password = "1234";
        return LoginRequestDTO.of(loginId, password);
    }

    public static ExtractableResponse<Response> 로그아웃요청(String token, String refreshToken) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .cookie("REFRESH_TOKEN", refreshToken)
                .when()
                .post(URL + "/logout")
                .then()
                .log().all().extract();
    }
}
