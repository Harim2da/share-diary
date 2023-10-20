package share_diary.diray.dailyDiary;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;
import share_diary.diray.diaryRoom.DiaryRoomSteps;
import share_diary.diray.member.MemberSteps;

import java.awt.*;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DailyDiaryApiTest extends ApiTest {

    private static final String URL = "/api/v0/daily-diaries";

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        //회원가입
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        //로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2", "1234"));
    }

    @Test
    @DisplayName("일기 작성 테스트")
    void writeDailyDiaryTest(){
        //given
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
            //일기방 생성
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of()));

        DailyDiaryCreateModifyRequest request = new DailyDiaryCreateModifyRequest("나는 오늘 일기를 씁니다.", MyEmoji.FUN, List.of(1L), DiaryStatus.SHOW);

        //expected
        var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL)
                .then()
                .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
