package share_diary.diray.memberInviteHistory;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.diaryRoom.DiaryRoomSteps;
import share_diary.diray.member.MemberSteps;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MemberInviteHistoryApiTest extends ApiTest {

    private static final String URL = "/api/v0/member-invite-histories";

    ExtractableResponse<Response> loginResponse = null;

    @BeforeEach
    void init(){
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2","jipdol2@gmail.com","1234","집돌2"));
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("somin2","somin2@gmail.com","1234","소민2"));
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2","1234"));
    }

    @Test
    @DisplayName("일기방 초대")
    void inviteDiaryRoomToMember(){
        //given
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of("jipsun2@gmail.com","jipal2@gmail.com")));
        MemberInviteRequest request = MemberInvitesHistorySteps.일기방초대요청_생성(List.of("boyoung2@gmail.com", "somin2@gmail.com"));

        //expected
        final var response = MemberInvitesHistorySteps.일기방초대요청(token,request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("일기방 초대 수락")
    void inviteDiaryRoomAccept(){
        //given
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of()));
        MemberInvitesHistorySteps.일기방초대요청(token,MemberInvitesHistorySteps.일기방초대요청_생성(List.of("boyoung2@gmail.com", "somin2@gmail.com")));

            //somin2 로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("somin2","1234"));
        token = loginResponse
                .body().jsonPath().getString("accessToken");

        //expected
        var response = MemberInvitesHistorySteps.일기방초대_알림내역조회_요청(token);
            //초대 id 조회
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long inviteId = response.body().jsonPath().getLong("data[0].id");

        MemberInvitesHistorySteps.일기방초대수락요청(token, inviteId, MemberInvitesHistorySteps.일기방초대수락요청_생성());

        response = MemberInvitesHistorySteps.일기방초대_알림내역조회_요청(token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("data[0].uuid")).isNotNull();
        assertThat(response.jsonPath().getString("data[0].email")).isEqualTo("somin2@gmail.com");
        assertThat(response.jsonPath().getString("data[0].status")).isEqualTo(InviteAcceptStatus.ACCEPT.toString());
    }

    @Test
    @DisplayName("알림 내역 조회 API TEST - empty")
    void findByInviteHistoryEmptyTest() throws Exception {
        //given
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of("jipsun2@gmail.com","jipal2@gmail.com")));

        //api 요청
        final var response = MemberInvitesHistorySteps.일기방초대_알림내역조회_요청(token);
        //then
        assertThat(response.jsonPath().getList("data")).isEmpty();
    }

    @Test
    @DisplayName("알림 내역 조회 API TEST")
    void findByInviteHistoryTest() throws Exception {
        //given
            //(jipdol2)로그인
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
        String refreshToken = loginResponse
                .cookie("REFRESH_TOKEN");

            //일기방생성
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성(List.of()));
            //일기방 초대
        MemberInvitesHistorySteps.일기방초대요청(token,MemberInvitesHistorySteps.일기방초대요청_생성(List.of("somin2@gmail.com")));
            //(jipdol2)로그아웃
        AuthSteps.로그아웃요청(token,refreshToken);

            //(somin)로그인
        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("somin2","1234"));
        token = loginResponse
                .body().jsonPath().getString("accessToken");

        //when
        final var response = MemberInvitesHistorySteps.일기방초대_알림내역조회_요청(token);

        //then
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertThat(response.jsonPath().getString("data[0].uuid")).isNotNull();
        assertThat(response.jsonPath().getString("data[0].email")).isEqualTo("somin2@gmail.com");
        assertThat(response.jsonPath().getString("data[0].status")).isEqualTo(InviteAcceptStatus.INVITE.toString());
        assertThat(response.jsonPath().getLong("data[0].diaryRoomId")).isEqualTo(1L);
        assertThat(response.jsonPath().getString("data[0].diaryRoomName")).isEqualTo("오늘의 일기방");
        assertThat(response.jsonPath().getString("data[0].inviteDate")).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }



}