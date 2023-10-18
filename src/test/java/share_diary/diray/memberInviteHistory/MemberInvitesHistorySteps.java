package share_diary.diray.memberInviteHistory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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

    public static MemberInviteRequest 일기방초대요청_생성(){
        final Long diaryRoomId = 1L;
        final List<String> emails = List.of("boyoung2@gmail.com", "somin2@gmail.com");
        final Long hostId = 1L;
        return MemberInviteRequest.of(diaryRoomId,emails, hostId);
    }
}
