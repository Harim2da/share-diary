package share_diary.diray.emoji;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.dailyDiary.DailyDiarySteps;
import share_diary.diray.diaryRoom.DiaryRoomSteps;
import share_diary.diray.emoji.dto.request.DiaryEmojiRequest;
import share_diary.diray.member.MemberSteps;

import java.util.List;

public class EmojiApiTest extends ApiTest {

    private static final String URL = "/api/emoji";

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        //회원가입
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("somin2","somin2@gmail.com","1234","소민2"));
        //로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2","1234"));
    }

    @Test
    @DisplayName("(내 일기)이모지 저장 테스트")
    void saveMyEmojiTest(){
        final String token = loginResponse
                .body()
                .jsonPath()
                .getString("accessToken");

            //1. 일기방 생성
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of("somin2@gmail.com")));

            //2. 일기 생성
        DailyDiarySteps.일기생성요청(token,DailyDiarySteps.일기생성요청_생성());

        //expected
//        response = RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers(HttpHeaders.AUTHORIZATION, token)
//                .when()
//                .post(URL + "/{diaryId}", diaryId)
//                .then()
//                .log().all().extract();
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
