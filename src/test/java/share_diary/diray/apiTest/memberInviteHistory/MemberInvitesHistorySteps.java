package share_diary.diray.apiTest.memberInviteHistory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequestDTO;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.util.List;

public class MemberInvitesHistorySteps {

    private static final String URL = "/api/v0/member-invite-histories";

    public static ExtractableResponse<Response> 일기방초대요청(String token, MemberInviteRequestDTO request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL)
                .then()
                .log().all().extract();
    }

    public static MemberInviteRequestDTO 일기방초대요청_생성(List<String> emails){
        final Long diaryRoomId = 1L;
        return MemberInviteRequestDTO.of(diaryRoomId,emails,null);
    }

    public static ExtractableResponse<Response> 일기방초대수락요청(String token, Long inviteId, InviteUpdateRequestDTO request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .patch(URL+"/{historyId}",inviteId)
                .then()
                .log().all().extract();
    }

    public static InviteUpdateRequestDTO 일기방초대수락요청_생성() {
        return new InviteUpdateRequestDTO(InviteAcceptStatus.ACCEPT);
    }

    public static ExtractableResponse<Response> 일기방초대_알림내역조회_요청(String token) {
        return RestAssured.given()
                .queryParams("inviteHistoryId","")
                .queryParams("limit",5)
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL)
                .then()
                .log().all().extract();
    }
}
