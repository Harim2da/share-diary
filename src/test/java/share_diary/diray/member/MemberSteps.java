package share_diary.diray.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

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
}
