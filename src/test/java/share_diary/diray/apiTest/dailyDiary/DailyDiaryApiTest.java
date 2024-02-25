package share_diary.diray.apiTest.dailyDiary;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import share_diary.diray.apiTest.ApiTest;
import share_diary.diray.apiTest.auth.AuthSteps;
import share_diary.diray.apiTest.diaryRoom.DiaryRoomSteps;
import share_diary.diray.apiTest.member.MemberSteps;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyDiaryApiTest extends ApiTest {

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        //회원가입
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        //로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2", "1234"));
    }

    @Test
    @DisplayName("일기 작성 API")
    void writeDailyDiaryTest(){
        //given
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
            //일기방 생성
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of()));

        //expected
        var response = DailyDiarySteps.일기생성요청(token, DailyDiarySteps.일기생성요청_생성());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
