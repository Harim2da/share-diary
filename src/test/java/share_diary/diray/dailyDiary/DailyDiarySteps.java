package share_diary.diray.dailyDiary;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;

import java.util.List;

public class DailyDiarySteps {

    private static final String URL = "/api/v0/daily-diaries";

    public static ExtractableResponse<Response> 일기생성요청(String token, DailyDiaryCreateModifyRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL)
                .then()
                .log().all().extract();
    }

    public static DailyDiaryCreateModifyRequest 일기생성요청_생성() {
        //TODO : List.of(1L) => 추후에 diaryRoomId 로 개선
        return new DailyDiaryCreateModifyRequest("나는 오늘 일기를 씁니다.", MyEmoji.FUN, List.of(1L), DiaryStatus.SHOW);
    }
}
