package share_diary.diray.apiTest.emoji;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.emoji.controller.request.DiaryEmojiRequestDTO;

public class EmojiSteps {

    private static final String URL = "/api/emoji";

    public static ExtractableResponse<Response> 이모지클릭요청(String token, DiaryEmojiRequestDTO request) {
        final var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL + "/{diaryId}", 1L)
                .then()
                .log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 이미지조회요청(String token) {
        final var response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL+"/{diaryId}",1L)
                .then()
                .log().all().extract();
        return response;
    }
}
