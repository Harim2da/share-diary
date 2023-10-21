package share_diary.diray.memberInviteHistory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequest;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;

import java.util.List;

public class MemberInvitesHistorySteps {

    private static final String URL = "/api/v0/member-invite-histories";

    public static ExtractableResponse<Response> 일기방초대요청(String token,MemberInviteRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .post(URL)
                .then()
                .log().all().extract();
    }

    public static MemberInviteRequest 일기방초대요청_생성(List<String> emails){
        final Long diaryRoomId = 1L;
        return MemberInviteRequest.of(diaryRoomId,emails,null);
    }

    public static ExtractableResponse<Response> 일기방초대수락요청(String token, Long inviteId,InviteUpdateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(HttpHeaders.AUTHORIZATION, token)
                .body(request)
                .when()
                .patch(URL+"/{historyId}",inviteId)
                .then()
                .log().all().extract();
    }

    public static InviteUpdateRequest 일기방초대수락요청_생성() {
        return new InviteUpdateRequest(InviteAcceptStatus.ACCEPT);
    }

    public static ExtractableResponse<Response> 일기방초대_알림내역조회_요청(String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get(URL)
                .then()
                .log().all().extract();
    }
}
