package share_diary.diray.diaryRoom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;
import share_diary.diray.member.MemberSteps;

import static org.assertj.core.api.Assertions.assertThat;

public class DiaryRoomApiTest extends ApiTest {

    private final String URL = "/api/v0/diary-rooms";

    @Test
    @DisplayName("일기방 생성 테스트")
    void createDiaryRoomTest(){
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        final String token = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"))
                .body().jsonPath().getString("accessToken");
        DiaryRoomCreateRequest request = DiaryRoomSteps.일기방생성요청_생성();

        //expected
        final var response = DiaryRoomSteps.일기방생성요청(token,request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
