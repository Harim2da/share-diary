package share_diary.diray.apiTest.diaryRoom;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import share_diary.diray.apiTest.ApiTest;
import share_diary.diray.apiTest.auth.AuthSteps;
import share_diary.diray.apiTest.member.MemberSteps;
import share_diary.diray.diaryRoom.DiaryRoomStatus;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DiaryRoomApiTest extends ApiTest {

    private final String URL = "/api/v0/diary-rooms";

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2","1234"));
    }

    @Test
    @DisplayName("일기방 생성 API")
    void createDiaryRoomTest(){
        //given
        final String token = loginResponse
                .body().jsonPath().getString("accessToken");
        DiaryRoomCreateRequest request = DiaryRoomSteps.일기방생성요청_생성(List.of("jipsun2@gmail.com","jipal2@gmail.com"));

        //expected
        final var response = DiaryRoomSteps.일기방생성요청(token,request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("참여하고 있는 일기방 목록 조회 API")
    void findByDiaryRoomsAll(){
        //given
        final String token = loginResponse
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token, DiaryRoomSteps.일기방생성요청_생성(List.of()));

        //expected
        final var response = DiaryRoomSteps.참여하고있는_일기방목록조회_요청(token);

        assertThat(response.body().jsonPath().getLong("id[0]")).isNotNull();
        assertThat(response.body().jsonPath().getString("name[0]")).isEqualTo("오늘의 일기방");
        assertThat(response.body().jsonPath().getString("status[0]")).isEqualTo(DiaryRoomStatus.OPEN.name());
        assertThat(response.body().jsonPath().getString("createBy[0]")).isEqualTo("jipdol2");
    }

}
