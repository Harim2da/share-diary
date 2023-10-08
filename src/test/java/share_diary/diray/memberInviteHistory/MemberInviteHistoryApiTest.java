package share_diary.diray.memberInviteHistory;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import share_diary.diray.ApiTest;
import share_diary.diray.auth.AuthService;
import share_diary.diray.auth.AuthSteps;
import share_diary.diray.auth.dto.request.LoginRequestDTO;
import share_diary.diray.crypto.PasswordEncoder;
import share_diary.diray.diaryRoom.DiaryRoomRepository;
import share_diary.diray.diaryRoom.DiaryRoomSteps;
import share_diary.diray.member.MemberSteps;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepository;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistoryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class MemberInviteHistoryApiTest extends ApiTest {

    private static final String URL = "/api/v0/member-invite-histories";

    @Test
    @DisplayName("일기방 초대")
    void inviteDiaryRoomToMember(){
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        String token = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"))
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성());
        MemberInviteRequest request = MemberInvitesHistorySteps.일기방초대요청_생성();

        //expected
        final var response = MemberInvitesHistorySteps.일기방초대요청(token,request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("알림 내역 조회 API TEST - empty")
    void findByInviteHistoryEmptyTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        String token = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"))
                .body().jsonPath().getString("accessToken");
        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성());

        //api 요청
        final var response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL)
                .then()
                .log().all().extract();
        //then
        assertThat(response.jsonPath().getInt("size")).isEqualTo(0);
    }

    @Test
    @DisplayName("알림 내역 조회 API TEST")
    void findByInviteHistoryTest() throws Exception {
        //given
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("jipdol2"));
        MemberSteps.회원가입요청(MemberSteps.회원가입요청_생성("somin2"));

        var loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("jipdol2"));
        String token = loginResponse
                .body().jsonPath().getString("accessToken");
        String refreshToken = loginResponse
                .cookie("REFRESH_TOKEN");

        DiaryRoomSteps.일기방생성요청(token,DiaryRoomSteps.일기방생성요청_생성());

        MemberInvitesHistorySteps.일기방초대요청(token,MemberInvitesHistorySteps.일기방초대요청_생성());

        AuthSteps.로그아웃요청(token,refreshToken);

        loginResponse = AuthSteps.회원로그인요청(AuthSteps.회원로그인요청_생성("somin2"));
        token = loginResponse
                .body().jsonPath().getString("accessToken");

        //expected
        final var response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION,token)
                .when()
                .get(URL)
                .then()
                .log().all().extract();
        //then
//        assertThat(response.jsonPath().getInt("result[0].id")).isEqualTo(1);
        assertThat(response.jsonPath().getString("result[0].uuid")).isNotNull();
        assertThat(response.jsonPath().getString("result[0].email")).isEqualTo("somin2@gmail.com");
        assertThat(response.jsonPath().getString("result[0].status")).isEqualTo(InviteAcceptStatus.INVITE.toString());
        assertThat(response.jsonPath().getString("result[0].createDate")).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(response.jsonPath().getInt("size")).isEqualTo(1);
    }

}