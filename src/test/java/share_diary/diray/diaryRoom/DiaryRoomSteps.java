package share_diary.diray.diaryRoom;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;

import java.util.List;

public class DiaryRoomSteps {

    private final static String URL = "/api/v0/diary-rooms";

    public static ExtractableResponse<Response> 일기방생성요청(String token,DiaryRoomCreateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL)
                .then()
                .log().all().extract();
    }

    public static DiaryRoomCreateRequest 일기방생성요청_생성(List<String> emails) {
        final String diaryRoomName = "오늘의 일기방";
        return new DiaryRoomCreateRequest(diaryRoomName,emails);
    }

    public static ExtractableResponse<Response> 참여하고있는_일기방목록조회_요청(String token) {
        final var response = RestAssured.given().log().all()
                .headers(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL)
                .then()
                .log().all().extract();
        return response;
    }
}
